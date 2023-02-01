# redis



## 安装

> redis只有linux版本的，平常在windows下使用的是微软转译过的。
>
> 我是使用的linux版本： Ubuntu 22.04 ARM64
>
> 虚拟机选择：Parallels Desktop 18    或租一个服务器

- 下载redis[官网下载](https://redis.io/download/)

- 移动到`/usr/local/src`下，并解压

  `mv  redis-6.2.6.tar.gz /usr/local/src/redis-6.2.6.tar.gz`

  `tar -xzf redis-6.2.6.tar.gz`

- 进入解压后的文件夹执行编译命令

  `make && make install`

### 启动redis方式



#### 默认启动

> redis编译好后会将`redis-server` 加入环境。直接执行redis-server即可。
>
> 这种启动属于`前台启动`，会阻塞整个会话窗口，窗口关闭或者按下`CTRL + C`则Redis停止。

进入`usr/local/bin`查看

```bash
cd /usr/local/bin
```

![image-20221229185053900](redis基础.assets/image-20221229185053900.png) 		

#### 自定义配置文件

> 此方式，redis会以守护进程的方式启动。

修改redis配置文件（`/usr/local/src/redis-6.2.6/redis.conf`）：

修改前备份一下（`cp redis.conf  redis.conf.bak`）

```bash
##允许访问的地址，默认是127.0.0.1，会导致只能在本地访问。修改为0.0.0.0则可以在任意IP访问，生产环境不要设置为0.0.0.0
bind 0.0.0.0
# 守护进程，修改为yes后即可后台运行
daemonize yes 
requirepass 123123
```

```bash
port 6379
# 工作目录，默认是当前目录，也就是运行redis-server时的命令，日志.持久化等文件会保存在这个目录
dir .
# 数据库数量，设置为1，代表只使用1个库，默认有16个库，编号0~15
databases 1
# 设置redis能够使用的最大内存
maxmemory 512mb
# 日志文件，默认为空，不记录日志，可以指定日志文件名
logfile "redis.log"
```

启动redis：

```bash
redis-server redis.conf
```



#### 注册redis服务&开机自启

首先，新建一个系统服务文件：

```sh
vi /etc/systemd/system/redis.service
```

```bash
[Unit]
Description=redis-server
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/bin/redis-server /usr/local/src/redis-6.2.6/redis.conf
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

然后重载系统服务：

```sh
systemctl daemon-reload
```

现在，我们可以用下面这组命令来操作redis了：

```sh
# 启动
systemctl start redis
# 停止
systemctl stop redis
# 重启
systemctl restart redis
# 查看状态
systemctl status redis
```

执行下面的命令，可以让redis开机自启：

```sh
systemctl enable redis
```



### redis客户端

- redis-cli 自带的命令行客户端
- 图形化客户端
- 各种编程客户端

#### redis-cli

Redis安装完成后就自带了命令行客户端：redis-cli，使用方式如下：

```sh
redis-cli [options] [commonds]
```

其中常见的options有：

- `-h 127.0.0.1`：指定要连接的redis节点的IP地址，默认是127.0.0.1
- `-p 6379`：指定要连接的redis节点的端口，默认是6379
- `-a 123321`：指定redis的访问密码 

其中的commonds就是Redis的操作命令，例如：

- `ping`：与redis服务端做心跳测试，服务端正常会返回`pong`

不指定commond时，会进入`redis-cli`的交互控制台：

![image-20221229190450851](redis基础.assets/image-20221229190450851.png)



#### 图形化客户端

> `quickredis`能用

[下载地址](https://github.com/quick123official/quick_redis_blog/releases/)

![image-20221229190531580](redis基础.assets/image-20221229190531580.png)



## 常用命令



### redis命令查询

> 如何查询redis命令：

- 官网查询
- 命令查询

[官网](https://redis.io/commands/)

![image-20221229191126885](redis基础.assets/image-20221229191126885.png)

> 官网给出的命令详情非常详细，命令行也可以给出命令简单信息：

`help`命令

```bash
127.0.0.1:6379> help
redis-cli 6.2.6
To get help about Redis commands type:
      "help @<group>" to get a list of commands in <group>
      "help <command>" for help on <command>
      "help <tab>" to get a list of possible help topics
      "quit" to exit
```

`help @group`:查看某一分组的所有命令

```bash
127.0.0.1:6379> help @String
  APPEND key value
  summary: Append a value to a key
  since: 2.0.0
  BITCOUNT key [start end]
  summary: Count set bits in a string
  since: 2.6.0
  BITFIELD key [GET type offset] [SET type offset value] [INCRBY type offset increment] [OVERFLOW WRAP|SAT|FAIL]
  summary: Perform arbitrary bitfield integer operations on strings
  since: 3.2.0
  
.....
```

`help command `：查看某一具体命令

```bash
127.0.0.1:6379> help set
  SET key value [EX seconds|PX milliseconds|EXAT timestamp|PXAT milliseconds-timestamp|KEEPTTL] [NX|XX] [GET]
  summary: Set the string value of a key
  since: 1.0.0
  group: string
```



### 通用命令

常用通用命令一般都是对key进行操作：

- KEYS：查看符合模板的所有key
- DEL：删除一个指定的key
- EXISTS：判断key是否存在
- EXPIRE：给一个key设置有效期，有效期到期时该key会被自动删除
- TTL：查看一个KEY的剩余有效期

例如：

```bash
127.0.0.1:6379> keys k*
1) "k3"
2) "k2"
3) "k1"
```

```bash
127.0.0.1:6379> del k1 k2
(integer) 2
127.0.0.1:6379> keys * 
1) "k3"
```

```bash
127.0.0.1:6379> EXISTS k1 k2 k3
(integer) 1
# 只有一个k3存在
```

`expire`只会对存在的key生效：

设置过期时间的key过期后ttl值为-2

不设置过期时间的key，也就是永久有效，ttl值为-1

```bash
127.0.0.1:6379> EXPIRE k1 10
(integer) 0
127.0.0.1:6379> ttl k1
(integer) -2
127.0.0.1:6379> EXPIRE k3 10
(integer) 1
127.0.0.1:6379> ttl k3
(integer) 5
127.0.0.1:6379> ttl k3
(integer) 1
127.0.0.1:6379> ttl k3
(integer) -2
127.0.0.1:6379> ttl k3
(integer) -2
```

### 基本数据类型

#### String

String类型，也就是字符串类型，是Redis中最简单的存储类型。

其value是字符串，不过根据字符串的格式不同，又可以分为3类：

* string：普通字符串
* int：整数类型，可以做自增.自减操作
* float：浮点类型，可以做自增.自减操作

String的常见命令有：

* SET：添加或者修改已经存在的一个String类型的键值对
* GET：根据key获取String类型的value
* MSET：批量添加多个String类型的键值对
* MGET：根据多个key获取多个String类型的value
* INCR：让一个整型的key自增1
* INCRBY:让一个整型的key自增并指定步长，例如：incrby num 2 让num值自增2
* INCRBYFLOAT：让一个浮点类型的数字自增并指定步长
* SETNX：添加一个String类型的键值对，前提是这个key不存在，否则不执行
* SETEX：添加一个String类型的键值对，并且指定有效期

example：

> set命令：如果key不存在则设置值，如果key存在则更新值

```bash
127.0.0.1:6379> set name yuyc
OK
127.0.0.1:6379> get name
"yuyc"
127.0.0.1:6379> set name yuycnew
OK
127.0.0.1:6379> get name
"yuycnew"
```

> MSET key value [key value....]
>
> MGET key [key ......]

```bash
127.0.0.1:6379> MSET k1 v1 k2 v2 k3 v3
OK
127.0.0.1:6379> MGET k1 k2 k3
1) "v1"
2) "v2"
3) "v3"
```

> Incr 命令，对存储的整数自增1
>
> incrby key increment ,对存储的整数自增,并指定步长
>
> INCRBYFLOAT key  increment

```bash
127.0.0.1:6379> set age 22
OK
127.0.0.1:6379> INCR age 
(integer) 23
127.0.0.1:6379> INCR age 
(integer) 24
127.0.0.1:6379> INCRBY age 2
(integer) 26
127.0.0.1:6379> INCRBYFLOAT age 0.1
"26.1"
```

> setnx key value 当key不存在添加key，当key存在添加失败
>
> setex 添加key并指定过期时间，等同于set key value ex secends

```bash
127.0.0.1:6379> setnx age 22
(integer) 0
127.0.0.1:6379> setex age 10 22
OK
127.0.0.1:6379> ttl age
(integer) 3
127.0.0.1:6379> ttl age
(integer) -2
127.0.0.1:6379> set age 10 ex 10
OK
```

##### 层次结构

> redis是一个key-value形式的存储结构，不允许存在重复key值，往往会有冲突。
>
> 通过为key添加前缀解决冲突。

```bash
127.0.0.1:6379> set test:user:1 '{"name":lizicheng","age":33}'
OK
127.0.0.1:6379> set test:user:2 '{"name":yuyc","age":22}'
OK
127.0.0.1:6379> set test:dept:1 '{"name":"dept1",num:1}'
OK
127.0.0.1:6379> set test:dept:2 '{"name":"dept2",num:100}'
OK
```

在redis-cli这里key值如下：

```bash
127.0.0.1:6379> keys test*
1) "test:user:1"
2) "test:dept:2"
3) "test:dept:1"
4) "test:user:2"
```

但是在quickredis中可分层次：

![image-20221230002854599](redis基础.assets/image-20221230002854599.png)

#### hash

> 使用String存储json字符串，不利于修改每一属性的值。使用redis的hash数据结构可对每一个属性单独存储，操作起来简单许多。

**Hash类型的常见命令**

- HSET key field value：添加或者修改hash类型key的field的值

- HGET key field：获取一个hash类型key的field的值

- HMSET：批量添加多个hash类型key的field的值   ( 现等同于HSET命令 )

- HMGET：批量获取多个hash类型key的field的值

- HGETALL：获取一个hash类型的key中的所有的field和value
- HKEYS：获取一个hash类型的key中的所有的field
- HINCRBY:让一个hash类型key的字段值自增并指定步长
- HSETNX：添加一个hash类型的key的field值，前提是这个field不存在，否则不执行



> HSET key field value [field value ....]  等同于  HMSET   （跟版本有关，有些版本hset不可批量设置）
>
> HGET key field
>
> HMGET key field [field ....]

```bash
127.0.0.1:6379> hset test:huser:1 name yuyc age 22
(integer) 2
127.0.0.1:6379> hset test:huser:2 name liziceng age 22
(integer) 2
127.0.0.1:6379> hget test:huser:1 name
"yuyc"
127.0.0.1:6379> HMGET test:huser:1 name age
1) "yuyc"
2) "22"
```



> HGETALL key 。
>
> 类似于HashMap的Entities

```bash
127.0.0.1:6379> HGETALL test:huser:1
1) "name"
2) "yuyc"
3) "age"
4) "22"
```



> HKEYS key     获取所有属性
>
> HVALS key     获取所有值

```bash
127.0.0.1:6379> hkeys test:huser:1
1) "name"
2) "age"
127.0.0.1:6379> HVALS test:huser:1
1) "yuyc"
2) "22"
```

#### list

Redis中的List类型与Java中的LinkedList类似，可以看做是一个双向链表结构。既可以支持正向检索和也可以支持反向检索。

特征也与LinkedList类似：

* 有序
* 元素可以重复
* 插入和删除快
* 查询速度一般

常用来存储一个有序数据，例如：朋友圈点赞列表，评论列表等。

**List的常见命令有：**

- LPUSH key element ... ：向列表左侧插入一个或多个元素
- LPOP key：移除并返回列表左侧的第一个元素，没有则返回nil
- RPUSH key element ... ：向列表右侧插入一个或多个元素
- RPOP key：移除并返回列表右侧的第一个元素
- LRANGE key star end：返回一段角标范围内的所有元素 (end 为 -1 代表返回start之后所有元素) 
- BLPOP和BRPOP：与LPOP和RPOP类似，只不过在没有元素时等待指定时间，而不是直接返回nil

```bash
127.0.0.1:6379> lpush ids 1 2 3
(integer) 3
127.0.0.1:6379> rpush ids -1 -2 -3
(integer) 6
127.0.0.1:6379> lrange ids 0 -1
1) "3"
2) "2"
3) "1"
4) "-1"
5) "-2"
6) "-3"
127.0.0.1:6379> lpop ids 2
1) "3"
2) "2"
127.0.0.1:6379> rpop ids 2
1) "-3"
2) "-2"
127.0.0.1:6379> lrange ids 0 -1
1) "1"
2) "-1"
## 没有元素 等待 30 再返回nil
127.0.0.1:6379> blpop l1 30
(nil)
(30.05s)
```

#### set

Redis的Set结构与Java中的HashSet类似，可以看做是一个value为null的HashMap。因为也是一个hash表，因此具备与HashSet类似的特征：

* 无序
* 元素不可重复
* 查找快
* 支持交集.并集.差集等功能

**Set类型的常见命令**

* SADD key member ... ：向set中添加一个或多个元素
* SREM key member ... : 移除set中的指定元素
* SCARD key： 返回set中元素的个数
* SISMEMBER key member：判断一个元素是否存在于set中
* SMEMBERS：获取set中的所有元素
* SINTER key1 key2 ... ：求key1与key2的交集
* SDIFF key1 key2 ... ：求key1与key2的差集
* SUNION key1 key2 ..：求key1和key2的并集

```bash
127.0.0.1:6379> sadd c1 a b c 
(integer) 3
127.0.0.1:6379> srem c1 c
(integer) 1
# 不可重复添加
127.0.0.1:6379> sadd c1 a
(integer) 0
127.0.0.1:6379> scard c1
(integer) 2
127.0.0.1:6379> SMEMBERS c1
1) "a"
2) "b"
127.0.0.1:6379> SISMEMBER c1 a
(integer) 1
127.0.0.1:6379> sadd c2 b c d
(integer) 3
# 交集
127.0.0.1:6379> SINTER c1 c2
1) "b"
# 并集
127.0.0.1:6379> SUNION c1 c2 
1) "c"
2) "a"
3) "b"
4) "d"
# 差集
127.0.0.1:6379> SDIFF c1 c2
1) "a"
```

#### sortedset

Redis的SortedSet是一个可排序的set集合，与Java中的TreeSet有些类似，但底层数据结构却差别很大。SortedSet中的每一个元素都带有一个score属性，可以基于score属性对元素排序，底层的实现是一个跳表（SkipList）加 hash表。

SortedSet具备下列特性：

- 可排序
- 元素不重复
- 查询速度快

因为SortedSet的可排序特性，经常被用来实现排行榜这样的功能。

SortedSet的常见命令有：

- ZADD key score member：添加一个或多个元素到sorted set ，如果已经存在则更新其score值
- ZREM key member：删除sorted set中的一个指定元素
- ZSCORE key member : 获取sorted set中的指定元素的score值
- ZRANK key member：获取sorted set 中的指定元素的排名
- ZCARD key：获取sorted set中的元素个数
- ZCOUNT key min max：统计score值在给定范围内的所有元素的个数
- ZINCRBY key increment member：让sorted set中的指定元素自增，步长为指定的increment值
- ZRANGE key min max：按照score排序后，获取指定排名范围内的元素
- ZRANGEBYSCORE key min max：按照score排序后，获取指定score范围内的元素
- ZDIFF.ZINTER.ZUNION：求差集.交集.并集

注意：所有的排名默认都是升序，如果要降序则在命令的Z后面添加REV即可，例如：

- **升序**获取sorted set 中的指定元素的排名：ZRANK key member
- **降序**获取sorted set 中的指定元素的排名：ZREVRANK key memeber



```bash
127.0.0.1:6379> zadd zsx 30 yuyc1 20 yuyc2 40 yuyc3 55 yuyc4 5 yuyc5
(integer) 5
127.0.0.1:6379> ZREM zsx yuyc1
(integer) 1
##  zrange key min max 获取指定名次
127.0.0.1:6379> zrange zsx 0 -1
1) "yuyc5"
2) "yuyc2"
3) "yuyc3"
4) "yuyc4"
127.0.0.1:6379> ZSCORE zsx yuyc2
## 返回指定成员排名  从0开始
127.0.0.1:6379> zrank zsx yuyc2
(integer) 1
## 返回成员个数
127.0.0.1:6379> zcard zsx
(integer) 4
## 返回指定分数范围内的成员个数
127.0.0.1:6379> zcount zsx 0 40
(integer) 3
## 给指定成员分数加3
127.0.0.1:6379> ZINCRBY zsx 3 yuyc2
"23"
## 返回指定分数范围内的成员  对比 zrange 
127.0.0.1:6379> ZRANGEBYSCORE zsx 0 40
1) "yuyc5"
2) "yuyc2"
3) "yuyc3"

