# 简介

ElasticSearch,简称es，es是一个开源的==高拓展==的==分布式==全文检索引擎，它可以近乎实施的存储、检索数据；本身扩展性很好，可以扩展到上百台服务器，处理PB级别的数据。es也使用java开发并使用Lucene 作为其核心来实现所有索引和搜索的功能，但是它的目的是==通过简单的RESTful API来隐藏Lucene的复杂性==，从而让全文搜索变得简单。

> `es`的核心还是使用`lucene`创建索引和检索数据，==通过简单的RESTful API来隐藏Lucene的复杂性==

`elasticSearch`的前身`Lucene`和`nutch`

`Lucene`函数库。没有搜索引擎、`nutch`在`lucene`的基础上添加了搜索引擎

`Doug cutting`  `NDFS`[`nutch distributed file system`]   => `HDFS` [`Hadoop distributed file system`]



# es VS solr

实时创建索引`solr`会有`io`阻塞。

`es`处理大量数据的时候性能没有明显缺失。

# 安装

elasticsearch-7.6.1：ES安装包

elasticsearch-head-master：可视化工具

kibana-7.6.1-windows-x86_64：命令行操作工具

lk分词器

> 可视化工具head调用9100接口跨域问题
>
> http.core.enabled：true
>
> http.core.allow-origin:"*"

# ik分词器安装

(ik分词器)[https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v7.6.1]

## 安装

作为es的插件安装，解压即可

## 使用

![image-20220123193854167](ElasticSearch.assets\image-20220123193854167.png)

> 除了默认提供的分词规则，也可以定制分词规则
>
> 启动es的时候会将自定义的分词规则加进来

- ik_smart 最粗细粒度
- ik_max_word  最细细粒度

> 自定义分词效果

```json
GET _analyze
{
  "analyzer": "ik_max_word",
  "text": "我超级爱狂神说java" 
}
```

```json
{
  "tokens" : [
    {
      "token" : "我",
      "start_offset" : 0,
      "end_offset" : 1,
      "type" : "CN_CHAR",
      "position" : 0
    },{
      "token" : "超级",
      "start_offset" : 1,
      "end_offset" : 3,
      "type" : "CN_WORD",
      "position" : 1
    },{
      "token" : "爱",
      "start_offset" : 3,
      "end_offset" : 4,
      "type" : "CN_CHAR",
      "position" : 2
    },{
      "token" : "狂神说",
      "start_offset" : 4,
      "end_offset" : 7,
      "type" : "CN_WORD",
      "position" : 3
    },{
      "token" : "java",
      "start_offset" : 7,
      "end_offset" : 11,
      "type" : "ENGLISH",
      "position" : 4
    }
  ]
}
```

> 总之除了定制分词效果，其他都是一般词组



# es命令

即通过resultful风格的命令简化lucenne的复杂操作

![image-20220123194553105](ElasticSearch.assets\image-20220123194553105.png)

# 索引

### 创建索引

> PUT /索引名/类型名/文档id {请求体}

```json
PUT /test1/_doc/yyc
{
  "name": "狂胜说",
  "age" : 3
}
```

- 索引名称  随意
- type类型名称，_doc(7.XX后类型弃用)
  - 即便类型为`type1`   我使用`get test1/_doc/yyc`也可以得到`yyc`文档
  - ![image-20220123195400887](ElasticSearch.assets\image-20220123195400887.png)

### 查看索引

> get  /索引名

```json
GET /test1
```

```json
{
  "test1" : {
    "aliases" : { },
    "mappings" : {
      "properties" : {
        "age" : {
          "type" : "long"
        },
        "name" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        }
      }
    },
    "settings" : {
      "index" : {
        "creation_date" : "1642936064329",
        "number_of_shards" : "1",
        "number_of_replicas" : "1",
        "uuid" : "4T9MIN4AReGODijRYtxUJA",
        "version" : {
          "created" : "7060199"
        },
        "provided_name" : "test1"
      }
    }
  }
}
```

### 查看文档

> 可以得到索引的相关信息
>
> 索引名称   别名   映射  类型

```json
GET /test1/type1/yyc
```

