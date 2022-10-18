# Java Basics

## java语言特点

- 面向对象（封装，继承，多态）；

- 跨平台（ Java 虚拟机实现平台无关性）；

- 支持多线程（ C++ 语言没有内置的多线程机制，因此必须调用操作系统的多线程功能来进行多线程程序设计，而 Java 语言却提供了多线程支持）；

- 可靠性、安全性（阿里之前用的php）；

- 支持网络编程并且很方便（ Java 语言诞生本身就是为简化网络编程设计的，因此 Java 语言不仅支持网络编程而且很方便）；

- 编译与解释并存

  ```bash
  ## 编译型语言、解释型语言
  编译型语言:将代码一次性编译成机器认识的代码。开发效率慢、执行效率高。c、c++、GO
  解释型语言:逐行解释（interpret），逐行运行。开发效率高、执行效率慢。Python、JavaScript、PHP。
  
  Java 语言既具有编译型语言的特征，也具有解释型语言的特征。
  Java 程序要经过先编译，后解释两个步骤，由 Java 编写的程序需要先经过编译步骤，生成字节码（.class 文件），这种字节码必须由 Java 解释器来解释执行。
  ```

- 运行时语言（反射机制）

## jdk  jre  jvm

jdk>jre>jvm

> jdk     java develepment kit   java开发者工具

```bash
包含功能齐全的java sdk 
比jre多了一些工具（javac javadoc jdb）
- javac  编译的
- javadoc 生成文档的
- jdb 调试工具（打断点的）
```

补充：java程序的运行只依赖于jre。

提问：如果说一个java程序是用jsp写的页面，需要安装jdk么？

需要。

jsp本质上是servlet。但是`.jsp`->`.java`->`.class`是需要`javac`的。

补充：jsp的现象。

将web程序部署到tomcat上的话，他会有一个工作目录（work）,第一次访问会生成.java  .class文件。这也是为啥第一次访问速度会相对慢，因为她需要经过编译和解释。

==什么是字节码?采用字节码的好处是什么?==

在 Java 中，JVM 可以理解的代码就叫做字节码（即扩展名为 `.class` 的文件），它不面向任何特定的处理器，只==面向虚拟机==。Java 语言通过字节码的方式，在==一定程度上解决了传统解释型语言执行效率低的问题==，同时又==保留了解释型语言可移植的特点==。所以， Java 程序运行时相对来说还是高效的（不过，和 C++，Rust，Go 等语言还是有一定差距的），而且，由于字节码并不针对一种特定的机器，因此，Java 程序无须重新编译便可在多种不同操作系统的计算机上运行。

我们需要格外注意的是 `.class->机器码` 这一步。在这一步 JVM 类加载器首先加载字节码文件，然后通过解释器逐行解释执行，这种方式的执行速度会相对比较慢。而且，有些方法和代码块是经常需要被调用的(也就是所谓的热点代码)，所以后面引进了 JIT（just-in-time compilation） 编译器，而==== JIT 属于运行时编译===。当 JIT 编译器完成第一次编译后，其会将字节码对应的机器码保存下来，下次可以直接使用。而我们知道，机器码的运行效率肯定是高于 Java 解释器的。这也解释了我们为什么经常会说 **Java 是编译与解释共存的语言** 。

HotSpot 采用了惰性评估(Lazy Evaluation)的做法，根据二八定律，消耗大部分系统资源的只有那一小部分的代码（热点代码），而这也就是 JIT 所需要编译的部分。JVM 会根据代码每次被执行的情况收集信息并相应地做出一些优化，因此执行的次数越多，它的速度就越快。

> jre  java runningtime evirment  java运行时环境

```bash
JRE 是 Java 运行时环境。
它是运行已编译 Java 程序所需的所有内容的集合，包括 Java 虚拟机（JVM），Java 类库，java 命令和其他的一些基础构件。
--rt.jar
但是，它不能用于创建新程序。
```

> jvm  java virtual machine   java虚拟机

```bash
我们平常所用的hotsopt。
Java 虚拟机（JVM）是运行 Java 字节码的虚拟机。JVM 有针对不同系统的特定实现（Windows，Linux，macOS），目的是使用相同的字节码，它们都会给出相同的结果。字节码和不同系统的 JVM 实现是 Java 语言“一次编译，随处可以运行”的关键所在。
```

## Java 和 C++的区别?

我知道很多人没学过 C++，但是面试官就是没事喜欢拿咱们 Java 和 C++ 比呀！没办法！！！就算没学过 C++，也要记下来！

- 都是面向对象的语言，都支持封装、继承和多态

- Java 不提供指针来直接访问内存，程序内存更加安全

- Java 的类是单继承的，C++ 支持多重继承；虽然 Java 的类不可以多继承，但是接口可以多继承。

- Java 有自动内存管理垃圾回收机制(GC)，不需要程序员手动释放无用内存。