## 升序 降序获取成员排名
127.0.0.1:6379> zrank zsx yuyc2
(integer) 1
127.0.0.1:6379> ZREVRANK zsx yuyc2
(integer) 2
```



### 特殊类型









## redis客户端

### jedis

> redis  java客户端之一 ----jedis

[redis客户端](https://redis.io/resources/clients/#java)

测试链接：

```java
@Test
public void test() {
   final Jedis jedis = new Jedis("10.211.55.4", 6379);
   jedis.auth("123123");
   jedis.set("key", "value");
   System.out.println(jedis.get("key"));
}
```





### jedis连接池

> jedis连接

```java
public class JedisFactory {
    private static JedisPool pool;

    static {

        final GenericObjectPoolConfig<Jedis> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(1024);
        config.setMaxIdle(10);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        pool = new JedisPool(config, "10.211.55.4", 6379, 6000, "123123");
    }
    public static Jedis jedis() {
        return pool.getResource();
    }
    public static void close(Jedis jedis) {
        jedis.close();
    }
}
```

```java
@Test
public void jedisPool(){
    final Jedis jedis = JedisFactory.jedis();
    System.out.println(jedis.get("key"));
    JedisFactory.close(jedis);
}
```

### SpringDataRedis-客户端

> SpringData是Spring中数据操作的模块，包含对各种数据库的集成，其中对Redis的集成模块就叫做SpringDataRedis，官网地址：https://spring.io/projects/spring-data-redis

* 提供了对不同Redis客户端的整合（Lettuce和Jedis）
* 提供了RedisTemplate统一API来操作Redis
* 支持Redis的发布订阅模型
* 支持Redis哨兵和Redis集群
* 支持基于Lettuce的响应式编程
* 支持基于JDK.JSON.字符串.Spring对象的数据序列化及反序列化
* 支持基于Redis的JDKCollection实现

#### Spring配置

想要通过IOC注册并使用RedisTemplate，必须先了解以下两个接口 `org.springframework.data.redis.connection` package and its `RedisConnection` and `RedisConnectionFactory` interfaces for working with and retrieving active connections to Redis.

- RedisConnection
- RedisConnectionFactory

> RedisConnectionFactory接口下有两个实现类JedisConnectionFactory和LettuceConnectionFactory。

##### xml配置

```xml
<!--  如果redis设置密码，reids客户端需要auth命令验证，这里也需要配置  -->
<bean id="redisPass" class="org.springframework.data.redis.connection.RedisPassword">
    <constructor-arg value="123123"/>
