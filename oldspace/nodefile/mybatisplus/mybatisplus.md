# 简介

```bash
（简称 MP）是一个MyBatis的增强工具。
 在MyBatis的基础上只做增强不做改变。
```

# 使用

## 依赖

```xml
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.37</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.0.5</version>
        </dependency>
```

## 数据库

```sql
CREATE TABLE user
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);
```

## 实体类

> 这边的id类型为Long，之前设置long死活不生成id（生成的id很大需要long）
>
> 原因：long是一个数据类型，他有默认值0，自动生成id的前提条件是id为空。而Long是long的包装类型，是对象类型默认为空，所以这里使用Long。

```java
@Lombok
public class User {
    @TableId
    private Long id;
    private String name;
    private long age;
    private String email;
}
```

## 编写mapper接口

> 在主启动类上加@ScannerMapper和这里加@mapper都可以扫描到

```java
@Repository
//@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```

## 测试

```java
@Test
void contextLoads() {
    List<User> users = userMapper.selectList(null);
    users.forEach(System.out::println);
}
```

```java
@Test
void contextLoads3() {
    User user = new User();
    user.setAge(23);
    user.setEmail("1056819225@qq.com");
    user.setName("于延闯");
    int insert = userMapper.insert(user);//自动生成id
    System.out.println(insert);//change  rows
    System.out.println(user);//回显id
}
```

# 主键生成策略snowflake（雪花算法）

> snowflake是Twitter开源的分布式ID生成算法，结果是一个long型的ID。其核心思想是：使用==41bit作为毫秒数==，==10bit作为机器的ID（5个bit是数据中心，5个bit的机器ID==），==12bit作为毫秒内的流水号==（意味着每个节点在每毫秒可以产生 4096 个 ID），最后还有一个符号位，永远是0。

```java
//数据库ID自增
AUTO(0),
//该类型为未设置主键类型
NONE(1),
//用户输入ID
//该类型可以通过自己注册自动填充插件进行填充
INPUT(2),
//以下3种类型、只有当插入对象ID 为空，才自动填充
//全局唯一ID (idWorker)
ID_WORKER(3),
//全局唯一ID (UUID)
UUID(4),
//字符串全局唯一ID (idWorker 的字符串表示)
ID_WORKER_STR(5);
```

# update

```java
	User user = new User();
    user.setId(6L);
    user.setAge(23);
    user.setName("李自成");
    userMapper.updateById(user);
```



![image-20220103164952310](D:..\\mybatisplus\mybatisplus.assets\image-20220103164952310.png)



![image-20220103165025625](D:..\\mybatisplus\mybatisplus.assets\image-20220103165025625.png)

> 根据条件自动拼接sql（不用 if test进行判断）

## sql自动填充

> 关于数据库表的操作，插入更新字段往往希望其自动填充。

- 方式一（数据库级别，对字段设置）==不推荐==

  ![image-20220103170721183](D:..\\mybatisplus\mybatisplus.assets\image-20220103170721183.png)

- 方式二（代码级别）==推荐==

  -  表字段填充
  - 给字段添加@TableField
  - 查看FieldFill枚举类型==DEFAULT,INSERT,UPDATE,INSERT_UPDATE==

  ```java
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  ```

  - 编写handler

    ```java
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
    ```

# 多线程下线程插队问题解决

## 乐观锁

> 认为程序不会出现问题，当出现问题时再做操作（出现问题的判定：version字段被修改）

实现乐观锁：

- 添加表字段：version
  
  - 设置默认值 1
- 对象属性：version并添加注解@version
  
- 也可以设置tablefield fill
  
- 编写config类（注入一个bean）

  - ```java
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
    ```

测试：

一般情况下：没有任何问题（version字段加一）

```java
User user = userMapper.selectById(10L);
System.out.println(user);
user.setName("乐观锁1");
userMapper.updateById(user);
```

跟新操作之前数据被跟新：

> 会阻止userMapper.updateById(user);提交

```java
User user = userMapper.selectById(9L);
System.out.println(user);
user.setName("乐观锁1");
//如果说中间有更新操作  乐观锁起作用 即回滚更新
User user2 = userMapper.selectById(9L);
System.out.println(user2);
user2.setName("乐观锁2");
userMapper.updateById(user2);
userMapper.updateById(user);
```

![image-20220103203000999](D:..\\mybatisplus\mybatisplus.assets\image-20220103203000999.png)

# 查询

```java
@Test
void query01() {
    List<User> users = userMapper.selectList(null);
    users.forEach(System.out::println);
}
@Test
void query02() {
    User user = userMapper.selectById(1L);
    System.out.println(user);
}
@Test
void query03() {
    List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
    users.forEach(System.out::println);
}
@Test
void query04() {
    Map<String, Object> map = new HashMap<>();
    map.put("name","李自成2");
    map.put("age",23);
    List<User> users = userMapper.selectByMap(map);
    users.forEach(System.out::println);
}
```

## 分页查询

