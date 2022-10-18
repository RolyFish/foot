# Spring5框架

# 1.spring

## 1.1 简介

 Spring : 春天 —>给软件行业带来了春天

 2002年，Rod Jahnson首次推出了Spring框架雏形interface21框架。

 2004年3月24日，Spring框架以interface21框架为基础，经过重新设计，发布了1.0正式版。

 很难想象Rod Johnson的学历 , 他是悉尼大学的博士，然而他的专业不是计算机，而是音乐学。

 Spring理念 : 使现有技术更加实用 . 本身就是一个大杂烩 , 整合现有的框架技术

SSH Struct2 Spring  Hibernate

SSM  SpringMVC Spring Mybatis

  

 官网 : http://spring.io/

 官方下载地址 : https://repo.spring.io/libs-release-local/org/springframework/spring/

 GitHub : https://github.com/spring-projects

导入相关包

```java

<!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.2.5.RELEASE</version>
</dependency>
 
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.2.3.RELEASE</version>
</dependency>
```



## 1.2 优点

Spring是一个开源免费的框架 , 容器 .

Spring是一个轻量级的框架 , 非侵入式的 .

控制反转 IoC , 面向切面 Aop

对事物的支持 , 对框架的支持

支持事物的处理，支持框架整合的支持

一句话概括：

Spring是一个轻量级的控制反转(IoC)和面向切面(AOP)的容器（框架）。

## 1.3 组成

![](spring5.assets/20210131140056459.png)

Spring 框架是一个分层架构，由 7 个定义良好的模块组成。Spring 模块构建在核心容器之上，核心容器定义了创建、配置和管理 bean 的方式 .

![](spring5.assets/20210131141437402.png)

组成 Spring 框架的每个模块（或组件）都可以单独存在，或者与其他一个或多个模块联合实现。每个模块的功能如下：

- 核心容器：核心容器提供 Spring 框架的基本功能。核心容器的主要组件是 BeanFactory，它是工厂模式的实现。BeanFactory 使用控制反转（IOC） 模式将应用程序的配置和依赖性规范与实际的应用程序代码分开。

- Spring 上下文：Spring 上下文是一个配置文件，向 Spring 框架提供上下文信息。Spring 上下文包括企业服务，例如 JNDI、EJB、电子邮件、国际化、校验和调度功能。

- Spring AOP：通过配置管理特性，Spring AOP 模块直接将面向切面的编程功能 , 集成到了 Spring 框架中。所以，可以很容易地使 Spring 框架管理任何支持 AOP的对象。Spring AOP 模块为基于 Spring 的应用程序中的对象提供了事务管理服务。通过使用 Spring AOP，不用依赖组件，就可以将声明性事务管理集成到应用程序中。

- Spring DAO：JDBC DAO 抽象层提供了有意义的异常层次结构，可用该结构来管理异常处理和不同数据库供应商抛出的错误消息。异常层次结构简化了错误处理，并且极大地降低了需要编写的异常代码数量（例如打开和关闭连接）。Spring DAO 的面向 JDBC 的异常遵从通用的 DAO 异常层次结构。

- Spring ORM：Spring 框架插入了若干个 ORM 框架，从而提供了 ORM 的对象关系工具，其中包括 JDO、Hibernate 和 iBatis SQL Map。所有这些都遵从 Spring 的通用事务和 DAO 异常层次结构。

- Spring Web 模块：Web 上下文模块建立在应用程序上下文模块之上，为基于 Web 的应用程序提供了上下文。所以，Spring 框架支持与 Jakarta Struts 的集成。Web 模块还简化了处理多部分请求以及将请求参数绑定到域对象的工作。

- Spring MVC 框架：MVC 框架是一个全功能的构建 Web 应用程序的 MVC 实现。通过策略接口，MVC 框架变成为高度可配置的，MVC 容纳了大量视图技术，其中包括 JSP、Velocity、Tiles、iText 和 POI。

## 1.4 拓展

- Spring Boot与Spring Cloud

- Spring Boot 是 Spring 的一套快速配置脚手架，可以基于Spring Boot 快速开发单个微服务;

- Spring Cloud是基于Spring Boot实现的；

- Spring Boot专注于快速、方便集成的单个微服务个体，Spring Cloud关注全局的服务治理框架；

- Spring Boot使用了约束优于配置的理念，很多集成方案已经帮你选择好了，能不配置就不配置 , Spring Cloud很大的一部分是基于Spring Boot来实现，Spring Boot可以离开Spring Cloud独立使用开发项目，但是Spring Cloud离不开Spring Boot，属于依赖的关系。

- SpringBoot在SpringClound中起到了承上启下的作用，如果你要学习SpringCloud必须要学习SpringBoot。

![](spring5.assets/20210131141539566.png)



- Spring Boot 是 Spring 的一套快速配置脚手架，可以基于Spring Boot 快速开发单个微服务;

- Spring Cloud是基于Spring Boot实现的；

- Spring Boot专注于快速、方便集成的单个微服务个体，Spring Cloud关注全局的服务治理框架；

- Spring Boot使用了约束优于配置的理念，很多集成方案已经帮你选择好了，能不配置就不配置 , Spring Cloud很大的一部分是基于Spring Boot来实现，Spring Boot可以离开Spring Cloud独立使用开发项目，但是Spring Cloud离不开Spring Boot，属于依赖的关系。

- SpringBoot在SpringClound中起到了承上启下的作用，如果你要学习SpringCloud必须要学习SpringBoot。

  

  因为现在大多数公司都在使用SpringBoot进行快速开发，学习SpringBoot的前提，需要完全掌握Spring以及SpringMVC 承上启下的作用

弊端：发展了太久之后，违背了原来的理念，配置十分繁琐，人称 配置地狱

# 2. IOC理论推导
## 2.1 分析实现
我们先用我们原来的方式写一段代码 .

1、先写一个UserDao接口

```java
public interface UserDao {
    public void getUser();
}
```

2、再去写Dao的实现类

```java
public class UserDaoImpl implements UserDao {
    @Override
    public void getUser() {
        System.out.println("获取用户数据");
    }
}
```

3、然后去写UserService的接口

```java
public interface UserService {
    public void getUser();
}
```

- 4、最后写Service的实现类

```java
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
 
    @Override
    public void getUser() {
        userDao.getUser();
    }
}
```

5、测试一下

```java
@Test
public void test(){
    UserService service = new UserServiceImpl();
    service.getUser();
}
```

这是我们原来的方式 , 开始大家也都是这么去写的对吧 . 那我们现在修改一下 .

把Userdao的实现类增加一个 .

```java
public class UserDaoMySqlImpl implements UserDao {
    @Override
    public void getUser() {
        System.out.println("MySql获取用户数据");
    }
}
```

- 紧接着我们要去使用MySql的话 , 我们就需要去service实现类里面修改对应的实现

```java
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoMySqlImpl();
 
    @Override
    public void getUser() {
        userDao.getUser();
    }
}
```

在假设, 我们再增加一个Userdao的实现类 .

```java
public class UserDaoOracleImpl implements UserDao {
    @Override
    public void getUser() {
        System.out.println("Oracle获取用户数据");
    }
}
```

那么我们要使用Oracle , 又需要去service实现类里面修改对应的实现 . 假设我们的这种需求非常大 , 这种方式就根本不适用了, 甚至反人类对吧 , 每次变动 , 都需要修改大量代码 . 这种设计的耦合性太高了, 牵一发而动全身 .



**那我们如何去解决呢 ?**

我们可以在需要用到他的地方 , 不去实现它 , 而是留出一个接口 , 利用set , 我们去代码里修改下 .