</bean>
<bean id="redisStandaloneConfiguration"
      class="org.springframework.data.redis.connection.RedisStandaloneConfiguration">
    <!--        <property name="hostName" value="10.211.55.4"/>-->
    <property name="hostName" value="127.0.0.1"/>
    <property name="port" value="6379"/>
    <!--        <property name="password" ref="redisPass"/>-->
</bean>

<!--  不设置则使用默认连接池配置  -->
<bean id="jedisPoolConfig" class="org.apache.commons.pool2.impl.GenericObjectPoolConfig">
    <property name="maxTotal" value="1024"/>
    <property name="maxIdle" value="10"/>
    <property name="testOnBorrow" value="true"/>
    <property name="testOnReturn" value="true"/>
</bean>

<bean id="jedisClientConfiguration"
      class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory.MutableJedisClientConfiguration">
    <property name="poolConfig" ref="jedisPoolConfig"/>
</bean>

<bean id="jedisConnectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
    <constructor-arg ref="redisStandaloneConfiguration"/>
    <constructor-arg ref="jedisClientConfiguration"/>
</bean>

<bean id="redisTemplate" class="org.springframework.data.redis.core.RedisTemplate">
    <property name="connectionFactory" ref="jedisConnectionFactory"/>
</bean>
```

验证：

```java
@Test
public void test1() {
    final ApplicationContext app = new ClassPathXmlApplicationContext("classpath:application.xml");
    final String[] beanDefinitionNames = app.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
        System.out.println(beanDefinitionName);
    }
    final RedisTemplate redisTemplate = app.getBean(RedisTemplate.class);
    final ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
    valueOperations.set("redistemplate", "value");
    System.out.println(valueOperations.get("redistemplate"));
}
```



##### javaconfig配置

```java
public class RedisConfigration {

