### JVM类加载过程

> 此处所说的类加载过程不单指类加载的某个阶段，而指类加载阶段到初始化阶段这个过程。



#### 类的生命周期

> 从一个.class文件，到被加载到虚拟机内存中，最后从虚拟机卸载出内存，称为类的生命周期。包括：加载（Loading）、验证（Verification）、准备（Preparation）、解析（Resolution）、初始化（Initialization）、使用（Using）和卸载（Uploading）7个阶段。其中验证、准备、解析3个部分统称为连接。

一个类的生命周期大致如下：

![image-20220725173957845](jvm.assets/image-20220725173957845.png)



![image-20221104004656129](jvm.assets/image-20221104004656129.png)



#### 类加载过程中的各个阶段

#####  加载

> 加载是类加载过程的一个阶段，在此阶段java虚拟机需要做如下事情：

- 通过一个类的全限定名来获取此类的二进制字节流
- 将字节流代表的类转化为==方法区==的运行时数据结构
- 在==堆==内存中生成唯一的Class对象，作为访问该类的入口

> 需要注意的是jvm对于第二点没有特定要求，并没有限定此二进制流是从文件中来的还是通过网络传输的

可以从

- 从压缩包中读取二进制流，zip、jar、war..
- 通过网络传输
- 运行时生成。例如：jdk动态代理，在运行期间通过ProxyGenerator.generateProxyClass来为特定接口生成形式为`$Proxy`的二进制字节流

- 从其他文件转化生成。例如：jsp文件会在首次访问的时候生成对应的servlet的java和class文件
- 数据库中读取

> 相较于类加载过程中的其他阶段，加载阶段相对可控，我们可以自定义加载器来加载我们所需要的指定类

##### 验证

> 验证是连接阶段的第一步，这一阶段的目的是为了确保 Class 文件的字节流中包含的信息符合当前虚拟机的要求，并且不会危害虚拟机自身的安全。

为何需要验证？

> 因为Class 文件来源是任意字节流，并不一定来自于 Java 源码编译，虚拟机如果不检查输入的字节流，对其完全信任的话，很可能会因为载入了有害的字节流而导致系统崩溃，所以验证是虚拟机对自身保护的一项重要工作。

验证哪些？

> 从整体上看，验证阶段大致上会完成下面4个阶段的检验动作：文件格式验证、元数据验证、字节码验证、符号引用验证。

- 文件格式验证

> 此阶段为验证阶段第一步，验证字节流格式是否符合当前版本虚拟机要求，当前阶段操作的是字节流，此阶段通过后jvm不会再去操作字节流，而是操作堆内存中代表class对象的数据结构



- 元数据验证

> 第二阶段是对字节码描述的信息进行语义分析，以保证其描述的信息符合 Java 语言规范的要求

1. 这个类是否有父类（除了`java.lang.Object`之外，所有的类都应当有父类）；

2. 这个类的父类是否有继承了不允许被继承的类（被 final 修饰的类）
3. 如果这个类不是抽象类，是否实现了其父类或接口中要求实现的所有方法；
4. 类中的字段、方法是否与父类产生矛盾（例如覆盖了父类的 fianl 字段，或者出现不符合规则的方法重载，例如方法参数都一致，但返回值类型却不同等）；

- 字节码验证

第三阶段是整个验证过程中最复杂的一个阶段，主要目的是通过数据流和控制流分析，确定程序语义是合法的、符合逻辑的。在第二阶段对元数据信息中的数据类型做完校验后，这个阶段对类的方法体进行校验分析，保证被校验的方法在运行时不会做出危害虚拟机安全的事件，例如：

1. 保证任何时刻操作数栈的数据类型与指令代码序列都能配合工作，例如不会出现类似这样的情况：在操作栈防止了一个 int 类型的数据，使用时却按 long 类型来加载如本地变量表中；
2. 保证跳转指令不会跳转到方法体以外的字节码指令上；
3. 保证方法体中的类型转换是有效地，例如可以把一个子类对象赋值给父类数据类型，这样是安全的，但是把父类对象赋值给子类数据类型，甚至把对象赋值给它毫无继承关系、毫不相关的一个数据类型，则是危险和不合法的；

如果一个类方法体的字节码没有通过字节码验证，那肯定是有问题的，但如果一个方法体通过了字节码验证，也不能说明其一定就是安全的。即使字节码验证之中进行了大量的检查，也不能保证这一点。这里设计了离散数学中一个很注明的问题“Halting Problem”：通俗一点的说法就是，通过程序去校验程序逻辑是无法做到绝对准确地 - 不能通过程序准确地检查出程序是否能在有限的时间之内结束运行。


-  符号引用验证

