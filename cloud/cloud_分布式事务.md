## 分布式事务



### 单机事务

> 单机系统写数据库的过程成为一次事务。

> 单机事务必须要满足原则：

- 原子性

  > 一个事务中的所有操作必须全部失败或全部成功

- 一致性

  > 事务前后数据库状态一致

- 隔离性

  > 事务与事务之间相互隔离,不可见。并且对同一资源的事务不可同时发生(阻塞)

- 持久性

  > 持久性,事务提交数据将写入磁盘永久保存

### 分布式事务

> **分布式事务**,就是在分布式系统下产生的事务。

比如服务A、B、C都有各自数据源,服务A需要调用B、C来完成某个业务,那么将三个本地事务看做一个业务就是分布式事务。

比如下单过程：

创建订单、扣减账户、扣减库存在各自数据库内的操作都是一个本地事务,各自可以保证ACID原则。

但是如果将这三个本地事务看做一个业务,这三个本地事务需要满足ACID原则,这就是分布式事务。

![image-20210724165338958](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20210724165338958.png)



### 演示分布式事务问题



#### 搭建微服务

> 搭建微服务并注册进nacos

![image-20230510001701647](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20230510001701647.png)

![image-20230510001803330](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20230510001803330.png)

#### 测试

##### 正常情况

发出下单请求：

```shell
curl --location --request POST 'http://localhost:8082/order?userId=user202103032042012&commodityCode=100202003032041&count=2&money=200'
```

业务逻辑：

- 创建订单
- 扣减账户
- 扣减库存

![image-20230510002527206](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20230510002527206.png)

> 业务正确执行

![image-20230510002702557](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20230510002702557.png)

##### 异常情况

> 当账户或库存不足时对应事务应该回滚,但是这里是分布式事务,只能回滚各自单机事务。
>
> 这样就会导致,订单创建了,但是账户或者库存没有扣减。

发出下单请求：

此时库存只剩8库存不够

```shell
curl --location --request POST 'http://localhost:8082/order?userId=user202103032042012&commodityCode=100202003032041&count=10&money=200'
```

> 业务出现异常：(订单没有添加@Transactional注解)
>
> 库存扣减失败数据回滚,但是订单和账户事务没能回滚,不满足一致性。

![image-20230510003215996](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20230510003215996.png)

## 理论基础

解决分布式事务问题，需要一些分布式系统的基础知识作为理论指导。



### CAP定理

> CAP三个指标不可能都达到。 只能满足其中两个指标。而分布式系统下由于网络问题不可能不出现集群分区,所以P必选, 再从A、C中选择一个。常见的系统大多数也是AP或CP的。
>
> 比如：
>
> - es就是CP的。出现集群分区,让部分节点不可用,将分片分配到其他节点,保证数据一致性
> - Eureka就是AP的

- Consistency（一致性）
- Availability（可用性）
- Partition tolerance （分区容错性）

#### 一致性

> Consistency(一致性)：用户访问分布式系统中的任意节点，得到的数据必须一致。

> 分布式系统中,某个节点更新数据,数据必须同步到其他节点,保证数据一致性。

#### 可用性

> Availability(可用性)：用户访问集群中的任意健康节点，必须能得到响应，而不是超时或拒绝。

> 分布式系统中,由于某个原因导致某个系统不可用。

#### 分区容错性

> Partition tolerance(分区容错性)。
>
> 因为网络故障或其它原因导致分布式系统中的部分节点与其它节点失去连接，形成独立分区。在集群出现分区时，整个系统也要持续对外提供服务

由于网络分布式系统形成分区,那么分区节点上的数据可能就不一致。

- 如果满足系统可用性,分区节点持续对外提供服务,就会不能满足一致性

- 如果满足一致性,就必须剔除数据不一致节点。就不能满足可用性

  > es中采用的就是CP。当出现分区,es会将节点置为不可用,并将其分片分配到其他节点以保证一致性。



### BASE理论

BASE理论是对CAP的一种解决思路，包含三个思想：

- **Basically Available** **（基本可用）**：分布式系统在出现故障时，允许损失部分可用性，即保证核心可用。
- **Soft State（软状态）：**在一定时间内，允许出现中间状态，比如临时的不一致状态。
- **Eventually Consistent（最终一致性）**：虽然无法保证强一致性，但是在软状态结束后，最终达到数据一致。

### 解决分布式事务

分布式事务最大的问题是各个子事务的一致性问题，因此可以借鉴CAP定理和BASE理论，有两种解决思路：

- AP模式：各子事务分别执行和提交，允许出现结果不一致，然后采用弥补措施恢复数据即可，实现最终一致。

- CP模式：各个子事务执行后互相等待，同时提交，同时回滚，达成强一致。但事务等待过程中，处于弱可用状态。

![image-20210724172123567](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20210724172123567.png)

这里的子系统事务，称为**分支事务**；有关联的各个分支事务在一起称为**全局事务**。



## 初始Seata

