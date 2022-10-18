# mybatis

- **依赖  pom.xml**
- **数据库的配置文件**
- **配置  config.xml**
- **mapper接口及文件**



## 引入依赖包

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.47</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.7</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.20</version>
</dependency>
```

## 配置静态资源过滤

即放行resource和java下的所有配置资源  xml  prop

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

## 数据库配置文件

```xml
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?useUnicode=true&charactEncoding=utf8&useSSL=true
username=root
passwd=123
```

## mybatis配置文件

```xml
<configuration>
   
    <!-- 引入外部配置文件 -->
    <properties resource="db.properties">
        <!--  property 配置自定义属性-->
        <!--  加载顺序：Datasource参数 properties内部自定义配置 外部引入properties配置-->
        <!--  引用顺序：覆盖  -->
        <property name="insideDriver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="insideUrl" value="jdbc:mysql://localhost:3306/school?allowPublicKeyRetrieval=true&amp;useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf8&amp;serverTimezone=CST"/>
        <property name="insideUsername" value="root"/>
        <property name="insidePassword" value="123"/>
    </properties>
    
    <!--    配置别名 -->
    <typeAliases>
        <!-- 包映射  -->
        <package name="com.roily.pojo"/>
        <!-- 绝对映射  实体类标注@Alias()  用的不多-->
        <typeAlias type="com.roily.pojo.Users" alias="users"/>
    </typeAliases>
    
    <settings>
        //显示开启一级缓存  mybatis默认一级缓存 且不会关闭
        //一级缓存 等通于sqlsession 从open到close
        	一级缓存失效情况：事务操作
        					及用户调用clearcache  和 close方法
        	二级缓存：namespace 配置在mapper文件中 二级缓存失效会默认降级放入一级缓存中
        <setting name="cacheEnabled" value="true"/> 
        <setting name="logImpl" value="STDOUT_LOGGING"/>
        <setting name="logImpl" value="LOG4J"/>
        
    </settings>
    
    
	<!--mybatis运行环境配置 可配置多个 需声明一个 即default-->
    <!-- 数据源  声明是否使用连接池 属性引入方式：1.字符串 2.$符号配合property -->
    <environments default="test1">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useUnicode=true&amp;charactEncoding=utf8&amp;useSSL=true"/>
                <property name="username" value="root"/>
                <property name="password" value="123"/>
            </dataSource>
        </environment>
        <environment id="test1">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${passwd}"/>
            </dataSource>
        </environment>
    </environments>
    <!--  mapper的映射  此框架强大在于映射  而未有自动注入 配置 及 属性   -->
    <!--  -->
    <mappers>
        <mapper resource="com/roily/dao/UserMapper.xml"/>
    </mappers>
