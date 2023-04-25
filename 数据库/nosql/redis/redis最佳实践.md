## Redis最佳实践



### redis键值设计

#### 优雅的Key设计

Redis的Key虽然可以自定义，但最好遵循下面的几个最佳实践约定：

- 遵循基本格式：[业务名称]:[数据名]:[id]
- 长度不超过44字节
- 不包含特殊字符

优点：

- 可读性强
- 避免key冲突
- 方便管理 (冒号分组,方便查看,方便删除)
- 更节省内存： key是string类型，底层编码包含int、embstr和raw三种。embstr在小于44字节使用，采用连续内存空间，内存占用更小。当字节数大于44字节时，会转为raw模式存储，在raw模式下，内存空间不是连续的，而是采用一个指针指向了另外一段内存空间，在这段空间里存储SDS内容，这样空间不连续，访问的时候性能也就会收到影响，还有可能产生内存碎片

![image-20230423234840445](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230423234840445.png)

#### 拒绝BigKey

##### 什么是BigKey

BigKey通常以Key的大小和Key中成员的数量来综合判定，例如：

- Key本身的数据量过大：一个String类型的Key，它的值为5 MB
- Key中的成员数过多：一个ZSET类型的Key，它的成员数量为10,000个
- Key中成员的数据量过大：一个Hash类型的Key，它的成员数量虽然只有1,000个但这些成员的Value（值）总大小为100 MB



##### 如何判断BigKey

- MEMORY USAGE KEY

  > 查看指定KEY及其VALUE内存占用大小
  >
  > 不推荐使用此命令,此命令CPU占用率较高,可能阻塞主进程,导致Redis性能下降
  >
  > 只需要衡量value值大小,或者集合元素个数即可

- STRLEN KEY

  > 查看STRING类型长度。例：name --->  4

- LLEN

  > 查看LIST类型元素个数。例: {a,b,c} ---> 3

- hlen

  > 查看hash结构指定key的field个数

- scard  & zcard

  > 查看sed或sortedset中成员个数

- 推荐值

  > 单个key的value值小于10k
  >
  > 对于集合类型的key,元素个数小于1000

##### BigKey危害

- 网络阻塞
  - 对BigKey执行读请求时，少量的QPS就可能导致带宽使用率被占满，导致Redis实例，乃至所在物理机变慢
- 数据倾斜
  - BigKey所在的Redis实例内存使用率远超其他实例，无法使数据分片的内存资源达到均衡
- Redis阻塞
  - 对元素较多的hash、list、zset等做运算会耗时较旧，使主线程被阻塞
- CPU压力
  - 对BigKey的数据序列化和反序列化会导致CPU的使用率飙升，影响Redis**实例和本机其它应用**



##### 如何发现BigKey

- 通过`redis-cli -a pass --bigkeys`命令分析bigkey

  > 此命令会分析出此Redis实例中所有数据类型的bigkey,不完善

  ![image-20230424085747182](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230424085747182.png)