- C ++同时支持方法重载和操作符重载，但是 Java 只支持方法重载（操作符重载增加了复杂性，这与 Java 最初的设计思想不符）。

  

## 静态方法为什么不能调用非静态成员?

这个需要结合 JVM 的相关知识，主要原因如下：

1. 静态方法是属于类的，在类加载的时候就会分配内存，可以通过类名直接访问。而非静态成员属于实例对象，只有在对象实例化之后才存在，需要通过类的实例对象去访问。
2. 在类的非静态成员不存在的时候静态成员就已经存在了，此时调用在内存中还不存在的非静态成员，属于非法操作。

## 重载&重写

> 重载

```bash
方法名必须相同，参数列表必许不同（参数类型、参数个数、参数顺序）,返回类型可以不同
```

==方法签名的概念==

方法签名可以完整的描述一个方法。

方法名+参数列表。返回类型不是方法签名的一部分。==只有返回类型不同的两个方法不产生重载==。

> 重写

重写发生在运行期，是子类对父类的允许访问的方法的实现过程进行重新编写。

1. 返回值类型、方法名、参数列表必须相同，抛出的异常范围小于等于父类，访问修饰符范围大于等于父类。
2. 如果父类方法访问修饰符为 `private/final/static` 则子类就不能重写该方法，但是被 static 修饰的方法能够被再次声明。
3. 构造方法无法被重写

**方法的重写要遵循“两同两小一大”**

- “两同”即方法名相同、形参列表相同；
- “两小”指的是子类方法返回值类型应比父类方法返回值类型更小或相等，子类方法声明抛出的异常类应比父类方法声明抛出的异常类更小或相等；
- “一大”指的是子类方法的访问权限应比父类方法的访问权限更大或相等。

⭐️ 关于 **重写的返回值类型** 这里需要额外多说明一下，上面的表述不太清晰准确：如果方法的返回类型是 void 和基本数据类型，则返回值重写时不可修改。但是如果方法的返回值是引用类型，重写时是可以返回该引用类型的子类的。



## `==`和equals

`==`对于基本类型和引用类型的作用效果是不同的：

- 对于基本数据类型来说，`==` 比较的是值。
- 对于引用数据类型来说，`==` 比较的是对象的内存地址。

`equals()` ==不能用于判断基本数据类型的变量==，只能用来判断两个对象是否相等。

`equals()`方法存在于`Object`类中，而`Object`类是所有类的直接或间接父类。

![image-20220208152319836](interview.assets\image-20220208152319836.png)

`Object`的`equals`方法，本质还是==

```java
public boolean equals(Object obj) {
    return (this == obj);
}
```

`String`的`equals`方法

```java
public boolean equals(Object anObject) {
    //如果是同一个引用的话直接返回true
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



> string  equals  和常量池
>
> 创建一个String对象时，java虚拟机会判断常量池是否有该字符串对象，如果有直接将引用赋给变量，如果没有则创建字符串对象，再将引用赋给变量。

```java
@Test
public void test04() {
    //创建字符串对象  将引用赋给s1
    String s1 = "hello world";
    String s2 = "hello world";
    System.out.println(s1 == s2);
    （true）
	//创建字符串对象  调用String构造器，创建新的string对象  将引用赋给s3
    String s3 = new String("hello world");
    //创建字符串对象  调用String构造器，创建新的string对象  将引用赋给s4
    //s3  s4 引用不一样
    String s4 = new String("hello world");
    System.out.println(s3 == s4);
    （false）
}
```



## hashCode() 与 equals()

hashcode是什么？哈希碰撞？为什么重写equals一定要重写hashcode？



哈希算法生成散列码和对象内存地址，以key-value的形式存储在散列表中。

hashCode()是使用native修饰的本地方法（使用c或者c++实现的）

```java
public native int hashCode();
```

hashCode()作用：根据散列码，快速的检索出对象内存地址。（快、开销小）

***为什么要有 hashCode？***

我们以“`HashSet` 如何检查重复”为例子来说明为什么要有 `hashCode`？

> 当你把对象加入 `HashSet` 时，`HashSet` 会先计算对象的 `hashCode` 值来判断对象加入的位置，同时也会与其他已经加入的对象的 `hashCode` 值作比较，如果没有相符的 `hashCode`，`HashSet` 会假设对象没有重复出现。但是如果发现有相同 `hashCode` 值的对象，这时会调用 `equals()` 方法来检查 `hashCode` 相等的对象是否真的相同。如果两者相同，`HashSet` 就不会让其加入操作成功。如果不同的话，就会重新散列到其他位置。。这样我们就大大减少了 `equals` 的次数，相应就大大提高了执行速度。

***哈希碰撞***

不同对象产生相同hashcode



***为什么重写equals一定要重写hashcode？***

equals是判断两个引用类型（对象）是否相等的，也就是说两个对象equals返回true，那么他们的hashcode一定得相等。如果我们不重写hashcode方法，在equals方法返回true时，hashcode却不同，不应该。



两个对象hashCode相等：不一定相等（hash碰撞）

两个对象hashCode不相等：不相等

两个对象equals返回true：相等，hashCode返回值相等

## 基本数据类型

基本数据类型、大小、默认值、包装类型、装箱拆箱、包装类型的常量池技术



byte（1） short（2） int（4） long（8） float（4） double（8） 

boolean（1b） char（2）

这八种基本类型都有对应的包装类分别为：`Byte`、`Short`、`Integer`、`Long`、`Float`、`Double`、`Character`、`Boolean` 。

包装类型不赋值就是 `Null` ，而基本类型有默认值且不是 `Null`。

==基本数据类型直接存放在 Java 虚拟机栈中的局部变量表中，而包装类型属于对象（引用）类型，我们知道对象实例都存在于堆中。相比于对象类型， 基本数据类型占用的空间非常小。==

## 包装类型的常量池技术了解么？

`Byte`、`Short`、`Integer`、`Long`整数类型常量池技术，数值范围：-128-127

`Character`  0-127

`Boolean`  true  false

`Long`、`Float`没有实现常量池技术

```java
Integer i1 = new Integer(10);
Integer i2 = new Integer(10);
//false  包装类型是引用（对象）类型
System.out.println(i1 == i2);

