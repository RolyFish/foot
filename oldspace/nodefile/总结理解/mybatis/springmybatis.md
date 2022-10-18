## pom

依赖，mybatis，mysql，mybatis-spring，jdbc，lombok

```xml
 		<dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.7</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.20</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.mybatis.caches/mybatis-ehcache -->
        <dependency>
            <groupId>org.mybatis.caches</groupId>
            <artifactId>mybatis-ehcache</artifactId>
            <version>1.2.1</version>
        </dependency>

        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.6</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.3.12</version>
        </dependency>
```

## 配置-mybatis

***spring整合mybatis只需留settings，typeraliases***

```xml
<!--  映入配置文件  -->
<properties resource="db.properties">
    <!--  property 配置自定义属性  优先使用外部web配置文件  -->
</properties>
<settings>
    <setting name="logImpl" value="STDOUT_LOGGING"/>
    <setting name="cacheEnabled" value="true"/>
</settings>
<typeAliases>
    <package name="com.roily.pojo"/>
    <!--        <typeAlias type="com.roily.pojo.Users" alias="user"/>-->
</typeAliases>
```

## 配置springdao（配置一些类）

***外部的数据库配置文件，也可以在这里引入，property-placeholder***

```xml
<!--    <context:property-placeholder location="classpath:db.properties"/>-->

<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url"
              value="jdbc:mysql://localhost:3306/mybatis?useUnicode=true&amp;charactEncoding=utf8&amp;useSSL=true"/>
    <property name="username" value="root"/>
    <property name="password" value="123"/>
</bean>

<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
    <property name="mapperLocations" value="classpath:com/roily/dao/UserMapper.xml"/>
</bean>

<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
    <constructor-arg name="sqlSessionFactory" ref="sqlSessionFactory"/>
</bean>


===事务===
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <tx:method name="add" propagation="REQUIRED"/>
        <tx:method name="delete" propagation="REQUIRED"/>
        <tx:method name="update" propagation="REQUIRED"/>
    </tx:attributes>
</tx:advice>

<aop:config>
    <aop:pointcut id="txPoint" expression="execution(* com.roily.dao.*.*(..))"/>
    <aop:advisor advice-ref="txAdvice" pointcut-ref="txPoint"/>
</aop:config>
```

## 配置：spring核心配置

1、注解支持

2、spring自动扫描

3、import将配置文件整合

```xml
<context:annotation-config/>
<context:component-scan base-package="com.roily"/>
<import resource="classpath:spring-dao.xml"/>
```

## 读取spring配置文件

```java
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
```

## SqlSessionDaoSupport

```java
//属性
private SqlSessionTemplate sqlSessionTemplate;
//SqlSessionFactory的set方法
public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
  if (this.sqlSessionTemplate == null || sqlSessionFactory != this.sqlSessionTemplate.getSqlSessionFactory()) {
    this.sqlSessionTemplate = createSqlSessionTemplate(sqlSessionFactory);
  }
}
//得到selSession
public SqlSession getSqlSession() {
    return this.sqlSessionTemplate;
}
```

```xml
SqlSessionDaoSupport有sqlSessionFactory的set方法和getSqlSession（）方法。
也就是说我们如果写一个mapper的实现类并且继承他，在spring中注入sqlsessionFactory的话就可以拿到sqlsession也就是sqlSessionTemplate。也就可以getmapper 也就可以执行sql。
```

## sqlSessionFactoryBean

```xml
首先这个类没有构造方法（有参），所以我们走无参构造set注入。注入dataSource，configLocation，mapperLocation。
它实现了FactoryBean<SqlSessionFActory>这么一个泛型的接口，需要重写他的一个方法叫getObject，返回的是一个sqlsessionFactory，也就是说最后得到的，被ioc容器管理的bean类型是sqlsessionFactory。
但是sqlSessionFactory的配置是怎么来的呢？看一个叫buildSqlsessionFactory的这么一个方法，这个方法的大概内容就是有一个xmlConfigBuilder这么一个对象，通过他将一些引入的配置整合，最后交予sqlsessionFActoryBuilder.build(xmlConfig),返回一个sqlsessionFactory对象，这样就很清晰了。我们一般的sqlsession也是最终调用build（xmlconfig）来实例化的。
但是这个buildSqlsessionFactory这个方法啥时候执行的呢？这也没有什么自动配置的注解啊！！！不急这里还有一个叫afterPropertiesSet的方法，顾名思义这就是在set注入后执行的方法啊！！
```

![image-20211215151645601](springmybatis.assets\image-20211215151645601.png)

注入配置没啥好说的！！！

------

![image-20211215151727583](springmybatis.assets\image-20211215151727583.png)

***这里我们做一个测试！！！看看实现FacoryBean<T>这个泛型接口，再交予spring管理，到底是啥类型。***

环境：一个User实体类，一个FactoryBean的实现类FactoryBeanTest。

```java
public class User {
  private long id;
  private String name;
  private String pwd;
}
```

```java
public class FactoryBeanImpl implements FactoryBean<User> {
    public User getObject() throws Exception {
        return new User("yyc","123");
    }
    public Class<?> getObjectType() {
        return User.class;
    }

}
```

在spring中注册！！

```xml
<bean id="test" class="com.roily.test.FactoryBeanImpl"/>
```

测试：

```java
public void test951(){
    ApplicationContext context 
            = new ClassPathXmlApplicationContext("applicationContext.xml");
    User test = context.getBean("test",User.class);
    System.out.println(test);
}
```

结果：

![image-20211215153956589](springmybatis.assets\image-20211215153956589.png)

这时候就有问题了，我这边是强转的，不严谨，ok我们再测一下

```java
public void test952(){
    ApplicationContext context
            = new ClassPathXmlApplicationContext("applicationContext.xml");
    FactoryBeanImpl test = context.getBean("test",FactoryBeanImpl.class);
    System.out.println(test);
}
```

结果：

![image-20211215154204394](springmybatis.assets\image-20211215154204394.png)

```xml
答案很明显了，说明继承自facoryBean<T>泛型接口的类其返回类型就是该泛型。
```

------

![image-20211215151842988](springmybatis.assets\image-20211215151842988.png)

![image-20211215151922877](springmybatis.assets\image-20211215151922877.png)



------

```java
return this.sqlSessionFactoryBuilder.build(targetConfiguration);
```

![image-20211215152056315](springmybatis.assets\image-20211215152056315.png)

------

afterProperties回调

![image-20211215152154750](springmybatis.assets\image-20211215152154750.png)



## sqlsessionTempleate

```xml
首先这个类没有set方法，也就是说我们得构造器注入，用name（字符串匹配的方法）方式，给一个sqlsessionFactory
他继承自sqlsession,也就是说他和一些sqlsession的实现类一样（defaultSqlSession）可以去进行获取mapper等等操作。
```

