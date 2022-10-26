## java成神之路读书笔记

> 借鉴地址Gitee Pages 完整阅读:http://hollischuang.gitee.io/tobetopjavaer
>
> 作者：Hollis ，阿里巴巴技术专家，51CTO 专栏作家，CSDN 博客专家，掘金优秀作者， 《程序员的三门课》联合作者，《Java 工程师成神之路》系列文章作者;热衷于分享计算 机编程相关技术，博文全网阅读量数千万。



### 面向对象

> java是一种面向对象的编程语言

#### 面向过程

> 什么是面向过程？

面向过程(Procedure Oriented)是一种以过程为中心的编程思想，是一种自顶而下的编程模式。简单来说，面向过程的开发范式中，程序员需要把问题分解成一个一个步骤，每个步骤用函数实现，依次调用即可。

面向过程的编程语言以诸多流程控制语句来实现一个功能，整体表现为流程化。

**优缺点**

> 优点

流程化，执行效率高

> 缺点

维护困难，复用性差



#### 面向对象

面向对象（Object Oriented），java是一种面向对象的编程语言。在面向对象的开发过程中，回将某件事情进行抽象，将一件事物的方法属性封装到一个类中，通过多个类之间的组合调用来实现某种功能。



#### 面向对象三大基本特征

> 封装、继承、多态

##### 封装

> 将数据和基于数据的操作放在一个不可分割的独立使体内。数据被保护在实体类，尽可能的隐藏内部实现细节，通过暴露一些接口，与外部进行联系。这就是封装。
>
> 不暴露内部实现细节，以及暴露对外接口，要求设置不同访问级别，一般来说，字段(属性)设置为私有的(private)。
>
> 对于访问级别的设置，遵循以下原则，如果不是清楚的知道方法或属性一定会供外部访问，会设置为私有的。

访问级别有以下几种

- public    所有类都可以访问
- protected   受保护的，默认访问级别，同级别包下的类可以访问
- private  私有的，任何其他类都不可以访问，只供其内部访问

优点：

- 减少耦合

> 尽量少暴露内部实现细节，也就是尽量少的供外部访问。

- 易于维护

> 低耦合会给软件带来易于维护的好处



##### 继承

> 继承是一种is -a 关系。
>
> 继承可提高代码复用，必须明确知道父类中的方法必须对所有子类都适用才可以使用，否则尽量避免使用，及使用集成时必须满足李氏替换原则。
>
> 继承是java为我们提供的可以实现代码复用的一种能力。可以拥有现有类的所有属性和功能（包括私有属性和私有方法），并且可以在此基础上进行扩展。

父类引用指向子类实例称为向上转型，向上转型不需要强制转化。对应的向下转型需要强制转化。



##### 多态

> ​	java中的多态指的是同一种操作，作用于不同的实例可以有不同的结果。多态分为编译时多态和运行时多态。
>
> ​	具体表现形式为父类或接口的引用指向子类或实现类的实例。调用父类或接口中定义或声明的方法，会根据传入的不同的子类或实现类来表现不同的逻辑。
>
> ​	多态机制使具有不同内部结构的对象可以共享相同的外部接口。

> 编译期&运行期

编译期指的是，将源代码编译成另一个中间语言，在此期间会做一些代码规范检查，以及编译期间代码优化。

运行期，指的是程序运行在内存中，进行交互。



###### 编译期间多态

> 在编译期间已经明确知道，具体类型，知道调用什么方法。

比如说方法重载、可以通过参数列表的不同确定调用的具体方法。



###### 运行期多态

> 指的是在运行期间才会确认具体类型，才会知道调用的方法，需要`extends``implament`关键字一层一层去找。

比如说使用父类或接口的引用，指向子类或实现类的实例。

```java
public class Demo {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> sonClass = Class.forName("com.roily.booknode.javatogod._01faceobj.extendsiscompile.Son");
        Person son = (Person) sonClass.newInstance();
        son.method1();
        Class<?> daughterClass = Class.forName("com.roily.booknode.javatogod._01faceobj.extendsiscompile.Daughter");
        Person daughter = (Person) daughterClass.newInstance();
        daughter.method1();
    }
}

class Person {
    void method1() {
        System.out.println("method1");
    }
}

class Son extends Person {
    @Override
    void method1() {
        System.out.println("son method01");
    }
}
class Daughter extends Person {
    @Override
    void method1() {
        System.out.println("Daughter method01");
    }
}
```

![image-20220729135114407](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208260159591.png)



#### 重写和重载

> 重写（Overriding）和重载（Overloading）是两个比较重要的概念。

##### 重载

> 指的是在同一个类中，多个方法的方法名称相同而方法签名不同的现象称为重载，这些方法互称为重载方法。
>
> 子类重写父类方法也可以成为重载，此时子类拥有父类的所有方法，所以并不会影响以上对于重载的概念。

方法签名：方法名+参数列表。（也就是方法名相同，参数列表不同才会构成重载）

返回类型不同不会构成重载。

- 方法名相同，参数列表不同
- 可以改变返回类型
- 可以修改访问修饰符
- 可以声明新的检查异常
- 重载可以发生在一个类中，或在子类和父类中



##### 重写

> 重写指的是子类中定义了和父类拥有相同方法签名，且符合重写要求的方法，那么称子类重写了父类的方法。
>
> 接口声明抽象方法，其实现类实现抽象方法，对应方法上可以加上@OverWriting注解，也可以称为重写，更多的称为实现。

```java
public class OverWriting {
    public static void main(String[] args) {
        final Animal dog = new Dog();
        dog.bark();
    }
}
class Animal {
    void bark() {
        System.out.println("动物叫");
    }
}
class Dog extends Animal {
    @Override
    void bark() {
        System.out.println("狗叫");
    }
}
```

`输出：狗叫`

> 这里子类实例指向父类引用，是多态的表现行式，编译期间会去检查父类中是否存在对应调用方法。而运行期间具体需要调用哪个方法，需要根据具体指向的实例来决定



方法重写的条件需要具备以下条件和要求：

> 两同两小一大

- 两同（方法签名相同）
  - 方法名相同
  - 参数列表相同

- 两小
  - 返回类型的范围需要相等或更小（比如父类返回ArrayList子类就不能返回list）
  - 抛出的检查异常范围要比父类被重写方法要小
- 一大
  - 访问级别限制，比被重写方法访问范围要大(即父类是protected的那么子类重写的方法不能申明为private)

> 其他

- 不能重写被final标识的方法
- 重写的前提是继承

```java
class Person {
    void method1(int a, int b) {
        System.out.println("XX");
    }
    ArrayList<Integer> method2() {
        return null;
    }
}
class Student extends Person {
    /**
     * 两同
     * - 方法名和参数列表相同
     */
    @Override
    void method1(int a, int b) {
        System.out.println("XX");
    }
    /**
     * 两小
     * - 返回参数要比被重写方法要小（范围）
     */
    //@Override
    //List<Integer> method2() {
    //    //通过不了编译
    //    return null;
    //}
}
```

> 子类的返回范围比父类的大，通过不了编译，反过来就行



#### 继承&实现

> 继承的关键字`extends`，实现关键字`implements`。



##### 继承

> 通过继承可以拥有父类的所有属性和方法，实现代码的重用。继承可以发生在类与类之间，这个类可以是具体的也可以是抽象的，同时继承也可以发生在接口与接口之间。

> 如果说可以从某个类中抽出来可以供于公共使用的功能，那么就可以抽出一个父类出来，其他类去继承这个父类，以继承的方式来实现对代码的重用。但前提是这个抽出来的这个父类得保持稳定，也就是少量修改，且这个父类得对其他类都得适用。

一般来说不会使用继承来实现重用，特别是继承至具体的类，如果说非得继承可以继承至抽象类。



##### 实现

> 实现发生在类与接口或抽象类之间，如果说一组业务的处理方式是一样的那么就可以制定抽象（制定标准），具体业务去实现定义的抽象

```java
/**
 * 可以实现一个接口
 */
interface IPerson{
    /**
     * 抽象方法
     */
    void method();
}
class Teacher implements IPerson{

    @Override
    public void method() {
        
    }
}

/**
 * 可以是类实现抽象类的抽象方法
 */
abstract class AbstractPerson{
    
    abstract void method();
    
}
class StudentImpl extends AbstractPerson{
    
    @Override
    void method() {
        
    }
}

/**
 * 可以是抽象类实现接口
 */
abstract class AbstractPersonX implements IPerson{
    @Override
    public void method() {

    }
}
```

##### java单继承

> `java`通过`extends`关键字实现继承，且不支持多继承。

为什么

> 菱形问题：假设B和C都继承自A，B和C都继承了父类A的所有属性和方法，如果java支持多继承的话，此刻有一个D继承自B和C，那么类D就同时拥有类B和类C的所有属性和方法，并且类D继承了两份来自于A的属性和方法，拥有同名属性和相同方法签名的方法是通过不了编译的，且如果通过编译，在调用的时候也会产生歧义。

<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282250770.png" alt="image-20220728231808420" style="zoom:50%;" />

##### java可以多实现

> java不支持多继承但是支持多实现

如下例子，我们在InterfaceA和InterfaceB中定义了两个同名的方法，然后使用ClassC实现它们，发现实现类对于相同方法只实现了一次。

```java
/**
 * 但是java可以多实现，且java8之后接口中可以定义default方法
 */
interface InterfaceA {
    void method1();
}
interface InterfaceB {
    void method1();
    
    void method2();
}
class ClassC implements InterfaceA, InterfaceB {
    @Override
    public void method1() {
        System.out.println("method1");
    }
    @Override
    public void method2() {
        System.out.println("method2");
    }
}
```

> 对于接口而言它只是一个标准、抽象，实现类按照约定实现标准。方然也可以指定标准，使用某个接口的引用指向实现类的实例。

```java
@Test
public void test1() {
    InterfaceB classC1 = new ClassC();
    InterfaceA classC2 = new ClassC();
}
```

> 接口中可以定义default方法，且我们可以使用Implement从多个接口中继承得到多个默认方法，特别的如果说两个接口存在相同方法签名的方法，实现类会被要求强制重写同名方法签名的方法来解决菱形问题。

```java
interface InterfaceC {
    default void method1() {
        System.out.println("InterfaceC default1方法");
    }
}
interface InterfaceD  {
    default void method1() {
        System.out.println("InterfaceD default1方法");
    }
}
/**
 * 可以使用implement从多个接口中得到多个default方法，
 * 如果存在菱形问题，会强制要求实现类重写同名方法
 */
class ClassD implements InterfaceC, InterfaceD {
    @Override
    public void method1() {
        InterfaceC.super.method1();
    }
}
```





#### 五大基本原则

> ​	面向对象五大基本原则，指导程序员编码，符合五大基本原则的程序，健壮性、可维护性和可扩展性都大大提高。

##### 五大基本原则，都旨在：

- 高内聚、低耦合
- 面向抽象、接口，而不是面向具体、实现

##### 单一职责

> 适用于方法、接口和类。一个类的职责尽量单一，只有一个引起它的变化。

对于方法而言，我们一般都遵守其单一职责原则

对于接口而言，抽象方法尽量要求要少，如果方法太多可以进行接口拆分

对于类而言，一般来说都不会严格遵守单一职责，比如说有一个类UserService，进行堆用户的增删改查，那么这个类想要严格遵守大一职责，完全可以拆分为四个类。

所以说，单一职责尽量遵守，类、接口、方法不要过于臃肿。在业务要求基础之上，合理遵守。



##### 开闭原则

> 对扩展开放、对修改关闭。

1、对扩展开放，意味着有新的需求时，可以在现有代码上进行扩展，以适应新的变化。

2、对修改关闭，当软件或系统一旦设计完成，可以独立完成工作，而不要其进行任何修改的尝试

> 开闭原则的重点在于，面向接口、抽象编程而不是面向实现、具体编程。
>
> 因为抽象也就是接口相对稳定，接口定义了一套标准，如果说接口添加新的抽象方法，那么就必须修改其实现类，所以说对于修改是关闭的。
>
> 而如果说现在有了一个新的需求，可以通过实现现有接口定义一个新的类，配合多态可以对当前系统功能进行扩展，所以说对扩展开放。

> 而不能面向具体，一般来说会以继承或组合的方式实现具体类的复用。具体并没有一套标准，也就是说父类修改对应逻辑，并不会要求子类修改，也就是对修改开放，违背开闭原则。

##### 里氏替换原则

> 里氏替换原则知道我们如何使用继承，是一种编程思想。
>
> 要求软件实体：子类必须能够替换其基类，并且不改变业务逻辑。



###### 里氏替换原则和继承的关系

- 继承

  > 继承是java提供的一种可以实现代码复用的语法，但是继承是侵入式的，如果在继承的过程中子类重写了父类的方法，那么说明父类的方法并不通用。

- 里氏替换原则

  > 里氏替换原则是一种编程思想，用于在指导我们合理使用继承。只有遵守了里氏替换原则，才可以实现继承复用。



##### 依赖倒置原则

> 面向接口编程，依赖于抽象。
>
> 具体定义：高层模块不依赖于底层模块，二者都依赖于抽象；抽象不依赖于具体，具体依赖于抽象。



##### 接口隔离原则

> 将臃肿的大接口拆分为一个个小接口，可以使用接口继承或实现多个接口的方式来实现多个接口定义的抽象方法。
>
> 如果一个接口过于庞大，或存在一些不必要实现的方法时，这是一种接口污染。



#### 继承和组合

> 继承和组合都是java用于实现代码复用的技术之二。优先考虑组合，尽量避免使用继承。

继承

> 继承(Inheritance)是一种联结类与类的层次模型。指的是一个类(子类、子接口)继承另外一个类(父类、父接口)的功能。并且可以增加自己新的功能的能力。继承是一种`AS-a`的关系。

![image-20220729130759731](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282250283.png)

组合

> 组合(Composition)体现的是整体与部分、拥有的关系，即`has-a`关系。

![image-20220729131252923](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282251014.png)



##### 组合&继承关系

继承是一种侵入式的代码结构，在继承关系中，父类的内部细节对于子类是可见的（继承是一种白盒式代码复用），如果父类的代码逻辑发生改变，那么如果子类调用了父类的类方法，子类的逻辑也会随之修改，甚至出错。（继承是一种编译期概念，在编译器就确认了类与类的关系）

组合是将现有对象进行拼装组合来实现较复杂的业务逻辑，将对象作为内部的一个属性，组合进来的对象其内部实现细节是不可见的（黑盒式代码复用）。（如果说组合进来的是一个接口或抽象类型，那么在编译期无法确认其具体类型，只有在运行期间才会确认，所以一定程度上复用性更高）



###### 对比

|                             组合                             |                         继承                         |
| :----------------------------------------------------------: | :--------------------------------------------------: |
|            不会破坏封装，整体类与局部类之间松耦合            | 破坏封装，子类依赖于父类，表现为父类与父类之间前耦合 |
|         组合进来的可以是一个抽象，因此具有一定扩展性         |                   可通过定义新方法                   |
| 整体类对组合进来的类进行包装，并可以做一些拓展，就是装饰器模式和代理模式 |                                                      |
|             创建整体对象的时候，必须创建局部对象             |           创建子类对象，会自动创建父类对象           |
|                                                              |                                                      |



##### 如何选择

> 多用组合、少用继承。

- 在同等情况下优先选择组合，利于扩展
- 继承也有用处，如果当前类必须要向基类进行向上转型，则可以考虑使用继承



#### 构造函数和默认构造函数

> 构造函数配合`new`关键字，用于创建实例对象和给成员变量赋值。

> 构造函数长什么样子？

构造函数和普通方法很相似，①构造函数名为类名②构造函数不声明返回类型，返回当前类对象

> 构造函数的重载

构造函数根据参数列表的不同可以实现重载。并且可以为特殊属性给与默认值。

```java
class Person{
    int age;
    String name;
    String address;
    Boolean sex;
    
    private Person(int age, String name, String address, Boolean sex){
        this.age = age;
        this.name = name;
        this.address = address;
        this.sex = sex;
    }
    public Person(int age, String name, String address){
        return Person(age,name,address,false);
    }
}
```



> 如果当前类没有构造函数，编译器会自动生成一个无参构造函数。其成员变量会被赋予默认值。

##### 如果没有构造函数

> 会生成默认构造函数

```java
public class ConstructorTest {
    int i;
    String str;
}
```

反编译后：

```java
public class ConstructorTest{
    public ConstructorTest(){
    }
    int i;
    String str;
}
```

##### 如果存在构造函数

> 会使用定义的构造函数，此刻空参构造函数不可用。

```java
class ConstructorTest2 {
    int i;
    String str;
    public ConstructorTest2(int i) {
        this.i = i;
    }
}
```

反编译后：

```java
class ConstructorTest2{
    public ConstructorTest2(int i){
        this.i = i;
    }
    int i;
    String str;
}
```



#### 类变量、成员变量和局部变量

> `java`中如果从，生命周期，作用域和内存角度去看，`java`的变量分为，类变量、成员变量和局部变量。

##### 类变量

> 类变量被`static`修饰，属于类，生命周期等同于类的生命周期，当一个类被类加载器成功加载到方法区，其就已经存在与方法区。当类被卸载的时候也跟着消失。

##### 成员变量

> 成员变量属于实例，生命周期等同于实例，当一个实例被new出来(或反射等其他方式)，会为其赋值，跟随实例存在于堆内存中。当实例对象被回收时，他也跟着消失。

##### 局部变量

> 局部变量存在于栈内存，一般存在于方法参数，循环体或方法中。

```java
public class Demo2 {
    //类变量
    final static String str1 = "abc";
    static String str2 = "abc";
 
    //成员变量
    String str3 = "abc";

    //局部变量
    void method01(String str1) {
        String str2 = "abc";
        for (StringBuilder str = new StringBuilder("abc"); str.toString().equals("abc"); ) {
        }
    }
}
```



#### 访问修饰符

- public  

> 公开的，被public修饰的成员变量和方法对所有类都是可见的，所有类和对象都可以直接访问

- private

> 私有的，被private修饰的成员变量和方法是私有的，只有当前类有访问权限。即便是子类也不可以访问

- protected

> 受保护的，被protected修饰的成员变量和方法是受保护的的，只有当前类和与其处于同一包下的类有访问权限。除非是子类

- default

> 默认的，被default修饰的成员变量和方法是受保护的的，只有当前类和与其处于同一包下的类有访问权限。即便是子类

```java
package com.roily.booknode.javatogod._01faceobj.extendsiscompile;

public class Demo3 {
    public String str1;
    String str2;
    private String str3;
    protected String str4;
}
```

子类不在同一个包下：

```java
package com.roily.booknode.javatogod._01faceobj;

import com.roily.booknode.javatogod._01faceobj.extendsiscompile.Demo3;
public class TestDemo extends Demo3{
    void method1(){
        System.out.println(str1);
        System.out.println(str4);
    }
}
```

> 可见如果不指定属性和方法的访问级别的话，默认为default。



#### java的值传递

> `java`关于关于参数的传递只有值传递，在传递参数的时候会将参数进行拷贝，在方法体中操作的都是拷贝的参数。

##### 形参、实参

> 形参：在定义方法的时候使用的参数，[参数类型+形参名称]，目的是为了接收参数
>
> 实参：在抵用方法的时候，被调方法会被传入一个参数 [参数名]，这个参数就叫实参

```java
/**
 * @param str 形参
 */
void method(String str) {
}
void method2() {
    /**
     * 123  实参
     * str  实参
     */
    method("123");
    String str = "123";
    method(str);
}
```



##### 为什么说java只有值传递

> 对于基本数据类型来说，它只有值的概念，所以对于基本数据类型的值传递没有任何异议。
>
> 对于引用数据类型来说，在对引用类型的参数进行传递的时候，会将参数进行拷贝，在方法体内实际操作的是拷贝的副本，如果我们没有改变引用关系而直接操作属性，是会对原对象有影响的，应为两个引用指向的是同一个对象。

例：

```java
/**
 * 基本数据类型，只有值的概念
 *
 * @param i
 */
void simpType(int i) {
    i = 999;
}

void referenceType1(StringBuilder sb) {
    sb.append("追加");
}

void referenceType2(StringBuilder sb) {
    sb = new StringBuilder();
    sb.append("追加");
}

@Test
public void test1() {
    System.out.println("基本数据类型");
    int i = 1;
    System.out.println("原值：" + i);
    simpType(i);
    System.out.println("修改后：" + i);

    System.out.println("引用数据类型，未修改引用");
    final StringBuilder sb1 = new StringBuilder("123");
    System.out.println("原值：" + sb1.toString());
    referenceType1(sb1);
    System.out.println("修改后：" + sb1.toString());

    System.out.println("引用数据类型，修改引用");
    final StringBuilder sb2= new StringBuilder("123");
    System.out.println("原值：" + sb2.toString());
    referenceType2(sb2);
    System.out.println("修改后：" + sb2.toString());
}
```

![image-20220730124812628](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282251391.png)

- 对于基本数据类型来说，值传递没有异议

原始参数通过值传递给方法。这意味着对参数值的任何更改都只存在于方法的范围内。当方法返回时，参数将消失，对它们的任何更改都将丢失

- 对于引用数据类型来说

也就是说，引用数据类型参数(如对象)也按值传递给方法。这意味着，当方法返回时，传入的引用仍然引用与以前相同的对象。但是，如果对象字段具有适当的访问级别，则可以在方法中更改这些字段的值

> 引用类型传递的时候发生了什么？

void referenceType1(StringBuilder sb)方法

<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282251594.png" alt="image-20220730125529126" style="zoom:67%;" />

void referenceType2(StringBuilder sb)方法

<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282251668.png" alt="image-20220730125718516" style="zoom: 67%;" />



<hr>


### java基础

#### 8大基本数据类型

> Java中有8种基本数据类型分为三大类。

- 字符型

char

- 布尔型

boolean

- 数值型

1.整型：byte、short、int、long

2.浮点型：float、double



##### 取值范围

> 在`java`中整数类型属于有符号类型，第一位用来表示符号0代表整数1代表负数。

比如说byte类型，占1字节8位，那么他的表示范围为：

最大值：0111 1111  (2^7^-1)

最小值：1000 0000  (-2^7^)

> 这里会有一个疑问？ 1000 0000 为什么 表示-2^7^呢？不是 -0么？