Integer i3 = 10;
Integer i4 = 10;
//true   常量池技术  i3  i4  拿到的是同一个引用（地址）
System.out.println(i3 == i4);

Integer i5 = 1000;
Integer i6 = 1000;
//false  常量池中没有1000  因此创建新的对心
System.out.println(i5 == i6);
```

**所有整型包装类对象之间值的比较，全部使用 equals 方法比较**。

对于基本数据类型 ` == `和`equals`比较的是数值。但是对于引用类型比较的是引用地址。

Integer是int的包装类型，即他也是引用类型，使用`==`比较的还是内存地址。

好在`Integer`重写的`equals`方法（其比较的是数值）

总结：对于在包装类型常量池范围中的对象可以使用`==`比较（因为包装类型的常量池技术可实现对象复用），对于范围之外的一律使用`equals`进行比较。

## 自动装箱，拆箱

```java
Integer i = 10;
//Integer.valueOf(10);
int l = i;
```

> 查看字节码文件，自动装箱就是调用`valueOf`方法、自动拆箱就是调用`intValue`方法

```bash
    INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;
   
    INVOKEVIRTUAL java/lang/Integer.intValue ()I
```

![image-20220208164021233](interview.assets\image-20220208164021233.png)

![image-20220208164144267](interview.assets\image-20220208164144267.png)



## 成员变量与局部变量的区别有哪些

**语法形式** ：==成员变量是属于类==，而局部变量是在代码块或方法中定义的变量或是方法的参数；成员变量可以被 `public`,`private`,`static` 等修饰符所修饰，而局部变量不能被访问控制修饰符及 `static` 所修饰；但是，成员变量和局部变量都能被 `final` 所修饰。

**存储方式** ：如果成员变量是使用 `static` 修饰的，那么这个成员变量是属于类的，如果没有使用 `static` 修饰，这个成员变量是属于实例的。而对象存在于堆内存，局部变量则存在于栈内存。

**生存时间** ：从变量在内存中的生存时间上看，成员变量是对象的一部分，它随着对象的创建而存在，而局部变量随着方法的调用而自动消失。 

**默认值** ：从变量是否有默认值来看，成员变量如果没有被赋初，则会自动以类型的默认值而赋值（一种情况例外:被 `final` 修饰的成员变量也必须显式地赋值），而局部变量则不会自动赋值。

## 面向对象三大特征

####  封装

> 将对象属性私有化。通过调用方法获得属性值（接口思想）。

#### 继承

> 提取公共属性或方法，简化对象创建。逻辑性也强。

**关于继承如下 3 点请记住：**

1. 子类拥有父类对象所有的属性和方法（包括私有属性和私有方法），但是父类中的私有属性和方法子类是无法访问，**只是拥有**。
2. 子类可以拥有自己属性和方法，即子类可以对父类进行==扩展==。
3. 子类可以用自己的方式实现父类的方法。（以后介绍）。

==不建议大量使用继承，oop原则：尽量使用组合而不是继承。==

####  多态

多态，顾名思义，表示一个对象具有多种的状态，具体表现为==父类的引用指向子类的实例==。

**多态的特点:**

- 对象类型和引用类型之间具有继承（类）/实现（接口）的关系；
- 引用类型变量发出的方法调用的到底是哪个类中的方法，必须在程序运行期间才能确定；
- 多态不能调用“只在子类存在但在父类不存在”的方法；
- 如果子类重写了父类的方法，真正执行的是子类覆盖的方法，如果子类没有覆盖父类的方法，执行的是父类的方法



## 深拷贝和浅拷贝区别了解吗？什么是引用拷贝？

**浅拷贝**：浅拷贝会在堆上创建一个新的对象（区别于引用拷贝的一点），不过，如果原对象内部的属性是引用类型的话，浅拷贝会直接复制内部对象的引用地址，也就是说拷贝对象和原对象共用同一个内部对象。

**深拷贝** ：深拷贝会完全复制整个对象，包括这个对象所包含的内部对象



***浅拷贝***

> 在堆中创建一个新的对象，内部对象共享地址。

<img src="interview.assets\image-20220208174419719.png" alt="image-20220208174419719" style="zoom:67%;" />

**深拷贝**

> 深拷贝会完全复制整个对象，包括这个对象所包含的内部对象



***引用拷贝***

> 复制引用地址

<img src="interview.assets\image-20220208173859136.png" alt="image-20220208173859136" style="zoom:67%;" />

```java
Person person1 = new Person(new Address("武汉"));
Person person1Copy = person1;
System.out.println(person1 == person1Copy);
```



***总结***

> `clone()`方法是`object`类下的。即所有对象都可以调用。

# 常用类

## Object类

```bash
public final native Class<?> getClass()//native方法，用于返回当前运行时对象的Class对象，使用了final关键字修饰，故不允许子类重写。

