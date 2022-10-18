# *单例模式*

## 恶汉式



```java
public class Hungry {
    /**
     * 此对象需要的数据空间（有点大，即便不用也在内存中占用着）
     */
    private byte[] data01 = new byte[1024*1024];
    private byte[] data02 = new byte[1024*1024];
    private byte[] data03 = new byte[1024*1024];
    private byte[] data04 = new byte[1024*1024];
    
    private Hungry() {}
    /**
     * 程序开启，创建饿汉式单例对象  （浪费内存资源资源）
     */
    private final static Hungry hungry = new Hungry();

    public static Hungry getInstance(){
        return hungry;
    }
}
```

## 懒汉式

> 一般情况下可以通过，双重检测锁以及防止指令重排（valatile）的操作实现单例。
>
> 不加锁和volatile，会破坏单例（得到的实例不一致）

```java
public class Lazy {
    private Lazy(){
        System.out.println(Thread.currentThread().getId()+"ok");
    }
    /**
     * 避免指令重排
     */
    private volatile static Lazy lazy;
    /**
     * 双重检测锁模式 懒汉式单例 （double check lock）dcl单例
     */
    public static Lazy getInstance(){
        if(lazy==null){
            synchronized (Lazy.class){
                if (lazy==null){
                    lazy=new Lazy();
                    /**
                     * 不是一个原子操作（可能会指令重排）
                     * 1、分配内存空间
                     * 2、执行构造方法，初始化操作
                     * 3、将初始化的对象指向内存空间
                     * 如果说某一个对象他的顺序改变了，可能对结果造成影响
                     * 123 A走123没问题
                     * 132 B走132会造成占有内存空间，但是未实例化
                     */
                }
            }
        }
        return lazy;
    }

    /**
     * 多线程环境下会有问题
     * 线程的执行是没有顺序的
     * 也就是说1号线程执行一半，cpu会去执行2号线程，此刻还未对lazy对象完成实例化，就会有多个线程指向的对象不一致问题
     * 但是无论如何这个数量会相对较少（总会有在对象new结束后才执行的线程）
     * 解决：加锁 双重判断
     */
    public static void main(String[] args) {
        for (int i=0; i<10; i++){
            new Thread(()->{
                Lazy.getInstance();
            }).start();
        }
    }
}
```

> 反射破坏单例模式
>
> 对应解决方法
>
> 1、构造函数内部加锁，判断对象是否为空
>
> 2、对于双反射：可添加秘文字段（但反射机制任有可能破解）

```java
public class Lazy2 {
    private Lazy2(){
        synchronized (Lazy2.class){
            if(lazy!=null){
                throw new RuntimeException("反射破坏单例模式");
            }
        }
        System.out.println(Thread.currentThread().getId()+"ok");
    }
    private volatile static Lazy2 lazy;

    public static Lazy2 getInstance(){
        if(lazy==null){
            synchronized (Lazy2.class){
                if (lazy==null){
                    lazy=new Lazy2();
                }
            }
        }
        return lazy;
    }
    public static void main(String[] args) throws Exception {
        Lazy2 instance = Lazy2.getInstance();
        Constructor<Lazy2> lazy2Constructor = Lazy2.class.getDeclaredConstructor(null);
        //破坏私有
        lazy2Constructor.setAccessible(true);
        Lazy2 lazy1 = lazy2Constructor.newInstance();
        /**
         * 得到的值不一样，单例模式被破坏了
         */
        System.out.println(instance);
        System.out.println(lazy1);
    }
}
```

> 枚举类避免反射破坏

jad查看class反编译文件

```
package com.roily.single01_lazy;

import java.lang.reflect.Constructor;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2021/12/26
 */
public enum EnumSigle {

    INSTANCE;

    public EnumSigle getInstance(){
        return INSTANCE;
    }
    public static void main(String[] args) {
           EnumSigle instance1 = EnumSigle.INSTANCE;
           EnumSigle instance2 = EnumSigle.INSTANCE;
           System.out.println(instance1.getClass().getClassLoader());
           System.out.println(instance2.getClass().getClassLoader());
    }

    /**
     * 枚举类 构造器是有参数的  且破坏不了他的单例
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        EnumSigle instance1 = EnumSigle.INSTANCE;
        Constructor<EnumSigle> declaredConstructor = EnumSigle.class.getDeclaredConstructor(String.class,int.class);
        declaredConstructor.setAccessible(true);
        EnumSigle enumSigle = declaredConstructor.newInstance();
        System.out.println(enumSigle);
    }
}
```

# 工厂模式

## 简单工厂模式（静态工厂模式simpleFactory）

![image-20211226181712168](designPattern.assets\image-20211226181712168.png)

```java
public class CarFactory {
    public static Car getCar(String name){
        if (name.equals("五菱")){
            return new Wuling();
        }else if (name.equals("特斯拉")){
            return new Tesla();
        }else {
            return null;
        }
    }
}
```

> 静态工厂违背开闭原则（不易拓展，拓展需要修改carFactory）

## 方法工厂模式（methodFactory）

![image-20211226182412013](designPattern.assets\image-20211226182412013.png)

```java
public class TeslaFactory implements CarFactory{
    @Override
    public  Car getCar() {
        return new Tesla();
    }
}
```

> 符合开闭原则，不修改原有代码
>
> 但是每一个汽车品牌都要实现car接口且每一个汽车工厂都要实现carfactory，实用性并不高

## 抽象工厂模式

![image-20211226183842563](designPattern.assets\image-20211226183842563.png)

![image-20211226184732854](designPattern.assets\image-20211226184732854.png)