    public @Bean RedisPassword redisPassword() {
        return RedisPassword.of("123123");
    }
    public @Bean RedisStandaloneConfiguration redisStandaloneConfiguration(@Autowired RedisPassword redisPassword) {
        final RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        // redisStandaloneConfiguration.setPassword(redisPassword);
        redisStandaloneConfiguration.setHostName("127.0.0.1");
        // redisStandaloneConfiguration.setHostName("10.211.55.4");
        redisStandaloneConfiguration.setPort(6379);
        return redisStandaloneConfiguration;
    }
    public @Bean JedisConnectionFactory jedisConnectionFactory(@Autowired RedisStandaloneConfiguration redisStandaloneConfiguration) {
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    public @Bean RedisTemplate redisTemplate(@Autowired JedisConnectionFactory jedisConnectionFactory) {
        final RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }
}
```

验证：

```java
@Test
public void test2() {
    final ApplicationContext app = new AnnotationConfigApplicationContext(RedisConfigration.class);
    final String[] beanDefinitionNames = app.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
        System.out.println(beanDefinitionName);
    }
    final RedisTemplate redisTemplate = app.getBean(RedisTemplate.class);
    final ValueOperations valueOperations = redisTemplate.opsForValue();
    System.out.println(valueOperations.get("redistemplate"));
}
```



##### redis支持自定义范型

> redisTemplate 可设置范型。
>
> 设置范型需要配合序列化工具使用

```java
/**
 * 对非string的key和value 需要设置序列化工具
 */