public native int hashCode() //native方法，用于返回对象的哈希码，主要使用在哈希表中，比如JDK中的HashMap。
public boolean equals(Object obj)//用于比较2个对象的内存地址是否相等，String类对该方法进行了重写用户比较字符串的值是否相等。

protected native Object clone() throws CloneNotSupportedException//naitive方法，用于创建并返回当前对象的一份拷贝。一般情况下，对于任何对象 x，表达式 x.clone() != x 为true，x.clone().getClass() == x.getClass() 为true。Object本身没有实现Cloneable接口，所以不重写clone方法并且进行调用的话会发生CloneNotSupportedException异常。

public String toString()//返回类的名字@实例的哈希码的16进制的字符串。建议Object所有的子类都重写这个方法。

public final native void notify()//native方法，并且不能重写。唤醒一个在此对象监视器上等待的线程(监视器相当于就是锁的概念)。如果有多个线程在等待只会任意唤醒一个。

public final native void notifyAll()//native方法，并且不能重写。跟notify一样，唯一的区别就是会唤醒在此对象监视器上等待的所有线程，而不是一个线程。

public final native void wait(long timeout) throws InterruptedException//native方法，并且不能重写。暂停线程的执行。注意：sleep方法没有释放锁，而wait方法释放了锁 。timeout是等待时间。

public final void wait(long timeout, int nanos) throws InterruptedException//多了nanos参数，这个参数表示额外时间（以毫微秒为单位，范围是 0-999999）。 所以超时的时间还需要加上nanos毫秒。

public final void wait() throws InterruptedException//跟之前的2个wait方法一样，只不过该方法一直等待，没有超时时间这个概念

protected void finalize() throws Throwable { }//实例被垃圾回收器回收的时候触发的操作

```

## String类

***String、StringBuffer、StringBuilder 的区别？String 为什么是不可变的?***

> ==String对象本质是一个字符数组==，类被fianl修饰（不可被继），承字符数组被private 和 final修饰（限制作用域，引用不可变）。内部没有修改字符数组的方法（不可变）

***<u>String不可变性</u>***

```bash
如果说强行理解:字符数组被final修饰，那么它不可变。
```

真实原因：

```bash
如果是基本数据类型，被final修饰，则其值不可变
如果是引用类型（对象类型），被final修饰，其引用地址不可变。
那么，我们修改一个字符数组其实很简单，也就是说string的不可变性并不是final这个修饰符。
##真正原因有三：
1、String这个类被final修饰，则它没有子类，也就是没有子类会破坏它的不可变性。
2、字符数组被private修饰，私有属性，即字符数组没有将它暴露给其他类。
3、String类里面没有提供任何修改该字符数组的方法。
```

说明：

```java
String str = "123";
str+="abc";
```

第一步创建一个新的`String`对象   `123abc`

第二步`str`指向 `123abc`的引用

![image-20220208192815339](interview.assets\image-20220208192815339.png)

***<u>StringBuilder</u>***

> 他是`AbstractStringBuilder`的一个子类，其本质也是一个字符数组，只不过没有任何修饰符。`final`修饰（不可继承），提供了大量修改方法（append  可变）。

```java
char[] value;
```

***<u>StringBuffer</u>***

> 他是`AbstractStringBuilder`的一个子类，其本质也是一个字符数组，只不过没有任何修饰符。`final`修饰（不可继承），提供了大量修改方法（append  可变）。



***线程安全性：***

> 多线程安全性问题只对修改铭感，
>
> 而`String`是不可变的所以说他是线程安全的
>
> `StringBuilder`、`StringBuffer`都是`AbstractStringBuilder`的子类，提供了修改字符串的方法，但是`StringBuffer`添加了同步锁（方法或被调用的方法加了`synchronized`修饰符）所以说`StringBuffer`线程安全。`StringBuilder`非线程安全。



***<u>例子</u>***

```java
@Test
public void test01() {
    String str = "abc";
    System.out.println(str.toString() + ">>>" + str.hashCode());
    str += "ABC";
    System.out.println(str.toString() + ">>>" + str.hashCode());
}
//hash值不等，指向了不同的引用。

