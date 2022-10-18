# 分页

***分页的好处***

1. 分页加载数据可以减轻服务器压力，显示也更友好
2. 

***Limit分页***

select * from user  limit  5,5     （参数说明：第一个：startindex  第二个 ：pagesize）

***Mybatis实现***

![image-20211021204810274](D:/File/Desktop/MakDown/Mybatis/mybatis2/mybatis2.assets/image-20211021204810274.png)

**RowBounds实现**   好鸡肋啊！！！。。。。

```xml
RowBounds rw = new RowBounds(1,2);   index   limit
sqlsession.selsetList("com.roily.dao.UserMapper.getUserList",nunll,rw);
```

***插件实现***

Mybatis-PageHelper

***总结***

底层都是limit实现。

# 使用注解开发

## 面向接口编程

接口可以实现项目整体约束，以及解耦，各个模块各司其职。

接口体现了一个项目的整体框架。

## 注解实现sql

![image-20211021212017678](D:/File/Desktop/MakDown/Mybatis/mybatis2/mybatis2.assets/image-20211021212017678.png)

```xml
配置文件无了只能通过类加载。 
<mapper class="com.roily.dao.UserMapper"/>
```



```txt
*使用反射机制实现：通过.class得到具体类，再去得到其中的具体方法
*这样对于结果集映射的处理不过灵活，所以其应用于简单sql
```

***增删改查：***

注解参数：

@param

```xml
基本类型都加上@param
引用类型不用  map  obj
建议为每一个基本类型都加上@param

#{}就是预编译  有效防止sql注入
```

```xml
<mapper class="com.roily.dao.UserMapper"/>
```

```java
@Select("select * from users where id = #{id}")
public  Users getUserByid(int id);
@Insert("insert into users(name,pwd) values(#{name},#{pwd})")
public int addUser(Users user);
@Update("update users set name=#{name}, pwd=#{pwd} where id=#{id}")
public int updateUser(Users user);
```

# lombok

```txt
Lombok config system
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
```

# 多对一

对于一个班一堂课而言

多个学生对应一个老师

一个老师对应多个学生

1. 按照查询嵌套查询

```xml
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
```

2. 按照结果嵌套查询

```xml
<select id="getStudentList2" resultMap="studentmap2">
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
```

# 一对多

```
<mapper namespace="com.roily.dao.TeacherMapper">
    <select id="getTeacherList" resultType="teacher">
        select * from mybatis.teacher
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

    <select id="getTeacher2" resultMap="teachermap2">
        select * from teacher where id = #{id}
    </select>
    <resultMap id="teachermap2" type="teacher">
        <result property="id" column="id"/>
        <result property="name" column="name"/>
        <collection property="students" column="id" javaType="ArrayList" ofType="student" select="getStudent">
        </collection>
    </resultMap>
    <select id="getStudent" resultType="student">
        select * from student where tid = #{id}
    </select>
```

# 动态sql

```xml
<where></where>标签：至少有一个匹配时才生效，且会自动去除and、or
<if test="id!=null">sql...</if>标签：判断
<choose><when test =""></when></choose> 只选一个筛选条件  otherwize
<set></set>  前置set语句  并去除不必要的逗号
```

## if

```
public List<Teacher> getTeacherIf(Map<String,Object> map);
```

```xml
<select id="getTeacherIf" parameterType="map"  resultType="teacher">
    select * from teacher where 1=1
    <if test="id!=null">
        and id = #{id}
    </if>
    <if test="name!=null">
        and name = #{name}
    </if>
</select>
=======================================
<select id="getTeacherIf" parameterType="map"  resultType="teacher">
    select * from teacher 
    <where>
    <if test="id!=null">
        and id = #{id}
    </if>
    <if test="name!=null">
        and name = #{name}
    </if>
    </where>    
</select>
```

## choose

```xml
<select id="getTeacherchoose" parameterType="map"  resultType="teacher">
    select * from teacher
    <where>
        <choose>
            <when test="id!=null">
                and id = #{id}
            </when>
            <when test="name!=null">
                and name = #{name}
            </when>
            <otherwise>

            </otherwise>
        </choose>
    </where>
</select>
```

## set

```
<update id="teacherUpdate" parameterType="map" >
    update teacher
    <set>
        <if test="name!=null">
            name = #{name},
        </if>
    </set>
    where id = #{id}
</update>
```

## trim

定制sql  前缀后缀

## sql片段

```sql
提取公用的片段
```

```
<update id="teacherUpdate2" parameterType="map" >
    update teacher
    <set>
        <include refid="if-name-id"/>
    </set>
    where id = #{id}
</update>
<sql id="if-name-id">
    <if test="name!=null">
        name = #{name}
    </if>
</sql>
```

## foreach

```xml
<select id="getTeacheIn" parameterType="map" resultType="teacher">
    select * from teacher
    <where>
        1=1 and id in
        <foreach collection="ids" item="id" open="(" separator="," close=")">
            #{id}
        </foreach>
    </where>
</select>
```

