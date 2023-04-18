# 原理

> 阿里云OSS路径：`nosql/redis/redis_high/`

## 分布式缓存

> 分布式缓存--分布式(微服务)环境下的缓存技术,如果缓存是单机的压力会很大。
>
> 基于Redis集群解决单机Redis存在的问题。

单机Redis存在的四个问题：

- 数据丢失问题

  > 实现数据持久化

- 并发能力问题

  > 搭建主从、集群,实现读写分离

- 故障恢复问题

  > Redis哨兵模式,实现将康监测和自动恢复

- 存储能力问题

  > 搭建分片集群,利用插槽机制实现动态扩容

### Redis持久化

Redis有两种持久化方案：

- RDB
- AOF

#### RDB

> RDB-Redis Database Backup File(Redis数据备份文件),也称作Redis数据快照。

RDB相关配置：

```shell
# If you want to save some CPU in the saving child set it to 'no' but
# 压缩rdb文件,默认开启,如果想要节省cpu资源,可以关闭
rdbcompression yes
# dump 文件名称 （在工作文件夹根路径）
dbfilename dump.rdb
# 工作路径
dir ./data/
# rdb策略
save 30 1 20 2 10 3
```

##### RDB执行时机

- 客户端执行save命令
- 客户端执行bgsave(background save)命令
- Redis停机
- Redis配置文件中配置的触发RDB规则

###### 命令

> 同步地保存数据到磁盘
>
> save命令由Redis主进程执行,会阻塞所有命令

```she
127.0.0.1:6379> help save
  SAVE -
  summary: Synchronously save the dataset to disk
  since: 1.0.0
  group: server
```

例子：

```shell
127.0.0.1:6379> get name
"yuyc"
127.0.0.1:6379> save
OK
```

会输出如下日志,并且下次启动redis时会去读取此快照文件

```tex
16995:M 17 Apr 2023 02:43:39.294 * Ready to accept connections
16995:M 17 Apr 2023 02:44:07.098 * DB saved on disk
```



> 异步地保存数据到磁盘
>
> bgsave命令由子进程执行,主进程不受影响

```shell
127.0.0.1:6379> help bgsave
  BGSAVE [SCHEDULE]
  summary: Asynchronously save the dataset to disk
  since: 1.0.0
  group: server
```

例子:

```shell
127.0.0.1:6379> set nane yuyc2
OK
127.0.0.1:6379> bgsave 
Background saving started
```

Redis会输出如下日志

```tex
16995:M 17 Apr 2023 02:48:44.121 * Background saving started by pid 17833
17833:C 17 Apr 2023 02:48:44.125 * DB saved on disk
17833:C 17 Apr 2023 02:48:44.125 * Fork CoW for RDB: current 0 MB, peak 0 MB, average 0 MB
16995:M 17 Apr 2023 02:48:44.194 * Background saving terminated with success
```



###### 停机时

当Redis正常关闭时,会输出如下日志：

```tex
16995:signal-handler (1681671015) Received SIGINT scheduling shutdown...
16995:M 17 Apr 2023 02:50:15.684 # User requested shutdown...
16995:M 17 Apr 2023 02:50:15.684 * Saving the final RDB snapshot before exiting.
16995:M 17 Apr 2023 02:50:15.688 * DB saved on disk
16995:M 17 Apr 2023 02:50:15.688 * Removing the pid file.
16995:M 17 Apr 2023 02:50:15.688 # Redis is now ready to exit, bye bye...
```



###### 触发RDB条件

> RDB条件可以在`redis.conf`中设置。

```shell
# 3600秒内1个key被修改。。。触发RDB
save 3600 1 300 100 60 10000
# ”“标识不触发rdb
save ""
```

例子：

我们设置成:`save 30 1 20 2 10 3`

当触发RDB条件时就会输出相应日志

