本文学习了解Condition的等待通知机制，分析原理、等待队列模型。



> [上一篇]()



### 简介

​	关于线程的等待唤醒机制，最初接触是在使用Synchronized实现同步那里，通过调用Object的wait()和notify()实现等待唤醒。java在lock体系下也提供了等待唤醒机制，也就是Condition的await()和signal(),对比Object的等待唤醒，Condition提供 超时等待、可响应中断等待并且可提供多个等待队列，具有更高的可控制性和可拓展性。

​	Condition是和Lock绑定的等待通知组件。

​	调用condition的await()方法首先得获取同步状态，和Object.wait()一样。

​	condition可以存在多个，即可以有多个等待队列，而Object只有一个队列。

<hr>

### api

等待：

1. void await() throws InterruptedException:当前线程进入等待状态，如果其他线程调用condition的signal或者signalAll方法并且当前线程获取Lock从await方法返回，如果在等待状态中被中断会抛出被中断异常；
2. long awaitNanos(long nanosTimeout)：当前线程进入等待状态直到被通知，中断或者**超时**；
3. boolean await(long time, TimeUnit unit)throws InterruptedException：同第二种，支持自定义时间单位
4. boolean awaitUntil(Date deadline) throws InterruptedException：当前线程进入等待状态直到被通知，中断或者**到了某个时间** 

唤醒：

1. void signal()：唤醒一个等待在condition上的线程，将该线程从**等待队列**中转移到**同步队列**中，如果在同步队列中能够竞争到Lock则可以从等待方法中返回。
2. void signalAll()：与1的区别在于能够唤醒所有等待在condition上的线程

<hr>

### 原理

说明：原理基于ReenTrantLock分析。

#### 等待队列

通过ReenTrantLock的newCondition()方法创建condition对象，查看源码发现其实创建的是AQS的内部类ConditionObject。

查看一下这个类：

两个重要属性：firstWaiter、lastWaiter分别是等待队列的头结点和尾结点。

```java
/** First node of condition queue. */
private transient Node firstWaiter;
/** Last node of condition queue. */
private transient Node lastWaiter;
```

其他都是节点入队和出队的方法。



其结构大概是这个样子：

入队操作是尾插法：

![image-20220402015947482](/Users/rolyfish/Desktop/MyFoot/thead/thread12-condition.assets/image-20220402015947482.png)

出队操作：

从等待队列断开，尾插法插入同步队列

![image-20220402020611487](/Users/rolyfish/Desktop/MyFoot/thead/thread12-condition.assets/image-20220402020611487.png)



#### 加入等待队列

##### await(方法)：

await作用的一定是获取了同步状态的线程不然在释放锁时会直接报异常，也就是调用await()方法首先得获取同步状态(锁)。

```java
public final void await() throws InterruptedException {
    //可响应中断异常
    if (Thread.interrupted())
        throw new InterruptedException();
    //将当前线程包装成等待节点插入等待队列
    Node node = addConditionWaiter();
    //当前线程释放锁，记录同步状态方便唤醒还原
    int savedState = fullyRelease(node);
    int interruptMode = 0;
    //如果节点存在于同步队列则跳出循环加入同步队列
    //否则进入循环，阻塞线程，并在被中断时跳出循环
    while (!isOnSyncQueue(node)) {
        LockSupport.park(this);
        //检测中断
        if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
            break;
    }
    //线程被中断或被唤醒 进行自旋获取锁
    if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
        interruptMode = REINTERRUPT;
    //当前节点下一个节点不为空  则将当前节点从等待队列移除
    if (node.nextWaiter != null) // clean up if cancelled
        unlinkCancelledWaiters();
    if (interruptMode != 0)
        reportInterruptAfterWait(interruptMode);
}
```

此方法包含操作：

①加入等待队列

②完全释放当前节点获取的锁，并缓存同步状态state，方便唤醒线程。此刻会唤醒head.next节点

③跳出while循环条件：1、线程被中断  2、unpark()使得节点==加入到同步队列中==

