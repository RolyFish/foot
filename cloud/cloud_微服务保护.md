## 雪崩问题及解决方案



### 雪崩问题



#### 雪崩问题

> 由于微服务中某个服务不可用从而导致整个微服务不可用,称为微服务雪崩。

导致服务不可用的原因：

- 网路
- GC
- 代码

导致雪崩的原因：

由于某个服务不可用,导致依赖于此服务的其他服务阻塞,从而耗尽资源(连接资源、内存资源、cpu资源等),形成级联失败就会导致整个微服务不可用。



> 微服务雪崩问题常见处理方式：

- 超时处理
- 苍壁模式
- 断路器
- 限流

### 超时处理

> 设定超时时间,请求超过一定时间没有响应就返回错误信息,不会无休止等待,释放资源。

超时处理只能一定程度上解决雪崩问题,一但超时时间内并发较高,还是会导致整个服务压力过大。



### 苍壁模式

> 限定每个业务能使用的线程数，避免耗尽整个tomcat的资源，因此也叫线程隔离。

雪崩问题可能只是由于某个业务出现不可用,导致整个服务器(tomcat)压力过大,所以仓壁模式采用限制每个业务可用资源数,来避免不可用业务对其他业务的影响。但是此模式下不可用业务还是占用了一些资源。



<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20210715173215243.png" alt="image-20210715173215243" style="zoom:50%;" />

### 熔断降级

> 由**断路器**统计业务执行的异常比例,如果超出阈值则会**熔断**该业务,拦截访问该业务的一切请求。

熔断降级模式,通过断路器统计业务异常比例,超过阀值将会拦截此业务的一切请求,快速释放资源。此模式下不可用业务将不会占用服务器资源。



断路器会统计访问某个服务的请求数量，异常比例：

<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508182221497.png" alt="image-20230508182221497" style="zoom:50%;" />

### 流量控制

> **流量控制**：限制业务访问的QPS，避免服务因流量的突增而故障。

![image-20210715173555158](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20210715173555158.png)



### 小结

什么是雪崩问题？

> 微服务中由于某个服务不可用(GC、网络、超时、代码),导致依赖于此服务的其他服务产生级联失败,导致整个微服务不可用。

解决方案：

- 超时处理

  > 服务已经不可用,避免整个微服务不可用

- 仓壁模式

  > 限制业务最大资源, 避免不可用业务对其他业务的影响。不可用业务还是会占据一定资源

- 服务降级

  > 通过断路器统计业务失败比例,超过一定阀值,将不再允许请求该业务

- 流量控制

  >  **限流**是对服务的保护，避免因瞬间高并发流量而导致服务故障，进而避免雪崩。是一种**预防**措施。
  >
  > 
  >
  > **超时处理、线程隔离、降级熔断**是在部分服务故障时，将故障控制在一定范围，避免雪崩。是一种**补救**措施。



## 服务保护技术对比

在SpringCloud当中支持多种服务保护技术：