```tex
20366:M 17 Apr 2023 03:04:20.090 * 1 changes in 30 seconds. Saving...
20366:M 17 Apr 2023 03:04:20.091 * Background saving started by pid 20453
20453:C 17 Apr 2023 03:04:20.096 * DB saved on disk
20453:C 17 Apr 2023 03:04:20.097 * Fork CoW for RDB: current 0 MB, peak 0 MB, average 0 MB
20366:M 17 Apr 2023 03:04:20.193 * Background saving terminated with success
20366:M 17 Apr 2023 03:04:36.513 * 3 changes in 10 seconds. Saving...
20366:M 17 Apr 2023 03:04:36.513 * Background saving started by pid 20499
20499:C 17 Apr 2023 03:04:36.519 * DB saved on disk
20499:C 17 Apr 2023 03:04:36.519 * Fork CoW for RDB: current 0 MB, peak 0 MB, average 0 MB
20366:M 17 Apr 2023 03:04:36.614 * Background saving terminated with success
```

##### RDB原理

>  save是主进程执行的,他会阻塞其他进程。
>
> 执行bgsave命令,首先会fork主进程得到子进程,子进程与主进程共享内存数据,完成fork后读取内存数据并写入dunp文件。

fork采用的是copy-on-write技术：

- 当主进程执行读操作时,主进程访问共享内存
- 当主进程执行写操作时,主进程会拷贝一份数据,执行写操作,此刻子进程读取共享内存并写入磁盘

##### 小结

> save & bgsave

save：同步保存数据,主进程执行,阻塞其他进程

bgsave：异步保存数据,fork得到的子进程保存数据

> RDB缺点

- RDB执行间隔时间长，两次RDB之间写入数据有丢失的风险
- fork子进程、压缩、写出RDB文件都比较耗时



#### AOF

> AOF - Append Only File (追加文件)。Redis处理的每一个写命令都会以命令的形式记录在AOF文件，可以看做是命令日志文件。

AOF配置：

```properties
# 是否开启aof,默认no,如果需要开启,则配置为yes即可
appendonly no
# aof文件名称
appendfilename "appendonly.aof"
# 工作路径下
appenddirname "appendonlydir"

# 表示每执行一条命令,就写入aof文件(磁盘io,性能较差)
appendfsync always
# 执行一条写命令后,将写命令存储在aof缓冲区,每隔一秒将缓冲区数据写入到aof,是默认方案
appendfsync everysec
# 执行一条写命令后,将写命令存储在aof缓冲区,什么时候写入aof磁盘文件由操作系统决定
appendfsync no

# AOF文件比上次文件 增长超过多少百分比则触发重写
auto-aof-rewrite-percentage 100
# AOF文件体积最小多大以上才触发重写 
auto-aof-rewrite-min-size 64mb 
```

##### 原理

> 当我们开启aof(采用everysec刷盘策略),每执行一条写命令,写命令会先存储在aof缓冲区,隔一秒后将缓冲区数据写入aof磁盘文件。

首先开启aof:

```properties
# 关闭rdb
rdbcompression no
save ""
appendonly yes
appendfilename "appendonly.aof"
appenddirname "appendonlydir"
appendfsync everysec
```

例子：

```shell
127.0.0.1:6379> set name yuyc
OK
127.0.0.1:6379> set age 22
OK
127.0.0.1:6379> set age 23
OK
127.0.0.1:6379> mget name age
1) "yuyc"
2) "23"
```

redis日志输出情况：

> 开启aof后,启动Redis就会输出创建aof文件的日志。并且`ctrl + c`退出redis后会调用和aof有关的fsync函数。

```tex
37049:M 17 Apr 2023 04:43:34.701 * Creating AOF base file appendonly.aof.1.base.rdb on server start
37049:M 17 Apr 2023 04:43:34.705 * Creating AOF incr file appendonly.aof.1.incr.aof on server start
37049:M 17 Apr 2023 04:43:34.705 * Ready to accept connections
^C
37049:signal-handler (1681677869) Received SIGINT scheduling shutdown...
37049:M 17 Apr 2023 04:44:29.080 # User requested shutdown...
37049:M 17 Apr 2023 04:44:29.080 * Calling fsync() on the AOF file.
37049:M 17 Apr 2023 04:44:29.081 * Removing the pid file.
37049:M 17 Apr 2023 04:44:29.081 # Redis is now ready to exit, bye bye...
```

查看aof文件内容：