第一点：在计算机中，数据的运算是以补码进行的[源码反码补码](https://juejin.cn/post/7092566574816559111)

第二点：为了防止 +0 和-0的出现，约定了 补码 1000 0000代表 -128 且移除 -0概念

以一个找规律的方式解释：

| 原码      | 反码      | 补码         | 值（10进制） |
| --------- | --------- | ------------ | ------------ |
| 0111 1111 | 0111 1111 | 0111 1111    | 127          |
| 0000 0000 | 0000 0000 | 0000 0000    | 0            |
| ......    | ......    | ......       | ......       |
| 1000 0001 | 1111 1110 | 1111 1111    | -1           |
| 1000 0010 | 1111 1101 | 1111 1110    | -2           |
| 1000 0011 | 1111 1100 | 1111 1101    | -3           |
|           |           | 补码不断减一 |              |
| 1111 1111 | 1000 0000 | 1000 0001    | -127         |
| 无法表示  | 无法表示  | 1000 0000    | -128         |

###### 整型

> 取值范围

| 数据类型 | 字节数、位数 | 范围                  |
| -------- | ------------ | --------------------- |
| byte     | 1字节、8位   | 【-128，127】         |
| short    | 2字节、16位  | 【-2^15^, 2^15^-1】   |
| Int      | 4字节、32位  | 【-2^31^  , 2^31^-1】 |
| long     | 8字节、64位  | 【-2^63^,  2^63^-1】  |
|          |              |                       |

> 溢出问题

由于整型的存储空间是有限的，那么就会存在溢出问题

这是因为int只占32位

0111 1111

0111 1111   +

-------------------------

1111 1110（补）=》 1111 1101(反) =》1000 0010(原)   =  -2

```java
/**
 * 溢出问题
 */
@Test
public void test2() {
    final int value = Integer.MAX_VALUE + Integer.MAX_VALUE;
    System.out.println(value);
}
```

![image-20220730161454604](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282251646.png)

###### 浮点数



[定点数&浮点数](https://juejin.cn/post/7126230681465651230/)

java为我们提供了float和double两个浮点数数据类型，分别占4字节32位和8字节64位。

相较于float(单精度),double(双精度)其表示的范围更大，且精度更高。

> 存储结构

float:        1位符号位，8位指数位，23位尾数位

double： 1位符号位，11位指数位，52位尾数位

> 浮点数存在精度问题，对于金额有严格精度要求的业务，不可使用浮点数来表示金额。

**定点数存在的问题？**

所有数据类型都占据一定的内存空间。定点数顾名思义就是小数点固定的数据类型，那么如果使用定点数来表示小数类型，就必须分配整数位和小数位，如果整数位分配空间大小数位内存空间小，数据的范围变大但是精度会降低，反过来整数位分配空间小小数位内存空间大，就会造成精度提升但是范围变小。

**所以java采用浮点数**

浮点数就是小数点会浮动的数据类型，可兼容范围和精度。



##### 自动装箱与拆箱

> 八大基本数据类型自动装箱与自动拆箱。八大基本数据类型都有对应的对象类型，自动装箱拆箱的意思就是在需要基本数据类型需要转化为对应的包装类型的时候不需要程序员主动的去操作，而是编译器会自动帮我们去做。

除了`int`对应的包装类型为`Integer`，``char`对应包装类型为`Character`外其他基本数据类型对应的包装类型都为对应基本数据类型首字母大写。

> `Java`是一种面向对象的编程语言，一切皆对象，为何需要基本数据类型？

基本数据类型，相较于对象类型运算简单。

> 包装类型存在的意义？

- 基本数据类型的包装类型，不仅仅只有值的概念，其扩展了额外的方法(比如equals)。

- 且对于集合框架来说，需要的是对象类型，我们无法将基本数据类型放进去。



###### 装箱&拆箱

> 装箱

```java
int i = 10;
Integer i2 = new Integer(i);
或
Integer i2 = Integer.valueOf(i);
```

> 拆箱

```java
Integer i = new Integer(10);
int i2 = i.intValue();
```



###### 自动装箱拆箱

> 基本数据类型在需要转化为对应包装类型的时候，无需程序员手动操作

```java
Integer i = 10;
int i2 = i;
```

对其进行反编译可以发现确实自动帮我们转化了：

![image-20220731161053533](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282251870.png)

> 还有就是集合的泛型是一个对象类型，但是我们在编码的时候可以直接将基本数据类型放入，因为编译器会帮我们自动装箱。

```java
List<Integer> ints = new ArrayList<>();
ints.add(10);
```

反编译看：

![image-20220731161803912](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282251026.png)

###### 问题

> 自动装箱与拆箱虽然给我们编码带来了方便，但也会有一些问题。

- 对于基本数据类型来说，我们只关心其数值，在自动装箱过后，超过缓存范围的包装类型，必须使用equals判等。不可使用 `==`
- 将包装类型拆箱的过程中，可能会出现空指针异常(NPE)

```java
Integer methodRe(){
    return null;
}
@Test
public void testRe(){
    int i = methodRe();
}
```



##### 基本数据类型的池化技术

> 基本数据类型（除了double、float）都有缓存技术，会缓存一定范围内的对象，原因就是`jvm`认为在此范围内的对象很常用，在需要使用的时候直接去池中拿取，而无需重新创建。

###### 缓存范围

除了`Character`没有负数概念，其缓存范围为：【0,127】,Boolean缓存范围 {true,false}

其他都是：【-128,127】

基本数据类型的包装类型，都存在一个内部类，XXXCache都会有有一个静态代码块，在首次主动引用时会触发初始化。

> 需要注意的是Integer的缓存范围是可配置的，其他的是固定的。

```java
@Test
public void testCache() {

    System.out.println("========char==========");
    Character c1 = 127;
    Character c2 = 127;
    Character c3 = 128;
    Character c4 = 128;
    System.out.println(c1 == c2);//true
    System.out.println(c3 == c4);//false

    System.out.println("========byte==========");
    Byte b1 = 127;
    Byte b2 = 127;
    Byte b3 = -128;
    Byte b4 = -128;
    System.out.println(b1 == b2);
    System.out.println(b3 == b4);

    System.out.println("========short==========");
    Short s1 = 127;
    Short s2 = 127;
    Short s3 = -129;
    Short s4 = -129;
    System.out.println(s1 == s2);
    System.out.println(s3 == s4);

    System.out.println("========int==========");
    Integer i1 = 127;
    Integer i2 = 127;
    Integer i3 = -129;
    Integer i4 = -129;
    System.out.println(i1 == i2);
    System.out.println(i3 == i4);

    System.out.println("========long==========");
    Long l1 = 127L;
    Long l2 = 127L;
    Long l3 = -129L;
    Long l4 = -129L;
    System.out.println(l1 == l2);
    System.out.println(l3 == l4);
  
    System.out.println("========Boolean==========");
    Boolean bb1 = false;
    Boolean bb2 = false;
    System.out.println(bb1 == bb2);
}
```

![image-20220731163829088](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282251176.png)



###### new关键字

> 特别的，如果使用`new`关键字创建包装类型，其不会存放于缓存池中，而是同普通对象一样存放于堆内存中

```java
@Test
public void testCache2() {
    Integer i1 = new Integer(128);
    Integer i2 = 128;
    System.out.println(i1 == i2);
    System.out.println("equals方法：" + i1.equals(i2));
}
```

![image-20220731163644314](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252694.png)



> XXX.valueOf()   和   new  XXX()区别

XXX.valueOf()，通过此方式创建基本数据类型的包装类型，会首先判断是否存在于缓存池中，存在则从缓存池中取出，不存在则调用new

 xxx（）创建对应包装类型，同普通引用类型一样存在于堆内存中。



###### 问题

> 池化技术可有效的节省内存空间，但是也会给我们带来一些问题。对于基本数据类型我们一般来说只关心其数值的大小，并不会去关心其对象具体。所以说对于基本数据类型的判等一般采用equals方法，这样即便数据超过缓存范围也可以准确判断。



###### 谁负责缓存

> java中会有专门的类负责缓存

有ByteCache用于缓存Byte对象

有ShortCache用于缓存Short对象

有LongCache用于缓存Long对象

有CharacterCache用于缓存Character对象

有IntegerCache用于缓存Integer对象



##### 对于boolean属性如何命名及返回值如何定义

> `Boolean`作为实体类的属性的时候如何命名？`success`or `isSuccess`?，`Boolean`作为方法返回参数的时候使用基本类型还是包装类型？



###### boolean作为属性

> 我们测试`Boolean`作为属性？其生成的`getter`和`setter`方法是什么样子的，对RPC框架有什么影响。

存在四种情况：

```java
Boolean success;
Boolean isSuccess;
boolean success;
boolean isSuccess;
```

分别举例：

> 使用Lombok自动生成getter和setter方法，编译查看对应代码

```java
@Data
class BooleanType1{
    boolean success;
}
@Data
class BooleanType2{
    boolean isSuccess;
}
@Data
class BooleanType3{
    Boolean success;
}
@Data
class BooleanType4{
    Boolean isSuccess;
}
```

> 编译后查看：

```java
class BooleanType1 {
    boolean success;
    public boolean isSuccess() {
        return this.success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
class BooleanType2 {
    boolean isSuccess;
    public boolean isSuccess() {
        return this.isSuccess;
    }
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
class BooleanType3 {
    Boolean success;
    public Boolean getSuccess() {
        return this.success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
class BooleanType4 {
    Boolean isSuccess;
    public Boolean getIsSuccess() {
        return this.isSuccess;
    }
    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
```

这里可以发现如果属性是基本数据类型的`boolean`生成的getter和setter方法是:isXXXX()和setXXX();

如果是包装类型生成的getter和setter方法是getXXX()和setXXX()

这里可以发现，如果是基本数据类型`boolean`作为属性的话，属性名success和isSuccess其对应的getter和setter方法是相同的。那么如果我们的属性名是isSuccess的话，在部分RPC框架中，得到的getter方法是isSuccess()，会误认为对应的属性名称是success，会导致获取不到属性，从而报出异常。

> ==所以说对于实体类如果存在Boolean数据类型的属性，使用包装类型。==

###### boolean对序列化的影响

> 使用FastJson  JACKSON、GSON这几个常见JSON序列话对比区别。

> 同样的对于基本数据类型的boolean，对不同的序列话工具会有不同的结果。而对于包装类型则没有影响。

fastjson和jackson是通过反射得到所有的getter方法（getXXX或isXXXX），然后认为 XXXX就是字段名称，并得到对应的值，直接序列化成对应JSON字符串。

Gson则是通过反射，遍历对象对应类的属性，再序列话成json字符串。

```java
@Test
public void test() throws JsonProcessingException {

    Gson gson = new Gson();
    ObjectMapper om = new ObjectMapper();

    BooleanType1 booleanType1 = new BooleanType1(true);
    System.out.println("booleanType1");
    System.out.println("FastJson:boolean success: => " + JSON.toJSON(booleanType1));
    System.out.println("Gson:boolean success: => " + gson.toJson(booleanType1));
    System.out.println("JackSon:boolean success: => " + om.writeValueAsString(booleanType1));

    BooleanType2 booleanType2 = new BooleanType2(true);
    System.out.println("booleanType2");
    System.out.println("FastJson:boolean isSuccess: => " + JSON.toJSON(booleanType2));
    System.out.println("Gson:boolean isSuccess: => " + gson.toJson(booleanType2));
    System.out.println("JackSon:boolean isSuccess: => " + om.writeValueAsString(booleanType2));

    BooleanType3 booleanType3 = new BooleanType3(true);
    System.out.println("booleanType3");
    System.out.println("FastJson:Boolean success: => " + JSON.toJSON(booleanType3));
    System.out.println("Gson:Boolean success: => " + gson.toJson(booleanType3));
    System.out.println("JackSon:Boolean success: => " + om.writeValueAsString(booleanType3));

    BooleanType4 booleanType4 = new BooleanType4(true);
    System.out.println("booleanType4");
    System.out.println("FastJson:Boolean isSuccess: => " + JSON.toJSON(booleanType4));
    System.out.println("Gson:Boolean isSuccess: => " + gson.toJson(booleanType4));
    System.out.println("JackSon:Boolean isSuccess: => " + om.writeValueAsString(booleanType4));
}
```

![image-20220801145946307](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252654.png)

> 对于boolean isSuccess 不同的JSON序列化工具，生成的JSON字符串并不是一样的。那么如果对于同一对象使用不同序列化工具序列化和反序列化会产生什么结果？

```java
@Test
public void testSer() throws IOException {

    BooleanType2 booleanType2 = new BooleanType2(true);
    //使用fastjson序列话
    String jsonStr = JSON.toJSONString(booleanType2);
    System.out.println("json字符串：=》" + jsonStr);
    //分别使用 fastJson 、 GSON  、 JackSon反序列化
    BooleanType2 t1 = JSON.toJavaObject(JSON.parseObject(jsonStr), BooleanType2.class);
    System.out.println("FastJson反序列化后=》" + t1);

    ObjectMapper om = new ObjectMapper();
    BooleanType2 t2 = om.readValue(jsonStr, BooleanType2.class);
    System.out.println("Jackson反序列化后=》" + t2);

    Gson gson = new Gson();
    BooleanType2 t3 = gson.fromJson(jsonStr, BooleanType2.class);
    System.out.println("Gson反序列化后=》" + t3);
}
```

![image-20220801152221308](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252408.png)

> 还是Gson出现的问题，对于同一个类，使用不同的序列化工具进行，序列话和反序列化，对象会产生前后不一致问题。

同样的对于fastjson和jackson来说，会根据success通过反射来找对应得setter方法，将属性set进去。而Gson会通过反射去找success属性，发现没找打，那么就只能赋予默认值false。

> 又一次证明了只能使用success而不可以使用isSuccess

###### Boolean  or   boolean

> 编码得时候使用Boolean  还是 boolean

- 对于实体类的属性，一律使用包装类型
- 对于远程调用的接口来说，必须使用包装类型。避免默认值的出现
- 对于局部变量来说使用基本数据类型

###### 小结

> 对于布尔值如何命名和使用success还是isSuccess。

第一：布尔值命名必须去掉 is

第二：除了局部变量，其他地方一律使用包装类型

#### String

> `String`在`java`中很常用，看似简单，也有很多知识点。

##### 不可变性

对象的不可变性指的是什么？

对象的不可变性指的是在对象创建完成，我们不可以调用方法来修改其属性。



######  现象

> 在编程中我们常常通过等号和加号来"修改"字符串的值，为什么还是不可变得呢？

比如：

```java
String str1 = "abc";
str1 = "123";
//
String str1 = "abc";
str1 += "123"; 
```

这不是修改了么？

> 这里两种方式好像都修改了str1的值，其实是修改了str1的引用，将str1指向了一个新的字符串对象。
>
> 对于字符串相加，回收先得到相加后的结果，创建对应字符串，然后赋予引用。

图示：

<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252745.png" alt="image-20220731210311597" style="zoom:67%;" />

<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252053.png" alt="image-20220731210337273" style="zoom:67%;" />



###### String为什么是不可变的

> 简单理解一下为什么String不可变

打开`String`类的源码，可以发现`String`底层是一个字符数组，且该属性被`final`修饰：

```java
private final char value[];
```

那么此刻自然就想到被`final`修饰的对象不可变。

> 其实被`final`修饰的对象不可变指的是，不可以修改其引用，如果说我可以通过调用对象提供的修改方法那么完全是可以修改的。



那么String类不可变的真正原因是什么呢？

- 首相String类被final修饰，也就是不可以被继承，我们知道继承及侵入式的，不可以继承那么就没有子类可以破坏其不可变性。（不仅仅是String八大基本数据类型的包装类型都是fianl的）
- String类底层是一个字符数组，其作为String的属性，被private修饰，也就是不提供外部访问
- String类底层是一个字符数组，其作为String的属性，被final修饰，不可修改字符数组引用
- 最后一点也是最重要的，String类中未提供任何修改其字符数组的方法（无论是私有的还是公开的），其内部方法返回的都是一个String



###### String真的不可变吗

> 我们直到`Java`提供了一个很强大的机制，就是反射，那么我们是否可以通过反射来破坏String底层数组的private和final呢？

写一个测试案例：可以通过反射来修改被fianl修饰的属性

```java
@Test
public void testFinal() throws NoSuchFieldException, IllegalAccessException {
  TestFinal testFinal = new TestFinal();
  System.out.println(testFinal.sb + ":" + VM.current().addressOf(testFinal.sb));
  final Field sb = testFinal.getClass().getDeclaredField("sb");
  final StringBuilder abc = new StringBuilder("abc");
  //反射破坏不可修改
  sb.setAccessible(true);
  sb.set(testFinal, abc);
  System.out.println(testFinal.sb + ":" + VM.current().addressOf(testFinal.sb));
}
class TestFinal {
    final StringBuilder sb = new StringBuilder("123");
}
class TestFinal {
    final StringBuilder sb = new StringBuilder("123");
}
```

结果很意外，被final修饰的属性其值可以被改变，且其内存地址也发生了改变，也就是sb的引用也别修改了

![image-20220731214248215](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252499.png)

同理我们尝试修改String的字符数组：

```java
/**
 * 我们都知道String其内部是字符数组，且是私有的，那么我们是否可以通过反射修改其私有属性
 */
@Test
public void test3() throws NoSuchFieldException, IllegalAccessException {
    String str = "123";
    System.out.println(str + ":" + VM.current().addressOf(str));

    final Field value = str.getClass().getDeclaredField("value");
    value.setAccessible(true);
    value.set(str, "abc".toCharArray());
    System.out.println(str + ":" + VM.current().addressOf(str));

    String str2 = "123";
    System.out.println(str2 + ":" + VM.current().addressOf(str2));

    String str3 = "abc";
    System.out.println(str3 + ":" + VM.current().addressOf(str3));
}
```

> 结果有令人很惊讶

- 可以修改String的属性字符数组的值，且不会修改其引用
- String str2 = "123";为何值为”abc“。String缓存池中记录着这么一个str = "123"，但是其内字符数组的值为"abc"

![image-20220731215113520](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252628.png)



###### String为什么设计成不可变的

- 缓存池

  > String是很常用的，为了避免频繁创建相同的字符串，JVM特地在堆内存中开辟了一块空间叫字符串缓存池，专门用于缓存已创建的字符串。

  如果说需要的字符串在缓存池中存在，那么直接去缓存池中取即可，不用再去创建。

- 安全

  > 字符串用于存储的内容还是很广泛的，密码、url、账号信息等等，如果说String可以很容易的被改变，那么整个系统奖没有可信度可言了。

- 线程安全

  > 线程安全性问题，只出现在可修改的数据上，String不可变那么自动保证线程安全。

- 拷贝安全

  > 我们知道在深拷贝的时候，需要考虑对象属性的拷贝，以不影响原型对象，而String不可变在拷贝的时候无需考虑他的拷贝。

- hash缓存

  > 当字符串作为哈希实现的key值的时候。在对这些散列实现进行操作时，经常调用hashCode()方法。
  >
  > 不可变性保证了字符串的值不会改变，其哈希值也不会变，只有在首次哈希的时候会计算哈希值，之后会直接去取已计算的哈希值。

  ```java
  /** Cache the hash code for the string */
  private int hash; // Default to 0
  public int hashCode() {
      int h = hash;
      if (h == 0 && value.length > 0) {
          char val[] = value;
          for (int i = 0; i < value.length; i++) {
              h = 31 * h + val[i];
          }
          hash = h;
      }
      return h;
  }
  ```

###### 小结

> 以上提到的String的缓存池技术，hashCode()缓存技术，都旨在与提高性能，因为String是非常常用的数据类型，对于它的性能即便是小小的提升，映射到整个java生态中，也是庞大的提升。



##### substring

> 介绍subString方法的原理，以及在jdk6和jdk6之后版本中的不同之处，jdk6中的substring方法的问题



###### JDK6中substring的实现原理

> String底层是一个字符数组，在jdk6中String有三个成员变量：`char value[]` `int offset` `int count`。分别表示字符数组、起始下标、字符个数。

```java
String(int offset, int count, char value[]) {
    this.value = value;
    this.offset = offset;
    this.count = count;
}

public String substring(int beginIndex, int endIndex) {
    //check boundary
    return  new String(offset + beginIndex, endIndex - beginIndex, value);
}
```

在调用substring方法的时候会返回一个新的string对象，但其内部的字符数组引用任然指向原堆中的字符数组，只不过起始下标和字符数量不同。

图示：

![image-20220731235844909](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252536.png)



###### JDK6中substring存在的问题

> 由于截取的字符串和原字符串引用的是同一个字符数组，如果原字符串很大，但是截取的部分很小，那么就会导致，原来很长的字符串所指向的字符数组即便不会使用也一直会被引用，就会无法回收，导致内存泄漏，内存泄露的堆积会造成内存溢出。 还有就是效率问题，我只需要截取一小段，却引用了整个字符数组。
>
> 解决方式是截取后的字符串重新创建一个字符串。

```java
x = x.substring(x, y) + ""
```



###### jdk7对于sbustring的优化

> 优化方式是调用sbustring方法生成的字符串其内部字符数组的引用，指向一个新创建的字符数组。

```java
//JDK 7
public String(char value[], int offset, int count) {
    //check boundary
    this.value = Arrays.copyOfRange(value, offset, offset + count);
}

public String substring(int beginIndex, int endIndex) {
    //check boundary
    int subLen = endIndex - beginIndex;
    return new String(value, beginIndex, subLen);
}
```

![image-20220801000754715](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252644.png)



##### replcae

```java
//将所有的replacement字符替换为target字符
public String replace(CharSequence target字符, CharSequence replacement) {
    return Pattern.compile(target.toString(), Pattern.LITERAL).matcher(
            this).replaceAll(Matcher.quoteReplacement(replacement.toString()));
}
//将所有的replacement字符串替换为target字符串
public String replaceAll(String regex, String replacement) {
    return Pattern.compile(regex).matcher(this).replaceAll(replacement);
}
//将首个replacement字符串替换为target字符串
public String replaceFirst(String regex, String replacement) {
    return Pattern.compile(regex).matcher(this).replaceFirst(replacement);
}
```



##### 字符串拼接



###### 通过+拼接

> 字符串通过`+`拼接的原理

```java
public void test1() {
  String str1 = "abc" + "123";
  System.out.println(str1);
}

void method(String str1, String str2) {
  final String strX = str1 + "," + str2;
}
```

反编译后：

```java
public void test1() {
  String str1 = "abc123";
  System.out.println(str1);
}

void method(String str1, String str2) {
  (new StringBuilder()).append(str1).append(",").append(str2).toString();
}
```

- 对于编译时期就知道字面量的字符串，进行常量折叠
- 对于编译器不确定的变量，会使用StringBuilder.append拼接



###### 通过concat拼接

> 会重新生成一个字符串对象，其内部的字符数组也是通过ArrayCopy新拷贝出来的。

```java
public String concat(String str) {
    int otherLen = str.length();
    if (otherLen == 0) {
        return this;
    }
    int len = value.length;
    char buf[] = Arrays.copyOf(value, len + otherLen);
    str.getChars(buf, len);
    return new String(buf, true);
}
```



###### StringBuffer&StringBuilder

> 可以使用在这两类对字符串进行拼接，最后toString返回即可

###### 第三方工具类

> StringUtils，可以使用Spring提供的也可以是apache提供的，都是一个用法，将一个String数组或集合，以某个字符分割拼接

```java
@Test
public void testAppendByUtil(){
    String[] value = {"hello", "你好", "hello"};
    String result = StringUtils.join(value, ",");
    System.out.println(result);
}
```

hello,你好,hello



###### String的join方法



```java
@Test
public void testAppendByStr(){
    String join = String.join(",", "hello", "你好", "hello", "4");
    System.out.println(join);
}
```

hello,你好,hello,4



###### 性能对比

```java
@Test
public void testAppend() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start("使用+拼接字符串");//任务说明
    String str1 = "";
    for (int i = 0; i < 50000; i++) {
        //str1 += "a";  拼接代码
    }
    stopWatch.stop();
    System.out.println(stopWatch.getLastTaskName() +"消耗时长："+stopWatch.getTotalTimeNanos());
}
```

```java
使用+拼接字符串消耗时长：1942387300
使用StringBuilder拼接字符串消耗时长：2846001
使用StringBuffer拼接字符串消耗时长：4217800
使用concat拼接字符串消耗时长：5055200
使用StringUtils  join拼接字符串消耗时长：39924299
```

结果是：StringBuilder > StringBuffer  >  concat> StringUtils > `+`

> StringBuffer  append方法基于StringBuilder实现，同时也是同步的，性能差一点点。

> concat每次循环，都会进行数组拷贝，创建新字符串，性能差点。但也是为了保证字符串的不可变性

> StringUtils底层使用的StringBuilder实现，拼接过程存在很多其他操作，回去判断对象是否为空等，性能也差点

> `+`号是我们很常用的，性能却最差，这是为什么呢？

查看使用`+`拼接字符串的反编译后的代码：

> 发现每次循环都会new一个StringBuilder出来，再进行append，性能自然不会很高了。频繁的创建对象，也是对内存资源的浪费。

```java
void append1() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start("使用+拼接字符串");
    String str1 = "";
    for (int i = 0; i < 50000; i++) {
        str1 += "a";
    }
    stopWatch.stop();
    System.out.println(stopWatch.getLastTaskName() + "消耗时长：" + stopWatch.getTotalTimeNanos());
}
```

```java
void append1()
{
    StopWatch stopWatch = new StopWatch();
    stopWatch.start("\u4F7F\u7528+\u62FC\u63A5\u5B57\u7B26\u4E32");
    String str1 = "";
    for(int i = 0; i < 50000; i++)
        str1 = (new StringBuilder()).append(str1).append("a").toString();

    stopWatch.stop();
    System.out.println((new StringBuilder()).append(stopWatch.getLastTaskName()).append("\u6D88\u8017\u65F6\u957F\uFF1A").append(stopWatch.getTotalTimeNanos()).toString());
}
```

###### 小结

> 对于循环体内字符串的拼接禁止使用`+`，采用`StringBuilder`的`append`的方式进行字符串拼接。有并发需求时，使用`StringBuffer`代替`StringBuilder`。



##### StringBuffer & StringBuilder

> String是不可变的，java还为我们提供了两个可变的用于操作字符串的类，StringBuffer  & StringBuilder

StringBuilder和StringBuilder都是AbstractStringBuilder的子类。底层也是字符数组，使用一个成员变量count来表示字符数组已使用的字符数。

```java
char[] value;

int count;
```

> StringBuilder是非线程安全的，StringBuffer是线程安全的（使用Synchronized保证）。





##### String.valueOf  & Intege.toString

> 将一个`Integer`转化为`String`有几种方式？

```java
@Test
public void test(){
    int i = 10;
    String str1 = i + "";
    String str2 = Integer.valueOf(i).toString();
    String str3 = String.valueOf(10);
}
```

> 第一种方式使用`StringBuilder`

```java
String str1 = (new StringBuilder()).append(i).append("").toString();
```

> 第二种和第三种都是使用`Integer.toString()`



##### switch支持String

> jdk7之后`switch`添加了对`String`的支持。
>
> `switch`目前支持的类型有Character, Byte, Short, Integer, String, or an enum，`switch`真正意义上只支持整型，对于`Character`会转化成ASCII码，ASCII是一个`int`类型的数据。`String`会优先通过`hashCode`判断，然后再通过`equals`进行安全检查，`hashCode`也是`int`类型的

###### int&short&byte

代码：

```java
@Test
public void testInt() {
    int i = 10;
    switch (i) {
        case 1:
            System.out.println(1);
            break;
        case 2:
            System.out.println(2);
            break;
        case 3:
            System.out.println(3);
            break;
        default:
            System.out.println(i);
    }
}
```

反编译查看：

> 没什么特别的，switch对int支持很好。对Short和byte也是一样的，不支持long

```java
public void testInt()
{
    int i = 10;
    switch(i)
    {
    case 1: // '\001'
        System.out.println(1);
        break;

    case 2: // '\002'
        System.out.println(2);
        break;

    case 3: // '\003'
        System.out.println(3);
        break;

    default:
        System.out.println(i);
        break;
    }
}
```



###### char

代码：

```java
@Test
public void testChar() {
    char c = 'a';
    switch (c) {
        case 'a':
            System.out.println('a');
            break;
        case 'b':
            System.out.println('b');
            break;
        case 'c':
            System.out.println('c');
            break;
        default:
            System.out.println(c);
    }
}
```

反编译查看：

> 会将char转化成对应的ascii码值，再通过整型switch

```java
public void testChar()
{
    char c = 'a';
    switch(c)
    {
    case 97: // 'a'
        System.out.println('a');
        break;
    case 98: // 'b'
        System.out.println('b');
        break;
    case 99: // 'c'
        System.out.println('c');
        break;
    default:
        System.out.println(c);
        break;
    }
}
```



###### string

代码：

```java
@Test
public void testString() {
    String str = "abc";
    switch (str) {
        case "abc":
            System.out.println("a");
            break;
        case "bac":
            System.out.println("b");
            break;
        case "cab":
            System.out.println("c");
            break;
        default:
            System.out.println(str);
    }
}
```

反编译查看：

> 发现首先获取哈希值，哈希值是整型，然后进行switch，最后使用equals进行安全判断。

```java
public void testString()
{
    String str = "abc";
    String s = str;
    byte byte0 = -1;
    switch(s.hashCode())
    {
    case 96354: 
        if(s.equals("abc"))
            byte0 = 0;
        break;

    case 97284: 
        if(s.equals("bac"))
            byte0 = 1;
        break;

    case 98244: 
        if(s.equals("cab"))
            byte0 = 2;
        break;
    }
    switch(byte0)
    {
    case 0: // '\0'
        System.out.println("a");
        break;

    case 1: // '\001'
        System.out.println("b");
        break;

    case 2: // '\002'
        System.out.println("c");
        break;

    default:
        System.out.println(str);
        break;
    }
}
```



##### 字符串缓存池

> 创建字符串的方式有以下两种方式:

```java
@Test
public void testStrCache(){
    String str1 = "abc";
    String str2 = new String("abc");
}
```

- 第一种方式通过"字面量"的形式赋值，字符串如果在缓存池中不存在，则会创建并放入缓存池
- 第二种方式会将字符串对象当作一个普通的对象类型，放在堆内存中

> 当我们使用字面量创建字符串的时候，jvm会对此字符串进行检查，如果该字符串在缓存池中不存在，则会创建该字符串，并将其放入字符串缓存池；如果该字符串存在，那么直接将缓存池中的字符串对象的引用返回。

```java
@Test
public void testStrCache2(){
    String str1 = "abc";
    String str2 = String.valueOf("abc");//String.valueOf也是字面量，调用toString方法直接返回
    String str3 = "abc";
    System.out.println(str1 == str2);//true
    System.out.println(str2 == str3);//true
    System.out.println(str1 == str3);//true
}
```



> 字符串缓存池在内存中的哪个位置

jdk7之前，字符串缓存池在永久代中。

jdk7中，由于后续版本计划通过元空间代替永久代，所以先将字符串缓存池从永久代移出，暂时放入堆内存。

jdk8中，彻底废除了永久代，字符串常量池由元空间和堆内存共同完成，元空间存的字符串引用，堆内存存的字符串数值。

##### String长度限制？

> `String`存不存在长度限制呢？

- 在编译期间不可以超过 2^16^-1 = 65535

  也就是我们在使用字面量对字符串赋值的时候如果字符串长度大于等于65535，就通过不了编译

- 运行期间限制：不能超过int的范围



##### String的intern

```java
public native String intern();
```

> 这是一个本地方法。
>
> 如果sting没有放入字符串缓存池，则放入缓存池并返回引用。



#### java中的各种关键字



##### transient

> 短暂瞬时的意思，java提供的关键字，用于修饰成员变量。如果一个变量被`transient`修饰，当对象需要序列化传输、或存储时，会忽略该变量。
>
> 当我们不希望对象的某个变量需要被序列化的时候，比如我们定义一个变量，该变量我们只希望它在当前系统中使用，而不希望他在上下游系统传输，可以使用`transient`修饰。

被transient修饰的引用类型也就是对象类型，在被反序列化的时候初始化为null，基本数据类型为默认值int就是0。

> 创建一个对象，注意需要实现序列化接口支持序列化操作。如果存在特殊需求可以重写writeObjec方法和readObject方法。

```java
@Data
class TransientTestClass implements Serializable {
    private static final long serialVersionUID = 9167810647635375505L;
  
    private String str;
    private Integer value;
    private transient String name;
    private transient int age;
}
```

> 将对象序列化持久化到本地

```java
String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";

@Test
public void test1() throws IOException {
    final TransientTestClass transientTestClass = new TransientTestClass();
    transientTestClass.setName("element");
    transientTestClass.setStr("element");
    transientTestClass.setValue(123);
    //序列化到文件
    final ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            new FileOutputStream(new File(filePath, transientTestClass.getClass().getName())));
    objectOutputStream.writeObject(transientTestClass);
    objectOutputStream.flush();
    objectOutputStream.close();
}
```

> 再通过反序列化将文件中的对象读出来，查看其属性值

```java
/**
 * 读出来，使用对象接收看看
 */
@Test
public void test2() throws IOException, ClassNotFoundException {
    final ObjectInputStream objectInputStream = new ObjectInputStream(
            new FileInputStream(new File(filePath, TransientTestClass.class.getName())));
    TransientTestClass transientTestClass = (TransientTestClass) objectInputStream.readObject();
    System.out.println(transientTestClass);
}
```

> 结果也如我们说的一样

![image-20220801235653264](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252970.png)

##### instanceof

> java关键字，类似于一个二元操作符，用于判断`instanceOf`左右两边对象类型是否一致。

```java
@Test
public void test() {
    System.out.println(InstanceofTest.class instanceof Object);
    System.out.println("InstanceofTest.class" instanceof String);
    System.out.println(Integer.valueOf(10) instanceof Integer);

    Object o = Integer.valueOf(10);
    System.out.println(o instanceof String);
}
```

![image-20220802000331345](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252444.png)



##### synchronized

[synchronized](https://juejin.cn/post/7077457448290189348)

> 后续重点看



##### volatile

[volatile](https://juejin.cn/post/7077459563963908109)

> 后续重点看



##### final

[final](https://juejin.cn/post/7077469542946979847)

> `final`是`java`提供的关键字，表示该部分不可修改，可修饰类、方法、变量。

######  final修饰类

> final 修饰类表示该类不可以被继承。一般是类的自我保护，不希望子类对父类造成破坏。

> 比如说`String`和八大基本数据类型的包装类型



###### final修饰方法

> 表示该方法不可以被子类重写，但是可以在本类中重载。



###### final修饰变量

> 被final修饰的变量如果是基本数据类型则其不可变，如果是引用数据类型则其引用地址不可变。

> 作为局部变量

不管是引用类型还是基本数据类型，初始化后，都不可以使用等号重新赋值。

但是引用类型存在修改api，则可以调用修改方法来修改引用所指对象。

```java
@Test
public void test01() {
    final StringBuilder sb = new StringBuilder("abc");
    final int i = 10;
    sb.append("123");
    System.out.println(sb.toString());
}
```

> 作为成员变量

```java
class MemberField {
    /**
     * 被static final修饰，属于类不可变。必须1、在声明的时候赋值 2、或static代码块中赋值
     */
    static final StringBuilder sb1 = new StringBuilder();
    /**
     * 被final修饰，属于实例，不可变。必须1、在声明的时候赋值 2、非static代码块中赋值 3、构造方法赋值
     */
    final StringBuilder sb2 ;
    {
        sb2 = new StringBuilder();
    }
}
```



##### static

> 用于修饰成员变量、方法或代码块，被static修饰的成员变量称为静态成员变量或类变量属于类，被static修饰的代码块称为静态代码块。

###### 静态成员变量

> 也称为类变量，它不属于类的某个实例，它被所有该类的实例共享，因此存在线程安全问题。
>
> 如果类变量没有被private修饰，可以使用”类名.变量名“的方式访问。

###### 静态方法

> 和静态变量一样，静态方法也属于类，以`类名 。方法名`调用，在此期间不必创建类的实例，因此会方便许多。

> 静态方法属于类，和类共生死，在类初始化时候，静态方法就已经有了，也就是说静态方法必须有实现

> 比如说集合工具类返回空集合Collections.emptyList();

> java8支持在接口中定义静态方法

```java
interface IStaticMethod{
    static void method1() {
        Collections.emptyList();
    }
    default void method2(){
    }
}
```



###### 静态代码块

> 静态代码块会在类初始化的时候，将所有静态代码组合成一个`cinit<>`方法，由类加载器执行。一个类在其一个生命周期内只加载一次，所以说对于静态代码块，在类生命周期中只执行一次。

```java
public class AboutStatic {  
    static int a;
    static {
        a = 1;
        System.out.println("静态代码块执行");
    }
    {
        System.out.println("非静态代码块执行");
    }
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new AboutStatic();
        }
    }
}
```

<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282252302.png" alt="image-20220802164043054" style="zoom:67%;" />

###### 静态内部类

> 静态内部类定义于普通类内部，可以和普通类一样使用。
>
> 下面列出类：内部类、静态内部类和匿名内部类写法

```java
public class AboutStaticClass {
    static class StaticClass {
        int a;
        static int b;
    }
    class InnerClass {
        int a;
        //不可以定义静态变量
        //static int b;
    }
    void method() {
        AboutStaticClass.InnerClass innerClass = new AboutStaticClass.InnerClass();
    }
    //匿名内部类写法
     IInterface ia = new IInterface() {
        @Override
        public void method() {
        }
    };
}
class TestClass {
    public static void main(String[] args) {
        AboutStaticClass.StaticClass staticClass = new AboutStaticClass.StaticClass();
        System.out.println(AboutStaticClass.StaticClass.b);

        //报错 非静态内部类的创建依赖于外部类实例
        // final AboutStaticClass.InnerClass innerClass = new AboutStaticClass.InnerClass();

        //ok
        final AboutStaticClass aboutStatic = new AboutStaticClass();
        final AboutStaticClass.InnerClass innerClass = aboutStatic.new InnerClass();
    }
}
@FunctionalInterface
interface IInterface{
    void method();
}
```

非静态内部类和静态内部类的区别：

非静态内部类依赖于外部实例，静态内部类则不需要。



###### 静态导包

> 通过静态导包，在引用静态变量和方法的时候可不再指明ClassName，可大大降低代码可读性。

使用如下格式

```java
import static com.roily.booknode.javatogod._01faceobj.javakeywords.aboutstatic.StaticClass1.method;
```

例子：

```java
public class StaticClass1 {
    static String str = "static str";
    static void method(){
    }
}
//正常情况
public class Test1 {
    //引用静态变量，方法
    void method2(){
        final String staticStr = StaticClass1.str;
        StaticClass1.method();
    }
}
//静态导包
import static com.roily.booknode.javatogod._01faceobj.javakeywords.aboutstatic.StaticClass1.method;
import static com.roily.booknode.javatogod._01faceobj.javakeywords.aboutstatic.StaticClass1.str;
public class Test2 {
    //引用静态变量，方法
    void method2(){
        final String str2 = str;
        method();
    }
}
```



###### 初始化顺序

> (静态/非静态)成员变量和(静态/非静态)代码块的初始化顺序取决于代码顺序。

例子：

```java
public class Parent {
    static {
        System.out.println("Parent 静态代码块");
    }
    {
        System.out.println("Parent 普通代码块");
    }
    public Parent() {
        System.out.println("Parent 构造方法");
    }
}
public class Son extends Parent{
    static {
        System.out.println("Son 静态代码块");
    }
    {
        System.out.println("Son 普通代码块");
    }
    public Son() {
        System.out.println("Son 构造方法");
    }
    public static void main(String[] args) {
        final Son son = new Son();
    }
}
```

![image-20221025173049847](java成神之路(基础).assets/image-20221025173049847.png)

##### super

> 通过super关键字可访问父类成员。
>
> super关键字不可出现在静态环境中（静态方法）

- 访问父类构造函数：super()访问父类构造函数，从而委托父类完成一些初始化操作。
- 访问父类成员：包括属性和方法 



##### const

> const和final相似，用于后期扩展。



#### 枚举

> 枚举类型是java5引入的，由一组固定常量组成的合法类型。

##### 在枚举引入之前如何定义一组常量

> java在枚举引入之前，我们一般会用一组int常量值，来表示一组固定的数据。比如使用1、2、3、4来表示春、夏、秋、冬。

```java
/**
 * 枚举类型一般会被系统共享，所以其访问修饰符一般为public
 */
class Season {
    public static final int SPRING = 1;
    public static final int SUMMER = 2;
    public static final int AUTUMN = 3;
    public static final int WINTER = 4;
}
```

> 可以根据传入的int值来判断对应季节

```java
@Test
public void test1() {
    final int spring = Season.SPRING;
    season(spring);
}
public void season(int value) {
    switch (value) {
        case 1:
            System.out.println("春天");
            break;
        case 2:
            System.out.println("夏天");
            break;
        case 3:
            System.out.println("秋天");
            break;
        case 4:
            System.out.println("冬天");
            break;
        default:
            System.out.println("输入不合法");
            break;
    }
}
```

这种方法称作int枚举模式。存在一些安全问题，就如上面判断季节的方法，default分支是我们不愿意看到的场景，如果说我们不加校验可能会产生问题。并且Season这个类打印出来的也只是一个int值1、2、3、4，表面并不能看出任何的意思。所以说int枚举模式他的安全性和可读性是不可观的。

> 当然了我们也可以使用字符串作为枚举值，但是字符串的比较算法相对来说比较浪费性能，也是不可取的。



##### 定义枚举

> 由于int枚举和字符串枚举存在着缺陷，java5引入了枚举类型`enum type`，接下来我们看如何定义一个枚举。

使用enum声明一个枚举，在枚举类中列举枚举值，使用逗号隔开，尾部使用分号结尾。

```java
enum Season2 {
    SPRING, SUMMER, AUTUMN, WINTER;  
}
```

并且我们还可以为枚举定义属性：

```java
@AllArgsConstructor
enum Season3 {
    SPRING(1, "春天"),
    SUMMER(1, "春天"),
    AUTUMN(1, "春天"),
    WINTER(1, "春天");
    int code;
    String msg;
}
```

##### 特点

- 简约
- 和普通class类一样，枚举类可以单独存在，也可以存在于其他java类中
- 枚举类可以实现接口
- 也可以定义新的属性和方法



##### switch对于枚举的支持

> 使用枚举改造上面代码

```java
public void seasonUseEnum(Season2 season) {
    System.out.println(Season2.SPRING);
    switch (season) {
        case SPRING:
            System.out.println("春天");
            break;
        case SUMMER:
            System.out.println("夏天");
            break;
        case AUTUMN:
            System.out.println("秋天");
            break;
        case WINTER:
            System.out.println("冬天");
            break;
        default:
            System.out.println("输入不合法");
            break;
    }
}

@Test
public void test2() {
    seasonUseEnum(Season2.SPRING);
}
```

> 如此判断季节的方法对于传入参数存在类型限制，不会再有不合法参数的出现。一般来说我们会对枚举添加表示域的属性和对应的描述，方便统一管理。

```java
public void seasonUseEnum(Season3 season) {
    System.out.println(Season2.SPRING);
    final StringBuilder sb = new StringBuilder();
    switch (season) {
        case SPRING:
        case WINTER:
        case AUTUMN:
        case SUMMER:
            sb.append(season.msg);
            break;
        default:
            System.out.println("输入不合法");
            break;
    }
    System.out.println(sb.toString());
}
@Test
public void test3() {
    seasonUseEnum(Season3.SPRING);
}
```



###### jad查看原理

> 可以使用jad 反编译一下，查看一下底层原理

可以得出如下结论：

- 枚举类经过编译器编译后会被当作普通类处理，继承自 `java.lang.Enum`
- 每一个枚举项是一个 `final static`的成员变量。天生是一个单例



```java
final class Season3 extends Enum
{
    private Season3(String s, int i, int code, String msg)
    {
        super(s, i);
        this.code = code;
        this.msg = msg;
    }
    public static final Season3 SPRING;
    public static final Season3 SUMMER;
    public static final Season3 AUTUMN;
    public static final Season3 WINTER;
    int code;
    String msg;
    private static final Season3 $VALUES[];
    static 
    {
        SPRING = new Season3("SPRING", 0, 1, "\u6625\u5929");
        SUMMER = new Season3("SUMMER", 1, 1, "\u6625\u5929");
        AUTUMN = new Season3("AUTUMN", 2, 1, "\u6625\u5929");
        WINTER = new Season3("WINTER", 3, 1, "\u6625\u5929");
        $VALUES = (new Season3[] {
            SPRING, SUMMER, AUTUMN, WINTER
        });
    }
}
```

> 但是要想知道switch对枚举的支持的原理，其实就在构造函数内，会调用super(s,i)。s是String类型为枚举项的字段名称，i为自动生成的编号。
>
> 我们使用jad对switch相关代码反编译一下：

- 首先枚举类中的每一个枚举都是一个单例对象，在使用new 关键字创建实例的时候会为各个实例添加一个编号 ordinal
- 在引用了枚举类的类中，会在static代码块中初始化一个int类型的数组，用于描述各个枚举值对应的编号
- switch还是对int做操作

```java
 {
     static final int $SwitchMap$com$roily$booknode$javatogod$_01faceobj$javakeywords$aboutenum$Season3[];
     static 
     {
         $SwitchMap$com$roily$booknode$javatogod$_01faceobj$javakeywords$aboutenum$Season3 = new int[Season3.values().length];
         $SwitchMap$com$roily$booknode$javatogod$_01faceobj$javakeywords$aboutenum$Season3[Season3.SPRING.ordinal()] int= 1;
         $SwitchMap$com$roily$booknode$javatogod$_01faceobj$javakeywords$aboutenum$Season3[Season3.WINTER.ordinal()] = 2;
         $SwitchMap$com$roily$booknode$javatogod$_01faceobj$javakeywords$aboutenum$Season3[Season3.AUTUMN.ordinal()] = 3;
         $SwitchMap$com$roily$booknode$javatogod$_01faceobj$javakeywords$aboutenum$Season3[Season3.SUMMER.ordinal()] = 4;
     }
 }
public void seasonUseEnum(Season3 season)
{
    System.out.println(Season2.SPRING);
    StringBuilder sb = new StringBuilder();
    switch(_cls1..SwitchMap.com.roily.booknode.javatogod._01faceobj.javakeywords.aboutenum.Season3[season.ordinal()])
    {
    case 1: // '\001'
    case 2: // '\002'
    case 3: // '\003'
    case 4: // '\004'
        sb.append(season.msg);
        break;
    default:
        System.out.println("\u8F93\u5165\u4E0D\u5408\u6CD5");
        break;
    }
}
```









##### 枚举是单例的最佳实践

> 单例的实现方式存在很多，懒汉式、饿汉式、双重检验锁、静态内部类、枚举

单例的设计主要考虑两个问题：

- 延时加载

  > 在希望使用的时候才进行单例创建，在未正真使用不创建。那么双重检验锁、静态内部类符合需求

- 线程安全

  > 单例实现的复杂问题在于需要考虑线程安全问题，同时兼虑性能。懒汉式非线程安全。

1、懒汉式可实现，但非线程安全

2、饿汉式不行，饿汉式单例的创建由类加载器实现，但线程安全

3、懒汉式配合Synchronized可实现，但影响性能(会对访问单例也进行加锁操作,但访问是没有线程安全问题的)

4、双重检验锁可延时加载：是对懒汉式+锁机制的优化。避免读时加锁

5、静态内部类可实现且线程安全，也是类加载器保证的线程安全	

###### 为何枚举是单例的最佳实现？

- 枚举天生单例，且线程安全，枚举作为内部类可实现延时加载。
- 枚举可避免序列化、或反射 破坏单例 （枚举的序列化是定制的，序列化时会将枚举项名记录，反序列化时会根据枚举项名称找到对应枚举项）

以上编写枚举反编译查看枚举中的每一个枚举项都被final static 修饰，且在static代码块中初始化，这也就是饿汉式单例的实现。

###### 尝试使用反射破坏单例

我们知道单例的实现，重点在于构造函数私有化，并提供获取实例的方法，那么我们就来破坏构造方法的私有性

首先写一个单例类：

```java
public class SingleDemo implements Serializable {
    private static final long serialVersionUID = -6489201409969990006L;+
    private static SingleDemo singleDemo;
    //构造方法私有化
    private SingleDemo() {
    }
    public static SingleDemo getInstance() {
        if (null == singleDemo){
            singleDemo = new SingleDemo();
        }
        return singleDemo;
    }
}
```

```java
@Test
public void testSingle() {
    final SingleDemo instance1 = SingleDemo.getInstance();
    final SingleDemo instance2 = SingleDemo.getInstance();
    System.out.println(instance1 == instance2);
}
```

![image-20220826013226826](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282253867.png)

反射破坏单例：

```java
final SingleDemo instance1 = SingleDemo.getInstance();
final Constructor<? extends SingleDemo> declaredConstructor = instance1.getClass().getDeclaredConstructor();
declaredConstructor.setAccessible(true);
final SingleDemo singleDemo = declaredConstructor.newInstance(null);
System.out.println(instance1 == singleDemo);
```

![image-20220826013415047](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282253301.png)

###### 序列化破坏单例

先执行test1，再执行test2

```java
@Test
public void test1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
    final SingleDemo instance1 = SingleDemo.getInstance();
    try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(filePath, "object.txt")))) {
        //将instance写入文件
        objectOutputStream.writeObject(instance1);
        objectOutputStream.flush();
    } catch (IOException e) {
    }
}
@Test
public void test2() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
    final SingleDemo instance1 = SingleDemo.getInstance();
    SingleDemo sngleDemo = null;
    try (final ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(filePath, "object.txt")))) {
        //将instance写入文件
        sngleDemo = (SingleDemo) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
    }
    System.out.println(sngleDemo == instance1);
    System.out.println(sngleDemo);
    System.out.println(instance1);
}
```

![image-20220826014043371](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282253856.png)



###### 枚举可避免以上问题

> 尝试使用反射破坏枚举单例：枚举的构造方法除枚举自定义的还有Enum类中的code。
>
> 会报出`IllegalArgumentException`异常，不可以使用反射创建枚举对象。

```java
@Test
public void test2() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    final Constructor<? extends SignalEnum> declaredConstructor = SignalEnum.SIGNAL_ENUM.getClass().getDeclaredConstructor(String.class,int.class);
    declaredConstructor.setAccessible(true);
    final SignalEnum signalEnum = declaredConstructor.newInstance("signalEnum",2);
    System.out.println(signalEnum == SignalEnum.SIGNAL_ENUM);
    System.out.println(SignalEnum.SIGNAL_ENUM);
    System.out.println(signalEnum);
}
```

![image-20220826015207256](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208260152336.png)

> 尝试使用序列化破坏枚举单例

先执行testx  再执行testy

结果是true，表示序列化不会破坏枚举单例。

```java
@Test
public void testx() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
    final SignalEnum signalEnum = SignalEnum.SIGNAL_ENUM;
    try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            new FileOutputStream(new File(filePath, "object2.txt")))) {
        //将instance写入文件
        objectOutputStream.writeObject(signalEnum);
        objectOutputStream.flush();
    } catch (IOException e) {
    }
}