> 产品接口。产品工厂。
>
> 产品实现对应产品接口。具体产品工厂实现对应产品工厂接口。
>
> 违背开闭原则（但是在系统有足够的统筹情况下很合理）

# 建造者模式（builder）

![image-20211226204224334](designPattern.assets\image-20211226204224334.png)

> 抽象建造者（抽象方法）。真实建造者（建造产品的具体方法）。管理者（建造产品的具体步骤）。产品。

![image-20211226204609013](designPattern.assets\image-20211226204609013.png)

> 去除管理者，对象的创建由用户决定。

# 原型模式（克隆模式）

> 实现cloneable接口，重写clone方法。

```java
public static void main(String[] args) throws Exception {
    //原型对象  v1
    Video v1 = new Video("狂神说java", new Date());
    System.out.println("v1=>"+v1.toString()+"--"+v1.hashCode());
    //克隆对象 v2
    Video v2 = (Video) v1.clone();
    System.out.println("v2=>"+v2.toString()+"--"+v2.hashCode());
}
```

![image-20211227063147151](designPattern.assets\image-20211227063147151.png)

> 得到对象属性一样但是，哈希值值不一样



> 问题：浅克隆，克隆得到的都是一些引用，会和所引用的对象挂钩

```java
//原型对象  v1
Date date = new Date();
Video v1 = new Video("狂神说java", date);
System.out.println("v1=>"+v1.toString()+"--"+v1.hashCode());
//克隆对象 v2
Video v2 = (Video) v1.clone();
System.out.println("v2=>"+v2.toString()+"--"+v2.hashCode());

date.setTime(123456789);
System.out.println("v1=>"+v1.toString()+"--"+v1.hashCode());
System.out.println("v2=>"+v2.toString()+"--"+v2.hashCode());
```

![image-20211227063536430](designPattern.assets\image-20211227063536430.png)

> 解决：clone他的属性

```java
@Override
protected Object clone() throws CloneNotSupportedException {
    //clone得到的对象
    Video video = (Video) super.clone();
    //clone属性
    video.createTime = (Date) this.createTime.clone();
    return video;
}
```

![image-20211227064336427](designPattern.assets\image-20211227064336427.png)



---

# 以上创建型模式

# 适配器模式（adapter）

> 真实对象（目标对象）。需要被适配的对象。适配器。
>
> 真实对象执行方法需要调用适配器，适配器去调用被适配的对象的具体方法

<img src="designPattern.assets\image-20211227071553122.png" alt="image-20211227071553122" style="zoom: 67%;" />

> 对象适配器。组合
>
> 类适配器。继承
>
> 应用：系统升级，新的接口不适配原先的类，但是还需要使用它内部方法

# 桥接模式（bridge）

> 将类进行拆分，通过组合的方式得到所需要的类，利于拓展

![image-20211227074121174](designPattern.assets\image-20211227074121174.png)



> 两个品牌（Lenovo，apple）。两种产品（desktop,laptop）。

> 品牌的抽象（输出品牌信息）

```java
public interface Brand {
    public void info();
}
```

> 产品组合品牌（这边存在属性使用抽象类）

```java
public abstract class Computer {
    //将品牌组合
    protected Brand brand;
    public Computer(Brand brand) {
        this.brand = brand;
    }
    public void info() {
        brand.info();
    }
}

class DeskTop extends Computer{
    public DeskTop(Brand brand) {
        super(brand);
    }
    @Override
    public void info() {
        super.info();
        System.out.println("台式机");
    }
}
class Laptop extends Computer{
    public Laptop(Brand brand) {
        super(brand);
    }
    @Override
    public void info() {
        super.info();
        System.out.println("笔记本");
    }
}
```

# 代理模式

## 静态代理

![image-20211227093251208](designPattern.assets\image-20211227093251208.png)

> 真实角色抽象接口，真实角色，代理角色

<img src="designPattern.assets\image-20211227093524023.png" alt="image-20211227093524023" style="zoom:67%;" />

> 优缺点：
>
> 1、解耦，符合开闭原则
>
> 2、每一个真实对象对应一个代理对象，类太多

## 动态代理

> 动态代理，三个点：
>
> 1、proxy  调用proxy的静态方法newproxyinstance得到一个代理实例
>
> 2、invocationHandler 反射包下的(reflect) 代理对象的调用处理程序 
>
> ​		作用：1、得到代理实例 	2、执行invoke方法
>
> 3、反射	利用反射得到真实对象的接口列表（此处可实现代理某一类的真实对象）

```java
public class ProxyInvocationHandler implements InvocationHandler {
    private Rent rent;
    public void setRent(Rent rent) {
        this.rent = rent;
    }
    public Object getProxy(){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                rent.getClass().getInterfaces(),this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        seeHouse();
        Object invoke = method.invoke(rent, args);
        money();
        return invoke;
    }
    public void seeHouse(){
        System.out.println("中介代看房屋");
    }
    public void money(){
        System.out.println("中介收钱");
    }
}
```

代理的对象的调用处理程序升级，可代理所有的对象

```java
public class ProxyInvocationHandler2 implements InvocationHandler {
    //被代理的对象
    private Object target;
    public void setRent(Rent rent) {
        this.target = rent;
    }
    public Object getProxy(){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                target.getClass().getInterfaces(),this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        seeHouse();
        Object invoke = method.invoke(target, args);
        money();
        return invoke;
    }
    public void seeHouse(){
        System.out.println("中介代看房屋");
    }
    public void money(){
        System.out.println("中介收钱");
    }
}
```