最后一个阶段的校验发生在虚拟机将符号引用转化为直接引用的时候，这个转化动作将在连接的第三阶段--解析阶段中发生。符号引用验证可以看做是对类自身以外（常量池中的各种符号引用）的信息进行匹配性校验，通常需要检验下列内容：

1. 符号引用中通过字符串描述的全限定名是否能找到对应的类；
2. 在指定类中是否存在符合方法的字段描述符以及简单名称所描述的方法和字段；
3. 符号引用中的类、字段、方法的访问性（private、default、protected、public）是否可被当前类访问；

符号应用验证的目的是确保解析动作能正常执行，如果无法通过符号引用验证，那么将会抛出一个`java.lang.IncompatibleClassChangeError`异常的子类，如`java.lang.IllegalAccessError`、`java.lang.NoSuchFieldError`、`java.lang.NoSuchMethodError`等。
对于虚拟机的类加载机制来说，验证阶段是一个非常重要的，但不是一定必要的（因为对程序运行期没有影响）的阶段。如果所运行的代码（包括自己编写的及第三方包中的代码）都已经被反复使用和验证过，那么在实施阶段就可以考虑使用 -Xverify:none 参数来关闭大部分的类验证措施，以缩短虚拟机类加载的时间。



##### 准备

> 准备阶段为被static修饰的成员变量在==方法区==分配内存并赋予初始值。

准备阶段是正式为类变量分配内存并设置类变量初始值的阶段，这些变量所使用的的内存都将在方法区中进行分配。这个阶段中有两个容易产生混淆的概念需要强调一下，首先，这时候进行内存分配的仅包括类变量（被 static 修饰的变量）而不包括实例变量，实例变量将在对象实例化时随着对象一起分配在 Java 堆中。其次，这里所说的初始值“通常情况”下是数据类型的零值，假设一个类变量的定义为：`public static int value = 123;`，那变量 value 在准备阶段过后的初始值为 0 而不是 123，因为这时候尚未开始执行任何 Java 方法，而把 value 赋值为 123 的 `putstatic` 指令是程序被编译后，存放于类构造器`<clinit>()`方法之中，所以把 value 赋值为 123 的动作将在初始化阶段才会执行。下表列出了 Java 中所有基本数据类型的零值：

- 基本数据类型      初始值为0
- 引用数据类型初始值为 null

##### 解析

解析阶段是虚拟机将常量池内的符号引用替换成直接引用的过程，符号引用在讲解 Class 文件格式的时候已经多次出现，在 Class 文件中它以 CONSTANT_Class_info、CONSTANT_Fieldref_info、CONSTANT_Methodref_info 等类型的常量出现，那解析阶段中所说的直接引用和符号引用又有什么关联呢？

- 符号引用（Symbolic References）：符号引用以一组符号来描述所引用的目标，符号可以使任何形式的字面量，只要使用时能无歧义地定位到目标即可。符号引用与虚拟机实现的内存布局无关，引用的目标并不一定已经加载到内存中。各种虚拟机实现的内存布局可以各不相同，但是它们能接受的符号引用必须都是一致的，因为符号引用的字面量形式明确定义在 Java 虚拟机规范的 Class 文件格式中。
- 直接引用（Direct References）：直接引用可以是直接指向目标的指针、相对偏移量或是一个能直接定位到目标的句柄。直接引用适合虚拟机实现的内存布局相关的，同一个符号引用在不同虚拟机实力上翻译出来的直接引用一般不会相同。如果有了直接引用，那引用的目标必定已经在内存中存在。



##### 初始化

> 类初始化阶段是类加载过程的最后一步，前面的类加载过程中，除了在加载阶段用户应用程序可以通过自定义类加载器参与之外，其余动作完全由虚拟机主导和控制。到了初始化阶段，才真正开始执行类中定义的 Java 程序代码（或者说字节码）。

在准备阶段类变量由虚拟机主导已经赋予过一次默认值，而在初始化阶段才会真正的按照程序员的主观意愿去给类变量设置初始值。此处涉及一个`cinit<>()`方法，可以理解为类的构造器，他会将被static修饰的变量及代码块按顺序封装起来，一起打包执行。

Code1：可以思考一下结果是多少

```java
public class StaticCodeTest {
    private static StaticCodeTest staticCodeTest = new StaticCodeTest();
    private static int a;
    private static int b = 0;
    static {
        a = 10;
        b = 9;
    }
    public StaticCodeTest() {
        a++;
        b++;
    }
    public static void main(String[] args) {
        System.out.println(a);
        System.out.println(b);
    }
}
```

![image-20220726003344759](jvm.assets/image-20220726003344759.png)

编译器会按顺序收集静态代码，并打包成`cinit`方法。

> `cinit<>()`方法可以理解为类的构造器，那么它与父类的类构造器有什么关系呢？