@Test
public void testy() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
    final SignalEnum signalEnum = SignalEnum.SIGNAL_ENUM;
    SignalEnum signalEnum2 = null;
    try (final ObjectInputStream objectInputStream = new ObjectInputStream(
            new FileInputStream(new File(filePath, "object2.txt")))) {
        //将instance写入文件
        signalEnum2 = (SignalEnum) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
    }
    System.out.println(signalEnum == signalEnum2);
    System.out.println(signalEnum);
    System.out.println(signalEnum2);
}
```

![image-20220826015618559](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282245977.png)

原因：

枚举类型拒绝反射创建实例。

枚举类型在序列话的时候会将枚举项，对应名称序列化，而具体信息不会序列话，在反序列化时，会根据枚举项名称，调用valueOf方法返回枚举项。



#### 接口  & 抽象类



##### 抽象类

- 抽象类使用`abstract`修饰方法使用`abstract`修饰

- 抽象类中国可以有抽象方法，抽象方法只能在抽象类中

- 抽象类不可实例化

##### 接口

- java8之前，接口中不含任何方法实现
- java8之后，接口中可以有默认方法实现，即dedfault修饰的方法
- 接口中的所有字段即方法都是`public`的
- 接口中的字段默认是`static final`的

##### 小结

> 抽象类和接口的选择：当你明确确定一些类需要共用一些方法的时候，可以考虑使用抽象类，除了一些特殊处理(比如模板方法模式)，一律使用接口。接口的扩展比较容易。



#### 常用类



##### Object

> 看一下Object类

![image-20221025101057243](java成神之路(基础).assets/image-20221025101057243.png)



###### equals

> 等价关系

**特点：**

- 自反性     x.equals(x) = true
- 对称性
- 传递性
- 一致性   多次调用equals()方法，条件不变，结果不变
- 与null比较都为false

**equals和==**

- equals()是等价，`==`是完全相等。
- equals()是java方法，只能对象之间进行比较。 `==`随意比较
- equals()返回true，`==`不一定返回true

**实现equals()**

equals()是object中的public方法，任何类继承自object拥有此方法，Object中的equals方法是通过`==`实现的。

我们如何实现？ 

- 首先通过 ==`判断是否指向同一个引用，是则返回true
- 判断类型是否相等，否则返回false
- 向下强转
- 再进行主要逻辑判断

参考String的equals()方法的实现：

```java
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }
    if (anObject instanceof String) {
        String anotherString = (String)anObject;
        int n = value.length;
        if (n == anotherString.value.length) {
            char v1[] = value;
            char v2[] = anotherString.value;
            int i = 0;
            while (n-- != 0) {
                if (v1[i] != v2[i])
                    return false;
                i++;
            }
            return true;
        }
    }
    return false;
}
```



###### hashCode

> hashcode，哈希值也叫散列值，通过散列值可快速寻址，hashCode可进行对象之间对比。相较于equals效率更高。多用于去重情况下的频繁比较，比如HashSet中判断元素是否重复。

两个对象的hashcode和equals有以下特点

- equals()方法返回true，两个对象的hashCode一定相等
- hashCode相等，两个对象的equals()方法不一定返沪true



如何实现hashCode()：

同一个对象的哈希值一定相等，即不可使用随机数生成算法代表哈希值。哈希值一定根据对象特点，均匀散列。

参考String的hashCode实现

```java
public int hashCode() {
    int h = hash;
    if (h == 0 && value.length > 0) {
        char val[] = value;
        for (int i = 0; i < value.length; i++) {
            h = 31 * h + val[i];
        }
        hash = h;
    }
    return h;
}
```

相当于一个数列的和

s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]

理解的话，看成一个31进制的数，每一特点位均匀散列在31进制的数的每一位上，为何选31？因为这是个奇素数。



###### toString

> 默认返回  XXXX@123abc，后面是16进制的散列值表示。
>
> 需定制显示得重写toString

###### clone

> object的clone()方法是protected的，也就是必须重写clone()方法，其他类才可以直接调用。
>
> 重写clone()方法，必须标注cloneable接口。

clone()方法的替代方案：

拷贝构造函数

```java
public class CloneExample {
    private StringBuilder sb;
    private String str;
    public CloneExample(StringBuilder sb, String str) {
        this.sb = sb;
        this.str = str;
    }
    public CloneExample(CloneExample clone) {
        this.str = clone.str;
        this.sb = new StringBuilder(clone.sb.toString());
    }
    public static void main(String[] args) {
        final CloneExample cloneExample1 = new CloneExample(new StringBuilder(), "");
        final CloneExample cloneExample2 = new CloneExample(cloneExample1);
    }
}
```





### 异常处理

> `ThrowAble`类下有两个重要的子类：`Error`和`Exception`,并且这两个子类下面也存在着大量的子类。
>
> `Error`表示系统或硬件级别的错误，由java虚拟机抛出异常，程序员无法处理。
>
> `Exception`表示程序级别的错误，是由于程序设计不完善而出现的问题，程序员必须手动处理

#### 异常类型

 `Exception`主要分两大类：

- 受检异常(checked   exception)
- 非受检异常(unchecked   exception)



##### 受检异常

> 受检异常声明：在对应方法上通过`throws`关键字，声明一个异常。然后此方法在被调用的时候，调用方一定要对其做处理(要么捕获、要么向上抛出)，否则是无法通过编译的。
>
> 所以当我们希望调用者，必须处理一些特殊情况的时候，就可以声明受检异常。

受检异常在io操作中使用的非常频繁，比如说`FileNotFoundException`异常以及`IOException`及其子类。

比如：

```java
public void test1() throws IOException {
    IOUtils.readLines(new FileInputStream("filename"),  StandardCharsets.UTF_8);
}
public void test2()   {
    try {
        IOUtils.readLines(new FileInputStream("filename"),  StandardCharsets.UTF_8);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

查看 IOUtils.readLines()方法：发现此方法声明了受检异常：

![image-20220808104023792](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282250788.png)

##### 非受检异常

> 非受检异常，在编码的时候不用强制捕获，但是如果不捕获，在出现异常的时候就会中断程序的运行。
>
> 一般来说都是运行时异常，为 `RuntimeException`及其子类。
>
> 比如说空指针异常(NPE)、数组下标越界异常(IOE)以及一些我们自定义的运行期间异常。对于非受检异常来说，如果代码编写的合理，这些异常都是可以避免的。

#### 关键字

- throws     方法声明异常

- throw       后跟异常实例显示抛出异常

- try            用来包裹一块可能出现异常的代码块

- catch        跟在try代码后，指定异常类型，并对异常进行处理

- finally        一些代码无论是否出现异常都会执行，可以定义在fianlly代码块李

  

#### 异常处理

> 要么自己try    catch处理
>
> 要么向上抛出，交给调用者处理

#### 自定义异常

> 一般通过继承`RuntimeException`定义一个自定义异常，用于抛出一些错误的业务。

```java
public class MyException  extends RuntimeException{
    
    private final String DEFAULT_ERROR_CODE = "5000";
    private final String DEFAULT_ERROR_MSG = "运行时异常";
    
    String code;
    String msg;
    //someMethod
    public MyException(Throwable cause, String code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }
}
```

#### 异常链

> 是指java在运行期捕获了一个异常，处理的时候，抛出了一个新的异常，所抛出的新的异常包含前一个异常的信息，如此形成一个异常链。

如果抛出的异常不包含前一个异常信息的话，我们就不会清楚的知道这个异常具体出现的原因：

```java
public void test1() {
    try {
        String str = null;
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
    } catch (NullPointerException npe) {
        throw new MyException("5000", "空指针异常");
    }
}
```

![image-20220808111542845](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282250911.png)

如果我们包含前一个异常信息，在异常抛出的时候可以，追溯到肯本原因：

```java
public void test1() {
。。。。
    throw new MyException(npe, "5000", "空指针异常");
。。。
}
```

![image-20220808111646319](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282250767.png)

#### try-with-resources

> java对于资源的操作，比如说io流、数据库连接，这些资源在非常昂贵，必须在使用结束后显示的关闭资源。
>
> 即在finally代码块内调用对应资源的close()方法。

```java
public void test2() {
    BufferedReader bi = null;
    try {
        bi = new BufferedReader(new FileReader("filename"));
        String line;
        while ((line = bi.readLine()) != null){
            System.out.println(line);
        }
    } catch (IOException e) {
        //dosomething
    } finally {
        try {
            IOUtils.close(bi);
        } catch (IOException e) {
            //dosomething
        }
    }
}
```

> java7 开始提供了一个跟好的处理资源的方式：try-with-resources 语句。这是一个类似于语法糖的语法，方便程序员编码，但是经过编译器编译后，都会转化成jvm认识的。

将资源定义在try括号内，便无需我们手动去关闭资源了：

```java
@Test
public void test4() {
    try( BufferedReader bi = new BufferedReader(new FileReader("filename"))) {
        String line;
        while ((line = bi.readLine()) != null){
            System.out.println(line);
        }
    } catch (IOException e) {
        //dosomething
    }
}
```

可以使用jad反编译查看一下：

发现编译器帮我们做了：

![image-20220808114543526](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282250315.png)

#### finally  & return

- ffinally代码块一定会执行么？
- return的结果是否被finally影响？
- return和finally代码执行顺序，孰先孰后？

##### finally代码块不一定执行

> finally代码块不一定会执行

- 当我们的代码在进入try代码块之前就已经return了，那么整个方法就结束了，finally代码块就不会执行
- 当虚拟机强制停止的时候  exit(0),finally代码块就不会执行

例：

以下两种方式finally代码块都不会执行

```java
public StringBuilder method1(Boolean flag) {
    StringBuilder sb = new StringBuilder();
    if (flag) {
        sb.append("方法在try代码块之前return\n");
        return sb;
    }
    try {

    } catch (Exception e) {
        System.out.println("进入try代码块\n");
    } finally {
        System.out.println("finally代码块执行\n");
    }
    return sb;
}
public StringBuilder method2(Boolean flag) {
    StringBuilder sb = new StringBuilder();
    if (flag) {
        System.exit(0);
    }
    try {

    } catch (Exception e) {
        System.out.println("进入try代码块\n");
    } finally {
        System.out.println("finally代码块执行\n");
    }
    return sb;
}

@Test
public void test1() {
    method1(true);
    method2(true);
}
```

##### finally对return结果的影响

> finally代码可能会对return的结果产生影响。
>
> 对于基本数据类型和一些不可变的引用类型return的结果不受finally的影响
>
> 对于可变的提供修改方法的引用类型，return的结果会受到finally的影响

- 对于基本数据类型  和  一些不可变的比如说String

finally代码块执行但是不影响return的结果

```java
public int method3() {
    int i = 0;
    try {
        return i;
    } finally {
        System.out.println("finally代码块执行");
        i += 1;
    }
}
public String method4() {
    String str = "123";
    try {
        return str;
    } finally {
        System.out.println("finally代码块执行");
        str += "abc";
    }
}
@Test
public void test2() {
    int i = method3();
    System.out.println("method3返回结果：" + i);

    String str = method4();
    System.out.println("method4返回结果：" + str);
}
```

![image-20220808130934740](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282253522.png)

- 对于可修改的引用类型(比如说StringBuilder)

finally代码会执行且影响了返回的结果

```java
public StringBuilder method5() {
    StringBuilder sb = new StringBuilder("");
    try {
        return sb.append("123");
    } finally {
        System.out.println("finally代码块执行");
        sb.append("abc");
    }
}

@Test
public void test3() {
    StringBuilder sb = method5();
    System.out.println("method5返回结果：" + sb.toString());
}
```

![image-20220808131219548](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282253387.png)

> 所以说我们可以得出一个结论：

return会记住需要返回结果的字面量信息，对于基本数据类型来说就是值，对于引用类型来说就是引用地址的值。对于基本数据类型和不可变引用类型需要通过`=`等号赋值，那就直接修改了引用，而return所记住的引用指向的对象并没有被修改。那么对于可变引用类型来说，return所记住的引用指向的对象可以在finally中被修改。

##### return和finally代码执行顺序

> 其实在上一个例子中已经有结果了，我们可以发现返回的sb为 123abc。
>
> 所以说可以得出的结论是：
>
> return  的代码执行在finally代码块之前
>
> finally代码执行在return代码之后，在完全return之前



<hr>

### 集合

> 集合相关简单关系如下图，没有列出所有的集合类

![image-20220809103023147](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282253243.png)



#### Iterable

- `Iterable`接口提供了一个获取迭代器的抽象方法，各种集合类去实现它，返回各自需要的迭代器`Iterator`。这些迭代器一般作为各种集合的内部类。

- 一个遍历方法  foreach(Consumer action)。各个集合实现
- 一个获取分离迭代器的方法。①可以split集合，用于可能并行操作的场景。②遍历

以ArrayList为例：

```JAVA
@Test
public void test1() {
    List<String> list = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));

    list.forEach(System.out::print);
    System.out.println("        foreach");

    Spliterator<String> spliterator = list.spliterator().trySplit();
    spliterator.forEachRemaining(System.out::print);
    System.out.println("        spliterator   forEachRemaining");

    Spliterator<String> spliterator1 = list.spliterator();
    while (spliterator1.tryAdvance(System.out::print)) ;
    System.out.println("         spliterator   tryAdvance");
}
```

![image-20220809125359887](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282253517.png)

使用trySplit方法将集合分割为多个小集合：每一次trySplit都会跟新 Spliterator的index属性

```java
public void test3() {
    List<String> list = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
    Spliterator<String> spliterator = list.spliterator();

    Spliterator<String> spliteratorTemp = null;
    while (null != (spliteratorTemp = spliterator.trySplit())) {
        spliteratorTemp.forEachRemaining(System.out::print);
        System.out.println();
    }
    spliterator.forEachRemaining(System.out::print);
}
```

![image-20220809145504844](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282253994.png)

#### collection

> Collection接口中除了一些关于集合状态的方法合一些对集合操作的方法外，还有两个获取流的方法

<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282253664.png" alt="image-20220809150217164" style="zoom:50%;" />

##### 使用stream来对集合进行操作

```java
@Test
public void test1() {
    System.out.println("===============流  转集合===============");
    List<String> list = new ArrayList<>(Arrays.asList("1", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
    List<String> collect1 = list.stream().collect(Collectors.toList());
    collect1.stream().forEach(System.out::print);
    System.out.println();

    System.out.println("===============遍历===============");
    list.stream().forEach(System.out::print);
    System.out.println();

    System.out.println("===============过滤===============");
    List<String> collect2 = list.stream().filter((ele) -> ele.equals("2")).collect(Collectors.toList());
    collect2.stream().forEach(System.out::print);
    System.out.println();

    System.out.println("===============映射===============");
    List<Integer> collect3 = list.stream().map(Integer::valueOf).collect(Collectors.toList());
    collect3.stream().forEach(System.out::print);
    System.out.println();

    System.out.println("===============求和 求平均值===============");
    int sum = list.stream().mapToInt(Integer::valueOf).sum();
    System.out.println(sum);

    System.out.println("===============去重===============");
    List<String> collect4 = list.stream().distinct().collect(Collectors.toList());
    collect4.stream().forEach(System.out::print);
    System.out.println();

    System.out.println("===============判断===============");
    final boolean b1 = list.stream().allMatch(ele -> "2".equals(ele));
    final boolean b2 = list.stream().anyMatch(ele -> "2".equals(ele));
    System.out.println(b1 + " " + b2);
    System.out.println();

    System.out.println("===============获取option===============");
    String s1 = list.stream().findAny().get();
    String s2 = list.stream().findFirst().get();
    System.out.println(s1 + " " + s2);
    System.out.println();

    System.out.println("===============对每一个元素进行操作===============");
    List<String> collect5 = list.stream().peek(ele -> {
      if (ele.equals("2")){
          System.out.println("xxxx");
      }
    }).collect(Collectors.toList());
    collect5.stream().forEach(System.out::print);
    System.out.println();

}
```



#### Collectors

> `Collectors`构造器私有化且未提供获取实例的方法，那么此类无法实例化。这是一个工具类，可以加快我们处理集合的效率。

我们经常需要将一个处理过的Stream转化为集合类，需要调用collect()方法，此方法需要一个参数：Collector，实现Collector接口还是很麻烦的，所以Collectors提供了许多静态方法，给我们构建需要的Collector。

+++以下例子基于着两个集合：

```java
final List<String> list1 = Arrays.asList("a", "ab", "abc", "abcd", "abcd");
final List<String> list2 = Arrays.asList("1", "12", "123", "1234", "1234");
```

##### toList

> `Collector.toList`方法，查看源码发现，默认转化为ArrayList。

```java
List<String> collect1 = list1.stream().filter("a"::equals).collect(Collectors.toList());
```

##### toSet

> `Collector.toSet`方法，查看源码发现，默认转化为HashSet。  转化为元素不重复集合

```java
final Set<String> collect2 = list1.stream().collect(Collectors.toSet());
```

##### toCollection

> 以上的`toList、toSet`方法转化的是特定的集合，那么如果有特殊需求需要转化为自定义集合的话就需要使用`toCollection`方法。

查看源码发现就是自定义集合类型：

```java
final LinkedList<String> collect3 = list1.stream().collect(Collectors.toCollection(() -> new LinkedList<>()));
//lambda表达式写法
final LinkedList<String> collect4 = list1.stream().collect(Collectors.toCollection(LinkedList::new));
```

##### toMap

> 将集合元素转化为map，默认HashMap。`Collectors.toMap()`方法需要两个参数：keyMapper和valueMapper，两个参数都是`Function`接口的实现类，参数是集合元素，返回结果是对应key  value。

```java
final Map<String, Integer> map1 = list1.stream().collect(Collectors.toMap(String::toString, String::length));
```

> 如果转化后的map的key存在重复元素，会报`java.lang.IllegalStateException`异常。需要我们主动合并。也就是`Collectors.toMap`的几个重载

这个合并的大致思路就是，会将存在重复记录的map节点提出来，然后重复记录的key对应的了两个value作为BinaryOperator接口的参数，返回结果类型和两个参数类型都一样。

比如：上面的集合转化成map{ab=2, a=1, abc=3, abcd=4,abcd=4},这个map是存在key重复记录的，是不允许的，那么需要将这个map分为两个map1{ab=2, a=1, abc=3, abcd=4},map2{abcd=4}。然后将两个map对应key重复的记录的value提出来作为BinaryOperator接口apply(T t,T u)的参数，我们这里做一个相加，即apply(4,4) return 4 + 4;。那么最终的结果为 map{ab=2, a=1, abc=3, abcd=8}。

```java
toMap(Function<? super T, ? extends K> keyMapper,
                                Function<? super T, ? extends U> valueMapper,
                                BinaryOperator<U> mergeFunction)
```

```java
final Map<String, Integer> map2 = list1.stream().collect(Collectors.toMap(String::toString, String::length, Integer::sum));
System.out.println(map2);
```

> 还有一个重载，可以自定义map

```java
final Map<String, Integer> map3 = list1.stream().collect(Collectors.toMap(String::toString, String::length, Integer::sum, LinkedHashMap::new));
```

##### collectingAndThen()

> 此方法允许我们对转化后的集合再做一次操作

这里的第二个参数，是一个函数式接口实现类，需要注意 第一个泛型 R是第一步流转集合的结果，也是函数式接口Function的apply(T t)方法的参数，第二个参数RR为apply(T t)方法返回结果，也是最终需要返回的结果，可以是任意的。这里返回集合

```java
Function<R,RR> finisher
```

```java
final List<String> collect5 = list1.stream().collect(Collectors.collectingAndThen(Collectors.toList(),
        (list -> list.stream().filter("abc"::contains).collect(Collectors.toList()))));
collect5.forEach(System.out::println);
```



##### joining

> 将集合内元素拼接成字符串

参数说明：

第一个参数：分割符号

第二个参数：返回结果字符串前缀

第三个参数：返回结果字符串后缀

```java
final String joinResult = list1.stream().collect(Collectors.joining(",", "<", ">"));
System.out.println(joinResult);
```



##### counting

> 统计个数

```java
final Long size = list1.stream().collect(Collectors.counting());
System.out.println(size);
```



##### summarizingDouble/Long/Int()

> 做统计

这里对集合内字符串长度做统计，得出合、最大、最小值

```java
final IntSummaryStatistics intSummaryStatistics = list1.stream().collect(Collectors.summarizingInt(String::length));
System.out.println(intSummaryStatistics.getSum());
System.out.println(intSummaryStatistics.getMax());
System.out.println(intSummaryStatistics.getMin());
```



##### groupBy

> 以一定条件分组，这里以字符串长度分组

```java
//分组
final Map<Integer, List<String>> map = list1.stream().collect(Collectors.groupingBy(String::length, Collectors.toList()));
System.out.println(map);
```

##### partitioningBy

> 特殊的分组，将集合分为两组，key值为boolean

```java
//特殊分组，以boolean作为map的key
final Map<Boolean, List<String>> map1 = list1.stream().collect(Collectors.partitioningBy(ele -> ele.length() > 2
));
System.out.println(map1);
```



#### Set  &  List

> `Set`和`List`接口都是`Collection`接口的子接口，用于存储同一类型的元素。

List：元素按顺序插入，可重复

Set：元素插入无序，不可重复。Set的实现由HashSet、TreeSet，虽然set插入无序但是TreeSet底层原理是红黑树，元素整体上大小有序。



####  ArrayList  &  LinkedList  & Vector

> 这三个类都是`List`的实现类。

##### ArrayList

> `ArrayList`底层是一个可边长数组，数据连续，当容量补不足的时候会进行扩容，扩1.5倍，使用Sysytem.arrayCopy()进行浅拷贝。
>
> ArrayList实现了`RandomAccess`接口，表明支持随机访问，搜索效率高。
>
> `elementData`使用`transient`修饰，优化序列化传输和存储
>
> 如果说在使用ArrayList之前知道需要存入集合的元素大致个数，可以一次性将集合扩容足`ensureCapacity(int minCapacity) `,避免频繁扩容导致降低集合效率。

重要属性：

- elementData 存放元素的数组
- size    集合大小（元素个数）

```java
transient Object[] elementData;
private int size;
private static final int DEFAULT_CAPACITY = 10;
//private static final Object[] EMPTY_ELEMENTDATA = {};
//private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
```



######  优缺点

优点：

- 搜索效率高
- 优化了序列话传输和序列化存储。（重写了WriteObject和ReadObject方法，不对null元素序列化传输）

缺点：

- 对插入不友好

插入默认尾插法：如果插入时容量足够，直接在对应位置赋值即可，但是容量如果不足的化，首先需要扩容，扩容时就必须涉及数组的拷贝，效率自然受影响。

如果说插入位置是程序员指定的，那么需要将该位置及其之后的元素都后移一位，然后再赋值，效率也会受影响。



##### LinkedList

> `LinkedList`除了实现了`List`接口，还实现了`Deque`接口，实现了`offer\peek\poll`等方法，对集合的操作更加灵活。
>
> `LinkedList`底层是一个双向链表，数据不连续，没有容量限制。
>
> 对插入友好，对访问不友好。`LinkedList`不支持随机访问。
>
> 链表的访问效率地下，特别的：如果我们每次访问的元素在链表尾部的时候，那么每次遍历都几乎需要循环整个链表。

重要属性:

- size  集合大小
- first    头节点
- last    尾节点

```java
transient int size = 0;
transient Node<E> first;
transient Node<E> last;
```

###### 优缺点

优点：

- 没有容量限制，添加元素无需考虑扩容，且添加元素只需要修改引用，效率较高

缺点：

- 访问效率低下

##### Vector

> `Vector`的实现和`ArrayList`基本相同，主要存在如下不同处:

- Vector  属于强同步类，而ArrayList非同步类
- Vector默认每次扩容两倍，ArrayList扩容1.5倍
- Arraylist对序列话传输和存储做了优化，而Vector没有

> Vector关于扩容

容量增长步数`capacityIncrement`如果不设置，默认每次扩两倍，如果设置，每次扩容capacityIncrement。

```java
//容量增长步数
protected int capacityIncrement;
//扩容关键代码
int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                                 capacityIncrement : oldCapacity);
```



#### Collections

> `Collections`的构造函数私有化被`private`修饰，不可实例化，除了构造方法外，存在很多被`static`修饰的静态方法，目的在于对集合进行操作。

- 排序

  > 调用的就是List的sort方法

  ```java
  //必须是Comparable的子类
  void sort(List<T> list);
  //可自定义比较器
  void sort(List<T> list, Comparator<? super T> c);
  ```

- 搜索

  > 二分查找。如果支持随机访问且集合不是很大，调用indexedBinarySearch方法，否则调用iteratorBinarySearch方法。

  ```java
  binarySearch(List<? extends Comparable<? super T>> list, T key);
  binarySearch(List<? extends T> list, T key, Comparator<? super T> c);
  ```

  > 返回指定集合，指定元素的出现次数

  ```java
  static int frequency(Collection<?> c, Object o);
  ```

  > 返回目标集合在源集合中首次出现的下标。
  >
  > 返回目标集合在源集合中首次出现的下标。
  >
  > 没有则返回-1

  ```java
  public static int indexOfSubList(List<?> source, List<?> target) ;
  public static int lastIndexOfSubList(List<?> source, List<?> target);
  ```

- 复制集合

  > 将源集合复制到目标集合中

  ```java
  public static <T> void copy(List<? super T> dest, List<? extends T> src);
  ```

- 反转

  > 将集合元素反转

  ```java
  void reverse(List<?> list);
  ```

  

- 洗牌

  > 打乱现有元素顺序，达到`洗牌`效果

  ```java
  void shuffle(List<?> list, Random rnd);
  void shuffle(List<?> list)
  ```

- 交换

  > 这个方法在reverse中也可能会使用到

  ```java
  swap(List<?> list, int i, int j);
  ```

- 填充

  > 填充集合。以某个对象替换集合中的所有元素

  ```java
  void fill(List<? super T> list, T obj);
  ```

- 拷贝

  > 将源集合中的元素拷贝到目标集合。`src`是源集合，`dest`是目标集合。
  >
  > 目标集合的`size`需要大于等于源集合，否则会报出`IndexOutofBoundsException`异常。
  >
  > 拷贝过后目标集合和源集合共享集合内的元素

  ```java
  void copy(List<? super T> dest, List<? extends T> src)
  ```

  ```java
  @Test
  public void test1() {
      final StringBuffer sb1 = new StringBuffer("a");
      final StringBuffer sb2 = new StringBuffer("b");
      final StringBuffer sb3 = new StringBuffer("c");
      final StringBuffer sb4 = new StringBuffer("d");
      final StringBuffer sb5 = new StringBuffer("e");
      final StringBuffer sb6 = new StringBuffer("f");
      final List<StringBuffer> sbSource = Arrays.asList(sb1, sb2, sb3, sb4, sb5);
      final List<StringBuffer> sbTarget = Arrays.asList(sb6, sb6, sb6, sb6, sb6, sb6);
      Collections.copy(sbTarget, sbSource);
      sb1.append("|update|");
  
      sbTarget.forEach(System.out::print);
  }
  ```

- 最大值最小值

  > 获取一个集合的极值，如果元素未实现Comparable接口，需要自定义Comparator。

  ```java
  <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll);
  <T> T min(Collection<? extends T> coll, Comparator<? super T> comp);
  <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll);
  <T> T max(Collection<? extends T> coll, Comparator<? super T> comp);
  ```

- 集合旋转特定距离

  > 什么意思？相当于集合右移  distance % size()。右边被挤出来的元素添加到集合头部。
  >
  > 如果`distance`为正数，整体右移，负数整体左移。

  比如：

  ```java
  @Test
  public void test1(){
      List<Integer> integers = Arrays.asList(0,1, 2, 3, 4, 5, 6, 7, 8, 9);
      Collections.rotate(integers,5);
      integers.stream().forEach(System.out::print);
  }
  ```

  ![image-20220811130849708](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282253438.png)

  ```java
  public static void rotate(List<?> list, int distance);
  ```

- 转化集合

  > 注意转化为不可变集合后，源集合任然可以进行修改操作并且可以直接影响到,不可变集合的不可变性。因为`Collections`转化不可变集合的操作是将源集合作为转换后不可变集合的属性。

  ```java
  //将目标集合转化成不可变集合，如果调用修改Api则会报出UnsupportedOperationException异常
  public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> c);
  public static <K,V> Map<K,V> unmodifiableMap(Map<? extends K, ? extends V> m);
  //转化为受检查的集合  在添加时会Class.isInstance(o)判断
  <E> Collection<E> checkedCollection(Collection<E> c,Class<E> type);
  //转化为同步集合
  synchronizedCollection(Collection<T> c);
  ```

- 创建集合

  > 返回的集合类型没有实现add等添加方法，如果做添加元素操作，会报出`UnsupportedOperationException`异常

  ```java
  //创建只有一个元素的集合
  public static <T> Set<T> singleton(T o);
  ```

  > 返回空集合,返回的集合类型没有实现add等添加方法，如果做添加元素操作，会报出`UnsupportedOperationException`异常

  ```java
   <T> Set<T> emptySet();
  ```

  > 生成只有一个元素的集合，该集合不可变

  ```java
  //set
  <T> Set<T> singleton(T o);
  <T> List<T> singletonList(T o);
  <K,V> Map<K,V> singletonMap(K key, V value);
  ```

  > 生成一个由指定对象的 n 个副本组成的不可变列表

  ```java
  //n拷贝个数      o 集合元素
  <T> List<T> nCopies(int n, T o);
  ```

  > 生成一个线程安全的集合
  
  `Collections.SynchronizedCollection`和`Vector`的区别：
  
  ①两者实现同步的关键就在于使用`Synchronized`关键字实现，而`Vector`大部分代码使用的是同步方法，也就是锁的`this`。而`Collections.SynchronizedCollection`可以指定锁的对象`mutex`，如果不传默认锁的还是`this`。
  
  ②`Vector`的底层是一个对象数组，在其构造函数的重载中，可以直接将一个`Collection`转化为`Vector`，但是如果被转化的集合是一个`LinkedList`的时候，需要改变其底层数据结构，也就是需要调用`toArray()`方法，将链表转化为数组。而`Collections.SynchronizedCollection`是不需要改变集合底层结构的，同样的被转化的集合作为`Collections.SynchronizedCollection`的内部属性。
  
  ```java
  <T> Collection<T> synchronizedCollection(Collection<T> c);
  //mutex 作为对象监视器。如果主动设置，则锁的是mutex。否则默认锁的this(这也是和Vector的区别)
  <T> Collection<T> synchronizedCollection(Collection<T> c, Object mutex);
  ```
  
  

#### Set如何保证元素不重复

> `Set`的实现主要有两个，一个是`HashSet`，一个是`TreeSet`。特点是元素不重复

- HashSet

  `HashSet`基于`HashMap`实现，`HashMap`的`key`值不重复，`HashSet`的元素就是`HashMap`的key值。只能存在一个null元素（`hashMap`中，null的hash值为0）。

  其判重方法是：首先使用`hash`值(散列值)判断，如果散列值不相等那么直接就不相等，如果散列值相等再使用`equals()`方法进行安全校验。原因在于：哈希值的比较效率高于对象的`equals()`方法。

- TreeSet

  `TreeSet`基于`TreeMap`实现，`TreeMap`底层是一颗红黑树(红黑树是对平衡二叉查找树的优化)。其内不可存储null元素(会报NPE异常)。

  其判重方式是：①如果元素实现了`Comparable`接口，直接使用元素的`compareTo()`方法。②如果元素没有实现`Comparable`接口必须指定`Comparator`，调用`Comparator.compare()`方法。

  都是子节点与父节点进行比较，比较结果小于0作为左孩子，大于0作为右孩子，等于0替换父节点value值。



#### hashMap & hashTable

> `HashTable`是一个较为古老的类，是一个线程安全的key - value键值对数据类型，其有一个`Property`子类，一般作为配置文件的工具类。
>
> `HashMap`可以认为是单线程环境下`HashTable`的替代品，其在避免哈希冲突、查找效率上都比`HashTable`要强。
>
> 一般情况下，`HashTable`已被弃用，单线程环境下使用`HashMap`，多线程环境需要保证线程安全的情况下使用`ConcurrentHashMap`。

##### 区别

- 线程安全

  > `HashMap`非同步，多线程环境下需要同步，则使用`ConcurrentHashMap`。
  >
  > `HashTable`同步，使用`Synchronized`保证，同步方法，锁的是`this`。

- 继承关系

  > `HashMap`是`AbstractMap`的子类，并实现了`Map`接口。
  >
  > `HashTable`是`Dictionary`的子类(JDK1.0提出的)，并实现了`Map`接口。

- 是否允许null值

  > `HashMap`键和值都可以添加null值，null值作为键只能出现一次(hashMap的`hash()`方法，null对应的是0)，null值作为value可以出现多次(即多个键值对应value都是null)。
  >
  > `HashTable`键和值都可以不可以为null值，在`put`的时候，会对值进行空校验，会调用键的`hashCode()`方法。

- 容量Capacity & 扩容机制

  > `HashMap`容量默认`1<<4`(16)，每次扩容2倍，为2^n^。
  >
  > `HashTable`容量默认11，每次扩容2n+1。

- hash值

  > `HashMap`对`key`值的`hashCode`进行了哈希扰动，有效的降低了`HashMap`的哈希冲突。
  >
  > `HashTable`直接使用的键的`hashCode`

- 遍历方式

  > `HashMap`的遍历可以通过获取`EntryIterator、ValueIterator、KeyIterator`这些迭代器来遍历。
  >
  > `HashTable`的遍历：对于`key 和  value`来说，可以通过`keys()和elementa()`方法获取`Enumeration`进行遍历，对于`Map.entry`可以获取`EntrySet`再进行遍历。
  >
  > Iterator支持fast - fail，而`Enumeration`不支持。

##### hashTable相关算法

> 先了解`hashTable`的相关算法，相比于`HashMap`容易理解，同时后续和`HashMap`比较着理解，可以感受`HashMap`设计的巧妙。

> 底层是数组+单向链表

- 如何确定元素散列下标

> hashTable使用的元素的哈希值，通过取余的方式获取散列下标。关键代码如下

哈希值和0x7FFFFFFF按位与，是为了防止负数的出现。和tab.length除取余得到的是 (0 -  tab.length)，随机散列到数组中。

```java
index = (hash & 0x7FFFFFFF) % tab.length;
```

- 扩容（reHash）

> 首先了解两个属性`loadFactor`和`threadshold`分别为加载因子和临界值。加载因子默认为0.75，算上哈希冲突，所以说hashmap的存储效率一般不会超过百分之五十。

存在如下关系：
$$
threadshold = table.length * loadFactor
$$


> 扩容的目的是为了避免频繁哈希冲突，扩容的时机是当集合元素个数`count`大于临界值`loadfactor`时，进行扩容，扩为2倍加一，`loadfactor`也随之修改。
>
> 扩容的方法是，数组容量扩充，新建一个扩充后长度的数组，将旧数组元素放入新创建的数组。

- 序列化

> 序列话传输的时候会剔除空节点，同时这也是必须的，因为`hashTable`不支持key  或  value中任意的null值。



##### hashMap相关算法

> `hashMap`底层是数组 + 单向链表 + 红黑树
>
> `hashMap`是对单线程下`hashTable`的优化。
>
> 主要通过  ①位运算  ②截断链表 ③转换红黑树   进行优化

- hashMap的capacity

> `HashMap`的容量为 2^n^。

通过一系列的移位运算和或运算找到任意数离其最近大于它的2^n^值，比如  5 ---> 8   、 11 -->  16、33 ---> 64。

基本思想就是：

5 的二进制表示为 0101，将高位第一位不为0的及所有低位置为1，即  0111，转换后加一，即 1000   => 8

11的二进制表示为 1011，将高位第一位不为0的及所有低位置为1，即  1111，转换后加一，即 0001 0000   => 16

- 如何确定散列下标

> 前提是：`HashMap`的容量为2^n^。散列下标为哈希值和table.length - 1按位与

`hashTable`是通过除取余的方式，同样的`hashMap`也是，也可以通过除取余的方式。

但是当数组的长度为2^n^时候，除取余  =  hash()  &  table.length - 1。

- hashMap的扰动函数

> `hashTable`对key值的哈希值没做任何处理，会出现一个问题，就是如果key的哈希算法很糟糕的话，会很频繁的出现哈希冲突，通过拉链法解决冲突的话，链表将会拉的很长，且`hashTable`没有截断链表和转化红黑树的操作，如此查询效率将会降低。
>
> 所以针对如上`hashMap`做了优化，可以理解为`hashMap`不信任我们写的哈希算法，它自己会做一层处理，基本思想是将高16位和低16位进行按位异或(哈希值是int类型32位)，如此低16位既代表了整个哈希值的特征，在使用扰动后的哈希值来确定散列下标，可有效降低哈希冲突。

- 扩容

> hashMap的扩容方法是`reSize()`，扩容为2倍。
>
> 此方法不仅仅只做了扩容操作，它还会将链表缩短(缩短一倍)，比如在原数组(数组长度为len)下标j处有一个链表长度为5，经resize方法后，会将此链表拆为两份，分别为长度为2和长度为3的链表，分别放在 新数组(2 + len)j处和len + j处。

- 转化红黑树

> 当哈希表的链表过长会影响到查询的效率，所以需要转化为红黑树，利用红黑树的有序性质，可以使得查询效率逼近二分查找。

转化红黑树的条件是：当链表长度等于8且table节点数组长度大于等于64

如果不满足table节点数组长度大于64的话，会进行扩容处理，因为扩容存在缩短链表的操作。

##### loadfactory

> 加载因子为何默认为0.75，在hashMap中容量为2^n^，乘3/4刚好没有小数位



##### 尽量设置初始容量

> 在创建hashMap的时候建议一次性申请足够多的容量，避免频繁扩容，因为每次扩容都需要重建hash表。

那么初始容量设置多少合适呢？

需要考虑装载因子，hashMap的有效容量为实际容量的0.75，所以设置初始化容量的时候申请大小需要大于实际需要大小。 

在hashMap的putall()方法中就有类型实现：

```java
//s 为需要容量大小， ft为实际申请容量大小
float ft = ((float)s / loadFactor) + 1.0F;
```

在guaua包下也有类型实现：

```java
public static <K extends @Nullable Object, V extends @Nullable Object>
    HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
  return new HashMap<>(capacity(expectedSize));
}
static int capacity(int expectedSize) {
    if (expectedSize < 3) {
      checkNonnegative(expectedSize, "expectedSize");
      return expectedSize + 1;
    }
    if (expectedSize < Ints.MAX_POWER_OF_TWO) {
      // This is the calculation used in JDK8 to resize when a putAll
      // happens; it seems to be the most conservative calculation we
      // can make.  0.75 is the default load factor.
      return (int) ((float) expectedSize / 0.75F + 1.0F);
    }
    return Integer.MAX_VALUE; // any large value
}
```

#### Stream

> 可以使用`Stream`来处理集合，结合`Lambda`表达式和函数式编程可以编写出简洁、高效的代码

##### 特点

- 无存储

  > `Stream`不是一种数据结构，它并不存储数据，它只是数据源的一个视图(操作集合的说明书)，数据源可以是一个集合或数组。

- 简洁

  > `Stream`的特性就是为函数式编程而生，结合函数式编程可以编写出简洁高效的代码。

- 惰式执行

  > `Stream`上的操作不会立刻执行，而是在被消费的时候才会真正执行

- 可消费性

  > `Stream`可被消费，且只能被消费一次。一旦遍历过就会失效。想要在此操作必须重写生成流。

  > 如下test2方法stringStream已被遍历过，再次对其操作会报出`java.lang.IllegalStateException: stream has already been operated upon or closed`错误。
  >
  > 必须如test3生成新的流。

```java
@Test
public void test2() {
    final Stream<String> stringStream = Stream.of("1", "2", "3");
    stringStream.filter("2"::equals);
    stringStream.forEach(System.out::println);
}
@Test
public void test3() {
    final Stream<String> stringStream = Stream.of("1", "2", "3");
    final Stream<String> stringStreamFilter = stringStream.filter("2"::equals);
    stringStreamFilter.forEach(System.out::println);
}
```



##### Stream操作

> 对于`Stream`流的处理主要有三种关键性操作：创建流、中间操作、最终操作

###### 创建流

- 通过集合类的stream方法创建流
- 使用Stream.of(T t)创建流
- 使用Arrays.stream(T[] t)创建流 。 Stream.of(T ...t)底层就是使用此方式

```java
//通过集合类创建流
final Collection<String> strings = Arrays.asList("1", "2", "3");
final Stream<String> stream = strings.stream();

