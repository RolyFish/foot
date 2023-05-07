## DSL查询文档

elasticsearch的查询依然是基于JSON风格的DSL来实现的。

### DSL查询分类

Elasticsearch提供了基于JSON的DSL（[Domain Specific Language](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html)）来定义查询。常见的查询类型包括：

- **查询所有**：查询出所有数据，一般测试用。例如：match_all

- **全文检索（full text）查询**：利用分词器对用户输入内容分词，然后去倒排索引库中匹配。例如：
  - match_query
  - multi_match_query
- **精确查询**：根据精确词条值查找数据，一般是查找keyword、数值、日期、boolean等类型字段。例如：
  - ids
  - range
  - term
- **地理（geo）查询**：根据经纬度查询。例如：
  - geo_distance
  - geo_bounding_box
- **复合（compound）查询**：复合查询可以将上述各种查询条件组合起来，合并查询条件。例如：
  - bool
  - function_score

查询的语法基本一致：

```json
GET /indexName/_search
{
  "query": {
    "查询类型": {
      "查询条件": "条件值"
    }
  }
}
```

我们以查询所有为例，其中：

- 查询类型为match_all
- 没有查询条件

```json
// 查询所有
GET /indexName/_search
{
  "query": {
    "match_all": {
    }
  }
}
```

其它查询无非就是**查询类型**、**查询条件**的变化。