</configuration>
```

## mapper

resoultmap结果集映射，sql片段

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.roily.dao.bill.BillerMapper">
    <select id="getBilllist" resultType="smbmsBill">
        select * from smbms_bill
    </select>
    <sql id="sqlid">
        ```` 
    </sql>
</mapper>
~~~



# mybatisutil

```tex
静态代码块在初次实例化该类的时候加载，接下来就不加载了，很合理。
```

```java
public class mybatisUtil {
    private static SqlSessionFactory sqlSessionFactory = null;
    static{
        try {
            String resouce = "mybatis-config.xml";
            //resource ibatis下的一个静态类 其下面有getResourceAsStream的静态方法
            //这是其中的一个重载
            //作用：以stream的形式返回一个资源，在classpath下
            InputStream in = Resources.getResourceAsStream(resouce);
            
            //四个重载 我们只给了一个输入流 properties说明此处是可以携带参数的property
            //public SqlSessionFactory build(Reader reader)
            //public SqlSessionFactory build(Reader reader, String environment) 
            //public SqlSessionFactory build(Reader reader, Properties properties){
            //public SqlSessionFactory build(Reader reader, String environment, Properties properties)
            sqlSessionFactory= new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SqlSession getSqlSession(){
        //此处可以携带是否自动提交的参数autocommit
        return sqlSessionFactory.openSession();
    }
}
```

```xml
这里得到的sqlsession其实是他的一个默认实现类defautsqlsession
getMapper调用crud
返回结果集
close
```

小tip：

```tex
mybatis的配置文件是有顺序的，严格准守dtd语法规则
```

# 批量插入

```xml
<!-- insert sql: 绑定insertManyUseMap,批量插入 -->
<insert id="insertManyUseList">
    insert into users values
    /* foreach 标签：
    -【item】属性： 表示集合中每一个元素进行迭代时的别名
    - 【collection】属性： 参数类型是一个List的时候，collection属性值为list
    - 【separator】属性： 表示在每次进行迭代之间以什么符号作为分隔符。
    */
    <foreach item="user" collection="list" separator=",">
        (#{user.id},#{user.name},#{user.pass},#{user.email})
    </foreach>
</insert>

int insertManyUseList(List<Users> users);
```

# mybatis注解



```xml
@Alias("aaa")//只有在包级别的别名设置这个注解才生效，类级别会被覆盖。
一、<typeAlias type="com.camemax.pojo.Users" alias="user"/>
二、配合<typeAlias package=""/>
```

@SQl

```java
/**
*  使用注解完成CRUD操作
*/
@Select("select * from users where id = #{id}")
Users getUserInfoById(@Param("id") int id);
```

# 一对多多对一resultmap

结果集映射

```xml

<select id="queryStudent" resultMap="studentmap2">
    select s.id, s.name, t.id tid, t.name tname from student s ,teacher t where s.tid = t.id
</select>
<resultMap id="studentmap2" type="Student">
    <result property="id" column="id"/>
    <result property="name" column="name"/>
    <association property="teacher" javaType="Teacher">
        <result property="id" column="tid"/>
        <result property="name" column="tname"/>
    </association>
</resultMap>


<select id="getStudentList" resultMap="studentmap">
    select * from student
</select>
<resultMap id="studentmap" type="Student">
    <result property="id" column="id" />
    <result property="name" column="name" />
    <!-- 复杂的属性需要单独处理
        对象：assocation
        集合： collection
        -->
    <association property="teacher" column="tid" javaType="Teacher" select="getTeacher">
    </association>
</resultMap>
<select id="getTeacher" resultType="teacher">
    select * from teacher where id = #{id}
</select>



<select id="getTeacher" resultMap="teachermap">
    select t.id tid, t.name tname, s.id sid, s.name sname, s.tid stid
    from teacher t, student s
    where s.tid = t.id
    and t.id = #{id}
</select>
<resultMap id="teachermap" type="teacher">
    <result property="id" column="tid"/>
    <result property="name" column="tname"/>
    <collection property="students" ofType="Student">
        <result property="id" column="sid"/>
        <result property="name" column="sname"/>
    </collection>
</resultMap>
```

# 读取mybatis配置文件

以一个流对象的形式返回一个在classpath下的资源

![image-20211212225603217](D:\File\Desktop\总结理解\mybatis\image-20211212225603217.png)

在build方法上有一些重载方法，也就是说这边是可以携带配置和环境的，当然会被外部的配置覆盖

![image-20211212230611325](D:\File\Desktop\总结理解\mybatis\image-20211212230611325.png)



# 总结：

```xml
mybatis的强大在于他的orm自动映射，包括参数和结果集的自动映射，避免手动设参数和获取结果集。
需要了解：依赖，配置，使用
依赖： pom文件需设置静态资源过滤
配置：
	properties引入外部db配置文件
	setting设置 日志，别名等
	typealias 配置别名
	envirment 环境配置，数据源
	mappers mapper文件引入
使用：
	Resources.getResourceAsStream（path）获得输入流in
		path：直接写resources目录下的配置文件名。
	sqlsessionfactorybuilder.build(in)  获得sqlsessionfactory
		build方法有重载，参数不只只有in，还有environment，properties
		但最终都会以xmlconfigBuilder解析
	openSession获得sqlSession
		也有重载一般设置个AutoCommint
```



```java
public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
  try {
    XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
    return build(parser.parse());
  } catch (Exception e) {
    throw ExceptionFactory.wrapException("Error building SqlSession.", e);
  } finally {
    ErrorContext.instance().reset();
    try {
      inputStream.close();
    } catch (IOException e) {
      // Intentionally ignore. Prefer previous error.
    }
  }
}

public SqlSessionFactory build(Configuration config) {
  return new DefaultSqlSessionFactory(config);
}
```

