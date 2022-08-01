本文学习一个同步阻塞、无界(可自定义容量)、双向链表。就是LinkedList的同步版。



> [xxxx]()



### 简介

​	`LinkedBlockingDeque`是一个同步的双向链表。通过first、last两个指针来管理入阻塞队出阻塞队操作。使用ReentranLock(默认非公平)锁来实现同步。notEmpty和notFull两个等待队列来管理特定情况下的入等待队、出等待队操作。

​	`apache`也提供了几乎一模一样的`LinkedBlockingDeque`，在`org.apache.commons.pool2.impl;`包下。本文以`java.util.concurrent`包下的`LinkedBlockingDeque`做理解。

​	`LinkedBlockingDeque`完全可以理解为`LinkedList`的同步版本。



<hr>



### 属性

```java
//头结点指针
transient Node<E> first;
//尾结点指针
transient Node<E> last;
//队列元素个数
private transient int count;
//队列容量。达到这个容量，添加元素操作，会进入等待队列。
private final int capacity;
//非公平锁，管理线程对 Queue的访问权限
final ReentrantLock lock = new ReentrantLock();
//当队列为空时   获取元素操作进入等待队列
private final Condition notEmpty = lock.newCondition();
//当队列满了时   添加元素操作进入等待队列
private final Condition notFull = lock.newCondition();
```



<hr>



### 构造器

```java
//默认容量为 int最大值
public LinkedBlockingDeque() {
    this(Integer.MAX_VALUE);
}
//可自定义容量
public LinkedBlockingDeque(int capacity) {
    if (capacity <= 0) throw new IllegalArgumentException();
    this.capacity = capacity;
}
//可初始化
public LinkedBlockingDeque(Collection<? extends E> c) {
}
```



<hr>


### 入队出队操作

LinkedBlockingDeque继承了AbstractQueue、实现了BlockingDeque。会使用加锁和释放锁来保证同步，但具体操作大致分为三种：

- 入队出队失败，返回false
- 入队出队失败，进行await()，加入等待队列
- 入队出队失败，进行awaitNacos()，进行超时等待



#### 入队出队语义

> 入队操作：当链表满了返回false，表示添加失败。（Queue  Full）
>
> 出队操作：当链表为空（first或last为null）返回false，表示添加失败。（Queue  Full）
>
> 这是两个私有方法，只提供内部方法使用。以下入队出队操作都会调用这些方法。这些方法内部调用了Condation.signal()方法，也就是这些方法必须在同步代码块中执行。

```java
//链接到链表头部
private boolean linkFirst(Node<E> node) ;
//链接到链表尾部
private boolean linkLast(Node<E> node) ;
//断开first节点
private E unlinkFirst();
//断开last节点
private E unlinkLast();
```

#### 入队

> offer()操作。  添加失败返回false

```java
//默认offerLast
public boolean offer(E e) ;
```

> add()操作。这里不会await()加入等待队列。

```java
//默认使用addLast()方法
public boolean add(E e);
//调用offerLast，添加失败，抛出异常
public void addLast(E e);
//调用linkLast方法。并返回是否添加成功
public boolean offerLast(E e); 
/**/
//调用offerFirst，添加失败，抛出异常
public void addFirst(E e);
//调用linkFirst方法。并返回是否添加成功
public boolean offerFirst(E e); 
```

> put() 操作。队列满了会await()。
>
> 奇怪的是这里并没有获取可响应中断的锁，却抛出了中断异常。

```java
//默认调用 putLast
public void put(E e) throws InterruptedException;
//会调用linkLast。队列满了会await()
public void putLast(E e) throws InterruptedException {
    while (!linkLast(node))
        notFull.await();
}
//会调用linkFirst。队列满了会await()
public void putFirst(E e) throws InterruptedException {
    while (!linkFirst(node))
        notFull.await();
}
```

> offer存在超时重载

添加失败会进入等待队列，超过等待时间，会自动唤醒，此刻若还是添加失败会返回false。

```java
public boolean offerFirst(E e, long timeout, TimeUnit unit)
    while (!linkFirst(node)) {
        if (nanos <= 0)
            return false;
        nanos = notFull.awaitNanos(nanos);
    }
    return true;
}
public boolean offerLast(E e, long timeout, TimeUnit unit);
```

#### 出队

> take()。方法会加入notEmpty等待队列。