//Stream创建流
final Stream<String> stringStream = Stream.of("1", "2", "3");
final Stream<String> a = Stream.of("a");

//Arrays.stream(T[] t)
final Stream<String> stream1 = Arrays.stream(new String[]{"1", "2", "3"});
```



###### 中间操作

> 对`Stream`做处理，包括过滤、映射、排序等

filter参数为Predicate，可使用多个Predicate配合and  or  组合成一个Predicate。

| 操作(Stream opration) | 说明                         | 参数                                    |
| --------------------- | ---------------------------- | --------------------------------------- |
| filter                | 过滤                         | Predicate<? super T> predicate          |
| map                   | 映射                         | Function<? super T, ? extends R> mapper |
| limit、skip           | 限制                         | long maxSize                            |
| sorted                | 自然排序或指定比较器         | Comparator<? super T> comparator        |
| distinct              | 使用元素的equals去除重复元素 |                                         |
| flatMap               | 合并流                       | Function<? super T, ? extends R> mapper |

###### 最终操作

> `Stream`是集合或容器的视图，是对集合或容器的操作描述，但是如果我们想要得到结果的话，就需要使用最终操作来将流转化为我们想要的结果。遍历、统计(个数)、转化集合等。

| 操作    | 说明     | 参数                                 |
| ------- | -------- | ------------------------------------ |
| foreach | 遍历     | Consumer<? super T> action           |
| count   | 计数     |                                      |
| Collect | 转化集合 | Collector<? super T, A, R> collector |
| Reduce  | 聚合操作 |                                      |
|         |          |                                      |

##### Stream转化

> Stream转化为IntStream、LongStream。。。。

```java
final IntStream intStream = stream.mapToInt(StringBuffer::length);
final DoubleStream doubleStream = stream.mapToDouble(StringBuffer::length);
final LongStream longStream = stream.mapToLong(StringBuffer::length);
```



##### 例子

```java
System.out.println("===============流  转集合===============");
List<String> list = new ArrayList<>(Arrays.asList("1", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
List<String> collect1 = list.stream().collect(Collectors.toList());
collect1.stream().forEach(System.out::print);
System.out.println();

System.out.println("===============遍历===============");
list.stream().forEach(System.out::print);
System.out.println();

System.out.println("===============过滤===============");
List<String> collect2 = list.stream().filter((ele) -> ele.equals("2")).collect(Collectors.toList());
collect2.stream().forEach(System.out::print);
System.out.println();

System.out.println("===============映射===============");
List<Integer> collect3 = list.stream().map(Integer::valueOf).collect(Collectors.toList());
collect3.stream().forEach(System.out::print);
System.out.println();

System.out.println("===============求和 求平均值===============");
int sum = list.stream().mapToInt(Integer::valueOf).sum();
System.out.println(sum);

System.out.println("===============去重===============");
List<String> collect4 = list.stream().distinct().collect(Collectors.toList());
collect4.stream().forEach(System.out::print);
System.out.println();

System.out.println("===============判断===============");
final boolean b1 = list.stream().allMatch(ele -> "2".equals(ele));
final boolean b2 = list.stream().anyMatch(ele -> "2".equals(ele));
System.out.println(b1 + " " + b2);
System.out.println();

System.out.println("===============获取option===============");
String s1 = list.stream().findAny().get();
String s2 = list.stream().findFirst().get();
System.out.println(s1 + " " + s2);
System.out.println();

final List<StringBuilder> sbs = Arrays.asList(new StringBuilder(), new StringBuilder());
System.out.println("===============对每一个元素进行操作===============");
List<StringBuilder> collect5 = sbs.stream().peek(ele -> ele.append("123")).collect(Collectors.toList());
collect5.stream().forEach(System.out::print);
System.out.println();

System.out.println("==============合并多个流===============");
final List<List<Integer>> lists = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(1, 2));
final List<Integer> collect = lists.stream().flatMap(Collection::stream).collect(Collectors.toList());
collect.forEach(System.out::print);

//对集合进行聚合求值，e1首次为集合第一个参数，之后为return结果，e2为 2、3.。。。。
final Optional<Integer> reduce1 = list1.stream().reduce(Integer::sum);
System.out.println(reduce1.get());

//reduce的重载，返回结果为T，e1首个为100
final Integer reduce2 = list1.stream().reduce(100, Integer::sum);
System.out.println(reduce2);

//非并行流，只有一个线程，第三个参数BinaryOperator，可以忽略
final StringBuilder reduce3 = list1.stream().reduce(new StringBuilder(), StringBuilder::append, (t, u) -> {
    int i = 0;
    return t;
});
```



##### 并行 & 非并行

> 并行流会启动多个线程对集合进行操作，因为结果分散到各个线程中，最终对结果进行汇总。
>
> 非并行流，单线程。
>
> 一般来说非并行流够用

现象：

```java
/**
 * 三个参数
 * Supplier 供应商，提供返回对象
 * BiConsumer  消费者  操作返回对象和流中的元素
 * BiConsumer  消费者  对并行流结果进行操作
 */
final HashMap<String, Integer> map2 = list1.parallelStream().collect(HashMap::new, (mapx, ele) -> {
    mapx.put(ele.toString(), ele);
}, (mapa, mapb) -> {
});
System.out.println("并行流，未addall" + map2);
```

解决：

```java
final HashMap<String, Integer> map3 = list1.parallelStream().collect(HashMap::new, (mapx, ele) -> {
    mapx.put(ele.toString(), ele);
}, (a1, a2) -> {
    a1.putAll(a2);
});
System.out.println("并行流，addall" + map3);
```

![image-20220923135653257](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209251249397.png)



#### 集合工具类

> 许多开源机构为我们提供了操作集合的工具类。

##### apache

> Apache.commons下的commons-collectionsX包对java集合框架(java collection framework)做扩展。

![image-20220818094842448](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282254261.png)

- Bag -  简化了一个对象在集合中存在多个副本的操作
- BidiMap -  提供双向映射，可通过键查找值，也可以通过值查找键
- Iterators - 方便迭代
- Transforming Decorators   在添加元素时，修改集合元素
- CompositeCollection  需要统一处理多个集合时可用

###### Bag

> `Bag`简化了一个对象存在多个副本的操作。

HashBag  & TreeBag

> hashBag & TreeBag的继承关系如下图所示：
>
> TreeBag 相较于HashBag多实现一个接口 ： SortedBag即表现为一个有序的bag。
>
> HashBag其内封装了一个HashMap，key是元素，value是元素个数。
>
> TreeMap其内封装了一个TreeMap，key是元素，value是元素个数。

![image-20220818151047171](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282254561.png)



> hashBag&TreeBag的创建。

```java
public HashBag() {
    super(new HashMap<E, MutableInteger>());
}
public HashBag(final Collection<? extends E> coll) {
    this();
    addAll(coll);
}

private transient Map<E, MutableInteger> map;
protected AbstractMapBag(final Map<E, MutableInteger> map) {
    super();
    this.map = map;
}
```

> hashBag&TreeBag基本使用

```java
/**
 * hashBag底层是一个hashMap 元素是key值，添加个数是value
 * 可以通过add(object,nCopies)方法为每个元素添加n个副本
 * 如果add两次则会跟新value值（加一）
 */
StringBuilder sb1 = new StringBuilder("a");
StringBuilder sb2 = new StringBuilder("b");
StringBuilder sb3 = new StringBuilder("c");
Bag<Object> hashBag = new HashBag<>();

hashBag.add(sb1);
hashBag.add(sb1);
hashBag.add(sb2, 3);
hashBag.add(sb3, 3);
System.out.println("Bag+元素个数:" + hashBag.size());
System.out.println("Bag中sb1个数:" + hashBag.getCount(sb1));
String result1 = hashBag.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
System.out.println("HashBag 内容：" + result1);
//可以使用Collection作为构造方法参数
Bag<Object> hashBag2 = new HashBag<>(Arrays.asList("1", "2", "3"));
String result3 = hashBag2.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
System.out.println("hashBag2 内容：" + result3);

/**
 * TreeBag：底层封装了一个TreeMap。
 * 其实可依发现HashBag中的元素是无序的，那么TreeBag就是一个有序的Bag
 */
TreeBag<String> treeBag = new TreeBag<>(Arrays.asList("99", "2", "3", "7"));
String result2 = treeBag.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
System.out.println("TreeBag 内容：" + result2);
```



CollectionBag

> `CollectionBag`没有无参构造，必须依赖一个`Bag`类型的参数。`CollectionBag`只是对`Bag`的封装，任何操作实际上操作的是封装的Bag。

```java
/**
 * CollectionBag的创建依赖于现有Bag，不可使用Collection作为构造方法参数
 *
 * 其内方法是对Bag的一层封装，实际调用的还是Bag的方法
 */
Bag<Object> collectionBag = CollectionBag.collectionBag(hashBag);
String result4 = collectionBag.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
System.out.println("CollectionBag的内容：" + result4);

Bag<Object> collectionBag2 = CollectionBag.collectionBag(treeBag);
String result5 = collectionBag2.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
System.out.println("CollectionBag2的内容：" + result5);
```



PredicatedBag & PredicatedSortedBag

> `PredicatedBag`的构造器依赖一个现有Bag和一个`Predicate`，可以对加入进来的元素进行限制，比如不允许添加空元素。

```java
/**
 * 创建PredicatedBag也依赖一个现有Bag，以及一个‘判断器’
 */
PredicatedBag<Object> predicatedBag = PredicatedBag.predicatedBag(collectionBag, Objects::nonNull);
String result6 = predicatedBag.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
System.out.println("predicatedBag的内容:" + result6);
```



SynchronizedBag & SynchronizedSortedBag

```java
/**
 * SynchronizedBag同步的bag，使用Synchronized同步代码块实现同步。
 * 可以指定锁对象，如果不指定则锁this
 */
SynchronizedBag<Object> synchronizedBag = SynchronizedBag.synchronizedBag(collectionBag);
SynchronizedSortedBag<Object> synchronizedSortedBag = SynchronizedSortedBag.synchronizedSortedBag(treeBag);
```



TransformedBag

> 对原集合进行转化，一般不会用这个，java8提供的`Stream API`有一个Map方法，可以将结果映射。

- transformingBag 方法，只会对后面add进来的元素进行转换，而对之前的初始化的不会转化
- transformedBag方法，会对后加的以及一开始初始化的都进行转化

```java
/**
 * TransformedBag
 */
Bag<Object> bag1 = TransformedBag.transformingBag(hashBag, Object::hashCode);
bag1.add("XX", 3);
String result8 = bag1.stream().map(Objects::toString).collect(Collectors.joining(",", "[", "]"));
System.out.println("TransformedBag.transformingBag " + result8);

Bag<Object> bag2 = TransformedBag.transformedBag(hashBag, Object::hashCode);
bag2.add("xx",3);
String result9 = bag2.stream().map(Objects::toString).collect(Collectors.joining(",", "[", "]"));
System.out.println("TransformedBag.transformingBag " + result9);
```



UnmodifiableBag

> 不可修改的Bag。



BagUtils

> 协助生成bag，其实调用的就是`XXXBag.xxxBag()`方法，每一个Bag类都会有一个静态方法，用于生成Bag。

```java
/**
 * BagUtils  Bag工具类 协助生成Bag
 */
final Bag<Object> bag = BagUtils.collectionBag(new HashBag<>());
```

###### BidiMap

> BidiMap -  提供双向映射，可通过键查找值，也可以通过值查找键

DualHashBidiMap

> 双重hashMap。其内封装了两个hashMap，bidimap的put()方法中key-value有任意重复的此条记录会被覆盖

```java
/**
 * bidimap
 * - bidimap的put()方法中key-value有任意重复的此条记录会被覆盖
 */
@Test
public void testBidiMap() {

    final DualHashBidiMap<String, Integer> dualHashBidiMap = new DualHashBidiMap<>();
    dualHashBidiMap.put("a", 1);
    dualHashBidiMap.put("b", 2);
    dualHashBidiMap.put("c", 3);
    dualHashBidiMap.put("d", 3);
    dualHashBidiMap.put("e", 12);
    dualHashBidiMap.put("e", 123);

    System.out.println(dualHashBidiMap.get("a"));
    System.out.println(dualHashBidiMap.getKey(1));
    System.out.println(dualHashBidiMap.getKey(3));
    System.out.println(dualHashBidiMap.values());
    System.out.println(dualHashBidiMap.keySet());
}
```

```bash
1
a
d
[1, 2, 3, 123]
[a, b, d, e]
```



###### iterators

> `iterators`提供了许多迭代包装类使我们很容易迭代集合，并且支持逆向迭代

ArrayItertor

> 数组迭代器，接收一个数组、迭代起始下标、迭代终止下标。

> 只接受迭代数组，因为next()方法会调用，本地静态方法`Array.get(array,index)`

```java
public static native Object get(Object array, int index)
    throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
```

> 简单使用

```java
 /**
 * ArrayIterator 数组迭代器，接受一个数组、起始下标、终止下标
 */
Iterator<Object> arrayIterator1 = new ArrayIterator<>(Arrays.asList("1", "2", "3").toArray(),0,2);
while (arrayIterator1.hasNext()) {
    System.out.println(arrayIterator1.next());
}
```



ArrayListIterator

> 接收一个数组，起始下标，终止下标。
>
> 对`ArrayIterator`的拓展，支持正向迭代、也支持反向迭代。

```java
/**
 * ArrayListIterator 
 * 支持正向迭代、逆向迭代
 */
ArrayListIterator<Object> arrayListIterator2 = new ArrayListIterator<>(Arrays.asList("1", "2", "3","4", "5", "6").toArray(), 2, 6);
while (arrayListIterator2.hasNext()) {
    System.out.println(arrayListIterator2.next());
}
while (arrayListIterator2.hasPrevious()) {
    System.out.println(arrayListIterator2.previous());
}
```



BoundedIterator

> 有边界的迭代器，接收三个参数`Iterator`迭代器、`offeset`偏移量、`max`迭代数量。
>
> 如下表示从下标2开始、迭代2个元素，结果是 3、4

```java
List<String> list = Arrays.asList("1", "2", "3", "4", "5");
BoundedIterator<String> boundedIterator = new BoundedIterator<>(list.iterator(), 2, 2);
while (boundedIterator.hasNext()) {
    System.out.println(boundedIterator.next());
}
```

CollectionIterator

```java
//比较器，会影响迭代结果
private Comparator<? super E> comparator = null;
//迭代器
private List<Iterator<? extends E>> iterators = null;
//待比较的元素
private List<E> values = null;
//迭代器是否还有值
private BitSet valueSet = null;
private int lastReturned = -1;
```

```java
CollatingIterator(final Comparator<? super E> comp, final Iterator<? extends E>[] iterators)
CollatingIterator(final Comparator<? super E> comp, final Collection<Iterator<? extends E>> iterators)
```

> 接收一个或多个迭代器和一个比较器，迭代结果会按一定顺序输出(原集合元素顺序不变)。

```java
System.out.println("CollatingIterator");
List<String> list1 = Arrays.asList("5", "4", "1", "2", "3");
List<String> list2 = Arrays.asList("2", "1", "c", "d", "e");
CollatingIterator<String> collatingIterator = new CollatingIterator<>(String::compareTo, list1.iterator(), list2.iterator());
while (collatingIterator.hasNext()) {
    System.out.print(collatingIterator.next());
}
```

输出：

> [21]54123[cde]

MapIterator

> util包下的map的迭代，如果想迭代key或value，需要借助entry。

```java
System.out.println("MapIterator");
final HashMap<Object, Object> map = new HashMap<>(8);
map.put("1", "a");
map.put("2", "b");
map.put("3", "c");
map.put("4", "d");
map.put("5", "e");
map.put("6", "f");
map.put("7", "g");
map.put("8", "h");
//entry迭代器
final Iterator<Map.Entry<Object, Object>> iterator =
        map.entrySet().iterator();
while (iterator.hasNext()) {
    final Map.Entry<Object, Object> next = iterator.next();
    System.out.println("key值:" + next.getKey() + "value值:" + next.getValue());
}
//key迭代器
map.keySet().iterator();
//value迭代器
map.values().iterator();
final HashedMap<Object, Object> hashedMap = new HashedMap<>(map);
final MapIterator<Object, Object> hashedMapIterator = hashedMap.mapIterator();
while (hashedMapIterator.hasNext()) {
    System.out.println(hashedMapIterator.next());
    System.out.println("key值:" + hashedMapIterator.getKey() + "value值:" + hashedMapIterator.getValue());
}
```



###### CollectionUtils

> `cpache`的集合工具类，提供很多有用的方法，此工具类在java8之前很有用，但java8的Stream  api提供了类似功能，因此许多方法都可以用stream代替。

ignor  null

添加元素时忽略null值

```java
final List<String> listX = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
final List<String> listY = new ArrayList<>(Arrays.asList("1", "2", "c", "b", "5"));
System.out.println(CollectionUtils.addIgnoreNull(listX, null));
System.out.println(CollectionUtils.addIgnoreNull(listX, "6"));
```

merge & sort

> 合并排序，如果不指定比较器，则会自然排序，如果指定比较器则按指定比较器来排序

```java
System.out.println("merge  and  sort");
final List<String> collate1 = CollectionUtils.collate(listX, listY);
System.out.println(collate1);
final List<String> collate2 = CollectionUtils.collate(listX, listY, String::compareTo);
System.out.println(collate2);
```

安全空检查

> 很多时候对集合遍历的时候，需要对集合进行安全空检查，CollectionUtils为我们提供了一套方式

```java
System.out.println("安全空检查");
System.out.println(CollectionUtils.isEmpty(listX));
System.out.println(CollectionUtils.isNotEmpty(listX));
```

交集  并集  外集

```java
System.out.println("交集" + CollectionUtils.intersection(listX, listY));
System.out.println("并集" + CollectionUtils.union(listX, listY));
System.out.println("外集" + CollectionUtils.subtract(listX, listY));
```



###### 小结

> java8的Stream可以适用于大部分的集合操作。

交集 外集 并集

```java
final List<String> collect1 = listX.stream().filter(listY::contains).collect(Collectors.toList());
System.out.println("交集" + collect1);
final List<String> collect2 = listX.stream().filter(ele -> !listY.contains(ele)).collect(Collectors.toList());
System.out.println("外集" + collect2);

System.out.println("Stream 合并集合");
final ArrayList<List<String>> lists = new ArrayList<>();
lists.add(listX);
lists.add(listY);
final List<Object> collect = lists.stream().flatMap(Collection::stream).collect(Collectors.toList());
System.out.println("并集"+collect);
```

其他的诸如过滤、排序、转换(映射)等Stream都可以



#### Arrays.asList(T ...t)

> 使用此方式创建集合需要注意什么？

- 此方式创建的集合是Arrays的一个子类ArrayList，并未实现增删方法，不可对其进行==增删==操作。

  会报出`java.lang.UnsupportedOperationException`

- 可进行修改操作

- 可将其作为参数，构造真正的ArrayList



#### 集合中的fail -fast

> fail- fast 快速失败，一种一旦检测出系统异常就会立刻上报的机制，此种机制可使系统避免在有安全隐患的情况下继续运行，常用的比如说参数校验。
>
> 看一下集合中的fail-fast机制：

常常出现在迭代中，防止下标越界或迭代不完全，以arrayList为例

expectedModCount此属性在获取迭代器的时候会赋值为modCount，如果在迭代时我们通过修改集合改变modeCount则会报出此异常

```java
final void checkForComodification() {
    if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
}
```

> 对集合进行增删操作会触发fail-fast机制

```java
final List<String> list = new ArrayList<>(Arrays.asList("1", "2", "3"));
final Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
  //list.remove("1");
  //list.add("1");
  iterator.next();
}
```

> foreach也会触发fail-fast机制，因为其底层就是使用迭代器迭代的

如果安装了阿里代码规约插件的话，那么已经帮你提示出来了

```java
final List<String> list = new ArrayList<>(Arrays.asList("1", "2", "3"));
for (String s : list) {
    list.add("a");
}
```

反编译看一下字节码

![image-20220821180247553](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208211804530.png)



####  集合中的fail - safe

> 为了避免fail-fast机制，可以使用采用fail-safe机制的集合类，这样的类在对集合进行操作的时候不会直接操作集合内容，而是通过拷贝一份，在拷贝的内容上操作，最后需要同步更改，则同步更改。



##### CopyOnWriteArrayList

> 这就是一个fail-safe集合类。
>
> 其内部的add  、reomve、set等操作都使用ReentrantLock保证同步，任何操作都是在拷贝对象数组上进行，最后再替换原集合对象数组。

> COW集合的迭代器:其内部包含了一个源集合对象数组的快照、副本、拷贝(snapshot)，任何迭代都是在此对象数组上完成的。并且由于COW在修改对象数组的时候，COW都会拷贝一份，所以并不会影响迭代器中的拷贝所指向的对象数组

> 使用COW代替ArrayList就不会发生CME异常

```java
CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>(Arrays.asList("1","2","3"));
for (String s : cowList) {
    if ("1".equals(s)){
        cowList.remove(s);
    }
}
```



但是也造成了一个问题：我们对集合的修该，修改对象数组，是对迭代器不可见的，因为在集合修改的时候，会使用System.arrayCopy()方法生成一个新的对象数组。

```java
CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>(Arrays.asList("1","2","3"));
Iterator<String> iterator = cowList.iterator();
//fail-sfae 集合修改
for (String s : cowList) {
    if ("1".equals(s)){
        cowList.remove(s);
    }
}
//已经修改
System.out.println(cowList);
//但对迭代器不可见
while (iterator.hasNext()) {
    System.out.println(iterator.next());
}
```

打印结果：

```bash
[2, 3]
1
2
3
```



###### 特点

- copy-on-write，写时复制，所有修改操作在快照上操作
- 同步，使用ReenTrantLock
- 写时加锁，避免拷贝出多个副本，导致并发写
- 读时不加锁，读写分离。会导致弱一致性问题：读取的不是最新数据



##### 循环中remove

> fail-fast机制出现于迭代器中修改集合操作，触发fail-fast机制，导致并发异常。

- forii   普通for循环
- iterator  的remove方法  （主要思想就是在修改modCount的同时修改expectedModCount）
- fail-safe集合（fail-safe集合的迭代器一般不支持remove方法，直接使用集合的remove方法）
- 增强for：fail-fast机制在next方法中调用，在修改集合后避免使用next方法即可

```java
List<String> list = new ArrayList<>(Arrays.asList("1","2","3"));
for (String s : list) {
    if ("1".equals(s)){
        list.remove(s);
    }
    break;
}
System.out.println(list);
```



### IO

> 文件操作是编程一部分，学习一下IO流。

####  字符流&字节流

> 从名称来看区别在于流的传输方式：字节  or    字符。

有了字节流为何还需要字符流？

字符流可以认为是字节流＋编码方式。编码方式指导字节流如何处理字节，将其组合成字符。原因就在于方便操作，对于中文可能不同的编码方式得到的字节数据是不同的，那么使用字节流读取可能出现乱码的情况，那么有了字符流可以指定编码，指导字节流读几个字节作为一个汉字。



##### 位、字节、字符

- Bit   最小二进制单位，0或1.
- Byte  字节，1 Byte = 8 Bit，取值 [-128,127]
- Char 字符，1Char = 16Bit，人能直观认识的最小单位，取值 [0,2^16^-1]

##### 字节流

> 操作字节，用于读取单个字节或字节数组，直接对文件进行操作，无需缓冲区（读出来的数据直接就可以用）。
>
> 主要操作类是：InputStream和OutputStream的子类

InputStream常用子类：

- FileInputStream：文件输入流，用于读取文件信息到内存中。
- ByteArrayInputStream：字节数组输入流
- ObjectInputStream: 对象输入流
- FilterInputStream  过滤流



OutputStream常用子类：

- FileOutputStram:文件输出流，用于将数据输出到文件中
- ByteArrayOutputStream:字节数组输出流
- ObjectOutputStream:对象输出流
- FilterOutputStream  

###### FileInputStream & FileOutputStream

> 用于读取文件信息到内存和将内存中的数据输出到文件

查看一下FileInputStream  api

```java
//文件描述实例，由java创建，不用我们创建
private final FileDescriptor fd;
//文件路径
private final String path;
//用于读取、写入、映射和操作文件的通道。与FileInputStream唯一关联。底层，不用管
private FileChannel channel = null;
//对象锁，阻塞io，使用Synchronized关键字阻塞
private final Object closeLock = new Object();
//资源是否关闭
private volatile boolean closed = false;