```tex
$ cat appendonly.aof.manifest 
file appendonly.aof.1.base.rdb seq 1 type b
file appendonly.aof.1.incr.aof seq 1 type i

$ cat appendonly.aof.1.base.rdb 
REDIS0010�	redis-ver7.0.10�
aof-base��e�v���z�parallels@ubuntu-linux-22-04-desktop:/usr/local/src/redis-

$ cat appendonly.aof.1.incr.aof 
*2
$6
SELECT
$1
0
*3
$3
set
$4
name
$4
yuyc
*3
$3
set
$3
age
$2
22
*3
$3
set
$3
age
$2
23
```

##### aof文件重写

> AOF会记录每一次的写命令,类似`set num 1 set num 2`,他会记录两条命令,存在无意义的记录。相较于RDB文件肯定会更大。
>
> Aof有文件重写的命令,`bgrewriteaof`。`set num 1 set num 2`重写后就变成 `set num 2`。

Redis也会在触发阈值时自动去重写AOF文件。阈值也可以在redis.conf中配置：

```properties
# AOF文件比上次文件 增长超过多少百分比则触发重写
auto-aof-rewrite-percentage 100
# AOF文件体积最小多大以上才触发重写 
auto-aof-rewrite-min-size 64mb 
```



#### 对比

RDB和AOF各有自己的优缺点，如果对数据安全性要求较高，在实际开发中往往会**结合**两者来使用。

![image-20210725151940515](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_high/image-20210725151940515.png)

RDB优点：

- RDB文件是进过压缩的二进制文件,文件占用空间较小,保存了Redis某个时间点的数据集合,适合用于数据备份。
- RDB数据恢复速度较快,Redis启动较快。
- RDB适用于灾难恢复,可以定时备份数据,并将数据发送到数据中心,这样即使服务崩掉,也可以恢复到某个指定版本。

RDB缺点：

- 存在数据丢失的可能性。通过配置save来实现备份策略。
- fork主进程得到子进程来备份数据,如果此刻服务器进程繁忙,也有可能造成服务阻塞。
- 可能占用较大内存空间,linux中fork主进程采用的是copy-on-write形式,当主进程执行写命令时,需要拷贝一份数据,然后再修改,极端情况下会多出一倍的内存占用。

AOF优点：

- AOF相对于RDB数据完整性更可靠,选择合适的刷盘策略可降低数据丢失的可能性
- AOF有压缩命令,可通过bgrewriteaof降低aof文件体积
- AOF在没有压缩前很容易读懂

AOF缺点：

- 文件体积较大,在未压缩前可能存储着不必要的命令
- 宕机恢复速度较慢,RDB存的是数据快照,AOF存的是命令



#### aof rdb混合持久化

> RDB持久化的数据完整性不高,但是体积小,数据恢复快.AOF持久化数据文件体积大,但是完整性高。可以混合持久化结合两者优点。
>
> 混合持久化是通过`bgrewriteaof`异步完成的。

配置：

```properties
# 开启混合持久化
aof-use-rdb-preamble yes 
```

混合持久化过程：

混合持久化是通过bgrewriteaof完成的，不同的是当开启混合持久化时，fork出的子进程先将共享的内存副本全量的以RDB方式写入aof文件，然后再将重写缓冲区的增量命令以AOF方式写入到文件，写入完成后通知主进程更新统计信息，并将新的含有RDB格式和AOF格式的AOF文件替换旧的的AOF文件。

新的AOF文件前半段是RDB格式的全量数据后半段是AOF格式的增量数据

数据恢复过程：

当我们开启了混合持久化时，启动redis依然优先加载aof文件，aof文件加载可能有两种情况如下：

aof文件开头是rdb的格式, 先加载 rdb内容再加载剩余的 aof。

aof文件开头不是rdb的格式，直接以aof格式加载整个文件。



### Redis主从

> 单点Redis的并发能力不够,搭建Redis主从集群来提升Redis并发能力。
>
> Redis大多情况下是读多写少,Redis主从架构,可实现读写分离,提升Redis并发能力。

#### 搭建主从架构

##### 主从结构

一个主节点,两个从节点

#####  搭建Redis集群

> 准备三份redis工作目录

```bash
mkdir 7001 7002 7003
```

