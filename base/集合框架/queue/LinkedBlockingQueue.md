本文学习LinkedBlockingQueue，同步、阻塞、无界，使用单向链表实现队列。



> [xxxxx]()



### 简介

​	LinkedBlockingQueue是一个使用单向链表实现的阻塞队列，按照 FIFO（先进先出）添加、删除元素，新元素会以尾插法插入到队列的尾部，出队操作会获取头结点元素(除了peek方法其余出队操作会修改头结点指针)。
​	LinkedBlockingQueue可指定容量。如果不指定，默认容量大小等于Integer.MAX_VALUE。

​	LinkedBlockingQueue通过takeLock、putLock分别对读写线程实现同步。并通过notEmpty、notFull来实现读写线程的等待操作。



<hr>



### 内部类

#### Node

> 只有两个属性，当前节点value和后继节点引用。
>
> 所以说`LinkedBlockingQueue`是一个单向队列。

```java
static class Node<E> {
    E item;
    Node<E> next;
    Node(E x) { item = x; }
}
```



<hr>



### 重要属性

```java
//队列容量 可自定义
private final int capacity;
//原子的int类型。队列元素个数
private final AtomicInteger count = new AtomicInteger();
//头结点  （这是一个空节点，不是第一个节点）
transient Node<E> head;
//尾结点
private transient Node<E> last;
//获取元素锁
private final ReentrantLock takeLock = new ReentrantLock();
//当队列为空时，获取元素操作会加入此等待队列
private final Condition notEmpty = takeLock.newCondition();
//添加元素锁
private final ReentrantLock putLock = new ReentrantLock();
//当队列满了时，添加元素操作会加入此等待队列
private final Condition notFull = putLock.newCondition();
```

值的注意的是这个count使用的是java.util.concurrent.atomic 下的原子类AtomicInteger。

- 为什么使用？

  ​	对比`ArrayBlockingQueue`这个同步队列来看，`ArrayBlockingQueue`里面所有操作（读、写）都是通过同一个ReentranLock来实现同步的，所有对count的修改操作都是在lock()....unlock()之间的，所以在多线程环境下没有线程安全问题，当然效率也有点低。

  ​	而`LinkedBlockingQueue`是通过`takeLock`和`putLock`两把锁来实现同步的，那么对于count的修改肯定会出现线程安全问题。

- AtomicInteger如何实现同步的？

  三个重要的东西实现同步：

  ```java
  //使用unsafe下的cas操作，保证原子性
  private static final Unsafe unsafe = Unsafe.getUnsafe();
  //偏移量，这个long值可以理解为内存地址值，用于快速检索出内存中的对象
  private static final long valueOffset;
  //volatile 来保证内存可见性和有序性（即当先线程修改了，其他线程会感知）
  private volatile int value;
  ```



<HR>



### 构造器

> 很简单的构造方法

- 默认容量int.max

- 可自定义队列容量

```java
public LinkedBlockingQueue() 
public LinkedBlockingQueue(int capacity)
public LinkedBlockingQueue(Collection<? extends E> c) {}
```





<hr>



### 获取队列状态的方法



```java
//队列元素个数
public int size() {
    return count.get();
}
//队列剩余容量
public int remainingCapacity() {
    return capacity - count.get();
}
//是否包含某个元素
public boolean contains(Object o);
//清除队列
public void clear();
//都加锁
void fullyLock() {
    putLock.lock();
    takeLock.lock();
}
//都解锁
void fullyUnlock() {
    takeLock.unlock();
    putLock.unlock();
}
//移除p节点。trail为p前驱节点
void unlink(Node<E> p, Node<E> trail);
//唤醒 NotEmpty等待队列上的线程
private void signalNotEmpty();
//唤醒 NotFull等待队列上的线程
private void signalNotFull();

```



<hr>



### 添加元素

- AbstractQueue.add()  会调用offer()方法
- offer()。存在一个超时重载，这个重载可响应中断异常。

- put()。存在一个超时重载，这个重载可响应中断异常。

