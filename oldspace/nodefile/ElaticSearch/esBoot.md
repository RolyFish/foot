

`ElasticsearchRestClientAutoConfiguration`

1、使`ElasticsearchRestClientProperties`和配置文件关联生效

2、注册`RestClientBuilder`

3、注册`RestHighLeaveClient`

`ElasticsearchDataAutoConfiguration`

1、`ElasticSearchDataConfigration`下的`RestClientConfigration`  注册了一个`ElasticSearchRestTemplate`（`ElasticsearchOprations`的实现类）

