# springcloud-study

### 模块说明 

| 项目        | 说明   |
| --------   | -----  |
| cloud-api      | 公共接口、实体类   |
| cloud-eureka-900X       |   eureka集群   |
| cloud-provider-700X        |    提供者集群 |
| cloud-customer        |    消费者 通过ribbon负载均衡调用服务 |
| cloud-customer-feign        |    消费者 通过feign方式调用restful |
| cloud-zuul-9527        |    zuul网关 |
| cloud-config-server        |    配置中心单机 |
| cloud-config-server-eureka        |    分布式配置注册到eureka |
| cloud-config-client       |    普通读取配置 |
| cloud-config-client-eureka     |    从eureka中读取配置 |
