



# mybatis

简介：

1. MyBatis ==持久层框架==，它支持自定义 SQL、存储过程
2. 强大的==orm高级映射==。
3. MyBatis ==免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作==。
4. MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。
5. ==params映射，结果集映射==
6. 总而言之它是用于我们简化jdbc操作

为啥使用mybatis

1. 方便，简化操作（不用set    result/params）
2. 内存断电即失  数据必须持久化

特点：

- 简单易学：==没有任何第三方依赖==，
- ==灵活、低耦合==：sql写在xml里  实现代码和sql分离，易于维护，易于排错测试。
- 提供映射标签，支持对象与数据库的orm字段关系映射
- 提供对象关系映射标签，支持对象关系组建维护
- ==提供if  xml标签，支持编写动态sql==。

## 第一个mybatis

搭建环境->jar包->测试

- 创建数据库表

```sql
DROP TABLE `users` IF EXISTS;
CREATE TABLE `users`( 
    id INT (20) NOT NULL AUTO_INCREMENT ,
    `name` VARCHAR(20) NOT NULL,
    `pwd` VARCHAR(20) NOT NULL, 
    PRIMARY KEY(id) 
)ENGINE=INNODB DEFAULT CHARSET='utf8'; 
```

- 导入依赖mybatis  mysql  junit

```xml
<dependencies>
        <!-- https://mvnrepository.com/artifact/junit/junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
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
</dependencies>
```

- 设置静态资源过滤（maven项目使用maven插件打包，会出现资源过滤问题，也就是会忽略.xml文件）

```xml
<build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
</build>
```

- 编写实体类

```java
public class Users {
    private long id;
    private String name;
    private String pwd;
}
```

- 编写接口

```java
public interface UserMapper {
    List<Users> getUserList();
}
```

- 编写mapper

```xml
<?xml version="1.0" encoding="UTF-8" ?>
        <!DOCTYPE mapper
                PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
                "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.roily.dao.UserMapper">
    <select id="getUserList" resultType="users">
   		 select * from users
    </select>
</mapper>
```

- service接口
- service实现类
- 测试

```java
SqlSession sqlSession = mybatisUtil.getSqlSession();
userMapper mapper = sqlSession.getMapper(userMapper.class);
List<Users> userList =
    mapper.getUserList();
for (Users users : userList) {
    System.out.println(users);
}
sqlSession.close();
```


## mybatis核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">##指定环境
        <environment id="development">##环境id
            <transactionManager type="JDBC"/>##事务类型  manageer
            <dataSource type="POOLED">##池化技术
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useUnicode=true&amp;charactEncoding=utf8&amp;useSSL=true"/>
                <property name="username" value="root"/>
                <property name="password" value="123"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="com/roilt/dao/UserMapper.xml"/>
    </mappers>
