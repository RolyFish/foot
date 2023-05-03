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

#### IK分词器模式

IK分词器包含两种模式：

* `ik_smart`：最少切分

* `ik_max_word`：最细切分



#### 扩展词典

> IK分词器可对中文进行分词,但是网络用语或其他新兴起的词汇不会被识别,所以需要配置扩展。

配置文件路径：`/elasticsearch-analysis-ik-7.13.4/config`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<comment>IK Analyzer 扩展配置</comment>
	<!--用户可以在这里配置自己的扩展字典 -->
	<entry key="ext_dict">ext.dict</entry>
	 <!--用户可以在这里配置自己的扩展停止词字典-->
	<entry key="ext_stopwords">stopword.dic</entry>
	<!--用户可以在这里配置远程扩展字典 -->
	<!-- <entry key="remote_ext_dict">words_location</entry> -->
	<!--用户可以在这里配置远程扩展停止词字典-->
	<!-- <entry key="remote_ext_stopwords">words_location</entry> -->
</properties>
```

配置一个ext.dict

```tex
奥利给
泰库辣
```

![image-20230503192645965](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503192645965.png)



## 索引库操作

索引库就类似数据库表，mapping映射就类似表的结构。

我们要向es中存储数据，必须先创建“库”和“表”。

索引库操作就类似于DML语句

### Mapping映射属性

mapping是对索引库中文档的约束，常见的mapping属性包括：

- type：字段数据类型，常见的简单类型有：
  - 字符串：text（可分词的文本）、keyword（精确值，例如：品牌、国家、ip地址）
  - 数值：long、integer、short、byte、double、float、
  - 布尔：boolean
  - 日期：date
  - 对象：object
- index：是否创建索引，默认为true
- analyzer：使用哪种分词器
- properties：该字段的子字段

例如下面的json文档：

```json
{
    "age": 21,
    "weight": 52.1,
    "isMarried": false,
    "info": "黑马程序员Java讲师",
    "email": "zy@itcast.cn",
    "score": [99.1, 99.5, 98.9],
    "name": {
        "firstName": "云",
        "lastName": "赵"
    }
}
```



对应的每个字段映射（mapping）：

- age：类型为 integer；参与搜索，因此需要index为true；无需分词器
- weight：类型为float；参与搜索，因此需要index为true；无需分词器
- isMarried：类型为boolean；参与搜索，因此需要index为true；无需分词器
- info：类型为字符串，需要分词，因此是text；参与搜索，因此需要index为true；分词器可以用ik_smart
- email：类型为字符串，但是不需要分词，因此是keyword；不参与搜索，因此需要index为false；无需分词器
- score：虽然是数组，但是我们只看元素的类型，类型为float；参与搜索，因此需要index为true；无需分词器
- name：类型为object，需要定义多个子属性
  - name.firstName；类型为字符串，但是不需要分词，因此是keyword；参与搜索，因此需要index为true；无需分词器
  - name.lastName；类型为字符串，但是不需要分词，因此是keyword；参与搜索，因此需要index为true；无需分词器



### 索引库CRUD

这里我们统一使用Kibana编写DSL的方式来演示。



#### 创建索引库

- 请求方式：PUT
- 请求路径：/索引库名，可以自定义
- 请求参数：mapping映射

格式：

```json
PUT /索引库名称
{
  "mappings": {
    "properties": {
      "字段名":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "字段名2":{
        "type": "keyword",
        "index": "false"
      },
      "字段名3":{
        "properties": {
          "子字段": {
            "type": "keyword"
          }
        }
      },
      // ...略
    }
  }
}
```

示例：

```json
PUT /heima
{
  "mappings": {
    "properties": {
      "info": {
        "type": "text",
        "analyzer": "ik_smart"
      },
      "email": {
        "type": "keyword",
        "index": false
      },
      "name": {
        "properties": {
          "firstName": {
            "type": "keyword"
          },
          "lastName": {
            "type": "keyword"
          }
        }
      }
    }
  }
}
```

#### 查询索引库

- 请求方式：GET

- 请求路径：/索引库名

- 请求参数：无

格式：

```json
GET /索引库名
```

示例：

```json
GET /heima
```

![image-20230503200504614](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503200504614.png)

#### 修改索引库

倒排索引结构虽然不复杂，但是一旦数据结构改变（比如改变了分词器），就需要重新创建倒排索引，这简直是灾难。因此索引库**一旦创建，无法修改mapping**。

虽然无法修改mapping中已有的字段，但是却允许添加新的字段到mapping中，因为不会对倒排索引产生影响。

##### 语法

```json
PUT /索引库名/_mapping
{
  "properties": {
    "新字段名":{
      "type": "integer"
    }
  }
}
```

##### 示例

```json
PUT /heima/_mapping
{
  "properties":{
    "age":{
      "type":"integer"
    }
  }
}
```

![image-20230503200855659](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503200855659.png)

#### 删除索引库

- 请求方式：DELETE

- 请求路径：/索引库名

- 请求参数：无

##### 格式

```json
DELETE /索引库名
```

##### 示例

```json
DELETE /heima
```



#### 小结

索引库操作：

- 创建索引库：PUT /索引库名
- 查询索引库：GET /索引库名
- 删除索引库：DELETE /索引库名
- 添加属性：PUT /索引库名/_mapping

## 文档操作



#### 新增文档

##### 语法

```json
POST /索引库名/_doc/文档id
{
    "字段1": "值1",
    "字段2": "值2",
    "字段3": {
        "子属性1": "值3",
        "子属性2": "值4"
    },
    // ...
}
```

##### 示例

```json
POST /heima/_doc/1
{
  "info": "黑马程序员Java讲师,泰库辣",
  "email": "1056819225@qq.com",
  "age": 23,
  "name": {
    "firstName": "哥",
    "lastName": "虎"
  }
}
```



#### 查询文档

##### 语法

```json
GET /索引库名称/_doc/文档id
```

##### 示例

```json
GET /heima/_doc/1
```

![image-20230503201601089](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503201601089.png)



#### 删除文档



##### 语法

```json
DELETE /索引库名称/_doc/文档id
```

##### 示例

```json
DELETE /heima/_doc/1
```



#### 修改文档

修改有两种方式：

- 全量修改：直接覆盖原来的文档
- 增量修改：修改文档中的部分字段

##### 全量修改

全量修改是覆盖原来的文档，其本质是：

- 根据指定的id删除文档
- 新增一个相同id的文档

**注意**：如果根据id删除时，id不存在，第二步的新增也会执行，也就从修改变成了新增操作了。



###### 语法

```json
PUT /索引库名称/_doc/id
{
    "字段1":"值",
    ....
}
```

###### 例子

```json
PUT /heima/_doc/1
{
  "info": "黑马程序员Java讲师,泰库辣",
  "email": "1056819225@qq.com",
  "age": 23,
  "name": {
    "firstName": "哥",
    "lastName": "虎"
  }
}
```

![image-20230503202006747](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503202006747.png)



##### 增量修改

增量修改是只修改指定id匹配的文档中的部分字段。

###### 语法

```json
POST /{索引库名}/_update/文档id
{
    "doc": {
         "字段名": "新的值",
    }
}
```

###### 示例

```json
POST /heima/_update/1
{
  "doc": {
    "email": "newemail@qq.com"
  }
}
```

![image-20230503202341265](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503202341265.png)



#### 小结

文档操作有哪些？

- 创建文档：POST /{索引库名}/_doc/文档id   { json文档 }
- 查询文档：GET /{索引库名}/_doc/文档id
- 删除文档：DELETE /{索引库名}/_doc/文档id
- 修改文档：
  - 全量修改：PUT /{索引库名}/_doc/文档id { json文档 }
  - 增量修改：POST /{索引库名}/_update/文档id { "doc": {字段}}



## RestClient操作索引库

[Java客户端](https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/index.html)

其中的Java Rest Client又包括两种：

- Java Low Level Rest Client
- Java High Level Rest Client



### 导入工程



#### Mapping映射分析

创建索引库，最关键的是mapping映射，而mapping映射要考虑的信息包括：

- 字段名
- 字段数据类型
- 是否参与搜索
- 是否需要分词
  - 如果分词，分词器是什么？


其中：

- 字段名、字段数据类型，可以参考数据表结构的名称和类型
- 是否参与搜索要分析业务来判断，例如图片地址，就无需参与搜索
- 是否分词呢要看内容，内容如果是一个整体就无需分词，反之则要分词
- 分词器，我们可以统一使用ik_max_word

来看下酒店数据的索引库结构:

```json
PUT /hotel
{
  "mappings": {
    "properties": {
      "id": {
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "ik_max_word",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword",
        "copy_to": "all"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "ik_max_word"
      }
    }
  }
}
```

几个特殊字段说明：

- location：地理坐标，里面包含精度、纬度
- all：一个组合字段，其目的是将多字段的值 利用copy_to合并，提供给用户搜索

地理坐标说明：

![image-20210720222110126](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20210720222110126.png)

copy_to说明：

![image-20210720222221516](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20210720222221516.png)

#### 初始化RestClient

在elasticsearch提供的API中，与elasticsearch一切交互都封装在一个名为RestHighLevelClient的类中，必须先完成这个对象的初始化，建立与elasticsearch的连接。

- 引入es的RestHighLevelClient依赖：

  ```xml
  <!-- elasticsearch-rest-high-level-client -->
  <dependency>
      <groupId>org.elasticsearch.client</groupId>
      <artifactId>elasticsearch-rest-high-level-client</artifactId>
      <version>7.13.4</version>
  </dependency>
  ```

- springboot管理的默认es版本与我们需要的不匹配需要覆盖

  ![image-20230503223433687](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230503223433687.png)

  ```xml
  <!-- 添加配置  -->
  <properties>
      <java.version>1.8</java.version>
      <elasticsearch.version>7.13.4</elasticsearch.version>
  </properties>
  
  <!--  应用  -->
  <dependency>
      <groupId>org.elasticsearch.client</groupId>
      <artifactId>elasticsearch-rest-high-level-client</artifactId>
      <version>${elasticsearch.version}</version>
  </dependency>
  ```

- 初始化RestHighLevelClient

  ```java
  class HotelDemoApplicationTests {
      RestHighLevelClient client;
      @BeforeEach
      void init() {
          client = new RestHighLevelClient(RestClient.builder(
                  HttpHost.create("http://10.211.55.4:9200")
          ));
      }
      @Test
      public void test() {
          log.info(client.toString());
      }
      @AfterEach
      void close() throws IOException {
          if (null != client) {
              client.close();
          }
      }
  }
  ```

  

### 创建索引库

创建索引库的API如下：

![image-20210720223049408](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20210720223049408.png)

代码分为三步：

- 1）创建Request对象。因为是创建索引库的操作，因此Request是CreateIndexRequest。
- 2）添加请求参数，其实就是DSL的JSON参数部分。因为json字符串很长，这里是定义了静态字符串常量MAPPING_TEMPLATE，让代码看起来更加优雅。
- 3）发送请求，client.indices()方法的返回值是IndicesClient类型，封装了所有与索引库操作有关的方法。

#### 示例

```java
@Test
public void createIndex() throws IOException {
    // 1.创建Request对象
    CreateIndexRequest request = new CreateIndexRequest("hotel");
    // 2.准备请求的参数：DSL语句
    request.source(HotelConstants.HOTEL_MAPPING_TEMPLATE, XContentType.JSON);
    // 3.发送请求
    client.indices().create(request, RequestOptions.DEFAULT);
}
```

HotelConstants.HOTEL_MAPPING_TEMPLATE内容：

```java
public class HotelConstants {
    public static final String HOTEL_MAPPING_TEMPLATE = 上面的大括号内的字符串
}
```



### 删除索引库

```json
DELETE /hotel
```

与创建索引库相比：

- 请求方式从PUT变为DELTE
- 请求路径不变
- 无请求参数

所以代码的差异，注意体现在Request对象上。依然是三步走：

- 1）创建Request对象。这次是DeleteIndexRequest对象
- 2）准备参数。这里是无参
- 3）发送请求。改用delete方法

```java
@Test
public void deleteIndex() throws IOException {
    // 创建deleteindex请求参数
    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("hotel");
    // 发送请求
    client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
}
```



### 判断索引库是否存在

```json
GET /hotel
```

因此与删除的Java代码流程是类似的。依然是三步走：

- 1）创建Request对象。这次是GetIndexRequest对象
- 2）准备参数。这里是无参
- 3）发送请求。改用exists方法

```java
@Test
public void indexExist() throws IOException {
    // 创建deleteindex请求参数
    GetIndexRequest getIndexRequest = new GetIndexRequest("hotel");
    // 发送请求
    final boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    log.info("索引hotel是否存在:{}", exists ? "存在" : "不存在");
}
```

### 4.4.总结

JavaRestClient操作elasticsearch的流程基本类似。核心是client.indices()方法来获取索引库的操作对象。

索引库操作的基本步骤：

- 初始化RestHighLevelClient
- 创建XxxIndexRequest。XXX是Create、Get、Delete
- 准备DSL（ Create时需要，其它是无参）
- 发送请求。调用RestHighLevelClient.indices().xxx()方法，xxx是create、exists、delete





## RestClient操作文档



### 新增文档

步骤

- 创建索引库对应实体类
- 使用mp查询数据,转化成索引库对应实体
- 创建IndexRequest请求
- 发送请求

#### 创建索引库实体类

> 只有一个字段location地理坐标特殊处理,其他都直接赋值。

```java
@Data
@NoArgsConstructor
public class HotelDoc {
    private Long id;
    private String name;
    private String address;
    private Integer price;
    private Integer score;
    private String brand;
    private String city;
    private String starName;
    private String business;
    private String location;
    private String pic;
    public HotelDoc(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.price = hotel.getPrice();
        this.score = hotel.getScore();
        this.brand = hotel.getBrand();
        this.city = hotel.getCity();
        this.starName = hotel.getStarName();
        this.business = hotel.getBusiness();
        this.location = hotel.getLatitude() + ", " + hotel.getLongitude();
        this.pic = hotel.getPic();
    }
}
```

#### 新增文档

> 新增文档的DSL语句：

```json
POST /{索引库名}/_doc/1
{
    "name": "Jack",
    "age": 21
}
```

> 新增文档RestClient实现

```java
@SpringBootTest
public class HotelDocumentTest {
    RestHighLevelClient client;
    /**
     * 引入service 查询数据库
     * 索引表中的数据都是从数据库查出来的
     */
    @Autowired
    IHotelService hotelService;
    @Test
    public void insetDoc() throws IOException {
        // 查询数据库
        Hotel hotel = hotelService.getById(36934L);
        // 转化成索引实体类
        HotelDoc hotelDoc = new HotelDoc(hotel);
        // 创建请求
        IndexRequest indexRequest = new IndexRequest("hotel")
                .id(hotelDoc.getId().toString())
                .source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }
    @BeforeEach
    void init() {
        client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://10.211.55.4:9200")
        ));
    }
    @AfterEach
    void close() throws IOException {
        if (null != client) {
            client.close();
        }
    }
}
```

![image-20230504000937183](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_base/image-20230504000937183.png)





###  查询文档

查询的DSL语句如下：

```json
GET /hotel/_doc/{id}
```

代码步骤：

- 准备Request对象
- 发送请求
- 解析结果

可以看到，结果是一个JSON，其中文档放在一个`_source`属性中，因此解析就是拿到`_source`，反序列化为Java对象即可。

#### 查询文档

```java
@Test
public void getDoc() throws IOException {
    // 创建请求
    GetRequest getRequest = new GetRequest("hotel", "36934");
    // 发送请求
    GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
    // 解析结果
    String source = getResponse.getSourceAsString();
    final HotelDoc hotelDoc = JSON.parseObject(source, HotelDoc.class);
    log.info("id:{},文档内容:{}", 36934, hotelDoc);
}
```



### 删除文档

删除的DSL为是这样的：

```json
DELETE /hotel/_doc/{id}
```

与查询相比，仅仅是请求方式从DELETE变成GET，可以想象Java代码应该依然是三步走：

- 1）准备Request对象，因为是删除，这次是DeleteRequest对象。要指定索引库名和id
- 2）准备参数，无参
- 3）发送请求。因为是删除，所以是client.delete()方法

#### 删除文档

```json
@Test
public void delDoc() throws IOException {
    // 创建删除请求
    DeleteRequest deleteRequest = new DeleteRequest("hotel", "36934");
    // 发送删除请求
    DeleteResponse delete = client.delete(deleteRequest, RequestOptions.DEFAULT);
    log.info(delete.toString());
}
```



### 修改文档

#### 全量修改

> 就是添加文档的api,es发现插入的文档id在索引库中存在,则会覆盖,即全量更新。

#### 局部修改

DSL语句为：

```json
POST /heima/_update/1
{
  "doc": {
    "email": "newemail@qq.com"
  }
}
```

##### 局部修改

与之前类似，也是三步走：

- 1）准备Request对象。这次是修改，所以是UpdateRequest
- 2）准备参数。也就是JSON文档，里面包含要修改的字段
- 3）更新文档。这里调用client.update()方法

```java
@Test
public void updateDoc() throws IOException {
    // 准备请求
    UpdateRequest updateRequest = new UpdateRequest()
            .index("hotel")
            .id("36934")
            .doc(XContentType.JSON,
                    "price", "952",
                    "starName", "四钻"
            );
    // 发送请求
    UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
    log.info(updateResponse.toString());
}
```



### 批量导入文档

案例需求：利用BulkRequest批量将数据库数据导入到索引库中。

步骤如下：

- 利用mybatis-plus查询酒店数据

- 将查询到的酒店数据（Hotel）转换为文档类型数据（HotelDoc）

- 利用JavaRestClient中的BulkRequest批处理，实现批量新增文档

```java
@Test
public void bulkPUTDoc() throws IOException {
    // 查询数据库
    List<Hotel> hotels = hotelService.list();
    // 创建bulkRequest请求
    final BulkRequest bulkRequest = new BulkRequest();
    int count = 0;
    for (Hotel hotel : hotels) {
        HotelDoc hotelDoc = new HotelDoc(hotel);
        bulkRequest.add(new IndexRequest()
                .index("hotel")
                .id(String.valueOf(hotelDoc.getId()))
                .source(JSON.toJSONString(hotelDoc),XContentType.JSON)
        );
        if (count % 127 == 0) {
            final BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info(bulkResponse.status().toString());
        }
        count++;
    }
}
```

### 小结

文档操作的基本步骤：

- 初始化RestHighLevelClient
- 创建XxxRequest。XXX是Index、Get、Update、Delete、Bulk
- 准备参数（Index、Update、Bulk时需要）
- 发送请求。调用RestHighLevelClient#.xxx()方法，xxx是index、get、update、delete、bulk
- 解析结果（Get时需要）