//使用文件名创建一个文件输入流会调用FileInputStream(File file)方法
public FileInputStream(String name) throws FileNotFoundException；
public FileInputStream(File file) throws FileNotFoundException；
//打开流，本地方法由c\C++编写，无需主动调用，构造方法已经调用，且是私有方法
private native void open0(String name) throws FileNotFoundException;
private void open(String name) throws FileNotFoundException {open0(name);}
//从流中读取一个字节，声明式异常，需要主动处理
public int read() throws IOException；
private native int read0() throws IOException
//从流中读取off开始读取len个字符到字符数组中指定下标处,并返回读取长度。一般来说从0开始
private native int readBytes(byte b[], int off, int len) throws IOException;
public int read(byte b[]) throws IOException {
    return readBytes(b, 0, b.length);
}
//跳过指定长度字符，返回跳过长度。移动指针
public long skip(long n) throws IOException {
  return skip0(n);
}
private native long skip0(long n) throws IOException;
//剩余字节数
public int available() throws IOException {
  return available0();
}
private native int available0() throws IOException;
//关闭流，必须关闭资源
public void close() throws IOException；
```

查看一下FileOutputStream  api

```java
//是否追加文件内容。默认false，即覆盖
private final boolean append;
public FileOutputStream(String name) throws FileNotFoundException；
public FileOutputStream(String name, boolean append) throws FileNotFoundException；
public FileOutputStream(File file) throws FileNotFoundException；
public FileOutputStream(File file, boolean append) throws FileNotFoundException；
//open  open0
//写入文件，字节byte可直接转化为int，安全不会溢出，没有负数
private native void write(int b, boolean append) throws IOException;
private native void writeBytes(byte b[], int off, int len, boolean append) throws IOException;
```



> 常用操作：这里使用try with resource语法糖，编译器会自动帮我们关闭资源，可以反编译查看

```java
@Test
public void inputStreamTest1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile/test.txt";
    try (FileInputStream fin = new FileInputStream(filePath)) {
        //跳过指定长度字节
        final long skip = fin.skip(3L);
        System.out.println(skip);
        final byte[] bytes = new byte[5];
        //从1开始读取4个字节，放入字节数组指定下标处
        final int read = fin.read(bytes, 1, 4);
        System.out.println(read);
        for (byte aByte : bytes) {
            System.out.println(Character.valueOf((char) aByte));
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
@Test
public void outputStreamTest1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile/test.txt";
    try (final FileOutputStream fop = new FileOutputStream(filePath, true)) {
        fop.write("可爱".getBytes());
        fop.write("abc".getBytes());
        //刷新流，将此之前的所有数据给操作系统，让操作系统写入硬件设备
        fop.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

###### ByteArrayInputStream & ByteArrayOutputStream

> 字节数组输入输出流。内部组合一个字节数组，用于缓冲数据。

```java
@Test
public void byteArrayInputStreamTest1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile/test.txt";
    try (final FileOutputStream fop = new FileOutputStream(filePath, true);
         final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("abcd".getBytes(StandardCharsets.UTF_8))) {
        System.out.println((char)byteArrayInputStream.read());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
@Test
public void byteArrayOutputStreamTest1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile/test.txt";
    try (final FileOutputStream fop = new FileOutputStream(filePath, true);
         final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024)) {
        //写入到字节数组
        byteArrayOutputStream.write("可可爱爱".getBytes());
        //一次性写入到，另一个输出流
        byteArrayOutputStream.writeTo(fop);
        //刷新流，将此之前的所有数据给操作系统，让操作系统写入硬件设备
        fop.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

###### ObjecInputStream & ObjectOutputStream

> 对象输入输出流，一般用于序列化操作，又称为序列化流和反序列化流。ObjectOutputStream用于将对象序列化输出到文件中，持久化保存，ObjecInputStream用于将对象从文件中读出来。
>
> 一般用于数据传输，或当某个对象实例生命周期已经结束，但是需要保存其状态，以便下此直接反序列化恢复的情况。

首先作为字节流，它拥有字节流的所有相关操作

```java
@Test
public void objectOutputStreamTest1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile/objectTest.txt";
    try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath,false))) {
        //写入到字节数组
        objectOutputStream.write("xxx".getBytes());
        //刷新流，将此之前的所有数据给操作系统，让操作系统写入硬件设备
        objectOutputStream.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

作为对象流，设计目的就是为了操作对象:

- class类必须实现Serializable接口，否则会报出`java.io.NotSerializableException`异常
- 序列号serialVersionUID对应唯一类，不可随意修改，在反序列化时会去匹配。如果修改则报出`java.io.InvalidClassException`异常
- 被transient修饰的属性在序列化时会被忽略

例子：

```java
@Data
@Accessors(chain = true)
class Person   implements Serializable {
    private static final long serialVersionUID = -8861126921891657698L;
  
    String str1;
    transient String str2;
    final String str3 = "123";
    static String str4;
    int age;
    Date birthday;
}
@Test
public void objectInputStreamTest1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile/objectTest.txt";
    try (final ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
        final Person o = (Person) objectInputStream.readObject();
        System.out.println(o);
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}
@Test
public void objectOutputStreamTest2() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile/objectTest.txt";
    try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath, false))) {
        final Person person = new Person();
        person.setStr1("str1")
                .setStr2("str2")
                .setAge(1)
                .setBirthday(Calendar.getInstance().getTime());
        objectOutputStream.writeObject(person);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```



###### FilterInputStream & FilterOutputStream

> 过滤流，装饰器模式，可以对字节流进行包装实现额外功能。

过滤流常用子类：

- DateInputStream ：基本数据类型流，提供基本数据类型读取写入操作方法
- BufferedInputStream : 缓冲输出流,其内维护一个字节数组，避免每次都和文件交互
- PushbackInputStream
- LineNumberInputStream

> DateInputStream ,基本数据类型流，提供对基本数据类型写入、读取操作方法，其对基本字节流做了一个封装，约定如何写入字节，底层还是调用基本字节流写入字节。比如int占32位，4个字节，那么通过移位运算将其拆分为四个字节逐个写入字节，读取时逐个读取，并按写入规则转化数据。

例一：

```java
@Test
public void dataOutputStreamTest1() {
    String filePath = "D:\\File\\Desktop\\blogXX\\foot\\testfile\\dataStreamFile.txt";
    try (final DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(filePath, false))) {
        //作为字节流，拥有字节流相关操作，写入字节或字节数组
        //dataOutputStream.write("开开心心".getBytes(StandardCharsets.UTF_8));
        dataOutputStream.writeUTF("可可爱爱");
        dataOutputStream.writeUTF("可可爱爱");
        dataOutputStream.writeUTF("开开心心");
        dataOutputStream.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
@Test
public void dataInputStreamTest1() {
    String filePath = "D:\\File\\Desktop\\blogXX\\foot\\testfile\\dataStreamFile.txt";
    try (final DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filePath))) {
        while (dataInputStream.available()>0){
            System.out.println(dataInputStream.readUTF());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```



> BufferedOutputStream  缓冲输出流，其内部维护一个字节数组，数据首先存放于字节数组，当调用flush方法或字节数组已经满了的情况，将字节数组中的字节一次性写入文件。如此不必频繁通过io通道和文件打交道。
>
> BufferedInputStream 缓冲输入流，内部有一个缓冲数组，数据读出来先存放于缓存数组中，当真正需要的时候将数据拷贝出来。

例二：

```java
@Test
public void bufferedOutputStreamTest1() {
    String filePath = "D:\\File\\Desktop\\blogXX\\foot\\testfile\\bufferStreamFile.txt";
    try (final FilterOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath, false), 1024)) {
        //作为字节流，拥有字节流相关操作，写入字节或字节数组
        bufferedOutputStream.write("abcde".getBytes(StandardCharsets.UTF_8));
        bufferedOutputStream.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
@Test
public void bufferedInputStreamTest1() {
    String filePath = "D:\\File\\Desktop\\blogXX\\foot\\testfile\\bufferStreamFile.txt";
    try (final FilterInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath))) {
         byte[] bytes = new byte[1024];
        while (bufferedInputStream.available() > 0) {
            bufferedInputStream.read(bytes);
            System.out.println(Arrays.toString(bytes));
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```



#####  字符流

> 操作字符，需要缓冲区，操作Reader、Writer的子类

![image-20220824221348711](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282254476.png)

Read常用子类：

- InptStreamReader ：将字节输入流转为字符输入流
- StreamDecoder:流解码器

- FileReader  文件输入流
- BufferedReader：缓存字符输入流

Writer常用子类

- OutputStreamWriter:将字符输出流转化为字节输出流

- FileWriter   文件输出流
- BufferedReader  缓存字符输出流



###### InputSteamReader & OutputStreamWriter

> 字节字符转化流。以规定的流解码器去读取字节数组，最后和转化为字符输出，我们只需要指定编码集。

例一：

```java
@Test
public void testFileOutputStream1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
    try (final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(filePath, "dataStreamFile.txt")), StandardCharsets.UTF_8)) {
        outputStreamWriter.write("hhhhhhhhhhhh哈哈哈哈");
        outputStreamWriter.flush();
    } catch (IOException e) {
    }
}
@Test
public void testFileInputStream1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
    try (final InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(filePath, "dataStreamFile.txt")), StandardCharsets.UTF_8)) {
        final char[] buffer = new char[1024];
        int len = 0;
        while ((len = inputStreamReader.read(buffer)) > 0) {
            System.out.println(String.valueOf(buffer, 0, len));
        }
    } catch (IOException e) {
    }
}
```



###### FileReader  & FileWriter

> 文件输入输出流，操作文件的便利类。
>
> 对InputStreamReader的封装。无需指定字符集，默认使用系统文件字符集。

查看系统文件字符集：

```java

final String s = AccessController.doPrivileged(
	new GetPropertyAction("file.encoding"));
System.out.println(s);
```

![image-20220824231942206](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282254532.png)

```java
@Test
public void testFileWriter1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
    try (final FileWriter fileWriter = new FileWriter(new File(filePath,"dataStreamFile.txt"))){
        fileWriter.write("12331");
        //追加写入，返回this，类似于StringBuilder 的append
        fileWriter.append("xxxxxx").append("追加");
        //将流刷入操作系统，让操作系统去写入硬件，最终结果是否写入成功由操作系统决定
        //如果不刷新，流也不关闭，数据不会写入硬件
        fileWriter.flush();
    }catch (IOException e){
    }
}
@Test
public void testFileReader1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
    try(final FileReader fileReader = new FileReader(new File(filePath,"dataStreamFile.txt"))){
        final char[] buffer = new char[1024];
        int len  = 0;
        while ((len = fileReader.read(buffer)) > 0) {
            System.out.println(String.valueOf(buffer,0, len));
        }
    }catch (IOException e){
    }
}
```

###### BufferedReader & BufferedWriter

> 缓冲字符流，上面的FileRead存在不可自定义字符编码的问题，那么使用BufferedReader可完美解决此问题。
>
> BufferedReader 是对OutputStreamReader的增强和包装，其内提供了读取一行字符的方法，以及将所有字符读出以Stream<String>流的方式返回。以上都是基于字符数组实现，默认容量是8192。

例子：

```java
@Test
public void testBufferedWriter1() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
    try (final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath, "dataStreamFile.txt")), StandardCharsets.UTF_8))) {
        bufferedWriter.write("可可爱爱");
        bufferedWriter.write("快快乐乐");
        bufferedWriter.write('a');
        bufferedWriter.write(96);
        bufferedWriter.write(new char[]{'x','x'});
        bufferedWriter.write("\n");
        bufferedWriter.write("\t");
        //新起一行
        bufferedWriter.newLine();
        bufferedWriter.write("开开心心");
    } catch (IOException e) {

    }
}

@Test
public void testBufferedReader1() {
  String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
  try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath, "dataStreamFile.txt")), StandardCharsets.UTF_8))) {
    final char[] chars = new char[1];
    final int read = bufferedReader.read();
    System.out.println(new String(chars, 0, 1));
    String buffer = "";
    while ((buffer = bufferedReader.readLine()) != null) {
      System.out.println(buffer);
    }
    //一次性读出来
    BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath, "dataStreamFile.txt")), StandardCharsets.UTF_8));
    bufferedReader1.lines().forEach(System.out::println);
  } catch (IOException e) {

  }
}
```



###### 字符流需要手动flush

> 如果操作字符流方法内没有自动帮我们flush，那么想要将数据刷入文件需要手动flush

```java
@Test
public void testFlush() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/myfoot/foot/testfile";
    OutputStreamWriter outputStreamWriter = null;
    try {
        outputStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(filePath, "dataStreamFile.txt")));
        //如此写入不了，没有flush 也没有 关闭流
        outputStreamWriter.write("你好呀");
    } catch (IOException e) {
    }
}
```





##### 字节流与字符流转化

> java io包下除了字节流与字符流以外，还包含一组字节流-字符流转化流。

InputStreamReader，是Reader的子类属于字符流，可以将输入的字节流转化为输入的字符流。

OutputStreamWriter，是Writer的子类属于字符流，可以将输出的字符流转换为输出的字节流。

##### 输入输出流

> 输入输出流是相对于参考系来说的，此参考系为存储数据的介质，往介质中存数据则为输入流，从介质中读出数据则为输出流。

比如：

将文件中数读出来，存到内存中，则为输入流，使用FileInputStream、FileReader

将内存中的数据输出到文件中，则为输出流，使用FileOutputStream、FileWriter



### 反射

> 反射式java为程序员提供的强大机制，赋予程序可以在运行期间，知道任意类的所有属性和方法，调用或修改任意对象的属性和方法的能力。
>
> Java反射就是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意方法和属性；并且能改变它的属性。而这也是Java被视为动态语言的一个关键性质。

#### Class类

> Class类用于封装加载到jvm中的类(包括接口和类)。当一个类被装载进jvm就会生成一个与之唯一对应的Class对象，通过这个Class对象我们就知道此类的所有信息。
>
> 在程序运行时，jvm会检查所需加载的类对应的Class对象是否已经加载，如果没有加载，jvm会根据类名查找对应的Class文件，并将其加载入jvm，jvm会保证每个class类只会生成唯一对应的class对象。

获取Class对象的方式：

- 对象.getClass()方法
- 类名.class
- Class.forName("class类全路径")  第二个参数表示是否触发初始化，默认触发，且只触发一次

```java
@Test
public void test() {
    final ClassPerson classPerson = new ClassPerson();
    //对象.getClass
    final Class<? extends ClassPerson> aClass = classPerson.getClass();
    //类名.class
    final Class<ClassPerson> classPersonClass = ClassPerson.class;
    try {
        //Class.forName  第二个参数boolean值表示是否触发初始化
        final Class<?> aClass1 = Class.forName("com.roily.booknode.javatogod._04reflect.ClassPerson");
        final Class<?> aClass2 = Class.forName("com.roily.booknode.javatogod._04reflect.ClassPerson", true, ClassLoader.getSystemClassLoader());
   		   System.out.println(VM.current().addressOf(aClass1));
            System.out.println(VM.current().addressOf(aClass2));
        } catch (Exception e) {
        }
        System.out.println(VM.current().addressOf(aClass));
        System.out.println(VM.current().addressOf(classPersonClass));
}
```

![image-20220825100540111](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208252320148.png)



#### 反射能干什么

- 使用反射创建实例对象
- 使用反射获取一个实例对象，所属类的所有信息(类信息[父类、接口、注解]、属性、方法[包括私有方法、构造方法])



##### 使用反射创建实例

> 除了new关键字可创建实例对象，反射机制也可创建实例。
>
> 主要有两种方式：
>
> ①class.newInstance()
>
> ②获取构造方法，执行构造方法

###### isInstance

isInstance()方法是instanceOf 关键字的平替，如果返回true则可正常转化类型

```java
/**
 * Class 类Api isInstance
 * isInstance方法，是instanceOf的平替
 */
@Test
public void testIsInstance() {
    System.out.println("如果Object参数为该类实例，返回true" + ClassPerson.class.isInstance(new ClassPerson()));
    System.out.println("如果Object参数为该类或其任意子类的实例，返回true" + ClassPerson.class.isInstance(new ClassSon()));
    System.out.println("如果Object参数为该接口实现类，返回true" + InterfaceTest.class.isInstance(new InterfaceTestImpl()));
    //如果是数组类型，可强制转化不报CastException异常
    final Object[] objects = new Object[1024];
    System.out.println(objects.getClass().isInstance(new Integer[11]));
}
```

###### newInstance

newInstance()方法创建实例：

```java
public class ClassPerson {
    public ClassPerson() {
         System.out.println("公开构造器");
    }
}
public class ClassPersonPrivate {
    private ClassPersonPrivate() {
        System.out.println("私有构造器");
    }
}
```

> 如果此类的构造器是public的，则可使用class.newInstance()方法创建实例，且会触发类的初始化。
>
>  如果此类的构造器是private的，则不可使用class.newInstance()方法创建实例，会报java.lang.IllegalAccessException异常。

```java
/**
 * 如果此类的构造器是public的，则可使用class.newInstance()方法创建实例
 * 切会触发类的初始化
 */
@Test
public void testNewInstance1() throws InstantiationException, IllegalAccessException {
    final ClassPerson classPerson = ClassPerson.class.newInstance();
    System.out.println(classPerson);
}
/**
 * 如果此类的构造器是private的，则不可使用class.newInstance()方法创建实例
 * 会报java.lang.IllegalAccessException异常
 */
@Test
public void testNewInstance2() throws InstantiationException, IllegalAccessException {
    final ClassPersonPrivate classPersonPrivate = ClassPersonPrivate.class.newInstance();
    System.out.println(classPersonPrivate);
}
```



###### getConstractor

> 获取任意Class对象的非私有构造器，可以指定构造参数

```java
/**
 * Class的getConstructor方法可以获取，任意类的非私有构造器
 * 可以指构造参数
 */
@Test
public void testGetConstructor() throws Exception {
    final Class<ClassPerson> classPersonClass = ClassPerson.class;
    //无参构造
    final Constructor<ClassPerson> constructorWithOutParams = classPersonClass.getConstructor(null);
    final ClassPerson classPerson1 = constructorWithOutParams.newInstance(null);
    System.out.println(classPerson1);
    //有参构造
    final Constructor<ClassPerson> constructorWithParams = classPersonClass.getConstructor(String.class);
    final ClassPerson classPerson2 = constructorWithParams.newInstance("小可爱");
    System.out.println(classPerson2);
}
```

![image-20220825110704311](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208252320961.png)

> 当然对于private私有构造器，不可以获取。
>
> 会报出`java.lang.NoSuchMethodException`异常

```java
@Test
public void testGetConstructor2() throws Exception {
    final Constructor<ClassPersonPrivate> constructor = ClassPersonPrivate.class.getConstructor(null);
    constructor.newInstance(null);
}
```

###### getDeclaredConstructor

> 获取任意类的构造器，如果是private的需要设置为可访问的

```java
/**
 * Class的getDeclaredConstructor方法可以获取，任意类的构造器
 * 可以指构造参数,如果是私有需要设置可访问,否则会爆出IllegalAccessException异常
 */
@Test
public void testGetDeclaredConstructor1() throws Exception {
    final Constructor<ClassPerson> declaredConstructor1 = ClassPerson.class.getDeclaredConstructor(null);
    declaredConstructor1.newInstance(null);
    final Constructor<ClassPersonPrivate> declaredConstructor2 = ClassPersonPrivate.class.getDeclaredConstructor(null);
    //设置可访问
    declaredConstructor2.setAccessible(true);
    declaredConstructor2.newInstance(null);
}
```



##### 属性、方法

> 使用反射操作属性、方法

###### 属性

> 获取属性Field

```java
//修改name属性为pubilc
public String name;
```

> 使用`getField()`方法只能获取`public`属性。
>
> 使用`getDeclaredField()`获取所有属性，设置`setAccessible(true)`可对非pulic属性进行访问

```java
@Test
public void testField1() throws Exception {
    //getField获取public 属性
    final Field name = ClassPerson.class.getField("name");
    System.out.println("field name :  => " + name.getName());
    System.out.println("field type :  => " + name.getType());
}
@Test
public void testField2() throws Exception {
    final Field[] fields =  ClassPerson.class.getDeclaredFields();
    Arrays.asList(fields).forEach(field -> field.setAccessible(true));
    for (Field field : fields) {
        System.out.println("field name :  => " + field.getName());
        System.out.println("field type :  => " + field.getType());
    }
}
```

> 可通过反射动态修改对象属性

```java
@Test
public void testField3() throws Exception {
    final ClassPerson classPerson = new ClassPerson();
    System.out.println("ClassPerson =>" + classPerson);
    //得到所有
    final Field[] fields = classPerson.getClass().getDeclaredFields();
    Arrays.asList(fields).forEach(field -> {
        field.setAccessible(true);
        Object obj;
        switch (field.getName()) {
            case "name":
                obj = "name";
                break;
            case "age":
                obj = 20;
                break;
            case "num":
                obj = 10;
                break;
            case "values":
                obj = new String[]{"values"};
                break;
            default:
                obj = null;   break;
        }
        try {
            field.set(classPerson,obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    });
    System.out.println(classPerson);
}
```

![image-20220825184025386](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208252320662.png)

###### 方法

> 通过反射调用方法

```java
@Test
public void testMethod1() throws Exception {
    final ClassPerson classPerson = new ClassPerson();
    System.out.println("ClassPerson =>" + classPerson);
    //获取public方法
    final Method methodWithoutParam = classPerson.getClass().getMethod("publicMethod");
    System.out.println("方法名:=>" + methodWithoutParam.getName());
    methodWithoutParam.invoke(classPerson);
    //获取public方法
    final Method methodWithParam = classPerson.getClass().getMethod("publicWithParamMethod", String.class, int.class);
    System.out.println("方法名:=>" + methodWithParam.getName());
    methodWithParam.invoke(classPerson, "str", 100);
}
```

![image-20220825185606816](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208252320034.png)



##### 使用反射获取其他信息

> 使用反射获取其他信息

```java
/**
 * 使用反射获取其他信息
 */
@Test
public void testGetOtherInfo() throws FileNotFoundException {
    final String string = new String();
    final Class<TestClass> testClassClass = TestClass.class;
    System.out.println("类名  => " + testClassClass.getSimpleName());
    System.out.println("类全限定名  => " + testClassClass.getName());
    System.out.println("包名  => " + testClassClass.getPackage());
    //获取类加载器，我们写的类，一般都是应用类加载器，也叫app加载器
    System.out.println("类加载器  =>");
    //其他加载器，扩展类加载器ext,引导类加载器我们得不到会返回null
    System.out.println("扩展类加载器 =>" + testClassClass.getClassLoader().getParent());
    System.out.println("扩展类加载器 =>" + testClassClass.getClassLoader().getParent().getParent());
    //注意如果注解的保留策略需设置为@Retention(RetentionPolicy.RUNTIME)
    System.out.println("runtime注解，运行期由VM保留  => " + testClassClass.getAnnotation(AnnotationTest.class));
    for (Class<?> anInterface : testClassClass.getInterfaces()) {
        System.out.println("获取接口  => " + anInterface);
    }
    System.out.println("获取父类  => " + testClassClass.getSuperclass());
    System.out.println("获取类路径下，也就是classes根目录下的某个资源文件输入流   => " + testClassClass.getResourceAsStream("/test.properties"));

}
```

![image-20220825234832246](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208252348911.png)

#### 工厂模式 + 反射实现ioc

> `Spring IOC`的实现就是基于反射 + 工厂模式实现的。



##### 不使用反射

> 不使用反射利用工厂模式创建bean，这里就以简单工厂模式实现

```java
public interface Fruit {
    /**
     * 描述
     */
    void describe();
}
public class Apple implements Fruit{
    String name;
    @Override
    public void describe() {
        System.out.println(this.name);
    }
}
public class Banana implements Fruit{
    String name;
    @Override
    public void describe() {
        System.out.println(this.name);
    }
}
public class Orange implements Fruit{
    String name;
    @Override
    public void describe() {
        System.out.println(this.name);
    }
}
```

```java
public class MyCustomizeFactory {
		//bean工厂
    final static HashMap<String, Object> mapFactory = new HashMap<>();
    public static Fruit getInstance(String beanName) {
        Fruit fruit = (Fruit) mapFactory.get(beanName);
        if (!ObjectUtils.isEmpty(fruit)) {
            return fruit;
        }
        switch (beanName) {
            case "Apple":
                fruit = new Apple(beanName);
                break;
            case "Orange":
                fruit = new Orange(beanName);
                break;
            case "Banana":
                fruit = new Banana(beanName);
                break;
            default:
                System.out.println("error");
                break;
        }
        mapFactory.put(beanName, fruit);
        return fruit;
    }
}
```

> 测试

```java
public static void main(String[] args) {
    final Fruit apple = MyCustomizeFactory.getInstance("Apple");
    System.out.println(apple);
    final Fruit banana = MyCustomizeFactory.getInstance("Banana");
    System.out.println(banana);
}
```

> 如此实现存在一个问题：如果添加实现类的话需要修改工厂代码，不符合开闭原则

![image-20220826002732640](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208260027034.png)

##### 使用反射

> 我们只需要知道此类的全限定名即可通过反射创建此对象

```java
public static Fruit getInstanceWithReflect(String name, String className) {
        Fruit fruit = (Fruit) mapFactory.get(className);
        if (!ObjectUtils.isEmpty(fruit)) {
            return fruit;
        }
        try {
            fruit = (Fruit) Class.forName(className).newInstance();
        } catch (Exception e) {
            System.out.println("error");
        }
        mapFactory.put(name, fruit);
        return fruit;
    }
```

> 测试

```java
 final Fruit apple = MyCustomizeFactory.getInstanceWithReflect("apple","com.roily.booknode.javatogod._04reflect.factoryioc.Apple");
        System.out.println(apple.getClass());
        final Fruit banana = MyCustomizeFactory.getInstanceWithReflect("banana","com.roily.booknode.javatogod._04reflect.factoryioc.Banana");
        System.out.println(banana.getClass());
        final Fruit orange = MyCustomizeFactory.getInstanceWithReflect("orange","com.roily.booknode.javatogod._04reflect.factoryioc.Orange");
        System.out.println(orange.getClass());
```

> 结合配置文件，一次性创建工厂，之后只需去工厂取bean即可

创建bean配置文件

![image-20220826005204288](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208260052813.png)

工厂类添加方法

```java
public static Fruit getInstanceWithReflect(String name) {
    return (Fruit) mapFactory.get(name);
}
```

测试：

程序运行触发初始化，对应bean只创建一次放入工厂，想要就去拿

```java
static {
    final InputStream in = ClientTest.class.getResourceAsStream("/bean.properties");
    final Properties properties = new Properties();
    try {
        properties.load(in);
        properties.keySet().forEach(ele -> MyCustomizeFactory.getInstanceWithReflect((String) ele, (String) properties.get(ele)));
    } catch (IOException e) {
        e.printStackTrace();
    }
}
public static void main(String[] args) {
    final Fruit apple = MyCustomizeFactory.getInstanceWithReflect("Apple");
    System.out.println(apple.getClass());
    final Fruit banana = MyCustomizeFactory.getInstanceWithReflect("Banana");
    System.out.println(banana.getClass());
    final Fruit orange = MyCustomizeFactory.getInstanceWithReflect("Orange");
    System.out.println(orange.getClass());
}
```

![image-20220826005421217](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208260054512.png)

### 范型

> Java范型时JDK5引入的特性，允许在定义类、接口和方法的时候可以使用类型参数。Java范型是==伪范型==，即Java在语法上支持范型，但会在编译阶段会进行==类型擦除==，将范型替换为原始类型。在真正使用的时候会进行强制转化得到真正需要的类型。
>
> java范型是java提供的语法糖，我们所定义的范型在编译期间都会进行类型擦除，使用泛型可提高代码的复用性。Java的集合框架都使用了范型，我们平常所定义的List<String>、List<Iteger>这两个集合类型是相同的，在编译的时候会进行类型擦除，擦除后的类型变为原始类型List。



#### 范型优点

- 代码复用

  >  范型类和范型方法，共用一份字节码文件

- 类型安全

  > 即使用时可指定范型，编译器会进行类型检查和帮助强制类型转化



两个例子说明：

此处使用范型方法对比普通方法，范型共享字节码，可实现代码复用

```java
public class FanxClass {
    private static void add(int a, int b) {
        System.out.println("int  a + b : " + (a + b));
    }
    private static void add(double a, double b) {
        System.out.println("double  a + b : " + (a + b));
    }
    private static void add(float a, float b) {
        System.out.println("float  a + b : " + (a + b));
    }

    //声明范型方法
    private static <T extends Number> void addF(T a, T b){
        System.out.println(a.getClass()+ " a + b : " + (a.doubleValue() + b.doubleValue()));
    }

    public static void main(String[] args) {
        addF(1,2);
        addF(1.0,2.0);
        addF(1.0f,2.0f);
    }
}
```

类型安全：

编译器提高编译前检查

反编译查看可知 1、List不支持基本数据类型，会对基本数据类型进行自动装箱   2、编译器 类型擦除、强制转化  

```java
public class NeedNotCast {
    public static void main(String[] args) {
        
        //不指定范型，也就是Object，容易CastException
        final List objList = new ArrayList();
        objList.add(1);
        objList.add("1");
        objList.add(null);

        //new 关键字后面的<>里面不用声明类型，java会进行类型推断
        final List<Integer> integers = new ArrayList<>();
        // integers.add("1");//报错
        integers.add(1);//报错
        //不用强制转化
        final Integer integer = integers.get(0);
    }
}
```

```java
public static void main(String args[])
{
    List objList = new ArrayList();
    objList.add(Integer.valueOf(1));
    objList.add("1");
    objList.add(null);
    List integers = new ArrayList();
    integers.add(Integer.valueOf(1));
    Integer integer = (Integer)integers.get(0);
}
```



#### 范型基本使用

> 范型三种使用方式，范型类、范型接口、范型方法

##### 范型类

> 注意这里的method1和method2不可称作为范型方法，这两个方法中的范型依赖于具体实例。

```java
@Data
@AllArgsConstructor
public class FanXClass<T> {
    T t;
    void method1(T t) {
        System.out.println("T  t :" + t.getClass() + "和  FanXClass<T> 类型一致");
    }
    T method2(T t) {
        return t;
    }
    public static void main(String[] args) {

        final FanXClass<String> stringFanXClass = new FanXClass<>("FanXClass");
        final String t = stringFanXClass.t;
        stringFanXClass.method1("");

        final FanXClass<Integer> intFanXClass = new FanXClass<>(1);
        final Integer t2 = intFanXClass.t;
        intFanXClass.method1(t2);
    }
}
```

##### 范型接口

```java
public interface FanXInterface<T> {
    T method(T t);
}
class FanXInterfaceImpl<T> implements FanXInterface<T>{
    @Override
    public T method(T t) {
        return t;
    }
    public static void main(String[] args) {
        final FanXInterface<String> fanXInterface = new FanXInterfaceImpl<>();
        fanXInterface.method("str");
    }
}
```



##### 范型方法

> 范型方法指的是在调用方法时指定具体类型。
>
> 范型方法的好处：例如上面范型类的method1和method2，其类型在实例new出来就已经确定了，如果想要另一种类型则需要重新new。而范型方法则不需要。

```java
public class FanXMethod<T> {
    /**
     * 范型方法声明
     */
    private <T> void method1(T t) {
        System.out.println(t.getClass());
    }
    /**
     * @param t
     * @param <T> 声明此方法为一个范型方法
     * @return 返回类型为范型
     */
    private static <T> T method2(T t) {
        System.out.println(t.getClass());
        return t;
    }
   
    public static void main(String[] args) {
        //范型方法使用，范型方法和所在范型类范型无关
        final FanXMethod<Integer> integerFanXMethod = new FanXMethod<>();
        integerFanXMethod.method1("123");
       
        FanXMethod.method2("method2");
    }
}
```



#### 伪范型









#### 类型擦除(type erasue)

##### 例子

> 写几个例子，反编译查看

例一：

```java
final List<String> list = new ArrayList<>(10);
list.add("可可爱爱");
System.out.println(list.get(0));
```

反编译后：

```java
List list = new ArrayList(10);
list.add("\u53EF\u53EF\u7231\u7231");
System.out.println((String)list.get(0));
```

> 编译过后List<String>的类型被擦除，也就是说只有List类型，而不存在List<String>这个类型。中间对于元素的具体操作，通过类型转化实现。

例二：

```java
/**
 * 指定范型类型，那么CompareTo方法也必须指定类型，如果不指定那么就会替换为其左边界Object
 * 编译后Comparable<MyNumericValue>的类型被擦除，变为Comparable<Object>
 * 那么直接导致未能实现compareTo方法
 * 编译器检测到了，就给生成一个桥接方法compareTo(Object object)
 */
class MyNumericValue implements Comparable<MyNumericValue> {
    private int value;
    @Override
    public int compareTo(MyNumericValue o) {
        return this.value - o.value;
    }
}
class MyNumericValue2 implements Comparable {
    private int value;
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
```

反编译查看：

```java
class MyNumericValue implements Comparable {
    MyNumericValue() {
    }
    public int compareTo(MyNumericValue o) {
        return value - o.value;
    }
    public volatile int compareTo(Object obj) {
        return compareTo((MyNumericValue) obj);
    }
    private int value;
}
//
class MyNumericValue2 implements Comparable {
    MyNumericValue2() {
    }
    public int compareTo(Object o) {
        return 0;
    }
    private int value;
}
```

> 对于一些范型接口的使用，如果接口的抽象方法的入参、出参是范型类型的话，如果在类型擦除时导致未能实现接口方法时，需要编译器生成桥接方法，通过此桥接方法调用原始方法。

例三：

```java
public <T extends List<E>, E extends Comparable<E>> void max(List<E> list) {
    E max = list.get(0);
    for (E e : list) {
        if (e.compareTo(max) > 0) {
            max = e;
        }
    }
    System.out.println("最大值=>" + max);
}
```

反编译查看：

```java
public void max(List list) {
    Comparable max = (Comparable) list.get(0);
    Iterator iterator = list.iterator();
    do {
        if (!iterator.hasNext())
            break;
        Comparable e = (Comparable) iterator.next();
        if (e.compareTo(max) > 0)
            max = e;
    } while (true);
    System.out.println((new StringBuilder()).append("\u6700\u5927\u503C=>").append(max).toString());
}
```

>  E extends Comparable<E>限定了范型边界，那么首先将所有的范型E替换为最左边界 Comparable<E>，然后进行类型擦除得到最终擦除后的结果 Comparable。
>
> 增强for循环底层使用的是迭代器进行遍历。

##### 小结

> 类型擦除指的是通过类型参数合并，将范型类型实例关联到一份字节码文件上，编译器只为范型类生成一份字节码文件。
>
> 类型擦除的过程中jvm会将范型java代码转化为普通java代码，编译器直接操作的是字节码。

#### 范型带来的问题

> java的违范型机制，类似于语法糖，它方便了我们编写代码，并提高了代码的可复用性，那么复杂的工作就是由jvm待做。

###### 重载

> 重载的条件是方法名相同、参数列表不同。那么如果对于相同的方法名，使用List<String>、List<Integer>，可以进行重载么？
>
> 显然是不行的，因为之前我们就说过jvm中只有List这一种类型，并不存在List<String>、List<Integer>,因为编译会进行类型擦除，

idea直接提示，两个方法拥有相同的范型，编译不可通过。

`'method1(List<String>)' clashes with 'method1(List<Integer>)'; both methods have same erasure`

```java
public void method1(List<String> list){
}
public void method1(List<Integer> list){
}
```

#### 范型通配符

##### 常用泛型符号

> 范型符号都有对应含义，常用范型符号如下：

- E     element    (在集合框架中表示元素的意思)

- T     Type    java类
- K     key   (键值)
- V     value   （value值）
- N   number    数值类型

> 对于普通泛型符号(除？以外)，必须预先声明才可以使用

<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208281522005.png" alt="image-20220828152230268" style="zoom:50%;" />

？   未知java类型（无限制通配符类型）

> 如下List<?> list 表示此方法可以接受任意类型的集合对象。
>
> 由于无从获知list内元素具体类型，因此get出来的元素都是Object的，且不可添加元素

```java
class  TestClass2 {
    public void test(List<?> list){
        //list.add("");
        final Object o = list.get(0);
        System.out.println(o);
    }
    public static void main(String[] args) {
        final TestClass2 testClass2 = new TestClass2();
        testClass2.test(new ArrayList<Integer>(Arrays.asList(1,2)));
        testClass2.test(new ArrayList<String>(Arrays.asList("1","2")));
        testClass2.test(new ArrayList<Comparable>(Arrays.asList(1,2)));
    }
}
```



##### 限定通配符



###### 例子

> 对于非限定通配符来说，它不限制类型，编译前也可以通过类型检查，业务过程手动强制转化可能出现`ClassCastException`异常。
>
> 先看一个例子：

```java
class TestExtendsA<T> {
    /**
     * 此方法目的是接受两个参数进行比较
     */
    void funA(T t1, T t2) {
        //强转
        Comparable c1 = (Comparable) t1;
        Comparable c2 = (Comparable) t2;
        System.out.println(c1.compareTo(c2));
    }
    public static void main(String[] args) {
        final TestExtendsA<Fruit> fruitTestExtendsA = new TestExtendsA<>();
        //classCastException
        fruitTestExtendsA.funA(new Fruit(), new Fruit());
    }
}
```

主要看list3  和  listB3的对比

list3指定范型方法类型为Comparable，而传入的是String通过不了类型检查。

lstB3指定范型为Comparable及其派生类，传入的String符合要求

```java
class TestAB {

    static void funA(List<Comparable> list) {
        final Comparable comparable = list.get(0);
    }

    static void funB(List<? extends Comparable> list) {
        final Comparable comparable = list.get(0);
    }

    public static void main(String[] args) {
        //没有问题
        final ArrayList list1 = new ArrayList<>(Arrays.asList("1", "2", "3"));
        funA(list1);

        //ClassCastException
        final ArrayList list2 = new ArrayList<>(Collections.singletonList(new StringBuilder()));
        funA(list2);

        //通过不了类型检查
        final ArrayList<String> list3 = new ArrayList<>(Arrays.asList("1", "2", "3"));
        funA(list3);

        //没有问题
        final ArrayList listB1 = new ArrayList<>(Arrays.asList("1", "2", "3"));
        funB(listB1);

        //ClassCastException
        final ArrayList listB2 = new ArrayList<>(Collections.singletonList(new StringBuilder()));
        funB(listB2);

        // 可以
        final ArrayList<String> listB3 = new ArrayList<>(Arrays.asList("1", "2", "3"));
        funB(listB3);
    }
}
```



###### 上界



> 表示上界，<? extends T>,可接收的泛型类型必须为T或T的派生类（可以是接口、也可以是子类）这里没有用任何意义上的继承关系

> <? extends T>表示可接收任意T即T的派生类类型。

> 用于取值。不可放值。

> <? extends T>编译时类型擦除到上边界，即用T代替范型，这时需要一次向上转型，运行时将其当作T来处理。

![image-20220828164524819](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208281645961.png)

> 为何作为上界不可存值

List<String>只能添加String类型，而编译期间类型会被擦除，当作Comparable来处理，那么添加的类型就不确定了。





###### 下界

> 表示下界,<？  super T>,   即可接受的类型必须为T或T的父类型
>
> 泛型类型必须为T或T的父类。
>
> <? super T>表示可接收任意T即T的父类类型。
>
> 用于存值。
>
> <? super T>编译时类型擦除到最左边界，最右边界是Object，即用Object代替范型，运行时将其当作Object来处理。

```java
class TestSG{

    static void superGet(List<? super String> list){
        list.add("z");
        //get得到的时String及String的父类，不能确定具体类型，没有意义
        final Object s = list.get(0);
    }

    public static void main(String[] args) {
        final List<Integer> integers = new ArrayList<>(Arrays.asList(1,2,3));
        final List<Comparable> comparableList = new ArrayList<>(Arrays.asList(1,2,3));
        // 报错
        // superGet(integers);
        superGet(comparableList);
        comparableList.forEach(System.out::print);
    }
}
```

> 关于存取值问题

![image-20220828170849157](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208282256806.png)

小结：

> 如果对于泛型没有限制，并且集合类型支持存取的话，直接使用非限定通配符T E等。
>
> 如果对于泛型有限制，则使用限定通配符 extents支持取，super支持存。
>
> ？对于取是不友好的，因为？表示任意java类型，那么取出来的一定是Object，而Object没有意义，强转存在ClassCastExpression异常风险。

###### 多限制

> 使用  `&` 表示多限制

```java
static <T extends Comparable<? super T> & CharSequence> void method(T t) {
    System.out.println(t);
}

public static void main(String[] args) {
    method("123");
}
```



##### 范型数组

> 如何定义范型数组？

方式一：

```java
//ok
final Demo[] demos1 = new Demo[10];
//ok
final Demo<?>[] demos2 = new Demo<?>[10];

//错误 Generic array creation
// final Demo<?>[] demos3 = new Demo<String>[10];

//可以  需要强转
final Demo<String>[] demos4 = (Demo<String>[]) new Demo<?>[10];


@Data
class Demo<T> {
    T t;
}
```

方式二：

```java
static <T> T[] createFunc(T... t) {
    return t;
}

static <T> void printFunc(T[] tArr) {

    for (int i = 0; i < tArr.length; i++) {
        System.out.print(tArr[i]);
    }
    System.out.println("    "+tArr.getClass());
}

public static void main(String[] args) {
    printFunc(createFunc(1, 2, 3, 4, 5, 6));

    printFunc(createFunc("a", "b", "c", "d", "e", "f"));
}
```

![image-20221026154852124](java成神之路(基础).assets/image-20221026154852124.png)

> 使用。

```java
(String[]) Array.newInstance(String.class, 10);

static <T> T[] createArray(Class<T> type, int size) {
    return (T[]) Array.newInstance(type, 10);
}
```



### 设计模式

> 简单了解

#### 代理模式

> 框架的灵魂就是代理＋反射，代理和反射几乎是所有框架的基础。了解一下代理模式。

##### 静态代理

> 要求代理角色和真实角色实现同一接口、代理角色将真实角色组合进来、代理角色对真实角色方法进行增强。

###### 简单实现

- 定义抽象接口

  ```java
  public interface SellGoods {
      void doSell();
  }
  ```

- 定义真实角色

  ```java
  public class GoodsOwner implements SellGoods {
      @Override
      public void doSell() {
          System.out.println("小杨哥的品牌方");
      }
  }
  ```

- 代理角色

  ```java
  @Data
  public class XiaoYangGe implements SellGoods {
      //小杨哥代理的品牌方
      private SellGoods sellGoods;
      @Override
      public void doSell() {
          prev();
          sellGoods.doSell();
          last();
      }
      private void prev(){
          System.out.println("测评、口碑、流量");
      }
      private void last(){
          System.out.println("质保");
      }
      @Test
      public void test(){
          //代理对象
          final XiaoYangGe xiaoYangGe = new XiaoYangGe();
          //代理对象组合真实对象
          xiaoYangGe.setSellGoods(new GoodsOwner());
          //代理对象对真实对象做增强
          xiaoYangGe.doSell();
      }
  }
  ```

![image-20220829140457256](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208300005131.png)

###### 优缺点

优点：

- 简单、直观

缺点：

- 只能代理一类角色，不通用，不利于扩展



##### 动态代理

> 动态代理的目的就是为了解决静态代理的却点。达到通用且易于扩展。

分为

- jdk动态代理

- CGLIB动态代理

Jdk动态代理，借助Proxy类和InvocationHandler接口，实现动态生成代理对象的能力。

Cglib动态代理，运行时在内存中生成一个子类对象，实现对目标对象的代理。

注意点：

jdk动态代理有一个限制，那就是被代理类必须实现一个或多个接口。

如果被代理类没有实现接口的化可以使用Cglib动态代理。

###### jdk动态代理

> jdk动态代理需要借助一个类Proxy和一个接口InvocationHandler。
>
> Proxy用于生成代理对象，InvocationHandler称为代理对象的调用处理程序。

以下实现将 SellGoods 替换为 Object，则可以代理所有实现接口的类的实例。

实现：

```java
@Data
public class SellGoodsInvocationHandler implements InvocationHandler {
    //代理谁？
    private SellGoods sellGoods;
    /**
     * 生成代理对象
     */
    public Object newProxyInstance() {
        return Proxy.newProxyInstance(sellGoods.getClass().getClassLoader(), sellGoods.getClass().getInterfaces(), this);
    }
    /**
     * 代理对象对真实对象增强
     *
     * @param proxy  代理对象
     * @param method 被代理对象method实例，内部包含被代理对象方法信息
     * @param args   method参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //代理对象实现接口
        System.out.println("代理对象实现接口:");
        for (Class<?> anInterface : proxy.getClass().getInterfaces()) {
            System.out.println(anInterface.getName());
        }
        prev();
        //invoke 第一个参数：被代理对象实例，第二个参数，被代理对象方法参数
        Object returnVal = method.invoke(sellGoods,args);
        last();
        return returnVal;
    }
    private void prev() {
        System.out.println(sellGoods.getClass().getName() + "类被代理，前置方法");
    }
    private void last() {
        System.out.println(sellGoods.getClass().getName() + "类被代理，后置方法");
    }
    @Test
    public void test(){
        final SellGoodsInvocationHandler sellGoodsInvocationHandler = new SellGoodsInvocationHandler();
        sellGoodsInvocationHandler.setSellGoods(new GoodsOwner());
        final SellGoods sellGoodsProxy = (SellGoods)sellGoodsInvocationHandler.newProxyInstance();
        sellGoodsProxy.doSell();
    }
}
```

![image-20220829171302109](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208300005024.png)

###### CGLIB

> 当JDK动态代理用不了了，可以用CGLIB动态代理。
>
> CGLIB(Code Generation Library) 是一个第三方代码生成类库，运行时在内存中动态生成一个子类对象从而实现对目标对象功能的扩展。
>
> CGLIB动态代理是通过继承来实现对目标对象的增强的，所以某个被final修饰的方法，是无法被代理的。

实现：

> 一个类，被代理类，一个被final修饰的方法，一个普通方法

```java
public class Obj {
    /**
     * 定义两个方法
     * 一个是普通方法，一个是被final修饰的方法
     */
    final public void doSomeThing() {
        System.out.println("被final修饰的方法");
    }

    public void doOtherThing() {
        System.out.println("普通方法");
    }
}
```

> 拦截器

```java
public class MyMethodInterceptor implements MethodInterceptor {
    /**
     * 拦截方法
     *
     * @param o           the enhanced obj  已被增强对象
     * @param method      intercepted Method 被拦截方法
     * @param objects     参数列表，基本类型会替换为包装类型
     * @param methodProxy 代理方法
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("被代理方法名称:" + method.getName());
        System.out.println("=====开始增强=====");
        final Object o1 = methodProxy.invokeSuper(o, objects);
        System.out.println("=====增强结束=====");
        return o1;
    }
}
```

> 生成代理对象

```java
@Data
public class CgLibProxy {
    private Class<? extends Object> object;
    public Object getProxy() {
        //创建Enhancer对象，类似于JDK动态代理的Proxy类
        Enhancer enhancer = new Enhancer();
        //设置目标类的字节码文件,很明显使用反射实现
        enhancer.setSuperclass(object);
        //设置借助哪个拦截器实现（也就是做哪些增强）
        enhancer.setCallback(new MyMethodInterceptor());
        //这里的creat方法就是正式创建代理类
        return enhancer.create();
    }
}
```

> 测试：

- 可实现增强
- 不可增强被final修饰的方法

```java
public static void main(String[] args) {
    //在指定目录下生成动态代理类，我们可以反编译看一下里面到底是一些什么东西
    System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\java\\java_workapace2");
    CgLibProxy cgLibProxy = new CgLibProxy();
    cgLibProxy.setObject(Obj.class);
    //这里的creat方法就是正式创建代理类
    Obj proxy = (Obj)cgLibProxy.getProxy();
    //调用代理类的final方法
    proxy.doSomeThing();
    //调用代理类的非final方法
    proxy.doOtherThing();
}
```



### 序列化

> 序列化是指将对象的状态信息转化为可存储或可传输的形式的过程。用于网络传输和RPC调用过程中的信息媒介。反序列化是序列化的逆过程。

#### java的序列化

> java中对象信息都存储与jvm运行时堆内存中，一旦jvm停止运行，java对象信息也将丢失。序列化是一种持久化手段，可以将对象信息以文件的信息存储于磁盘中，当我们想再次使用的时候可以将序列化的信息读出来，反序列化还原对象信息。

- java想要实现序列化必须实现Serializable接口
- java序列化存储的是对象信息，只存储成员变量信息，类变量信息不会被存储
- 借助ObjectOutputStream和ObjectInputStream对对象进行序列化和反序列化
- 序列化与反序列化要求SerialVersionUID序列化与反序列化前后必须相同
- Transient表示瞬时的意思，即被它修饰的属性不会参与序列化过程
- 可以在类中添加writeObject和readObject方法来定制对象的序列化和反序列化
- 可序列化类的子类都可序列化

##### Serializable接口

> Java中想要实现序列化必须实现Serializable接口。如果没有实现序列化接口却尝试使用序列化的话会报出`NotSerializableException`异常。
>
> `Serializable`接口没有任何方法，类似于一个标识接口，但是想要实现序列化必须实现此接口。当我们没有定制序列化时默认使用`ObjectOutputStream的defaultWriteObject()`和`InputOutputStream的defaultReadObject()`方法进行序列化。
>
> 序列化时想要序列化父类属性，那么父类也必须实现序列化接口

例子：序列化时想要序列化父类属性，那么父类也必须实现序列化接口

```java
@Data
public class ClassParent implements Serializable {
  private static final long serialVersionUID = 1L;
    public String value1;
}
@Data
@ToString(callSuper = true)
class ClassSon extends ClassParent {
  private static final long serialVersionUID = 1L;
    public String value2;
}

```

```java
/**
 * 想要序列化父类属性，父类也需要实现序列化接口
 */
@Test
public void test2() {
    String filePath = "/Users/rolyfish/Desktop/MyFoot/foot/testfile";
    final ClassSon classSon = new ClassSon();
    classSon.setValue1("Parent");
    classSon.setValue2("Son");
    try (final ObjectOutputStream objectOutputStream =
                 new ObjectOutputStream(new FileOutputStream(new File(filePath, classSon.getClass().getSimpleName() + ".txt")))) {
        objectOutputStream.writeObject(classSon);
        objectOutputStream.flush();
    } catch (IOException e) {
    }

    try (final ObjectInputStream objectInputStream =
                 new ObjectInputStream(new FileInputStream(new File(filePath, classSon.getClass().getSimpleName() + ".txt")))) {
        final Object o = objectInputStream.readObject();
        System.out.println(o);
    } catch (IOException e) {
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
}
```

![image-20220830010017176](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208300100164.png)

##### Externalizable

> `Externalizable`接口继承自`Serializable`接口，与`Serializable`接口不同的是`Externalizable`接口定义了两个方法，在使用`Externalizable`接口进行序列化操作的时候必须实现这两个方法，否则序列化操作后所有属性都会变成默认值。

注意：

使用`Externalizable`接口进行序列化的时候，会调用类的无参构造器，再将对象的属性填充到此对象中，所以说使用`Externalizable`接口进行序列化需要一个public的无参构造器。

例子：

```java
@Data
public class ClassExternalizable implements Externalizable {
    private static final long serialVersionUID = -7287239868922811345L;
    String value;
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(value);
    }
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.value = (String) in.readObject();
    }
    @Test
    public void test2() {
        String filePath = "/Users/rolyfish/Desktop/MyFoot/foot/testfile";
        final ClassExternalizable classExternalizable = new ClassExternalizable();
        classExternalizable.setValue("ClassExternalizable");
        try (final ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(new File(filePath, ClassExternalizable.class.getSimpleName() + ".txt")))) {
            objectOutputStream.writeObject(classExternalizable);
            objectOutputStream.flush();
        } catch (IOException e) {
        }
        try (final ObjectInputStream objectInputStream =
                     new ObjectInputStream(new FileInputStream(new File(filePath, ClassExternalizable.class.getSimpleName() + ".txt")))) {
            final Object o = objectInputStream.readObject();
            System.out.println(o);
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

![image-20220830013944110](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208300139614.png)

##### SeriaVersionUID

> 序列号，在序列化的时候会将类的序列号存储，在反序列化的时候会检查序列号是否一致，如果一致才会进行接下来的序列化操作。如果不一致则会抛出`InvalidCastException`异常。

例子：

> 先执行testWrite ，然后修改SerialVersionUID，再执行testRead

```java
@Data
public class ClassSerialVersionUID implements Serializable {
    private static final long serialVersionUID = 2221061315871513751L;

    String value;
    @Test
    public void testWrite() {
        String filePath = "/Users/rolyfish/Desktop/MyFoot/foot/testfile";
        final ClassSerialVersionUID classSerialVersionUID = new ClassSerialVersionUID();
        classSerialVersionUID.setValue("classSerialVersionUID");
        try (final ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(new File(filePath, ClassSerialVersionUID.class.getSimpleName() + ".txt")))) {
            objectOutputStream.writeObject(classSerialVersionUID);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

```java
@Data
public class ClassSerialVersionUID implements Serializable {
    private static final long serialVersionUID = 123123151L;
    String value;
    @Test
    public void testRead() {
        String filePath = "/Users/rolyfish/Desktop/MyFoot/foot/testfile";
        try (final ObjectInputStream objectInputStream =
                     new ObjectInputStream(new FileInputStream(new File(filePath, ClassSerialVersionUID.class.getSimpleName() + ".txt")))) {
            final ClassSerialVersionUID classSerialVersionUID = (ClassSerialVersionUID) objectInputStream.readObject();
            System.out.println(classSerialVersionUID);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

> 会报出InvalidCastException异常，且友好提示二进制流中的序列号id和本地类中的序列号id不一致。

![image-20220830015417052](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208300154672.png)



###### SerialVersionUID哪来的

> 如果在编写java类时没有显示定义一个`SerialVersionUID`，那么编译器会为根据Class类的属性特征生成一个序列号，如果此类不发生改变那么经过多次编译也不会报错，如果此类发生改变（添加字段、修改字段），那么在反序列化时会报错。所以说一般我们会自定义一个序列号

借助idea生成序列号：

在设置的检查项可以设置缺省序列号时的告警级别，

![image-20220830020226044](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208300202768.png)

编写class类不添加序列号会有警告提示：

快捷键 opt + enter

![image-20220830020400580](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208300204003.png)



##### 定制序列化策略

> java在序列化的时候会首相检查，当前类是否存在`writeObject`和`readObject`方法，如果有则优先使用自定义的序列化策略，否则才会使用默认的`defaultWriteObject  和 defaultReadObject`方法。
>
> 所以定制序列化策略也就是自己添加`writeObject 和 readObject`方法。

###### ArrayList

> 首先可以查看一下ArrayList的序列化策略。
>
> ArrayList底层是一个对象数组，且该属性被transient修饰，也就是不参与序列化，那么他是如何保存对象信息的呢？就是自定义序列化策略，ArrayList添加了`writeObject 和 readObject`方法。且ArrayList优化了序列化策略，它只会保存非null元素，而null元素则会被忽略。

###### 自定义序列化策略

> 上面我们聊Externalizable时就已经涉及了。

```java
@Data
public class ClassCustomizeSerializable implements Serializable {
    private static final long serialVersionUID = 2936590571416558935L;
    transient Date date;
    String value;
    private void writeObject(ObjectOutputStream out)
            throws java.io.IOException {
        out.writeObject(Optional.ofNullable(date).orElse(Calendar.getInstance().getTime()));
        out.writeObject(value);
    }
    private void readObject(ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        final Object o1 = in.readObject();
        final Object o2 = in.readObject();
        date = (Date) ((o1 instanceof Date) ? o1 : o2);
        value = (String) ((o1 instanceof String) ? o1 : o2);
    }
    @Test
    public void test() {
        final ClassCustomizeSerializable classCustomizeSerializable = new ClassCustomizeSerializable();
        classCustomizeSerializable.setValue("classCustomizeSerializable");
        String filePath = "/Users/rolyfish/Desktop/MyFoot/foot/testfile";
        try (final ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(new File(filePath, ClassCustomizeSerializable.class.getSimpleName() + ".txt")))) {
            objectOutputStream.writeObject(classCustomizeSerializable);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (final ObjectInputStream objectInputStream =
                     new ObjectInputStream(new FileInputStream(new File(filePath, ClassCustomizeSerializable.class.getSimpleName() + ".txt")))) {
            ClassCustomizeSerializable o = (ClassCustomizeSerializable) objectInputStream.readObject();
            System.out.println(o);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

![image-20220830022442495](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208300224278.png)

### 注解

> Java 注解（Annotation）又称 Java 标注，是 JDK5.0 引入的一种注释机制。

从以下几点了解：

- 注解语法
- 元注解
- java内置注解
- 注解的继承性
- 简单使用

#### 注解语法

> 注解的定义很简单，使用`@interface`声明，表示一个注解。

```java
public @interface MyDefinitionAnnotation {
}
```

> 反编译查看，可得结论
>
> - 所定义的注解就是一个接口
> - 此接口继承自`Annotation`接口
>
> 所以定义注解时无需关系底层实现，编译器和虚拟机会帮我们完成底层的实现。

jad反编译：

```java
public interface MyDefinitionAnnotation extends Annotation{
}
```



#### 元注解

> 元注解起到对其他注解进行说明的作用，可以定义其他注解

元注解有四个：

- @Target
- @Retention
- @Documented
- @Inherited

##### @Documented&@Inherited 

> 这两个注解分别表示，是否在JavaDoc中保存注解和是否允许子类继承父类注解。
>
> 这两个注解没有内部属性，都被
>
> ```java
> @Retention(RetentionPolicy.RUNTIME)
> @Target(ElementType.ANNOTATION_TYPE)
> ```
>
> 注释，保留策略为Runtime，即会被VM加载进内存，可反射获取、类型属性为`ElementType.ANNOTATION_TYPE`即只可定义在注解上。

##### @Target

> @Target注解只可用于注解类型上、可保留进javadoc、保留策略为RUNTIME。
>
> 此注解有一个属性，为数组表示被@Target修饰的注解可用于什么地方，如果不使用@Targer注释则表示该注解可用于任何地方。

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Target {
    ElementType[] value();
}
```

> ElementType枚举值说明：

常用的：TYPE、FIELD、METHOD

```java
public enum ElementType {
    /** Class, interface (including annotation type), or enum declaration */
    //标注此注解可用于 类、接口、注解、以及枚举上（注解就是接口、枚举就是类）
    TYPE,
    /** Field declaration (includes enum constants) */
    //标注此注解可用于字段属性上
    FIELD,
    /** Method declaration */
    //标注此注解可用于方法上
    METHOD,
    /** Formal parameter declaration */
    //标注此注解可用于方法参数上，比如@Valid @RequestBody
    PARAMETER,
    /** Constructor declaration */
    //标注此注解可用于构造方法上
    CONSTRUCTOR,
    /** Local variable declaration */
     //标注此注解可用于本地变量
    LOCAL_VARIABLE,
    /** Annotation type declaration */
     //标注此注解可用于注解上，元注解都有此属性
    ANNOTATION_TYPE,
    /** Package declaration */
    //可用于package-info.java中
    PACKAGE,
    /**
     * Type parameter declaration
     *
     * @since 1.8
     */
    TYPE_PARAMETER,
    /**
     * Use of a type
     *
     * @since 1.8
     */
    TYPE_USE
}
```

##### @Retention

> 保留策略，注解只是保留在代码中、还是编译进class文件中、还是在运行期间保留在虚拟机中（可以通过反射访问）
>
> 有一个RetentionPolicy value属性，RetentionPolicy 是一个枚举类型。

```java
public enum RetentionPolicy {
    //表示只保留在javaDoc中，会被编译器忽略，被编译器忽略自然也不会加载进虚拟机
    SOURCE,
    //表示会被编译器编译生成class文件，但不会由VM在运行时保留
    CLASS,
    //会编译、也会由VM在运行时保留，注解为此保留策略可通过反射获取注解信息
    RUNTIME
}
```



####  java内置注解

> Java内部定义了一套注解，共有10个，6 个在 java.lang 中，剩下 4 个在 java.lang.annotation 中，
>
> 除了三面提供的四个元注解(四个元注解都在 java.lang.annotation中)，还有6个在`java.lang`包下。

##### Deprecated

> 注释于构造器、属性、本地变量、方法、包、接口、方法上，表示过时的意思。
>
> 使用过时的类、方法、属性等，会有一个横线标识、不影响使用。

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
public @interface Deprecated {
}
```



##### @SuppressWarnings

> @SuppressWarnings("all")，抑制警告，`all`代表抑制所有警告，包括未检测警告、过时警告等。



#####  @Override

> 注解于方法上，表示重写方法。

##### @SafeVarargs

> 注释于构造方法或方法上，忽略任何使用参数为泛型变量的方法或构造函数调用产生的警告



##### @FunctionalInterface

> 注释于接口上，表示为一个函数式接口。

##### @Repeatable

> 注释于注解上，表示该注解可重复声明多次。

![image-20220830204849836](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208312252848.png)

使用:

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public   @interface Persons {
   Person[] value();
}
```

```java
@Repeatable(Persons.class)
public  @interface Person{
    String role() default "";
}
```

```java
@Person(role = "男的")
@Person(role = "打工族")
public class MeClass {
}
```



#### 注解的继承

> 注解的继承是指什么意思？

首先看一个例子：

结论表明如果一个注解被@Inherited 注释的话，那么子类可以继承得到父类的注解

```java
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited // 声明注解具有继承性
@interface AnnotationInherited {
    String value() default "";
}
```

```java
@AnnotationInherited
public class SuperClass {
}
public class SonClass extends SuperClass{
}
```

```java
public static void main(String[] args) {
    System.out.println(Arrays.asList(SonClass.class.getAnnotations()));
}
```

![image-20220831224956656](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208312250324.png)



#### 简单使用

> 定义一个注解最重要的就是设置@Target和@Retention。分别表示该注解可以放在哪里和该注解的保留策略。

- RetentionPolicy.RUNTIME   会编译、也会由VM在运行时保留，注解为此保留策略可通过反射获取注解信息
- @Target如果不指定，表示该注解可放于任何地方。如果指定那么就只可以放在指定地方

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyDefinitionAnnotation {
}
```

##### 通过反射获取注解信息

> 如果不设置@Retention(RetentionPolicy.RUNTIME)的话是获取不到注解信息的

```java
@MyDefinitionAnnotation
public class MyTestClass {   
}
public static void main(String[] args) {
    final Annotation[] annotations = MyTestClass.class.getAnnotations();
    for (Annotation annotation : annotations) {
        System.out.println(annotation.annotationType().getSimpleName());
    }
}
```

![image-20220830230013493](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208312252822.png)

##### 注解可定义属性并赋默认值

> 注解可定义属性并可以给属性赋予默认值，可以通过属性来控制，类、方法、字段的行为。

以下例子我们通过AnnotationWithValue注解实现了类似于@Value注解的作用

```java
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationWithValue {

    String value() default "默认值";

}
```

```java
@Data
public class TestClass {

    @AnnotationWithValue(value = "注解赋值")
    public String value1;

    @AnnotationWithValue
    public String value2;

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {

        final TestClass testClass = new TestClass();
        final Field value1 = TestClass.class.getField("value1");
        final String str1 = value1.getAnnotation(AnnotationWithValue.class).value();
        value1.set(testClass,str1);
        final Field value2 = TestClass.class.getField("value2");
        final String str2 = value2.getAnnotation(AnnotationWithValue.class).value();
        value2.set(testClass,str2);
        System.out.println(testClass);
    }
}
```

![image-20220831000337366](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208312252339.png)



#### 注解+反射实现自动装配

> Spring的自动装配原理就是  注解 + 反射，自动装配就是获取字段上的@Value、@AutoWrite注解并进行赋值操作。实现的步骤如下：

- 想使用反射创建对象的化，即必须获取待装配的，类的全限定名。即如何扫描待装配的类
- 自定义一套注解
- 最后就是创建bean并装配属性

#####  如何扫描类

> 如何扫描待装配的类。这里的思路是，首先有一个启动类，获取启动类所在目录及其子目录下所有类全限定名称，放入一个List中。
>
> 方式为文件操作。

下面的方法目的是为了获取启动类所在目录，以便后面扫描此路径。

- 获取路径
- 将 .  替换为 \

```java
public static void run() throws FileNotFoundException {
    //获取类路径，到****/classes/
    final String classPath = ResourceUtils.getURL("classpath:").getPath();
    //获取package名com.xx.xx.xx
    final String packageName = ScannerPackage.class.getPackage().getName();
    //replace正则匹配进行替换，. --> \\.   File.separator 在win下为\会被当成转译字符
    final String packageNameNew =
            packageName.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
    //当前类所在包路径
    String rootPath = String.join("", classPath, packageNameNew);
    final File rootFile = new File(rootPath);
    dir(Collections.singletonList(rootFile));
    for (String path : classPaths) {
        System.out.println(path);
    }
}
```

记录所有类全限定名称：

```java
public static List<String> classPaths = new ArrayList<>();
/**
 * 扫描某路径下的所有文件
 */
public static void dir(List<File> dirList) {
    //遍历当前类，将文件分组，文件夹一组、非文件夹一组。非文件夹记录进集合，文件夹继续操作
    final HashMap<Boolean, List<File>> fileMap =
            dirList.stream().collect(Collectors.groupingBy(File::isDirectory, HashMap::new, Collectors.toList()));
    //文件
    final List<File> fileList = fileMap.get(false);
    final List<File> dir2List = fileMap.get(true);
    files(Optional.ofNullable(fileList).orElse(Collections.emptyList()));
    if (!CollectionUtils.isEmpty(dir2List)) {
        //文件夹
        for (File file : dir2List) {
            dir(Arrays.asList(Optional.ofNullable(file.listFiles()).orElse(new File[0])));
        }
    }
}
public static void files(List<File> fileList) {
    final List<String> fileNameList = fileList.stream().map(file -> {
        //得到 com/xx/xx
        final String str1 = file.getPath().split("classes" + Matcher.quoteReplacement(File.separator))[1];
        final String str2 = str1.replaceAll(Matcher.quoteReplacement(File.separator), ".");
        final String str3 = str2.substring(0, str2.lastIndexOf("."));
        return str3;
    }).collect(Collectors.toList());
    classPaths.addAll(fileNameList);
}
```

测试一下：

```java
public static void main(String[] args) throws FileNotFoundException {
    run();
}
```

![image-20220831161805033](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208312252895.png)



##### 自定义一套注解

> 我们已经获取启动类所在目录下的所有类的全限定的名称，那么创建类已经不是问题了。
>
> 接下来定义一套自己的注解。



######  RolyValue

> 模拟@Value

```java
/**
 * @Date: 2022/08/31/15:55
 * @Description: @Value替代品   可用于方法、字段上
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolyValue {
    String value() default "";
}
```

> 测试一下

创建一个Bean对象，存在三个属性

```java
public class RolyValueBean {
    @RolyValue(value = "RolyValue给的值value1")
    String value1;
    @RolyValue(value = "RolyValue给的值value2")
    String value2;
    
    String value3;
   //toString
}
```

单元测试，循环给字段赋值：

如果字段不是public的则需要设置AccessAble

```java
@Test
public void testRolyValue() throws IllegalAccessException {
    final RolyValueBean rolyValueBean = new RolyValueBean();
    System.out.println("原对象: ==>" + rolyValueBean);
    final Field[] declaredFields = rolyValueBean.getClass().getDeclaredFields();
    for (Field declaredField : declaredFields) {
        if (!declaredField.isAccessible()) {
            declaredField.setAccessible(true);
        }
        final RolyValue rolyValue = declaredField.getAnnotation(RolyValue.class);
        if (null != rolyValue) {
            declaredField.set(rolyValueBean, rolyValue.value());
        }
    }
    System.out.println("处理后: ==>" + rolyValueBean);
}
```

![image-20220831163503151](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208312253378.png)

###### RolyComponent

> 自定义组件注解，模拟@Component。

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolyComponent {

}
```



###### RolyBean

> 模拟@Bean

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolyBean {
	//bean名称
    String value() default "";
}
```



###### RolyValid

> 模拟@Valid 。此注解作用于方法字段上，判断方法类型。

```java
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolyValid {
    Class<? extends Object> value() default Object.class;
}
```

测试:

定义这么一个方法：使用@RolyValid限定此方法参数类型为String。这里故意给一个StringBuilder

```java
public void method(@RolyValid(value = String.class) StringBuilder sb){
}
```

```java
public void testRolyValid() throws IllegalAccessException {
    final Method[] declaredMethods = RolyValueBean.class.getDeclaredMethods();
    for (Method declaredMethod : declaredMethods) {
        if (declaredMethod.isAccessible()) {
            declaredMethod.setAccessible(true);
        }
        final Parameter[] parameters = declaredMethod.getParameters();
        for (Parameter parameter : parameters) {
            final RolyValid declaredAnnotation = parameter.getDeclaredAnnotation(RolyValid.class);
            if (null != declaredAnnotation) {
                if (!parameter.getType().equals(declaredAnnotation.value())) {
                    throw new RuntimeException(declaredMethod.getName() +
                            "方法参数不合法:" +
                            "require:" + declaredAnnotation.value() +
                            "given:" + parameter.getType());
                }
            }
        }
    }
}
```

![image-20220831170816999](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208312253494.png)



##### 实现

> 目录结构：

![image-20220831231917742](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208312319401.png)



> 定义两个个待初始化类

```java
@RolyComponent
public class UserDao {
    @RolyValue("@RolyValue给的值")
    String name;
    //没有设置属性
    String other;
}
@RolyService
public class UserService {
    @RolyValue("@RolyValue给的值")
    String name;
    String other;
}
```

> bean工厂或者叫做上下文，负责初始化bean并加入容器。

下面省略的方法即是获取启动类所在目录的class全限定名称，在上文都有提到

```java
public class BeanFactory {
    //扫描启动类所在包下所有类，将类的全限定名称保存在此list中
    public static List<String> classPaths = new ArrayList<>();
    //bean工厂
    private static Map<String, Object> beanFactory = new HashMap<>();
    static {
        try {
            init();
            initBean();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void init() throws FileNotFoundException {
       。。。
    }

    /**
     * 扫描某路径下的所有文件
     */
    public static void dir(List<File> dirList) {
   。。。
    }

    public static void files(List<File> fileList) {
     。。。
    }

    public static void initBean() {
        System.out.println("+++++++++++++创建类放入bean容器中++++++++++++");
        for (String classPath : classPaths) {
            createBeanByName(classPath);
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
    }

    private static void createBeanByName(String classPath) {
        Class<?> beanClass;
        try {
            //默认触发初始化
            beanClass = Class.forName(classPath);
            //判断是否需要创建
            if (!shouldInit(beanClass)) {
                return;
            }
            final Object bean = beanClass.newInstance();
            //注入属性
            initProperty(bean);
            beanFactory.put(beanClass.getSimpleName(), bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initProperty(Object bean) throws IllegalAccessException {
        //获取所有的Field
        final List<Field> fields = Arrays.asList(bean.getClass().getDeclaredFields());

        for (Field field : fields) {
            //设置允许访问
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            //获取RolyValue注解的属性
            final RolyValue declaredAnnotation = field.getDeclaredAnnotation(RolyValue.class);
            if (null != declaredAnnotation) {
                field.set(bean, declaredAnnotation.value());
            }

        }
    }

    /**
     * 判断beanClass是否需要 自动注入 也就是是否包含@RolyComponent注解
     *
     * @param beanClass
     * @return
     */
    public static boolean shouldInit(Class beanClass) {

        final List<Annotation> annotations = Arrays.asList(beanClass.getDeclaredAnnotations());
        if (annotations.isEmpty()) {
            return false;
        } else {
            final List<? extends Class<? extends Annotation>> annotationTypes = annotations.stream().map(Annotation::annotationType).collect(Collectors.toList());
            if (annotationTypes.contains(RolyComponent.class)) {
                return true;
            }
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == RolyComponent.class) {
                    return true;
                }
                return shouldInit(annotation.annotationType());
            }
        }
        return false;
    }
    public static Map<String, Object> objList() {
        return beanFactory;
    }
    public static Object getObjByName(String name) {

        return beanFactory.get(name);
    }
}
```

> 启动类，负责触发类的初始化

这里容器里的bean都是单例的，也可以自定义Scope注解来设置bean声明周期

```java
public class DemoApplication {
    /**
     * 启动类
     */
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        Class.forName("com.roily.booknode.javatogod._07Annotation.a03.demo.BeanFactory");
        System.out.println(BeanFactory.objList());
        System.out.println("容器中的bean实例都是单例的:" + BeanFactory.getObjByName("UserDao") == BeanFactory.getObjByName("UserDao"));
    }
}
```

![image-20220831231756424](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202208312317518.png)



### 时间处理

#### 时区

> 人们一般通过日出日落来定义时间，地球是圆的各个地方日出日落时间不等，所以定义时区概念，一个区域使用同一个时区。
>
> 地球被划分为24个时区，相邻时区之间时间相差一小时。（向西减一小时、向东加一小时）所以我们比漂亮国等西方国家时间要快。
>
> 但是呢，我们国家东西跨度很大，差不多横跨5个时区，但是为了方便管理，我们国家统一使用东八区。

#### 时间戳

> 时间戳是指格林威治时间1970-01-01 00：00：00（北京时间1970-01-01 08：00：00）起至现在的总毫秒数

```java
final Date time = Calendar.getInstance().getTime();
final long timeStamp = time.getTime();
System.out.println("当前时间：=>" + time);
System.out.println("当前时间戳：=>" + timeStamp);
final Date date = new Date(timeStamp + 1000);
System.out.println("使用时间戳创建日期：=>" + date);
```

![image-20220901111621031](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209042254952.png)

#### 格林威治时间

> 英文简称GMT。
>
> 用GMT + 8表示中国时间，中国位于东八区，时间比格林威治时间快8小时。
>
> GMT 已经被 UTC取代，可以理解为一个东西。

CST（China Standard Time）中国标准时间。

CST  =  GMT/UTC + 8



#### 时间格式化

> 可以实现Date ---> String，String  ---> Date。
>
> 可以将日期对象

##### SimpleDateFormat

> 使用SimpleDateFormat的format方法，将一个Date类型转化成String类型，并且可以指定输出格式。

```java
final Date time = Calendar.getInstance().getTime();
final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
final String format = simpleDateFormat.format(time);
System.out.println(format);
```

![image-20220901124353128](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209042253108.png)

##### 自定义输出格式

> 可以使用SimpleDateFormat自定义日期格式化输出格式。首先了解一下DateFormat给的模式字母。

A~Z 和a~z。其他未使用到的字母作为保留。

![image-20220901130030922](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209042253534.png)

> 常用的一个格式就是 年年年年-月月-日日  时时：分分：秒秒 对应模式字母表示就是：`yyyy-MM-dd hh:mm:ss`。
>
> 大小写不要混淆，大小写字母也对应着不同表示。
>
> 模式字母的个数代表输出字符串的长度。

例子：

```java
final Date time = Calendar.getInstance().getTime();
System.out.println("date的toString：===>" + time);

System.out.println("模式字符串:==>>  yyyy-MM-dd HH:mm:ss");
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
System.out.println(simpleDateFormat.format(time));

//从后往前截取  2022 ==>> 22
System.out.println("模式字符串:==>>  yy-M-dd HH:mm:ss");
simpleDateFormat = new SimpleDateFormat("yy-M-dd HH:mm:ss");
System.out.println(simpleDateFormat.format(time));

//w代表周  大写代表月中的周 小写代表年中的周
System.out.println("模式字符串:==>>  yyyy-MM-dd WW周/月 ww周/年 HH:mm:ss");
simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd WW周/月 ww周/年 HH:mm:ss");
System.out.println(simpleDateFormat.format(time));

//d代表天  大写代表年中的天 小写代表月中的天
System.out.println("模式字符串:==>>   yyyy-MM-dd DD天/年 dd天/月 HH:mm:ss");
simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd DD天/年 dd天/月 HH:mm:ss");
System.out.println(simpleDateFormat.format(time));

//F代表月份中的星期（不好用） 一般用EE代表周几
System.out.println("模式字符串:==>>  yyyy-MM-dd FF/月 EE/周 HH:mm:ss");
simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd FF/月 EE/周 HH:mm:ss");
System.out.println(simpleDateFormat.format(time));

//a上下午标志 am pm
System.out.println("模式字符串:==>>  yyyy-MM-dd aa HH:mm:ss");
simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd aa HH:mm:ss");
System.out.println(simpleDateFormat.format(time));

//小时数 HH 24小时制 hh 12小时制
System.out.println("模式字符串:==>>  yyyy-MM-dd HH:mm:ss aa hh:mm:ss");
simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa hh:mm:ss");
System.out.println(simpleDateFormat.format(time));

//ss秒数  SS毫秒数
System.out.println("模式字符串:==>>  yyyy-MM-dd HH:mm:ss aa hh:mm:ss");
simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SS毫秒");
System.out.println(simpleDateFormat.format(time));

//时区信息
System.out.println("模式字符串:==>>  yyyy-MM-dd zzzz ZZZZ");
simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd zzzz ZZZZ");
System.out.println(simpleDateFormat.format(time));
```

![image-20220901132440802](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209042253376.png)

##### 输出其他时区时间

> 指定时区输出时间。。

我们比纽约快了12小时

```java
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss 时区:zzzz");
System.out.println("系统时区:===>>" + simpleDateFormat.format(Calendar.getInstance().getTime()));
simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
System.out.println("America/New_York:===>>" + simpleDateFormat.format(Calendar.getInstance().getTime()));
```

![image-20220901133221969](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209042253676.png)



#####  DateFormart非线程安全

> DateFormat是非线程安全类，如果将DateFormait作为全局共享的格式化时间类的话，需要加锁。
>
> JDK文档明确指出，SimpleDateFormat不应该在多线程环境下使用。

![image-20220902170545946](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209132248912.png)

SimpleDateFormat非线程安全，必须为每一个线程创建独立的实例。如果必须同步使用一个日期格式，必须在外部加锁。

![image-20220902171057527](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209042253473.png)

具体原因就是Format个方法，每一个SimpleDateFormat内部维护一个Calandar实例，如果当前线程设置了calandar，没来得及返回，另一个线程就获得了该SimpleDateFormat实例，如此就会造成线程安全问题。

测试：

定义一个全局的SimpleDateFormat，作为全局共享的格式化时间工具类.

定义两个线程安全额的HashSet用于存放Calender和格式化后的日期字符串

定义1000个线程去格式化时间

> 可以发现格式化后的日期字符串没有达到预期数量，也就是多线程环境下，当前线程可能获取其他线程的数据。

```java
public class TestDFIsNotSyn {
    /**
     * 定义一个全局的SimpleDateFormat
     */
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final static Set<Calendar> calendars = Collections.synchronizedSet(new HashSet<>());
    final static Set<String> dates = Collections.synchronizedSet(new HashSet<>());
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            new Thread(() -> {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, finalI);
                calendars.add(calendar);
                final String format = simpleDateFormat.format(calendar.getTime());
                dates.add(format);
            }).start();
        }
        Thread.sleep(5000);
        System.out.println(calendars.size());
        System.out.println(dates.size());
    }
}
```

![image-20220901183737158](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209042253994.png)

#### java中时间处理

> java旧的关于时间处理的类Date涉及存在较大缺陷，因此后续版本中添加了Calendar和TimeZone类来完善对于时间的处理。

##### 旧

> 旧版本的时间处理类`java.util.Date`存在较大缺陷

- 结构很乱，在`java.util`和`java.sql`包下都存在`Date`类，名称相同，可读性不好。并且对于日期进行格式化的类在`java.text`包下。
- 不支持时区设置，`Java.util.Date`不提供国际化处理



##### 新

> java8中对时间处理进行了完善。集中在`java.time`包下

- Instant：时间戳
- Duration：时间段
- LocalDate：日期
- LocalTime：时间
- LocalDateTime：时间  日期
- Period：日期段
- 

```java
final Duration durationDay = Duration.ofDays(1);
System.out.println("间隔一天，多少秒：" + durationDay.get(durationDay.getUnits().get(0)));
System.out.println(24 * 60 * 60);

final Duration durationMin = Duration.ofMinutes(5);
System.out.println("间隔5分钟，多少秒：" + durationMin.get(durationMin.getUnits().get(0)));
System.out.println(5 * 60);

final Duration plus = durationMin.plus(1, durationDay.getUnits().get(0));
System.out.println("加一秒，多少秒：" + plus.get(plus.getUnits().get(0)));

final Duration minus = durationDay.minus(Duration.ofMinutes(10).getSeconds(), durationDay.getUnits().get(0));
System.out.println("减10分钟" + minus.getSeconds());
```

- LocalDate：只包含日期 年月日

```java
System.out.println("当下日期:=>"+LocalDate.now());
System.out.println("自定义日期:=>"+LocalDate.of(1999, Month.FEBRUARY,1));
System.out.println("withXXX修改日期:=>"+LocalDate.of(1999, Month.FEBRUARY,1).withMonth(12));
```

- LocalTime：当下时间   时分秒 精确到毫秒

```java
System.out.println("当下时间:=>"+ LocalTime.now());
System.out.println("自定义时间:=>"+LocalTime.of(12, 0,1,1));
System.out.println("withXXX修改时间:=>"+LocalTime.of(12, 0,1,1).withHour(13));
```

- LocalDateTime：localdate + localTime    年月日 时分秒

```java
System.out.println("当下时间:=>"+ LocalDateTime.now().toString().replaceAll("T"," "));
System.out.println("自定义时间:=>"+LocalDateTime.of(LocalDate.now(),LocalTime.now()));
System.out.println("withXXX修改时间:=>"+LocalDateTime.of(LocalDate.now(),LocalTime.now()).withDayOfMonth(1));
```

- Period： 日期段

```java
final Period period = Period.of(1, 05, 20);
System.out.println("创建时期:=>" + period);

final Period between = Period.between(LocalDate.of(1999, 5, 20), LocalDate.of(2022, 9, 2));
System.out.println("间隔日期,用于计算年龄:=>" + between);

final Period period1 = period.withDays(10);
System.out.println("withday修改day: =>" + period1);

final Period period2 = period.plusDays(1);
System.out.println("plus加一天、minu减一天: =>" + period2);
```

- ZoneOffset：时区偏移量

```java
System.out.println("默认时区偏移量：+》" + ZoneOffset.systemDefault());
```

- ZonedDateTime:带时区的时间

```java
System.out.println("带时区的时间：=》" + ZonedDateTime.now());
```

- Clock

```java
System.out.println("默认时钟:+>" + Clock.systemDefaultZone());
final Clock America = Clock.system(ZoneId.of("America/Los_Angeles"));
System.out.println("美国时钟:+>" + America);
final LocalDateTime now = LocalDateTime.now(America);
System.out.println("美国时间:+>" + now);
```



### 编码

> 编码规则是字节到字符、字符到字节的规则。如果规则不统一则会出现乱码的现在想。

#### ASCLL

> ASCII（ American Standard Code for InformationInterchange， 美国信息交换标准代码） 是基于拉丁字母的⼀套电脑编码系统， 主要⽤于显⽰现代英语和其他西欧语⾔。

标准ASCII 码也叫基础ASCII码， 使⽤7 位⼆进制数（ 剩下的1位⼆进制为0） 来表⽰所有的⼤写和⼩写字母， 数字0 到9、 标点符号， 以及在美式英语中使⽤的特殊控制字符。

其中：

0～31及127(共33个)是控制字符或通信专⽤字符（ 其余为可显⽰字符） ， 如控制符： LF（ 换⾏） 、 CR（ 回车） 、 FF（ 换页） 、 DEL（ 删除） 、 BS（ 退格)、 BEL（ 响铃） 等； 通信专⽤字符： SOH（ ⽂头） 、 EOT（ ⽂尾） 、 ACK（ 确认） 等；

ASCII值为8、 9、 10 和13 分别转换为退格、 制表、 换⾏和回车字符。 它们并没有特定的图形显⽰， 但会依不同的应⽤程序，⽽对⽂本显⽰有不同的影响

32～126(共95个)是字符(32是空格） ， 其中48～57为0到9⼗个阿拉伯数字。

65～90为26个⼤写英⽂字母， 97～122号为26个⼩写英⽂字母， 其余为⼀些标点符号、 运算符号等。



#### Unicode

> Unicode又称万国码，是计算机领域的一项标准，囊括了世界上大部分文字系统。

ASCII码，只有256个字符，美国人倒是没啥问题了，他们用到的字符几乎都包括了，但是世界上不只有美国程序员啊，所以需要一种更加全面的字符集。

Unicode（中文：万国码、国际码、统一码、单一码）是计算机科学领域里的一项业界标准。它对世界上大部分的文字系统进行了整理、编码，使得计算机可以用更为简单的方式来呈现和处理文字。

Unicode伴随着通用字符集的标准而发展，同时也以书本的形式对外发表。Unicode至今仍在不断增修，每个新版本都加入更多新的字符。目前最新的版本为2018年6月5日公布的11.0.0，已经收录超过13万个字符（第十万个字符在2005年获采纳）。Unicode涵盖的数据除了视觉上的字形、编码方法、标准的字符编码外，还包含了字符特性，如大小写字母。

Unicode发展由非营利机构统一码联盟负责，该机构致力于让Unicode方案取代既有的字符编码方案。因为既有的方案往往空间非常有限，亦不适用于多语环境。

Unicode备受认可，并广泛地应用于计算机软件的国际化与本地化过程。有很多新科技，如可扩展置标语言（Extensible Markup Language，简称：XML）、Java编程语言以及现代的操作系统，都采用Unicode编码。

Unicode可以表示中文。



#### 有了Unicode为啥还需要UTF-8

> Unicode 是字符集。UTF-8 是编码规则。
>
> 由于Unicode囊括了大部分文字系统，所以对于单个文字的表示可能占有3到4个字节，而对于英文字符和一些简单的文字系统，就必须高位补0，极大浪费内存。
>
> UTF-8使用可变长度字节来储存 Unicode字符，例如ASCII字母继续使用1字节储存，重音文字、希腊字母或西里尔字母等使用2字节来储存，而常用的汉字就要使用3字节。辅助平面字符则使用4字节

广义的 Unicode 是一个标准，定义了一个字符集以及一系列的编码规则，即 Unicode 字符集和 UTF-8、UTF-16、UTF-32 等等编码规则。

Unicode 是字符集。UTF-8 是编码规则。

unicode虽然统一了全世界字符的二进制编码，但没有规定如何存储。

如果Unicode统一规定，每个符号就要用三个或四个字节表示，因为字符太多，只能用这么多字节才能表示完全。

一旦这么规定，那么每个英文字母前都必然有二到三个字节是0，因为所有英文字母在ASCII中都有，都可以用一个字节表示，剩余字节位置就要补充0。

如果这样，文本文件的大小会因此大出二三倍，这对于存储来说是极大的浪费。这样导致一个后果：出现了Unicode的多种存储方式。

UTF-8就是Unicode的一个使用方式，通过他的英文名Unicode Tranformation Format就可以知道。

==UTF-8使用可变长度字节来储存 Unicode字符，例如ASCII字母继续使用1字节储存，重音文字、希腊字母或西里尔字母等使用2字节来储存，而常用的汉字就要使用3字节。辅助平面字符则使用4字节。==

一般情况下，同一个地区只会出现一种文字类型，比如中文地区一般很少出现韩文，日文等。所以使用这种编码方式可以大大节省空间。比如纯英文网站就要比纯中文网站占用的存储小一些。

#### UTF8、UTF16、UTF32区别

Unicode 是容纳世界所有文字符号的国际标准编码，使用四个字节为每个字符编码。

UTF 是英文 Unicode Transformation Format 的缩写，意为把 Unicode 字符转换为某种格式。UTF 系列编码方案（UTF-8、UTF-16、UTF-32）均是由 Unicode 编码方案衍变而来，以适应不同的数据存储或传递，它们都可以完全表示 Unicode 标准中的所有字符。目前，这些衍变方案中 UTF-8 被广泛使用，而 UTF-16 和 UTF-32 则很少被使用。

UTF-8 使用一至四个字节为每个字符编码，其中大部分汉字采用三个字节编码，少量不常用汉字采用四个字节编码。因为 UTF-8 是可变长度的编码方式，相对于 Unicode 编码可以减少存储占用的空间，所以被广泛使用。

UTF-16 使用二或四个字节为每个字符编码，其中大部分汉字采用两个字节编码，少量不常用汉字采用四个字节编码。UTF-16 编码有大尾序和小尾序之别，即 UTF-16BE 和 UTF-16LE，在编码前会放置一个 U+FEFF 或 U+FFFE（UTF-16BE 以 FEFF 代表，UTF-16LE 以 FFFE 代表），其中 U+FEFF 字符在 Unicode 中代表的意义是 ZERO WIDTH NO-BREAK SPACE，顾名思义，它是个没有宽度也没有断字的空白。

UTF-32 使用四个字节为每个字符编码，使得 UTF-32 占用空间通常会是其它编码的二到四倍。UTF-32 与 UTF-16 一样有大尾序和小尾序之别，编码前会放置 U+0000FEFF 或 U+0000FFFE 以区分。



#### 有了UTF8为什么还需要GBK？

其实UTF8确实已经是国际通用的字符编码了，但是这种字符标准毕竟是外国定的，而国内也有类似的标准指定组织，也需要制定一套国内通用的标准，于是GBK就诞生了。



#### GBK、GB2312、GB18030之间的区别

三者都是支持中文字符的编码方式，最常用的是GBK。

以下内容来自CSDN，介绍的比较详细。

GB2312（1980年）：16位字符集，收录有6763个简体汉字，682个符号，共7445个字符； 优点：适用于简体中文环境，属于中国国家标准，通行于大陆，新加坡等地也使用此编码； 缺点：不兼容繁体中文，其汉字集合过少。

GBK（1995年）：16位字符集，收录有21003个汉字，883个符号，共21886个字符； 优点：适用于简繁中文共存的环境，为简体Windows所使用（代码页cp936），向下完全兼容gb2312，向上支持 ISO-10646 国际标准 ；所有字符都可以一对一映射到unicode2.0上； 缺点：不属于官方标准，和big5之间需要转换；很多搜索引擎都不能很好地支持GBK汉字。

GB18030（2000年）：32位字符集；收录了27484个汉字，同时收录了藏文、蒙文、维吾尔文等主要的少数民族文字。 优点：可以收录所有你能想到的文字和符号，属于中国最新的国家标准； 缺点：目前支持它的软件较少。



#### URL编解码

网络标准RFC 1738做了硬性规定 :只有字母和数字[0-9a-zA-Z]、一些特殊符号“$-_.+!*'(),”[不包括双引号]、以及某些保留字，才可以不经过编码直接用于URL;

除此以外的字符是无法在URL中展示的，所以，遇到这种字符，如中文，就需要进行编码。

所以，把带有特殊字符的URL转成可以显示的URL过程，称之为URL编码。

反之，就是解码。

URL编码可以使用不同的方式，如escape，URLEncode，encodeURIComponent。



#### Big Endian和Little Endian

字节序，也就是字节的顺序，指的是多字节的数据在内存中的存放顺序。

在几乎所有的机器上，多字节对象都被存储为连续的字节序列。例如：如果C/C++中的一个int型变量 a 的起始地址是&a = 0x100，那么 a 的四个字节将被存储在存储器的0x100, 0x101, 0x102, 0x103位置。

根据整数 a 在连续的 4 byte 内存中的存储顺序，字节序被分为大端序（Big Endian） 与 小端序（Little Endian）两类。

Big Endian 是指低地址端 存放 高位字节。 Little Endian 是指低地址端 存放 低位字节。

Java采用Big Endian来存储数据、C\C++采用Little Endian。在网络传输一般采用的网络字节序是BIG-ENDIAN。和Java是一致的。

所以在用C/C++写通信程序时，在发送数据前务必把整型和短整型的数据进行从主机字节序到网络字节序的转换，而接收数据后对于整型和短整型数据则必须实现从网络字节序到主机字节序的转换。如果通信的一方是JAVA程序、一方是C/C++程序时，则需要在C/C++一侧使用以上几个方法进行字节序的转换，而JAVA一侧，则不需要做任何处理，因为JAVA字节序与网络字节序都是BIG-ENDIAN，只要C/C++一侧能正确进行转换即可（发送前从主机序到网络序，接收时反变换）。如果通信的双方都是JAVA，则根本不用考虑字节序的问题了。



### 语法糖

> 语法糖（Syntactic Sugar），也称糖衣语法，是指编程语言提供一种简易语法，这种语法不会改变程序逻辑，因此借助语法糖可编写出简洁的代码。
>
> 以下了解java语法糖。

#### 解语法糖

Java虚拟机并不支持语法糖。这些语法糖在编译阶段就会被还原成简单的基础语法结构，这个过程就是解语法糖。

Java 中最常用的语法糖主要有泛型、变长参数、条件编译、自动拆装箱、内部类等。本文主要来分析下这些语法糖背后的原理。一步一步剥去糖衣，看看其本质。



#### Switch支持String与枚举

> switch可支持的数据类型有：char, byte, short, int, Character, Byte, Short, Integer, String, or an enum

char只占用1个字节8位，可以和int平替，switch的时候也是先将char转为int再进行switch的：

```java
char c1 = 'a';
switch (c1) {
    case 'a':
        break;
}
```

反编译得到：

```java
char c1 = 'a';
switch(c1){
case 97: // 'a'
    break;
}
```



##### switch支持String

> switch可以认为只支持int类型。
>
> switch对于String的支持，是首先获取String类型的hashCode，对hashCode进行switch，再通过equals进行安全校验

```java
String str = "abc";
switch (str) {
    case "123":
        break;
    case "abc":
        break;
    default:
        break;
}
```

反编译:

```java
String str = "abc";
String s = str;
byte byte0 = -1;
switch(s.hashCode())
{
case 48690: 
    if(s.equals("123"))
        byte0 = 0;
    break;
case 96354: 
    if(s.equals("abc"))
        byte0 = 1;
    break;
}
switch(byte0)
{
case 0: // '\0'
case 1: // '\001'
default:
    return;
}
```



##### switch支持enum

> switch可以认为只支持int类型。
>
> switch对于enum的支持，首先enum只是继承自Enum类的特殊Java类，编译器会为每一个枚举项设置对应编号，在switch的时候会使用此编号进行switch

```java
enum Season{
    SPRING,SUMMER;
}
```

```java
public void switchSupportEnum(Season season) {
    switch (season) {
        case SPRING:
            break;
        case SUMMER:
            break;
        default:
            break;
    }
}
```

分别反编译枚举和switch方法：

枚举：

以下可获取信息：

- 枚举类是继承自Enum的特殊java类，编译器会将其还原至jvm支持的编码规范
- 枚举类中，每一个枚举项都是一个public static final的静态常量，且还有一个静态常量数组。这些属性都会在静态代码块钟完成初始化
- 构造方法有两个字段：name和ordinal，分别表示枚举项名称和枚举项编号

```java
final class Season extends Enum
{
    public static Season[] values()
    {
        return (Season[])$VALUES.clone();
    }
    public static Season valueOf(String name)
    {
        return (Season)Enum.valueOf(com/roily/booknode/javatogod/_09sugar/Season, name);
    }
    private Season(String s, int i)
    {
        super(s, i);
    }

    public static final Season SPRING;
    public static final Season SUMMER;
    private static final Season $VALUES[];
    static 
    {
        SPRING = new Season("SPRING", 0);
        SUMMER = new Season("SUMMER", 1);
        $VALUES = (new Season[] {
            SPRING, SUMMER
        });
    }
}
```

switch方法：

编译器会自动生成一个静态内部类，静态内部类中会初始化一个int数组，此int数组会和枚举的ordinal编号一一对应，最后完成switch

```java
public void switchSupportEnum(Season season){
    static class _cls1{
        static final int $SwitchMap$com$roily$booknode$javatogod$_09sugar$Season[];
        static {
            $SwitchMap$com$roily$booknode$javatogod$_09sugar$Season = new int[Season.values().length];
            $SwitchMap$com$roily$booknode$javatogod$_09sugar$Season[Season.SPRING.ordinal()] = 1;
            $SwitchMap$com$roily$booknode$javatogod$_09sugar$Season[Season.SUMMER.ordinal()] = 2;
        }
    }
    switch(_cls1..SwitchMap.com.roily.booknode.javatogod._09sugar.Season[season.ordinal()]){
    case 1: // '\001'
    case 2: // '\002'
    default:
        return;
    }
}
```



#### 范型

> java范型使得一份字节码可表示多种类型，有可重用的作用。比如List<String>和List<Integer>都映射到唯一字节码List。不存在List<String>和List<Integer>类型，在加载进jvm时，回进行类型擦除，变为原始类型，在需要的时候进行类型强转。

> 通常情况下，一个编译器处理泛型有两种方式：`Code specialization`和`Code sharing`。C++和C#是使用`Code specialization`的处理机制，而Java使用的是`Code sharing`的机制。

- Code specialization 

  为每一个范型创建单独字节码

-   Code sharing

  所有范型共享一份字节码

> Code sharing方式为每个泛型类型创建唯一的字节码表示，并且将该泛型类型的实例都映射到这个唯一的字节码表示上。将多种泛型类形实例映射到唯一的字节码表示是通过类型擦除（`type erasue`）实现的。

> 类型擦除的主要过程如下： 1.将所有的泛型参数用其最左边界（最顶级的父类型）类型替换。 2.移除所有的类型参数。

```java
public void method1(List<String> list){
    final String s = list.get(0);
    list.add("123");
}
/**
 * extends适合get
 * @param list
 */
public void method2(List<? extends String> list){
    final String s = list.get(0);
    list.add(null);
}
/**
 * extends适合add
 * @param list
 */
public void method3(List<? super String> list){
    //得到obj没有意义
    final Object o =  list.get(0);
    //强转可能会造成，Cast异常
    final Comparable s = (Comparable) list.get(0);
    list.add("null");

}
```

jad反编译：

```java
public void method1(List list)
{
    String s = (String)list.get(0);
    list.add("123");
}

public void method2(List list)
{
    String s = (String)list.get(0);
    list.add(null);
}

public void method3(List list)
{
    Object o = list.get(0);
    Comparable s = (Comparable)list.get(0);
    list.add("null");
}
```



#### 自动装箱拆箱

> java允许基本数据类型的包装类型直接指向基本数据类型，基本数类型直接指向基本数据类型包装类型。
>
> 此过程称为装箱、拆箱

自动装箱的过程会调用 valueOf方法

自动拆箱过程会调用xxxValue()方法

```java
//自动装箱
Integer i = 10;
//自动拆箱
int i2 = i;
//自动拆箱
Integer i3 = 10;
for (Integer integer = 0; integer < i3; integer++) {
}
```

反编译：

```java
Integer i = Integer.valueOf(10);
int i2 = i.intValue();
Integer i3 = Integer.valueOf(10);
for(Integer integer = Integer.valueOf(0); integer.intValue() < i3.intValue();)
{
    Integer integer1 = integer;
    Integer integer2 = integer = Integer.valueOf(integer.intValue() + 1);
    Integer _tmp = integer1;
}
```

#### 可变长参数

> 方法的参数可设置为可变长参数，允许添加个数不同的参数。但可变长参数必须设置才方法参数的最后一个，使用三个点定义。
>
> 当调用此方法时，会将可变长参数转化为数组，再调用此方法

```java
void method2(String str, Integer... integers) {

}
```

```java
transient void method2(String s, Integer ainteger[]){
}
```



#### 内部类

> 普通内部类和静态内部类，都属于语法糖。允许再一个类内部定义其他类，一但编译成功就会生成多份字节码。以下例子就会生成
>
> TestInnerClass.class   
>
> TestInnerClass$StaticInnerClass.class
>
> TestInnerClass$InnerClass.class
>
> 三份字节码

```java
public class TestInnerClass {
    class InnerClass{
    }
    static class StaticInnerClass{
    }
}
```



#### 条件编译

> 根据条件编译代码。属于编译器编译期间对代码的优化。

```java
//包装类型需要转换所以无论如何都会编译
final Boolean flag1 = false;
if (flag1) {
    System.out.println("flag1");
}else {
    System.out.println("flag1   xxxx");
}

//基本数据类型，无需转化，编译期间就知道对应值，会舍弃其中一个分支
final boolean flag2 = true;
if (flag2) {
    System.out.println("flag2");
}else {
    System.out.println("flag2   XXX");
}

//flag3为false  编译器会舍弃这个分之
final boolean flag3 = false;
if (flag3) {
    System.out.println("flag3");
}else {
    System.out.println("flag3  xxx");
}
```

反编译：

可以发现对于编译期间就可得知的值，编译器会做出条件编译优化：

```java
Boolean flag1 = Boolean.valueOf(false);
if(flag1.booleanValue())
    System.out.println("flag1");
else
    System.out.println("flag1   xxxx");
boolean flag2 = true;
System.out.println("flag2");
boolean flag3 = false;
System.out.println("flag3  xxx");
```

#### Assert断言

> 一般用于方法入参检查

```java
public void testAssert(int num, StringBuilder sb) {
    assert num > 0 && null != sb;
}
```

反编译：

需要开启断言，如果不满足条件将抛出异常。

```java
public void testAssert(int num, StringBuilder sb)
{
    if(!$assertionsDisabled && (num <= 0 || null == sb))
        throw new AssertionError();
    else
        return;
}
```

#### 数值字面量

> 无论是整数还是浮点数允许插入下划线方便阅读，编译时会去掉下划线

```java
int num1 = 100_0_0_000;
//一般以千为单位
int num2 = 10_000_000;
float f = 10_000.0_100_100_001f;
```

反编译

```java
int num1 = 0x989680;
int num2 = 0x989680;
float f = 10000.01F;
```



#### 增强for循环

> 增强for循环提供简单的遍历方式，底层使用迭代器，所以说禁止在增强for循环中对使用集合api元素进行增删操作。
>
> 只能使用迭代器提供的添加方法

```java
final ArrayList<String> strings = new ArrayList<>(Arrays.asList("1", "2", "3"));
for (String string : strings) {
    System.out.println(string);
}

System.out.println("---------------");
//即便添加元素，当前迭代也是迭代不出来的。光标会后移
final ListIterator<String> iterator = strings.listIterator();
while (iterator.hasNext()){
    final String next = iterator.next();
    System.out.println(next);
    if (next.equals("2")){
        iterator.add("a");
    }
}
```

反编译增强for循环：

```java
ArrayList strings = new ArrayList(Arrays.asList(new String[] {
    "1", "2", "3"
}));
String string;
for(Iterator iterator2 = strings.iterator(); iterator2.hasNext(); System.out.println(string))
    string = (String)iterator2.next();
```



#### try with resources

> 在进行资源连接以及关闭操作时，需要手动在finally代码块中独立进行，即便使用apache工具类提供的关闭资源方法也非常麻烦。
>
> 因此java提供try  with  resources方式，可以自动为我们关闭资源，前提是资源的创建需要在try()括号内。

普通资源关闭操作：

繁琐，需要多重try  cache

```java
String filePath = "E://1.txt";
FileReader fr = null;
try {
    fr = new FileReader(filePath);
}catch (IOException e){
}finally {
    try {
        IOUtils.close(fr);
    } catch (IOException e) {
    }
}
```

使用try with resources:

代码就简洁了很多

```java
String filePath = "E://1.txt";
try(FileReader fr = new FileReader(filePath)) {
}catch (IOException e){
}
```

反编译查看：我们偷的懒，编译器都帮我们做了

```java
public void method1()
{
    String filePath = "E://1.txt";
    try
    {
        FileReader fr = new FileReader(filePath);
        Throwable throwable = null;
        if(fr != null)
            if(throwable != null)
                try
                {
                    fr.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            else
                fr.close();
    }
    catch(IOException ioexception) { }
}
```



#### Lambda表达式

> Lambda表达式，是java8特性，合理使用一下可以写出简洁高效的代码。
>
> 外部类 --> 内部类 --> 静态内部类  --> 匿名内部类 --> lambda表达式，一步步的简化接口实现的方式。
>
> 但是Lambda并不是匿名内部类的语法糖，lambda表达式的实现，是借助于Jvm提供的几个Api实现的。

```java
Comparator<String> c = (v1,v2)->{
  return   v1.compareTo(v2);
};
```

反编译：使用CRF，jad不可以反编译lambda表达式，太久不更新了：

CRF的使用：

- 下载CRF.jar包
-  使用  java -jar CRF.jar  XXXX.class --option  

CRF存在很多可选参数，可使用 ` java -jar .\cfr-0.152.jar --help`查看

使用`java -jar .\cfr-0.152.jar .\TestLambda.class --decodelambdas false`命令反编译：

可以发现会调用`LambdaMetafactory.metafactory`此方法，并且在第3、5个参数声明入参类型和出参数类型，在第四个参数指定方法实现。

```java
public class TestLambda {
    public void method1() {
        Comparator c = (Comparator)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;Ljava/lang/Object;)I, lambda$method1$0(java.lang.String java.lang.String
        ), (Ljava/lang/String;Ljava/lang/String;)I)();
    }
    private static /* synthetic */ int lambda$method1$0(String v1, String v2) {
        return v1.compareTo(v2);
    }
}
```

![image-20220906004444580](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209060044591.png)

当使用int、double定义BigDecimal时equals返回true，当使用String定义BigDecimal时可能返回false。

### Lambda表达式

[lambda表达式](https://www.runoob.com/java/java8-lambda-expressions.html)

> Lambda 表达式，也可称为闭包，它是推动 Java 8 发布的最重要新特性。
>
> Lambda 允许把函数作为一个方法的参数（函数作为参数传递进方法中）。
>
> 使用 Lambda 表达式可以使代码变的更加简洁紧凑。

Lambda表达式结合Stream api对集合进行处理，简洁而又高效。

Lambda表达式结合一些函数式接口，使用起来非常方便。

#### 语法

```java
(parameters) -> expression
或
(parameters) ->{ statements; }
```

#### 特性

- **可选类型声明：**不需要声明参数类型，编译器可以统一识别参数值。
- **可选的参数圆括号：**一个参数无需定义圆括号，但多个参数需要定义圆括号。
- **可选的大括号：**如果主体包含了一个语句，就不需要使用大括号。
- **可选的返回关键字：**如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定表达式返回了一个数值。

#### 实例

```java
// 1. 不需要参数,返回值为 5  
() -> 5  
  
// 2. 接收一个参数(数字类型),返回其2倍的值  
x -> 2 * x  
  
// 3. 接受2个参数(数字),并返回他们的差值  
(x, y) -> x – y  
  
// 4. 接收2个int型整数,返回他们的和  
(int x, int y) -> x + y  
    
// 5. 接受一个 string 对象,并在控制台打印,不返回任何值(看起来像是返回void)  
(String s) -> System.out.print(s)
```

例子：

```java
interface Demo1 {
    /**
     * 没有参数，返回值为null
     */
    void method();
}

interface Demo2 {
    /**
     * 没有参数，返回值为String类型
     */
    String method();
}

interface Demo3 {

    String method(int value);
}

interface Demo4 {

    String method(int value1, int value2);
}
```

```java
Demo1 demo1 = () -> {
};
Demo2 demo2 = () -> {
    return "123";
};
//只有一个参数，括号可以省略
Demo3 demo3 = value -> {
    return String.valueOf(value);
};
//有多个参数，括号不可以省略
Demo4 demo4 = (value1, value2) -> {
    return String.valueOf(value1 + value2);
};
```



####  由繁至简

> lambda表达式主要用于定义行内执行的方法类型接口。(lambda表达式用于实现，函数式接口、或接口中只有一个方法且实现逻辑较为简单)。
>
> lambda表达式免去了使用匿名内部类的方法实现，并且给予Java强大的函数式编程的能力。

- ClassOuter，定义在同一个java文件中。 编译时会生成独立的class字节码文件
- ClassInner，内部类，编译时会生成独立字节码信息，inner class，主类名称`$`内部类名称 如(`Parsing inner class .\TestLambda2$1.class`)
- 静态内部类
- 匿名内部类，没有名字，但是编译器会自动生成
- lambda表达式，它不是匿名内部类的语法糖。借助LambdaMetafactory实现

```java
public class TestLambda2 {
    /**
     * 内部类
     */
    class ClassInner implements Demo {
        @Override
        public void method() {
        }
    }
    /**
     * 静态内部类
     */
    static class StaticClassInner implements Demo {
        @Override
        public void method() {
        }
    }
    public static void main(String[] args) {
        /**
         * 匿名内部类
         */
        final Demo demo = new Demo() {
            @Override
            public void method() {
            }
        };

        /**
         * lambda表达式
         */
        Demo demo1 = () -> {
        };
    }
}
interface Demo {
    /**
     * 没有参数，返回值为null
     */
    void method();
}

/**
 * 普通外部类
 */
class ClassOuter implements Demo {
    @Override
    public void method() {
    }
}
```



#### 变量作用域

> `Variable used in lambda expression should be final or effectively final`。
>
> Lambda表达式中使用的变量，要么被final修饰，要么具有被final修饰的语义，也就是在lambda表达式中使用的变量都是不可变的。

lambda中使用的变量特点和final一致：

- 对于基本数据类型，只有值得概念，不可变
- 引用类型，可使用内部修改方法来修改内部属性值，但不可修改引用

![image-20220913225238338](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209132252815.png)

#### 方法声明

> java中方法声明使用双冒号`::`。

- 使用双冒号声明方法，要求方法不可对参数做修改，只是简单引用方法

如：

```java
//第一个String为调用toString方法对象，第二个为返回类型
final Function<String, String> stringFunction = String::toString;
final Function<String, Integer> stringFunction1 = String::length;
```

例子：

> 问题：` void method2(String str1, String str2)`可以进行方法引用么？

```java
class Person {
    public void method1() {
        System.out.println();
    }
    public void method2(String str1) {
        System.out.println("method2(String str1)");
    }
    public void method2(String str1, String str2) {
        System.out.println("method2(String str1, String str2)");
    }
}
//以下两组是等价的
Consumer<Person> method1x = (person) -> {
};
//简单方法引用，无参数
final Consumer<Person> method1 = Person::method1;

final BiConsumer<Person, String> method2x = (person, str) -> {
    person.method2(str);
};
//一个参数方法，范型：第一个调用方，第二个方法参数
final BiConsumer<Person, String> method2 = Person::method2;
```

> 自定义接口

```java
interface MyDemoI<T, K, V> {
    void method(T t, K k, V v);
}
MyDemoI<Person,String,String> myDemoI = Person::method2;
```



####  函数式接口

> 只有一个未实现的抽象方法的接口叫做函数式接口。

被@FunctionInterface修饰的接口，编译器要求该接口具有以下特点：

- 是Interface而不是class或enum
- 只有一个未实现抽象方法的接口
- 可以有default或final方法（这不是抽象方法）

编译器会自动识别满足函数式接口的接口，而不需要强制使用@FuncationInterface



##### 内置四大函数式接口

> java提供的四大内置函数式接口。

- public interface Consumer<T>   void accept(T t);

  > 消费接口，接收一个参数，无返回值。

- public interface Supplier<T>   T get();

  > 供给接口，无参数有返回值，生产一个

- public interface Predicate<T> boolean test(T t);

  > 判断接口，有参数，有返回值，对传入参数i进行断言

- public interface Function<T, R>  R apply(T t);

  > 函数式接口，有参，有返回值

### BigDecimal

> BigDecimal用于解决浮点数精度问题。

#### (0.1d + 0.2d) ！= 0.3d

> 浮点数存在精度问题，只能表示大概数值，不能精确表示

现象：

```java
double d1 = 0.1d;
double d2 = 0.2d;
System.out.println(d1 + d2);
System.out.println((d1 + d2) == 0.3d);
```

![image-20220906002546839](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209060025441.png)

#### 借助BigDecimal

> 借助bigDecimal解决浮点数精度问题

```java
double d1 = 0.1d;
double d2 = 0.2d;
double d3 = 0.3d;
final BigDecimal bigDecimal1 = BigDecimal.valueOf(d1);
final BigDecimal bigDecimal2 = BigDecimal.valueOf(d2);

final BigDecimal result = bigDecimal1.add(bigDecimal2);
System.out.println(result);
System.out.println(result.compareTo(BigDecimal.valueOf(d3)) == 0);
```

![image-20220906003717302](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209060037744.png)

#### BigDecimal使用CompareTo判等

> BigDecimal的判等，首先 `==`肯定不能用，equals方法会首先比较精度1.0和1.00的scale不等，直接返回false，应该使用CompareTo方式进行判等。

```java
final BigDecimal bigDecimal1 = new BigDecimal("1.0");
final BigDecimal bigDecimal2 = new BigDecimal("1.00");

System.out.println(bigDecimal1 == bigDecimal2);
System.out.println(bigDecimal1.equals(bigDecimal2));
System.out.println(bigDecimal1.compareTo(bigDecimal2) == 0);
```

因为使用String定义BigDecimal时，若精度不同，也就是多了几个0，字符数组的长度是不同的，那么BigDecimal的Scale即精度就会不同，而equals时回收先进行scale的对比。

equals的javadoc也给出解释：

当前BigDecimal和指定对象进行equals的时候，不同于compareTo，equals方法只有在两个BigDecimal的值和精度都相等的情况下两个对象才相等(因此2.0 不等于 2.00)。前提是使用String定义BigDecimal。

```doc
Compares this BigDecimal with the specified Object for equality. Unlike compareTo, this method considers two compareTo objects equal only if they are equal in value and scale (thus 2.0 is not equal to 2.00 when compared by this method)
```



#### BigDecimal原理

>BigDecimal类似于科学计数法，其内部维护了一个无标度数值和一个标度。

BigDecimal中标度使用Scale表示。

当scale为0或整数时，表示该数小数点右边位数；当scale为负数时，即表示该数为无标度数值后加n个0。

123.123 的无标度数为123123，标度为3。

0.1 无标度数为1，标度为1。

> 那么就可以表示二进制数不能表示的数



> BigDecimal有如下几种构造方法，这些构造方法的Scale的表示是不同的。

- int和long都是正整数，没有小数部分，他们的scale为0
- double的scale由其具体表示值决定
- string的scale由具体表示值决定

```java
BigDecimal(int)
BigDecimal(double) 
BigDecimal(long) 
BigDecimal(String)
```

可以debug看一下如下代码：

使用BigDecimal表示0.1d，并不精确，原因在浮点数的精度问题

```java
double d1 = 0.1d;
final BigDecimal bigDecimal = new BigDecimal(d1);
System.out.println(bigDecimal);
```

![image-20220906012031070](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209060123055.png)

但是如果此浮点数是可准确表示的，那么Bigdecimal也没有问题。但是BigDecimal就是为了解决精度问题的，在不存在精度问题才可准确表示，意义何在？

![image-20220906012306647](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209060123915.png)

> 使用Bigdecimal(String)来创建Bigdecimal,可准确表示

![image-20220906012608197](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209060126818.png)

##### 小结

> 创建Bigdecimal使用如下方式：

valueof(val)会调用Double.toString(val)保证进度准确

```java
Bigdecimal(String str);
Bigdecimal.valueof(double val);
```



### 并发编程



#### 并发&并行

##### 并发

> 单CPU环境下，多个作业感官上`同时运行`，实际上是切换运行，某一时刻，只能有一个作业在运行。

​	无论是Windows、Linux还是Mac OS，都是多用户多任务分时操作系统。操作系统将CPU资源(时间)划分为多个CPU时间片，由操作系统分配这些时间片，当一个作业时间片消耗完或失去时间片，那么这个作业就会暂停，此刻操作系统重写分配时间片，获得CPU时间片的作业将继续运行，CPU切换时间片很快且基本上是轮询，所以说感官上是多个任务同时进行`。所以说单核CPU环境下，任务不可能同时进行。	

​	上下文切换：CPU进行上下文切换时，会停止当前进程，当前线程状态会变为挂起、删除，并保存此进程的状态以便恢复。

##### 并行

> 并行才是真正意义上的同时进行。
>
> 并行只能出现在多核CPU环境下，多个进程可以同时运行，两个进程互不影响。

##### 区别

> 并发：单核CPU下，多个任务交替执行。
>
> 并行：多核CPU下，多个任务独立执行，互不影响。



#### 进程&线程

> 进程是CPU分配资源的最小单位，线程是执行的最小单位。
>
> 进程是线程的集合，一个进程可完整完成一件事情。

打开一个浏览器就开启一个进程。打开一个记事本就开启一个进程。打开2个记事本，就开启两个进程。

进程是线程的集合，记事本可以进行统计、打字等功能，每一个功能可能都是一个线程。



##### 线程

特点：

- 共享进程资源

  > 多个线程可共享同一个进程的内存空间

- 可并发执行

  > 单个进程下多线程可以并发执行，提升效率。
  >
  > 当然也会带来问题，由于多线程共享进程内存空间，存在线程安全问题，保证线程安全也是并发编程需要解决的事

Java线程状态：

线程存在状态，并且线程状态会切换

- 初始化(INIT)

  > 创建线程，还未执行。new出来，但没有start

- 运行(RUNABLE)

  - 就绪 (READY)  调用start方法，等待获取CPU时间片的过程
  - 运行中 (RUNNING) 获取了CPU时间片，正在执行

- 阻塞(BLOCKED)

  > 线程阻塞，失去CPU时间片

- 等待(WAITING)

  > 处于此状态的线程，需要等待被其他线程唤醒

- 超时等待(TIMED_WAITING)

  > 超过等待延时时间可自己唤醒

- 终止(TERMINATED)

  > 线程执行完毕



#### 线程优先级

> Java虚拟机采用抢占式调度模型，也就是会给优先级更高的线程优先分配CPU。
>
> Java线程的调度是自动进行的，可为线程设置优先级，但Java虚拟机是抢占式调度，优先级高的线程不一定会抢占到CPU资源。
>
> 所创建的线程优先级默认为父线程优先级，可通过setPriority设置线程优先级，getPriority获得线程优先级

Java语言一共设置了十个线程优先级别

![image-20220913230104244](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209132301686.png)

Thread的init方法：

```java
private void init(ThreadGroup g, Runnable target, String name,
                  long stackSize, AccessControlContext acc,
                  boolean inheritThreadLocals) {
.....

    Thread parent = currentThread();
.....
}
```

setPriority

```java
System.out.println(Thread.currentThread().getPriority());
Thread.currentThread().setPriority(7);
System.out.println(Thread.currentThread().getPriority());
```

![image-20220913230547558](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209132305126.png)

#### 线程调度

**给多个线程按照特定的机制分配CPU的使用权的过程就叫做线程调度。**

操作系统给线程分配CPU时间片

- 对于单CPU的计算机来说，一个时刻只可运行一个线程

- 多线程并发，宏观上看多线程一起运行，实际上某一时刻只有一个线程处于运行中状态。而线程想要从就绪状态变为运行中状态，就必须获得CPU使用权。

##### Linux线程调度

在Linux中，线程是由进程来实现，线程就是轻量级进程（ lightweight process ），因此在Linux中，线程的调度是按照进程的调度方式来进行调度的，也就是说线程是调度单元。

Linux这样实现的线程的好处的之一是：线程调度直接使用进程调度就可以了，没必要再搞一个进程内的线程调度器。在Linux中，调度器是基于线程的调度策略（scheduling policy）和静态调度优先级（static scheduling priority）来决定那个线程来运行。

在Linux中，主要有三种调度策略。分别是：

- SCHED_OTHER 分时调度策略，（默认的）
- SCHED_FIFO 实时调度策略，先到先服务
- SCHED_RR 实时调度策略，时间片轮转

##### Windows线程调度

Windows 采用基于优先级的、抢占调度算法来调度线程。

用于处理调度的 Windows 内核部分称为调度程序，Windows 调度程序确保具有最高优先级的线程总是在运行的。由于调度程序选择运行的线程会一直运行，直到被更高优先级的线程所抢占，或终止，或时间片已到，或调用阻塞系统调用（如 I/O）。如果在低优先级线程运行时，更高优先级的实时线程变成就绪，那么低优先级线程就被抢占。这种抢占使得实时线程在需要使用 CPU 时优先得到使用。

##### Java线程调度

可以看到，不同的操作系统，有不同的线程调度策略。但是，作为一个Java开发人员来说，我们日常开发过程中一般很少关注操作系统层面的东西。

主要是因为Java程序都是运行在Java虚拟机上面的，而虚拟机帮我们屏蔽了操作系统的差异，所以我们说Java是一个跨平台语言。

**在操作系统中，一个Java程序其实就是一个进程。所以，我们说Java是单进程、多线程的！**

前面关于线程的实现也介绍过，Thread类与大部分的Java API有显著的差别，它的所有关键方法都是声明为Native的，也就是说，他需要根据不同的操作系统有不同的实现。

在Java的多线程程序中，为保证所有线程的执行能按照一定的规则执行，JVM实现了一个线程调度器，它定义了线程调度模型，对于CPU运算的分配都进行了规定，按照这些特定的机制为多个线程分配CPU的使用权。

主要有两种调度模型：**协同式线程调度**和**抢占式调度模型**。

###### 协同式线程调度

协同式调度的多线程系统，线程的执行时间由线程本身来控制，线程把自己的工作执行完了之后，要主动通知系统切换到另外一个线程上。协同式多线程的最大好处是实现简单，而且由于线程要把自己的事情干完后才会进行线程切换，切换操作对线程自己是可知的，所以没有什么线程同步的问题。

###### 抢占式调度模型

抢占式调度的多线程系统，那么每个线程将由系统来分配执行时间，线程的切换不由线程本身来决定。在这种实现线程调度的方式下，线程的执行时间是系统可控的，也不会有一个线程导致整个进程阻塞的问题。

系统会让可运行池中优先级高的线程占用CPU，如果可运行池中的线程优先级相同，那么就随机选择一个线程，使其占用CPU。处于运行状态的线程会一直运行，直至它不得不放弃CPU。

**Java虚拟机采用抢占式调度模型。**

虽然Java线程调度是系统自动完成的，但是我们还是可以“建议”系统给某些线程多分配一点执行时间，另外的一些线程则可以少分配一点——这项操作可以通过设置线程优先级来完成。Java语言一共设置了10个级别的线程优先级（Thread.MIN_PRIORITY至Thread.MAX_PRIORITY），在两个线程同时处于Ready状态时，优先级越高的线程越容易被系统选择执行。

不过，线程优先级并不是太靠谱，原因是Java的线程是通过映射到系统的原生线程上来实现的，所以线程调度最终还是取决于操作系统，虽然现在很多操作系统都提供线程优先级的概念，但是并不见得能与Java线程的优先级一一对应。



#### 多线程Debug

> idea  多线程Debug

如果不设置，被多个线程访问的代码，断点只会停留一次。

右击断点设置为Thread即可

![image-20220913234255079](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209132342275.png)

可选择线程执行顺序，也可观测线程状态。ZOMBIE僵尸线程：资源已经释放但线程实例还在。

![image-20220913234425219](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209132345728.png)



#### 线程创建方式



##### 继承Thread类重写start方法

> 继承Thread重写run方法，直接new出来，调用start方法

```java
public class ExtendsThread extends Thread {
    @Override
    public void run() {
        super.run();
        System.out.println(Thread.currentThread());
    }
}
```



##### 实现runable接口

> Runable可避免多继承问题，java不支持多继承，如果一个类已经有了父类，那么就不就可以使用继承Thread的方式来创建线程。
>
> 实现Runable接口重写run方法，作为构造参数给Thread，调用start方法即可

```java
class ImplementsRunnable  implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread());
    }
}

new Thread(new ImplementsRunnable()).start();
```

> 也可先使用FutureTask包装，指定默认返回值即可

```java
final ImplementsRunnable implementsRunnable = new ImplementsRunnable();
final FutureTask<String> vFutureTask = new FutureTask<>(implementsRunnable,"");
new Thread(vFutureTask).start();
```



##### 实现Callable接口

> Callable接口可接收一个返回值，可指定泛型。
>
> 需要使用FutureTask包装再送给Thread作为构造参数

```java
class ImplementsCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        return Thread.currentThread().getName();
    }
}
```

```java
final FutureTask<String> futureTask = new FutureTask<>(new ImplementsCallable());
new Thread(futureTask).start();
System.out.println(futureTask.get());
```

> FutureTask位于concurrent包下。实现了Runbale接口，可对实现Runable和Calable接口的类进行包装，可获取线程执行结果，适合于异步获取结果的场景。
>
> 值得注意的是FutureTask的get方法是阻塞的，对于异步获取线程执行结果的操作可放于主线程后。

如下：主线程的执行会在futureTask之后

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    final ImplementsRunnable implementsRunnable = new ImplementsRunnable();
    final FutureTask<String> vFutureTask = new FutureTask<>(implementsRunnable,"xxx");
    new Thread(vFutureTask).start();
    //vFutureTask.get()是一个阻塞任务，可放置于最后，当主线程执行完后再来监控此返回结果
    System.out.println(vFutureTask.get());
    System.out.println("主线程。。。。。");
}
```

改造如下：

```java
final ImplementsRunnable implementsRunnable = new ImplementsRunnable();
final FutureTask<String> vFutureTask = new FutureTask<>(implementsRunnable,"xxx");
new Thread(vFutureTask).start();

System.out.println("主线程。。。。。"); 
while (!vFutureTask.isDone())
System.out.println(vFutureTask.get());
```



##### 通过线程池创建线程

> 推荐使用线程池创建线程，线程属于资源，池化管理可重复使用节省资源。

```java
System.out.println(Thread.currentThread().getName());
System.out.println("通过线程池创建线程");
ExecutorService executorService = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>(10), (r) -> {
    //线程名前缀
    String namePrefix = "线程：";
    int no = 0;
    Thread t = new Thread(null, r, namePrefix + no++, 0);
    //设置为费守护线程
    t.setDaemon(false);
    //设置线程优先级为5
    t.setPriority(Thread.NORM_PRIORITY);
    return t;
});
executorService.execute(() -> System.out.println(Thread.currentThread().getName()));
executorService.shutdown();
```

![image-20220914005031768](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/java%E7%AC%94%E8%AE%B0/%E8%AF%BB%E4%B9%A6%E7%AC%94%E8%AE%B0/java%E6%88%90%E7%A5%9E%E4%B9%8B%E8%B7%AF/202209140050528.png)