```java
public class UserServiceImpl implements UserService {
   private UserDao userDao;
// 利用set实现
   public void setUserDao(UserDao userDao) {
       this.userDao = userDao;
  }
 
   @Override
   public void getUser() {
       userDao.getUser();
  }
}
```

之前，程序主动创建对象，控制权在程序员手上。

使用了set注入后，程序不再具有主动性，而是变成了被动的接受对象

这种思想，从本质上解决了问题，我们程序员再也不用取关对象的创建了。系统的耦合性大大降低，专注业务上。这是IOC 的原型。



## 2.2 IOC本质
控制反转IoC(Inversion of Control)，是一种设计思想，DI(依赖注入)是实现IoC的一种方法，也有人认为DI只是IoC的另一种说法。没有IoC的程序中 , 我们使用面向对象编程 , 对象的创建与对象间的依赖关系完全硬编码在程序中，对象的创建由程序自己控制，控制反转后将对象的创建转移给第三方，个人认为所谓控制反转就是：获得依赖对象的方式反转了。

早期程序，创建对象的主动权在业务层上

![img](spring5.assets/20210131155538230.png)

经过控制反转之后

创建对象的主动权在用户层上

![img](spring5.assets/20210131155552506.png)

![img](spring5.assets/20210131155810470.png)

IoC是Spring框架的核心内容，使用多种方式完美的实现了IoC，可以使用XML配置，也可以使用注解，新版本的Spring也可以零配置实现IoC。

Spring容器在初始化时先读取配置文件，根据配置文件或元数据创建与组织对象存入容器中，程序使用时再从Ioc容器中取出需要的对象。

![img](spring5.assets/20210131155832652.png)

采用XML方式配置Bean的时候，Bean的定义信息是和实现分离的，而采用注解的方式可以把两者合为一体，Bean的定义信息直接以注解的形式定义在实现类中，从而达到了零配置的目的。

控制反转是一种通过描述（XML或注解）并通过第三方去生产或获取特定对象的方式。在Spring中实现控制反转的是IoC容器，其实现方法是依赖注入（Dependency Injection,DI）。



# 3. HelloSpring

1、编写一个Hello实体类

> 导入Jar包

注 : spring 需要导入commons-logging进行日志记录 . 我们利用maven , 他会自动下载对应的依赖项 .

```java
<dependency>
   <groupId>org.springframework</groupId>
   <artifactId>spring-webmvc</artifactId>
   <version>5.1.10.RELEASE</version>
</dependency>
```

编写代码

编写一个实体类

```java
public class Hello {
   private String name;
 
   public String getName() {
       return name;
  }
   public void setName(String name) {
       this.name = name;
  }
 
   public void show(){
       System.out.println("Hello,"+ name );
  }
}
```

2、编写我们的spring文件 , 这里我们命名为beans.xml 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="hello" class="com.kuang.pojo.Hello">
        <property name="str" value="Spring"/>
    </bean>
</beans>
```

3、我们可以去进行测试了 .

```java
 
@Test
public void test(){
   //解析beans.xml文件 , 生成管理相应的Bean对象
   ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
   //getBean : 参数即为spring配置文件中bean的id .
   Hello hello = (Hello) context.getBean("hello");
   hello.show();
}
```

- Hello 对象是谁创建的 ?  【hello 对象是由Spring创建的
- Hello 对象的属性是怎么设置的 ?  hello 对象的属性是由Spring容器设置的

bean =对象

id=变量名

class=new 对象

property 相当于给兑现中的属性设置一个值。

核心用set注入

这个过程就叫控制反转 :

控制 : 谁来控制对象的创建 , 传统应用程序的对象是由程序本身控制创建的 , 使用Spring后 , 对象是由Spring来创建的

反转 : 程序本身不创建对象 , 而变成被动的接收对象 .

依赖注入 : 就是利用set方法来进行注入的.

IOC是一种编程思想，由主动的编程变成被动的接收

 

ref 引用Spring 容器中创建好的对象

value 具体的值，基本数据类型



## 4. IOC创建对象方式

### 4.1 通过无参构造来创建

1、User.java

```java
public class User {
 
    private String name;
 
    public User() {
        System.out.println("user无参构造方法");
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public void show(){
        System.out.println("name="+ name );
    }
 
}
```



2、beans.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">
 
    <bean id="user" class="com.kuang.pojo.User">
        <property name="name" value="kuangshen"/>
    </bean>
 
</beans>
```



3、测试类

```java
@Test
public void test(){
    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    //在执行getBean的时候, user已经创建好了 , 通过无参构造
    User user = (User) context.getBean("user");
    //调用对象的方法 .
    user.show();
}
```

- 结果可以发现，在调用show方法之前，User对象已经通过无参构造初始化了！

### 4.2 通过有参构造方法来创建

将无参构造更改为有参构造

```java
public UserT(String name) {
	this.name = name;
}
```

2、beans.xml 有三种方式编写

```java
<!-- 第一种根据index参数下标设置 -->
<bean id="userT" class="com.kuang.pojo.UserT">
    <!-- index指构造方法 , 下标从0开始 -->
    <constructor-arg index="0" value="kuangshen2"/>
</bean>
 
<!-- 第二种根据参数名字设置 -->
<bean id="userT" class="com.kuang.pojo.UserT">
    <!-- name指参数名 -->
    <constructor-arg name="name" value="kuangshen2"/>
</bean>
 
<!-- 第三种根据参数类型设置 -->
<bean id="userT" class="com.kuang.pojo.UserT">
    <constructor-arg type="java.lang.String" value="kuangshen2"/>
</bean>
```



3、测试

```java
@Test
public void testT(){
    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    UserT user = (UserT) context.getBean("userT");
    user.show();
}
```

- 结论：在配置文件加载的时候。其中管理的对象都已经初始化了！



# 5. Spring 配置

### 5.1 别名

```xml
<!--  别名 : 如果添加了别名，我们也可以使用别名获取到这个对象 -->
<alias name="User" alias="u1"></alias>
```

### 5.2 Bean的配置

```java
<!--
  id : bean的唯一标识符，相当于类名
  class : bean 对象所对应的完全限定名：包名 + 类型
  name : 也是别名,而且name更高级，可以取多个，而且有多个分隔符
  -->
<bean id="UserT" class="com.kuang.pojo.UserT" name="u2 u21,u22;u23">
    <property name="name" value="123"/>
</bean>
```

### 5.3 import

团队的合作通过import来实现 .

```xml
<import resource="beans.xml"/>
```

能将多个人开发的不同的配置xml文件整合到applicationContext.xml文件中，并且能够合适的去重。



# 6.依赖注入（DI）

- 依赖注入（Dependency Injection,DI）。
- 依赖 : 指Bean对象的创建依赖于容器 . Bean对象的依赖资源 .
- 注入 : 指Bean对象所依赖的资源 , 由容器来设置和装配 .



### 6.1 构造器注入