#### 查询所有

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  }
}
```

![image-20230504012010247](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504012010247.png)



#### 全文检索查询



##### 使用场景

全文检索查询的基本流程如下：

- 对用户搜索的内容做分词，得到词条
- 根据词条去倒排索引库中匹配，得到文档id
- 根据文档id找到文档，返回给用户

比较常用的场景包括：

- 商城的输入框搜索
- 百度输入框搜索

因为是拿着词条去匹配，因此参与搜索的字段也必须是可分词的text类型的字段。



##### 基本语法

常见的全文检索查询包括：

- match查询：单字段查询
- multi_match查询：多字段查询，任意一个字段符合条件就算符合查询条件

match查询语法如下：

```json
GET /hotel/_search
{
  "query": {
    "match": {
      "FIELD": "TEXT"
    }
  }
}
```

mulit_match语法如下：

```json
GET /indexName/_search
{
  "query": {
    "multi_match": {
      "query": "TEXT",
      "fields": ["FIELD1", " FIELD12"]
    }
  }
}
```



##### match示例

![image-20230504015144615](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504015144615.png)

可以看到，两种查询结果是一样的，为什么？

因为我们将brand、name、business值都利用copy_to复制到了all字段中。因此你根据三个字段搜索，和根据all字段搜索效果当然一样了。

但是，搜索字段越多，对查询性能影响越大，因此建议采用copy_to，然后单字段查询的方式。



##### 小结

match和multi_match的区别是什么？

- match：根据一个字段查询
- multi_match：根据多个字段查询，参与查询字段越多，查询性能越差



#### 精确查询

精确查询一般是查找keyword、数值、日期、boolean等类型字段。所以**不会**对搜索条件分词。常见的有：

- term：根据词条精确值查询
- range：根据值的范围查询

##### term查询

因为精确查询的字段搜是不分词的字段，因此查询的条件也必须是**不分词**的词条。查询时，用户输入的内容跟自动值完全匹配时才认为符合条件。如果用户输入的内容过多，反而搜索不到数据。

语法：

```json
// term查询
GET /indexName/_search
{
  "query": {
    "term": {
      "FIELD": {
        "value": "VALUE"
      }
    }
  }
}
```

例子：

![image-20230504020248435](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504020248435.png)

##### range查询

范围查询，一般应用在对数值类型做范围过滤的时候。比如做价格范围过滤。

基本语法：

```json
// range查询
GET /indexName/_search
{
  "query": {
    "range": {
      "FIELD": {
        "gte": 10, // 这里的gte代表大于等于，gt则代表大于
        "lte": 20 // lte代表小于等于，lt则代表小于
      }
    }
  }
}
```

例子：

![image-20230504020556183](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504020556183.png)

##### 小结

精确查询常见的有哪些？

- term查询：根据词条精确匹配，一般搜索keyword类型、数值类型、布尔类型、日期类型字段
- range查询：根据数值范围查询，可以是数值、日期的范围



#### 地理坐标查询

所谓的地理坐标查询，其实就是根据经纬度查询，[官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-queries.html)

常见的使用场景包括：

- 携程：搜索我附近的酒店
- 滴滴：搜索我附近的出租车
- 微信：搜索我附近的人

##### 矩形范围查询

矩形范围查询，也就是geo_bounding_box查询，查询坐标落在某个矩形范围的所有文档：

![DKV9HZbVS6](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/DKV9HZbVS6.gif)

查询时，需要指定矩形的**左上**、**右下**两个点的坐标，然后画出一个矩形，落在该矩形内的都是符合条件的点。

语法如下：

```json
// geo_bounding_box查询
GET /indexName/_search
{
  "query": {
    "geo_bounding_box": {
      "FIELD": {
        "top_left": { // 左上点
          "lat": 31.1,
          "lon": 121.5
        },
        "bottom_right": { // 右下点
          "lat": 30.9,
          "lon": 121.7
        }
      }
    }
  }
}
```

例子：

![image-20230504021705104](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504021705104.png)



##### 附近查询

附近查询，也叫做距离查询（geo_distance）：查询到指定中心点小于某个距离值的所有文档。

换句话来说，在地图上找一个点作为圆心，以指定距离为半径，画一个圆，落在圆内的坐标都算符合条件

![vZrdKAh19C](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/vZrdKAh19C.gif)

语法说明：

```json
// geo_distance 查询
GET /indexName/_search
{
  "query": {
    "geo_distance": {
      "distance": "15km", // 半径
      "FIELD": "31.21,121.5" // 圆心
    }
  }
}
```

示例：

```json
GET hotel/_search
{
  "query": {
    "geo_distance": {
      "distance": "2km",
      "location": "31.21,121.5"
    }
  }
}
```



查询31.21,121.5附近三公里内的所有文档

![image-20230504021917417](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504021917417.png)

#### 复合查询



##### 相关性算分

当我们利用match查询时，文档结果会根据与搜索词条的关联度打分（_score），返回结果时按照分值降序排列。

例如，我们搜索 "虹桥如家"，结果如下：

![image-20230504142838739](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504142838739.png)

在elasticsearch中，早期使用的打分算法是TF-IDF算法，公式如下：

![image-20210721190152134](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20210721190152134.png)在后来的5.1版本升级中，elasticsearch将算法改进为BM25算法，公式如下：

![image-20210721190416214](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20210721190416214.png)



##### 算分函数查询



###### 语法说明

![image-20210721191544750](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20210721191544750.png)

function score 查询中包含四部分内容：

- **原始查询**条件：query部分，基于这个条件搜索文档，并且基于BM25算法给文档打分，**原始算分**（query score)
- **过滤条件**：filter部分，符合该条件的文档才会重新算分
- **算分函数**：符合filter条件的文档要根据这个函数做运算，得到的**函数算分**（function score），有四种函数
  - weight：函数结果是常量
  - field_value_factor：以文档中的某个字段值作为函数结果
  - random_score：以随机数作为函数结果
  - script_score：自定义算分函数算法
- **运算模式**：算分函数的结果、原始查询的相关性算分，两者之间的运算方式，包括：
  - multiply：相乘
  - replace：用function score替换query score
  - 其它，例如：sum、avg、max、min

function score的运行流程如下：

- 1）根据**原始条件**查询搜索文档，并且计算相关性算分，称为**原始算分**（query score）
- 2）根据**过滤条件**，过滤文档
- 3）符合**过滤条件**的文档，基于**算分函数**运算，得到**函数算分**（function score）
- 4）将**原始算分**（query score）和**函数算分**（function score）基于**运算模式**做运算，得到最终结果，作为相关性算分。

因此，其中的关键点是：

- 过滤条件：决定哪些文档的算分被修改
- 算分函数：决定函数算分的算法
- 运算模式：决定最终算分结果



###### 例子

需求：给“如家”这个品牌的酒店排名靠前一些

翻译一下这个需求，转换为之前说的四个要点：

- 原始条件：不确定，可以任意变化
- 过滤条件：brand = "如家"
- 算分函数：可以简单粗暴，直接给固定的算分结果，weight
- 运算模式：比如求和

因此最终的DSL语句如下：

```json
GET /hotel/_search
{
  "query": {
    "function_score": {
      "query": {
        "match": {
          "all": {
            "query": "虹桥如家"
          }
        }
        
      },
      "functions": [
        {
          "filter": {
            "term": {
              "brand": "如家"
            }
          },
          "weight": 2
        }
      ],
      "score_mode": "sum"
    }
  }
}
```

![image-20230504145239225](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504145239225.png)



###### 小结

function score query定义的三要素是什么？

- 过滤条件：哪些文档要加分
- 算分函数：如何计算function score
- 加权方式：function score 与 query score如何运算



##### 布尔查询

布尔查询是一个或多个查询子句的组合，每一个子句就是一个**子查询**。子查询的组合方式有：

- must：必须匹配每个子查询，类似“与”
- should：选择性匹配子查询，类似“或”
- must_not：必须不匹配，**不参与算分**，类似“非”
- filter：必须匹配，**不参与算分**

需要注意的是，搜索时，参与**打分的字段越多，查询的性能也越差**。因此这种多条件查询时，建议这样做：

- 搜索框的关键字搜索，是全文检索查询，使用must查询，参与算分
- 其它过滤条件，采用filter查询。不参与算分

###### 语法说明

```json
GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {"term": {"city": "上海" }}
      ],
      "should": [
        {"term": {"brand": "皇冠假日" }},
        {"term": {"brand": "华美达" }}
      ],
      "must_not": [
        { "range": { "price": { "lte": 500 } }}
      ],
      "filter": [
        { "range": {"score": { "gte": 45 } }}
      ]
    }
  }
}
```

###### 例子

需求：搜索名字包含“如家”，价格不高于400，在坐标31.21,121.5周围10km范围内的酒店。

分析：

- 名称搜索，属于全文检索查询，应该参与算分。放到must中
- 价格不高于400，用range查询，属于过滤条件，不参与算分。放到must_not中
- 周围10km范围内，用geo_distance查询，属于过滤条件，不参与算分。放到filter中

![image-20230504151455987](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504151455987.png)



###### 小结

bool查询有几种逻辑关系？

- must：必须匹配的条件，可以理解为“与”
- should：选择性匹配的条件，可以理解为“或”
- must_not：必须不匹配的条件，不参与打分
- filter：必须匹配的条件，不参与打分

### 搜索结果处理

搜索的结果可以按照用户指定的方式去处理或展示。

#### 排序

elasticsearch默认是根据相关度算分（_score）来排序，但是也支持自定义方式对搜索[结果排序](https://www.elastic.co/guide/en/elasticsearch/reference/current/sort-search-results.html)。可以排序字段类型有：keyword类型、数值类型、地理坐标类型、日期类型等。

##### 普通字段排序

keyword、数值、日期类型排序的语法基本一致。

**语法**：

```json
GET /indexName/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "FIELD": "desc"  // 排序字段、排序方式ASC、DESC
    }
  ]
}
```

排序条件是一个数组，也就是可以写多个排序条件。按照声明的顺序，当第一个条件相等时，再按照第二个条件排序，以此类推

###### 例子

> 查询品牌为汉庭,按评分降序,按价格升序排序。

```json
GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "brand": "汉庭"
          }
        }
      ]
    }
  },
  "sort": [
    {
      "score": {
        "order": "desc"
      }
    },
    {
      "price": {
        "order": "asc"
      }
    }
  ]
}
```



##### 地理坐标排序

地理坐标排序略有不同。

**语法说明**：

```json
GET /indexName/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "_geo_distance" : {
          "FIELD" : "纬度，经度", // 文档中geo_point类型的字段名、目标坐标点
          "order" : "asc", // 排序方式
          "unit" : "km" // 排序的距离单位
      }
    }
  ]
}
```

这个查询的含义是：

- 指定一个坐标，作为目标点
- 计算每一个文档中，指定字段（必须是geo_point类型）的坐标 到目标点的距离是多少
- 根据距离排序

**示例：**

需求描述：实现对酒店数据按照到你的位置坐标的距离升序排序

提示：获取你的位置的经纬度的方式：https://lbs.amap.com/demo/jsapi-v2/example/map/click-to-get-lnglat/

假设我的位置是：31.034661，121.612282，寻找我周围距离最近的酒店。

```json

