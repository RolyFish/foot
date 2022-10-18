# ioc

## ioc怎么来的

```txt
java类中的属性除了基本类型，也就是对象的声明和定义是分开的。
```

dao

```java
public interface UserDao {
    public  void getUser();
}
```

daoimpl1

```java
public class UserDaoImpl1 implements UserDao {
    public void getUser() {
        System.out.println("dao1层得到用户");
    }
}
```

daoimpl2

```java
public class UserDaoImpl2 implements UserDao {
    public void getUser() {
        System.out.println("dao2层得到用户");
    }
}

```

service

```java
public class UserServiceImpl implements UserService {
    /**
     * 如果我们想要使用UserDao中的方法
     * 1、声明它
     * 2、想要用就得定义它  new
     * 如果说UserDao有另一个实现类 想用又得去new
     * 改变了UserService  接口 实现 参数
     * 弊端很多
     */
    private UserDao userDao1 = new UserDaoImpl1();
    private UserDao userDao2= new UserDaoImpl2();

    public void getUser1() {
        System.out.println("service 层  调用dao层");
        userDao1.getUser();
    }
    public void getUser2() {
        System.out.println("service 层  调用dao层");
        userDao2.getUser();
    }
}
```

通过set方法对他进行改进：

```java
public class UserServiceImpl2 implements UserService2 {
    private UserDao userDao;
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    public void getUser() {
        System.out.println("service调用dao");
        userDao.getUser();
    }
}
```

通过构造器改进：

```java
public class UserServiceImpl2 implements UserService2 {
    private UserDao userDao;
    public UserServiceImpl2(UserDao userDao) {
        this.userDao = userDao;
    }
    public UserServiceImpl2() {
        this.userDao = new UserDaoImpl1();
    }
    public void getUser() {
        System.out.println("service调用dao");
        userDao.getUser();
    }
}
```

```xml
这里只给一个声明 它的实现是什么并不关心 只需调用其方法就行
但是也得将他实例化
两种方法：
1、set
   userdao可以是他的任何实现类
   这样就可以少修改点service的代码
2、构造器
   有参构造可实现注入 
```

**通过以上说明可以发现，set和构造器大有文章可做**

## 依赖注入（属性注入）

user实体类

```java
public class User {
    private String name;
    private String pwd;
    public User() {
        System.out.println("无参构造器");
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void show() {
        System.out.println("Hello," + name);
    }
}
```

配置文件：set注入

```xml
<bean id="user" class="com.roily.pojo.User">
    <property name="name" value="Spring"/>
</bean>
```

测试：

```java
public void test01(){
    ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
    User user = (User) context.getBean("user");
    user.show();
}
```

![image-20211212154224817](C:\Users\Roly_Fish\AppData\Roaming\Typora\typora-user-images\image-20211212154224817.png)

结论：

***set注入默认走无参构造，实例化他***

构造器注入：

1. index  下标
2. name  属性名称
3. type   属性类型

```xml
<bean id="user2" class="com.roily.pojo.User">
    <constructor-arg index="0" value="springuser2"/>
</bean>
<bean id="user3" class="com.roily.pojo.User">
    <constructor-arg name="name" value="springuser3"/>
</bean>
<bean id="user4" class="com.roily.pojo.User">
    <constructor-arg  type="java.lang.String" value="springuser4"/>
</bean>
<bean id="user5" class="com.roily.pojo.User">
    <constructor-arg  type="java.lang.String" value="springuser5"/>
    <constructor-arg  type="java.lang.String" value="springuser5"/>
</bean>
```

说明：读取配置文件信息

![image-20211212154820273](C:\Users\Roly_Fish\AppData\Roaming\Typora\typora-user-images\image-20211212154820273.png)

可以发现打的target包，其配置文件在根目录下。

```java
这个就是classpath:ApplicationContext.xml
    
ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
```

## 看一看图片

![image-20211212155232097](C:\Users\Roly_Fish\AppData\Roaming\Typora\typora-user-images\image-20211212155232097.png)

spring默认创建对象模式，单例模式，及读取配置文件的时候ioc容器中实例化了该对象，且全局唯一，每次拿到的都是同一个对象。

![image-20211212161555161](C:\Users\Roly_Fish\AppData\Roaming\Typora\typora-user-images\image-20211212161555161.png)

原型模式，propertype，在读取配置文件并没有实例化他，在get的时候实例化，拿到的不一样。

![image-20211212161814938](C:\Users\Roly_Fish\AppData\Roaming\Typora\typora-user-images\image-20211212161814938.png)

## 自动装配

首先看实体类

- Cat
- Dog
- People

配置文件：

想要使用注解就得引入<<context:annotation-config/>>标签

```xml
<context:annotation-config/>
<bean id="cat" class="com.roily.pojo.Cat"/>
<bean id="dog1" class="com.roily.pojo.Dog"/>
<bean id="dog2" class="com.roily.pojo.Dog"/>
<bean id="people" class="com.roily.pojo.People">
```

```java
public class People {
    private String name;
    @Autowired
    @Qualifier("dog1")
    private Dog dog;
    @Autowired
    private Cat cat;
    setget~~~~~~
}
```