 前面已经介绍

### 6.2 Set方式注入（重点）

依赖注入：set注入

依赖：bean对象的创建依赖于容器

注入：bean对象中所有的属性，由容器来注入

普通值注入，基于set注入

环境搭建

Address类

```java
package com.kuang.pojo;
 
public class Address {
    private String address;
 
    public Address(String address) {
        this.address = address;
    }
 
    public String getAddress() {
        return address;
    }
 
    public void setAddress(String address) {
        this.address = address;
    }
}
```

Student类

```java
package com.kuang.pojo;
 
import java.util.*;
 
public class Student {
    private String name;
    private Address address;
    private String[] books;
    private Map<String,String> card;
    private Set<String> games;
    private List<String> hobbys;
    private Properties info;
    private String wife;
 
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", books=" + Arrays.toString(books) +
                ", card=" + card +
                ", games=" + games +
                ", hobbys=" + hobbys +
                ", info=" + info +
                ", wife='" + wife + '\'' +
                '}';
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public Address getAddress() {
        return address;
    }
 
    public void setAddress(Address address) {
        this.address = address;
    }
 
    public String[] getBooks() {
        return books;
    }
 
    public void setBooks(String[] books) {
        this.books = books;
    }
 
    public Map<String, String> getCard() {
        return card;
    }
 
    public void setCard(Map<String, String> card) {
        this.card = card;
    }
 
    public Set<String> getGames() {
        return games;
    }
 
    public void setGames(Set<String> games) {
        this.games = games;
    }
 
    public List<String> getHobbys() {
        return hobbys;
    }
 
    public void setHobbys(List<String> hobbys) {
        this.hobbys = hobbys;
    }
 
    public Properties getInfo() {
        return info;
    }
 
    public void setInfo(Properties info) {
        this.info = info;
    }
 
    public String getWife() {
        return wife;
    }
 
    public void setWife(String wife) {
        this.wife = wife;
    }
}
```

beans.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">
  
 
 
 
</beans>
```

### 6.2.1 常量注入

```xml
<bean id="Student" class="com.kuang.pojo.Student">
    <!--第一种，普通值注入，value-->
    <property name="name" value="kuangshen"></property>
</bean>
```

### 6.2.2 数组注入

```xml
<property name="books">
            <array>
                <value>红楼梦</value>
                <value>西游记</value>
                <value>水浒传</value>
                <value>三国演义</value>
            </array>
</property>
```



### 6.2.3 列表注入

```xml
<property name="hobbys">
            <list>
                <value>听歌</value>
                <value>敲代码</value>
                <value>看电影</value>
            </list>
 </property>
```

### 6.2.4 map注入

```xml
<property name="card">
            <map>
                <entry key="身份证" value="11111111"/>
                <entry key="银行卡" value="11111111"/>
            </map>
</property>
```

### 6.2.5 set注入

```xml
<property name="games">
            <set>
                <value>LOL</value>
                <value>COC</value>
                <value>WOW</value>
            </set>
</property>
```

### 6.2.6 null注入

```xml
<property name="wife">
            <null/>
        </property>
```

### 6.2.7 properties注入

```xml
<property name="info">
            <props>
                <prop key="学号">2019929</prop>
                <prop key="性别">男</prop>
                <prop key="username">kuangshen</prop>
            </props>
        </property>
```

完善注入信息

### 6.3 拓展方式注入

p命名和c命名不能直接使用，需要在头部导入xml约束

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:c="http://www.springframework.org/schema/c"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    https://www.springframework.org/schema/beans/spring-beans.xsd">
</beans>
```

```xml
xmlns:p="http://www.springframework.org/schema/p"
xmlns:c="http://www.springframework.org/schema/c"
```



```java
package com.kuang.pojo;
 
public class User02 {
    private int id;
    private String name;
    private int age;
 
    public User02() {
    }
 
    public User02(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public int getAge() {
        return age;
    }
 
    public void setAge(int age) {
        this.age = age;
    }
}
```

p命名注入 property

```xml
<!-- p命名空间注入，可以直接注入属性的值：property -->
<bean id="User"  class="com.kuang.pojo.User" p:name ="老秦" p:age ="18"/>
```



c命名空间注入 constructor

```xml
<!-- c命名空间注入，通过构造器注入：construct-args -->
<bean id="User2" class="com.kuang.pojo.User" c:age="18" c:name="老李"/>
```

注意点：p命名和c命名不能直接使用，需要导入xml约束



### 6.4 bean的作用域

在Spring中，那些组成应用程序的主体及由Spring IoC容器所管理的对象，被称之为bean。简单地讲，bean就是由IoC容器初始化、装配及管理的对象 .

![img](spring5.assets/20210201113650722.png)

1 代理模式 Spring 默认机制



## 6.4.1 单例模式(Spring默认机制)
当一个bean的作用域为Singleton，那么Spring IoC容器中只会存在一个共享的bean实例，并且所有对bean的请求，只要id与该bean定义相匹配，则只会返回bean的同一实例。Singleton是单例类型，就是在创建起容器时就同时自动创建了一个bean的对象，不管你是否使用，他都存在了，每次获取到的对象都是同一个对象。注意，Singleton作用域是Spring中的缺省作用域。要在XML中将bean定义成singleton，可以这样配置：

```xml
<bean id="User2" class="com.kuang.pojo.User" c:age="18" c:name="老李" scope="singleton"/>
```

```java
@Test
public void test1(){
    ApplicationContext context = new ClassPathXmlApplicationContext("userbeans.xml");
    User user1 = context.getBean("User2", User.class);
    User user2 = context.getBean("User2", User.class);
    System.out.println(user1==user2);
}
```

- 说明user1和user2是同一个对象，即是有且只有一个user

![img](spring5.assets/20210201113727810.png)



### 6.4.2 原型模式

每次从容器中get的时候，都会产生一个新的对象，不像单例模式

当一个bean的作用域为Prototype，表示一个bean定义对应多个对象实例。Prototype作用域的bean会导致在每次对该bean请求（将其注入到另一个bean中，或者以程序的方式调用容器的getBean()方法）时都会创建一个新的bean实例。Prototype是原型类型，它在我们创建容器的时候并没有实例化，而是当我们获取bean的时候才会去创建一个对象，而且我们每次获取到的对象都不是同一个对象。根据经验，对有状态的bean应该使用prototype作用域，而对无状态的bean则应该使用singleton作用域。在XML中将bean定义成prototype，可以这样配置：

```xml
<bean id="User2" class="com.kuang.pojo.User" c:age="18" c:name="老李" scope="prototype"/>
```

## 6.4.3 其余模式
如request,session,application(全局活着)这些只能在web开发中使用!

# 7.Bean的自动装配
自动装配是使用spring满足bean依赖的一种方法
spring会在应用上下文中为某个bean寻找其依赖的bean。
Spring中bean有三种装配机制，分别是：

在xml中显式配置；
在java中显式配置；
隐式的bean发现机制和自动装配。
这里我们主要讲第三种：自动化的装配bean。

Spring的自动装配需要从两个角度来实现，或者说是两个操作：

组件扫描(component scanning)：spring会自动发现应用上下文中所创建的bean；
自动装配(autowiring)：spring自动满足bean之间的依赖，也就是我们说的IoC/DI；
组件扫描和自动装配组合发挥巨大威力，使得显示的配置降低到最少。

推荐不使用自动装配xml配置 , 而使用注解 .

1 搭建环境

一个人两个宠物

传统装配

```xml
<bean id="cat" class="com.kuang.pojo.Cat"/>
    <bean id="dog" class="com.kuang.pojo.Dog"/>
    <bean id="person" class="com.kuang.pojo.Person">
        <property name="name" value="小狂神啊"/>
        <property name="dog" ref="dog"/>
        <property name="cat" ref="cat"/>
    </bean>
```



### 7.1 byName自动装配

byName 会自动在容器上下文中查找，和自己对象set方法后面的值 对应的bean id

```xml
<bean id="cat" class="com.kuang.pojo.Cat"/>
    <bean id="dog" class="com.kuang.pojo.Dog"/>
    <bean id="person" class="com.kuang.pojo.Person" autowire="byName">
        <property name="name" value="小狂神啊"/>
        
    </bean>
```

如果我们将 cat 的bean id修改为 catXXX

再次测试， 执行时报空指针java.lang.NullPointerException。因为按byName规则找不对应set方法，真正的setCat就没执行，对象就没有初始化，所以调用时就会报空指针错误。一定要注意对应宠物的bean id 一定改成小写开头的

当一个bean节点带有 autowire byName的属性时。

1. 将查找其类中所有的set方法名，例如setCat，获得将set去掉并且首字母小写的字符串，即cat。
2. 去spring容器中寻找是否有此字符串名称id的对象。
3. 如果有，就取出注入；如果没有，就报空指针异常。



### 7.2 byType自动装配

```xml
<bean id="cat" class="com.kuang.pojo.Cat"/>
    <bean id="dog" class="com.kuang.pojo.Dog"/>
    <bean id="person" class="com.kuang.pojo.Person" autowire="byType">
        <property name="name" value="小狂神啊"/>
        
    </bean>
```



byType 会自动在容器上下文中查找，和自己对象属性相同的值对应的Bean id

- byName: 需要保证所有的bean的id唯一，并且这个bean需要和自动注入的属性的set的方法的值一致！
- byType: 需要保证所有的bean的class唯一，并且这个bean需要和自动注入的属性的类型一致！



### 7.3 使用注解实现自动装配

要使用注解

1.导入约束

context约束

2.配置注解的支持