GET /hotel/_search
{
  "query": {
   "match_all": {}
  },
  "sort": [
    {
      "_geo_distance": {
        "location": {
          "lat": 31.034,
          "lon": 121.612
        },
        "order": "asc",
        "unit": "km"
      }
    }
  ]
}
```



### 分页

elasticsearch 默认情况下只返回top10的数据。而如果要查询更多数据就需要修改分页参数了。elasticsearch中通过修改from、size参数来控制要返回的分页结果：

- from：从第几个文档开始
- size：总共查询几个文档

类似于mysql中的`limit ?, ?`

#### 基本分页

分页的基本语法如下：

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0, // 分页开始的位置，默认为0
  "size": 10, // 期望获取的文档总数
  "sort": [
    {"price": "asc"}
  ]
}
```

例子：

> 按距离排序,从下标10条开始查询20个。

```json
GET /hotel/_search
{
  "query": {
   "match_all": {}
  },
  "sort": [
    {
      "_geo_distance": {
        "location": {
          "lat": 31.034,
          "lon": 121.612
        },
        "order": "asc",
        "unit": "km"
      }
    }
  ],
  "from": 10,
  "size": 20
}
```



#### 深度分页问题

现在，我要查询990~1000的数据，查询逻辑要这么写：

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "from": 990, // 分页开始的位置，默认为0
  "size": 10, // 期望获取的文档总数
  "sort": [
    {"price": "asc"}
  ]
}
```

这里是查询990开始的数据，也就是 第990~第1000条 数据。

不过，elasticsearch内部分页时，必须先查询 0~1000条，然后截取其中的990 ~ 1000的这10条：

![image-20210721200643029](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20210721200643029.png)

查询TOP1000，如果es是单点模式，这并无太大影响。

但是elasticsearch将来一定是集群，例如我集群有5个节点，我要查询TOP1000的数据，并不是每个节点查询200条就可以了。

因为节点A的TOP200，在另一个节点可能排到10000名以外了。

因此要想获取整个集群的TOP1000，必须先查询出每个节点的TOP1000，汇总结果后，重新排名，重新截取TOP1000。

![image-20210721201003229](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20210721201003229.png)

那如果我要查询9900~10000的数据呢？是不是要先查询TOP10000呢？那每个节点都要查询10000条？汇总到内存中？

当查询分页深度较大时，汇总数据过多，对内存和CPU会产生非常大的压力，因此elasticsearch会禁止from+ size 超过10000的请求。

针对深度分页，ES提供了两种解决方案，[官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/current/paginate-search-results.html)：

- search after：分页时需要排序，原理是从上一次的排序值开始，查询下一页数据。官方推荐使用的方式。
- scroll：原理将排序后的文档id形成快照，保存在内存。官方已经不推荐使用。

##### 例子

> 使用深度分页查询,from必须为0,因为不需要我们指定,es会根据上一个分片的值去下一个分片查询。
>
> 使得聚合的结果不那么多

```json
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "id": {
        "order": "asc"
      }
    }
  ],
  "from": 0,
  "size": 10000, 
  "search_after": [
    "1393017952"
  ]
}
```



#### 小结

分页查询的常见实现方案以及优缺点：

- `from + size`：
  - 优点：支持随机翻页
  - 缺点：深度分页问题，默认查询上限（from + size）是10000
  - 场景：百度、京东、谷歌、淘宝这样的随机翻页搜索
- `after search`：
  - 优点：没有查询上限（单次查询的size不超过10000）
  - 缺点：只能向后逐页查询，不支持随机翻页
  - 场景：没有随机翻页需求的搜索，例如手机向下滚动翻页

- `scroll`：
  - 优点：没有查询上限（单次查询的size不超过10000）
  - 缺点：会有额外内存消耗，并且搜索结果是非实时的
  - 场景：海量数据的获取和迁移。从ES7.1开始不推荐，建议用 after search方案。



### 高亮

#### 高亮原理

什么是高亮显示呢？

我们在百度，京东搜索时，关键字会变成红色，比较醒目，这叫高亮显示。

高亮显示的实现分为两步：

- 1）给文档中的所有关键字都添加一个标签，例如`<em>`标签
- 2）页面给`<em>`标签编写CSS样式

#### 实现高亮

语法：

```java
GET /hotel/_search
{
  "query": {
    "match": {
      "FIELD": "TEXT" // 查询条件，高亮一定要使用全文检索查询
    }
  },
  "highlight": {
    "fields": { // 指定要高亮的字段
      "FIELD": {
        "pre_tags": "<em>",  // 用来标记高亮字段的前置标签
        "post_tags": "</em>" // 用来标记高亮字段的后置标签
      }
    }
  }
}
```

**注意：**

- 高亮是对关键字高亮，因此**搜索条件必须带有关键字**，而不能是范围这样的查询。
- 默认情况下，**高亮的字段，必须与搜索指定的字段一致**，否则无法高亮
- 如果要对非搜索字段高亮，则需要添加一个属性：required_field_match=false



##### 例子

![image-20230504161208025](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504161208025.png)



#### 小结

查询的DSL是一个大的JSON对象，包含下列属性：

- query：查询条件
- from和size：分页条件
- sort：排序条件
- highlight：高亮条件



## RestClient查询文档



### match_all

步骤：

- 构建查询请求
  - 创建searchRequest对象
- 准备请求参数
  - 指定source也就是searchSourceBuilder
- 发送查询请求
- 解析结果



#### 示例

```java
@Test
public void matchAll() throws IOException {
    // 创建请求
    SearchRequest searchRequest = new SearchRequest("hotel");
    /**
     * 准备请求参数
     * - 查询hotel索引库
     * - 查询方式 match_all
     */
    searchRequest.source().query(QueryBuilders.matchAllQuery());
    // 发送请求
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

    /**
     * 解析响应
     * json字符串封装的对象 结构为：
     *   "hits" : {
     *     "total" : {
     *       "value" : 30,
     *       "relation" : "eq"
     *     },
     *     "max_score" : null,
     *     "hits" : [
     *       {
     */
    SearchHits searchHits = searchResponse.getHits();
    // 命中数
    TotalHits totalHits = searchHits.getTotalHits();
    if (0 >= totalHits.value) {
        return;
    }
    List<HotelDoc> result = new ArrayList<>((int) totalHits.value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        result.add(JSON.parseObject(hit.getSourceAsString(), HotelDoc.class));
    }
    log.info("共查询到{}条数据",totalHits.value);
    log.info("查询结果{}",result);
}
```



### 全文检索

#### match

> match和match_all差异在于查询类型和查询条件。

步骤：

- 构建查询请求
  - 创建searchRequest对象
- 准备请求参数
  - 指定source也就是searchSourceBuilder
- 发送查询请求
- 解析结果

```java
@Test
public void match() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.matchQuery("all","如家"));
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = searchResponse.getHits();
    parseResult(searchHits);
}
private static void parseResult(SearchHits searchHits) {
    // 命中数
    TotalHits totalHits = searchHits.getTotalHits();
    if (0 >= totalHits.value) {
        return;
    }
    List<HotelDoc> result = new ArrayList<>((int) totalHits.value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        result.add(JSON.parseObject(hit.getSourceAsString(), HotelDoc.class));
    }
    log.info("共查询到{}条数据", totalHits.value);
    log.info("查询结果{}", result);
}
```



#### multi_match

> 多条件全文检索,同样的也是查询类型和查询条件不一样。

![image-20230504165237204](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504165237204.png)

```java
@Test
public void multiMatch() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.multiMatchQuery("如家", "name","brand","business"));
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = searchResponse.getHits();
    parseResult(searchHits);
}
```

### 精确查询

#### term

> 也是一样查询类型和查询条件不一样

![image-20230504165527592](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504165527592.png)

```java
@Test
public void term() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.termQuery("brand", "如家"));
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = searchResponse.getHits();
    parseResult(searchHits);
}
```



#### range

> 也是一样查询类型和查询条件不一样

![image-20230504165607794](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504165607794.png)

```java
@Test
public void range() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
  searchRequest.source().query(QueryBuilders.rangeQuery("price").gte(100).lte(400));
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = searchResponse.getHits();
    parseResult(searchHits);
}
```



### 地理坐标查询

![image-20230504174229710](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504174229710.png)



```java
@Test
public void geo() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.geoDistanceQuery("location")
            .point(31.21,121.5)
            .distance(3, DistanceUnit.KILOMETERS));
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = searchResponse.getHits();
    parseResult(searchHits);
}
```



### 复合查询

#### 布尔查询

> 布尔查询是用must、must_not、filter等方式组合其它查询，代码示例如下：

![image-20230504180930327](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504180930327.png)

```java
@Test
public void bool() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.boolQuery()
            .must(QueryBuilders.multiMatchQuery("上海","brand","name","business"))
            .should(QueryBuilders.matchQuery("brand","皇冠假日"))
            .should(QueryBuilders.matchQuery("brand","如家"))
            .mustNot(QueryBuilders.rangeQuery("price").gte(4000))
            .filter(QueryBuilders.rangeQuery("score").gte(40))
    );
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = searchResponse.getHits();
    parseResult(searchHits);
}
```

#### 分页

![image-20230504182318178](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504182318178.png)

```java
@Test
public void splitPage() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source()
            .query(QueryBuilders.matchAllQuery())
            .from(0).size(20);
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = searchResponse.getHits();
    parseResult(searchHits);
}
```



#### 排序



##### 分数排序

###### 算分函数查询

![image-20230504183242611](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230504183242611.png)

```java
@Test
public void funSort() throws IOException {
    final FunctionScoreQueryBuilder.FilterFunctionBuilder filterFunctionBuilder = new FunctionScoreQueryBuilder.FilterFunctionBuilder(
            QueryBuilders.rangeQuery("score").gte(40),ScoreFunctionBuilders.weightFactorFunction(10)
    );
    final FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[1];
    filterFunctionBuilders[0] = filterFunctionBuilder;
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source()
            .query(QueryBuilders.functionScoreQuery(
                            QueryBuilders.matchQuery("all", "如家"),
                            filterFunctionBuilders
                            )
                    .boostMode(CombineFunction.SUM)
            )
            .sort(SortBuilders.scoreSort().order(SortOrder.DESC))
            .from(0).size(20);
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = searchResponse.getHits();
    parseResult(searchHits);
}
```



##### 字段排序

![image-20230505000139571](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230505000139571.png)

```java
@Test
public void fieldSort() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source()
            .query(QueryBuilders.matchQuery("all", "如家"))
            .size(100)
            .sort("score", SortOrder.DESC)
            .sort("price", SortOrder.ASC);
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = searchResponse.getHits();
    parseResult(searchHits);
}
```

### 高亮

![image-20230505001649282](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230505001649282.png)

> 利用反射通过字段名称设置属性值

```java
static Field[] fields;