> 关闭aof、开启rdb

```properties
save 3600 1 300 100 60 10000
# save ""
appendonly no
```

> 拷贝配置文件

```bash
root@ubuntu-# cp redis-7.0.10/redis.conf ./7001/redis.conf
root@ubuntu-# cp redis-7.0.10/redis.conf ./7002/redis.conf
root@ubuntu-# cp redis-7.0.10/redis.conf ./7003/redis.conf

mkdir 7001/data 7002/data 7003/data

## 快捷
echo 7001 7002 7003 | xargs -t -n 1 cp redis-6.2.4/redis.conf
```

> 修改对应配置

- 端口
- 工作目录

```bash
sed -i -e 's/6379/7001/g' -e 's/dir .\/data\//dir \/usr\/local\/src\/7001\/data\//g' 7001/redis.conf
sed -i -e 's/6379/7002/g' -e 's/dir .\/data\//dir \/usr\/local\/src\/7002\/data\//g' 7002/redis.conf
sed -i -e 's/6379/7003/g' -e 's/dir .\/data\//dir \/usr\/local\/src\/7003\/data\//g' 7003/redis.conf
```



如果要一键停止，可以运行下面命令：

```sh
printf '%s\n' 7001 7002 7003 | xargs -I{} -t redis-cli -p {} shutdown
```

#####  启动

```shell
redis-server ./7001/redis.conf
redis-server ./7002/redis.conf
redis-server ./7003/redis.conf
```

#####  开启主从关系

> 配置主从关系的命令：replicaof 或者slaveof（5.0以前）

命令行配置(临时有效,重启失效)：

- 执行slaveof或者replicaof命令

  ```sh
  SLAVEOF host port
  REPLICAOF host port
  ```

配置文件配置(永久有效)：

- 在redis.conf中添加一行配置：`slaveof <masterip> <masterport>`或者`replicaof <masterip> <masterport>`



> 使用命令行方式配置主从关系

```sh
parallels@ubuntu-linux-22-04-desktop:~$ redis-cli -p 7002
127.0.0.1:7002> replicaof 127.0.0.1 7001
OK
127.0.0.1:7002> exit
parallels@ubuntu-linux-22-04-desktop:~$ redis-cli -p 7003
127.0.0.1:7003> replicaof 127.0.0.1 7001
OK
```

建立主从关系后redis会输出对应的日志信息：

![image-20230417085935996](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_high/image-20230417085935996.png)

查看主节点主从关系：

![image-20230417090156531](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_high/image-20230417090156531.png)

并且主节点可以读写、从节点只能读,并且主节点数据可同步到从节点:

![image-20230417090416245](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_high/image-20230417090416245.png)



#### 主从数据同步原理

> 主从节点数据同步分为全量同步和增量同步,判断依据是从节点是否是第一次与主节点建立连接。

##### 全量同步

> 主从关系第一次建立,就会执行全量同步,将主节点数据全部拷贝给从节点。
>
> 当从节点与主节点建立主从关系,从节点就会向主节点发送同步数据的请求(携带上版本号`replid`和偏移量`offset`)。主节点判断从节点是初次建立链接,第一次进行数据同步,就会进行全量同步。



###### 是否初次数据同步

> 主从关系第一次建立,就会进行首次数据同步。
>
> 此刻判断两个Redis实例的`Replid`是否一致来决定是否使用全量同步

两个关键信息：

- `Replication Id` ：是数据集标记,id一致则表示redis实例属于同一个数据集,即可进行数据同步。每个Redis实例都拥有自己的`replid`,但是从节点的`replid`继承自主节点。
- `offset` : 偏移量，随着记录在repl_baklog中的数据增多而逐渐增大。slave完成同步时也会记录当前同步的offset。如果slave的offset小于master的offset，说明slave数据落后于master，需要更新。



###### 全量同步过程

- 从节点执行`replicaof`命令,与主节点建立连接
- 从节点发送同步数据请求(携带上版本号`replid`和偏移量`offset`)
- 主节点判断`replid`是否相等,判断是否是否首次数据同步
  - 首次建立连接: `master`执行`bgsave`命令,将保存的快照文件发送给`replication`(bgsave和同步期间执行的写操作会记录在`repl_baklog`中)
  - 不是首次建立连接: `master`将`repl_baklog`中的命令发送给`replication`做数据同步(此刻如果offset过旧需要全量复制来更新offset)