```json
{
  "_index" : "test1",
  "_type" : "type1",
  "_id" : "yyc",
  "_version" : 2,
  "_seq_no" : 2,
  "_primary_term" : 1,
  "found" : true,
  "_source" : {
    "name" : "狂胜说",
    "age" : 3
  }
}
```

### 文档key value字段类型

==数据类型==

字符串类型text、 keyword
数值类型long, integer, short, byte, double, float, half float, scaled float
日期类型date
布尔值类型boolean
二进制类型binary

### 声明索引文档字段类型

> 约束类型

```json
PUT /test2
{
  "mappings": {
    "properties": {
      "age": {
        "type": "long"
      },
      "name": {
        "type": "text"
      },
      "birthday":{
        "type": "date"
      }
    }
  }
}
```

### 修改文档

> 改  put覆盖。age没带，age变为空

```json
PUT /test1/_doc/yyc
{
  "name": "于延闯"
}
```

> post  索引/类型/文档id/_update

```json
POST test2/_update/yyc
{
  "doc": {
    "name":"roilyfish",
    "age": 22
  }
}
```

### 删除索引

> DELETE test1

# 文档

## 基本查询

> GET test/_doc/user2

```json
{
  "_index" : "test",
  "_type" : "_doc",
  "_id" : "user2",
  "_version" : 1,
  "_seq_no" : 3,
  "_primary_term" : 1,
  "found" : true,
  "_source" : {
    "name" : "于延闯",
    "age" : 10,
    "hobbys" : [
      "唱歌",
      "篮球",
      "游戏"
    ]
  }
}
```

> GET test/_doc/_search?q=name:"于延闯"
>
> ==文档类型已弃用==看具体版本

```json
GET test2/_search
{
  "query":{
    "match":{
      "name":"yyc"
    }
  }
}
```

```bash
"hits" : {
    "total" : {
      "value" : 3,  	 		## 三条匹配数据
      "relation" : "eq"  		## 匹配关系  equals
    },
    "max_score" : 0.44183272,	## 最大分数
    "hits" : [
      {
        "_index" : "test",    	## 索引名称
        "_type" : "_doc",		## 文档类型
        "_id" : "user2",		## 文档id
        "_score" : 0.44183272,	## 分数
        "_source" : {			## 数据
          "name" : "于延闯",
          "age" : 10,
          "hobbys" : [
            "唱歌",
            "篮球",
            "游戏"
          ]
        }
      },
```

## 条件查询

```bash
GET test/_doc/_search
{
  "query":{
    "match":{
      "name":"于"  ## name属性带有'于'的
    }
  },
  "_source":["name","age"],  ## 展示 name 和 age 字段
  "sort":[ 
    {
      "age":{            ## 以age降序排序
        "order":"asc"
      }
    }
  ],
  "from":0,				##分页 start 0开始 pagesize 1 
  "size":1
}
```

> 多条件查询
>
> must  =>and需要都满足
>
> should =>or  有一个满足即可
>
> must_not  都不满足

```bash
GET _search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "name": "于"
          }
        },
        {
          "match": {
            "age": "10"
          }
        }
      ]
    }
  }
}
```

> 过滤
>
> gte  >= gratethanequals
>
> lte <=
>
> lt <
>
> gt >

```bash

GET _search
{
  "query": {
    "bool": {
      "should": [
        {
          "match": {
            "name": "于"
          }
        }
      ],
      "filter": {
        "range": {
          "age": {
            "gte": 10,
            "lte": 20
          }
        }
      }
    }
  }
}
```

## keyword  text

> term  目前只支持查询keyword字段
>
> keyword字段不会被分词器解析，会进行精确查询

```json
GET testdb/_search
{
  "query": {
    "term": {
      "name": {
        "value": "狂神说java name2"
      }
    }
  }
}
```

> match对于keyword字段也不会走分词器解析，也是精确查询

```json
GET testdb/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "name": "狂神说java name"
          }
        }
      ]
    }
  }
}
```

> term不可以（部分匹配）模糊查询

> match 对于非keyword（text）字段可以部分匹配

> trem也可以  or