- scan扫描

  > 自己编程，利用scan扫描Redis中的所有key，利用strlen、hlen等命令判断key的长度（此处不建议使用MEMORY USAGE）

  ```shell
  ## SCAN cursor [MATCH pattern] [COUNT count] [TYPE type] scan默认每次扫描10个key， 可以指定游标的扫描个数,sacn  cursor Count count
  127.0.0.1:6379> scan 0
  1) "1"
  2)  1) "item:id:10004"
      2) "item:stock:id:10001"
      3) "item:id:10001"
      4) "stream1"
      5) "item:stock:id:10101"
      6) "h1"
      7) "item:id:10005"
      8) "item:stock:id:11101"
      9) "item:id:10002"
     10) "item:stock:id:10005"
     11) "item:id:10003"
  ```

  > 使用scan扫描所有key,scan返回结果参数：
  >
  > - 第一个是游标位置,继续基于此游标扫描,当返回游标为0表示扫描结束。
  > - 第二个是list key集合

  ```java
  public class JedisTest {
      private Jedis jedis;
      final Logger logger = LoggerFactory.getLogger(getClass());
      final static int STR_MAX_LEN = 10 * 1024;
      final static int HASH_MAX_LEN = 500/*3*/;
      @BeforeEach
      private void init() {
          jedis = new Jedis("10.211.55.4", 6380);
          jedis.auth("123123");
      }
      @Test
      public void test() {
          final Set<String> keys = jedis.keys("*");
          logger.info("keys * : {}", keys);
      }
      @Test
      void testScan() {
          int maxLen = 0;
          long len = 0;
          String cursor = "0";
          do {
              // 扫描并获取一部分key
              ScanResult<String> result = jedis.scan(cursor);
              // 记录cursor
              cursor = result.getCursor();
              List<String> list = result.getResult();
              if (list == null || list.isEmpty()) {
                  break;
              }
              // 遍历
              for (String key : list) {
                  // 判断key的类型
                  String type = jedis.type(key);
                  switch (type) {
                      case "string":
                          len = jedis.strlen(key);
                          maxLen = STR_MAX_LEN;
                          break;
                      case "hash":
                          len = jedis.hlen(key);
                          maxLen = HASH_MAX_LEN;
                          break;
                      case "list":
                          len = jedis.llen(key);
                          maxLen = HASH_MAX_LEN;
                          break;
                      case "set":
                          len = jedis.scard(key);
                          maxLen = HASH_MAX_LEN;
                          break;
                      case "zset":
                          len = jedis.zcard(key);
                          maxLen = HASH_MAX_LEN;
                          break;
                      default:
                          break;
                  }
                  if (len >= maxLen) {
                      logger.info("Found big key : {}, type:{} , length or size: {}", key, type, len);
                  }
              }
          } while (!cursor.equals("0"));
      }
      @AfterEach
      private void resolveJedis() {
          jedis.close();
      }
  }
  ```