![image-20210725152700914](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_high/image-20210725152700914.png)

###### 例子

> 全量复制发生在首次建立主从关系时,我们启动两个节点。

在从节点执行`replicaof host port`命令建立主从关系：

```sh
replicaof 127.0.0.1 7001
```

使用`info replication`命令查看实例状态：

- 可以得到主从关系
- 从节点继承了主节点的`replication id`,也就是此时两个Redis实例属于同一个数据集可以进行数据同步

![image-20230418022820117](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_high/image-20230418022820117.png)



分析全量同步流程：

全量同步前从节点会缓存当前数据,并和主节点同步数据合并(主节点数据优先)

![image-20230418030137435](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_high/image-20230418030137435.png)



##### 增量同步

> 全量同步,RDB文件将在网络中传输,成本太大。除了在首次建立主从关系时进行全量同步外,其他大部分情况下都是增量同步。
>
> 增量同步只会更新主从节点之间差异的部分。

![image-20210725153201086](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_high/image-20210725153201086.png)

###### repl_backlog原理

> repl_backlog本质上是一个固定大小的环形数组,且会覆盖旧数据。其中缓存着部分redis数据,和主从节点offset。
>
> 进行增量同步时,会同步repl_backlog中主从角标之间的数据。

> 当主节点的offset,大于从节点的offset超过整个repl_backlog的话,则无法使用基于repl_backlog的增量同步,需要进行新的一轮全量同步,来更新offset。



#### 主从同步优化

主从同步可以保证主从数据的一致性，非常重要。

可以从以下几个方面来优化Redis主从就集群：

- 在master中配置repl-diskless-sync yes启用无磁盘复制

  > 避免全量同步时的磁盘IO,前提是网络带宽足够。

- Redis单节点上的内存占用不要太大，减少RDB导致的过多磁盘IO

- 适当提高repl_baklog的大小，发现slave宕机时尽快实现故障恢复，尽可能避免全量同步

- 限制一个master上的slave节点数量，如果实在是太多slave，则可以采用主-从-从链式结构，减少master压力



### Redis哨兵

>  搭建一个三节点形成一主两从的哨兵(Sentinel)集群，来监管之前的Redis主从集群。
>
> 哨兵模式可以在主节点挂掉后,重新选举其中一个从节点作为主节点,自动恢复,提高可用性。

#### 搭建哨兵集群

> 需要启动三个哨兵实例,得准备三个配置文件以及工作目录。



> 创建工作目录

```sh
mkdir s1 s2 s3
```

> 配置文件。拷贝redis的`sentinel.conf`到s1、s2、s3目录下

```shell
cp sentinel.conf ../s1/sentinel.conf
cp sentinel.conf ../s2/sentinel.conf
cp sentinel.conf ../s3/sentinel.conf
```

> 修改对应的配置,主要是端口

```shell
sed -i -e 's/26379/27001/g' -e  's/6379/7001/g'  -e 's/dir \/tmp/\/usr\/local\/src\/s1/g' s1/sentinel.conf
sed -i -e 's/26379/27002/g' -e  's/6379/7001/g'  -e 's/dir \/tmp/\/usr\/local\/src\/s2/g' s2/sentinel.conf
sed -i -e 's/26379/27003/g' -e  's/6379/7001/g'  -e 's/dir \/tmp/\/usr\/local\/src\/s3/g' s3/sentinel.conf
```



```properties
## 哨兵端口
port 27001
## 哨兵ip,默认本地
sentinel announce-ip  127.0.0.1
## 哨兵监听的redis的master节点,  2-代表有两个哨兵认为此节点挂了,它才挂了
sentinel monitor mymaster 127.0.0.1 7001 2
sentinel down-after-milliseconds mymaster 5000
sentinel failover-timeout mymaster 60000
dir /usr/local/src/s1/
```