```java
@Test
public void test08() {
    SqlSession sqlSession = mybatisUtil.getSqlSession();
    TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
    Map<String, Object> map = new HashMap<String, Object>();
    ArrayList<Integer> ids = new ArrayList<Integer>();
    ids.add(1);
    ids.add(2);
    ids.add(3);
    map.put("ids",ids);
    List<Teacher> teacheIn = mapper.getTeacheIn(map);
    for (Teacher teacher : teacheIn) {
        System.out.println(teacher);
    }
    sqlSession.close();
}
```

# 万能的map！！！！！！！！！！！！！！！！！！！！！！！！！！！！

# 总结

```txt
动态sql也是sql
先写完整sql（整理逻辑），再写mybatis的sql
并不是很方便，但是很标准，易于排错
```

# 缓存

- 在内存中的临时数据

  

场景：频繁读取，且不改变的数据

好处：减少io操作，减少和数据库的交道

## mybatis缓存

一级缓存，二级缓存

sqlsession级别：一级  在getsession和close之间有效

namespace缓存：二级 基于一个mapper

***一级缓存***

```java
@Test
public void test01(){
    SqlSession sqlSession = mybatisUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    List<Users> userList = mapper.getUserList();
    for (Users users : userList) {
        System.out.println(users);
    }
    System.out.println("======================");
    List<Users> userList2 = mapper.getUserList();
    for (Users users : userList2) {
        System.out.println(users);
    }
    System.out.println(userList==userList2);
    sqlSession.close();
}
```

![image-20211023181622149](D:/File/Desktop/MakDown/Mybatis/mybatis2/mybatis2.assets/image-20211023181622149.png)

```xml
查询同一目标：不执行sql 地址都一样，所以说他是从内存中拿的
查询不同的目标当然不是从内存中拿的

缓存失效：
增删改，在sqlsession中进行增删改操作一定会刷新缓存，之前的缓存没了
```

***二级缓存***

开启

```xml
<settings>
    <setting name="logImpl" value="STDOUT_LOGGING"/>
    <setting name="cacheEnabled" value="true"/>
</settings>
```

```xml
<!--  开启二级缓存 -->
    <cache eviction="FIFO"
           flushInterval="60000"
           size="512"
           readOnly="true"
    />
```

```java
@Test
public void test03(){
    SqlSession sqlSession = mybatisUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    Users userbyid = mapper.getUserbyid(1);
    System.out.println(userbyid);
    System.out.println("================");
    SqlSession sqlSession2 = mybatisUtil.getSqlSession();
    UserMapper mapper1 = sqlSession2.getMapper(UserMapper.class);
    Users userbyid1 = mapper1.getUserbyid(1);
    System.out.println(userbyid1);
    System.out.println(userbyid==userbyid1);
    sqlSession.close();
    sqlSession2.close();
}
```

![image-20211023183435085](D:\File\Desktop\MakDown\Mybatis\mybatis2\mybatis2.assets/image-20211023183435085.png)

```
@Test
public void test04(){
    SqlSession sqlSession = mybatisUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    Users userbyid = mapper.getUserbyid(1);
    System.out.println(userbyid);
    sqlSession.close();
    System.out.println("================");
    SqlSession sqlSession2 = mybatisUtil.getSqlSession();
    UserMapper mapper1 = sqlSession2.getMapper(UserMapper.class);
    Users userbyid1 = mapper1.getUserbyid(1);
    System.out.println(userbyid1);
    System.out.println(userbyid==userbyid1);
    sqlSession2.close();
}
```

![image-20211023183536850](D:/File/Desktop/MakDown/Mybatis/mybatis2/mybatis2.assets/image-20211023183536850.png)

```txt
同一个mapper开启二级缓存
如果sqlsession未关闭未失效的情况下，不会提升所缓存的数据作用域
当selsession关闭或失效的情况下提升作用域，在另一个sqlsession中查到的数据是从缓存中拿的
```

![image-20211023184707048](D:/File/Desktop/MakDown/Mybatis/mybatis2/mybatis2.assets/image-20211023184707048.png)

***自定义缓存***

repo.spring.io/release/org/springframework/spring/

### 缓存生命周期

- 一级缓存

  - ```bash
    一级缓存默认开启
    和sqlsession生命周期一样（sqlsession没有失效就回去内存中拿）
    ```

  - 失效（刷新缓存）：

    ```bash
    有事务操作：增删改
    清理策略使刷新缓存
    ```

- 二级缓存

  - ```bash
    在mapper文件中设置  <cache/>
    和mapper生命周期一样，也就是同一个mapper里面的方法调用触发缓存
    ```

  - ```bash
    二级缓存失效会先放入一级缓存，最后再清理
    ```

## 清理策略



![image-20220108222134369](D:\File\Desktop\MakDown\Mybatis\mybatis2\mybatis2.assets\image-20220108222134369.png)

## 了解一下ehcache