```json
GET testdb/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "term": {
            "desc": {
              "value": "狂神说java name2"
            }
          }
        },{
          "term": {
            "des": {
              "value": "狂神说java name"
            }
          }
        }
      ]
    }
  }
}
```

## highlight

```json
GET test2/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "match": {
            "name": "于"
          }
        },{
          "match": {
            "desc": "于延"
          }
        }
      ]
    }
  },
  "highlight": {
    "pre_tags": "<p>",
    "post_tags": "<p/>",## 自定义高亮前缀后缀
    "fields": {
      "name": {},
      "desc": {} ## 需要高亮的字段 query匹配需要作用到此字段
    }
    
  }
}
```

# springboot整合

## maven依赖  dependency

```xml
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.6.1</version>
</dependency>
```

## 初始化  init

```java
RestHighLevelClient client = new RestHighLevelClient(
        RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")));

//记得关闭资源
client.close();
```

## 基本使用 - 索引

```java
	@Test
    void createIndex() throws IOException {
        //创建索引   索引名称  request就是创建语句
        CreateIndexRequest request = new CreateIndexRequest("roilyfish_index");
        //执行创建语句  default默认创建规则
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().
                create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }
    @Test
    void existIndex() throws IOException {
        GetIndexRequest requset = new GetIndexRequest("roilyfish_index");
        boolean exists = restHighLevelClient.indices().exists(requset, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    @Test
    void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("roilyfish_index");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }
```

## 文档

```java
@Test
void documentAdd() throws IOException {
    User user = new User("于延闯", 10);
    String users = JSON.toJSONString(user);
    //put users_index/_doc/user1
    IndexRequest request = new IndexRequest("users_index");
    request.id("user1");
    request.timeout("1s");
    //{...}
    request.source(users, XContentType.JSON);
    IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
    System.out.println(response.status());
    System.out.println(response.toString());
}

@Test
void existDocument() throws IOException {
    //get user_index/_doc/user1
    GetRequest getRequest = new GetRequest("users_index", "user1");
    // 过滤_source  上下文
    getRequest.fetchSourceContext(new FetchSourceContext(false));
    getRequest.storedFields("_none_");
    boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
    System.out.println(exists);
}
@Test
void getDocument() throws IOException {
    //get user_index/_doc/user1
    GetRequest getRequest = new GetRequest("users_index", "user1");
    GetResponse documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
    System.out.println(documentFields);
    System.out.println(documentFields.getSourceAsString());

}

@Test
void updateDocument() throws IOException {
    //get user_index/_doc/user1
    UpdateRequest updateRequest = new UpdateRequest("users_index", "user1");
    updateRequest.timeout("1s");
    User user = new User("于延闯new", 24);
    updateRequest.doc(JSON.toJSONString(user),XContentType.JSON);
    UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
    System.out.println(update.toString());
    System.out.println(update.status());
}

@Test
void deleteDocument() throws IOException {
    String index = "users_index";
    String id = "user1";
    GetRequest getRequest = new GetRequest(index, id);
    getRequest.fetchSourceContext(new FetchSourceContext(false));
    getRequest.storedFields("_none_");
    boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
    if (exists){
        System.out.println("exists");
        DeleteRequest deleteRequest = new DeleteRequest(index, id);
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(delete.status());
        System.out.println(delete.toString());
    }
}
```

> 批量插入
>
> 链式编程

```java
@Test
void listInsert() throws IOException {
    ArrayList<User> users = new ArrayList<>();
    users.add(new User("于延闯1",10));
    users.add(new User("于延闯2",20));
    users.add(new User("于延闯3",30));
    users.add(new User("于延闯4",20));
    users.add(new User("于延闯5",20));
    users.add(new User("于延闯6",10));

    BulkRequest bulkRequest = new BulkRequest();
    Iterator<User> iterator = users.iterator();
    int index = 7;
    while (iterator.hasNext()){
        User next = iterator.next();
        //IndexRequest request = new IndexRequest("users_index");
        //request.source(JSON.toJSONString(next),XContentType.JSON);
        //request.id("user"+index);
        bulkRequest.add(new IndexRequest("users_index")
                .id("user"+index)
                .source(JSON.toJSONString(next),XContentType.JSON));
        index++;
    }
    BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    System.out.println(bulk.status());
    System.out.println(bulk.toString());
}
```