 <context:annotation-config/>【重要】



```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">
 
    <context:annotation-config/>
</beans>
```



**@Autowired**

@Autowired是按类型自动转配的，不支持id匹配。byType
需要导入 spring-aop的包！
直接在属性上使用即可！也可以在set方式上使用！

使用Autowired我们可以不用编写Set方法了，前提是这个自动装配的属性在IOC容器中存在，且符合名字byname。

```java
 @Autowired
    private Cat cat;
    @Autowired
    private Dog dog;
```

```java
@Nullable     // 字段标记了这个注解，说明这个字段可以为null
```



或者 如果显示定义了Autowired的required 的属性为false ，说明这个对象可以为null，允许为空

 

autowired 注解应该是只能是别的，当注入 在IOC容器中该类型只有一个时，就通过byType进行装配，当注入容器存在多个同意类型的对象是，就是根据byName进行装配

如果@Autowired自动装配的环境比较复杂，自动装配无法通过一个注解[@Autowired]完成的时候，我们可以使用@Qualifier(value=“XXX”)去配置@Autowired的使用，指定一个唯一的bean对象注入。

**@Qualifier**

@Autowired是根据类型自动装配的，加上@Qualifier则可以根据byName的方式自动装配
@Qualifier不能单独使用。

```java
public class People {
    private String name;
    @Autowired
    @Qualifier("cat")
    private Cat cat;
    @Autowired
    @Qualifier("dog")
    private Dog dog;
}
```



**@Resource注解**

- @Resource如有指定的name属性，先按该属性进行byName方式查找装配；
- 其次再进行默认的byName方式进行装配；
- 如果以上都不成功，则按byType的方式自动装配。
- 都不成功，则报异常。



```java
public class People {
    private String name;
    @Resource(name = "cat")
    private Cat cat;
    @Resource(name = "dog")
    private Dog dog;
```

小结
@Autowired与@Resource异同：

@Autowired与@Resource都可以用来装配bean。都可以写在字段上，或写在setter方法上。

@Autowired默认按类型装配（属于spring规范），默认情况下必须要求依赖对象必须存在，如果要允许null 值，可以设置它的required属性为false，如：@Autowired(required=false) ，如果我们想使用名称装配可以结合@Qualifier注解进行使用

@Resource（属于J2EE复返），默认按照名称进行装配，名称可以通过name属性进行指定。如果没有指定name属性，当注解写在字段上时，默认取字段名进行按照名称查找，如果注解写在setter方法上默认取属性名进行装配。当找不到与名称匹配的bean时才按照类型进行装配。但是 需要注意的是，如果name属性一旦指定，就只会按照名称进行装配。

它们的作用相同都是用注解方式注入对象，但执行顺序不同。@Autowired先byType，@Resource先byName。



## 8. 使用注解开发

使用注解开发

### 8.1说明

在spring4之后，想要使用注解形式，必须得要引入aop的包

![img](spring5.assets/c9b4e2f4450d046a60621bfeba00791e.png)

在配置文件当中，还得要引入一个context约束

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">
 
</beans>
```

2、在指定包下编写类，增加注解

```java
@Component("user")
// 相当于配置文件中 <bean id="user" class="当前注解的类"/>
public class User {
    public String name = "秦疆";
}
```

3、测试

```java
@Test
public void test(){
    ApplicationContext applicationContext =
        new ClassPathXmlApplicationContext("beans.xml");
    User user = (User) applicationContext.getBean("user");
    System.out.println(user.name);
}
```

### 8.2 Bean的实现

我们之前都是使用 bean 的标签进行bean注入，但是实际开发中，我们一般都会使用注解！

1、配置扫描哪些包下的注解

```xml
<!--指定注解扫描包-->
<context:component-scan base-package="com.kuang.pojo"/>
```



### 8.3 属性注入

使用注解注入属性

1、可以不用提供set方法，直接在直接名上添加@value(“值”)

```java
@Component("user")
// 相当于配置文件中 <bean id="user" class="当前注解的类"/>
public class User {
    @Value("秦疆")
    // 相当于配置文件中 <property name="name" value="秦疆"/>
    public String name;
}
```



2、如果提供了set方法，在set方法上添加@value(“值”);

```java
@Component("user")
public class User {
 
    public String name;
 
