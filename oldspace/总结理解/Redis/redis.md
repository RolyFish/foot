# windows安装redis

下载，解压。

> 开启服务redis-server，默认端口号6379

![image-20211227103134101](redis.assets\image-20211227103134101.png)

> 开启客户端redis-cli

![image-20211227103214685](redis.assets\image-20211227103214685.png)

kv键值对存储数据

# linux安装redis

下载gz包。

> 放到linuxfu服务器上，直接拖过去就行
>
> 一般将程序放在opt文件夹下

![image-20211227105335774](redis.assets\image-20211227105335774.png)

![image-20211227105456206](redis.assets\image-20211227105456206.png)

>解压 tar -zxvf redis-6.2.6.tar.gz 
>
>就可以得到redis的文件夹了

![image-20211227105646787](redis.assets\image-20211227105646787.png)

> 并不像windows下游可执行的exe

![image-20211227105931343](redis.assets\image-20211227105931343.png)

> 所以我们需要安装环境
>
> 安装gcc   cd 
>
> 查看gcc版本  gcc -v
>
> 执行  make
>
> 执行make install查看安装状态，服务端客户端以及性能监控都安装成功

![image-20211227110352625](redis.assets\image-20211227110352625.png)

> linux程序默认安装路径:/usr/local/bin
>
> 刚刚安装的都在这

![image-20211227110721076](redis.assets\image-20211227110721076.png)

> 复制一份redis.confg作为开发使用

![image-20211227111719428](redis.assets\image-20211227111719428.png)

> 修改redis.config   daemonize ->yes



> 启动redis指定配置文件 redis-server 配置文件的path
>
> 用redis-cli测试

![image-20211227112323444](redis.assets\image-20211227112323444.png)

> 查看redis服务开启状态

![image-20211227112619383](redis.assets\image-20211227112619383.png)

> 关闭redis服务  shutdown

![image-20211227112714894](redis.assets\image-20211227112714894.png)

> 压力测试 redis-benchmark -h localhost -p 6379 -c 100 -n 100000

```txt
====== SET ======     set请求                                              
  100000 requests completed in 1.36 seconds  100000个请求1.36秒完成
  100 parallel clients                       100个并发
  3 bytes payload							每次写入3个字节
  keep alive: 1								保持1个连接
  host configuration "save": 3600 1 300 100 60 10000
  host configuration "appendonly": no
  multi-thread: no

```

# 基本知识

> redis默认有16个数据库

<img src="redis.assets\image-20211227151009552.png" alt="image-20211227151009552" style="zoom:67%;" />

> 查看当前数据库size  DBSIZE
>
> 切换数据库select integet
>
> 查看所有的key  keys *
>
> 清空当前数据库  flushdb
>
> 清空全部数据库  flushall

![image-20211227151729850](redis.assets\image-20211227151729850.png)

> redis6379端口的故事：一个女明星的名字缩写

> redis是单线程的？

```txt
首先redis是非常快的，我们用redis-benchmark进行压测表示它性能很快。redis基于内存的，他的性能瓶颈不在于cpu，而是在于内存及网络带宽，多加俩线程而内存不够大性能还是提升不了，所以单线程实现就够了。
```

> 为啥redis单线程还是很快（100000+的qps）不比memcache差

多线程服务的性能不一定比单线程高：多线程的调度以及上下文的切换是非常消耗时间的。

redis的所有读写操作都在内存中完成，他的多次读写基于同一个cpu和内存，没有cpu上下文调度问题，所以说他是很快的。

# redis数据类型

> Redis 是一个开源（BSD许可）的，内存中的数据结构存储系统，它可以用作数据库、缓存和消息中间件

```txt
字符串（strings）， 散列（hashes）， 列表（lists）， 集合（sets）， 有序集合（sorted sets）
```

## redis-key

> keys * 命令查看所有的key

> exists key  是否存在某个key  （1/0）

> move key db  从某个数据库移除某个key

> ==expire key  秒数  设置失效时间（用户session  单点登录）==

> type  key 查看key的类型（string 、int ）

> ttl key 查看多久过期

## string（字符串）

> append   str 追加字符串  返回整个字符串长度

> strlen  key  查看某个key值的长度

> incr key   加一

> decr key  减一

> incrby key  steps   步长

> decrby key steps  

> getrange key 0 4  截取0-4   、 0  -1  获取全部

> setrange key  index   str    从某个下表开始用str替换后面的字符

> setex key 秒数 value = set key expire  set值并且设置过期时间

> setnx  key value  = set if not exist  不存在设置  存在不会覆盖

> mset k1 v1 k2 v2 k3 v3  批量set

> mget k1  k2 k3   批量get

> msetnx  同时设置多个不存在的key（原子性操作，一起成功，一起失败）

> mset user:1:name yyc user:1:age 123  是现存对象

> getset   先get再set     不存在null

## List（值可重复）

> lpush  key values  往list的头部存数据

> lrange  key 0 -1    拿到数据

> rpush key values    往list的尾部存数据

> lpop key   count      移除头部前count数据

> rpop key   count  	移除尾部前count数据

> lindex key index    查看下标为index的元素

> llen key   查看list长度

> lrem key count element   移除指定元素，指定个数的值