@Test
public void test3() {
    final ApplicationContext app = new AnnotationConfigApplicationContext(RedisConfigration.class);

    final RedisTemplate redisTemplate = app.getBean(RedisTemplate.class);
    GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
    redisTemplate.setKeySerializer(RedisSerializer.string());
    redisTemplate.setHashKeySerializer(RedisSerializer.string());
    redisTemplate.setValueSerializer(jsonRedisSerializer);
    redisTemplate.setHashValueSerializer(jsonRedisSerializer);

    final ListOperations<String, User> listOperations = redisTemplate.opsForList();
    final List<User> users = Arrays.asList(new User("李自成", 21), new User("李自成", 21));
    final Long count = listOperations.leftPushAll("users", users);
    System.out.println(count);

    final List<User> users2 = listOperations.range("users",0,listOperations.size("users"));
    users2.forEach(System.out::println);
    
    final User users1 = listOperations.leftPop("users");
    System.out.println(users1);
}
```



#### SpringBoot配置

依赖：

```java
<!--redis依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version>2.6.6</version>
</dependency>
```

配置：

```yml
spring:
 redis:
#    host: 10.211.55.4
    host: 127.0.0.1
    port: 6379
#    password: 123123
    lettuce:
      pool:
        max-active: 8  #最大连接
        max-idle: 8   #最大空闲连接
        min-idle: 0   #最小空闲连接
        max-wait: 100ms #连接等待时间