[seata官网](http://seata.io/en-us/index.html)

### Seata架构

Seata事务管理中有三个重要的角色：

- **TC (Transaction Coordinator) -** **事务协调者：**维护全局和分支事务的状态，协调全局事务提交或回滚。

- **TM (Transaction Manager) -** **事务管理器：**定义全局事务的范围、开始全局事务、提交或回滚全局事务。

- **RM (Resource Manager) -** **资源管理器：**管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。

整体的架构如图：

![image-20210724172326452](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20210724172326452.png)

Seata基于上述架构提供了四种不同的分布式事务解决方案：

- XA模式：强一致性分阶段事务模式，牺牲了一定的可用性，无业务侵入
- TCC模式：最终一致的分阶段事务模式，有业务侵入
- AT模式：最终一致的分阶段事务模式，无业务侵入，也是Seata的默认模式
- SAGA模式：长事务模式，有业务侵入

无论哪种方案，都离不开TC，也就是事务的协调者。



### 部署TC服务

1. [下载seata](https://github.com/seata/seata/releases)

2. 解压

   ```shell
   tar -zxvf seata-server-1.4.2.tar.gz
   ```

3. 修改配置

   ```shell
   cp seata/seata-server-1.4.2/conf/registry.conf seata/seata-server-1.4.2/conf/registry.conf.tmp
   rm seata/seata-server-1.4.2/conf/registry.conf
   vim seata/seata-server-1.4.2/conf/registry.conf
   ```

   ```properties
   registry {
     # tc服务的注册中心类，这里选择nacos，也可以是eureka、zookeeper等
     type = "nacos"
   
     nacos {
       # seata tc 服务注册到 nacos的服务名称，可以自定义
       application = "seata-tc-server"
       serverAddr = "127.0.0.1:8848"
       group = "DEFAULT_GROUP"
       namespace = ""
       cluster = "SH"
       username = "nacos"
       password = "nacos"
     }
   }
   
   config {
     # 读取tc服务端的配置文件的方式，这里是从nacos配置中心读取，这样如果tc是集群，可以共享配置
     type = "nacos"
     # 配置nacos地址等信息
     nacos {
       serverAddr = "127.0.0.1:8848"
       namespace = ""
       group = "SEATA_GROUP"
       username = "nacos"
       password = "nacos"
       dataId = "seataServer.properties"
     }
   }
   ```

4. 在nacos配置

   > 为了让tc服务的集群可以共享配置，我们选择了nacos作为统一配置中心。因此服务端配置文件seataServer.properties文件需要在nacos中配好。

   ![image-20230510014027792](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20230510014027792.png)

   ```properties
   # 数据存储方式，db代表数据库
   store.mode=db
   store.db.datasource=druid
   store.db.dbType=mysql
   store.db.driverClassName=com.mysql.jdbc.Driver
   store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true&rewriteBatchedStatements=true
   store.db.user=root
   store.db.password=123456
   store.db.minConn=5
   store.db.maxConn=30
   store.db.globalTable=global_table
   store.db.branchTable=branch_table
   store.db.queryLimit=100
   store.db.lockTable=lock_table
   store.db.maxWait=5000
   # 事务、日志等配置
   server.recovery.committingRetryPeriod=1000
   server.recovery.asynCommittingRetryPeriod=1000
   server.recovery.rollbackingRetryPeriod=1000
   server.recovery.timeoutRetryPeriod=1000
   server.maxCommitRetryTimeout=-1
   server.maxRollbackRetryTimeout=-1
   server.rollbackRetryTimeoutUnlockEnable=false
   server.undo.logSaveDays=7
   server.undo.logDeletePeriod=86400000
   
   # 客户端与服务端传输方式
   transport.serialization=seata
   transport.compressor=none
   # 关闭metrics功能，提高性能
   metrics.enabled=false
   metrics.registryType=compact
   metrics.exporterList=prometheus
   metrics.exporterPrometheusPort=9898
   ```

5. 创建数据库表

6. 启动TCC服务

   



## Seata模式

TM ：事务管理器， 管理,全局事务,调用分支事务

RM： 资源管理器, 管理分支事务(事务提交和回滚以及其他操作)

TC：事务协调者     分支事务执行



### XA模式

![image-20210724174424070](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20210724174424070.png)

RM一阶段的工作：

​	① 注册分支事务到TC

​	② 执行分支业务sql但不提交

​	③ 报告执行状态到TC

TC二阶段的工作：

- TC检测各分支事务执行状态

  a.如果都成功，通知所有RM提交事务

  b.如果有失败，通知所有RM回滚事务

RM二阶段的工作：

- 接收TC指令，提交或回滚事务



#### 优缺点

XA模式的优点是什么？

- 事务的强一致性，满足ACID原则。
- 常用数据库都支持，实现简单，并且没有代码侵入

XA模式的缺点是什么？

- 因为一阶段需要锁定数据库资源，等待二阶段结束才释放，性能较差
- 依赖关系型数据库实现事务



### AT模式

![image-20210724175327511](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20210724175327511.png)



阶段一RM的工作：

- 注册分支事务
- 记录undo-log（数据快照）
- 执行业务sql并提交
- 报告事务状态

阶段二提交时RM的工作：

- 删除undo-log即可

阶段二回滚时RM的工作：

- 根据undo-log恢复数据到更新前



#### 优缺点

AT模式的优点：

- 一阶段完成直接提交事务，释放数据库资源，性能比较好
- 利用全局锁实现读写隔离
- 没有代码侵入，框架自动完成回滚和提交

AT模式的缺点：

- 两阶段之间属于软状态，属于最终一致
- 框架的快照功能会影响性能，但比XA模式要好很多



### TCC

![image-20210724182937713](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20210724182937713.png)

#### 优缺点

TCC模式的每个阶段是做什么的？

- Try：资源检查和预留
- Confirm：业务执行和提交
- Cancel：预留资源的释放

TCC的优点是什么？

- 一阶段完成直接提交事务，释放数据库资源，性能好
- 相比AT模型，无需生成快照，无需使用全局锁，性能最强
- 不依赖数据库事务，而是依赖补偿操作，可以用于非事务型数据库

TCC的缺点是什么？

- 有代码侵入，需要人为编写try、Confirm和Cancel接口，太麻烦
- 软状态，事务是最终一致
- 需要考虑Confirm和Cancel的失败情况，做好幂等处理



### 对比

![image-20210724185021819](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/image-20210724185021819.png)