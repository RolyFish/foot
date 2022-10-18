# 线程  进程

> 进程由多个线程组成
>
> 线程是程序执行的最小单位

# 多线程

> 多线程实现的三个方式

- 继承Thread类
- 实现runable接口
- 实现callabel接口

## 继承Thread类

```java
public class MyThread extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("thread==>"+i);
        }
    }
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        //调用run方法会以单线程的方式执行  先执行run在执行main
        //myThread.run();
        myThread.start();
        for (int i = 0; i < 20; i++) {
            System.out.println("main==>"+i);
        }
    }
}
```

> 调用start方法执行多线程任务
>
> 结果是交替执行
>
> 结论：线程开启，他的执行由cpu调度，无规则

<img src="thread.assets\image-20211230113221387.png" alt="image-20211230113221387" style="zoom:50%;" />

## 实现runable接口

> 线程启动方式：通过thread（runnable）.start启动
>
> （代理）

```java
public class Runable01 implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("runnable=>"+i);
        }
    }
    public static void main(String[] args) {
        //启动一个子线程
        Runable01 runable01 = new Runable01();
        new Thread(runable01).start();
        //主线程
        for (int i = 0; i < 20; i++) {
            System.out.println("main=>"+i);
        }
    }
}
```

## 问题（并发）

> 多线程操作统一资源造成数据紊乱
>
> （某一线程未执行完毕，数据未释放，另一个线程又去操作统一数据）

```java
public class Runable02 implements Runnable {
    private int ticket;
    public Runable02(int ticket) {
        this.ticket = ticket;
    }
    @Override
    public void run() {
        for (int i = 0; i < ticket; i++) {
            System.out.println(Thread.currentThread().getName()+"拿到了第"+ticket--+"张票");
            try {
                //模拟延时操作
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        Runable02 runable01 = new Runable02(10);
        new Thread(runable01,"于延闯1").start();
        new Thread(runable01,"于延闯2").start();
        new Thread(runable01,"于延闯3").start();
    }
}
```

## 实现callable接口

```java
public class Callable01 implements Callable {
    String url;
    String pathname;
    Downloader downloader;
    public Callable01(String url, String pathname, Downloader downloader) {
        this.url = url;
        this.pathname = pathname;
        this.downloader = downloader;
    }
    @Override
    public Object call() throws Exception {
        System.out.println(pathname);
        downloader.WebDownloader(url, pathname);
        return pathname;
    }
    public static void main(String[] args) {
        System.out.println("开始下载图片");
        Callable01 t1 = new Callable01("https://pic3.zhimg.com/80/v2-80d6db29319d4b59924724fd15a032b6_1440w.jpg?source=1940ef5c\n", "image1/1.jpg", new Downloader());
        Callable01 t2 = new Callable01("https://pic3.zhimg.com/80/v2-80d6db29319d4b59924724fd15a032b6_1440w.jpg?source=1940ef5c\n", "image1/2.jpg", new Downloader());
        Callable01 t3 = new Callable01("https://pic3.zhimg.com/80/v2-80d6db29319d4b59924724fd15a032b6_1440w.jpg?source=1940ef5c\n", "image1/3.jpg", new Downloader());
        ExecutorService service = Executors.newFixedThreadPool(3);
        Future rs1 = service.submit(t1);
        Future rs2 = service.submit(t2);
        Future rs3 = service.submit(t3);
        
        try {
            Object o1 = (String)rs1.get();
            Object o2 = (String)rs2.get();
            Object o3 = (String)rs3.get();

            System.out.println(o1);
            System.out.println(o2);
            System.out.println(o3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }
}
class Downloader {
    public void WebDownloader(String url, String pathname) {
        try {
            FileUtils.copyURLToFile(new URL(url), new File(pathname));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

> ExecutorService service = Executors.newFixedThreadPool(3);
>
>  Future rs1 = service.submit(t1);
>
>  Object o1 = (String)rs1.get();
>
>  service.shutdown();

## lambda表达式

```java
public class Lambda {