    @Value("秦疆")
    public void setName(String name) {
        this.name = name;
    }
}
```

## 8.4 衍生注解
我们这些注解，就是替代了在配置文件当中配置步骤而已！更加的方便快捷！

@Component三个衍生注解

为了更好的进行分层，Spring可以使用其它三个注解，功能一样，目前使用哪一个功能都一样。

@Controller：web层
@Service：service层
@Repository：dao层
写上这些注解，就相当于将这个类交给Spring管理装配了！

自动装配注解

在Bean的自动装配已经讲过了，可以回顾！

作用域

@scope

singleton：默认的，Spring会采用单例模式创建这个对象。关闭工厂 ，所有的对象都会销毁。
prototype：多例模式。关闭工厂 ，所有的对象不会销毁。内部的垃圾回收机制会回收。

```java
@Controller("user")
@Scope("prototype")
public class User {
    @Value("秦疆")
    public String name;
}
```

​	**小结**

**XML与注解比较**

- XML可以适用任何场景 ，结构清晰，维护方便
- 注解不是自己提供的类使用不了，开发简单方便

**xml与注解整合开发** ：推荐最佳实践

- xml管理Bean
- 注解完成属性注入
- 使用过程中， 可以不用扫描，扫描是为了类上的注解

```xml
<context:annotation-config/>  
```



作用：

- 进行注解驱动注册，从而使注解生效
- 用于激活那些已经在spring容器里注册过的bean上面的注解，也就是显示的向Spring注册
- 如果不扫描包，就需要手动配置bean
- 如果不加注解驱动，则注入的值为null！



## 9. 使用java的方式配置Spring

我们现在要完全不使用spring的xml配置，全权交给java来做。

实体类User

```java
package com.kuang.pojo;
 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
// 这里的这个注解的意思，就是说明这个类被Spring托管了，注册到了容器中
@Controller
public class User {
    // 属性的注入
    @Value("qingjiang")
    private String name;
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
```

配置类

```java
package com.kuang.config;
 
import com.kuang.pojo.User;
import org.springframework.context.annotation.*;
 
// @Configuration本质上也是一个Controller，也会被spring托管，注册到容器中
// @Configuration表示这是一个配置类，和之前的beans.xml的功能一样的
 
@Configuration
@ComponentScan("com.kuang.pojo")        // @ComponentScan表示组件的扫描
@Import(MyConfig1.class)                 // 通过import导入其他的配置类
public class MyConfig {
    // @Bean 表示注册一个bean
    // 方法的名字即是bean中的id
    // 方法的返回值就是bean中的class
    @Bean
    public User getUser(){
        return new User();      // return就是返回要注入到bean中的对象
    }
 
}
```

测试类

```java
import com.kuang.config.MyConfig;
import com.kuang.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
 
public class MyTest {
    @Test
    public void test(){
        // 如果使用了java配置类的方式来注册bean，
        // 只能通过AnnotationConfig上下文来获取容器，通过配置类的class对象加载！
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        // 获取通过配置类中的方法getUser方法来获得的User对象
        User user = (User) context.getBean("getUser");
        System.out.println(user.toString());
    }
}
```

- 这种纯java配置方式，在SpringBoot中随处可见！

```java
@Component 
```

这里这个注解的意思就是说明这个类被Spring接管了，注册到了容器中

@Configuration本质上还是一个@Component，代表这个一个配置类和bean.xml一样

@ComponentScan("com.kuang.pojo") // @ComponentScan表示组件的扫描 @Import(MyConfig1.class) // 通过import导入其他的配置类



# 10 代理模式

![img](spring5.assets/20210201192134505.png)


代理模式：

- 静态代理
- 动态代理



## 10.1 静态代理
角色分析：

抽象角色 : 一般使用接口或者抽象类来实现

真实角色 : 被代理的角色

代理角色 : 代理真实角色 ; 代理真实角色后 , 一般会做一些附属的操作 .

客户 : 使用代理角色来进行一些操作 .

 

rent接口

```java
package com.kuang.Daili;
 
public interface Rent {
    public void rent();
}
```

Client租户

```java
package com.kuang.Daili;
 
public class Client {
    public static void main(String[] args) {
        Host host=new Host();
        //host.rent();
        Proxy proxy=new Proxy(host);
        proxy.rent();
    }
}
```

proxy代理角色

```java
package com.kuang.Daili;
 
public class Proxy {
    private Host host;
    public Proxy(){
 
    }
    public Proxy(Host host){
        this.host=host;
    }
    public void rent(){
        seeHouse();
 
        host.rent();
        hetong();
        fare();
    }
    public void seeHouse(){
        System.out.println("中介带你看房");
    }
    public void fare(){
        System.out.println("收中介费!");
    }
    public void hetong(){
        System.out.println("签租领合同");
    } 
 
}
```

房东

```java
package com.kuang.Daili;
 
public class Host implements Rent{
    @Override
    public void rent() {
        System.out.println("房东出租房子！");
    }
}
```

分析：在这个过程中，你直接接触的就是中介，就如同现实生活中的样子，你看不到房东，但是你依旧租到了房东的房子通过代理，这就是所谓的代理模式，程序源自于生活，所以学编程的人，一般能够更加抽象的看待生活中发生的事情。



静态代理的好处:

- 可以使得我们的真实角色更加纯粹 . 不再去关注一些公共的事情 .
- 公共的业务由代理来完成 . 实现了业务的分工 ,
- 公共业务发生扩展时变得更加集中和方便 .



缺点 :

- 类多了 , 多了代理类 , 工作量变大了 . 开发效率降低 .
  我们想要静态代理的好处，又不想要静态代理的缺点，所以 , 就有了动态代理

  

代码步骤:

- 接口
- 真实角色
- 代理角色
- 客户端访问代理角色



## 10.2 静态代理的再理解
1、创建一个抽象角色，比如咋们平时做的用户业务，抽象起来就是增删改查！

```java
//抽象角色：增删改查业务
public interface UserService {
    void add();
    void delete();
    void update();
    void query();
}
```

2、我们需要一个真实对象来完成这些增删改查操作

```java
//真实对象，完成增删改查操作的人
public class UserServiceImpl implements UserService {
 
    public void add() {
        System.out.println("增加了一个用户");
    }
 
    public void delete() {
        System.out.println("删除了一个用户");
    }
 
    public void update() {
        System.out.println("更新了一个用户");
    }
 
    public void query() {
        System.out.println("查询了一个用户");
    }
}
```

3、需求来了，现在我们需要增加一个日志功能，怎么实现！

- 思路1 ：在实现类上增加代码 【麻烦！】
- 思路2：使用代理来做，能够不改变原来的业务情况下，实现此功能就是最好的了！



4、设置一个代理类来处理日志！代理角色

```java
//代理角色，在这里面增加日志的实现
public class UserServiceProxy implements UserService {
    private UserServiceImpl userService;
 
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }
 
    public void add() {
        log("add");
        userService.add();
    }
 
    public void delete() {
        log("delete");
        userService.delete();
    }
 
    public void update() {
        log("update");
        userService.update();
    }
 
    public void query() {
        log("query");
        userService.query();
    }
 
    public void log(String msg){
        System.out.println("执行了"+msg+"方法");
    }
 
}
```

OK，到了现在代理模式大家应该都没有什么问题了，重点大家需要理解其中的思想；我们在不改变原来的代码的情况下，实现了对原有功能的增强，这是AOP中最核心的思想



## 10.3 动态代理
动态代理的角色和静态代理的一样 .

动态代理的代理类是动态生成的 . 静态代理的代理类是我们提前写好的

动态代理分为两类 : 一类是基于接口动态代理 , 一类是基于类的动态代理

基于接口的动态代理----JDK动态代理
基于类的动态代理–cglib
现在用的比较多的是 javasist 来生成动态代理 . 百度一下javasist
我们这里使用JDK的原生代码来实现，其余的道理都是一样的！

### 10.3.1 JDK的动态代理需要了解两个类

核心 : InvocationHandler 和 Proxy ， 打开JDK帮助文档看看

【InvocationHandler：调用处理程序】

![img](spring5.assets/20210201202822371.png)



```java
Object invoke(Object proxy, 方法 method, Object[] args)；
//参数
//proxy - 调用该方法的代理实例
//method -所述方法对应于调用代理实例上的接口方法的实例。方法对象的声明类将是该方法声明的接口，它可以是代理类继承该方法的代理接口的超级接口。
//args -包含的方法调用传递代理实例的参数值的对象的阵列，或null如果接口方法没有参数。原始类型的参数包含在适当的原始包装器类的实例中，例如java.lang.Integer或java.lang.Boolean 。
```



【Proxy : 代理】

![img](spring5.assets/bfc070f91aded96f7bce9380e24e3721.png)



![img](spring5.assets/25fad03708df3a3b1f1b9c13c3821b77.png)



```java
//生成代理类
public Object getProxy(){
    return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                  rent.getClass().getInterfaces(),this);
}
```

**代码实现**

抽象角色和真实角色和之前的一样！

Rent . java 即抽象角色

```java
//抽象角色：租房
public interface Rent {
    public void rent();
}
```



Host . java 即真实角色

```java
//真实角色: 房东，房东要出租房子
public class Host implements Rent{
    public void rent() {
        System.out.println("房屋出租");
    }
}
```

ProxyInvocationHandler. java 即代理角色

```java
public class ProxyInvocationHandler implements InvocationHandler {
    private Rent rent;
 
    public void setRent(Rent rent) {
        this.rent = rent;
    }
 
    //生成代理类，重点是第二个参数，获取要代理的抽象角色！之前都是一个角色，现在可以代理一类角色
    public Object getProxy(){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                rent.getClass().getInterfaces(),this);
    }
 
    // proxy : 代理类 method : 代理类的调用处理程序的方法对象.
    // 处理代理实例上的方法调用并返回结果
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        seeHouse();
        //核心：本质利用反射实现！
        Object result = method.invoke(rent, args);
        fare();
        return result;
    }
 
    //看房
    public void seeHouse(){
        System.out.println("带房客看房");
    }
    //收中介费
    public void fare(){
        System.out.println("收中介费");
    }
 
}
```

- Client . java

```java
//租客
public class Client {
 