public static String StringSp(String str) {
    return str += "aaa";
}
public static StringBuilder StringApp(StringBuilder sb) {
    return sb.append("aaa");
}
@Test
public void tt() {
    String str = new String("bbb");
    String s = StringSp(str);
    System.out.println(str);
    //bbb
    StringBuilder sb = new StringBuilder("bbb");
    StringBuilder stringBuilder = StringApp(sb);
    System.out.println(sb);
    //bbbaaa
}
```



***<u>思考</u>***

> HashSet  HashMap是不允许键值重复的，如果我们使用可变类型作为他的键值类型的话会怎样？

```bash
很多的keyValue形式的数据类型，都会选择使用String类型作为键值类型，就是因为他的不可变性。
如果使用StringBuilder作为键值数据类型，我们只需要得到StringBuilder类型的引用就可以修改它的值。
{'a','ab'}  'a'.append("b")  =》{'ab','ab'}
这样就破坏了HashSet键值唯一性。
```



***<u>String的字符串常量池</u>***

```java
public void test01(){
    String str1 = "abc";
    String str2 = "abc";
    System.out.println(str1==str2);
}
public void test02(){
    String str1 = new String("abc");
    String str2 = new String("abc");
    System.out.println(str1==str2);
}
//结果：
true   false
```



***<u>String的equals</u>***

> 重写了`object`的`equals()`，只比较值



## 泛型

***<u>Java泛型了解么？什么是类型擦除？介绍一下常用的通配符？</u>***

**Java 泛型（generics）**提供了编译时类型安全检测机制，该机制允许程序员在编译时检测到非法的类型。泛型的本质是参数化类型，也就是说所操作的数据类型被指定为一个参数。

Java 的泛型是==伪泛型==，这是因为 Java 在运行期间，所有的泛型信息都会被擦掉，这也就是通常所说类型擦除 。

==泛型一般有三种使用方式: 泛型类、泛型接口、泛型方法。==



**1.泛型类**：

```java
//在实例化泛型类时，必须指定T的具体类型
public class Generic<T> {
    private T value;
    public Generic(T value) {
        this.value = value;
    }
    public T getValue() {
        return value;
    }
}
##
Generic<Integer> genericInteger = new Generic<Integer>(123456);
```

**2.泛型接口** ：

```java
public interface Generator<T> {
    public T method();
}
```

实现泛型接口，不指定类型：

```java
class GeneratorImpl<T> implements Generator<T>{
    @Override
    public T method() {
        return null;
    }
}
```

实现泛型接口，指定类型：

```java
class GeneratorImpl implements Generator<String>{
    @Override
    public String method() {
        return "hello";
    }
}
```

**3.泛型方法** ：

```java
public static <E> void printArray(E[] inputArray) {
    for (E element : inputArray) {
        System.out.printf("%s ", element);
    }
    System.out.println();
}