④这个acquireQueued方法参数savedState就是上边记录的，该方法就是自旋获取同步状态



##### addConditionWaiter()

加入等待对列方法

```java
private Node addConditionWaiter() {
    Node t = lastWaiter;
    // If lastWaiter is cancelled, clean out.
    //如果等待队列尾结点被取消，将其从同步队列剔除
    if (t != null && t.waitStatus != Node.CONDITION) {
        unlinkCancelledWaiters();
        t = lastWaiter;
    }
    //否则创建新的等待节点，以尾插法的方式插入等待队列
    Node node = new Node(Thread.currentThread(), Node.CONDITION);
    if (t == null)
        firstWaiter = node;
    else
        t.nextWaiter = node;
    lastWaiter = node;
    //并返回插入节点
    return node;
}
```



##### fullyRelease

完全释放当前线程所占锁，会将当前节点状态cas为0

```java
final int fullyRelease(Node node) {
    boolean failed = true;
    try {
        int savedState = getState();
        //一次性释放重入次数
        if (release(savedState)) {
            failed = false;
            return savedState;
        } else {
            throw new IllegalMonitorStateException();
        }
    } finally {
        //如果释放失败(抛出异常)则会取消当前节点
        if (failed)
            node.waitStatus = Node.CANCELLED;
    }
}
```

##### isOnSyncQueue

这个方法检测节点是否存在于同步队列

```java
final boolean isOnSyncQueue(Node node) {
    if (node.waitStatus == Node.CONDITION || node.prev == null)
        return false;
    if (node.next != nul) // If has successor, it must be on queue
        return true;
    return findNodeFromTail(node);
}
```



#### 唤醒节点



##### signal()

```java
public final void signal() {
    //判断当前节点是否是同步状态拥有者
    if (!isHeldExclusively())
        throw new IllegalMonitorStateException();
    Node first = firstWaiter;
    if (first != null)
        //唤醒首节点
        doSignal(first);
}
```



##### doSignal

唤醒成功则跳出循环

```java
private void doSignal(Node first) {
    do {
        if ( (firstWaiter = first.nextWaiter) == null)
            lastWaiter = null;
        first.nextWaiter = null;
    } while (!transferForSignal(first) &&
             (first = firstWaiter) != null);
}
```



##### transferForSignal

这个方法是唤醒的核心方法

```java
final boolean transferForSignal(Node node) {
    /*
     * If cannot change waitStatus, the node has been cancelled.
     */‘
     //cas设置 等待节点 状态为0  也就是初始状态
    if (!compareAndSetWaitStatus(node, Node.CONDITION, 0))
        return false;

    /*
     * Splice onto queue and try to set waitStatus of predecessor to
     * indicate that thread is (probably) waiting. If cancelled or
     * attempt to set waitStatus fails, wake up to resync (in which
     * case the waitStatus can be transiently and harmlessly wrong).
     */
    //尾插法插入同步队列
    Node p = enq(node);
    int ws = p.waitStatus;
    //cas设置同步状态为-1  并唤醒线程
    if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL))
        LockSupport.unpark(node.thread);
    return true;
}
```

<hr>

#### 总结

- 想要调用await()方法，必须首先获取同步状态，也就是调用await()方法线程所属节点为head节点。

- 在调用await()方法会做：
  - 加入等待队列
  - 完全释放当前节点同步状态，记录savedState方便恢复节点，此刻会唤醒head.next节点的线程，并覆盖head节点，这里也是需要await的节点从同步队列断开的过程。
  - while判断当前节点是否存在于同步队列，不存在则调用LockSuport.park()阻塞。这里跳出while循环的条件是①线程被中断(中断也是会释放LockSuport的许可信息)②线程被唤醒，加入同步队列
  - 自旋获取同步状态(这里就用到了上面记录的savedState)
  - 如果等待队列存在节点，就需要将当前节点从等待队列断开

- 唤醒就做两件事：加入同步队列、唤醒线程。