    public static void main(String[] args) {
        //真实角色
        Host host = new Host();
        //代理实例的调用处理程序
        ProxyInvocationHandler pih = new ProxyInvocationHandler();
        pih.setRent(host); //将真实角色放置进去！
        Rent proxy = (Rent)pih.getProxy(); //动态生成对应的代理类！
        proxy.rent();
    }
 
}
```

- 核心：**一个动态代理 , 一般代理某一类业务 , 一个动态代理可以代理多个类，代理的是接口！**



### 10.3.2 编写一个通用代理工具类：

```java
public class ProxyInvocationHandler implements InvocationHandler {
    private Object target;
 
    public void setTarget(Object target) {
        this.target = target;//得到真实角色
    }
 
    //生成代理类
    public Object getProxy(){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                target.getClass().getInterfaces(),this);
    }
 
    // proxy : 代理类
    // method : 代理类的调用处理程序的方法对象.
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log(method.getName());
        Object result = method.invoke(target, args);
        return result;
    }
 
    public void log(String methodName){
        System.out.println("执行了"+methodName+"方法");
    }
 
}
```



测试！

```java
public class Test {
    public static void main(String[] args) {
        //真实对象
        UserServiceImpl userService = new UserServiceImpl();
        //代理对象的调用处理程序
        ProxyInvocationHandler pih = new ProxyInvocationHandler();
        pih.setTarget(userService); //设置要代理的对象
        UserService proxy = (UserService)pih.getProxy(); //动态生成代理类！
        proxy.delete();
    }
}
```

- 测试，增删改查，查看结果！



动态代理，由于静态代理需要给每一个实体类创建一个代理类，实现静态代理。这样做的缺点就是让代码量变得异常多，所以产生了动态代理。

我们的目标是生成代理类，接口拥有代理对象和目标对象共同的类信息。所以动态代理可以看作是通过接口生成一个新的代理类



# 11. AOP
## 11.1 什么是AOP
AOP（Aspect Oriented Programming）意为：面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。

![img](spring5.assets/20210201213236844.png)

### 11.2 Aop在Spring中的作用

![img](spring5.assets/20210201213350579.png)

![img](spring5.assets/20210201213439581.png)



**即 Aop 在 不改变原有代码的情况下 , 去增加新的功能 .**

### 11.3 使用Spring实现Aop

【重点】使用AOP织入，需要导入一个依赖包！

```xml
<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.4</version>
</dependency>
```

11.3.1 第一种方式

**通过 Spring API 实现**

首先编写我们的业务接口和实现类

```java
public interface UserService {
 
    public void add();
 
    public void delete();
 
    public void update();
 
    public void search();
 
} 
```

```java
public class UserServiceImpl implements UserService{
 
    @Override
    public void add() {
        System.out.println("增加用户");
    }
 
    @Override
    public void delete() {
        System.out.println("删除用户");
    }
 
    @Override
    public void update() {
        System.out.println("更新用户");
    }
 
    @Override
    public void search() {
        System.out.println("查询用户");
    }
}
```

然后去写我们的增强类 , 我们编写两个 , 一个前置增强 一个后置增强

```java
public class Log implements MethodBeforeAdvice {
 
    //method : 要执行的目标对象的方法
    //objects : 被调用的方法的参数
    //Object : 目标对象
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println( o.getClass().getName() + "的" + method.getName() + "方法被执行了");
    }
}
```

```java
public class AfterLog implements AfterReturningAdvice {
    //returnValue 返回值
    //method被调用的方法
    //args 被调用的方法的对象的参数
    //target 被调用的目标对象
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("执行了" + target.getClass().getName()
        +"的"+method.getName()+"方法,"
        +"返回值："+returnValue);
    }
}
```

最后去spring的文件中注册 , 并实现aop切入实现 , 注意导入约束 .

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">
 
    <!--注册bean-->
    <bean id="userService" class="com.kuang.service.UserServiceImpl"/>
    <bean id="log" class="com.kuang.log.Log"/>
    <bean id="afterLog" class="com.kuang.log.AfterLog"/>
 
    <!--aop的配置-->
    <aop:config>
        <!--切入点  expression:表达式匹配要执行的方法-->
        <aop:pointcut id="pointcut" expression="execution(* com.kuang.service.UserServiceImpl.*(..))"/>
        <!--执行环绕; advice-ref执行方法 . pointcut-ref切入点-->
        <aop:advisor advice-ref="log" pointcut-ref="pointcut"/>
        <aop:advisor advice-ref="afterLog" pointcut-ref="pointcut"/>
    </aop:config>
 
</beans>
```



测试

```java
public class MyTest {
    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserService userService = (UserService) context.getBean("userService");
        userService.search();
    }
}
```

Aop的重要性 : 很重要 . 一定要理解其中的思路 , 主要是思想的理解这一块 .

Spring的Aop就是将公共的业务 (日志 , 安全等) 和领域业务结合起来 , 当执行领域业务时 , 将会把公共业务加进来 . 实现公共业务的重复利用 . 领域业务更纯粹 , 程序猿专注领域业务 , 其本质还是动态代理 .

Aop的重要性 : 很重要 . 一定要理解其中的思路 , 主要是思想的理解这一块 .

Spring的Aop就是将公共的业务 (日志 , 安全等) 和领域业务结合起来 , 当执行领域业务时 , 将会把公共业务加进来 . 实现公共业务的重复利用 . 领域业务更纯粹 , 程序猿专注领域业务 , 其本质还是动态代理 .

## 11.3.2 第二种方式

**自定义类来实现Aop**

# **主要是切面**

目标业务类不变依旧是userServiceImpl

第一步 : 写我们自己的一个切入类

```java
public class DiyPointcut {
 
    public void before(){
        System.out.println("---------方法执行前---------");
    }
    public void after(){
        System.out.println("---------方法执行后---------");
    }
    
}
```

- 去spring中配置

```xml
<!--第二种方式自定义实现-->
<!--注册bean-->
<bean id="diy" class="com.kuang.config.DiyPointcut"/>
 
<!--aop的配置-->
<aop:config>
    <!--第二种方式：使用AOP的标签实现-->
    <aop:aspect ref="diy">
        <aop:pointcut id="diyPonitcut" expression="execution(* com.kuang.service.UserServiceImpl.*(..))"/>
        <aop:before pointcut-ref="diyPonitcut" method="before"/>
        <aop:after pointcut-ref="diyPonitcut" method="after"/>
    </aop:aspect>
</aop:config>
```

测试：

```java
public class MyTest {
    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserService userService = (UserService) context.getBean("userService");
        userService.add();
    }
}
```

# 11.3.3 第三种方式

**使用注解实现AOP**

第一步：编写一个注解实现的增强类



```java
package com.kuang.config;
 
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
 
@Aspect
public class AnnotationPointcut {
    @Before("execution(* com.kuang.service.UserServiceImpl.*(..))")
    public void before(){
        System.out.println("---------方法执行前---------");
    }
 
    @After("execution(* com.kuang.service.UserServiceImpl.*(..))")
    public void after(){
        System.out.println("---------方法执行后---------");
    }
 
    @Around("execution(* com.kuang.service.UserServiceImpl.*(..))")
    public void around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("环绕前");
        System.out.println("签名:"+jp.getSignature());
        //执行目标方法proceed
        Object proceed = jp.proceed();
        System.out.println("环绕后");
        System.out.println(proceed);
    }
}
```

第二步：在Spring配置文件中，注册bean，并增加支持注解的配置