// 创建不同类型数组： Integer, Double 和 Character
Integer[] intArray = { 1, 2, 3 };
String[] stringArray = { "Hello", "World" };
printArray(intArray);
printArray(stringArray);
```

**常用的通配符为： T，E，K，V，？**

- ？ 表示不确定的 Java 类型
- T (type) 表示具体的一个 Java 类型
- K V (key value) 分别代表 Java 键值中的 Key Value
- E (element) 代表 Element

你的项目中哪里用到了泛型？

- 可用于定义通用返回结果 `CommonResult<T>` 通过参数 `T` 可根据具体的返回类型动态指定结果的数据类型
- 定义 `Excel` 处理类 `ExcelUtil<T>` 用于动态指定 `Excel` 导出的数据类型
- 用于构建集合工具类。参考 `Collections` 中的 `sort`, `binarySearch` 方法



## 反射

总结为一句话：得到class就得到了类的全部（属性，方法，甚至可以破坏私有性）

在运行时分析类以及执行类中方法的能力、机制。（这也是为什么说java是运行时语言）

通过反射你可以获取任意一个类的所有属性和方法（包括注解，接口列表），你还可以调用这些方法和属性。

***<u>获取class对象方式</u>***

> 类名+class

```java
Class class = className.class;
```

> 通过类加载器  classloader.loaderClass(classpath)

```java
Class class =  classloader.loaderClass("com.roily.test")
```

==以上不会触发类的初始化==

> 通过 Class.forName(classPath)

```java
Class.forName("com.roily.test")
```

> 通过instance.getClass()

```java
Test test = new Test();
Class class = test.getClass();
```

==这两种方式会触发类的初始化==



***<u>类的加载</u>***

> 类通过编译生成的`class`字节码文件放入内存的过程。被`final`修饰的成员变量和`class`对象会存入堆中的运行时方法区。供其他类和方法调用。

***<u>类的链接</u>***

> 当前`java`程序调用其他`java`程序的方法时，查看字节码文件，会发现去找类的全限定名。
>
> 会给成员变量、实例变量分配内存空间和设置默认值（基本数据类型【对应默认值】、引用数据类型【null】）

***<u>类的初始化</u>***

> `<cinit>`方法，会将被`static`修饰的成员变量和静态代码块合并执行。
>
> 对应`<init>`方法，实例变量和非静态代码块合并执行。



***<u>类加载器（classLoader）</u>***

可以指定具体的类加载器，加载类

```bash
作用：加载类
```

```bash
分类：
	系统类加载器（app加载器）【getSystemClassLoader()】{AppClassLoader} 平时自己写的`class`基本都是通过它加载
	扩展加载器 {ExtClassLoader}  jre运行时的具体依赖  rt.jar包下的io time....
	根加载器{BootstrapClassLoader}  值为null
```

```bash
双亲委派机制：
	如果指定类加载器为`app`加载器，会先去找`Ext`加载器，再去找根加载器。
	这也是为啥我们定义的类名不能一样
```



***<u>类的主动引用和被动引用</u>***

> 类的主动引用会触发类的初始化，被动引用不会触发类的初始化

```bash
主动引用：
使用new关键字创建对象。
调用（或修改）静态成员变量
使用反射（class.foName）方式获取class对象
使用xxxClass.newInstance()创建实例
```

```bash
除此之外都是被动引用：
调用静态常量（final修饰的常量，属于类，存于方法区常量池中）
调用父类静态常量或成员变量（静态方法）
创建对象数组（数组的创建只分配内存空间，不进行初始化【也就是null值】）
```

> 注意

```bash
启动一个程序jvm会优先找到当前`main`方法所在类，对他进行初始化
如果创建实例有父类，会优先实例化父类，再实例化子类
```

## 动态代理

> 静态代理

```bash
接口、真实角色（实现接口）、代理角色（实现接口）
代理角色对真实角色进行方法增强
优点：简单
缺点：不够灵活，代码臃肿
```



> 动态代理

两个类：`InvocationHandler`·`Proxy`

`InvocationHandler`代理类的调用处理程序所需实现的接口

​		invoke方法

`Proxy`调用其静态方法`newProxyInstance`生成代理类。参数：`classLoader``interfaces[]`(接口列表)`InvocationHandler`(this实现接口的类【多态】)



利用反射对真实对象增强



**CGLIB 动态代理机制中 `MethodInterceptor` 接口和 `Enhancer` 类是核心。**

## 注解

***<u>内置注解</u>*** 

@Override 定义在 java.lang.Override 中 , 此注释只适用于修辞方法 , 表示一个方法声明打算重写超类中 的另一个方法声明. 

@Deprecated 定义在java.lang.Deprecated中 , 此注释可以用于修辞方法 , 属性 , 类 , 表示不鼓励程序员使用这样的元素 , 通常是因为它很危险或者存在更好的选择 . 

@SuppressWarnings 定义在java.lang.SuppressWarnings中,用来抑制编译时的警告信息. 与前两个注释有所不同,你需要添加一个参数才能正确使用,这些参数都是已经定义好了的,我们 选择性的使用就好了 .

@SuppressWarnings("all") 

@SuppressWarnings("unchecked") 

@SuppressWarnings(value={"unchecked","deprecation"})

 等等 .....

***<u>元注解</u>***

```java
//注解可以在哪里生效
@Target(value = {ElementType.METHOD,ElementType.TYPE})
//注解在什么场景下有效  runtime>class>sources
@Retention(value = RetentionPolicy.RUNTIME)
//注解可以在生成在doc文档中
@Documented
//次注解可以被继承
@Inherited
```

***<u>自定义注解</u>***



```java
package com.roily.Annocation01;

import java.lang.annotation.*;

@MyAnnocation(name = "yyc",age = 1)
public class Meta_Annocation {
}

//自定义注解
/**
 * @author RoilyFish
 */