- `port 27001`：是当前sentinel实例的端口
- `sentinel monitor mymaster 127.0.0.1 7001 2`：指定主节点信息
  - `mymaster`：主节点名称，自定义，任意写
  - `127.0.0.1 7001`：主节点的ip和端口
  - `2`：选举master时的quorum值

##### 启动

```sh
# 第1个
redis-sentinel s1/sentinel.conf
# 第2个
redis-sentinel s2/sentinel.conf
# 第3个
redis-sentinel s3/sentinel.conf
```

##### 测试

> 让Redis主节点宕机,观察哨兵日志。

![image-20230418052351445](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_high/image-20230418052351445.png)

> 查看7002info

![image-20230417102549847](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/redis_high/image-20230417102549847.png)

##### 小结

> 哨兵模式可以在Redis主节点挂掉后,重新选举其中一个从节点作为新的主节点(故障切换),自动恢复,提高可用性。
>
> - 监控:哨兵监控Redis主节点
> - 故障转移:挂掉的主节点重启后作为新的主节点的从节点




#### 原理

##### 哨兵集群作用

- 监控: Redis 的 Sentinel通过心跳机制,不断地向Redis集群实例发送ping命令,检测master和reposte工作状态

- 自动故障恢复: 如果master宕机了,Sentinel会选举一个reposte作为新的master。保证Redis的高可用

- 通知: Sentinel作为Redis客户端的服务发现来源,当集群发生故障转移时,会将最新消息发送给客户端

  > Sentinel监控master节点,master节点存在所有reposte节点的信息。lettuce可以以sentinel作为Redis服务发现来源

##### 集群监控原理

> 主观下线 `$$` 客观下线：

- 主观下线： 某个Sentinel节点发现某个实例超时未响应,则该Sentinel节点主观认为此Redis实例主观下线
- 客观下线： 若达到配置的(quorum)数量的Sentinel节点,认为Redis实例主观下线,则此Redis实例客观下线

##### 自动故障恢复原理

> 当Redis集群中主节点宕机了,Sentinel则会选举一个reposte作为新的master节点。Sentinel集群会选举一个leader来进行自动故障修复。

选举依据：

- 首先判断slave节点与master节点断开时长,如果超过指定值(down-after-milliseconds `*` 10),则会排除该salve节点
- 然后判断slave节点的`slave-property`值(默认100),越小优先级越高,如果是0则用不参与选举
- 如果`slave-property`值也是一样,则判断slave的offset值,越大优先级越高(offset值越大说明数据完整性越好)
- 如果offset值也一样,则判断slave节点的id,越小优先级越高

> 当选举出新的master之后,sentinel如何进行自动故障修复：

- 首先Sentinel集群的leader会发送一个`slaveof noone`命令给到备选slave,让此slave成为新的master
- leader向其他的slave节点发送`slaveof host port`命令,让这些slave作为新master的从节点
- 最后leader将故障的master节点标记为slave,当此故障master节点重启时则作为从节点



#### RedisTemplate

> 在Sentinel集群监管下的Redis主从集群，其节点会因为自动故障转移而发生变化，Redis的客户端必须感知这种变化，及时更新连接信息。Spring的RedisTemplate底层利用lettuce实现了节点的感知和自动切换。
>
> 下面，我们通过一个测试来实现RedisTemplate集成哨兵机制。

配置Sentinel：

```yml
logging:
  level:
    io.lettuce.core: debug
  pattern:
    dateformat: MM-dd HH:mm:ss:SSS
spring:
  redis:
    sentinel:
      master: mymaster
      nodes:
        - 10.211.55.4:27001
        - 10.211.55.4:27002
        - 10.211.55.4:27003
```

配置lettuce客户端：

```java
@Bean
public LettuceClientConfigurationBuilderCustomizer clientConfigurationBuilderCustomizer() {
        return clientConfigurationBuilder -> clientConfigurationBuilder.readFrom(ReadFrom.REPLICA_PREFERRED);
}
```

这个bean中配置的就是读写策略，包括四种：

- MASTER：从主节点读取
- MASTER_PREFERRED：优先从master节点读取，master不可用才读取replica
- REPLICA：从slave（replica）节点读取
- REPLICA _PREFERRED：优先从slave（replica）节点读取，所有的slave都不可用才读取master



