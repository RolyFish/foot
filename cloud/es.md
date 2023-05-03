# ES



## ES基础



### ES作用

elasticsearch是一款非常强大的开源搜索引擎，具备非常多强大功能，可以帮助我们从海量数据中快速找到需要的内容。

- GITHUP 搜索仓库
- Google搜索bug
- 在电商平台搜索商品

很快在海量数据中搜索出匹配结果,并高亮显示



### ELK技术栈

elastic stack（ELK）包括：

- ElasticSearch

  > 核心

- kibana

  > 数据可视化,以及DevTols

- Logstash

  > 数据抓取

- Beats

  > 数据抓取

被广泛应用在日志数据分析、实时监控等领域：



### elasticsearch和lucene

elasticsearch底层是基于**lucene**来实现的。

**Lucene**是一个Java语言的搜索引擎类库，是Apache公司的顶级项目，[官网地址](https://lucene.apache.org/)



### 搜索引擎技术对比

搜索引擎占比排行榜：

![image-20210720195142535](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20210720195142535.png)



### 小结

什么是elasticsearch？

- 一个开源的分布式搜索引擎，可以用来实现搜索、日志统计、分析、系统监控等功能

什么是elastic stack（ELK）？

- 是以elasticsearch为核心的技术栈，包括beats、Logstash、kibana、elasticsearch

什么是Lucene？

- 是Apache的开源搜索引擎类库，提供了搜索引擎的核心API



## 倒排索引

倒排索引的概念是基于MySQL这样的正向索引而言的。

### 正向索引

> 正常的关系型数据库的查询过程,根据关键字匹配行。在做大数据量的模糊匹配性能较低,因为前置`%`会使得索引失效,导致全表扫描,性能地下。

### 倒排索引

倒排索引中有两个非常重要的概念：

- 文档（`Document`）：对应关系型数据库的row
- 词条（`Term`）：对文档数据或用户搜索数据，利用某种算法分词，得到的具备含义的词语就是词条。例如：我是中国人，就可以分为：我、是、中国人、中国、国人这样的几个词条



**创建倒排索引**是对正向索引的一种特殊处理，流程如下：

- 将每一个文档的数据利用算法分词，得到一个个词条
- 创建表，每行数据包括词条、词条所在文档id、位置等信息
- 因为词条唯一性，可以给词条创建索引，例如hash表结构索引

倒排索引的**搜索流程**如下：

- 搜索关键字分词,得到此条
- 此条,去倒排索引中匹配文档id
- 返回符合匹配度的文档



## ES相关概念

elasticsearch中有很多独有的概念，与mysql中略有差别，但也有相似之处。

| **MySQL** | **Elasticsearch** | **说明**                                                     |
| --------- | ----------------- | ------------------------------------------------------------ |
| Table     | Index             | 索引(index)，就是文档的集合，类似数据库的表(table)           |
| Row       | Document          | 文档（Document），就是一条条的数据，类似数据库中的行（Row），文档都是JSON格式 |
| Column    | Field             | 字段（Field），就是JSON文档中的字段，类似数据库中的列（Column） |
| Schema    | Mapping           | Mapping（映射）是索引中文档的约束，例如字段类型约束。类似数据库的表结构（Schema） |
| SQL       | DSL               | DSL是elasticsearch提供的JSON风格的请求语句，用来操作elasticsearch，实现CRUD |

两者各自有自己的擅长支出：

- Mysql：擅长事务类型操作，可以确保数据的安全和一致性

- Elasticsearch：擅长海量数据的搜索、分析、计算

因此在企业中，往往是两者结合使用：

- 对安全性要求较高的写操作，使用mysql实现
- 对查询性能要求较高的搜索需求，使用elasticsearch实现
- 两者再基于某种方式，实现数据的同步，保证一致性



## 安装ES、KIBANA



### 安装ES

> 我是Mac  m1底层架构问题,需要选择合适的版本。这里选了7.13.4

![image-20230503143644246](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503143644246.png)

#### 部署单点ES

1. 创建网络

   > 因为我们还需要部署kibana容器，因此需要让es和kibana容器互联。这里先创建一个网络：

   ```shell
   docker network ceate es-create
   ```

   ![image-20230503135020873](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503135020873.png)

2. 拉取镜像

   ```shell
   docker pull elasticsearch:7.13.4
   ```

3. 运行elasticSearch

   > 命令解释：
   >
   > - `-e "cluster.name=es-docker-cluster"`：设置集群名称
   > - `-e "http.host=0.0.0.0"`：监听的地址，可以外网访问
   > - `-e "ES_JAVA_OPTS=-Xms512m -Xmx512m"`：内存大小
   > - `-e "discovery.type=single-node"`：非集群模式
   > - `-v es-data:/usr/share/elasticsearch/data`：挂载逻辑卷，绑定es的数据目录
   > - `-v es-logs:/usr/share/elasticsearch/logs`：挂载逻辑卷，绑定es的日志目录
   > - `-v es-plugins:/usr/share/elasticsearch/plugins`：挂载逻辑卷，绑定es的插件目录
   > - `--privileged`：授予逻辑卷访问权
   > - `--network es-net` ：加入一个名为es-net的网络中
   > - `-p 9200:9200`：端口映射配置

   ```shell
   docker run -d \
   	--name es \
       -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
       -e "discovery.type=single-node" \
       -v es-data:/usr/share/elasticsearch/data \
       -v es-plugins:/usr/share/elasticsearch/plugins \
       --privileged \
       --network es-net \
       -p 9200:9200 \
       -p 9300:9300 \
   elasticsearch:7.13.4
   ```

   ![image-20230503135857354](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503135857354.png)

   ![image-20230503143510430](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503143510430.png)



#### 部署kinana

- `--network es-net` ：加入一个名为es-net的网络中，与elasticsearch在同一个网络中
- `-e ELASTICSEARCH_HOSTS=http://es:9200"`：设置elasticsearch的地址，因为kibana已经与elasticsearch在一个网络，因此可以用容器名直接访问elasticsearch
- `-p 5601:5601`：端口映射配置

```shell
docker run -d \
--name kibana \
-e ELASTICSEARCH_HOSTS=http://es:9200 \
--network=es-net \
-p 5601:5601  \
kibana:7.13.4
```

![image-20230503143535552](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503143535552.png)

### DevTools

kibana中提供了一个DevTools界面：

这个界面中可以编写DSL来操作elasticsearch。并且对DSL语句有自动补全功能。

![image-20230503143811974](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503143811974.png)





### 安装IK分词器

#### 自带分词器

> ES自带的分词器对中文分词不太友好

![image-20230503144620664](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503144620664.png)

#### 安装ik分词器

[IK分词器GitHup官网](https://github.com/medcl/elasticsearch-analysis-ik)

> 查看版本支持。我是7.13.4,[下载对应版本。](https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v7.13.4)

```shell
## 查看挂载卷信息
docker volume ls
## 查看指定挂载卷信息
docker inspect es-plugins
## 进入挂载卷目录
cd /var/lib/docker/volumes/es-plugins/_data
```

拷贝分词器插件到es-plugins挂载卷目录下

```shell
cp -r elasticsearch-analysis-ik-7.13.4/ /var/lib/docker/volumes/es-plugins/_data
```

重启es

```shell
docker restart es
```

测试

![image-20230503151245974](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503151245974.png)