//注解可以在哪里生效
@Target(value = {ElementType.METHOD,ElementType.TYPE})
//注解在什么场景下有效  runtime>class>sources
@Retention(value = RetentionPolicy.RUNTIME)
//注解可以在生成在doc文档中
@Documented
//次注解可以被继承
@Inherited
@interface MyAnnocation{
    //注解参数  参数类型 参数名（） default
    String name() default "yyc";
    int age() default -1;// -1 表示不存在
}
```

## 异常

***<u>Exception 和 Error 有什么区别？</u>***

在 Java 中，`Throwable` 为所有的异常的超类。`Throwable` 类有两个重要的子类:

- **`Exception`** :程序本身可以处理的异常，可以通过 `catch` 来进行捕获。`Exception` 又可以分为 Checked Exception (受检查异常，必须处理) 和 Unchecked Exception (不受检查异常，可以不处理)。
- **`Error`** ：`Error` 属于程序无法处理的错误 ，我们没办法通过 `catch` 来进行捕获 。例如Java 虚拟机运行错误（`Virtual MachineError`）、虚拟机内存不够错误(`OutOfMemoryError`)、类定义错误（`NoClassDefFoundError`）等 。这些异常发生时，Java 虚拟机（JVM）一般会选择线程终止。

<img src="interview.assets\image-20220208225126345.png" alt="image-20220208225126345" style="zoom:67%;" />

<img src="interview.assets\image-20220208225144174.png" alt="image-20220208225144174" style="zoom:67%;" />

***<u>Checked Exception 和 Unchecked Exception 有什么区别？</u>***

**Checked Exception** 即受检查异常，Java 代码在编译过程中，如果受检查异常没有被 `catch`/`throw` 处理的话，就没办法通过编译 。

除了`RuntimeException`及其子类以外，其他的`Exception`类及其子类都属于受检查异常 。常见的受检查异常有： IO 相关的异常、`ClassNotFoundException` 、`SQLException`...。

**Unchecked Exception** 即 **不受检查异常** ，Java 代码在编译过程中 ，我们即使不处理不受检查异常也可以正常通过编译。

`RuntimeException` 及其子类都统称为非受检查异常，例如：`NullPointerException`、`NumberFormatException`（字符串转换为数字）、`ArrayIndexOutOfBoundsException`（数组越界）、`ClassCastException`（类型转换错误）、`ArithmeticException`（算术错误）等。 



## 序列化、反序列化

如果我们需要持久化 Java 对象比如将 Java 对象保存在文件中，或者在网络传输 Java 对象，这些场景都需要用到序列化。

简单来说：

- **序列化**： 将数据结构或对象转换成二进制字节流的过程（且存储其关系）
- **反序列化**：将在序列化过程中所生成的二进制字节流转换成数据结构或者对象的过程

![image-20220208230607586](interview.assets\image-20220208230607586.png)

## 不做序列化操作字段

对于不想进行序列化的变量，使用 `transient` 关键字修饰

- `transient` 只能修饰变量，不能修饰类和方法。
- `transient` 修饰的变量，在反序列化后变量值将会被置成类型的默认值。例如，如果是修饰 `int` 类型，那么反序列后结果就是 `0`。
- `static` 变量因为不属于任何对象(Object)，所以无论有没有 `transient` 关键字修饰，均不会被序列化。

static修饰的变量属于类不属于任何实例



## IO流





## Java值传递

形参&实参、值传递&引用传递

**实参（实际参数）** ：用于传递给函数/方法的参数，必须有确定的值。

**形参（形式参数）** ：用于定义函数/方法，接收实参，不需要有确定的值。

```java
String hello = "Hello!";
// hello 为实参
sayHello(hello);
// str 为形参
void sayHello(String str) {
			System.out.println(str);
}
```

***<u>参数传递方式</u>***

值传递：创建副本，实际传递副本，不会影响原参数。

引用传递：不创建副本，传递原参数的地址，修改操作会影响原参数。



***<u>传递基本数据类型</u>***

```java
public void modify(int a, int b) {
    int temp = a;
    a = b;
    b = temp;
    System.out.println("a+>" + a);
    System.out.println("b+>" + b);
}
@Test
public void test() {
    int a = 1;
    int b = 2;
    modify(a, b);
    System.out.println("a+>" + a);
    System.out.println("b+>" + b);
}
```

> 值传递，并不会影响原参数



***<u>传递引用数据类型</u>***

> 创建数组的方式
>
> 其实还是值传递，只不过传递的拷贝是引用地址，这样就可以修改内容。

```java
public static void change(int[] array) {
    // 将数组的第一个元素变为0
    array[0] = 0;
}
@Test
public void test01(){
    int a[] = {1,2,3};
    int b[] = new int[3];
    b[0] = 1;
    b[1] = 2;
    b[2] = 3;
    int[] c = new int[]{1,2,3};
    System.out.println(a[0]);
    System.out.println(b[0]);
    System.out.println(c[0]);
    change(a);
    System.out.println(a[0]);
}
```



```java
public static void main(String[] args) {

    User user1 = new User("于延闯1");
    User user2 = new User("于延闯2");

    swap(user1,user2);
    System.out.println("user1=>"+user1);
    System.out.println("user2=>"+user2);
}
public static void swap(User u1, User u2) {
    User temp = u1;
    u1 = u2;
    u2 = temp;
    System.out.println("u1=>"+u1);
    System.out.println("u2=>"+u2);
}
```

![image-20220209010732669](interview.assets\image-20220209010732669.png)

> 未能修改原参数

分析

![image-20220209011243170](interview.assets\image-20220209011243170.png)

> 首先java还是值传递，复制一分实例地址，分别指向同一个内存地址

> user1  和 user2 拷贝值交换值，交换的是，引用地址值，并未修改实例



## 浮点数精度问题

***<u>浮点数存储方式</u>***

`float`4位，32字节。

|        | 符号位 | 指数位 | 尾数位   |
| ------ | ------ | ------ | -------- |
| float  | 1      | 8      | 23（24） |
| double | 1      | 11     | 52（53） |

十进制小数=>二进制小数

使用科学计数法表示：
$$
1.M....*2^E
$$
第一个不为0的后面加小数点，二进制非0即1，所以尾数可以多一位。

指数部分用127加，保证指数位不为0

0.25(10)=> 二进制  0.01(2)   => 1.0 *2^-2

整数除二取余（截止条件 / = 0），小数乘二取整(小数部分为0)

0   01111101  0000 0000 0000 0000 0000 000

3.5   11.1   1.11*2^1

0 10000000 1100 0000 0000 0000 0000 000

0.2 (10)  =>2         1.1001001  

0 01111100 1001 1001 1001 1001 1001 100

```java
@Test
public void test01(){
    float f1 = 0.25f -0.2f;
    System.out.println(f1);
}
0.049999997
```

***<u>解决浮点数精度问题</u>***

bigDecemal

> `java.util`下的Objects类的equals方法

```java
BigDecimal a = new BigDecimal("1.0");
BigDecimal b = new BigDecimal("0.9");
BigDecimal c = new BigDecimal("0.8");