> 批量更新   先看存不存在

```java
@Test
void listUpdate() throws IOException {

    BulkRequest bulkRequest = new BulkRequest();
    // 批量更新的id
    List<String> ids = new ArrayList<>();
    ids.add("user1");
    ids.add("user2");
    ids.add("user3");

    Iterator<String> iterator = ids.iterator();

    while (iterator.hasNext()){
        String id = iterator.next();
        GetRequest getRequest = new GetRequest("users_index", id);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        if(restHighLevelClient.exists(getRequest,RequestOptions.DEFAULT)){
            UpdateRequest updateRequest = new UpdateRequest("users_index", id);
            User userUpdate = new User("yycUpdate", 100);
            updateRequest.doc(JSON.toJSONString(userUpdate),XContentType.JSON);
            bulkRequest.add(updateRequest);
        }
    }
    BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    System.out.println(bulk.status());
}
```

> 批量删除差不多



> 搜索

```JAVA
public List<Book> searchHighLight(String keyword, int pageIndex, int pageSize) {

    List<Book> books = new ArrayList<>();
    //搜索请求
    SearchRequest searchRequest = new SearchRequest(keyword+"_index");
    //搜索请求构建器
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

    //分页
    sourceBuilder.from(pageIndex);
    sourceBuilder.size(pageSize);

    //精准匹配
    TermQueryBuilder termQueryBuilder = new TermQueryBuilder("title", keyword);

    //高亮
    HighlightBuilder highlightBuilder = new HighlightBuilder();
    highlightBuilder.field("title");
    highlightBuilder.preTags("<span style='color:red'>");
    highlightBuilder.postTags("</span>");
    //一个字段高亮一处
    highlightBuilder.requireFieldMatch(false);

    sourceBuilder.query(termQueryBuilder);
    sourceBuilder.highlighter(highlightBuilder);

    searchRequest.source(sourceBuilder);

    SearchResponse searchResponse = null;
    try {
        searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
        e.printStackTrace();
    }
    SearchHits hits = searchResponse.getHits();

    for (SearchHit hit : hits) {
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        HighlightField title = highlightFields.get("title");
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        System.err.println(title);
        if(title!=null){
            Text[] fragments = title.fragments();
            System.err.println(fragments);
            String n_title = "";
            for (Text fragment : fragments) {
                n_title+=fragment;
            }
            sourceAsMap.put("title",n_title);
        }
        Book book = new Book();
        book.setImgSrc((String)sourceAsMap.get("imgSrc"));
        book.setTitle((String)sourceAsMap.get("title"));
        book.setPrice((String)sourceAsMap.get("price"));
        books.add(book);
    }
    return books;
}
```

# 文档查询详细api

```java
@Test
void query01() throws IOException {
    //搜索请求
    SearchRequest searchRequest = new SearchRequest("users_index");
    //搜索命令构造器
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    //bool  组合查询（聚合）
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    //must    and
    boolQueryBuilder.must().add(QueryBuilders.matchQuery("name", "于延闯"));
    //should   or
    //boolQueryBuilder.should().add(QueryBuilders.matchQuery("age",11));
    //sort  排序
    searchSourceBuilder.sort("age", SortOrder.ASC);
    //from size  分页
    searchSourceBuilder.from(0);
    searchSourceBuilder.size(1);
    //filter   过滤
    searchSourceBuilder.postFilter(QueryBuilders.rangeQuery("age").gt(10));
    //_source  显示哪些字段
    searchSourceBuilder.fetchSource(new String[]{"name"},new String[]{"age"});


    ////匹配命令构造器
    //MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "于延闯");
    //
    //searchSourceBuilder.query(matchQueryBuilder);

    searchSourceBuilder.query(boolQueryBuilder);
    searchRequest.source(searchSourceBuilder);

    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

    System.out.println(JSON.toJSONString(search.getHits()));
    System.out.println("========");

    for (SearchHit hit : search.getHits()) {
        System.out.println(hit.getId());
        System.out.println(hit.getSourceAsString());
        System.out.println(hit.getSourceAsMap());
    }

}
```