- [Netfix Hystrix](https://github.com/Netflix/Hystrix)  -- 不再维护
- [Sentinel](https://github.com/alibaba/Sentinel)
- [Resilience4J](https://github.com/resilience4j/resilience4j)

早期比较流行的是Hystrix框架，但目前国内实用最广泛的还是阿里巴巴的Sentinel框架，这里我们做下对比：

|                | **Sentinel**                                   | **Hystrix**                   |
| -------------- | ---------------------------------------------- | ----------------------------- |
| 隔离策略       | 信号量隔离                                     | 线程池隔离/信号量隔离         |
| 熔断降级策略   | 基于慢调用比例或异常比例                       | 基于失败比率                  |
| 实时指标实现   | 滑动窗口                                       | 滑动窗口（基于 RxJava）       |
| 规则配置       | 支持多种数据源                                 | 支持多种数据源                |
| 扩展性         | 多个扩展点                                     | 插件的形式                    |
| 基于注解的支持 | 支持                                           | 支持                          |
| 限流           | 基于 QPS，支持基于调用关系的限流               | 有限的支持                    |
| 流量整形       | 支持慢启动、匀速排队模式                       | 不支持                        |
| 系统自适应保护 | 支持                                           | 不支持                        |
| 控制台         | 开箱即用，可配置规则、查看秒级监控、机器发现等 | 不完善                        |
| 常见框架的适配 | Servlet、Spring Cloud、Dubbo、gRPC  等         | Servlet、Spring Cloud Netflix |



## Sentinel介绍安装

[Sentinel](https://sentinelguard.io/zh-cn/index.html)是阿里巴巴开源的一款微服务流量控制组件。

Sentinel 具有以下特征:

• **丰富的应用场景**：Sentinel 承接了阿里巴巴近 10 年的双十一大促流量的核心场景，例如秒杀（即突发流量控制在系统容量可以承受的范围）、消息削峰填谷、集群流量控制、实时熔断下游不可用应用等。

• **完备的实时监控**：Sentinel 同时提供实时的监控功能。您可以在控制台中看到接入应用的单台机器秒级数据，甚至 500 台以下规模的集群的汇总运行情况。

•**广泛的开源生态**：Sentinel 提供开箱即用的与其它开源框架/库的整合模块，例如与 Spring Cloud、Dubbo、gRPC 的整合。您只需要引入相应的依赖并进行简单的配置即可快速地接入 Sentinel。

•**完善的** **SPI** **扩展点**：Sentinel 提供简单易用、完善的 SPI 扩展接口。您可以通过实现扩展接口来快速地定制逻辑。例如定制规则管理、适配动态数据源等。



### 安装Sentinel

1. 下载

   > sentinel官方提供了UI控制台，方便我们对系统做限流设置。[GitHub地址](https://github.com/alibaba/Sentinel/releases)。
   >
   > 这是一个SpringBoot项目

2. 启动

   将jar包放到任意非中文目录，执行命令：

   ```sh
   java -jar sentinel-dashboard-1.8.1.jar
   ```

   如果要修改Sentinel的默认端口、账户、密码，可以通过下列配置：

   | **配置项**                       | **默认值** | **说明**   |
   | -------------------------------- | ---------- | ---------- |
   | server.port                      | 8080       | 服务端口   |
   | sentinel.dashboard.auth.username | sentinel   | 默认用户名 |
   | sentinel.dashboard.auth.password | sentinel   | 默认密码   |

   例如，修改端口：

   ```sh
   java -Dserver.port=8090 -Dsentinel.dashboard.auth.username=rolyfish -Dsentinel.dashboard.auth.password=123456 -jar sentinel-dashboard-2.0.0-alpha-preview.jar
   ```

3. 登录控制台

   > `localhost:8090`

   ![image-20230508185900518](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508185900518.png)

### 导入demo

#### docker安装nacos

[nacos官网](https://github.com/alibaba/nacos) 

[nacos docker hup](https://hub.docker.com/r/nacos/nacos-server)

1. 拉取镜像

   ```shell
   docker pull nacos/nacos-server:v2.2.2-slim
   ```

2. 启动nacos

   ```shell
   docker run --name nacos-standalone \
   -e MODE=standalone \
   -p 8848:8848 \
   -d nacos/nacos-server:v2.2.2-slim 
   ```

3. 查看日志

   ```shell
   docker logs -f nacos-standalone
   ```

4. 访问nacos管理页面

   `http://localhost:8848/nacos/index.html`

#### 初始化数据

```sql
-- 创建数据库并执行sql
create database  cloud_order;
create database  cloud_user;
```



####  启动微服务

![image-20230508201420868](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508201420868.png)



### 微服务整合Sentinel

order-service中整合sentinel

1. 引入依赖

   ```xml
   <!--sentinel-->
   <dependency>
       <groupId>com.alibaba.cloud</groupId> 
       <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
   </dependency>
   ```

2. 配置sentinel控制台

   ```yaml
   server:
     port: 8088
   spring:
     cloud: 
       sentinel:
         transport:
           dashboard: localhost:8090
   ```

3. 请求order-service接口，查看sentinel控制台

   `http://localhost:8088/order/101`

   ![image-20230508202335898](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508202335898.png)



## 流量控制

雪崩问题虽然有四种方案，但是限流是避免服务因突发的流量而发生故障，是对微服务雪崩问题的预防。我们先学习这种模式。



### 簇点链路

当请求进入微服务时，首先会访问DispatcherServlet，然后进入Controller、Service、Mapper，这样的一个调用链就叫做**簇点链路**。簇点链路中被监控的每一个接口就是一个**资源**。

默认情况下sentinel会监控SpringMVC的每一个端点（Endpoint，也就是controller中的方法），因此SpringMVC的每一个端点（Endpoint）就是调用链路中的一个资源。

![image-20230508203531801](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508203531801.png)

流控、熔断等都是针对簇点链路中的资源来设置的，因此我们可以点击对应资源后面的按钮来设置规则：

- 流控：流量控制
- 降级：降级熔断
- 热点：热点参数限流，是限流的一种
- 授权：请求的权限控制



### 快速入门

#### 示例

> 为端点(资源,Controller方法)/order/{orderId}做限流,限制qps 为 5/s。

![image-20230508203735394](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508203735394.png)

#### 使用Jmeter测试

![image-20230508203951426](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508203951426.png)

结果：

![image-20230508204133469](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508204133469.png)

sentinel控制台：

![image-20230508204342479](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508204342479.png)



### 流控模式

在添加限流规则时，点击高级选项，可以选择三种**流控模式**：

- 直接：统计当前资源的请求，触发阈值时对当前资源直接限流，也是默认的模式
- 关联：统计与当前资源相关的另一个资源，触发阈值时，对当前资源限流
- 链路：统计从指定链路访问到本资源的请求，触发阈值时，对指定链路限流

![image-20230508204918195](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508204918195.png)





#### 直接模式

> 入门案例测的就是直连

#### 关联模式

> **关联模式**：统计与当前资源相关的==另一个资源,触发阈值时==,==对当前资源限流==

- 在OrderController新建两个端点(资源、接口)：/order/query和/order/update，无需实现业务

- 配置流控规则，当/order/ update资源被访问的QPS超过5时，对/order/query请求限流

1. 创建端点

   ```java
   @GetMapping("query")
   public String query() {
       return "查询订单";
   }
   
   @GetMapping("update")
   public String update() {
       return "更新订单";
   }
   ```

2. 配置流控规则,关联模式。当update的qps打到5,对query限流

   ![image-20230508211148802](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508211148802.png)

3. Jmeter测试

   ![image-20230508211305972](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508211305972.png)

4. sentinel控制台

   > update请求没有限流都成功了,query被限流。

   ![image-20230508211454615](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508211454615.png)

   ![image-20230508211420440](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508211420440.png)

5. 应用场景

   > 当两个接口存在资源竞争的情况, 优先级高的端点关联优先级低的端点。
   >
   > 当优先级高的端点达到阀值,对优先级低的端点限流。

   

#### 链路模式

**链路模式**：只针对从指定链路访问到本资源的请求做统计，判断是否超过阈值。

查询订单和创建订单业务,两者都需要查询商品。针对从查询订单进入到查询商品的请求统计,并设置限流。

步骤：

1. 在OrderService中添加一个queryGoods方法，不用实现业务
2. 在OrderController中，改造/order/query端点，调用OrderService中的queryGoods方法
3. 在OrderController中添加一个/order/save的端点，调用OrderService的queryGoods方法
4. 给queryGoods设置限流规则，从/order/query进入queryGoods的方法限制QPS必须小于2



1. service：

```java
public void queryGoods() {
    System.err.println("查询商品");
}
```

2. controller:

```java
@GetMapping("query")
public String query() {
    orderService.queryGoods();
    return "查询订单";
}

@GetMapping("save")
public String save() {
    orderService.queryGoods();
    return "保存订单";
}
```

3. 监控service中的方法

   > Sentinel默认只监控所有Controller方法

   ```java
   @SentinelResource(value = "goods")
   public void queryGoods() {
       System.err.println("查询商品");
   }
   ```

   > 并且sentinel默认会给进入SpringMVC的所有请求设置同一个root资源，会导致链路模式失效。

   ```yaml
   spring:
     cloud:
       sentinel:
         web-context-unify: false # 关闭context整合
   ```

4. 重启服务配置链路规则

   > 两个链路就被监控了

   ![image-20230508221433760](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508221433760.png)

   > 配置链路规则,限制/order/query进入queryGoods的方法限制QPS必须小于2

   ![image-20230508221617228](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508221617228.png)

5. 测试

   ![image-20230508221731579](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508221731579.png)

   结果：

   ![image-20230508222247940](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508222247940.png)



### 流控效果

在流控的高级选项中，还有一个流控效果选项：

![image-20230508223018917](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508223018917.png)

流控效果是指请求达到流控阈值时应该采取的措施，包括三种：

- 快速失败：达到阈值后，新的请求会被立即拒绝并抛出FlowException异常,响应码429。是默认的处理方式。

- warm up：预热模式，对超出阈值的请求同样是拒绝并抛出异常。但这种模式阈值会动态变化，从一个较小值逐渐增加到最大阈值。

- 排队等待：让所有的请求按照先后次序排队执行，两个请求的间隔不能小于指定时长

#### warm up

阈值一般是一个微服务能承担的最大QPS，但是一个服务刚刚启动时，一切资源尚未初始化（**冷启动**），如果直接将QPS跑到最大值，可能导致服务瞬间宕机。

warm up也叫**预热模式**，是应对服务冷启动的一种方案。请求阈值初始值是 maxThreshold / coldFactor，持续指定时长后，逐渐提高到maxThreshold值。coldFactor的默认值是3.

1. 配置流控效果

   ![image-20230508223405491](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508223405491.png)

2. 测试

   ![image-20230508223525324](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508223525324.png)







#### 排队等待

当请求超过QPS阈值时，快速失败和warm up 会拒绝新的请求并抛出异常。

而排队等待则是让所有请求进入一个队列中，然后按照阈值允许的时间间隔依次执行。后来的请求必须等待前面执行完成，如果请求预期的等待时间超出最大时长，则会被拒绝。

工作原理

例如：QPS = 5，意味着每200ms处理一个队列中的请求；timeout = 2000，意味着**预期等待时长**超过2000ms的请求会被拒绝并抛出异常。

那什么叫做预期等待时长呢？

比如现在一下子来了12 个请求，因为每200ms执行一个请求，那么：

- 第6个请求的**预期等待时长** =  200 * （6 - 1） = 1000ms
- 第12个请求的预期等待时长 = 200 * （12-1） = 2200ms



给/order/{orderId}这个资源设置限流，最大QPS为10，利用排队的流控效果，超时时长设置为5s

1. 配置流控效果

   ![image-20230508224315284](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508224315284.png)

2. 测试

   ![image-20230508224419876](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508224419876.png)

   ![image-20230508224816259](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230508224816259.png)



#### 小结

流控效果有哪些？

- 快速失败：QPS超过阈值时，拒绝新的请求

- warm up： QPS超过阈值时，拒绝新的请求；QPS阈值是逐渐提升的，可以避免冷启动时高并发导致服务宕机。

- 排队等待：请求会进入队列，按照阈值允许的时间间隔依次执行请求；如果请求预期等待时长大于超时时间，直接拒绝



### 热点参数限流

之前的限流是统计访问某个资源的所有请求，判断是否超过QPS阈值。而热点参数限流是**分别统计参数值相同的请求**，判断是否超过QPS阈值。

**注意事项**：热点参数限流对默认的SpringMVC资源无效，需要利用@SentinelResource注解标记资源

**案例需求**：给/order/{orderId}这个资源添加热点参数限流，规则如下：

•默认的热点参数规则QPS是2

•给102这个参数设置例外：QPS为4

•给103这个参数设置例外：QPS为10

1. 修改代码,监控参数

   ```java
   @SentinelResource(value = "hot")
   @GetMapping("{orderId}")
   public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
       // 根据id查询订单并返回
       return orderService.queryOrderById(orderId);
   }
   ```

2. 重启

   ![image-20230509000446525](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509000446525.png)

3. 配置热点参数限流规则

   ![image-20230509000749026](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509000749026.png)

4. 测试

   > 预期结果是 103不受影响, 102 20%请求失败, 101 60%请求失败

   ![image-20230509001010942](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509001010942.png)

   ![image-20230509001540682](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509001540682.png)



## 隔离和降级

> 限流是一种预防措施,避免因高并发而引起的服务故障,但服务还会因为其它原因而故障。

而要将这些故障控制在一定范围，避免雪崩，就要靠**线程隔离**（舱壁模式）和**熔断降级**手段了。隔离和降级都是为了保护服务调用者不受服务提供者的影响导致级联失败。

- 线程隔离(仓壁模式)：为业务分配固定线程数或设置信号量,达到阀值则不再接受请求。避免故障业务影响到其他业务
- 熔断降级：是在调用方这边加入断路器，统计对服务提供者的调用，如果调用的失败比例过高，则熔断该业务，不允许访问该服务的提供者了。

### FeignClient整合Sentinel

SpringCloud中,微服务之间是通过Feign通信的,因此做客户端保护必须整合Feign和Sentinel。

#### 开启Sentinel支持

> 修改服务调用者(order-service)yml文件。

```yaml
feign:
  sentinel:
    enabled: true # 开启feign对sentinel的支持
```

#### 编写失败降级逻辑

业务失败后，不能直接报错，而应该返回用户一个友好提示或者默认结果，这个就是失败降级逻辑。

给FeignClient编写失败后的降级逻辑

①方式一：FallbackClass，无法对远程调用的异常做处理

②方式二：FallbackFactory，可以对远程调用的异常做处理，我们选择这种

1. 定义降级逻辑类

   > 在Feignt-api定义失败降级逻辑类

   ```java
   @Slf4j
   public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
       @Override
       public UserClient create(Throwable throwable) {
           return new UserClient() {
               @Override
               public User findById(Long id) {
                   log.error("查询用户异常", throwable);
                   return new User();
               }
           };
       }
   }
   ```

2. 注入UserClientFallBackFactory,给Spring管理

   ```java
   @Bean
   public UserClientFallBackFactory userClientFallBackFactory() {
       return new UserClientFallBackFactory();
   }
   ```

3. 整合FeignClient

   ```java
   @FeignClient(value = "userservice", fallbackFactory = UserClientFallbackFactory.class)
   public interface UserClient {
   }
   ```

4. 重启

   > 重启出现循环依赖问题, 对照[spring-cloud-alibaba版本说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)

5. 测试

   ![image-20230509014234499](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509014234499.png)

### 线程隔离(仓壁模式)

线程隔离有两种方式实现：

- 线程池隔离
  - 优点
    - 支持主动超时

    - 支持异步调用

  - 缺点
    - 每个远程调用都会创建新的线程,开销较大

  - 场景
    - 适合低善出场景

- 信号量隔离（Sentinel默认采用）
  - 优点
    - 简单,无额外开销。只有一个计数器,一次远程调用计数器减一,远程代用结束归还计数器
  - 缺点
    - 不支持主动超时。只能依赖于feign的超时
    - 不支持异步调用
  - 场景
    - 适合高扇出场景



#### Sentinel的线程隔离

在添加限流规则时，可以选择两种阈值类型：

![image-20230509020251937](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509020251937.png)

- QPS：就是每秒的请求数

- 线程数：也就是信号量计数器

#### 例子

**案例需求**：给 order-service服务中的UserClient的查询用户接口设置流控规则,线程数不能超过 2。然后利用jemeter测试。

1. 配置线程隔离规则

   ![image-20230509020418723](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509020418723.png)

2. jmeter测试

   > 并发为10,我们配置的信号量技术器为2,当超过2个并发远程调用UserClient时就会拒绝

   ![image-20230509020517563](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509020517563.png)

   > 超过阀值2,就会进入熔断

   ![image-20230509021031134](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509021031134.png)

   ![image-20230509020757555](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509020757555.png)

#### 小结

线程隔离的两种手段是？

- 信号量隔离

- 线程池隔离

信号量隔离的特点是？

- 基于计数器模式，简单，开销小

线程池隔离的特点是？

- 基于线程池模式，有额外开销，但隔离控制更强



### 熔断降级

> 熔断降级是解决雪崩问题的重要手段。其思路是由**断路器**统计服务调用的异常比例、慢调用比例，如果超出阈值则会**熔断**该服务。
>
> 即拦截访问该服务的一切请求；而当服务恢复时，断路器会放行访问该服务的请求。

断路器控制熔断和放行是通过状态机来完成的：

![image-20210716130958518](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20210716130958518.png)

状态机包括三个状态：

- closed：关闭状态，断路器放行所有请求，并开始统计异常比例、慢请求比例。超过阈值则切换到open状态
- open：打开状态，服务调用被**熔断**，访问被熔断服务的请求会被拒绝，快速失败，直接走降级逻辑。Open状态5秒后会进入half-open状态
- half-open：半开状态，放行一次请求，根据执行结果来判断接下来的操作。
  - 请求成功：则切换到closed状态
  - 请求失败：则切换到open状态



断路器熔断策略有三种：慢调用、异常比例、异常数

#### 慢调用比例

> **慢调用**：业务的响应时长(RT)大于指定时长的请求认定为慢调用请求。在断路器统计时长内,如果慢调用请求数量超过设定的阀值,则触发熔断。

需求：给 UserClient的查询用户接口设置降级规则,慢调用的RT阈值为50ms,统计时间为1秒,最小请求数量为5,失败阈值比例为0.4,熔断时长为5

1. 模拟慢调用

   ```java
   @GetMapping("/{id}")
   public User queryById(@PathVariable("id") Long id,
                         @RequestHeader(value = "Truth", required = false) String truth) throws InterruptedException {
       System.out.println("truth: " + truth);
       if (id == 1) {
           Thread.sleep(60);
       }
       return userService.queryById(id);
   }
   ```

   

2. 设置慢调用规则

   ![image-20230509023617649](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509023617649.png)

3. 测试

   ![image-20230509023951735](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509023951735.png)

#### 异常比例、异常数

> **异常比例或异常数**：统计指定时间内的调用，如果调用次数超过指定请求数，并且出现异常的比例达到设定的比例阈值（或超过指定异常数），则触发熔断。

需求：给 UserClient的查询用户接口设置降级规则，统计时间为1秒，最小请求数量为5，失败阈值比例为0.4，熔断时长为5s

1. 模拟远程调用异常

   ```java
   @GetMapping("/{id}")
   public User queryById(@PathVariable("id") Long id,
                         @RequestHeader(value = "Truth", required = false) String truth) throws InterruptedException {
       System.out.println("truth: " + truth);
       if (id == 1) {
           Thread.sleep(60);
       }
       if (id == 2) {
           throw new RuntimeException("模拟异常");
       }
       return userService.queryById(id);
   }
   ```

2. 设置熔断规则

   ![image-20230509024325108](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509024325108.png)

3. 测试

   ![image-20230509024627031](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509024627031.png)





## 授权规则

> Sentinel除了可以做限流、线程隔离、熔断降级外,还可以做权限校验,对请求方来源做判断和控制。

> SpringCloudGetway也可以做权限校验,为什么还需要Sentinel？
>
> Getway只会对进入网关的请求做校验,如果服务的地址暴露了,那么就可以绕过网关。而Sentinel则可以避免这种情况,Sentinel会对请求来源做判断(浏览器、网关?),符合要求的请求才会放行。



### 黑白名单

授权规则可以对调用方的来源做控制，有白名单和黑名单两种方式。

- 白名单：来源（origin）在白名单内的调用者允许访问

- 黑名单：来源（origin）在黑名单内的调用者不允许访问

![image-20230509142053375](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509142053375.png)

#### 如何获取请求来源名称

Sentinel是通过RequestOriginParser这个接口的parseOrigin来获取请求的来源的。

```java
public interface RequestOriginParser {
    /**
     * 从请求request对象中获取origin，获取方式自定义
     */
    String parseOrigin(HttpServletRequest request);
}
```

在order-service中实现接口,并注入交由Spring管理

```java
@Component
public class HeaderOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        String origin = httpServletRequest.getHeader("origin");
        if (StringUtils.isBlank(origin)) {
            origin = "default";
        }
        return origin;
    }
}
```

#### 网关添加请求头

> 正常流程中,所有请求都会进入网关,执行完所有过滤器,符合条件后路由到指定服务。
>
> 在网关中可以对请求添加请求头。

过滤器类型：default-filters

```java
default-filters:
  - AddRequestHeader=origin,getway
```



#### 配置授权规则

![image-20230509145114623](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509145114623.png)

#### 测试

![image-20230509150001163](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509150001163.png)

### 自定义异常

默认情况下，发生限流、降级、授权拦截时，都会抛出异常到调用方。异常结果都是flow limmiting（限流）。这样不够友好，无法得知是限流还是降级还是授权拦截。

#### 异常类型

而如果要自定义异常时的返回结果，需要实现Sentinel提供的接口,BlockExceptionHandler

```java
public interface BlockExceptionHandler {
    void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception;
}
```

此接口只有一个默认实现类：DefaultBlockExceptionHandler

```java
public class DefaultBlockExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        response.setStatus(429);
        out.print("Blocked by Sentinel (flow limiting)");
        out.flush();
        out.close();
    }
}
```

handle方法有三个参数：

- HttpServletRequest request：request对象

- HttpServletResponse response：response对象,输出结果

- BlockException e：被sentinel拦截时抛出的异常

  > BlockException的子类：
  >
  > - FlowException 限流异常
  > - ParamFlowException 热点参数限流异常
  > - DegradeException  降级异常
  > - AuthorityException 授权异常
  > - SystemBlockException  系统规则异常

#### 自定义异常

> 自定义异常处理类,覆盖自动装配的默认异常处理类：

```java
@Component
public class SentinelExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        String msg = "未知异常";
        int status = 429;
        if (e instanceof AuthorityException) {
            msg = "权限校验异常";
            status = 401;
        } else if (e instanceof DegradeException) {
            msg = "降级异常";
        } else if (e instanceof FlowException) {
            msg = "限流异常";
        } else if (e instanceof ParamFlowException) {
            msg = "热点参数限流异常";
        } else if (e instanceof SystemBlockException) {
            msg = "系统规则异常";
        }
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(status);
        final PrintWriter writer = response.getWriter();
        writer.println("{\"msg\": " + msg + ", \"status\": " + status + "}");
        writer.flush();
        writer.close();
    }
}
```



##### 测试

1. 权限校验异常

   ![image-20230509154919190](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509154919190.png)

2. 限流

   ![image-20230509155035824](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509155035824.png)

3. 熔断降级异常

   ![image-20230509160036487](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/cloud_%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BF%9D%E6%8A%A4/image-20230509160036487.png)






## 持久化

> 开源的Sentinel未提供持久化。企业级的Sentinel可以持久化。

> 可以改源码

规则是否能持久化，取决于规则管理模式，sentinel支持三种规则管理模式：

- 原始模式：Sentinel的默认模式，将规则保存在内存，重启服务会丢失。

- pull模式

  > 控制台将配置的规则推送到Sentinel客户端，而客户端会将配置规则保存在本地文件或数据库中。以后会定时去本地文件或数据库中查询，更新本地规则。

- push模式

  > 控制台将配置规则推送到远程配置中心，例如Nacos。Sentinel客户端监听Nacos，获取配置变更的推送消息，完成本地配置更新。

### push模式

push模式：控制台将配置规则推送到远程配置中心，例如Nacos。Sentinel客户端监听Nacos，获取配置变更的推送消息，完成本地配置更新。





#### 实现push模式

> 哥们又失败了~

修改OrderService，让其监听Nacos中的sentinel规则配置。

1. 引入依赖

   ```xml
   <dependency>
       <groupId>com.alibaba.csp</groupId>
       <artifactId>sentinel-datasource-nacos</artifactId>
   </dependency>
   ```

2. 启动nacos并配置持久化

   ```shell
    bin/startup.sh -m standalone
   ```

3. 配置nacos地址

   ```yaml
   spring:
     cloud:
       sentinel:
         datasource:
           flow:
             nacos:
               server-addr: localhost:8848 # nacos地址
               dataId: orderservice-flow-rules
               groupId: SENTINEL_GROUP
               rule-type: flow # 规则,这里是流控规则。还可以是：degrade(熔断)、authority(授权)、param-flow(热点参数)
   ```

4. 启动Sentinel

   