***@autowired 默认使用类型匹配，如果说类型匹配不准确，也就是注入了多个同类型的bean***

***就得使用Qualifier来过滤***

## 扫描+装配

配置文件：支持注解，扫描装配

```xml
<context:annotation-config/>
<context:component-scan base-package="com.roily"/>
```

实体类：声明他是一个组件可以被扫描，@value给他初始化一个属性

```java
@Component
public class User2 {
    @Value("于延闯2")
    private String name;
    public String getName() {
        return name;
    }
}
```

test

```java
@org.junit.Test
public void test05(){
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    User2 user2 = context.getBean("user2", User2.class);
    System.out.println(user2.getName());
}
```

## javaconfig

这是一个配置类，configration 配置了俩bean，引入了config2配置类

```java
@Configuration
//@ComponentScan("com.roily.pojo")
@Import(JavaConfig2.class)
public class JavaConfig {
    @Bean
    User1 getUser1(){
        return new User1();
    }
    @Bean
    public User2 getUser2(){
        return new User2();
    }
}
=========================================================================================
@Configuration
//@ComponentScan("com.roily.pojo")
public class JavaConfig2 {

    @Bean
    public User3 getUser3(){
        return new User3();
    }
}
```

test，这里因为没有引入componentscan不自动扫描

```java
@org.junit.Test
public void test03(){
    ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
    User3 getUser = (User3) context.getBean("getUser3");
    System.out.println(getUser.getName());
}
```

# aop

## 代理模式

### 静态代理

真实角色

```java
public class Host implements Rent{
    public void rent() {
        System.out.println("真实对象：房东：出租房子！！");
    }
}
```

代理角色:对真实角色做增强

```java
public class Proxy implements Rent{
    private Host host;

    public Proxy() {
    }

    public Proxy(Host host) {
        this.host = host;
    }

    public void rent() {
        this.seeHouse();
        host.rent();
    }
    public void seeHouse() {
        System.out.println("代理对象：中介：看房");
    }
}
```

实现了不改变原有代码的基础上对对象进行增强，**有aop的味道了**

但是每一个真实对象，都需要一个代理对象（即便是同一类《实现同一个接口》）

所以说不太好

### 动态代理

```java
代理对象的调用处理程序所需要的实现的接口
InvocationHandler
```

```java
通多proxy这个静态类的静态方法newProxyInstance，得到代理实例
Proxy
```

代理实例的调用处理程序

```java
public class ProxyInvh implements InvocationHandler {
    //需要代理的对象，真实对象
    private Rent rent;
    
    public void setRent(Rent rent) {
        this.rent = rent;
    }
	//得到代理角色
    public Rent getProxy(){
        return (Rent) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                rent.getClass().getInterfaces(),this);
    }
    //方法增强
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        seehouse(rent.getClass().getName());
        Object result = method.invoke(rent, args);
        return result;
    }
    //增强的方法
    public void seehouse(String msg){
        System.out.println("代理对象:看房"+msg);
    }
}
```

test

```java
Host host = new Host();
ProxyInvh proxyInvh = new ProxyInvh();
proxyInvh.setRent(host);
Rent proxy = (Rent) proxyInvh.getProxy();
proxy.rent();
```

### 动态代理2

newProxyInstance返回的是一个object，所以说我们可以代理任何的真实角色，稍作修改即可

public class ProxyInvh implements InvocationHandler {
    //需要代理的对象，真实对象
    private Rent rent;

```java
public class ProxyInvh implements InvocationHandler {
    //需要代理的对象，真实对象
    private Object object;
    public void setRent(Rent rent) {
        this.rent = rent;
    }
	//得到代理角色
    public Object getProxy(){
        return  Proxy.newProxyInstance(this.getClass().getClassLoader(),
                object.getClass().getInterfaces(),this);
    }
    //方法增强
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        seehouse(object.getClass().getName());
        Object result = method.invoke(object, args);
        return result;
    }
    //增强的方法
    public void seehouse(String msg){
        System.out.println("代理对象:看房"+msg);
    }
}
    
```
## aop

引入aspactj织入包

<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.4</version>
</dependency>

切面       从切入点插入一个面，执行方法经过这个切面，对方法进行增强（前置通知，后置通知。。）

切入点  切入的那个点

通知 advice 

```xml
这是两个通知
<bean id="log" class="com.roily.log.Log"/>
<bean id="afterLog" class="com.roily.log.AfterLog"/>
<aop:config>
    <!--    切入点    -->
    <aop:pointcut id="pointcut" expression="execution(* com.roily.service.UserServiceImpl.*(..))"/>
    <!-- 执行环绕增强       -->
    <aop:advisor advice-ref="log" pointcut-ref="pointcut"/>
    <aop:advisor advice-ref="afterLog" pointcut-ref="pointcut"/>
</aop:config>
```

```xml
自动义通知
<bean id="defaultlog" class="com.roily.log.defaultlog"/>
<aop:config>
    <aop:aspect ref="defaultlog">
        <aop:pointcut id="point" expression="execution(* com.roily.service.UserServiceImpl.*(..))"/>
        <aop:before method="before" pointcut-ref="point"/>
        <aop:after method="after" pointcut-ref="point"/>
    </aop:aspect>
</aop:config>
```