```java
//调用takeFirst()方法
public E take() throws InterruptedException ;
//调用unlinkFirst()方法。头结点出链。 出链失败(链表为空)进行等待。
public E takeFirst() throws InterruptedException {
    E x;
    while ( (x = unlinkFirst()) == null)
        notEmpty.await();
    return x;
}
//调用unlinkLast()方法。尾结点出链。出链失败(链表为空)进行等待。
public E takeLast() throws InterruptedException;
```

> poll()和take()差不多，都会调用unlink。
>
> 不同的是：poll()会直接返回出队结果，不会调用await()加入等待队列。

```java
public E pollFirst() {
    return unlinkFirst();
}
public E pollLast() {
    return unlinkLast();
}
```

> remove()。对poll的一层封装，返回删除元素失败，会抛出异常。

```java
//默认removeFirst
public E remove() {
    return removeFirst();
}
//调用pollFirst()，出队失败，抛出异常。
public E removeFirst() {
    E x = pollFirst();
    if (x == null) throw new NoSuchElementException();
    return x;
}
public E removeLast() {
    E x = pollLast();
    if (x == null) throw new NoSuchElementException();
    return x;
}
```



> poll()也存在一个超时重载。

出队失败会进入等待队列，超过等待时间，会自动唤醒，此刻若还是出队失败会返回null。

```java
public E pollFirst(long timeout, TimeUnit unit){
    while ( (x = unlinkFirst()) == null) {
        if (nanos <= 0)
            return null;
        nanos = notEmpty.awaitNanos(nanos);
    }
    return x;
}
public E pollLast(long timeout, TimeUnit unit);
```



#### 获取元素

> peek()
>
> 不会调用unLink方法移除元素，只是获取
>
> 队列为空返回null
>
> 不会await()

```java
//默认调用peekFirst
public E peek();
//获取first.item。队列为空返回null
public E peekFirst() {
    return (first == null) ? null : first.item;
}
//获取last.item。队列为空返回null
public E peekLast() {
    return (last == null) ? null : last.item;
}
```

> getFirst()&getLast()会调用peekFirst()&peekLast()
>
> 队列为空抛出异常。

```java
public E getFirst() {
    E x = peekFirst();
    if (x == null) throw new NoSuchElementException();
    return x;
}
public E getLast() {
    E x = peekLast();
    if (x == null) throw new NoSuchElementException();
    return x;
}
```



<hr>



### 遍历

```java
public Queue createQueue(final int capacity) {
    assert capacity > 0;
    LinkedBlockingDeque<Object> queue = new LinkedBlockingDeque<>();
    for (int i = 0; i < capacity; i++) {
        queue.add(i);
    }
    return queue;
}

public void mySout(StringBuffer sb) {
    StringBuffer temp = new StringBuffer("{");
    sb.deleteCharAt(sb.lastIndexOf(","));
    sb.append("}");
    temp.append(sb);
    System.out.println(temp);
    sb.delete(0, sb.length());
}


@Test
public void test() throws InterruptedException {

    LinkedBlockingDeque<Integer> queue = (LinkedBlockingDeque) createQueue(15);
    StringBuffer sb = new StringBuffer();
    //collection 转数组 for循环
    Object[] array = queue.toArray();
    for (int i = 0; i < array.length; i++) {
        sb.append(array[i] + ", ");
    }
    mySout(sb);

    //collection strem foreach
    queue.stream().forEach((obj) -> sb.append(obj + ", "));
    mySout(sb);

    //iterable strem foreach
    queue.forEach((obj) -> sb.append(obj + ", "));
    mySout(sb);

    //iterator
    Iterator<Integer> iterator = queue.iterator();
    while (iterator.hasNext()) sb.append(iterator.next() + ", ");
    mySout(sb);

    //remove()  会抛出异常 捕获处理一下
    while (true) {
        try {
            sb.append(queue.remove() + ", ");
        } catch (NoSuchElementException e) {
            break;
        }
    }
    mySout(sb);

    //take() 借助size
    try {
        int size = queue.size();
        while (size-- > 0)
            sb.append(queue.take() + ", ");
    } catch (InterruptedException e) {
        e.printStackTrace();
    } finally {
        mySout(sb);
    }

    //poll()
    Object obj = null;
    while ((obj = queue.poll()) != null) sb.append(obj + ", ");
    mySout(sb);

    //分离器1
    Spliterator<Integer> spliterator = queue.spliterator();

    while (spliterator.tryAdvance((obj) -> sb.append(obj + ", "))) ;
    mySout(sb);

    //分离器2
    spliterator.forEachRemaining((obj) -> sb.append(obj + ", "));
    mySout(sb);
}
```