- 第三方工具

  > 利用第三方工具，如 Redis-Rdb-Tools 分析RDB快照文件，全面分析内存使用情况
  >
  > [redis-rdb-tools](https://github.com/sripathikrishnan/redis-rdb-tools)  tip:有版本问题,更新不频繁,不好用

- 网络监控

  > 使用大厂平台,阿里云的云平台



##### 如何删除BigKey

> BigKey由于占用内存较大,不可使用del命令删除.避免主线程阻塞。

- redis3.0版本之前

  > 如果是集合类型,可以遍历BigKey,先删除子成员,最后再删除BigKey

  ![image-20230424131649170](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230424131649170.png)

- redis4.0之后

  > 异步删除命令:unlink。
  >
  > 和del不同的是此命令不会阻塞主线程,而是首先取消键与键所在内存空间之间的连接,后续开启线程异步删除。



#### 恰当的数据类型

> 选择合适的数据类型,兼顾①空间利用率,避免bigkey ② 方便操作



##### 例一

> 存储一个user对象有三种存储方式

1. JSON字符串存储
   - 优点：实现简单
   - 缺点：数据耦合,不方便操作。(比如更新user的name属性,则需要覆盖整个user数据)
2. 数据分层。通过冒号的形式将user属性作为redis的key
   - 缺点： 浪费内存、占用空间较大
   - 优点：可灵活操作user各个属性
3. hash存储， user属性作为hash的field
   - 优点：底层使用ziplist,占用空间小、可灵活操作user各个属性
   - 缺点：客户端需要做属性映射

##### 例二

> 假如有hash类型的key，其中有100万对field和value，field是自增id，这个key存在什么问题？如何优化？

存在问题：

- hash的entry数量超过500,底层不再使用ziplist而是使用哈希表,内存占用较多
- 可通过[redis官方内存优化配置](https://redis.io/docs/management/optimization/memory-optimization/),配置ziplist的entries上限。通过`config get hash-max-ziplist-entries`命令查看(不同版本有区别)



> 放入500个entry到hash结构,查看其类型和底层编码类型

```java
@Test
void testSetBigKey() {
    Map<String, String> map = new HashMap<>();
    for (int i = 1; i <= 500; i++) {
        map.put("key" + i, "value" + i);
    }
    jedis.hmset("hash:total:500", map);
}
```

![image-20230424141826460](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230424141826460.png)

> 将hash结构内部数量提升至 1000。
>
> 底层数据结构不再是压缩表,而是哈希表。数据量提升一倍,但是内存结构确成倍增长

```java
@Test
void testSetBigKey1000() {
    Map<String, String> map = new HashMap<>();
    for (int i = 1; i <= 1000; i++) {
        map.put("key" + i, "value" + i);
    }
    jedis.hmset("hash:total:1000", map);
}
```

![image-20230424141952505](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230424141952505.png)

> 插入100_100_0entry到哈希结构中,查看内存占用：

```java
@Test
void testSetBigKey100_000_0() {
    Map<String, String> map = new HashMap<>();
    for (int i = 1; i <= 100_000_0; i++) {
        map.put("key" + i, "value" + i);
    }
    jedis.hmset("hash:total:100_000_0", map);
}
```

![image-20230424142725603](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230424142725603.png)

###### 方案一

> 避免BigKey,将hash结构拆分为String存储。

```java
@Test
void testBigString() {
    for (int i = 1; i <= 100_000_0; i++) {
        jedis.set("str:total:100_000_0:" + i, "value" + i);
    }
}
```

此例执行很慢,因为有一百万次网络io

![image-20230424143040588](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230424143040588.png)

总内存占用155M,155-63 = 92M,内存占用不如hash,并且不好管理,现在执行keys `*`是非常恐怖的![image-20230424143217214](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230424143217214.png)



###### 方案二

> 拆分哈希,将大哈希结构拆分为多个小哈希结构。
>
> 这样既利用了哈希会优化底层存储,并且不至于key数量过多

```java
@Test
void testSmallHash() {
    int hashSize = 500;
    // 初始化避免频繁扩容
    Map<String, String> map = new HashMap<>(hashSize);
    for (int i = 1; i <= 100_000_0; i++) {
        // 取整作为key
        int k = (i - 1) / hashSize;
        // 取余作为field
        int v = i % hashSize;
        map.put("key_" + v, "value_" + v);
        if (v == 0) {
            jedis.hmset("test:small:hash_" + k, map);
        }
    }
}
```



首先执行耗时不高了因为(100_000_0/500) = 2000,只有两千次网络io

![image-20230424143739706](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230424143739706.png)

```shell
keys test:small:hash_*
# 返回2000个key
```

总内存占用174M,174-155 = 19,最优解

![image-20230424144248065](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230424144248065.png)

#### 总结

- Key的最佳实践
  - 固定格式：[业务名]:[数据名]:[id]
  - 足够简短：不超过44字节
  - 不包含特殊字符
- Value的最佳实践：
  - 合理的拆分数据，拒绝BigKey
  - 选择合适数据结构
  - Hash结构的entry数量不要超过1000
  - 设置合理的超时时间



### 批处理优化

> redis与客户端的交互延时主要包括：网络延时 + redis命令执行耗时,其中redis命令执行耗时较短,是微妙级别的,而网络延时是毫秒级别的。所以在交互时需要尽量减少redis与客户端的网络io次数。

#### redis自带的批处理命令

> redis提供许多`M*`这样的命令,可以实现批量插入数据

- mset
- hmset
- zadd
- sadd
- Lpush、rpush

使用mset命令,优于使用set命令循环插入数据：

```java
/**
 * 循环插入数据,多次网络io
 */
@Test
public void testForSet() {
    for (int i = 0; i < 100_000_0; i++) {
        jedis.set("forset:" + i, "value_" + i);
    }
}

/**
 * 利用mset减少网络io
 */
@Test
public void testMSet() {
    // 每次插入 1000个k-v
    String[] kvBf = new String[2000];
    for (int i = 0; i < 100_000_0; i += 2) {
        // 0-1998
        int keyIndex = i % 2000;
        int valueIndex = keyIndex + 1;
        kvBf[keyIndex] = "mset:key_" + i / 2;
        kvBf[valueIndex] = "mset_value_" + i / 2;
        if (valueIndex == 1999) {
            jedis.mset(kvBf); 
        }
    }
}
```



#### Pipeline

> 对于简单且数据类型单一的操作,可以使用Redis提供的批处理命令。但是对于复杂的操作,可以使用Pipelline。

比如一次批处理涉及多条命令、多种数据类型。

```java
@Test
public void testPipelined() {
    // 创建管道
    Pipeline pipelined = jedis.pipelined();
    for (int i = 1; i <= 100_000_0; i++) {
        pipelined.set("pipelined:key:" + i, "value_" + i);
        pipelined.sadd("pipelined:smembers:" + i, "member" + i);
        if (i % 1000 == 0) {
            // 每1000次循环异步执行一次
            pipelined.sync();
        }
    }
}
```



#### 集群下的批处理

> 在redis集群模式下,批处理处理的多个key要求在同一个插槽内,否则就会执行失败。

例如：

![image-20230425015051448](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425015051448.png)



##### 处理方式

- 串行化执行命令

  > for循环遍历依次执行插入命令。实现简单,但是耗时较久。

- 串行slot

  > 在Redis客户端对批处理的key做分组,相同的slot分到同一个组,分别执行批处理。
  >
  > 串行化执行,也存在多次网络io,如果slot很多,性能也不一定得行

- 并行slot

  > 和串行slot一样首先根据slot值对key进行分组。
  >
  > 并行执行批处理

- hash_tag

  > redis在计算slot值时,只计算有效部分,我们可以为批处理命令的key设置有效部分,使得这些key落在同一个插槽内。
  >
  > 这样的话会有数据倾斜的问题



##### 串行化slot

```java
@BeforeEach
public void init() {
    // 配置连接池
    final GenericObjectPoolConfig<Connection> config = new GenericObjectPoolConfig<>();
    config.setMaxTotal(8);
    config.setMaxIdle(8);
    config.setMinIdle(0);
    config.setMaxWaitMillis(1000);
    HashSet<HostAndPort> nodes = new HashSet<>();
    nodes.add(new HostAndPort("10.211.55.4", 7001));
    nodes.add(new HostAndPort("10.211.55.4", 7002));
    nodes.add(new HostAndPort("10.211.55.4", 7003));
    nodes.add(new HostAndPort("10.211.55.4", 8001));
    nodes.add(new HostAndPort("10.211.55.4", 8002));
    nodes.add(new HostAndPort("10.211.55.4", 8003));
    jedisCluster = new JedisCluster(nodes, config);
}

@Test
void testMSet2() {
    Map<String, String> map = new HashMap<>(3);
    map.put("name", "Jack");
    map.put("age", "21");
    map.put("sex", "Male");
    // 对Map数据进行分组。根据相同的slot放在一个分组
    // key就是slot，value就是一个组
    Map<Integer, List<Map.Entry<String, String>>> result = map.entrySet()
            .stream()
            .collect(Collectors.groupingBy(
                    entry -> ClusterSlotHashUtil.calculateSlot(entry.getKey()))
            );
    // 串行的去执行mset的逻辑
    for (List<Map.Entry<String, String>> list : result.values()) {
        String[] arr = new String[list.size() * 2];
        int j = 0;
        for (int i = 0; i < list.size(); i++) {
            j = i << 2;
            Map.Entry<String, String> e = list.get(i);
            arr[j] = e.getKey();
            arr[j + 1] = e.getValue();
        }
        jedisCluster.mset(arr);
    }
}
```

##### Spring下异步slot

```java
@Autowired
private StringRedisTemplate redisTemplate;

@Test
public void test() {
    Map<String, String> map = new HashMap<>(3);
    map.put("name", "Rose");
    map.put("age", "21");
    map.put("sex", "Female");
    redisTemplate.opsForValue().multiSet(map);
    List<String> strings = redisTemplate.opsForValue().multiGet(Arrays.asList("name", "age", "sex"));
    strings.forEach(System.out::println);
}
```

原理：

1. 方法调用栈

   ![image-20230425023856397](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425023856397.png)

2. 原理

   在RedisAdvancedClusterAsyncCommandsImpl 类中

   - 首先`SlotHash.*partition*(codec, *map*.keySet());`方法得到一个`map<Integer,List<K>>`,此map的key是slot值,value为slot对应redis的key。

   - 通过 RedisFuture<String> mset = super.mset(op);进行异步的消息发送
   - 最后通过`MultiNodeExecution.*firstOfAsync*(*executions*);`保证所有的future都完成



### 服务器端优化

#### 持久化配置

Redis的持久化虽然可以保证数据安全，但也会带来很多额外的开销，因此持久化请遵循下列建议：

* 用来做缓存的Redis实例尽量不要开启持久化功能。数据安全要求不高
* 建议关闭RDB持久化功能，使用AOF持久化
* 利用脚本定期在slave节点做RDB，实现数据备份
* 设置合理的rewrite阈值，避免频繁的bgrewrite
* 配置no-appendfsync-on-rewrite = yes，禁止在rewrite期间做aof，避免因AOF引起的阻塞
* 部署有关建议：
  * Redis实例的物理机要预留足够内存，应对fork和rewrite
  * 单个Redis实例内存上限不要太大，例如4G或8G。可以加快fork的速度、减少主从同步、数据迁移压力
  * 不要与CPU密集型应用部署在一起
  * 不要与高硬盘负载应用一起部署。例如：数据库、消息队列

#### 慢查询优化

##### 什么是慢查询

Redis中的慢查询指的是执行时耗时超过某个阈值的命令，称为慢查询。

慢查询的危害：由于Redis是单线程的，客户端发出的指令会进入到redis底层的queue来执行，如果此时有一些慢查询的数据，就会导致大量请求阻塞，从而引起报错，所以我们需要解决慢查询问题。

> 慢查询阀值：默认10000,单位微秒。redis是微秒级别的可以调为1000
>
> 慢查询日志长度：默认128,改成1000

可以通过`config get slowlog-log-slower-than`查看慢查询阀值

可以通过`config get slowlog-max-len`查看慢查询日志最大长度

```shell
127.0.0.1:6379> config get slowlog-log-slower-than
1) "slowlog-log-slower-than"
2) "10000"
127.0.0.1:6379> config get slowlog-max-len
1) "slowlog-max-len"
2) "128"
```

也可以查看redis配置：

![image-20230425123712571](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425123712571.png)

修改配置：

```shell
127.0.0.1:6379> config set slowlog-log-slower-than 1000
OK
127.0.0.1:6379> config get slowlog-log-slower-than 1000
1) "slowlog-log-slower-than"
2) "1000"
127.0.0.1:6379> config set slowlog-max-len 1000
OK
127.0.0.1:6379> config get slowlog-max-len 1000
1) "slowlog-max-len"
2) "1000"
```



##### 查看慢查询日志

相关命令

* slowlog len：查询慢查询日志长度
* slowlog get count：读取头部慢查询信息。默认10，-1表示全部
* slowlog reset：清空慢查询列表

![image-20230425124836182](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425124836182.png)

#### 命令及安全配置

> 登录linux服务器,除了通过账号密码登录,还可通过ssh公私秘钥认证的方式登录,只需将公钥放入linux,就可以通过手中的私钥登录linux。而redis配置的不好的话,很容易将公钥放入linux。

[redis漏洞](https://cloud.tencent.com/developer/article/1039000)

> 造成此漏洞的原因：

- bind配置为0.0.0.0:6379,这会导致我们的redis暴露在公网上
- 密码未设置或设置的太简单
- 利用redis的 config set命令,修改redis配置 (比如持久化配置,将redis持久化到ssh目录下)
- 使用root账号启动redis

> 解决方式

1. bind设置：限制网卡,设置两个网卡,一个内网网卡,一个外网网卡,禁止外网网卡访问redis

2. 修改redis默认端口

3. redis设置较为复杂的密码。(redis响应非常快,避免暴力破解)

4. 禁止使用以下命令：`keys、flushdb、flushall、config set`

   > 可以利用`rename-command`配置对命令重命名或禁用。

   ![image-20230425133025705](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425133025705.png)

   ![image-20230425133218013](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425133218013.png)

5. 开启防火墙

6. 不使用root账号启动redis

#### 内存配置

当Redis内存不足时，可能导致Key频繁被删除、响应时间变长、QPS不稳定等问题。当内存使用率达到90%以上时就需要我们警惕，并快速定位到内存占用的原因。

- **有关碎片问题分析**

  Redis底层分配并不是这个key有多大，他就会分配多大，而是有他自己的分配策略，比如8,16,20等等，假定当前key只需要10个字节，此时分配8肯定不够，那么他就会分配16个字节，多出来的6个字节就不能被使用，这就是我们常说的 碎片问题

- **进程内存问题分析：**

  这片内存，通常我们都可以忽略不计

- **缓冲区内存问题分析：**

  一般包括客户端缓冲区、AOF缓冲区、复制缓冲区等。客户端缓冲区又包括输入缓冲区和输出缓冲区两种。这部分内存占用波动较大，所以这片内存也是我们需要重点分析的内存问题。

| **内存占用** | **说明**                                                     |
| ------------ | ------------------------------------------------------------ |
| 数据内存     | 是Redis最主要的部分，存储Redis的键值信息。主要问题是BigKey问题、内存碎片问题（重启清清除碎片、应该批量重启） |
| 进程内存     | Redis主进程本身运⾏肯定需要占⽤内存，如代码、常量池等等；这部分内存⼤约⼏兆，在⼤多数⽣产环境中与Redis数据占⽤的内存相⽐可以忽略。 |
| 缓冲区内存   | 一般包括客户端缓冲区、AOF缓冲区、复制缓冲区等。客户端缓冲区又包括输入缓冲区和输出缓冲区两种。这部分内存占用波动较大，不当使用BigKey，可能导致内存溢出。 |

于是我们就需要通过一些命令，可以查看到Redis目前的内存分配状态：

- info memory：查看内存分配的情况

- memory xxx：查看key的主要占用情况

  [memory stats官网说明](https://redis.io/commands/memory-stats/)

  ```shell
  ## 内存诊断
  MEMORY DOCTOR 
  summary: Outputs memory problems report
  ## 帮助
  MEMORY HELP 
  ## 请求分配器释放内存
  MEMORY PURGE 
  ## 内存状态,和info memory显示信息存在重复
  MEMORY STATS 
  ## 查看某个key的内促使用情况
  MEMORY USAGE key [SAMPLES count]
  ```

##### 缓冲区

> 数据内存需要注意bigkey问题、已经合理分配业务。
>
> 进程内存是必须的,所占内存不多,无需优化
>
> 缓冲区是需要注意的内存空间,当出现内存问题如何定位：

内存缓冲区常见的有三种：

* 复制缓冲区：主从复制的repl_backlog_buf，如果太小可能导致频繁的全量复制，影响性能。通过` repl-backlog-size 1mb`来设置，默认1mb

* AOF缓冲区：AOF刷盘之前的缓存区域，AOF执行rewrite的缓冲区。无法设置容量上限

* 客户端缓冲区：分为输入缓冲区和输出缓冲区，输入缓冲区最大1G且不能设置。输出缓冲区可以设置

  ```shell
  # 客户端输出缓冲区大小
  client-output-buffer-limit <class> <hard limit> <soft limit> <soft seconds>
  ```

  客户端分为：

  - normal：普通客户端比如我们的Java客户端
  - replica：主从同步时,会创建这种客户端
  - pubsub: 通知订阅模型会创建这种客户端

  默认配置如下：

  ```shell
  # Both the hard or the soft limit can be disabled by setting them to zero.
  client-output-buffer-limit normal 0 0 0
  client-output-buffer-limit replica 256mb 64mb 60
  client-output-buffer-limit pubsub 32mb 8mb 60
  ```

  

### 集群还是主从

> 单机redis的QPS已经可以达到万级别,配合哨兵模式可以进一步提升性能,所以在非必要情况下优先选择哨兵模式的主从。

#### 集群的问题

- 集群完整性

  > 在redis默认配置中,当集群中某个节点宕机了以后(某片插槽不可用),则整个集群不可用。当然可以配置,但依然存在集群完整性问题

- 集群带宽问题

  > 由于集群中没有哨兵机制,集群中的所有redis实例需要通过互相ping来监测健康状态,也会占用带宽,如果redis集群实例过多的话,会占满带宽,导致性能下降

- 数据倾斜问题

  > 当许多bigkey放入同一片插槽中的话,会导致数据倾斜问题

- 客户端性能问题

  > 由于在执行命令前需要确认key的插槽值,所以不得不在执行命令之前对key做插槽值的运算

- 命令兼容问题

  > redis自带的批处理命令,mset、hmset等,是原子性的,集群环境下很容易报错

- lua和事务问题

  > lua脚本其实也是批处理的一种,他可以保证redis命令的原子性,也会在集群环境下报错



##### 集群完整性问题

> 在Redis的默认配置中，如果发现任意一个插槽不可用，则整个集群都会停止对外服务

![image-20230425204339904](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425204339904.png)



###### 测试一下：

![image-20230425212053528](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425212053528.png)

> 8003的master节点是7001,尝试关闭这两个节点,测试集群是否可用(此刻的`cluster-require-full-coverage`默认配置是yes)

`printf "%d\n" 7001 8003 | xargs -I{} -t redis-cli -p {} shutdown`

7001和8003两个节点都停掉了：

![image-20230425212405003](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425212405003.png)

整个集群不可用：

![image-20230425212536694](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425212536694.png)

> 修改`cluster-require-full-coverage`配置为no,再测试一下

`cluster-require-full-coverage no`

重新启动的集群节点信息为:![image-20230425213537293](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425213537293.png)

停止一下7001和8001这两个节点

`printf "%d\n" 7001 8001 | xargs -I{} -t redis-cli -p {} shutdown`

7001和8001这两个节点宕机了,整个集群还是可以对外提供服务,但是0-5460这片插槽不可用,redis集群完整性缺失

![image-20230425214013474](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425214013474.png)



##### 带宽问题

集群节点之间会不断的互相Ping来确定集群中其它节点的状态。每次Ping携带的信息至少包括：

* 插槽信息
* 集群状态信息

集群中节点越多，集群状态信息数据量也越大，10个节点的相关信息可能达到1kb，此时每次集群互通需要的带宽会非常高，这样会导致集群中大量的带宽都会被ping信息所占用，这是一个非常可怕的问题，所以我们需要去解决这样的问题

**解决途径：**

* 避免大集群，集群节点数不要太多，最好少于1000，如果业务庞大，则建立多个集群。
* 避免在单个物理机中运行太多Redis实例
* 配置合适的cluster-node-timeout值



##### 命令兼容性问题

> redis单此执行命令的key必须在同一个插槽中,redis批处理key时,不能保证此点,比如mset命令、lau脚本等。

![image-20230425221043758](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_best_usingway/image-20230425221043758.png)



##### 选集群还是主从

> 单体Redis（主从Redis）已经能达到万级别的QPS，并且也具备很强的高可用特性。如果主从能满足业务需求的情况下，所以如果不是在万不得已的情况下，尽量不搭建Redis集群
>
> 主从redis可以配合哨兵模式满足redis的可用性。