```java
@Test
void query05() {
    Page<User> objectPage = new Page<User>(1, 5);
    IPage<User> userIPage = userMapper.selectPage(objectPage, null);
    userIPage.getRecords().forEach(System.out::println);
}
```

![image-20220103205328084](D:..\\mybatisplus\mybatisplus.assets\image-20220103205328084.png)

> ```bash
> userIPage.getTotal()
> ```
>
> 分页查询之前会查询count

# 删除

```java
@Test
void delete01() {
    int row = userMapper.deleteById(1L);
    System.out.println(row);
}
@Test
void delete02() {
    int row = userMapper.deleteBatchIds(Arrays.asList(1,2,3));
    System.out.println(row);
}
@Test
void delete03() {
    Map<String, Object> map = new HashMap<>();
    map.put("name","李自成2");
    map.put("age",23);
    int row = userMapper.deleteByMap(map);
    System.out.println(row);
}
```

## 逻辑删除

> 用一个字段使他失效（管理员可以查看删除信息）

```yml
mybatis-plus:
  global-config:
    db-config:
#      logic-delete-field: flag # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
```

```java
//逻辑删除字段
@TableLogic
private Integer deleted;
```

```java
//逻辑删除组件
@Bean
public ISqlInjector sqlInjector() {
    return new LogicSqlInjector();
}
```

测试

```java
@Test
void delete01() {
    int row = userMapper.deleteById(14L);
    System.out.println(row);
}
```

> 执行的是update操作

> ![](D:..\\mybatisplus\mybatisplus.assets\image-20220103213419971.png)

## 物理删除

> 从磁盘中移除

# 性能监测插件

> 在yaml中激活环境

```java
@Bean
@Profile({"dev", "test"})// 设置 dev test 环境开启
public PerformanceInterceptor performanceInterceptor() {
    PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
    performanceInterceptor.setFormat(true);//格式化语句
    //performanceInterceptor.setMaxTime(5);//执行时间超过多少秒会抛出异常
    return performanceInterceptor;
}
```

# 条件查询器

用于生成 sql 的 where 条件, entity 属性也用于生成 sql 的 where 条件
注意: entity 生成的 where 条件与 使用各个 api 生成的 where 条件**没有任何关联行为**

```java
QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
userQueryWrapper
        .isNotNull("name")
        .isNotNull("age")
        .ge("age",20);
List<User> users = userMapper.selectList(userQueryWrapper);
users.forEach(System.out::println);
```

![image-20220103220912221](D:..\\mybatisplus\mybatisplus.assets\image-20220103220912221.png)



> eq  ne（等于  =    不等于 <> ）

```java
@Test
void queryWrapper02() {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.eq("name","乐观锁2");
    System.out.println(userMapper.selectOne(userQueryWrapper));
}
```

> gt(>)  ge(>=) lt(<) le()<=

> between   between  a and b
>
> notBetween 	not between   a and b

```java
@Test
void queryWrapper03() {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.between("age",20,30);
    System.out.println(userMapper.selectCount(userQueryWrapper));
}
```

> like  notlike  likelift  likeright 

```java
@Test
void queryWrapper04() {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.like("name","李");
    userMapper.selectList(userQueryWrapper).forEach(System.out::println);
}
```

> isnull  isnotnunll

```java
@Test
void queryWrapper05() {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.isNull("name");
    userMapper.selectList(userQueryWrapper).forEach(System.out::println);
}
```

> in   notIn

```java
@Test
void queryWrapper06() {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.in("age",Arrays.asList(23,24,25));
    //userMapper.selectBatchIds(Arrays.asList(1,2,3));
    userMapper.selectList(userQueryWrapper).forEach(System.out::println);
}
```

> insql  noinsql（子查询）
>
> - 字段 IN ( sql语句 )
> - 例: `inSql("id", "select id from table where id < 3")`--->`id in (select id from table where id < 3)`

  ***id IN (select id from user)*** 

```java
@Test
void queryWrapper07() {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.inSql("id","select id from user");
    userMapper.selectList(userQueryWrapper).forEach(System.out::println);
}
```

> orderByAsc    orderByDesc  orderBy

```java
@Test
void queryWrapper08() {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    //升序
    userQueryWrapper.orderByAsc("id");
    userMapper.selectList(userQueryWrapper).forEach(System.out::println);
}


orderBy(true,true,"id","age")
```

**orderBy参数：（是否开启）boolean condition,（是否升序） boolean isAsc,（参数列表，如果有中文另作处理） R... columns**

> or

```java
@Test
void queryWrapper011() {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.like("name","李").or().gt("age",0);
    userMapper.selectList(userQueryWrapper).forEach(System.out::println);
}
```

> or嵌套（i代表queryWrapper<User>）

```java
void queryWrapper012() {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.gt("id",0).or(i->i.gt("age",0).like("name","李"));
    userMapper.selectList(userQueryWrapper).forEach(System.out::println);
}
```

![image-20220103230008633](D:..\mybatisplus\mybatisplus.assets\image-20220103230008633.png)

# 代码生成器