```xml
<!--第三种方式:注解实现-->
<bean id="annotationPointcut" class="com.kuang.config.AnnotationPointcut"/>
<aop:aspectj-autoproxy/>
```

aop:aspectj-autoproxy：说明

```xml
通过aop命名空间的<aop:aspectj-autoproxy />声明自动为spring容器中那些配置@aspectJ切面的bean创建代理，织入切面。当然，spring 在内部依旧采用AnnotationAwareAspectJAutoProxyCreator进行自动代理的创建工作，但具体实现的细节已经被<aop:aspectj-autoproxy />隐藏起来了
 
<aop:aspectj-autoproxy />有一个proxy-target-class属性，默认为false，表示使用jdk动态代理织入增强，当配为<aop:aspectj-autoproxy  poxy-target-class="true"/>时，表示使用CGLib动态代理技术织入增强。不过即使proxy-target-class设置为false，如果目标类没有声明接口，则spring将自动使用CGLib动态代理。
```



# 12 整合Mybatis

```xml
<dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.6</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.23</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/log4j/log4j -->
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.10</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>RELEASE</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>RELEASE</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.mybatis.caches</groupId>
      <artifactId>mybatis-ehcache</artifactId>
      <version>1.2.1</version>
    </dependency>
    
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.3.1</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.3.1</version>
    </dependency>
    
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>2.0.2</version>
    </dependency>
```



### 12.1 回忆mybatis

**1.编写实体类**

```java
package com.kuang.pojo;
 
public class User {
    private int id;  //id
    private String name;   //姓名
    private String pwd;   //密码
}
```

- **2.实现mybatis的配置文件**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
 
    <typeAliases>
        <package name="com.kuang.pojo"/>
    </typeAliases>
 
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=true&amp;useUnicode=true&amp;characterEncoding=utf8"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
 
    <mappers>
        <package name="com.kuang.dao"/>
    </mappers>
</configuration>
```

- **3.编写接口**

  UserDao接口编写

  ```java
  public interface UserMapper {
      public List<User> selectUser();
  }
  ```

**4.编写相应接口的Mapper.xml文件**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kuang.dao.UserMapper">
 
    <select id="selectUser" resultType="User">
      select * from user
    </select>
 
</mapper>
```

**5.测试**

```java
@Test
public void selectUser() throws IOException {
 
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession sqlSession = sqlSessionFactory.openSession();
 
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
 
    List<User> userList = mapper.selectUser();
    for (User user: userList){
        System.out.println(user);
    }
 
    sqlSession.close();
}
```

## 12.2 Mybatis-spring
引入Spring之前需要了解mybatis-spring包中的一些重要类；

http://www.mybatis.org/spring/zh/index.html

什么是 MyBatis-Spring？

MyBatis-Spring 会帮助你将 MyBatis 代码无缝地整合到 Spring 中。

知识基础

在开始使用 MyBatis-Spring 之前，你需要先熟悉 Spring 和 MyBatis 这两个框架和有关它们的术语。这很重要

MyBatis-Spring 需要以下版本：

| MyBatis-Spring | MyBatis | Spring 框架 | Spring Batch | Java    |
| :------------- | :------ | :---------- | :----------- | :------ |
| 2.0            | 3.5+    | 5.0+        | 4.0+         | Java 8+ |
| 1.3            | 3.4+    | 3.2.2+      | 2.1+         | Java 6+ |

如果使用 Maven 作为构建工具，仅需要在 pom.xml 中加入以下代码即可：

```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>2.0.2</version>
</dependency>
```

要和 Spring 一起使用 MyBatis，需要在 Spring 应用上下文中定义至少两样东西：一个 SqlSessionFactory 和至少一个数据映射器类即DataSource。

在 MyBatis-Spring 中，可使用SqlSessionFactoryBean来创建 SqlSessionFactory。要配置这个工厂 bean，只需要把下面代码放在 Spring 的 XML 配置文件中：

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="dataSource" ref="dataSource" />
</bean>
```

注意：SqlSessionFactory需要一个 DataSource（数据源）。这可以是任意的 DataSource，只需要和配置其它 Spring 数据库连接一样配置它就可以了。

在基础的 MyBatis 用法中，是通过 SqlSessionFactoryBuilder 来创建 SqlSessionFactory 的。而在 MyBatis-Spring 中，则使用 SqlSessionFactoryBean 来创建。

在 MyBatis 中，你可以使用 SqlSessionFactory 来创建 SqlSession。一旦你获得一个 session 之后，你可以使用它来执行映射了的语句，提交或回滚连接，最后，当不再需要它的时候，你可以关闭 session。

SqlSessionFactory有一个唯一的必要属性：用于 JDBC 的 DataSource。这可以是任意的 DataSource 对象，它的配置方法和其它 Spring 数据库连接是一样的。

一个常用的属性是 configLocation，它用来指定 MyBatis 的 XML 配置文件路径。它在需要修改 MyBatis 的基础配置非常有用。通常，基础配置指的是 < settings> 或 < typeAliases>元素。

需要注意的是，这个配置文件并不需要是一个完整的 MyBatis 配置。确切地说，任何环境配置（），数据源（）和 MyBatis 的事务管理器（）都会被忽略。SqlSessionFactoryBean 会创建它自有的 MyBatis 环境配置（Environment），并按要求设置自定义环境的值。

SqlSessionTemplate 是 MyBatis-Spring 的核心。作为 SqlSession 的一个实现，这意味着可以使用它无缝代替你代码中已经在使用的 SqlSession。

模板可以参与到 Spring 的事务管理中，并且由于其是线程安全的，可以供多个映射器类使用，你应该总是用 SqlSessionTemplate 来替换 MyBatis 默认的 DefaultSqlSession 实现。在同一应用程序中的不同类之间混杂使用可能会引起数据一致性的问题。

可以使用 SqlSessionFactory 作为构造方法的参数来创建 SqlSessionTemplate 对象。

 

 

 

 

目前，我们需要将spring和mybatis进行整合，那么首先我们需要考虑 mybatis做了什么，mybaits通过读取配置文件的信息，然后生成sqlSessionFactory 进一步生成sqlsession，然后进行操作。

因此我们新建一个spring xml文件

 

这段代码相当于之前的mybaits-confi 中数据库信息

```xml
 <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/mybatis?userSSL=true&amp;useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=UTC"/>
        <property name="username" value="root"/>
        <property name="password" value="123456"/>
 
    </bean>
```

这段代码相当于配置好sqlsessionFactory，

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <!--绑定mybatis配置文件-->
        <property name="configuration" value="classpath:mybatis-config.xml"/>
        <property name="mapperLocations" value="classpath:com/kuang/mapper/*.xml"/>
    </bean>
```

如何生成sqlsession呢，我们需要在spring中进行配置。

```xml
 <!--就是我们使用的sqlsession-->
    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
        <!--只能使用构造器注入，因为没有set方法-->
        <constructor-arg index="0" ref="sqlSessionFactory"/>
    </bean>
```

在之前的mybatis中，我们的sqlsession是通过在实现类生成sqlsession来执行的，那么在spring中，我们是通过注入进行的。

在具体的实现类中，我们生成set方法，以便spring进行注入。

```java
package com.kuang.mapper;
 
import com.kuang.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
 
import java.util.List;
 
public class UserMapperImpl implements UserMapper{
 
    //我们所有的操作，在原来我们的操作都使用sqlsession来执行，现在我们使用sqlsessionTemplate进行
    private SqlSessionTemplate sqlSessionTemplate;
    public void setSqlSession(SqlSessionTemplate sqlSession){
        this.sqlSessionTemplate=sqlSession;
    }
    @Override
    public List<User> selectUser() {
        UserMapper mapper=sqlSessionTemplate.getMapper(UserMapper.class);
        return mapper.selectUser();
    }
}
```