BigDecimal x = a.subtract(b);
BigDecimal y = b.subtract(c);

System.out.println(x); /* 0.1 */
System.out.println(y); /* 0.1 */
System.out.println(Objects.equals(x, y)); /* true */
System.out.println(x.equals(y)); /* true */
System.out.println(x ==  y); /* false */
```

> 加减乘除

```java
BigDecimal a = new BigDecimal("1.0");
BigDecimal b = new BigDecimal("0.9");
System.out.println(a.add(b));// 1.9
System.out.println(a.subtract(b));// 0.1
System.out.println(a.multiply(b));// 0.90
System.out.println(a.divide(b));// 无法除尽，抛出 ArithmeticException 异常
System.out.println(a.divide(b, 2, RoundingMode.HALF_UP));// 1.11
```



> 除得时候避免除不尽   使用 三个参数的重载  定义保留位数  保留规则

```java
public BigDecimal divide(BigDecimal divisor, int scale, RoundingMode roundingMode) {
    return divide(divisor, scale, roundingMode.oldMode);
}
```

> BIGDECIMAL      uitl

```java
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 简化BigDecimal计算的小工具类
 */
public class BigDecimalUtil {

    /**
     * 默认除法运算精度
     */
    private static final int DEF_DIV_SCALE = 10;

    private BigDecimalUtil() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double subtract(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double multiply(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2) {
        return divide(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = BigDecimal.valueOf(v);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 提供精确的类型转换(Float)
     *
     * @param v 需要被转换的数字
     * @return 返回转换结果
     */
    public static float convertToFloat(double v) {
        BigDecimal b = new BigDecimal(v);
        return b.floatValue();
    }

    /**
     * 提供精确的类型转换(Int)不进行四舍五入
     *
     * @param v 需要被转换的数字
     * @return 返回转换结果
     */
    public static int convertsToInt(double v) {
        BigDecimal b = new BigDecimal(v);
        return b.intValue();
    }

    /**
     * 提供精确的类型转换(Long)
     *
     * @param v 需要被转换的数字
     * @return 返回转换结果
     */
    public static long convertsToLong(double v) {
        BigDecimal b = new BigDecimal(v);
        return b.longValue();
    }

    /**
     * 返回两个数中大的一个值
     *
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 返回两个数中大的一个值
     */
    public static double returnMax(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.max(b2).doubleValue();
    }

    /**
     * 返回两个数中小的一个值
     *
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 返回两个数中小的一个值
     */
    public static double returnMin(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.min(b2).doubleValue();
    }

    /**
     * 精确对比两个数字
     *
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
     */
    public static int compareTo(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.compareTo(b2);
    }

}
```