static {
    fields = HotelDoc.class.getDeclaredFields();
}

/**
 * 通过字段名称设置属性值
 */
public static void setValueByFieldName(HotelDoc hotelDoc, String fieldName, Object value) throws IllegalAccessException {
    for (Field field : fields) {
        if (field.getName().equals(fieldName)) {
            field.setAccessible(true);
            field.set(hotelDoc,value);
            break;
        }
    }
}
```

> 高亮显示请求构建和结果处理

```java
@Test
public void highLight() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source()
            .query(QueryBuilders.matchQuery("all", "如家"))
            .size(100)
            .sort("score", SortOrder.DESC)
            .sort("price", SortOrder.ASC)
            .highlighter(new HighlightBuilder()
                    .field(new HighlightBuilder.Field("name").requireFieldMatch(false).preTags("<hl>").postTags("</hl>"))
                    .field(new HighlightBuilder.Field("brand").requireFieldMatch(false).preTags("<hlz>").postTags("</hlz>"))
            );
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits searchHits = searchResponse.getHits();
    parseResult(searchHits);
}
```

```java
private static void parseResult(SearchHits searchHits) {
    // 命中数
    TotalHits totalHits = searchHits.getTotalHits();
    if (0 >= totalHits.value) {
        return;
    }
    List<HotelDoc> result = new ArrayList<>((int) totalHits.value);
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
        HotelDoc hotelDoc = JSON.parseObject(hit.getSourceAsString(), HotelDoc.class);
        result.add(hotelDoc);
        // 获取高亮字段
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        if (null != highlightFields && highlightFields.size() > 0) {
            highlightFields.forEach((key, hf) -> {
                if (null != hf && hf.getFragments().length > 0) {
                    String hlValue = hf.getFragments()[0].toString();
                    String fieldName = hf.getName();
                    try {
                        HotelDoc.setValueByFieldName(hotelDoc,fieldName,hlValue);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
    log.info("共查询到{}条数据", totalHits.value);
    log.info("查询结果{}", result);
}
```

![image-20230505005400612](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230505005400612.png)



## 黑马旅游案例



### 搜索

![image-20230506225813424](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230506225813424.png)



> 通过`QueryBuilders.matchQuery()`构建

```json
{
  "query": {
    "match": {
      "FIELD": "TEXT"
    }
  }
}
```



#### 请求参数

前端发出请求参数：

```json
{"key":"如家","page":1,"size":5,"sortBy":"default"}
```

请求参数接收对象：

```java
@Data
public class SearchRequestParams {

    /**
     * 关键字
     */
    private String key;

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 页面大小
     */
    private Integer size;

    /**
     * 排序方式
     */
    private String sortBy;
}
```



#### 查询步骤

- 构建查询请求
- 准备dsl
- 发出请求
- 解析结果

### 过滤

![image-20230506230148324](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230506230148324.png)

> 由于存在多个查询条件,所以使用`QueryBuilder.boolQuery()`查询。
>
> 对于需要参与算分的使用must、对于不需要算分的使用filer

```json
POST /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "FIELD": "TEXT"
          }
        }
      ],
      "filter": [
        {
          "range": {
            "FIELD": {
              "gte": 10,
              "lte": 20
            }
          }
        }
      ]
    }
  }
  ,
  "from": 0,
  "size": 20
}
```



### 附近酒店

- 发送当前经纬度
- 按距离升序排序
- 解析结果,返回距离

排序：

```java
// 3.0 排序
// 3.1 按距离排序
if(StringUtils.isNotBlank(searchRequestParams.getLocation())){
    searchRequest.source().sort(SortBuilders.geoDistanceSort("location",
            new GeoPoint(searchRequestParams.getLocation())).order(SortOrder.ASC).unit(DistanceUnit.KILOMETERS));
}
```

解析结果：

![image-20230506232226690](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230506232226690.png)

```java
// 解析sorted 有多个 和 放入排序条件顺序有关, 距离排序放在最后,也就是最后一个sort值
Object[] sortValues = hit.getSortValues();
if (sortValues != null && sortValues.length > 0) {
    // 距离
    Object geoSort = sortValues[sortValues.length - 1];
    hotelDoc.setDistance(geoSort);
}
```



### 广告置顶

> 算分函数查询

![image-20230506234230182](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230506234230182.png) 



## 数据聚合

**[聚合（](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)[aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)[）](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html)**可以让我们极其方便的实现对数据的统计、分析、运算。例如：

- 什么品牌的手机最受欢迎？
- 这些手机的平均价格、最高价格、最低价格？
- 这些手机每月的销售情况如何？

实现这些统计功能的比数据库的sql要方便的多，而且查询速度非常快，可以实现近实时搜索效果。



### 聚合种类

聚合常见的有三类：

- **桶（Bucket）**聚合：用来对文档做分组
  - TermAggregation：按照文档字段值分组，例如按照品牌值分组、按照国家分组
  - Date Histogram：按照日期阶梯分组，例如一周为一组，或者一月为一组

- **度量（Metric）**聚合：用以计算一些值，比如：最大值、最小值、平均值等
  - Avg：求平均值
  - Max：求最大值
  - Min：求最小值
  - Stats：同时求max、min、avg、sum等
- **管道（pipeline）**聚合：其它聚合的结果为基础做聚合

> **注意：**参加聚合的字段必须是keyword、日期、数值、布尔类型



### DSL实现聚合

现在，我们要统计所有数据中的酒店品牌有几种，其实就是按照品牌对数据分组。此时可以根据酒店品牌的名称做聚合，也就是Bucket聚合。



#### Bucket

语法如下：

```json
GET /hotel/_search
{
  "size": 0,  // 设置size为0，结果中不包含文档，只包含聚合结果
  "aggs": { // 定义聚合
    "brandAgg": { //给聚合起个名字
      "terms": { // 聚合的类型，按照品牌值聚合，所以选择term
        "field": "brand", // 参与聚合的字段
        "size": 20 // 希望获取的聚合结果数量
      }
    }
  }
}
```

![image-20230507123505552](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507123505552.png)

#### 聚合结果排序

> 默认情况下，Bucket聚合会统计Bucket内的文档数量，记为`_count`，并且按照`_count`降序排序。
>
> 我们可以指定order属性，自定义聚合的排序方式：

```json
GET /hotel/_search
{
  "size": 0, 
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 10,
        "order": {
          "_count": "asc"
        }
      }
    }
  }
}
```



#### 限定聚合范围

默认情况下，Bucket聚合是对索引库的所有文档做聚合，数据量较大则内存、cpu压力较大。

可以限定要聚合的文档范围，只要添加query条件即可：

```json
GET /hotel/_search
{
  "query": {
    "range": {
      "price": {
        "lte": 200
      }
    }
  }, 
  "size": 0, 
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 10,
        "order": {
          "_count": "asc"
        }
      }
    }
  }
}
```

![image-20230507124036710](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507124036710.png)

#### Metric聚合

对酒店按照品牌分组，形成了一个个桶。现在我们需要对桶内的酒店做运算，获取每个品牌的用户评分的min、max、avg等值。

这就要用到Metric聚合了，例如stat聚合：就可以获取min、max、avg等结果。

语法如下：

![image-20230507124326300](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507124326300.png)

> Metric聚合一般在Bucket(桶、分组)聚合后进行,对每个Bucket内数据做聚合。

![image-20230507124728913](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507124728913.png)



##### 按metric聚合结果排序

> metric聚合结果内有最大值,最小值,平均値等。这些数据往往用于排序条件。

![image-20230507125014494](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507125014494.png)

#### 小结

aggs代表聚合，与query同级，此时query的作用是？

- 限定聚合的的文档范围

聚合必须的三要素：

- 聚合名称
- 聚合类型
- 聚合字段

聚合可配置属性有：

- size：指定聚合结果数量
- order：指定聚合结果排序方式
- field：指定聚合字段



### RestClient实现聚合

聚合条件与query条件同级别，因此需要使用request.source()来指定聚合条件。

![image-20210723173057733](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20210723173057733.png)

#### 例子

> status 改成 stats

![image-20230507133908294](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507133908294.png)

> java代码

```java
@Test
public void testAgg() throws IOException {
    // 1 创建查询请求对象
    SearchRequest searchRequest = new SearchRequest("hotel");
    // 2 dsL
    // 2.1 查询
    searchRequest.source().query(QueryBuilders.rangeQuery("price").lte(200L));
    // 2.2 按品牌(brand)聚合聚合
    searchRequest.source().aggregation(AggregationBuilders
            .terms("brandAgg")
            .field("brand")
            .order(BucketOrder.aggregation("score_status", "avg", true))
            .size(10)
            .subAggregation(AggregationBuilders.stats("score_stats").field("score"))

    );
    // 2.3 限制size，只看聚合结果
    searchRequest.source().size(0);
    // 3 发出请求
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    // 4 解析结果
    // 4.1 获取聚合结果集
    Aggregations aggregations = searchResponse.getAggregations();
    Terms brandAgg = aggregations.get("brandAgg");
    final List<? extends Terms.Bucket> buckets = brandAgg.getBuckets();
    // 4.2 遍历
    for (Terms.Bucket bucket : buckets) {
        Aggregations aggregationsScoreStats = bucket.getAggregations();
        Stats scoreStats = aggregationsScoreStats.get("score_stats");
        log.info("品牌名称:{},个数:{} 评分:平均{},最大{},最小{},合计{}",
                bucket.getKeyAsString(),
                bucket.getDocCount(),
                scoreStats.getAvg(),
                scoreStats.getMax(),
                scoreStats.getMin(),
                scoreStats.getSum()
        );
    }
}
```

![image-20230507135751669](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507135751669.png)



#### 业务需求

需求：搜索页面的品牌、城市等信息不应该是在页面写死，而是通过聚合索引库中的酒店数据得来的。



##### 多字段聚合

> es允许在一个查询请求中对多个字段进行聚合。并且结果会封装到一个map集合中。

![image-20230507140903406](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507140903406.png)

代码：

```java
@Test
public void test() throws IOException {

    // 1 创建搜索请求
    SearchRequest searchRequest = new SearchRequest("hotel");
    // 2 DSL
    // 2.1 查询请求
    searchRequest.source().query(QueryBuilders.rangeQuery("price").lte(200L));
    // 2.2 多条件集合。多个聚合条件会放入一个集合中
    searchRequest.source()
            .aggregation(AggregationBuilders.terms("brandAgg").field("brand").order(BucketOrder.aggregation("_count", true)))
            .aggregation(AggregationBuilders.terms("cityAgg").field("city").order(BucketOrder.aggregation("_count", true)))
            .aggregation(AggregationBuilders.terms("starNameAgg").field("starName").order(BucketOrder.aggregation("_count", true)));
    // 3 发起请求
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    // 4 解析结果
    // List<String> brandList = new ArrayList<>();
    //         // List<String> cityList = new ArrayList<>();
    //         // List<String> startNameList = new ArrayList<>();
    Aggregations aggregations = searchResponse.getAggregations();
    Map<String, Aggregation> asMap = aggregations.getAsMap();
    asMap.forEach((aggName, agg) -> {
        log.info("aggName:{}", aggName);
        MultiBucketsAggregation multiBucketsAggregation = (MultiBucketsAggregation) agg;
        final List<? extends MultiBucketsAggregation.Bucket> buckets = multiBucketsAggregation.getBuckets();
        buckets.forEach(bucket ->{
            log.info(bucket.getKeyAsString());
        });
    });
}
```

![image-20230507143021452](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507143021452.png)



##### 实现

> 过滤条件可选,需要保证选择过滤条件查询有结果。

这就要求对查询结果做聚合,按品牌、按城市、按星级进行bucket聚合。



## 自动补全

### 拼音分词器

#### 安装

1. 下载

   [选择合适版本下载](https://github.com/medcl/elasticsearch-analysis-pinyin/releases/tag/v7.13.4)我是7.13.4

   ![image-20230507181825585](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507181825585.png)

2. 移动到plugins挂载卷目录下

   ```shell
   mv elasticsearch-analysis-pinyin-7.13.4/  /var/lib/docker/volumes/es-plugins/_data
   ```

3. 重启

   ```shell
   docker restart es
   ```

4. 测试

   ![image-20230507182606314](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507182606314.png)



##### 默认拼音分词器问题

- 会对每一个汉字单独分为拼音，而不是先分词



#### 自定义分词器

[拼音分词器官方配置选项](https://github.com/medcl/elasticsearch-analysis-pinyin)

默认的拼音分词器会将每个汉字单独分为拼音，而我们希望的是每个词条形成一组拼音，需要对拼音分词器做个性化定制，形成自定义分词器。

elasticsearch中分词器（analyzer）的组成包含三部分：

- character filters：在tokenizer之前对文本进行处理。例如删除字符、替换字符
- tokenizer：将文本按照一定的规则切割成词条（term）。例如keyword，就是不分词；还有ik_smart
- tokenizer filter：将tokenizer输出的词条做进一步处理。例如大小写转换、同义词处理、拼音处理等

文档分词时会依次由这三部分来处理文档：

![image-20210723210427878](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20210723210427878.png)

声明自定义分词器的语法如下：

> 自定义分词器需要在创建索引库时自定义,只在当前索引库中有效。

```json
PUT /test
{
  "settings": {
    "analysis": {
      "analyzer": { 
        "my_analyzer": { // 自定义分词器名称
          "tokenizer": "ik_max_word", // 对text进行分词
          "filter": "py"              // 在使用拼音分词器分词
        }
      },
      "filter": { // 自定义tockennizer filter
        "py": { 
          "type": "pinyin", // 使用拼音分词器
          "keep_full_pinyin": false, // 不对每个中文进行分词
          "keep_joined_full_pinyin": true, // 此条全拼
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true, // 删除重复项
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": { // 声明映射关系
    "properties": {
      "name":{
        "type": "text"
      }
    }
  }
}
```

测试：

![image-20230507214356003](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507214356003.png)

##### 问题

> 自定义分词器如果不加以设置的话,会在插入时创建倒排索引和搜索时对text进行分词都生效。
>
> 而对于一些同音字,如果采用中文搜索的话就会将不想要的结果返回。

例如：

```java
POST /test/_doc/1
{
  "id": 1,
  "name": "狮子"
}
POST /test/_doc/2
{
  "id": 2,
  "name": "虱子"
}

# 狮子会被自定义分词器分词为 shizi sz 狮子
GET /test/_search
{
  "query": {
    "match": {
      "name": "狮子"
    }
  }
}
```

狮子会被自定义分词器分词为 shizi sz 狮子,去倒排索引里匹配会匹配到虱子。

![image-20230507221612796](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507221612796.png)



##### 解决

所以需要对属性(filter)做配置,插入时使用自定义分词器,查询时使用ik分词器。

```json
"mappings": {
    "properties": {
      "name":{
        "type": "text",
        "search_analyzer": "ik_smart",
        "analyzer": "my_analyzer"
      }
    }
}
```



总结：

如何使用拼音分词器？

- ①下载pinyin分词器

- ②解压并放到elasticsearch的plugin目录

- ③重启即可

如何自定义分词器？

- ①创建索引库时，在settings中配置，可以包含三部分

- ②character filter

- ③tokenizer

- ④filter

拼音分词器注意事项？

- 为了避免搜索到同音字，搜索时不要使用拼音分词器



#### 自动补全查询

elasticsearch提供了[Completion Suggester](https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-suggesters.html)查询来实现自动补全功能。这个查询会匹配以用户输入内容开头的词条并返回。为了提高补全查询的效率，对于文档中字段的类型有一些约束：

- 参与补全查询的字段必须是completion类型

- 字段的内容一般是用来补全的多个词条形成的数组

比如，一个这样的索引库：

```json
// 创建索引库
PUT test
{
  "mappings": {
    "properties": {
      "title":{
        "type": "completion"
      }
    }
  }
}
```

然后插入下面的数据：

```json
// 示例数据
POST test/_doc
{
  "title": ["Sony", "WH-1000XM3"]
}
POST test/_doc
{
  "title": ["SK-II", "PITERA"]
}
POST test/_doc
{
  "title": ["Nintendo", "switch"]
}
```

查询的DSL语句如下：

```json
// 自动补全查询
GET /test/_search
{
  "suggest": {
    "title_suggest": { // 补全查询名称
      "text": "s", // 关键字
      "completion": {
        "field": "title", // 补全查询的字段
        "skip_duplicates": true, // 跳过重复的
        "size": 10 // 获取前10条结果
      }
    }
  }
}
```

![image-20230507222958821](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507222958821.png)

### 自动补全

#### 删除索引库重新创建

> 删除索引库重新创建
>
> - 删除索引库
> - 自定义分词器
> - 添加suggestion字段,对suggestion做自动补全
>
> 修改实体类
>
> - 添加suggestion字段 类型为`List<String>`类型

```json
# 酒店数据索引库
PUT /hotel
{
  "settings": {
    "analysis": {
      "analyzer": {
        "text_anlyzer": { // 自定义分词器
          "tokenizer": "ik_max_word",
          "filter": "py"
        },
        "completion_analyzer": {
          "tokenizer": "keyword",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "id":{
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "text_anlyzer", // 插入时使用
        "search_analyzer": "ik_smart", // 查询时使用
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
        "type": "keyword"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword",
        "copy_to": "all"
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
        "analyzer": "text_anlyzer", // 插入时使用
        "search_analyzer": "ik_smart" // 查询时使用
      },
      "suggestion":{
          "type": "completion",
          "analyzer": "completion_analyzer" // 自动补全
      }
    }
  }
}
```

#### 修改实体类

添加属性

```java
private List<String> suggestion;

// 为suggestion赋值
List<String> suggestionList = new ArrayList<>(Arrays.asList(hotel.getName(), hotel.getCity()));
if (hotel.getBusiness().contains("/")) {
    // Business可能会以/分割,所以想要自动补全得拆出来
    String[] split = hotel.getBusiness().split("/");
    Collections.addAll(suggestionList, split);
}else {
    suggestionList.add(hotel.getBusiness());
}
this.suggestion = suggestionList;
```

#### 重新导入数据

![image-20230507225601685](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507225601685.png)

#### 自动补全测试

```java
GET /hotel/_search
{
  "suggest": {
    "suggestions": {
      "text": "sh",
      "completion":{
        "field":"suggestion",
        "skip_duplicates":true,
        "size":10
      }
    }
  }
}
```



#### RestClient测试自动补全

>  suggest和query同级,所以在search.score()后面加DSL即可。

```java
@Test
public void testSuggester() throws IOException {

    // 1 创建查询请求
    SearchRequest searchRequest = new SearchRequest("hotel");
    // 2 suggester DSL
    searchRequest.source().suggest(new SuggestBuilder()
            .addSuggestion("suggestions", // 补全名称
                    SuggestBuilders.completionSuggestion("suggestion") // 补全字段
                            .prefix("sh") // 搜索框输入前缀,根据这个自动补全
                            .skipDuplicates(true) // 跳过重复
                            .size(10) // 显示十条
            )
    );
    // 3 发出查询请求
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    // 4 解析结果
    // 返回自动补全数据
    List<String> result = new ArrayList<>();
    Suggest suggest = searchResponse.getSuggest();
    // 和上面的补全方式对应
    CompletionSuggestion completionSuggestion = suggest.getSuggestion("suggestions");
    List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
    for (CompletionSuggestion.Entry.Option option : options) {
        result.add(option.getText().string());
    }
    log.info(result.toString());
}
```

![image-20230507233715041](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/es_search/image-20230507233715041.png)



### 黑马案例自动补全

自动补全请求：

```tex
http://localhost:8089/hotel/suggestion?key=sh
```

创建接口

```java
/**
 * 自动补全
 * @param key
 * @return
 */
@RequestMapping(method = RequestMethod.GET, value = "/suggestion")
private List<String> suggestion(@RequestParam("key") String key) {
    return hotelService.suggestion(key);
}
```

service

```java
public List<String> suggestion(String key) {
    // 1 创建查询请求
    SearchRequest searchRequest = new SearchRequest("hotel");
    // 2 自动补全dsl
    searchRequest.source().suggest(new SuggestBuilder()
            .addSuggestion("suggestions", SuggestBuilders
                    .completionSuggestion("suggestion")
                    .skipDuplicates(true)
                    .size(10)
                    .prefix(key)
            )
    );
    // 3 发起请求
    SearchResponse searchResponse;
    try {
        searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
        throw new RuntimeException("异常", e);
    }
    // 4 解析结果
    List<String> result = new ArrayList<>();
    final Suggest suggest = searchResponse.getSuggest();
    CompletionSuggestion suggestion = suggest.getSuggestion("suggestions");
    List<CompletionSuggestion.Entry.Option> options = suggestion.getOptions();
    options.forEach(option -> result.add(option.getText().string()));
    return result;
}
```



## 数据同步

### 同步调用

> 更新Mysql时远程同步调用ES的更新接口。

### MQ异步通知

> 更新Mysql时将需要更新的数据ID发送到消息队列,ES服务监听队列并消费消息,更新es。

### 监听BinLog

> 开启Mysql BinLog, 引入Canal(利用Mysql主从原理)中间件监听Binlog 变化, 将更新同步到es。

### 对比

- 同步调用
  - 优点：实现简单，粗暴
  - 缺点：业务耦合度高
- 异步通知
  - 优点：低耦合，实现难度一般
  - 缺点：依赖mq的可靠性
- 监听binlog
  - 优点：完全解除服务间耦合
  - 缺点：开启binlog增加数据库负担、实现复杂度高



### 利用MQ实现数据同步

利用课前资料提供的hotel-admin项目作为酒店管理的微服务。当酒店数据发生增、删、改时，要求对elasticsearch中数据也要完成相同操作。

步骤：

- 导入课前资料提供的hotel-admin项目，启动并测试酒店数据的CRUD

- 声明exchange、queue、RoutingKey

- 在hotel-admin中的增、删、改业务中完成消息发送

- 在hotel-demo中完成消息监听，并更新elasticsearch中数据

- 启动并测试数据同步功能

#### 分析

> 消息生产者和消息消费者

- hotel-admin是消息生产者
  - 生产消息
- hotel-demo是消息消费者
  - 消费消息
  - 创建listener监听队列变化

> 消息类型

- 消息为Long类型的id

  > MQ需要内存,网络传输也需要带宽,数据越小越好

> 交换机、消息队列、RoutingKey

- 交换机名称   hotel.topic

- 队列名称

  > 数据更新存在以下三种情况
  >
  > - 新增
  > - 修改
  > - 删除
  >
  > 新增和修改作为一类成为insert(mq中插入重复id数据就是修改)
  >
  > 删除成为delete
  >
  > 所以有两个消息队列：
  >
  > - hotel.insert.queue   更新
  > - hotel.delete.queue

- routingkey

  > - hotel.insert
  > - hotel.delete

#### 实现

##### 定义交换机等

> 定义交换机、队列、绑定关系

```java
public class HotelConstants {

    /**
     * topic交换机名称
     */
    public static final String EXCHANG_HOTEL_TOPIC = "hotel.topic";

    /**
     * 更新队列
     */
    public static final String HOTEL_INSERT_QUEUE = "hotel.insert.queue";
    
    /**
     * 删除队列
     */
    public static final String HOTEL_DELETE_QUEUE = "hotel.delete.queue";
    
    /**
     * 更新routingkey
     */
    public static final String ROUTINGKEY_HOTEL_INSERT = "hotel.insert";
    
    /**
     * 删除routingkey
     */
    public static final String ROUTINGKEY_HOTEL_DELETE = "hotel.delete";

    /**
     * 索引库名称
     */
    public static final String ES_INDEX_NAME = "hotel";
}
```



##### 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

##### 配置mq

```yml
spring:
  rabbitmq:
    host: 10.211.55.4
    port: 5672 # 端口
    virtual-host: / # 虚拟主机
    username: rolyfish # 用户名
    password: 123456 # 密码
    listener:
      simple:
        prefetch: 1 # 限制消费者每次只取一个消息,消息处理完成再去消息队列取消息
```

##### 声明交换机等

```java
@Component
public class MqConfig {
    /**
     * 声明交换机topic
     */
    protected @Bean TopicExchange topicExchange() {
        return new TopicExchange(HotelConstants.EXCHANG_HOTEL_TOPIC);
    }
    /**
     * 声明队列  insertQueue
     */
    protected @Bean Queue insertQueue() {
        return new Queue(HotelConstants.HOTEL_INSERT_QUEUE, true);
    }
    /**
     * 声明队列  deleteQueue
     */
    protected @Bean Queue deleteQueue() {
        return new Queue(HotelConstants.HOTEL_DELETE_QUEUE, true);
    }
    /**
     * insertQueue与交换机绑定
     */
    protected @Bean Binding topicExchangeBandingInsertQueue(@Autowired TopicExchange topicExchange, @Autowired /*@Qualifier("fanoutQueue1") */Queue insertQueue) {
        return BindingBuilder.bind(insertQueue).to(topicExchange).with(HotelConstants.ROUTINGKEY_HOTEL_INSERT);
    }
    /**
     * deleteQueue与交换机绑定
     */
    protected @Bean Binding topicExchangeBandingDeleteQueue(@Autowired TopicExchange topicExchange, @Autowired /*@Qualifier("fanoutQueue1") */Queue deleteQueue) {
        return BindingBuilder.bind(deleteQueue).to(topicExchange).with(HotelConstants.ROUTINGKEY_HOTEL_DELETE);
    }
}
```

##### 注册监听器

> 注册监听器监听队列变化,消费消息

```java
@Slf4j
@Component
public class MqListener {
    @Autowired
    IHotelService hotelService;
    @Autowired
    RestHighLevelClient client;
    @RabbitListener(queues = {HotelConstants.HOTEL_INSERT_QUEUE})
    public void listenerInsert(Long id) {
        // 0 查询数据转化成 hotelDoc
        Hotel hotel = hotelService.getById(id);
        HotelDoc hotelDoc = new HotelDoc(hotel);
        // 1 创建请求
        IndexRequest indexRequest = new IndexRequest(ES_INDEX_NAME);
        // 2 dsl
        indexRequest.id(id.toString()).source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        // 3 发送请求
        IndexResponse indexResponse;
        try {
            indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("异常", e);
        }
        log.info("结果:{}", indexResponse.getResult().toString());
    }
    @RabbitListener(queues = {HotelConstants.HOTEL_DELETE_QUEUE})
    public void listenerDelete(Long id) {
        // 1 创建请求
        DeleteRequest deleteRequest = new DeleteRequest(ES_INDEX_NAME);
        deleteRequest.id(id.toString());
        // 2 发送请求
        DeleteResponse deleteResponse;
        try {
            deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("异常", e);
        }
        log.info("结果:{}", deleteResponse.getResult().toString());
    }
}
```

##### 生产消息

```java
@Autowired
private IHotelService hotelService;
@Autowired
RabbitTemplate rabbitTemplate;
@PostMapping
public void saveHotel(@RequestBody Hotel hotel) {
    boolean b = hotelService.save(hotel);
    if (b) {
        // 发送消息
        rabbitTemplate.convertAndSend(EXCHANG_HOTEL_TOPIC, ROUTINGKEY_HOTEL_INSERT, hotel.getId());
    }
}
@PutMapping()
public void updateById(@RequestBody Hotel hotel) {
    if (hotel.getId() == null) {
        throw new InvalidParameterException("id不能为空");
    }
    boolean b = hotelService.updateById(hotel);
    if (b) {
        // 发送消息
        rabbitTemplate.convertAndSend(EXCHANG_HOTEL_TOPIC, ROUTINGKEY_HOTEL_INSERT, hotel.getId());
    }
}
@DeleteMapping("/{id}")
public void deleteById(@PathVariable("id") Long id) {
    boolean b = hotelService.removeById(id);
    if (b) {
        rabbitTemplate.convertAndSend(EXCHANG_HOTEL_TOPIC, ROUTINGKEY_HOTEL_DELETE, id);
    }
}
```