在xml中进行注册

```xml
 <bean id="userMapper" class="com.kuang.mapper.UserMapperImpl">
        <property name="sqlSession" ref="sqlSession"/>
 
</bean>
```

具体的步骤：

1 编写数据源配置

2 sqlSessionFactory

3 sqlSessionTemplate

4 需要给接口加实现类

5 将自己写的实现类，注入到Spring中

6 测试使用即可 

SqlSessionTemplate是mybatis-Spring 的核心。作为SqlSession的一个实现，这意味着可以使用它无缝代替代码中已经存在使用的SqlSession ，SqlSessionTemplate是线程安全的。可以被多个DAO映射器使用。




### 12.2.2 整合Mybatis与spring方式二
mybatis-spring1.2.3版以上的才有这个 .
官方文档截图 :

dao继承Support类 , 直接利用 getSqlSession() 获得 , 然后直接注入SqlSessionFactory . 比起方式1 , 不需要管理SqlSessionTemplate , 而

![img](spring5.assets/fb347108b36c97f56e3c4bd9c6dfba32.png)



测试：

1、将我们上面写的UserDaoImpl修改一下

```java
public class UserDaoImpl extends SqlSessionDaoSupport implements UserMapper {
    public List<User> selectUser() {
        UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
        return mapper.selectUser();
    }
}
```

2、修改bean的配置

```xml
<bean id="userDao" class="com.kuang.dao.UserDaoImpl">
    <property name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>
```

3、测试

```xml
@Test
public void test2(){
    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    UserMapper mapper = (UserMapper) context.getBean("userDao");
    List<User> user = mapper.selectUser();
    System.out.println(user);
}
```

**总结 : 整合到spring以后可以完全不要mybatis的配置文件，除了这些方式可以实现整合之外，我们还可以使用注解来实现，这个等我们后面学习SpringBoot的时候还会测试整合！**



# 13.声明式事物
## 13.1 事物的回顾
事务在项目开发过程非常重要，涉及到数据的一致性的问题，不容马虎！
事务管理是企业级应用程序开发中必备技术，用来确保数据的完整性和一致性。
事务就是把一系列的动作当成一个独立的工作单元，这些动作要么全部完成，要么全部不起作用。

事务四个属性ACID

- 原子性（atomicity）

  - 事务是原子性操作，由一系列动作组成，事务的原子性确保动作要么全部完成，要么完全不起作用

- 一性（consistency）

  - 一旦所有事务动作完成，事务就要被提交。数据和资源处于一种满足业务规则的一致性状态中

- 隔离性（isolation）

  - 可能多个事务会同时处理相同的数据，因此每个事务都应该与其他事务隔离开来，防止数据损坏

- 持久性（durability）

  - 事务一旦完成，无论系统发生什么错误，结果都不会受到影响。通常情况下，事务的结果被写到持久化存储器中

  

### 13.2 测试

将上面的代码拷贝到一个新项目中

在之前的案例中，我们给userDao接口新增两个方法，删除和增加用户；

```java
//添加一个用户
int addUser(User user);
 
//根据id删除用户
int deleteUser(int id);
```



mapper文件，我们故意把 deletes 写错，测试！

```xml
 <insert id="addUser" parameterType="com.kuang.pojo.User">
 insert into user (id,name,pwd) values (#{id},#{name},#{pwd})
 </insert>
 <delete id="deleteUser" parameterType="int">
 deletes from user where id = #{id}
</delete>
```



编写接口的实现类，在实现类中，我们去操作一波

```java
public class UserDaoImpl extends SqlSessionDaoSupport implements UserMapper {
 
    //增加一些操作
    public List<User> selectUser() {
        User user = new User(4,"小明","123456");
        UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
        mapper.addUser(user);
        mapper.deleteUser(4);
        return mapper.selectUser();
    }
 
    //新增
    public int addUser(User user) {
        UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
        return mapper.addUser(user);
    }
    //删除
    public int deleteUser(int id) {
        UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
        return mapper.deleteUser(id);
    }
 
}
```

- 测试

```java
@Test
public void test2(){
    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    UserMapper mapper = (UserMapper) context.getBean("userDao");
    List<User> user = mapper.selectUser();
    System.out.println(user);
}
```



报错：sql异常，delete写错了

结果 ：插入成功！

没有进行事务的管理；我们想让他们都成功才成功，有一个失败，就都失败，我们就应该需要事务！

以前我们都需要自己手动管理事务，十分麻烦！

但是Spring给我们提供了事务管理，我们只需要配置即可；

## 13.3 Spring中的事务管理
Spring在不同的事务管理API之上定义了一个抽象层，使得开发人员不必了解底层的事务管理API就可以使用Spring的事务管理机制。Spring支持编程式事务管理和声明式的事务管理。

编程式事务管理

- 将事务管理代码嵌到业务方法中来控制事务的提交和回滚

- 缺点：必须在每个事务操作业务逻辑中包含额外的事务管理代码

  

声明式事务管理

- 一般情况下比编程式事务好用。
- 将事务管理代码从业务方法中分离出来，以声明的方式来实现事务管理。
- 将事务管理作为横切关注点，通过aop方法模块化。Spring中通过Spring AOP框架支持声明式事务管理。



JDBC事务

```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
 </bean>
```



**配置好事务管理器后我们需要去配置事务的通知**

```xml
<!--配置事务通知-->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <!--配置哪些方法使用什么样的事务,配置事务的传播特性-->
        <tx:method name="add" propagation="REQUIRED"/>
        <tx:method name="delete" propagation="REQUIRED"/>
        <tx:method name="update" propagation="REQUIRED"/>
        <tx:method name="search*" propagation="REQUIRED"/>
        <tx:method name="get" read-only="true"/>
        <tx:method name="*" propagation="REQUIRED"/>
    </tx:attributes>
</tx:advice>
```



spring事务传播特性：

事务传播行为就是多个事务方法相互调用时，事务如何在这些方法间传播。spring支持7种事务传播行为：

- propagation_requierd：如果当前没有事务，就新建一个事务，如果已存在一个事务中，加入到这个事务中，这是最常见的选择。
- propagation_supports：支持当前事务，如果没有当前事务，就以非事务方法执行。
- propagation_mandatory：使用当前事务，如果没有当前事务，就抛出异常。
- propagation_required_new：新建事务，如果当前存在事务，把当前事务挂起。
- propagation_not_supported：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
- propagation_never：以非事务方式执行操作，如果当前事务存在则抛出异常。
- propagation_nested：如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与propagation_required类似的操作
  Spring 默认的事务传播行为是 PROPAGATION_REQUIRED，它适合于绝大多数的情况。



假设 ServiveX#methodX() 都工作在事务环境下（即都被 Spring 事务增强了），假设程序中存在如下的调用链：Service1#method1()->Service2#method2()->Service3#method3()，那么这 3 个服务类的 3 个方法通过 Spring 的事务传播机制都工作在同一个事务中。

就好比，我们刚才的几个方法存在调用，所以会被放在一组事务当中！

配置AOP

导入aop的头文件！

```xml
<!--配置aop织入事务-->
<aop:config>
    <aop:pointcut id="txPointcut" expression="execution(* com.kuang.dao.*.*(..))"/>
    <aop:advisor advice-ref="txAdvice" pointcut-ref="txPointcut"/>
</aop:config>
```



**进行测试**

删掉刚才插入的数据，再次测试！

```java
@Test
public void test2(){
    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    UserMapper mapper = (UserMapper) context.getBean("userDao");
    List<User> user = mapper.selectUser();
    System.out.println(user);
}
```

