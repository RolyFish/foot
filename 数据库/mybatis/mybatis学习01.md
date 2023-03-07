本文是`Mybatis`的初步了解。

> `Mybatis`是为数不多的跟着官网学习，就能学明白的，建议多看看官方文档。
>
> 传送门:[Mybatis官网](https://mybatis.org/mybatis-3/zh/index.html)
>
> [<上一章]()   ||   [下一章>]()

#### 什么是`Mybatis`

> 官网简介：

​		MyBatis 是一款优秀的==持久层框架==，它支持==自定义 SQL==、存储过程以及==高级映射==。MyBatis ==免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作==。MyBatis 可以通过简单的 ==XML== 或==注解==来配置和==映射==原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

提取关键字：

- 持久层框架
- `sql`拼接、动态`sql`、自定义`sql`
- 自动映射(包括1、简单数据类型自动映射 2、实体类映射 3、结果集映射)
- `Xml`配置

> `mybatis`最牛逼的地方就是它的`ORM(Object RelationShip Mapping)`自动关系映射(半自动的，复杂对象需要我们手动配置映射关系)。
>
> 使用`Xml`配置`sql`语句，参数，结果集。使得`java`代码和`sql`语句分离，便于错误定位，和维护。
>
> `Xml`里通过标签来定义`sql`语句，结构清晰。

#### 关于持久化

​		将内存中的数据写入磁盘中的过程成为持久化。我们也可以自己通过`io`将数据写入本地磁盘，只不过数据库包含数据库引擎以及数据结构，无论是便利性还是性能都肯定优于我们自己写的`io`。



#### `mybatis`自定义`sql`

​		通过`mybatis`处理过的`sql`和我们在客户端执行的`sql`没有两样。

​		一般来说我们需要传入参数，根据参数来对我们的`sql`进行动态拼接。

​		`mybatis`提供的标签就可以控制`sql`的动态拼接，比如：<if>标签，<foreach>标签，甚至是自定义标签<trim>。

#### 自动映射

> 看一下这个类`org.apache.ibatis.type.TypeAliasRegistry`
>
> 他会将所有的关系映射都放入`TypeAliasRegistry`这个类的`tapeAliases``HashMap`中。映射关系是别名(`Alias`)对应类`class`。包括简单类型、实体类类型和其他的，比如说数据源`POOLED`对应`PooledDatasourceFactory.class`。

```java
private final Map<String, Class<?>> typeAliases = new HashMap<>();
public TypeAliasRegistry() {
  registerAlias("string", String.class);
  registerAlias("byte", Byte.class);
  registerAlias("long", Long.class);
  registerAlias("short", Short.class);
  registerAlias("int", Integer.class);
  registerAlias("integer", Integer.class);
  registerAlias("double", Double.class);
  registerAlias("float", Float.class);
  registerAlias("boolean", Boolean.class);

  registerAlias("byte[]", Byte[].class);
  registerAlias("long[]", Long[].class);
  registerAlias("short[]", Short[].class);
  registerAlias("int[]", Integer[].class);
  registerAlias("integer[]", Integer[].class);
  registerAlias("double[]", Double[].class);
  registerAlias("float[]", Float[].class);
  registerAlias("boolean[]", Boolean[].class);

  registerAlias("_byte", byte.class);
  registerAlias("_long", long.class);
  registerAlias("_short", short.class);
  registerAlias("_int", int.class);
  registerAlias("_integer", int.class);
  registerAlias("_double", double.class);
  registerAlias("_float", float.class);
  registerAlias("_boolean", boolean.class);

  registerAlias("_byte[]", byte[].class);
  registerAlias("_long[]", long[].class);
  registerAlias("_short[]", short[].class);
  registerAlias("_int[]", int[].class);
  registerAlias("_integer[]", int[].class);
  registerAlias("_double[]", double[].class);
  registerAlias("_float[]", float[].class);
  registerAlias("_boolean[]", boolean[].class);

  registerAlias("date", Date.class);
  registerAlias("decimal", BigDecimal.class);
  registerAlias("bigdecimal", BigDecimal.class);
  registerAlias("biginteger", BigInteger.class);
  registerAlias("object", Object.class);

  registerAlias("date[]", Date[].class);
  registerAlias("decimal[]", BigDecimal[].class);
  registerAlias("bigdecimal[]", BigDecimal[].class);
  registerAlias("biginteger[]", BigInteger[].class);
  registerAlias("object[]", Object[].class);
  registerAlias("map", Map.class);
  registerAlias("hashmap", HashMap.class);
  registerAlias("list", List.class);
  registerAlias("arraylist", ArrayList.class);
  registerAlias("collection", Collection.class);
  registerAlias("iterator", Iterator.class);
  registerAlias("ResultSet", ResultSet.class);
}
```