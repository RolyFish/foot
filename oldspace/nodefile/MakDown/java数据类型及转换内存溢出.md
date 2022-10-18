# 注释 

1. //  单行
2.   /**/  多行

# PrimitiveType

## 数值类型

1. byte   1  -128~127  
2. short  2  -65536~65535
3. int       4  -2147483648 ~  2147483647
4. long    **8  -9334472036854775808 ~ 9334472036854775807**
5. float  4      
6. double 8    
7. boolean  true /false
8. char 

## 进制

1. int i = 10;
2. int i = 010;
3. int i = 0x10;

## 知识点

> double  float 精度问题
>



```java
@Test
public void test02(){
    float f = 0.123456789123f;
    double d = 0.123456789123456789;
    System.out.println(f);
    System.out.println(d);

    float f1 = 0.25f;
    double d1 = 2.5/10;
    System.out.println(f1==d1);

    float f2 = 0.2f;
    double d2 = 2.0/10;
    System.out.println(f2==d2);
    double d3 = (double)f2;
    System.out.println(d3);
}
```

结果：

![image-20220104211412997](java数据类型及转换.assets\image-20220104211412997.png)

float  在计算机中的存储方式 1位符号位，8位指数位，23位位数位（省略整数1）

double同理  1  16  47

第一个尾数大小就这样。

<hr>

0.25float类型在计算机中的表示方法：

0 01111101 001 0000 0000 0000 0000

1.0  2的-2次方

0.25double类型在计算机中的表示方法：

0 0111111111111101 001 0000 0000 0000 0000 0000 0000 0000 0000

1.0  2的-2次方

除得尽转化为十进制数值也相等。

<hr>

0.2float类型在计算机中的表示方法：

小数部分转换二进制方式：乘二取整  0011  0011.。。。。无限循环

0 01111110  0011  0011.。。。。

0.2double类型在计算机中的表示方法：

0 0111111111111110 010 0000 0000 0000 0000 0000 0000 0000 0000

除得尽转化为十进制数值也相等。

## char

```java
char c1  = 90;
System.out.println(c1);
char c2  = 65535;
System.out.println(c2);
char c3 = 'a';
System.out.println(c3);
System.out.println((int)c3);
char c4 = '\u0061';
System.out.println(c4);
char c5 = 97;
System.out.println(c5);
```

![image-20220104213648561](java数据类型及转换.assets\image-20220104213648561.png)



> char类型占2字节8位，没有负数

[浮点数在内存中的存储方式](https://www.cnblogs.com/jillzhang/archive/2007/06/24/793901.html)

## string

```java
@Test
public void test04(){
    String s1 = "hello world";
    String s2 = "hello world";
    System.out.println(s1==s2);

    String s3 = new String("hello world");
    String s4 = new String("hello world");
    System.out.println(s3==s4);

}
```

![image-20220104214039886](java数据类型及转换.assets\image-20220104214039886.png)

> java中每一个基本类型都有其对应的包装类  int =》integer

String str = “hello world”；

第一创建，java虚拟机回去jvm字符串常量池中去寻找这个‘’hello world‘’字符串对象，如果没有则创建并将字符串对象的应用赋给str。

第二次创建时直接将“hello world”字符串对象引用赋给str2.

所以说==相同

 String s3 = new String("hello world"); 这个是new了一个新的对象，引用自然不同

[内存分析](https://juejin.cn/post/6844903984323362829)

## 类型转换

```java
@Test
public void test05() {
    int i = 128;
    byte b = (byte) 128;//内存溢出
    System.out.println("int(i)128:  " + i + "\n" +
            "byte(b)128:  " + b);
    byte b2 = (byte) 129;//内存溢出
    System.out.println(b2);
    double d = i;
    System.out.println("int(i)128:  " + i + "\n" +
            "double(d)i:  " + d);
}
```

内存角度：

<img src="java数据类型及转换.assets\image-20220104220214049.png" alt="image-20220104220214049" style="zoom:50%;" />

1000 0001 反码加一 11111111  第一位符号位 -127

000000

原码  反码   补码
    原码---正数本身-负数符号位不变 其余取反---->反码
    原码----正数本身---负数 符号不变 取反加一--->补码