    //静态内部类
    static class Ilambda2 implements Ilike {
        @Override
        public void lambda() {
            System.out.println("Lambda2");
        }
    }
    public static void main(String[] args) {
        //内部类
        Ilike ilike = new Ilambda();
        ilike.lambda();
        //静态内部类
        ilike = new Ilambda2();
        ilike.lambda();
        //局部内部类
        class Ilambda3 implements Ilike {
            @Override
            public void lambda() {
                System.out.println("Lambda3");
            }
        }
        //局部内部类
        ilike = new Ilambda3();
        ilike.lambda();
        //匿名内部类
        ilike = new Ilike() {
            @Override
            public void lambda() {
                System.out.println("Lambda4");
            }
        };
        ilike.lambda();
        //lamdba表达式
        ilike = ()->{
            System.out.println("Lambda5");
        };
        ilike.lambda();
    }
}
//函数式接口
interface Ilike {
    void lambda();

}
//内部类
class Ilambda implements Ilike {
    @Override
    public void lambda() {
        System.out.println("Lambda");
    }
}
```

> 简化过程：外部实现类，内部实现类，静态内部类，局部内部类，匿名内部类（需要括号【也就是方法参数】），lambda表达式

> lambda表达式的简化
>
> 函数式接口（只有一个抽象方法【如果有参数】）
>
> ()就是方法参数可以去掉参数类型  {}就是代码块

> 1、(int a)->{sout("......")}
>
> 2、如果只有一个参数可以吧（）去掉  =》a->{sout(".....")}
>
> 3、如果方法体中只有一行代码{}去掉    a->sout(''....'')

# 线程状态

- new（创建
- start就绪
- cpu调度 运行
- 阻塞状态
- dead运行结束

# 停止一个线程

> jdk提供的stop  destroy不建议使用

> 添加一个标志位  将一个死循环线程自己停止（也就是执行完毕）

```java
public class stop implements Runnable {
    private static Boolean flag = true;
    @Override
    public void run() {
        while (flag){
            System.out.println(Thread.currentThread().getName()+"run");
        }
    }
    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName()+"run"+i);
                if (i==80){
                    System.out.println("停止子线程");
                    flag = false;
                }
            }
        }).start();
        stop stop = new stop();
        new Thread(stop,"子线程").start();
    }
}
```

# 线程休眠

> Sleep方法

应用：

- 计时
- 输出实时时间

# 线程礼让 yield

```java
public class Yield {
    public static void main(String[] args) {
        test t1 = new test();
        new Thread(t1,"线程一").start();
        new Thread(t1,"线程二").start();
    }
}
class test implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"执行中");
        Thread.yield();
        System.out.println(Thread.currentThread().getName()+"执行结束");
    }
}
```

> 礼让不一定成功（礼让==》回到同一起点 线程从执行状态回到就绪状态）

# state

开两个线程查看线程状态

```java
Thread thread = new Thread(() -> {
    for (int i = 0; i < 100; i++) {
        //System.out.println("========");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}, "被监测线程");
thread.start();
new Thread(()->{
    while (thread.getState()!= Thread.State.TERMINATED){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread.getState());
    }
},"监测线程").start();
```

# 优先级  priority

> 没卵用        增加cpu调度的概率

# 守护线程daemon

> 虚拟机需要确保用户进程执行完毕
>
> 虚拟机不管守护进程状态（用户进程执行完毕虚拟机该停停）

> 用于后台日志操作，内存监控，垃圾回收

```java
public class Daemon {
    public static void main(String[] args) {
        Thread gad = new Thread(new gad(), "上帝");
        gad.setDaemon(true);
        gad.start();
        Thread you = new Thread(new you(), "you");
        you.start();
    }
}
class you implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 365; i++) {
            System.out.println("==我活着");
        }
        System.out.println("==我死了==");
    }
}
class gad implements Runnable{
    @Override
    public void run() {
        while (true){
            System.out.println("==上帝保佑着我==");
        }
    }
}
```

# 线程同步

> 多线程操作同一个数据的时候会出现线程不安全问题

> arrayList是线程不安全的  copyonwriteArraylist是线程安全的

```java
public static void main(String[] args) {
    List<Object> list = new ArrayList<>();
    for (int i = 0; i < 10000; i++) {
        new Thread(()->{
            list.add(Thread.currentThread().getName());
        }).start();
    }
    try {
        Thread.sleep(4000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println(list.size());
}
```

![image-20211230164729235](thread.assets\image-20211230164729235.png)

```java
public static void testStringBuffer() {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < 10000; i++) {
        new Thread(()->
            stringBuffer.append("a")
        ).start();
    }
    try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println(stringBuffer.length());
}
```

> 我们的stringbuffer就是线程安全的

==线程不同步的原因是由于同一时间不同线程将统一数据都拿到自己的工作空间中（也就是在拿的时候通过了程序的判断，这边拿的是引用），对其修改==

> 解决：队列＋锁（synchronized）

***不加锁操作同一资源：会出现数据不安全问题***

<img src="thread.assets\image-20211230170759940.png" alt="image-20211230170759940" style="zoom:67%;" />

***解决：同步方法，在闰方法上加synchronized关键字即可***

```java
@Override
public synchronized void run() {
    while (ticket > 0) {
        System.out.println(Thread.currentThread().getName() + "拿到了第" + ticket-- + "张票");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

***问题***：synchronize默认锁的是this，也就是当前对象的资源实现了线程安全，如果操作其他对象中的数据也不行

***解决***：***同步块***

```java
new Thread(() -> {
    synchronized (list) {
        list.add(Thread.currentThread().getName());
    }
}).start();
```

# 死锁

<img src="thread.assets\image-20211230173102060.png" alt="image-20211230173102060" style="zoom:67%;" />

例子：

```java
public class DeadLock {
    public static void main(String[] args) {
        MakeUp girl1 = new MakeUp(0, "女孩1");
        MakeUp girl2 = new MakeUp(1, "女孩2");
        new Thread(girl1,"女孩1").start();
        new Thread(girl2,"女孩2").start();
    }
}
class LipStick {
}

class Mirror {
}
class MakeUp implements Runnable {
    static LipStick lipStick = new LipStick();
    static Mirror mirror = new Mirror();
    int choice;
    String name;
    public MakeUp(int choice, String name) {
        this.choice = choice;
        this.name = name;
    }
    @Override
    public void run() {
        if (choice==0){
            synchronized (lipStick){
                System.out.println("锁住了口红");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("想去拿镜子");
                synchronized (mirror){
                    System.out.println("锁住了镜子");
                }
            }
            System.out.println("结束");
        }
        if (choice==1){
            synchronized (mirror){
                System.out.println("锁住了镜子");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("想去拿口红");
                synchronized (lipStick){
                    System.out.println("锁住了口红");
                }
            }
            System.out.println("结束");
        }
    }
}
```

结果就是卡死了

![image-20211230174042840](thread.assets\image-20211230174042840.png)

***解决***

别将共享的资源放在同步块内

# lock 

ReentrantLock可重复锁

```java
public class TestLock {
    public static void main(String[] args) {
        ticket ticket = new ticket();
        new Thread(ticket,"小明").start();
        new Thread(ticket,"小刚").start();
        new Thread(ticket,"小芳").start();
    }
}
class ticket implements Runnable{
    int num = 10;
    final  ReentrantLock lock = new ReentrantLock();
    @Override
    public void run() {
        lock.lock();
        try {
            while (num>0){
                System.out.println(Thread.currentThread().getName()+"拿到了"+num--);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
```

# 线程通信问题

## 缓冲区：生产者消费者

## 信号灯

> 通过wait 和 notifyall控制线程执行顺序。
>
> cpu调度线程是没有规律的，顶多可设置优先级，但是调度的具体情况我们无从得知
>
> synchronized同步方法和同步代码块以及lock锁方式保证的是多线程资源同步的问题，而wait、notifyall用于控制线程执行顺序

```java
public class ThreadCommunication2 {
    public static void main(String[] args) {
        Video video = new Video();
        player player = new player(video);
        watcher watcher = new watcher(video);
        new Thread(player).start();
        new Thread(watcher).start();
    }
}
//生产者
class player implements Runnable {

    private Video v;

    public player(Video v) {
        this.v = v;
    }
    @Override
    public void run() {
        while (true) {
            v.play("XXX节目");
        }
    }
}
//消费者
class watcher implements Runnable {

    private Video v;

    public watcher(Video v) {
        this.v = v;
    }

    @Override
    public void run() {
        while (true) {
            v.view();
        }
    }
}
//节目
class Video {
    //节目名称
    String name;
    //节目状态  false生产  true观看
    Boolean flag = false;
    public synchronized void view() {
        if (!flag) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //观看
        System.out.println("观众观看=》" + name);
        //观看结束  通知其他线程使用资源
        this.notifyAll();
        flag = !flag;
    }
    public synchronized void play(String name) {
        if (flag) {
            //正在观看中
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //play
        System.out.println("演员表演" + name);
        this.name = name;
        //表演结束  通知其他线程使用资源
        this.notifyAll();
        flag = !flag;
    }
}
```

# 线程池

> 该念和数据库连接池一样，==可管理线程（包括连接数量，关闭线程。。）==保证效率提高性能

> 理解一下线程池执行实现runnable和callable对象的区别
>
> 一般都会用newFixedThreadPool来创建一个ExecutorService

```pash
实现runable通过execute的方式执行线程
实现callcable通过submit的方式执行线程

创建线程池（newfixedThreadPool）
执行线程（submit execute）
获取返回结果（get）
关闭线程池（shutdown）
```

# 总结

> 线程创建的三种方式

```pash
一、继承Thread类重写run方法
	调用方式：new myThread 调用start方法（调用run方法和普通方法一样）
二、实现runnable接口重写run方法
	调用方式：new myRunable new Thread（runable）.start
三、实现callable接口重写run方法
	1、创建一个线程池  newFixedThreadPool
	2、执行线程 submit、execute
	3、又返回结果获取返回结果
	4、关闭资源
```

==通过Thread来执行实现callable接口==

```bash
两个类：
	FuctureTask
		他的构造方法里有一个参数（实现callable接口的类）
	RunnableFucture
		FuctureTask实现了	RunnableFucture接口	RunnableFucture接口实现了Runnable接口
结论：可以将实现了Callable接口的类放入FuctureTask中，执行new Thread.start
此刻阿里编程规范会告诉我们：请不要显示的创建线程，推荐使用线程池
```

