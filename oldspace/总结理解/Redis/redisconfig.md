# redis启动命令

> redis-server ../roilyfishconfig/redis.conf
>
> 指定配置文件启动

# redis基本

- 对大小写不敏
  - ![image-20211229085606557](redisconfig.assets\image-20211229085606557.png)
- 可以通过include命令去引入其他配置文件
  - ![image-20211229085818826](redisconfig.assets\image-20211229085818826.png)

# 网络配置

> bind 127.0.0.1    绑定的ip  想使用远程连接改为*或者指定ip
>
> protected-mode yes  保护模式
>
> port 6379		端口设置*

# GENERAL

> daemonize yes（no:不会以后台的方试运行）以守护进程的方式运行redis服务
> pidfile /var/run/redis_6379.pid（指定一个pid文件）
>
> 日志
>
> loglvel notice
> logfile ""  # 日志的文件位置名
> databases 16 #数据库的数量，默认为16个
> always-show-logo yes # 是否显示logo

## 快照（snapshutting持久化操作）

```bash
# 如果900秒内至少有一个key进行了修改，我们就进行持久化操作
save 900 1
# 如果300秒内至少有10key进行了修改，我们就进行持久化操作
save 300 10
# 如果60秒内至少有10000key进行了修改，我们就进行持久化操作
save 60 10000
# 我们之后学习持久化，会自己定义这个配置
stop-writes-on-bgsave-error yes # 持久化出错后是否继续工作
rdbcompression yes # 是否压缩rdb文件，需要消耗一些cpu的资源
rdbchecksum yes	# 报错rdb文件时候进行错误的检查校验
dir ./ # rdb文件保存的目录，默认为当前目录

```

## SECURITY(安全)

可以在这里设置redis的密码，默认是没有密码的

```bash
127.0.0.1:6379> config get requirepass	# 获取redis的密码
1) "requirepass"
2) ""
127.0.0.1:6379> config set requirepass "123456"	# 设置redis密码
OK
auth 123456 # 输入密码后才可以登录
```

## 限制CLIENTS

```bash
maxclients 10000	# 设置能连接上的最大客户端的数量
maxmemory <bytes>	# redis最大的内存容量
maxmemory-policy noeviction	# 内存达到上限的处理策略
	# 移除一些过期的key
	# 报错
	# 。。。
noeviction: 不删除策略, 达到最大内存限制时, 如果需要更多内存, 直接返回错误信息。（默认值）
allkeys-lru: 所有key通用; 优先删除最近最少使用(less recently used ,LRU) 的 key。
volatile-lru: 只限于设置了 expire 的部分; 优先删除最近最少使用(less recently used ,LRU) 的 key。
allkeys-random: 所有key通用; 随机删除一部分 key。
volatile-random: 只限于设置了 expire 的部分; 随机删除一部分 key。
volatile-ttl: 只限于设置了 expire 的部分; 优先删除剩余时间(time to live,TTL) 短的key。
redis中并不会准确的删除所有键中最近最少使用的键，而是随机抽取maxmeory-samples个键，删除这三个键中最近最少使用的键。
```

## APPEND ONLY 模式 aof配置

```bash
appendonly no	# 默认不开启aof模式，默认使用rdb方式持久化，几乎在所有情况下rdb够用

appendfilename "appendonly.aof" # 持久化文件的名字

# appendfsync always	# 每次修改都会同步，消耗性能
appendfsync everysec	# 每秒都同步一次 sync，可能会丢失这1s数据
# appendfsync no		# 不执行同步，这时候操作系统自己同步数据，速度最快
```

