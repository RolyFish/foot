本文学习，PriorityBlockingQueue，是一个阻塞的二叉堆。主要对锁机制的理解，和对cas操作的理解。



### 简介

​	PriorityBlockingQueue是线程安全的PriorityQueue，其实现原理与PriorityQueue类似，在此基础上实现了BlockingQueue接口，能够作为阻塞队列使用。

其只有一个等待队列notEmpty，用来管理当队列为空的时候进行出队操作的线程加入等待队列。对于队列满了的情况，使用一个原子的int类型`allocationSpinLock`配合cas来控制。



### 属性

```java
private static final int DEFAULT_INITIAL_CAPACITY = 11;
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
//queue存放元素数组，不参与序列化。
private transient Object[] queue;
//队列元素个数
private transient int size;
//comparator。可自定义，实现排序
private transient Comparator<? super E> comparator;
//锁
private final ReentrantLock lock;
//等待队列
private final Condition notEmpty;
//配合cas操作可实现，扩容操作原子性
private transient volatile int allocationSpinLock;
//参与序列化，与反序列化
private PriorityQueue<E> q;
//UNSAFE
private static final sun.misc.Unsafe UNSAFE;
//偏移量，可看做allocationSpinLock的内存地址。通过他可以快速访问对象
private static final long allocationSpinLockOffset;
```

> 看一下这个静态代码块。

- 被static final修饰的变量，必须在静态代码块初始化。
- 反射：获取allocationSpinLock的offset：allocationSpinLockOffset。类似于内存地址。

```java
static {
    try {
        UNSAFE = sun.misc.Unsafe.getUnsafe();
        Class<?> k = PriorityBlockingQueue.class;
        allocationSpinLockOffset = UNSAFE.objectFieldOffset
            (k.getDeclaredField("allocationSpinLock"));
    } catch (Exception e) {
        throw new Error(e);
    }
}
```

> 关于序列化

`PriorityBlockingQueue`会将队列元素和比较器放入`PriorityQueue`进行传输。



<hr>



### 动态扩容

> 之所以说是动态扩容，是因为PriorityBlockingQueue对于扩容方法不会阻塞，而是使用cas自旋的方式，通过allocationSpinLock这个值来控制线程安全。

- 既然不会阻塞了，那效率就高一些

PriorityBlockingQueue扩容时，因为增加堆数组的长度并不影响队列中元素的出队操作，因而使用自旋CAS操作来控制扩容操作，仅在数组引用替换和拷贝元素时才加锁，从而减少了扩容对出队操作的影响。

```java
private void tryGrow(Object[] array, int oldCap) {
    //在执行扩容操作释放锁资源
    lock.unlock(); // must release and then re-acquire main lock
    Object[] newArray = null;
    //当队列处于可扩容状态、且cas成功时才进行扩容操作
    if (allocationSpinLock == 0 &&
        UNSAFE.compareAndSwapInt(this, allocationSpinLockOffset,
                                 0, 1)) {
        try {
            int newCap = oldCap + ((oldCap < 64) ?
                                   (oldCap + 2) : // grow faster if small
                                   (oldCap >> 1));
            if (newCap - MAX_ARRAY_SIZE > 0) {    // possible overflow
                int minCap = oldCap + 1;
                if (minCap < 0 || minCap > MAX_ARRAY_SIZE)
                    throw new OutOfMemoryError();
                newCap = MAX_ARRAY_SIZE;
            }
            if (newCap > oldCap && queue == array)
                newArray = new Object[newCap];
        } finally {
            //扩容操作完成，修改队列处于可扩容状态
            allocationSpinLock = 0;
        }
    }
    //当newArray为null，即队列处于不可扩容状态或cas失败。进行线程礼让
    if (newArray == null) // back off if another thread is allocating
        Thread.yield();
    lock.lock();
    if (newArray != null && queue == array) {
        queue = newArray;
        System.arraycopy(array, 0, newArray, 0, oldCap);
    }
}
```

- 线程礼让

  执行礼让的线程，会释放锁资源，重新竞争锁资源

  并且只会被同等级的线程获取锁资源

  并不意味着执行礼让的线程不会重新获取锁

这个操作，尽可能的避免了后续获取锁操作，线程竞争资源的情况。