```

```java
/**
    * 注册redisTemplate 并设置序列化工具
    * @param connectionFactory
    * @return
*/
public @Bean RedisTemplate<String, Object> redisTemplate(@Autowired RedisConnectionFactory connectionFactory){
    // 创建RedisTemplate对象
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    // 设置连接工厂
    template.setConnectionFactory(connectionFactory);
    // 创建JSON序列化工具
    GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
    // 设置Key的序列化
    template.setKeySerializer(RedisSerializer.string());
    template.setHashKeySerializer(RedisSerializer.string());
    // 设置Value的序列化
    template.setValueSerializer(jsonRedisSerializer);
    template.setHashValueSerializer(jsonRedisSerializer);
    // 返回
    return template;
}
```

验证：

```java
public static void main(String[] args) {
    final ApplicationContext app = SpringApplication.run(RedisDemoApplication.class, args);
    //打印beannames
    final String[] beanNames = app.getBeanDefinitionNames();
    for (String beanName : beanNames) {
        System.out.println(beanName);
    }
    /**
     * 得到redisTemplate并进行插入查询操作
     */
    final RedisTemplate redisTemplate = app.getBean("redisTemplate",RedisTemplate.class);
    final ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
    valueOperations.set("name","李自成");
    final Object name = valueOperations.get("name");
    log.info("name:{}",name);
}
```