> ==ltrim  key start end  截断删除的操作==

> rpoplpush  source destination    将指定list的尾部第一个元素移除并添加到目标list中

> lset key index value      index所指下表值存在，将其替换为value

> linsert key before  value1 value2     往list中value1的前面插入value2

这里不难发现这个数据类型是基于链表的，对查询复杂，对插入简单

总结：list插入操作lpush，查看操作lindex  lrange 删除操作 lpop rpop ltrim ==lrem key count element==

list允许重复可删除多个重复的element，lrem key count element

list底层是一个链表，链表对于插入友好即 linsert key before v1 v2

## set（值不可重复）

> sadd  myset values   添加值

> smenbers  myset    查看所有set值

> sismember myset value  查看set中是否存在某个值

> scard myset   元素个数

> srem myset values 移除元素

> srandmember  myset  count  随机获取count个元素

> spop myset count   随机移除count个元素

> smove myset myset2 value  将指定元素移到myset2

> sdiff myset myset2   第一个做参照和myset2中不同的

> sinter myset myset2 交集

> sunion myset myset2 并集

## hash（map集合）

> hset myhash name yyc age 23

> hget myhash name

> hmset

> hmget

> hgetall myhash  得到所有的键值对

> hdel myhash name 删除指定的键值对

> hlen  得到键值对数量

> hexists myhash name 判断指定字段是否存在

> hkeys myhash  得到所有的key

> hvals myhash   得到所有的values

> HINCRBY MYHASH  age -1(1)   自增

> hsetnx   hash set if not exists

存可变数据，用户

hash和我们的字符串差不多（换句话说hash可以做的字符串都可以）

## zset（有序集合）

> zadd myzset 1 one 2 twe 3 three  添加三条数据

> zrange myzset 0 -1 查看所有数据（排序过后的）  从小到大

> zrevrange myzset 0 -1    从大到小

> ZRANGEBYSCORE myzset -inf +inf  查询所有的数据  inf可以是具体的值

>  ZRANGEBYSCORE myzset -inf +inf withscores  附带分数

> zrem myzset one  删除指定元素

> zcard myzset   元素数量

> zcount myzset -inf +inf   查看一个范围的数量

## geospatial（地理空间）

> <u>***将指定的地理空间位置（纬度、经度、名称）添加到指定的`key`中***</u>

> geoadd china:city 121.48 31.23 shanghai 114.06 22.54 shenzhen  添加地理坐标

> geopos  china:city shanghai  得到具体的地理坐标

> geodist china:city shanghai beijing m

- **m** 表示单位为米。
- **km** 表示单位为千米。
- **mi** 表示单位为英里。
- **ft** 表示单位为英尺。

> georadius china:city 121.48 31.22 2000 km withcoord withdist withhash count 2 desc 
>
> 查询121.48 31.22 两千公里以内的地理坐标坐标
>
> withcoord携带坐标
>
> withdist携带距离
>
> withhash  hash值
>
> count 2 显示俩个
>
> desc  倒序

> GEORADIUSBYMEMBER china:city beijing 2000 km withcoord withdist withhash count 4  以某个地理坐标为中心

> geohash

```bash
127.0.0.1:6379> geohash china:city beijing chongqing
1) "wx4dzjubj70"
2) "wm78zcm2rb0"
```

geo底层是一个zset,可以用zset写操作geo

geo没有删除操作，可以用zset来操作

> zrem china:city  beijing

## HyperLogLog

> 基数?    {a.b.c.d}  {a,b.c.d.e}  基数=5

> redis HyperLogLog基数统计的算法 

使用场景：统计网页**uv**一般通过set用户id来做，然后进行集合的并集等操作完成计数，但是我们的目的是为了计数而不是存用户id，这里HyperLogLog使用固定内存

> pfadd  pfcount pfmerge

```bash
剔除重复
127.0.0.1:6379> pfadd usercount a b c d e f g a
(integer) 1
127.0.0.1:6379> pfcount usercount
(integer) 7
127.0.0.1:6379> pfadd usercount2 a2 b2 a b
(integer) 1
127.0.0.1:6379> pfcount usercount2
(integer) 4
合并集合
127.0.0.1:6379> pfmerge usercount3 usercount usercount2
OK
127.0.0.1:6379> pfcount usercount3
(integer) 9
```

## bitmap(位图)

==用于统计状态  非0既1的状态==

> setbit   getbit   bitcount

> 统计一星期打卡天数

```bash
127.0.0.1:6379> setbit sign 0 1
(integer) 0
127.0.0.1:6379> setbit sign 1 0
(integer) 0
127.0.0.1:6379> setbit sign 2 1
(integer) 0
127.0.0.1:6379> setbit sign 4 1
(integer) 0
127.0.0.1:6379> setbit sign 5 0
(integer) 0
127.0.0.1:6379> setbit sign 6 0
(integer) 0
127.0.0.1:6379> setbit sign 3 0
(integer) 0
127.0.0.1:6379> getbit sign 0
(integer) 1
127.0.0.1:6379> bitcount sign
(integer) 3
```

# 事务