<hr>



### 构造器

```java
//队列长度默认11。且只可以添加可比较的对象
public PriorityBlockingQueue() {
    this(DEFAULT_INITIAL_CAPACITY, null);
}
//自定义队列长度
public PriorityBlockingQueue(int initialCapacity) {
    this(initialCapacity, null);
}
//自定义队列长度和比较器。 可添加非可比对象
public PriorityBlockingQueue(int initialCapacity,
                             Comparator<? super E> comparator) {
    if (initialCapacity < 1)
        throw new IllegalArgumentException();
    this.lock = new ReentrantLock();
    this.notEmpty = lock.newCondition();
    this.comparator = comparator;
    this.queue = new Object[initialCapacity];
}
```



> 我们可以将任何一个集合初始化为一个priorityQueue。

```java
-public PriorityBlockingQueue(Collection<? extends E> c) {
    this.lock = new ReentrantLock();
    this.notEmpty = lock.newCondition();
    //是否需要堆放
    boolean heapify = true; // true if not known to be in heap order
    //是否需要扫描空元素
    boolean screen = true;  // true if must screen for nulls
    if (c instanceof SortedSet<?>) {
        SortedSet<? extends E> ss = (SortedSet<? extends E>) c;
        this.comparator = (Comparator<? super E>) ss.comparator();
        heapify = false;
    }
    else if (c instanceof PriorityBlockingQueue<?>) {
        PriorityBlockingQueue<? extends E> pq =
            (PriorityBlockingQueue<? extends E>) c;
        this.comparator = (Comparator<? super E>) pq.comparator();
        screen = false;
        if (pq.getClass() == PriorityBlockingQueue.class) // exact match
            heapify = false;
    }
    Object[] a = c.toArray();
    int n = a.length;
    // If c.toArray incorrectly doesn't return Object[], copy it.
    if (a.getClass() != Object[].class)
        a = Arrays.copyOf(a, n, Object[].class);
    if (screen && (n == 1 || this.comparator != null)) {
        for (int i = 0; i < n; ++i)
            if (a[i] == null)
                throw new NullPointerException();
    }
    this.queue = a;
    this.size = n;
    if (heapify)
        heapify();
}
```



<hr>

### 入队操作

> add()、put()、offer()。主要看一下offer方法。

```java
public boolean offer(E e) {
    //不可添加空元素
    if (e == null)
        throw new NullPointerException();
    final ReentrantLock lock = this.lock;
    lock.lock();
    int n, cap;
    Object[] array;
    //动态扩容，队列容量够了，才会执行入队操作
    while ((n = size) >= (cap = (array = queue).length))
        tryGrow(array, cap);
    try {
        Comparator<? super E> cmp = comparator;
        if (cmp == null)
            siftUpComparable(n, e, array);
        else
            siftUpUsingComparator(n, e, array, cmp);
        size = n + 1;
        //唤醒notEmpty等待队列上的等待线程
        notEmpty.signal();
    } finally {
        lock.unlock();
    }
    return true;
}
```



### 出队操作

- peek()  方法，实现Queue定义的抽象方法，单纯的获取元素，不会修改队列。也没有等待操作。
- poll()方法，实现Queue定义的抽象方法，执行出队操作，会修改队列。没有等待操作。
- take()方法，实现BlockingQueue定义的抽象方法，执行出队操作，会修改队列。有等待操作。
- poll(long timeout, TimeUnit unit) ，执行出队操作，会修改队列。有等待操作。   注意若超过等待时间，获取了锁资源，队列还是为空，则返回null。

```java
public E poll() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        return dequeue();
    } finally {
        lock.unlock();
    }
}

public E take() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    E result;
    try {
        while ( (result = dequeue()) == null)
            notEmpty.await();
    } finally {
        lock.unlock();
    }
    return result;
}

public E poll(long timeout, TimeUnit unit) throws InterruptedException {
    long nanos = unit.toNanos(timeout);
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    E result;
    try {
        while ( (result = dequeue()) == null && nanos > 0)
            nanos = notEmpty.awaitNanos(nanos);
    } finally {
        lock.unlock();
    }
    return result;
}

public E peek() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        return (size == 0) ? null : (E) queue[0];
    } finally {
        lock.unlock();
    }
}
```