</configuration>
```

## mybatisUtil工具类

> 了解文件读取方式，sqlSessionFactory初始化过程

```java
//sqlsessionfactory  ->sqlsession
public class mybatisUtil {
    private static SqlSessionFactory sqlSessionFactory = null;
    static{
        try {
            String resouce = "mybatis-config.xml";
            InputStream in = Resources.getResourceAsStream(resouce);
            sqlSessionFactory= new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }
}
```

> util工具类没有设置事务自动提交，这里需要处理事务
>

```java
    @Test
    public void test04(){
    SqlSession sqlSession = mybatisUtil.getSqlSession();
    userMapper mapper = sqlSession.getMapper(userMapper.class);
    int i = mapper.addUser(new Users(4, "123", "123"));
    //处理事务
    sqlSession.commit();
    System.out.println(i);
    sqlSession.close();
```

## 使用map传参

```xml
<!-- map   -->
<insert id="addUser2" parameterType="map" >
    insert into users(id,name,pwd) values(#{userid},#{username},#{userpwd})
</insert>
```

```java
@Test
public void test08(){
    SqlSession sqlSession = mybatisUtil.getSqlSession();
    userMapper mapper = sqlSession.getMapper(userMapper.class);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("userid",7);
    map.put("username","于延闯");
    map.put("userpwd","123");
    int i = mapper.addUser2(map);
    sqlSession.commit();
    System.out.println(i);
    sqlSession.close();
}
```

## 模糊查询

> 使用这种方式会好一些concat("%",#{name},"%")

> 了解两种占位符的区别  # / ￥

```xml
<select id="getUsers" parameterType="map" resultType="com.roilt.pojo.Users">
    select * from users where name like "%"#{name}"%"
</select>
```

```java
public void test09(){
    SqlSession sqlSession = mybatisUtil.getSqlSession();
    userMapper mapper = sqlSession.getMapper(userMapper.class);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("name","1");
    List<Users> users = mapper.getUsers(map);
    for (Users user : users) {
        System.out.println(user);
    }
    sqlSession.close();
}
```

## mybatis-config.xml 配置

> 有顺序的配置文件

```xml
configuration（配置）
[properties（属性）]
[settings（设置）]
[typeHandlers（类型处理器）]
[objectFactory（对象工厂）]
[plugins（插件）]
environments（环境配置）
  - environment（环境变量）
    - transactionManager（事务管理器）
    - dataSource（数据源）
[databaseIdProvider（数据库厂商标识）]
[mappers（映射器）]
```

### properties（属性）

> 引入外部文件的配置信息

> 了解properties的加载顺序，优先级
>

### typeAliases（类型处理器） 起别名

```xml
<!--    配置别名 -->
<typeAliases>
    <typeAlias type="com.roilt.pojo.Users" alias="User"/>
</typeAliases>
```

```xml
//扫描包给每一个实体类起别名  为  首字母小写
<typeAliases>
    <package name="com.roilt.pojo"/>
</typeAliases>
```

注解配置别名：

```
@Alias("u1")
```

> 1.<typeAliases>标签只有一个 且标签体内 要么都是typeAlias  要么都是package
> 2.优先使用注解配置  前提是使用package方式配置别名（扫描了整个包）
> 3.如果是 typeAlias配置的话注解配置不起效果

### settings（设置）

```bash
cacheEnabled	全局性地开启或关闭所有映射器配置文件中已配置的任何缓存。	
lazyLoadingEnabled	延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置 fetchType 属性来覆盖该项的开关状态。	
mapUnderscoreToCamelCase	是否开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。
logimpl 日志
```

### 映射器（mappers）

> 三种映射mapper文件的方式 
>
> 第一种好用一点
> 后面俩需要  mapper和接口类文件必须同名

- <mapper resource="com/roily/dao/UserMapper.xml"/>
- <mapper class="com.roily.dao.UserMapper"/>
- <package name="com.roily.dao.UserMapper"/>

# mybatis-config.xml生命周期

资源加载及内部运行机制

![image-20211021005341663](D:/File/Desktop/MakDown/Mybatis/mybatis1/maybatis1.assets/image-20211021005341663.png)

SqlSessionFactoryBuilder
这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。 因此 SqlSessionFactoryBuilder 实例的最佳作用域是方法作用域（也就是**局部方法变量**）。 你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，但最好还是不要一直保留着它，以保证所有的 XML 解析资源可以被释放给更重要的事情。

SqlSessionFactory
SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。因此 SqlSessionFactory 的最佳作用域是**应用作用域**。 有很多方法可以做到，最简单的就是使用**单例模式或者静态单例模式**。

SqlSession
每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例==不是线程安全的==，因此是不能被共享的，所以它的最佳的作用域是**请求或方法作用域**。 绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。 也绝不能将 SqlSession 实例的引用放在任何类型的托管作用域中，比如 Servlet 框架中的 HttpSession。 如果你现在正在使用一种 Web 框架，考虑将 SqlSession 放在一个和 HTTP 请求相似的作用域中。 换句话说，**每次收到 HTTP 请求，就可以打开一个 SqlSession，返回一个响应后，就关闭它。** 这个关闭操作很重要，为了确保每次都能执行关闭操作，你应该把这个**关闭操作放到 finally 块中**。 下面的示例就是一个确保 SqlSession 关闭的标准模式：

# 数据库字段和实体类属性名不一致问题

- 起别名

  ```txt
  pwd as password
  ```

- resultMap  结果集映射

  ```xml
  <resultMap id = "ResultMap" type = "com.roily.pojo.User">
  	<result column = "id" property="id"/>
      <result column = "name" property="name"/>
      <result column = "pwd" property="password"/>
  </resultMap>
  <select id = "getUsers" resultMap="ResultMap">
      select * from users
  </select>
  ```


# 日志工厂

> 在<settings>中配置   logImpl。STDOUT_LOGGING 没有第三方依赖，LOG4J 需要导包

- LOG4J 
- STDOUT_LOGGING 

STDOUT_LOGGING

标准日志输出

```xml
 <setting name="logImpl" value="STDOUT_LOGGING"/>
```

![image-20211021013127040](D:/File/Desktop/MakDown/Mybatis/mybatis1/maybatis1.assets/image-20211021013127040.png)

log4j

1. 输出：控制台、文件、GUI组件，套接口服务器、NT的事件记录器、UNIXSyslog守护进程等；
2. 可以控制每一条日志的输出格式；
3. 可以定义每一条日志信息的级别，细致地控制日志的生成过程。
4. 可以通过一个配置文件来进行配置，不需要修改应用的代码。

```xml
<setting name="logImpl" value="LOG4J"/>
```

```xml
#将等级为DEBUG的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
log4j.rootLogger=DEBUG,console,file
#控制台输出的相关设置
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold=DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=[%c]-%m%n
#文件输出的相关设置
log4j.appender.file = org.apache.log4j.RollingFileAppender
log4j.appender.file.File=./log/RoilyFish.log
log4j.appender.file.MaxFileSize=10mb
log4j.appender.file.Threshold=DEBUG
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=[%p][%d{yy-MM-dd}][%c]%m%n
#日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

![image-20211021014817883](D:/File/Desktop/MakDown/Mybatis/mybatis1/maybatis1.assets/image-20211021014817883.png)

log4j简单使用

![image-20211021015219162](D:/File/Desktop/MakDown/Mybatis/mybatis1/maybatis1.assets/image-20211021015219162.png)

导入jar包

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

```bash
日志级别
logger.debug("test02");
logger.info("test02");
logger.error("test02");
```

# maybatis分页

> 分页技术控制一次查询数量，减轻服务器压力，同时对于体验没有较大影响

```xml
<select id="getUserLimit" parameterType="map" resultType="u1">
	select * from users limit #{startIndex},#{pageSize}
</select>
```

> 代码实现分页

rowbounds

# mybatis参数自动映射

> 对于一些基本类型参数  int string 以及他的包装类型无需显示声明 parameterType，甚至是一些实体类User也无需显示声明parameterType。
>
> 但是对于一些复杂类型，比如说map就需要显示声明parameterType
>
> 现实中对于简单参数类型不用声明，但是对于一些应用类型最好还是显示声明一下。

# mybatis结果集映射

> 对于简单映射零配置，对于复杂映射说明关系（colum、property 列和属性对应）即可

> 当然会有很杂的映射关系，比如一个对象中存在另一个对象数组

> ***<u>自动结果集映射</u>***    
>
> ​	对于实体类：前提是属性名和列明对应
>
> ​	对于map：    属性名（map的name）和列明对应

> ***<u>手动结果集映射</u>***
>
> ​	对于实体类：字段名和属性名不是一一对应的需要设置resultMap

# 注解

@param  @alias  @select

> @param  为基本类型参数都加上
>
> @alias  别名那里看



> @select  注解实现sql  ==别用==
>
> 同事不注意会骂你，而且对于复杂一点点的sql就处理不了了（简单结果集映射）

```java
@Select("select * from users where id = #{id}")
public  Users getUserByid(int id);
@Insert("insert into users(name,pwd) values(#{name},#{pwd})")
public int addUser(Users user);
@Update("update users set name=#{name}, pwd=#{pwd} where id=#{id}")
public int updateUser(Users user);
```