```bash
ACID，是指数据库管理系统（DBMS）在写入或更新资料的过程中，为保证事务（transaction）是正确可靠的，所必须具备的四个特性：原子性（atomicity，或称不可分割性）、一致性（consistency）、隔离性（isolation，又称独立性）、持久性（durability）。

Atomicity（原子性）：一个事务（transaction）中的所有操作，要么全部完成，要么全部不完成，不会结束在中间某个环节。事务在执行过程中发生错误，会被恢复（Rollback）到事务开始前的状态，就像这个事务从来没有执行过一样。
Consistency（一致性）：在事务开始之前和事务结束以后，数据库的完整性没有被破坏。这表示写入的资料必须完全符合所有的预设规则，这包含资料的精确度、串联性以及后续数据库可以自发性地完成预定的工作。
Isolation（隔离性）：数据库允许多个并发事务同时对其数据进行读写和修改的能力，隔离性可以防止多个事务并发执行时由于交叉执行而导致数据的不一致。事务隔离分为不同级别，包括读未提交（Read uncommitted）、读提交（read committed）、可重复读（repeatable read）和串行化（Serializable）。
Durability（持久性）：事务处理结束后，对数据的修改就是永久的，即便系统故障也不会丢失
```

> redis事务本质：一组命令的队列！事务中的命令都会被序列化，且会按顺序执行。

一次性，顺序性，排他性（不会受别的事务影响）

> **==redis单条命令是保持原子性的（mset k1 v1 k2 v3...），但是redis事务不保证原子性==**

- 开启事务（multi）
- 命令入队 （自动入队）
- 执行事务（exec）
- 放弃事务（discard）  清空事务队列，放弃事务

**正常的事务**

```bash
127.0.0.1:6379> multi
OK
127.0.0.1:6379(TX)> mset k1 v1 k2 v2 k3 v3
QUEUED
127.0.0.1:6379(TX)> mget k1 k2
QUEUED
127.0.0.1:6379(TX)> mset k4 v4
QUEUED
127.0.0.1:6379(TX)> exec
1) OK
2) 1) "v1"
   2) "v2"
3) OK
```

**入队出错的事务：直接报错（编译时错误）**

```bash
127.0.0.1:6379(TX)> get k1 k2
(error) ERR wrong number of arguments for 'get' command
127.0.0.1:6379(TX)> exec
(error) EXECABORT Transaction discarded because of previous errors.
127.0.0.1:6379> 
```

**执行时报错的事务（执行时错误）**

比如我们在事务队列中加一句  incrby key 10(给一个str执行增加操作)

> ==这里只报了错误的那一个命令其他正常执行，所以redis事务不保证原子性。==

==官方的一句话==

![image-20211228100747716](redis.assets\image-20211228100747716.png)

```bash
127.0.0.1:6379> multi
OK
127.0.0.1:6379(TX)> incrby k1 2
QUEUED
127.0.0.1:6379(TX)> mget k1 k2
QUEUED
127.0.0.1:6379(TX)> get k1
QUEUED
127.0.0.1:6379(TX)> exec
1) (error) ERR value is not an integer or out of range
2) 1) "v1"
   2) "v2"
3) "v1"
```

- 悲观锁

  认为任何时候都会出问题，任何情况下都加锁，性能低下。

- 乐观锁

  认为任何情况下都不会出问题。更新数据的时候查询version，判断数据是否修改过。

  

> redis的乐观锁（类似） watch（加锁）  unwatch（解锁）

正常执行：

```bash
127.0.0.1:6379> mget money
1) "100"
127.0.0.1:6379> mget money out
1) "100"
2) "0"
127.0.0.1:6379> watch money 
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379(TX)> decrby money 10
QUEUED
127.0.0.1:6379(TX)> incrby out 10
QUEUED
127.0.0.1:6379(TX)> exec
1) (integer) 90
2) (integer) 10
```

在执行之前，入队之后修改监控的数据

执行失败，监控信息判断money的数据被修改了，事务不会执行

```bash
127.0.0.1:6379> watch money
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379(TX)> mget money out
QUEUED
127.0.0.1:6379(TX)> decrby money 10
QUEUED
127.0.0.1:6379(TX)> incrby out 10
QUEUED
127.0.0.1:6379(TX)> exec
(nil)
```

```bas
localhost:6379> incrby money 100
(integer) 190
```

> ==REDIS使用的是乐观锁，watch（key）命令，监控我们的事务需要操作的字段。操作key的时候查询他的versin（是否被改变）==

# jedis

依赖

```xml
<dependencies>
    <!-- jedis -->
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>3.3.0</version>
    </dependency>

    <!--  fastjson  -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.73</version>
    </dependency>
</dependencies>
```

测试连接：

## 本地连接：开启本地redis服务

```java
public static void main(String[] args) {
    Jedis localhost = new Jedis("localhost", 6379);
    String ping = localhost.ping();
    System.out.println(ping);
}
```

## 连接远程redis服务：

```java
JedisShardInfo jedisShardInfo = new JedisShardInfo("47.103.215.51", 6379);
jedisShardInfo.setPassword("roilyfish");
Jedis jedis = new Jedis(jedisShardInfo);
jedis.connect();
```

出现的问题：**timeout连接超时**，**以及本地连接保护**