`<clinit>()`方法与类的构造函数（或者说实例构造器`<init>()`方法）不同，它不需要显示地调用父类构造器，虚拟机会保证在子类的`<clinit>()`方法执行之前，父类的`<clinit>()`方法已经执行完毕。因此在虚拟机第一个被执行的`<clinit>()`方法的类肯定是`java.lang.Object`。

Code2:结果为2，优先触发父类的初始化

```java
public class Parent {
    static int a = 1;
    static {
        ++a;
    }
}

class Son extends Parent {
    private static int b = a;
    public static void main(String[] args) {
        System.out.println(Son.b);
    }
}
```

![image-20220726004205338](jvm.assets/image-20220726004205338.png)

> `cinit<>()`不是必须的

- `<clinit>()`方法并不是必须的，如果一个类中没有静态语句块，也没有对类变量的赋值操作，那么编译器可以不为这个类生成`<clinit>()`方法
- 接口中不能使用静态语句块，但仍然有变量初始化的赋值操作，因此接口与类一样都会生成`<clinit>()`方法。但接口与类不同的是，执行接口的`<clinit>()`方法不需要先执行父接口的`<clinit>()`方法。只有当父接口中定义的变量使用时，父接口才会初始化。另外，接口的实现类在初始化时也一样不会执行接口的`<clinit>()`方法
- 虚拟机会保证一个类的`<clinit>()`在多线程环境中被正确的加锁、同步，如果多个线程同时去初始化一个类，那么只会有一个线程去执行这个类的`<clinit>()`方法，其他线程都需要阻塞等待，知道活动线程执行`<clinit>()`方法完毕。如果在一个类的`<clinit>()`方法中有耗时很长的操作，就可能造成多个线程阻塞。

第三点也是恶汉式单例可以保证线程安全的原因。



#### 何时会触发类的初始化

> 当对一个类进行直接引用的时候会触发类的初始化

##### 直接引用

> 什么情况下是对类的直接引用？

- 使用new关键字实例化对象时、读取或设置类的静态成员变量、调用静态方法
- 使用反射创建或调用对象时
- 当一个类存在父类，初始化当前类，此刻若父类没有初始化，则会先初始化父类
- 启动类所使用的main方法所在类

Code1：

```java
public class Code1 {
    static int i = 1;
    static {
        i = 10;
        System.out.println("i的值为：" + i);
    }
}
class Test{
    static{
        System.out.println("main方法所在类初始化。。。");
    }
    public static void main(String[] args) {
        final Code1 code1 = new Code1();
    }
}
```

此例说明第一点和第四点

![image-20220726010524997](jvm.assets/image-20220726010524997.png)

Code2:

```java
public class Code2 {
    public static void main(String[] args) throws ClassNotFoundException {     Class.forName("com.roily.classloader.cinit.Code1",true,ClassLoader.getSystemClassLoader() );
    }
}
```

第一个参数为类的全限定名称

第二个参数为是否触发类的初始话

第三个为类加载器

此例说明反射也会触发类的初始化。

![image-20220726010900070](jvm.assets/image-20220726010900070.png)

第二个参数设为false不会触发类的初始化：

![image-20220726011132098](jvm.assets/image-20220726011132098.png)



##### 间接引用

> 这里列出间接引用的例子，其他情况基本上都会触发类的初始化

- 通过子类引用父类的静态字段，只会触发父类的初始化，而不会触发子类的初始化。
- 定义对象数组和集合，不会触发该类的初始化
- 类A引用类B的static final常量不会导致类B初始化（注意静态常量必须是字面值常量，否则还是会触发B的初始化）
- 通过类名获取Class对象，不会触发类的初始化。如System.out.println(Person.class);
- 通过Class.forName加载指定类时，如果指定参数initialize为false时，也不会触发类初始化。
- 通过ClassLoader默认的loadClass方法，也不会触发初始化动作



Code1：通过子类调用父类静态成员变量不会触发子类初始化

```java
public class Code1 {
    public static void main(String[] args) {
        System.out.println(Son.a);
    }
}
class Parent {
    static int a = 1;
    static {
        System.out.println("父类初始化");
    }
}
class Son extends Parent {
    static {
        System.out.println("子类初始化");
    }
}
```

![image-20220726011653498](jvm.assets/image-20220726011653498.png)



Code2: 引用被final static修饰的只有字面量的变量不会触发类的初始化

```java
public class Code2 {
    
    public static void main(String[] args) {
        System.out.println(Demo.a);
        System.out.println(Demo2.str);
    }
}

class Demo {
    final static int a = 1;
    static {
        System.out.println("Demo初始化");
    }
}

class Help {
    static String a = "abc";
}

class Demo2 {
    /**
     * 不是字面量，会触发类的初始化
     */
    final static String str = Help.a;
    static {
        System.out.println("Demo2初始化");
    }
}
```

![image-20220726012228025](jvm.assets/image-20220726012228025.png)

