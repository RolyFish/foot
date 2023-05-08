## es集群

### 简介

> 单机es存在问题即解决方式：

- 海量数据存储问题：将索引库从逻辑上拆分为N个分片（shard），存储到多个节点
- 单点故障问题：将分片数据在不同节点备份（replica ）

> ES集群相关概念:

* 集群（cluster）：一组拥有共同的 cluster name 的 节点。

* <font color="red">节点（node)</font>   ：集群中的一个 Elasticearch 实例

* <font color="red">分片（shard）</font>：索引可以被拆分为不同的部分进行存储，称为分片。在集群环境下，一个索引的不同分片可以拆分到不同的节点中

![image-20200104124440086-5602723](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20200104124440086-5602723.png)

> es如何保证高可用,数据完整。

es集群数据备份策略：

- 首先对数据分片，存储到不同节点
- 然后对每个分片进行备份，放到对方节点，完成互相备份(满足节点分片和副本分片不在同一个节点上)

![image-20230508142339205](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20230508142339205.png)

### 搭建es集群

部署es集群可以直接使用docker-compose来完成，但这要求你的Linux虚拟机至少有**4G**的内存空间

1. 创建网络

   ```shell
   docker network create elastic
   ```

2. docker-compose

   ```yaml
   version: '2.2'
   services:
     es01:
       image: elasticsearch:7.13.4
       container_name: es01
       environment:
         - node.name=es01
         - cluster.name=es-docker-cluster
         - discovery.seed_hosts=es02,es03
         - cluster.initial_master_nodes=es01,es02,es03
         - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
       volumes:
         - data01:/usr/share/elasticsearch/data
       ports:
         - 9201:9200
       networks:
         - elastic
     es02:
       image: elasticsearch:7.13.4
       container_name: es02
       environment:
         - node.name=es02
         - cluster.name=es-docker-cluster
         - discovery.seed_hosts=es01,es03
         - cluster.initial_master_nodes=es01,es02,es03
         - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
       volumes:
         - data02:/usr/share/elasticsearch/data
       ports:
         - 9202:9200
       networks:
         - elastic
     es03:
       image: elasticsearch:7.13.4
       container_name: es03
       environment:
         - node.name=es03
         - cluster.name=es-docker-cluster
         - discovery.seed_hosts=es01,es02
         - cluster.initial_master_nodes=es01,es02,es03
         - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
       volumes:
         - data03:/usr/share/elasticsearch/data
       networks:
         - elastic
       ports:
         - 9203:9200
   volumes:
     data01:
       driver: local
     data02:
       driver: local
     data03:
       driver: local
   
   networks:
     elastic:
       driver: bridge
   ```

3. 修改`/etc/sysctl.conf`文件

   ```shell
   vim /etc/sysctl.conf
   ```

   添加如下内容

   ```properties
   vm.max_map_count=262144
   ```

   生效配置

   ```shell
   sysctl -p
   ```

4. docker-compose 启动集群

   ```shell
   docker-compose up -d
   ```

   ![image-20230508143725002](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20230508143725002.png)

5. 可以使用docker logs监控节点运行情况

   ```shell
   docker logs -f es01
   ```

6. 查看集群状态

   ```shell
   http://10.211.55.4:9200/_cat/nodes
   ```

   

   ![image-20230508154231303](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20230508154231303.png)

### 监控集群状态

使用[cerebro  ](https://github.com/cerebroapp/cerebro/releases)来监控es集群状态，哥们失败了~



### 创建索引库

#### 启动kibana

> 只能连接一个es节点

```shell
docker run -d \
--name kibana-9200 \
-e ELASTICSEARCH_HOSTS=http://es:9200 \
--network=elastic \
-p 5601:5601  \
kibana:7.13.4
```



### es集群脑裂问题

#### 集群节点职责划分

elasticsearch中集群节点有不同的职责划分：

![image-20210723223008967](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20210723223008967.png)

默认情况下，集群中的任何一个节点都同时具备上述四种角色。

但是真实的集群一定要将集群职责分离：

- master节点：不做数据处理,监控集群的状态,对CPU要求高，但是内存要求第
- data节点：需要做数据处理, 对CPU和内存要求都高
- coordinating节点：类似于网关路由, 对网络带宽、CPU要求高

职责分离可以让我们根据不同节点的需求分配不同的硬件去部署。而且避免业务之间的互相干扰。

一个典型的es集群职责划分如图：

![**image-20210723223629142**](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20210723223629142.png)



#### 脑裂

脑裂是因为集群中的节点失联导致的。

node1 node2 node3

node1为主节点,某一时刻node1失联(网络问题), node2 node3选举node3为master节点,当node1重新连接上来,造成一个集群中有两个节点,那么请求过来就会到达两个es主节点,导致数据不一致。

##### 解决

解决脑裂的方案是，要求选票超过 ( eligible节点数量 + 1 ）/ 2 才能当选为主，因此eligible节点数量最好是奇数。对应配置项是discovery.zen.minimum_master_nodes，在es7.0以后，已经成为默认配置，因此一般不会发生脑裂问题

例如：3个节点形成的集群，选票必须超过 （3 + 1） / 2 ，也就是2票。node3得到node2和node3的选票，当选为主。node1只有自己1票，没有当选。集群中依然只有1个主节点，没有出现脑裂。

#### 小结

master eligible节点的作用是什么？

- 参与集群选主
- 主节点可以管理集群状态、管理分片信息、处理创建和删除索引库的请求

data节点的作用是什么？

- 数据的CRUD

coordinator节点的作用是什么？

- 路由请求到其它节点

- 合并查询到的结果，返回给用户



### es集群分布式存储

当新增文档时，数据会保存到不同分片，保证数据均衡，是由coordinating node保证的。

#### 搜索数据携带分片信息

```json
GET /test/_search
{
    "explain": true,
    "query":{
        "match_all":{}
    }
}
```

![image-20230508160101363](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20230508160101363.png)

#### 分片存储原理

elasticsearch会通过hash算法来计算文档应该存储到哪个分片：

![image-20210723224354904](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20210723224354904.png)

说明：

- _routing默认是文档的id
- 算法与分片数量有关，因此索引库一旦创建，分片数量不能修改！



新增文档的流程如下：

![image-20210723225436084](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20210723225436084.png)



解读：

- 1）新增一个id=1的文档
- 2）对id做hash运算，假如得到的是2，则应该存储到shard-2
- 3）shard-2的主分片在node3节点，将数据路由到node3
- 4）保存文档
- 5）同步给shard-2的副本replica-2，在node2节点
- 6）返回结果给coordinating-node节点



### es集群分布式查询

elasticsearch的查询分成两个阶段：

- scatter phase：分散阶段，coordinating node会把请求分发到每一个分片

- gather phase：聚集阶段，coordinating node汇总data node的搜索结果，并处理为最终结果集返回给用户

![image-20210723225809848](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20210723225809848.png)



### es故障转移

集群的master节点会监控集群中的节点状态，如果发现有节点宕机，会立即将宕机节点的分片数据迁移到其它节点，确保数据安全，这个叫做故障转移。

1）例如一个集群结构如图：

现在，node1是主节点，其它两个节点是从节点。

![image-20210723225945963](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20210723225945963.png)

2）突然，node1发生了故障：

![image-20210723230020574](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20210723230020574.png)

宕机后的第一件事，需要重新选主，例如选中了node2：

![image-20210723230055974](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20210723230055974.png)

node2成为主节点后，会检测集群监控状态，发现：shard-1、shard-0没有副本节点。因此需要将node1上的数据迁移到node2、node3：

![image-20210723230216642](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_cluster/image-20210723230216642.png)