- 远程主机防火墙端口6379没开

  > firewall-cmd --zone=public --add-port=8080/tcp --permanent（永久开启6379端口）

- 阿里云的服务器需要配置安全组规则

  ![image-20211228112433047](redis.assets\image-20211228112433047.png)

- redis本机连接保护
  - vim  redis.config
  - ![image-20211228113051208](redis.assets\image-20211228113051208.png)
  - protected-node改为no（关闭保护模式）
    - ![image-20211228113248226](redis.assets\image-20211228113248226.png)



#  springboot整合redis

> springboot  2.0之后，原来的jedis被替换为lettuce

jedis：采用直连，多线程环境下是不安全的，使用jedis pool连接池。

lettuce：采用netty   实例可以在多个线程中共享，不存在线程不安全问题，减少线程，像nio模式

## 基本数据类型操作，及事务

```java
ValueOperations valueOperations = redisTemplate.opsForValue();
//list
ListOperations listOperations = redisTemplate.opsForList();
//set
SetOperations setOperations = redisTemplate.opsForSet();
//hash
HashOperations hashOperations = redisTemplate.opsForHash();
//zser
ZSetOperations zSetOperations = redisTemplate.opsForZSet();
//geo
GeoOperations geoOperations = redisTemplate.opsForGeo();
//hyperloglog
HyperLogLogOperations hyperLogLogOperations = redisTemplate.opsForHyperLogLog();
//bitmap
//redisTemplate.getConnectionFactory().getConnection().setBit()
//flushdb
redisTemplate.getConnectionFactory().getConnection().flushDb();
```

## 对象的操作

> 如果不对他进行序列化的话，jdk默认的序列化方式key值出现乱码，value（对象）值不会序列化
>
> 解决：
>
> 1、用jackson将其转化为json字符串
>
> 2、实现serializeable接口

![image-20211228215419450](redis.assets\image-20211228215419450.png)

```java
String s = new ObjectMapper().writeValueAsString(u);
valueOperations.set("user",s);
```

```java
public class User implements Serializable
```

> 一般数据都会以json字符串的形式存储
>
> 对象存储用于session之类需要即用的

==这边的key值是乱码，也就是说key值也需要序列化==

<img src="redis.assets\image-20211228222200555.png" alt="image-20211228222200555" style="zoom:67%;" />

> **解决：修改他的defaultserilizabler，自定义Redistemplate**

![image-20211228222347778](redis.assets\image-20211228222347778.png)

==redisconfig==

```java
@Configuration
public class RedisConfig {
    //编写自己的RedisTemplate
    //自己定义了一个RedisTemplate
    //这是一个固定模板，拿去可以直接使用
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        //为了我们自己开发方便，一般直接使用<String,Object>
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        //json序列化配置
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectJackson2JsonRedisSerializer.setObjectMapper(om);
        //String的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //key采用String序列化方式
        template.setKeySerializer(stringRedisSerializer);
        //hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        //value的序列化方式采用jackson
        template.setValueSerializer(objectJackson2JsonRedisSerializer);
        //hash的value序列化方式采用jackson
        template.setHashValueSerializer(objectJackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
```

# redis.config

# redis持久化

==redis是内存数据库，必须持久化==

## RDB（Redis Database）

<img src="redis.assets\image-20211229091903503.png" alt="image-20211229091903503" style="zoom: 50%;" />



> 在主从复制中，rdb就是备用的！在从机上面
>
> 面试和工作，持久化必须是重点
>
> Redis是内存数据库，如果不将内存中的数据库状态保存到磁盘，那么一旦服务器进程退出，服务器中的数据库状态也会消失。所以redis提供了持久化功能
>
> 在==指定时间间隔==内将内存中的数据集写入磁盘，也就是Snapshot快照，==它恢复时是将文件直接读到内存里==
>
> Redis会单独创建（fork）一个子进程来进行持久化，会先将数据写入到一个临时rdb文件中，待持久化过程都结束了。再用这个临时文件替换上次持久化好的文件。==整个过程中，主进程是不进行任何IO操作的==，这就确保了极高的性能。如果需要大规模数据的恢复，且==对于数据恢复的完整性不是非常敏感==，那RDB方式要比AOF更加高效，RDB的缺点是最后一次持久化后的数据可能丢失。我们默认的就是RDB，一般情况下不需要修改这个配置。

> RDB保存的文件是dump.rdb都是在我们配置文件中的快照中进行配置的

![image-20211229092430352](redis.assets\image-20211229092430352.png)

<u>***触发持久化的规则：***</u>

> ==配置的save规则满足==
>
> ==执行flushall或者shutdown==

**<u>*恢复rdb文件*</u>**

> 启动redis服务时会去扫描这个dump.rdb文件（redis-server同级目录）

<u>***优点***</u>

- 适合大规模数据的恢复
- 对于数据恢复的完整性不是非常敏感（最后一次修改的数据可能会丢失）

*<u>**缺点**</u>*

- 最后一次修改的数据可能会丢失
- fork一个子进程，占用一定资源

## aof（append ONLY file）