> 添加元素，没有什么特别操作，主要分一下四步。

- 创建Node节点，尾插法插入链表尾部。
- 原子操作修改count值
- 容量够唤醒notFull上的线程
- 添加元素成功，释放锁后，唤醒NotEmpty上的线程



**enqueue(Node<E> node)**

这个方法也简单。

```java
private void enqueue(Node<E> node) {
    // assert putLock.isHeldByCurrentThread();
    // assert last.next == null;
    last = last.next = node;
}
```



### 获取元素

- peek()

  ①获取第一个节点的value值

  ②注意这个方法不会移动头尾指针

  ③不会加入notEmpty等待队列，获取失败直接返回null。

```java
public E peek() {
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lock();
    Node<E> first = head.next;
    if (first == null)
        return null;
    else
        return first.item;
	takeLock.unLock();
}
```

- take()

  ①获取第一个节点value值

  ②获得一个可响应中断锁

  ③队列为空时会家拖入notEmpty等待队列

  ④断开第一个节点，移动头结点指针

  ⑤原子修改count值

  ⑥count大于1，唤醒notEmpty上的等待线程

  ⑦符合条件，唤醒NotFull上的线程。

```java
public E take() throws InterruptedException {
    E x;
    int c = -1;
    final AtomicInteger count = this.count;
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lockInterruptibly();
    try {
        while (count.get() == 0) {
            notEmpty.await();
        }
        x = dequeue();
        c = count.getAndDecrement();
        if (c > 1)
            notEmpty.signal();
    } finally {
        takeLock.unlock();
    }
    if (c == capacity)
        signalNotFull();
    return x;
}
```



- poll()

> 和take()类似，只不过不会加入notEmpty等待队列，获取失败会返回null。
>
> 他的超时重载方法会加入notEmpty等待队列，和take()方法一样。



<hr>



### 遍历

- Collection接口下的Stream()的Foreach方法
- toArray转数组，普通for循环
- 增强for循环
- Iterable下的foreach()方法
- 迭代器
- take()、poll()方法
- 分离器

```java
@Test
public void foreach() throws InterruptedException {

    LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<>();
    addRandom(queue, 15);

    Object[] objects = queue.toArray();
    for (int i = 0; i < objects.length; i++) {
        System.out.printf(objects[i] + ", ");
    }
    System.out.println();

    for (Object obj : queue) {
        System.out.printf(obj + ", ");
    }
    System.out.println();


    queue.stream().forEach((obj) -> System.out.printf(obj + ", "));
    System.out.println();

    queue.forEach((obj) -> System.out.printf(obj + ", "));
    System.out.println();

    Iterator<Object> iterator = queue.iterator();
    while (iterator.hasNext()) System.out.printf(iterator.next() + ", ");
    System.out.println();


    //while (queue.size() > 0) System.out.printf(queue.take() + ", ");
    System.out.println();

    Object obj = null;
    //while (queue.size() > 0) System.out.printf(queue.poll() + ", ");
    //while ((obj = queue.poll()) != null) System.out.printf(obj + ", ");
    System.out.println();


    Spliterator<Object> spliterator = queue.spliterator();
    //while (spliterator.tryAdvance(o -> System.out.printf(o + ", ")));
    System.out.println();

    spliterator.forEachRemaining(o -> System.out.printf(o + ", "));
    System.out.println();
}

public void addRandom(Queue<Object> queue, Integer capacity) {
    assert capacity > 0;
    for (Integer i = 0; i < capacity; i++) {
        queue.add((int) (Math.random() * 20));
    }
}
```



### 总结

> ​	LinkedBlockingQueue是一个单向队列，使用putLock和takeLock来分别管理添加、获取元素的同步状态，一定程度上提高效率。使用了Integer的原子类AtomicInteger避免对于count修改造成的并发问题。
>
> ​	使用NotEmpty和NotFull来管理；两个等待队列。
>
> ​	表现为一个FiFo先进先出队列。