==将所有命令都记录下来，history，恢复的时候把这个文件全部再执行一遍==
以日志的形式来记录每个==写==的操作，将Redis执行过的所有指令记录下来（==读操作不记录==），只许追加文件，但是不可以改写文件，redis启动之初会读取该文件重新构建数据，换言之，redis重启的话就根据日志文件的内容将写指令从签到后执行一次以完成数据的恢复工作
AOF保存的是appendonly.aof文件
默认是不开启的，我们需要手动进行配置，我们只需要将appendonly改为yes即可
重启redis就可以生效了
如果这个aof文件有错位，这时候redis是启动不起来的，我们需要修复这个aof文件
==redis给我们提供了redis-check-aof --fix appendonly.aof来进行appendonly.aof的修复==
如果文件正常，重启就可以直接恢复了

![img](https://img-blog.csdnimg.cn/img_convert/3eb0e5a9501dfa3228e646cb18d34e44.png)

## aof配置

> 包括是否开启，保存的文件name

![image-20211229094127105](redis.assets\image-20211229094127105.png)

***<u>优点和缺点</u>***

**优点：**

1. 每一次修改都同步，文件完整性更加好
2. 每同步一次，可能会丢失一秒的数据
3. 从不同步

**缺点：**

1. 相对于数据文件来说，aof远远大于rdb，修复速度也比rdb慢
2. AOF运行效率也要比rdb慢，redis默认的配置就是rdb持久

```
扩展：
Redis 持久化
Redis 提供了不同级别的持久化方式:

RDB持久化方式能够在指定的时间间隔能对你的数据进行快照存储.
AOF持久化方式记录每次对服务器写的操作,当服务器重启的时候会重新执行这些命令来恢复原始的数据,AOF命令以redis协议追加保存每次写的操作到文件末尾.Redis还能对AOF文件进行后台重写,使得AOF文件的体积不至于过大.
如果你只希望你的数据在服务器运行的时候存在,你也可以不使用任何持久化方式.
你也可以同时开启两种持久化方式, 在这种情况下, 当redis重启的时候会优先载入AOF文件来恢复原始的数据,因为在通常情况下AOF文件保存的数据集要比RDB文件保存的数据集要完整.
最重要的事情是了解RDB和AOF持久化方式的不同,让我们以RDB持久化方式开始:
RDB的优点
RDB是一个非常紧凑的文件,它保存了某个时间点得数据集,非常适用于数据集的备份,比如你可以在每个小时报保存一下过去24小时内的数据,同时每天保存过去30天的数据,这样即使出了问题你也可以根据需求恢复到不同版本的数据集.

RDB是一个紧凑的单一文件,很方便传送到另一个远端数据中心或者亚马逊的S3（可能加密），非常适用于灾难恢复.

RDB在保存RDB文件时父进程唯一需要做的就是fork出一个子进程,接下来的工作全部由子进程来做，父进程不需要再做其他IO操作，所以RDB持久化方式可以最大化redis的性能.

与AOF相比,在恢复大的数据集的时候，RDB方式会更快一些.

RDB的缺点
如果你希望在redis意外停止工作（例如电源中断）的情况下丢失的数据最少的话，那么RDB不适合你.虽然你可以配置不同的save时间点(例如每隔5分钟并且对数据集有100个写的操作),是Redis要完整的保存整个数据集是一个比较繁重的工作,你通常会每隔5分钟或者更久做一次完整的保存,万一在Redis意外宕机,你可能会丢失几分钟的数据.

RDB 需要经常fork子进程来保存数据集到硬盘上,当数据集比较大的时候,fork的过程是非常耗时的,可能会导致Redis在一些毫秒级内不能响应客户端的请求.如果数据集巨大并且CPU性能不是很好的情况下,这种情况会持续1秒,AOF也需要fork,但是你可以调节重写日志文件的频率来提高数据集的耐久度.

AOF 优点
使用AOF 会让你的Redis更加耐久: 你可以使用不同的fsync策略：无fsync,每秒fsync,每次写的时候fsync.使用默认的每秒fsync策略,Redis的性能依然很好(fsync是由后台线程进行处理的,主线程会尽力处理客户端请求),一旦出现故障，你最多丢失1秒的数据.

AOF文件是一个只进行追加的日志文件,所以不需要写入seek,即使由于某些原因(磁盘空间已满，写的过程中宕机等等)未执行完整的写入命令,你也也可使用redis-check-aof工具修复这些问题.

Redis 可以在 AOF 文件体积变得过大时，自动地在后台对 AOF 进行重写： 重写后的新 AOF 文件包含了恢复当前数据集所需的最小命令集合。 整个重写操作是绝对安全的，因为 Redis 在创建新 AOF 文件的过程中，会继续将命令追加到现有的 AOF 文件里面，即使重写过程中发生停机，现有的 AOF 文件也不会丢失。 而一旦新 AOF 文件创建完毕，Redis 就会从旧 AOF 文件切换到新 AOF 文件，并开始对新 AOF 文件进行追加操作。

AOF 文件有序地保存了对数据库执行的所有写入操作， 这些写入操作以 Redis 协议的格式保存， 因此 AOF 文件的内容非常容易被人读懂， 对文件进行分析（parse）也很轻松。 导出（export） AOF 文件也非常简单： 举个例子， 如果你不小心执行了 FLUSHALL 命令， 但只要 AOF 文件未被重写， 那么只要停止服务器， 移除 AOF 文件末尾的 FLUSHALL 命令， 并重启 Redis ， 就可以将数据集恢复到 FLUSHALL 执行之前的状态。

AOF 缺点
对于相同的数据集来说，AOF 文件的体积通常要大于 RDB 文件的体积。

根据所使用的 fsync 策略，AOF 的速度可能会慢于 RDB 。 在一般情况下， 每秒 fsync 的性能依然非常高， 而关闭 fsync 可以让 AOF 的速度和 RDB 一样快， 即使在高负荷之下也是如此。 不过在处理巨大的写入载入时，RDB 可以提供更有保证的最大延迟时间（latency）
```



# redis发布订阅

消息队列

通信，队列 发送者 订阅者

Redis 发布订阅（pub/sub）是一种消息通信模式：发送者（pub）发送消息，订阅者（sub）接收消息。微信，微博

==Redis客户端可以订阅任意数量的频道==

订阅/发布消息图：

第一个：消息发送者，第二个：频道 第三个：消息订阅者！


<img src="https://img-blog.csdnimg.cn/img_convert/dd6b8ef093cf55e0aa316e0a4f94a200.png" alt="img" style="zoom: 50%;" />



***<u>命令：</u>***

> ==subcribe pattern== [pattern] 订阅一个或多个符合给定模式的频道
>
> pubsub subcommand [argument [argument]] 查看订阅与发布系统状态
>
> ==publish channel message==将信息发送到指定的频道  （消息发送者）
>
> punsubscribe [pattern [pattern]] 退订所有给定模式的频道（订阅者）
>
> subscribe channel [channel 订阅一个或多个频道的信息   （订阅者）
>
> unsubscribe [channel [channel]] 退订指定的频道              （订阅者）
> 

测试：

> subscribe roliyfish roliyfish2   订阅一个或多个频道的信息   （订阅者）

<img src="redis.assets\image-20211229111523255.png" alt="image-20211229111523255" style="zoom: 67%;" />



> 发布消息者：publish roilyfish hello

![image-20211229111747678](redis.assets\image-20211229111747678.png)

> 订阅者自动接收：

<img src="redis.assets\image-20211229111840344.png" alt="image-20211229111840344" style="zoom:67%;" />

## 原理

<img src="redis.assets\image-20211229182823282.png" alt="image-20211229182823282" style="zoom: 67%;" />

Redis是使用C实现的，通过分析Redis源码里的pubsub.c文件，了解发布和订阅机制的底层实现，以此加深对Redis 的理解。Redis通过PUBLISH 、SUBSCRIBE和PSUBSCRIBE等命令实现发布和订阅功能。
微信:
通过SUBSCRIBE命令订阅某频道后，redis-server里维护了一个字典，字典的键就是一个个频道!，而字典的值则是一个链表，链表中保存了所有订阅这个channel的客户端。SUBSCRIBE命令的关键，就是将客户端添加到给定channel的订阅链表中。

通过PUBLSH命令向订阅者发送消息，redis-server会使用给定的频道作为键，在它所维护的channel字典中查找记录了订阅这个频道的所有客户端的链表，遍历这个链表，将消息发布给所有订阅者。

Pub/Sub从字面上理解就是发布(Publish)与订阅( Subscribe )，在Redis中，你可以设定对某一个key值进行消息发布及消息订阅，当一个key值上进行了消息发布后，所有订阅它的客户端都会收到相应的消息。这一功能最明显的用法就是用作实时消息系统，比如普通的即时聊天，群聊等功能。

##  使用场景：
实时消息系统
实时聊天（频道当做聊天室）
订阅关注系统
稍微复杂的场景，消息中间件MQ（）

# redis主从复制

主从复制，是指将一台Redis服务器的数据，复制到其他的Redis服务器。前者称为主节点(master、leader)，后者称为从节点(slave、follower);数据的复制是==单向==的，只能由主节点到从节点。Master以写为主，Slave以读为主。

主从复制的作用主要包括:
1、数据冗余∶主从复制实现了数据的热备份，是持久化之外的一种数据冗余方式。
2、故障恢复∶当主节点出现问题时，可以由从节点提供服务，实现快速的故障恢复;实际上是一种服务的冗余。
3、负载均衡︰在主从复制的基础上，配合读写分离，可以由主节点提供写服务，由从节点提供读服务（即写Redis数据时应用连接主节点，读Redis数据时应用连接从节点），分担服务器负载﹔尤其是在==写少读多的场景==下，通过多个从节点分担读负载，可以大大提高Redis服务器的并发量。
4、高可用（集群）基石︰除了上述作用以外，主从复制还是哨兵和集群能够实施的基础，因此说主从复制是Redis高可用的基础。

一般来说，要将Redis运用于工程项目中，只使用一台Redis是万万不能的(宕机，最少3个），原因如下:
1、从结构上，单个Redis服务器会发生单点故障，并且一台服务器需要处理所有的请求负载，压力较大;
2、从容量上，单个Redis服务器内存容量有限，就算一台Redis服务器内存容量为256G，也不能将所有内存用作Redis存储内存，一般来说，单台Redis最大使用内存不应该超过20G。
电商网站上的商品，一般都是一次上传，无数次浏览的，说专业点也就是"多读少写"。对于这种场景，我们可以使如下这种架构︰
<img src="https://img-blog.csdnimg.cn/img_convert/d8f97ed2519b086a298024d5c7675343.png" alt="img" style="zoom: 80%;" />

主从复制，读写分离！80%情况下都是在进行读操作！减缓服务器压力，架构经常使用！一主二从！

只要在公司中，主从复制就是必须要使用的，因为在真实的项目中，不可能单机使用redis！

## 配置

复制三个配置文件：

<img src="redis.assets\image-20211229115322577.png" alt="image-20211229115322577" style="zoom:80%;" />

只配置从库，不用配置主库

1. 端口号
2. pid
3. 日志名字
4. 备份文件名字 dump.rdb

![image-20211229115829094](redis.assets\image-20211229115829094.png)

启动服务：

> redis-server   redis.conf

查看服务状态：

![image-20211229121150767](redis.assets\image-20211229121150767.png)

查看日志状态：

![image-20211229121224254](redis.assets\image-20211229121224254.png)

执行save命令查看备份状态：

![image-20211229121250760](redis.assets\image-20211229121250760.png)

## 配置主从规则

> info replication   ==默认都是主机（master）==且没有从节点

<img src="redis.assets\image-20211229121443886.png" alt="image-20211229121443886" style="zoom:80%;" />

> 都是主机只用配置从机就好，认大哥
>
> slaveof  host  port
>
> 查看info replication可以查看主机的一些状态信息

![image-20211229124516668](redis.assets\image-20211229124516668.png)

> 再次查看主机（master）

![image-20211229124628637](redis.assets\image-20211229124628637.png)

一主二从就搭建好了

## 配置文件配置主从复制

> 修改reolicaof这一项属性即可（配置后启动他就是一个从机）
>
> 有密码配密码（主机密码）

![image-20211229130045765](redis.assets\image-20211229130045765.png)

***<u>注意点</u>***

> 如果配置了requirepass的化，需要在从机中配置masterauth（pass）配从不配主



> 主机写（可读），从机不可写，只可读

![image-20211229125435859](redis.assets\image-20211229125435859.png)

> 主机down机了，从机正常读只不过连接状态为down，主机再连上来一切正常

> 从机dowm机了，再次连接（其状态为master），命令行再次配置主机数据都可以拿到（全量复制）

### 复制原理

```txt
Slave启动成功连接到master后会发送一个sync命令
Master接到命令，启动后台的存盘进程，同时收集所有接收到的用于修改数据集命令，在后台进程执行完毕之后，master将传送整个数据文件到slave，并完成一次完全同步。
```

1. 全量复制︰而slave服务在接收到数据库文件数据后，将其存盘并加载到内存中。

2. 增量复制:Master继续将新的所有收集到的修改命令依次传给slave，完成同步。

但是只要是重新连接master，一次完全同步(全量复制）将被自动执行。我们的数据一定可以在从机中看到

## 问题

> 主机死了咋办？需要很久才可以连接上来
>
> 这边有两种模式
>
> 一主二从
>
> 层层链路

<img src="redis.assets\image-20211229142419644.png" alt="image-20211229142419644" style="zoom:67%;" />

***解决：***

第一种的话如果主机断了

1、我们需要手动设置==slaveof no one==（将某一个设为主机）

2、将另一个设置为从机

第二种：直接手动设置中间的那一个从机为主机即可。

# 哨兵模式

主从切换的方法是：当主服务器宕机后，需要手动把一台服务器切换为主服务器，这就需要人工干预，费时费力，还会造成一段时间内服务器不可用。这不是一种推荐方式，更多时候，我们优先考虑哨兵模式，Redis从2.8开始正式提供了Sentinel（哨兵）架构来解决这个问题。

能够后台监控主机是否故障，如果故障了根据投票数自动将库转换为主库。

哨兵模式是一种特殊的模式，首先Redis提供了哨兵的命令，哨兵是一个独立的进程，作为进程，它会独立运行。其原理是哨兵通过发送命令（心跳包），等待Redis服务器响应，从而监控运行的多个Redis实例

> 单进程哨兵

<img src="https://img-blog.csdnimg.cn/img_convert/c1ba98cbc27441ca87553f823873fac2.png" alt="image-20201105125043998" style="zoom: 50%;" />

哨兵有两个作用

1、通过发送命令，让Redis服务器返回监控其运行状态，包括主服务器和从服务器
2、当哨兵监测到master宕机，会自动将slave切换成master，然后通过发布订阅模式通知其他的从服务器，修改配置文件，让他们切换主机

> 然而一个哨兵进程对Redis服务器进行监控，可能会出现问题，为此，我们可以使用多个哨兵进行监控。各个哨兵之间还会进行监控，这样就形成了==多哨兵模式==。
>
> 哨兵是一个进程也会宕机

<img src="https://img-blog.csdnimg.cn/img_convert/3cad6f6728ec2c403ae6d074bbc51823.png" alt="img" style="zoom:50%;" />

假设主服务器宕机，哨兵1先检测到这个结果，系统并不会马上进行failover过程，仅仅是哨兵1主观的认为主服务器不可用，这个现象成为主观下线。当后面的哨兵也检测到主服务器不可用，并且数量达到一定值时，那么哨兵之间就会进行一次投票，投票的结果由一个哨兵发起，进行==failover[故障转移]==操作。切换成功后，就会通过发布订阅模式，让各个哨兵把自己监控的从服务器实现切换主机，这个过程称为客观下线。

## 测试

> 配置sentinel.config

```bash
sentinel monitor myredis 127.0.0.1 6379 1
#哨兵监控  127.0.0.1 6379
sentinel auth-pass myredis roilyfish
#这里如果主机设置了密码需要配上（不然failover）
```

> 启动服务redis-sentinel  sentinel.conf
>
> 有一些信息：
>
> 哨兵的端口号：26379
>
> 哨兵的id
>
> 监控的主机（master）：127..0.0.1 6379
>
> 以及主机下的从机（slave）：6380 6381

<img src="redis.assets\image-20211229154833841.png" alt="image-20211229154833841" style="zoom:80%;" />

> 测试将主机宕机shutdown
>
> 查看哨兵进程：
>
> 大概意思就是
>
> 1、主机宕机了
>
> 2、我试了很多次他不理我
>
> 3、我再等一会
>
> 4、忍不了了选下一个，投个票
>
> 最后：
>
>  # +switch-master myredis 127.0.0.1 6379 127.0.0.1 6380

```bash
26546:X 29 Dec 2021 15:44:34.189 # +sdown master myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:34.189 # +odown master myredis 127.0.0.1 6379 #quorum 1/1
26546:X 29 Dec 2021 15:44:34.189 # +new-epoch 1
26546:X 29 Dec 2021 15:44:34.189 # +try-failover master myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:34.192 # +vote-for-leader efe9c21b86931e1b7134430804523cdb589b67c6 1
26546:X 29 Dec 2021 15:44:34.192 # +elected-leader master myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:34.192 # +failover-state-select-slave master myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:34.269 # +selected-slave slave 127.0.0.1:6380 127.0.0.1 6380 @ myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:34.269 * +failover-state-send-slaveof-noone slave 127.0.0.1:6380 127.0.0.1 6380 @ myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:34.321 * +failover-state-wait-promotion slave 127.0.0.1:6380 127.0.0.1 6380 @ myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:34.842 # +promoted-slave slave 127.0.0.1:6380 127.0.0.1 6380 @ myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:34.842 # +failover-state-reconf-slaves master myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:34.916 * +slave-reconf-sent slave 127.0.0.1:6381 127.0.0.1 6381 @ myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:35.867 * +slave-reconf-inprog slave 127.0.0.1:6381 127.0.0.1 6381 @ myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:35.867 * +slave-reconf-done slave 127.0.0.1:6381 127.0.0.1 6381 @ myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:35.958 # +failover-end master myredis 127.0.0.1 6379
26546:X 29 Dec 2021 15:44:35.958 # +switch-master myredis 127.0.0.1 6379 127.0.0.1 6380
26546:X 29 Dec 2021 15:44:35.958 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ myredis 127.0.0.1 6380
26546:X 29 Dec 2021 15:44:35.958 * +slave slave 127.0.0.1:6379 127.0.0.1 6379 @ myredis 127.0.0.1 6380
26546:X 29 Dec 2021 15:45:06.014 # +sdown slave 127.0.0.1:6379 127.0.0.1 6379 @ myredis 127.0.0.1 6380

```

> 我们再看一次sentinel的配置文件
>
> 他的监控端口变了
>
> 他会默认关闭我们的安全连接

![image-20211229155625529](redis.assets\image-20211229155625529.png)

> 测试将我们宕机的服务重连回来
>
> info replication查看其主从关系:他是一个从机了。。。。老大是别人了
>
> 其实在上面刚宕机的时候就有提示

```bash
#选取老大  80
26546:X 29 Dec 2021 15:44:35.958 # +switch-master myredis 127.0.0.1 6379 127.0.0.1 6380
#监控他的从机  81  79
26546:X 29 Dec 2021 15:44:35.958 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ myredis 127.0.0.1 6380
26546:X 29 Dec 2021 15:44:35.958 * +slave slave 127.0.0.1:6379 127.0.0.1 6379 @ myredis 127.0.0.1 6380
#79监控不到
26546:X 29 Dec 2021 15:45:06.014 # +sdown slave 127.0.0.1:6379 127.0.0.1 6379 @ myredis 127.0.0.1 6380
```



```bash
127.0.0.1:6379> info replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6380
master_link_status:up
```

## 优缺点

如果主机此时回来了，只能归并到新的主机下，当做从机，这就是哨兵模式的规则！

***优点：***

1、哨兵模式，基于主从复制模式，所有主从配置的优点它都有
2、主从可以切换，故障可以转移，系统的可用性就会更好
3、哨兵模式就是主从模式的升级，手动到自动，更加健壮
***缺点：***

1、Redis不好在线扩容，集群容量一旦达到上限，在线扩容就十分麻烦
2、实现哨兵模式的配置是非常麻烦的，里面有很多选择
