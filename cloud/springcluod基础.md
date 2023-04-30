# 微服务技术栈

## 什么是微服务

微服务是分布式架构的一种解决方案,它不等于SpringCloud,SpringCloud只解决了微服务服务拆分时的服务治理问题,而其他更复杂的问题并没有解决。

微服务包含以下模块：

- 服务拆分

  > 使得服务满足单一职责,各个服务配合工作,形成一个服务集群

- 注册中心

  > 那么微服务中的每个服务之间如何调用？
  >
  > 不能够写死,所以就需要一个注册中心,记录整个微服务中的服务的ip、端口以及责任。
  >
  > 当某个服务需要调用另一个服务时,不需要直接去找对应服务,直接去找注册中心拿取目标服务信息。

- 配置中心

  > 微服务中每个服务都有配置,如果需要逐个修改服务配置将会非常麻烦。所以说需要一个配置中心,统一管理配置,如果存在配置更新,直接修改配置中心的配置,配置中心会将修改的配置热更新到对应服务。

- 微服务网关

  > 一个请求过来,需要到达微服务中的哪个服务？所以说就需要一个微服务网关
  >
  > 校验权限,限制不是任何人都可以访问
  >
  > 路由请求
  >
  > 负载均衡

- 分布式缓存

  > 减轻数据库压力,提高响应速度

- 分布式搜索

  > 对于一些海量数据的搜索、统计和分析,传统的数据库满足不了性能需求,而缓存也不适合做海量数据的搜索。

- 数据库

  > 处理一些数据安全较高的、以及需要事务支持的需求。

- 消息队列

  > 在微服务架构中一个请求可能会跨越多个服务,而如果采取的是同步式方案,那么整个请求的响应时长将会非常长,并发也不会高。所以就需要一个异步通信的消息队列组件,通过通知的方式异步调用其他服务,降低请求的响应时长。

- 分布式日志服务

- 系统监控和链路追踪

  > 在服务运行期间难免出现问题,如何方便快速的定位问题非常重要,所以需要引入分布式日志服务和系统监控(cpu、内存占用)链路追踪。

- 持续集成

  > 这么庞大的微服务架构,如果还是人为去发布维护,将会很麻烦,所以就需要一套自动化部署的方案。
  >
  > Jenkins：可以为微服务中的服务做自动化编译
  >
  > docker：打包项目、形成镜像
  >
  > K8S： 自动部署

![image-20230429180433765](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429180433765.png)



## 认识微服务

### 单体架构

> 将所有功能集中在一个项目中,打成一个包部署,架构简单,适合规模较小的项目

优点：

- 架构简单
- 部署成本低

缺点：

- 部署时间较高,某个功能的修改,整个服务都需要重新部署

- 耦合度高

  > 单体项目不符合单一职责原则,随着时间推移,各个功能相互耦合难以维护

### 分布式架构

> 根据业务功能对系统进行拆分,每个业务模块独立开发,成为一个个服务

优点：

- 耦合度低,利于维护
- 有利于服务升级和扩展



#### 服务治理

> 分布式架构,对服务进行拆分,需要考虑的问题也会变多

- 服务拆分粒度如何？
- 服务集群地址如何维护？
- 服务之间如何实现远程调用？
- 服务健康状态如何感知？

> 针对以上分布式架构需要考虑的问题,提出来很多解决方案,dubbo、springcloud、微服务等。



### 微服务

> 微服务是一种经过良好架构设计的分布式架构方案，微服务架构特征：

- 单一职责：微服务拆分粒度更小，每一个服务都对应唯一的业务能力，做到单一职责，避免重复业务开发

- 面向服务：微服务对外暴露业务接口

  > 暴露接口对外提供服务,隐藏内部实现细节

- 自治：团队独立、技术独立、数据独立、部署独立

  > 服务拆分后各自功能也拆分,只需要较小的团队就可以。并且各个服务各自决定技术、数据、部署

- 隔离性强：服务调用做好隔离、容错、降级，避免出现级联问题

  > 服务隔离,互不影响,做好容错降级,避免级联出错



### 小结

单体架构特点：

- 简单方便，高度耦合，扩展性差，适合小型项目。例如：学生管理系统

分布式架构特点：

- 松耦合，扩展性好，但架构复杂，难度大。适合大型互联网项目，例如：京东、淘宝

微服务：一种良好的分布式架构方案优点

- 拆分粒度更小、服务更独立、耦合度更低
- 缺点：架构非常复杂，运维、监控、部署难度提高



## 微服务架构


### 微服务技术对比

![image-20230429184640504](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429184640504.png)



![image-20230429184903728](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429184903728.png)

### SpringCloud SpringBoot兼容

![image-20230429185135026](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429185135026.png)

### 服务拆分&远程调用

#### 服务拆分

- 单一职责: 不要重复开发相同业务
- 数据独立：不要访问其他服务数据库
- 面向服务: 暴露接口供其他服务调用

#### 远程调用

> 前端通过ajax、aiox发送http请求到后端。
>
> 如果使用http协议,那么远程调用就是后端消费者发送请求到后端生产者。而Spring提供了一个类：RestTemplate它就可以发送http请求,可以区分请求方式`get  &  post`,并且可以将Json字符串自动解析成类对象。

```java
User user = restTemplate.getForObject("http://localhost:8081/user/" +order.getUserId(), User.class);
```

#### 服务生产者 & 消费者

服务生产者和消费者是一个相对概念,一个服务既可以是生产者也可以是消费者。

### Eureka注册中心

> 上面的例子我们使用RestTemplate使用硬编码的方式将user-server的IP地址和端口记录下来,通过Http协议发送一个远程调用,这么做存在问题：
>
> - user-server之后可能会集群部署,怎么请求
> - user-server后期ip端口改动,order-server也要跟着改动,耦合度高
>
> 所以我们需要一个注册中心帮我们维护服务生产者的信息。

#### eureka的作用

![image-20230429194154868](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429194154868.png)

消费者该如何获取服务提供者具体信息？

- 服务提供者启动时向eureka注册自己的信息
- eureka保存这些信息
- 消费者根据服务名称向eureka拉取提供者信息

如果有多个服务提供者，消费者该如何选择？

- 服务消费者利用负载均衡算法，从服务列表中挑选一个消费者

如何感知服务提供者健康状态？

- 服务提供者会每隔30秒向EurekaServer发送心跳请求，报告健康状态
- eureka会更新记录服务列表信息，心跳不正常会被剔除
- 消费者就可以拉取到最新的信息

在Eureka架构中，微服务角色有两类：

- EurekaServer：服务端，注册中心
  - 记录服务信息
  - 心跳监控
- EurekaClient：客户端
  - Provider：服务提供者，例如案例中的 user-service
    - 注册自己的信息到EurekaServer
    - 每隔30秒向EurekaServer发送心跳
  - consumer：服务消费者，例如案例中的 order-service
    - 根据服务名称从EurekaServer拉取服务列表
    - 基于服务列表做负载均衡，选中一个微服务后发起远程调用

#### 搭建Eureka server

1. 创建module

2. 引入eureka依赖

   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
   	<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
   </dependency>
   ```

3. 启动类开启eureka开关

   ```java
   @EnableEurekaServer
   @SpringBootApplication
   public class EurekaServerApplication {
       public static void main(String[] args) {
           SpringApplication.run(EurekaServerApplication.class, args);
       }
   }
   ```

4. 配置eureka

   ```yml
   server:
     port: 10086 # eureka服务端口
   spring:
     application:
       name: eureka-server # eureka服务名称
   eureka:
     client:
       service-url:
         defaultZone: http://127.0.0.1:10086/eureka # eureka注册自己到eureka
       fetch-registry: false
       register-with-eureka: false #eureka server和client是同一台机器, 关闭集群
   ```

5. 启动

   > 访问`http://127.0.0.1:10086/`

   ![image-20230429202738632](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429202738632.png)

#### 注册服务

> 注册user-service、order-service注册进eureka-server中



1. 引入eureka-client依赖

   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
   </dependency>
   ```

2. 配置eureka-server地址

   ```yml
   eureka:
     client:
       service-url:
         defaultZone: http://127.0.0.1:10086/eureka
   ```

3. 注册多个user-service做负载均衡

   ![image-20230429204129633](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429204129633.png)

4. 启动验证

   ![image-20230429204205131](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429204205131.png)

#### 服务发现&服务拉取

> 之前的RestTemplate需要硬编码ip和端口,现在我们将服务注册进eureka可以通过服务名称来拉取服务进行调用。

1. order-service引入依赖

   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
   </dependency>
   ```

2. 配置负载均衡

   > 为RestTemplate添加`@LoadBalanced`注解

   ```java
   @Bean
   @LoadBalanced
   public RestTemplate restTemplate(){
       return new RestTemplate();
   }
   ```

3. 通过服务名称调用user-service服务

   > 这个`user-service`就是user-service配置的服务名称。
   >
   > order-service底层调用Ribbon拉取名为user-service的服务,然后通过轮询的方式请求已经注册的服务

   ```java
   public Order queryOrderById(Long orderId) {
       // 1.查询订单
       Order order = orderMapper.findById(orderId);
       // final User user = restTemplate.getForObject("http://localhost:8081/user/" + order.getUserId(), User.class);
       final User user = restTemplate.getForObject("http://user-service/user/" + order.getUserId(), User.class);
       order.setUser(user);
       // 4.返回
       return order;
   }
   ```

#### Ribbon负载均衡

> restTemplate通过服务名称访问服务,此方式给的URL`http://user-service/user/`实际是不可访问的,那么底层是如何转换的呢？
>
> Ribbon帮我们做了拦截,拉取服务列表,并做了负载均衡。

- 客户端发出的Http请求(RestTemplate发出的请求)会被拦截`LoadBalancerInterceptor`
- 然后回根据服务名称去eureka拉取可用服务列表,获取ip和端口
- 最后通过负载均衡策略,发出http请求

##### 负载均衡策略IRule

1. ribbon有哪些负载均衡策略

   > 默认是ZoneAvoidanceRule,按zone轮询,如果服务注册在同一个机房,就是轮询

   ![image-20230429225115775](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429225115775.png)

2. 配置ribbon负载均衡策略

   1. 自定义IRule覆盖默认配置

      > 此方式配置的负载均衡策略,对所有服务生效

      ```java
      /**
       * 此方式配置的负载均衡策略,对所有服务生效
       * @return
       */
      @Bean
      public IRule RandomRule(){
          return new RandomRule();
      }
      ```

   2. 配置文件方式,对指定服务配置负载均衡策略

      ```yml
      user-service: # 给某个微服务配置负载均衡规则，这里是user-service服务
        ribbon:
          NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule # 负载均衡规则 
      ```

3. 饥饿加载

   > Ribbon默认是采用懒加载，即第一次访问时才会去创建LoadBalanceClient，首次请求时间会很长。
   >
   > 而饥饿加载则会在项目启动时创建，降低第一次访问的耗时，通过下面配置开启饥饿加载：

   ```yml
   ribbon:
     eager-load:
       enabled: true
       clients:
         - user-service # 服务集合,哪些服务需要饥饿加载
   ```

   ![image-20230429230610510](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429230610510.png)

#### 小结

- Eureka作用

  > - 注册服务
  >   - 服务提供者提供服,注册到eureka中
  >   - 服务消费者拉取服务
  > - 心跳检测,剔除宕机服务,避免客户端拉取不可用服务

- Ribbon原理

  > 服务消费者通过服务名称发起一个HTTP请求,Ribbon会拦截这个请求,根据得到的服务名称去eureka中拉取可用服务列表,映射成ip+端口,再发送HTTP请求。

- Ribbon负载均衡策略

  - 默认轮询
  - 可配置
    - 通过注入Bean覆盖自动装配默认配置
    - 配置文件,为指定服务配置指定负载均衡策略

- Ribbon的LoadBalance默认懒加载

  - 可配置成饥饿加载,缩短首次请求响应时长	

### nacos注册中心

> [Nacos](https://nacos.io/)是阿里巴巴的产品，现在是[SpringCloud](https://spring.io/projects/spring-cloud)中的一个组件。相比[Eureka](https://github.com/Netflix/eureka)功能更加丰富，在国内受欢迎程度较高。
>
> 国内公司一般都推崇阿里巴巴的技术，比如注册中心，SpringCloudAlibaba也推出了一个名为Nacos的注册中心。

#### 安装

1. [下载地址](https://github.com/alibaba/nacos/releases/tag/2.2.2)

2. 解压缩、配置端口

   ```properties
   ### Default web server port:
   server.port=8848
   
   # 关闭密码、登录就不需要密码了
   # db.user.0=nacos
   # db.password.0=nacos
   ```

3. 启动

   ```shell
   sh startup.sh -m standalone
   ```

   ![image-20230429232735630](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429232735630.png)

4. 登录nacos后台管理界面

   ![image-20230429232859110](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429232859110.png)

#### 服务注册

1. 引入`spring-cloud-alibaba`版本管理依赖

   ```xml
   <!--  spring-cloud-alibaba-dependencies -->
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-alibaba-dependencies</artifactId>
       <version>2.2.6.RELEASE</version>
       <type>pom</type>
       <scope>import</scope>
   </dependency>
   ```

2. 在`user-service和order-service`注释掉eureka依赖并引入`nacos-discovery`服务发现依赖

   ```xml
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
   </dependency>
   ```

3. user-service、order-service配置nacos地址

   ```yml
   sprting: 
     cloud:
       nacos:
         server-addr: 127.0.0.1:8848 # nacos地址
   ```

4. nacos后台管理页面查看

   > nacos提供的服务信息较全面

   ![image-20230429235311811](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230429235311811.png)

5. 测试服务拉取

#### 服务分级存储模型

> nacos的集群默认是`private String clusterName = "DEFAULT";`。也就是只有一个集群。

![image-20230430000015341](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430000015341.png)

##### user-service配置集群分组

> nacos允许对集群分组,将附近的服务划分到一个集群里,处理附近请求。

`127.0.0.1:8080、127.0.0.1:8081`划分到SH集群

`127.0.0.1L:8082`划分到HZ集群。

启动服务可以配置启动参数

```yml
-Dserver.port=8083 -Dspring.cloud.nacos.discovery.cluster-name=SH
```

```yml
spring:
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848 # nacos地址
      discovery:
        cluster-name: SH
```

这样就有两个集群了：

![image-20230430000507712](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430000507712.png)

##### order-service配置集群分组

1. 配置集群分组

   > `order-service`分到HZ组

   ```yml
   spring:
     cloud:
       nacos:
         server-addr: 127.0.0.1:8848 # nacos地址
         discovery:
           cluster-name: HZ
   ```

2. 配置负载均衡策略

   > 之前是轮询,现在使用`NacosRule`

   ```yml
   user-service:
     ribbon:
       NFLoadBalancerRuleClassName: com.alibaba.cloud.nacos.ribbon.NacosRule # 负载均衡规则 
   ```

3. 测试

   > 结果就是`order-service`服务消费者调用`user-service`服务提供者的请求都请求到了HZ这个分组。

4. 停用HZ分组

   > 模拟HZ分组下的服务不可用。
   >
   > 请求就会到达SH分组下,并且服务消费者`order-service`会输出警告信息：==一次跨集群的请求==

   ```xml
   04-30 00:21:11:281  WARN 53965 --- [nio-8080-exec-5] c.alibaba.cloud.nacos.ribbon.NacosRule   : A cross-cluster call occurs，name = user-service, clusterName = HZ, instance = [Instance{instanceId='192.168.0.105#8002#SH#DEFAULT_GROUP@@user-service', ip='192.168.0.105', port=8002, weight=1.0, healthy=true, enabled=true, ephemeral=true, clusterName='SH', serviceName='DEFAULT_GROUP@@user-service', metadata={preserved.register.source=SPRING_CLOUD}}, Instance{instanceId='192.168.0.105#8081#SH#DEFAULT_GROUP@@user-service', ip='192.168.0.105', port=8081, weight=1.0, healthy=true, enabled=true, ephemeral=true, clusterName='SH', serviceName='DEFAULT_GROUP@@user-service', metadata={preserved.register.source=SPRING_CLOUD}}]
   ```



> NacosRule的负载均衡的策略是,首先根据分组选择优先分组,随后在分组内随机。

##### 权重配置

> nacos可以配置服务权重,权重大的被访问的频率高,反之低。我们可以对机器性能好的服务设置较大权重。

在nacos控制台，找到user-service的实例列表，点击编辑，即可修改权重：

![image-20230430002824290](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430002824290.png)

> 如果权重修改为0,则表示永远不会被访问
>
> 此方式可用于服务升级：
>
> 可将此服务权重设置为0,对其做升级,升级完成后,设置较小权重先做少量测试,慢慢提高权重。

#### 环境隔离-namspace

Nacos提供了namespace来实现环境隔离功能。

- nacos中可以有多个namespace
- namespace下可以有group、service等
- 不同namespace之间相互隔离，例如不同namespace的服务互相不可见

##### 创建namespace

![image-20230430003758419](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430003758419.png)

![image-20230430003907543](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430003907543.png)

##### 服务指定命名空间

> 命名空间默认public。
>
> 通过namespace的id在yml中配置`ordr-service`的命名空间：

```yml
spring:
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848 # nacos地址
      discovery:
        cluster-name: HZ
        namespace: 9f698efc-3d3b-422e-907f-781cdcc60e71
```

重启后`order-service`就在dev命名空间下了,并且不可以调用`public`下的服务：

![image-20230430004259302](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430004259302.png)

![image-20230430004312438](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430004312438.png)

#### nacos细节

##### 时实例 & 非临时实例

Nacos的服务实例分为两种类型：

- 临时实例：如果实例宕机超过一定时间，会从服务列表剔除，默认的类型。

- 非临时实例：如果实例宕机，不会从服务列表剔除，也可以叫永久实例。

配置一个服务实例为永久实例：

```yml
spring: 
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848 # nacos地址
      discovery:
        cluster-name: HZ
        namespace: 9f698efc-3d3b-422e-907f-781cdcc60e71
        ephemeral: false # 配置实例为非临时实例
```

##### nacos服务健康检测和服务拉取机制

Nacos和Eureka整体结构类似，服务注册、服务拉取、心跳等待，但是也存在一些差异：

![image-20210714001728017](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20210714001728017.png)

#### nacos&eureka对比

> nacos功能将对于eureka较全。
>
> eureka服务发现采用的是周期性的拉取机制,那么在周期时间内服务提供者服务不可用的话对服务消费者是不可见的,而nacos则会推送消息变更,通知服务消费者服务不可用。
>
> eureka健康检测采用的是服务提供者主动发送心跳信息,而nacos支持临时&非临时节点,临时节点采用心跳监测,非临时节点采用主动询问。

- Nacos与eureka的共同点
  - 都支持服务注册和服务拉取
  - 都支持服务提供者心跳方式做健康检测

- Nacos与Eureka的区别
  - Nacos支持服务端主动检测提供者状态：临时实例采用心跳模式，非临时实例采用主动检测模式
  - 临时实例心跳不正常会被剔除，非临时实例则不会被剔除
  - Nacos支持服务列表变更的消息推送模式，服务列表更新更及时
  - Nacos集群默认采用AP方式，当集群中存在非临时实例时，采用CP模式；Eureka采用AP方式



## nacos配置管理

> nacos不仅可以做注册中心,也可以作为一个配置管理来使用。



### 统一配置管理

> 当项目以集群部署的话,涉及多个服务实例,如果因为某个配置的修改而需要去修改整个集群中所有服务实例的配置更改,那将非常麻烦,并且配置修改后还需要重启服务。
>
> 所以我们需要一个配置中心,来帮我们统一管理配置,并且实现热更新。注意这里管理的配置一般是需要热更新的配置。

nacos作为配置中心的工作流程：

- 配置变更后,nacos会通知服务拉取变更配置,完成配置热更新

![image-20230430012634153](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430012634153.png)

#### 在nacos中添加配置文件

> 这里配置文件放在指定命名空间dev下,所以后续读取也要配置

![image-20230430020535835](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430020535835.png)

#### 服务拉取nacos配置

> 读取nacos的配置需要在读取本地`application.yml`配置之前,所以nacos的配置需要比默认路径下的`application.yml`要优先。
>
> 所以`nacos`的配置可以配置在`bootstrap.yml`中。

1. 引入nacos配置中心依赖

   ```xml
   <!-- nacos-config -->
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
   </dependency>
   ```

2. 创建`bootstrap.yml`,并将有关nacos的配置移到此配置文件中

   > 根据服务名称`user-service`激活配置环境`dev`以及后缀名可以确定nacos中配置文件的`Data ID`。再根据nacos地址就可以确定配置文件

   ```yml
   spring:
     application:
       name: user-service
     cloud:
       nacos:
         server-addr: 127.0.0.1:8848 # nacos地址
         #      discovery:
         #        cluster-name: HZ
         config:
           file-extension: yaml # 默认是properties
           namespace: 9f698efc-3d3b-422e-907f-781cdcc60e71 # 配置命名空间
     profiles:
       active: dev
   ```

3. 测试

   > 通过`@Value`检查nacos中的配置及是否被读取

   ```java
   @Value("${pattern.dateformat}")
   private String dateformat;
   @RequestMapping(method = RequestMethod.POST, path = "now")
   public String now() {
       return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
   }
   ```



#### 配置热更新

> Nacos配置修改,服务不重启并且感知配置变化。

##### 方式一

> 在`@Value`所在组件上添加`@refreshScope`注解

```java
@RefreshScope
public class UserController {
	@Value("${pattern.dateformat}")
    private String dateformat;
}
```

##### 方式二

> 不通过`@Value`注入配置,而是通过`@ConfigrationProperties`注解注入配置

```java
 */
@Data
@Component
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties {
    String dateformat;
}
```

```java
@Autowired
private PatternProperties patternProperties;

@RequestMapping(method = RequestMethod.POST, path = "now")
public String now() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(patternProperties.getDateformat(), Locale.CHINA));
}
```



#### 多环境配置共享

> 微服务启动时,会去nacos读取多个配置文件,例如：

- `[spring.application.name]-[spring.profiles.active].yaml`，例如：user-service-dev.yaml
- `[spring.application.name].yaml`，例如：user-service.yaml
- `[spring.application.name]`，例如：user-service

而`[spring.application.name].yaml`和`[spring.application.name]`不包含环境，因此可以被多个环境共享。

从服务启动日志也可以得到体现:

![image-20230430023642569](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430023642569.png)

##### 添加共享配置

![image-20230430024652302](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430024652302.png)

##### 读取并验证

> `user-service`和`user-service.yaml`中的配置文件可以共享

```java
@Data
@Component
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties {
    String dateformat;
    String envSharedValue;
    String envSharedValueWithOutExtension;
    String sharedValue;
}

@RequestMapping(method = RequestMethod.GET, path = "prop")
public PatternPropGETies patternProperties() {
    return patternProperties;
}
```

![image-20230430025119150](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430025119150.png)

##### 配置优先级

- 远程优先

- 最佳匹配优先

  > 日志输出的配置文件顺序就是优先级顺序。	
  >
  > ![image-20230430023642569](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430023642569.png)

​	

## nacos集群搭建

[nacos官方集群部署手册](https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html)

### 集群结构

![image-20230430130256760](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430130256760.png)

> SLB负载均衡器使用nginx
>
> 三个nacos地址：
>
> | 节点   | ip            | port |
> | ------ | ------------- | ---- |
> | nacos1 | 192.168.0.105 | 8840 |
> | nacos2 | 192.168.0.105 | 8843 |
> | nacos3 | 192.168.0.105 | 8846 |



### 搭建集群



#### 配置数据源

> 使用内置数据源,无需任何配置。
>
> 一般会使用外置数据源,配置Mysql。[官方githupsql脚本](https://github.com/alibaba/nacos/blob/master/distribution/conf/mysql-schema.sql)



#### 配置nacos

`nacos conf`目录下拷贝配置模板：

```shell
cp cluster.conf.example cluster.conf
```

添加集群配置：

> 我mac启动,连续端口就会报错,所以设置+3

```properties
192.168.0.105:8840
192.168.0.105:8843
192.168.0.105:8846
```

修改`conf 下 application.properties`配置：[官方模板](https://github.com/alibaba/nacos/blob/master/distribution/conf/application.properties)

```properties
spring.datasource.platform=mysql

db.num=1

db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=123456
```



#### 启动

1. 复制三份`nacos`。

   ```shell
   ➜  nacos cp -r nacos nacos1
   ➜  nacos cp -r nacos nacos2
   ➜  nacos cp -r nacos nacos3
   ```

2. 修改端口为8840、8843、8846

3. 启动

   ```shell
   nacos1/bin/startup.sh
   nacos2/bin/startup.sh
   nacos3/bin/startup.sh
   ```

#### 配置反向代理

> 使用docker安装nginx。

1. 挂载目录

   ```shell
   ➜  nginx mkdir nacos-nginx
   ➜  nginx pwd
   /Users/rolyfish/home/nginx
   ```

2. 拉取镜像

   ![image-20230430133443446](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430133443446.png)

3. 启动

   ```shell
   docker run --name nacos-nginx -p 80:80 \
       -v /Users/rolyfish/home/nginx/nacos-nginx/logs/:/var/log/nginx/ \
       -v /Users/rolyfish/home/nginx/nacos-nginx/conf.d/:/etc/nginx/conf.d/ \
       --privileged=true -d nginx:latest
   ```

   > 拷贝配置文件。ps：只会挂载文件夹不会挂载文件。。。,或者在conf.d下创建XXX.conf也可以。

   ```shell
   docker cp nacos-nginx:/etc/nginx/nginx.conf nginx.conf
   ```

   > 配置负载均衡,在http块下添加如下配置

   ```properties
   upstream nacos-cluster {
       server 192.168.0.105:8840;
   	server 192.168.0.105:8843;
   	server 192.168.0.105:8846;
   }
   
   server {
       listen       80;
       server_name  localhost;
   
       location /nacos {
           proxy_pass http://nacos-cluster;
       }
   }
   ```

4. 重启nacos-docker

   ```shell
   docker run --name nacos-nginx -p 80:80 \
       -v /Users/rolyfish/home/nginx/nacos-nginx/nginx.conf:/etc/nginx/nginx.conf \
       -v /Users/rolyfish/home/nginx/nacos-nginx/logs/:/var/log/nginx/ \
       -v /Users/rolyfish/home/nginx/nacos-nginx/conf.d/:/etc/nginx/conf.d/ \
       --privileged=true -d nginx:latest
   ```

5. 访问测试,`http://localhost/nacos`

   > 如果`127.0.0.1`502错误,则ip修改成启动日志的ip`http://192.168.0.105:8840/nacos/index.html`。使用docker就访问不了

   ![image-20230430142121570](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430142121570.png)

6. 在nacos总添加配置会持久化到数据库



#### 客户端配置

> 由于nacos是集群部署,不可以指定Ip端口注册,而是通过nginx负载均衡到nacos集群。

```yaml
spring:
  application:
    name: user-service
  cloud:
    nacos:
      #      server-addr: 127.0.0.1:8848 # nacos地址
      server-addr: 192.168.0.105:80 # nacos地址
      discovery:
        cluster-name: HZ
      config:
        file-extension: yaml # 默认是properties
  #        namespace: 9f698efc-3d3b-422e-907f-781cdcc60e71 # 配置命名空间
  profiles:
    active: dev
```





## Feign远程调用

使用RestTemplate实现远程调用的缺点：

- 可读性差,不统一
- 代码嵌套url,不好看



### 简介

> Feign是一个声明式的http客户端，[官方地址](https://github.com/OpenFeign/feign)
>
> 其作用就是帮助我们优雅的实现http请求的发送,提供一种简单的方式实现JavaHttp客户端,解决上面提到的问题。

Feign工作原理就是将注解处理成模板化的请求,请求参数可以通过方法参数传递,并且基于Feign可以很容易实现单元测试。Feign内部集成了Ribbon,自动实现负载均衡。



### 使用Feign实现远程调用

1. 引入OpenFeign依赖

   ```xml
   <!--  openfeign -->
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```

2. 开启Feign客户端

   > 启动类添加`@EnableFeignClient`

   ```java
   @MapperScan("cn.itcast.order.mapper")
   @SpringBootApplication
   @EnableFeignClients
   public class OrderApplication {
   }
   ```

3. 编写FeignClient客户端

   > Feign就可以帮我们发送Http请求

   ```java
   @FeignClient(name = "user-service")
   public interface UserServiceFeignClient {
       /**
        * value --请求路径,必须是user-service实际存在可达接口
        */
       @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
       User getUserById(@PathVariable("id") Long userId);
   }
   ```

4. 测试

   ```java
   @Autowired
   UserServiceFeignClient userServiceFeignClient;
   public Order queryOrderById(Long orderId) {
       // 1.查询订单
       Order order = orderMapper.findById(orderId);
       final User user = userServiceFeignClient.getUserById(order.getUserId());
       order.setUser(user);
       // 4.返回
       return order;
   }
   ```

   

   

   

### Feign自定义配置

Feign可以支持很多的自定义配置，如下表所示：

| 类型                   | 作用             | 说明                                                   |
| ---------------------- | ---------------- | ------------------------------------------------------ |
| **feign.Logger.Level** | 修改日志级别     | 包含四种不同的级别：NONE、BASIC、HEADERS、FULL         |
| feign.codec.Decoder    | 响应结果的解析器 | http远程调用的结果做解析，例如解析json字符串为java对象 |
| feign.codec.Encoder    | 请求参数编码     | 将请求参数编码，便于通过http请求发送                   |
| feign. Contract        | 支持的注解格式   | 默认是SpringMVC的注解                                  |
| feign. Retryer         | 失败重试机制     | 请求失败的重试机制，默认是没有，不过会使用Ribbon的重试 |

一般情况下，默认值就能满足我们使用，如果要自定义时，只需要创建自定义的@Bean覆盖默认Bean即可。

#### Feign自定义配置日志

> feign.Logger.Level有四个级别

而日志的级别分为四种：

- NONE：不记录任何日志信息，这是默认值。
- BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
- HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息
- FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据。

##### 配置文件

- 全局有效,对所有服务有效

  ```yml
  feign:
    client:
      config:
        default: # default默认对全局有效
          logger-level: full
  ```

- 针对某个服务配置日志级别

  ```yaml
  feign:
    client:
      config:
        user-service: # user-service这个服务有效
          logger-level: full
  ```

  

##### JavaConfig方式

> 基于Java代码来修改日志级别，先声明一个类，然后声明一个Logger.Level的对象：

```java
public class DefaultFeignConfiguration {
    @Bean
    Logger.Level level() {
        return Logger.Level.FULL;
    }
}
```

- 全局有效,对所有服务有效

  > 在@EnableFeignClients注解中添加全局配置

  ```java
  @EnableFeignClients(defaultConfiguration = DefaultFeignConfiguration.class)
  ```

- 针对某个服务配置日志级别

  > 在对应feignclient上添加配置

  ```java
  @FeignClient(name = "user-service", configuration = DefaultFeignConfiguration.class)
  ```

### Feign优化

Feign底层发起http请求，依赖于其它的框架。其底层客户端实现包括：

- URLConnection：默认实现，不支持连接池
- Apache HttpClient ：支持连接池
- OKHttp：支持连接池

> URLConnection是JDK自带的Http连接客户端,而他是不支持连接池的。Feign除了实现了默认Http连接客户端外,还实现了Apache HttpClient和OKHttp这两个Http连接诶客户端。所以我们自定自定义Feign底层Http连接客户端,来提升响应效率。



#### ApacheHttpClient

1. 引入依赖

   ```xml
   <!--httpClient的依赖 -->
   <dependency>
       <groupId>io.github.openfeign</groupId>
       <artifactId>feign-httpclient</artifactId>
   </dependency>
   ```

2. 配置Feign底层Http连接客户端

   ```yaml
   feign:
     httpclient:
       enabled: true # 开启HttpClient
       max-connections: 200 # 连接池最大连接数
       max-connections-per-route: 50 # 单个请求最大连接数
   ```

   ![image-20230430180006818](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430180006818.png)

#### 小结

总结，Feign的优化：

1. 日志级别尽量用basic

2. 使用HttpClient或OKHttp代替URLConnection

   - 引入feign-httpClient依赖

   -  配置文件开启httpClient功能，设置连接池参数



### Feign最佳实践

> Feign的定义和服务提供者的接口定义是一样的,也就是说是固定的,那么如果存在多个服务消费者,我们就需要写多个相同的FeignClient。
>
> 所以得避免这种情况。

![image-20230430181830170](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430181830170.png)



#### 继承方式

一样的代码可以通过继承来共享：

1）定义一个API接口，利用定义方法，并基于SpringMVC注解做声明。

2）Feign客户端和Controller都继承该接口

优点：

- 简单
- 实现了代码共享

缺点：

- 服务提供方、服务消费方紧耦合

- 参数列表中的注解映射并不会继承，因此Controller中必须再次声明方法、参数列表、注解



#### 抽取方法

> 将Feign的Client抽取为独立模块，并且把接口有关的POJO、默认的Feign配置都放到这个模块中，提供给所有消费者使用。
>
> 例如，将UserClient、User、Feign的默认配置都抽取到一个feign-api包中，所有微服务引用该依赖包，即可直接使用。



##### 实现

1. 新建一个模块

   ![image-20230430183823679](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430183823679.png)

2. feign-api中引入依赖

   ```xml
   <!--  openfeign -->
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```

3. 在服务消费者引入feign-api

   ```xml
   <!-- feign-api -->
   <dependency>
       <groupId>cn.itcast</groupId>
       <artifactId>feign-api</artifactId>
       <version>1.0</version>
   </dependency>
   ```

4. 扫描注册feign-api下的FeignClient

   > `@EnableFeignClient`默认只扫描同级别包下的FeignClient。所以得手动配置一下

   ```java
   // 扫描
   @EnableFeignClients(basePackages = {"cn.itcast.feignapi.feign"})
   //或者 指定feignclient
   @EnableFeignClients(clients = {UserServiceFeignClient.class})
   ```

   

   



## 网关Geteway

### 网关是什么

> Gateway网关是我们所有微服务的统一入口。

在SpringCloud中网关的实现包括两种：

- gateway
- zuul

Zuul是基于Servlet的实现，属于阻塞式编程。而SpringCloudGateway则是基于Spring5中提供的WebFlux，属于响应式编程的实现，具备更好的性能。

> Spring Cloud Gateway 是 Spring Cloud 的一个全新项目，该项目是基于 Spring 5.0，Spring Boot 2.0 和 Project Reactor 等响应式编程和事件流技术开发的网关，它旨在为微服务架构提供一种简单有效的统一的 API 路由管理方式。



### 网关作用

- 权限控制

  > 网关是所有请求的路口,在这可以做权限控制。

- 请求路由和负载均衡

  > 网关是所有请求的路口,它不处理业务,而是根据配置规则,将请求转发到目标微服务,这个过程就叫做请求路由。
  >
  > 当路由目标有多个则可以做负载均衡。

- 限流削峰

  > 当某一时刻请求流量达到配置峰值时,则可以等待或拒绝,保证微服务正常稳定运行。



![image-20210714210131152](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20210714210131152.png)

### 搭建网关



#### 创建getway服务

![image-20230430210340498](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430210340498.png)

#### 引入依赖

> getway是一个单独的服务,也需要注册进Nacos,拉取可用服务,做路由配置。

```xml
<!-- 服务发现依赖 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>

<!-- getway -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```



#### 配置路由规则

```yaml
server:
  port: 10010 # 网关端口
spring:
  application:
    name: gateway # 服务名称
  cloud:
    nacos:
      server-addr: localhost:80 # nacos地址
    gateway:
      routes: # 网关路由配置
        - id: user-service # 路由id，自定义，只要唯一即可
          # uri: http://127.0.0.1:8081 # 路由的目标地址 http就是固定地址
          uri: lb://user-service # 路由的目标地址 lb就是负载均衡，后面跟服务名称
          predicates: # 路由断言，也就是判断请求是否符合路由规则的条件
            - Path=/user/** # 这个是按照路径匹配，只要以/user/开头就符合要求        
        
        - id: order-service
          uri: lb://order-service
          predicates: 
            - Path=/order/**
```



#### 测试

> getaway注册成功

![image-20230430210737351](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430210737351.png)

> 测试路由

![image-20230430210909463](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430210909463.png)



#### 路由过程

![image-20210714211742956](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20210714211742956.png)

#### 小结

网关搭建步骤：

- 创建getaway服务

  > 路由也是nacos中的一员,需要注册进去,并拉取可用服务做路由则。

- 配置路由规则

  - 路由id(id)：随意唯一
  - 目标地址(uri)：可以是单个服务http，也可以是集群lb(会做负载均衡)
  - 路由断言(pridicates)： 判断路由规则(匹配)
  - 路由过滤(filters)：对请求或响应做处理

### 断言工厂(pridicates)

[spring官网predicates](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-xforwarded-remote-addr-route-predicate-factory)

> 在配置文件中配置的predicates是一个且判断,配置的规则会生成对应的断言工厂,所有请求多会进过断言工厂判断,符合条件则路由通过,否则404。
>
> path最常用。

![image-20230430212436590](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430212436590.png)

| **名称**   | **说明**                       | **示例**                                                     |
| ---------- | ------------------------------ | ------------------------------------------------------------ |
| After      | 是某个时间点后的请求           | -  After=2037-01-20T17:42:47.789-07:00[America/Denver]       |
| Before     | 是某个时间点之前的请求         | -  Before=2031-04-13T15:14:47.433+08:00[Asia/Shanghai]       |
| Between    | 是某两个时间点之前的请求       | -  Between=2037-01-20T17:42:47.789-07:00[America/Denver],  2037-01-21T17:42:47.789-07:00[America/Denver] |
| Cookie     | 请求必须包含某些cookie         | - Cookie=chocolate, ch.p                                     |
| Header     | 请求必须包含某些header         | - Header=X-Request-Id, \d+                                   |
| Host       | 请求必须是访问某个host（域名） | -  Host=**.somehost.org,**.anotherhost.org                   |
| Method     | 请求方式必须是指定方式         | - Method=GET,POST                                            |
| Path       | 请求路径必须符合指定规则       | - Path=/red/{segment},/blue/**                               |
| Query      | 请求参数必须包含指定参数       | - Query=name, Jack或者-  Query=name                          |
| RemoteAddr | 请求者的ip必须是指定范围       | - RemoteAddr=192.168.1.1/24                                  |
| Weight     | 权重处理                       |                                                              |



### 过滤器工厂

[spring官网过滤器工厂](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gatewayfilter-factories)

> GatewayFilter是网关中提供的一种过滤器，可以对进入网关的请求和微服务返回的响应做处理：

![image-20210714212312871](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20210714212312871.png)

种类,例如：

| **名称**             | **说明**                     |
| -------------------- | ---------------------------- |
| AddRequestHeader     | 给当前请求添加一个请求头     |
| RemoveRequestHeader  | 移除请求中的一个请求头       |
| AddResponseHeader    | 给响应结果中添加一个响应头   |
| RemoveResponseHeader | 从响应结果中移除有一个响应头 |
| RequestRateLimiter   | 限制请求的流量               |



#### 添加请求头过滤器

> 为请求添加请求头。
>
> authorization, admin逗号是等于的意思。

[官网例子](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-addrequestheader-gatewayfilter-factory)

> 为user-service服务添加请求头过滤器

```yaml
spring:
  cloud:
    gateway:
      routes: # 网关路由配置
        - id: user-service # 路由id，自定义，只要唯一即可
          # uri: http://127.0.0.1:8081 # 路由的目标地址 http就是固定地址
          uri: lb://user-service # 路由的目标地址 lb就是负载均衡，后面跟服务名称
          predicates: # 路由断言，也就是判断请求是否符合路由规则的条件
            - Path=/user/** # 这个是按照路径匹配，只要以/user/开头就符合要求
          filters:
            - AddRequestHeader=authorization, admin
```

获取请求头

```java
@GetMapping("/{id}")
public User queryById(@PathVariable("id") Long id, @RequestHeader(value = "authorization" ,required = false) String authorization) {
    log.info("authorization:" + authorization);
    return userService.queryById(id);
}

@GetMapping("/{id}")
public User queryById(@PathVariable("id") Long id, @RequestHeader MultiValueMap authorization) {
    log.warn("authorization:" + authorization.get("authorization"));
    return userService.queryById(id);
}
```



#### 默认过滤器

> 对所有服务都生效的过滤器。目的是为所有服务添加统一过滤器避免重复配置。

> 为所有服务添加默认过滤器,功能是添加请求参数。[官网例子](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-addrequestparameter-gatewayfilter-factory)

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - AddRequestParameter=authorization, admin
```

获取添加的请求参数

```java
@GetMapping("/{id}")
public User queryById(@PathVariable("id") Long id, @RequestHeader MultiValueMap authorization, @RequestParam(value = "authorization", required = false) String authorizationP) {
    log.warn("authorization:" + authorization.get("authorization"));
    log.warn("authorization param:" + authorizationP);
    return userService.queryById(id);
}
```

![image-20230430220403899](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430220403899.png)



### 自定义全局过滤器

[spring-globalfilter](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#global-filters)

> 自定义全局过滤器,只要实现以下接口就行。
>
> 网关提供的服务器,是固定用法,如果需要定制的过滤器,则需要自己实现。

```java
public interface GlobalFilter {
    Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain);
}
```

> - 自定义全局过滤器,获取请求参数并校验
> - 设置过滤器优先级,越小优先级越高

```java
@Slf4j
// @Order(-1)//优先级，默认最大，越小越优先
@Component// 作为组件给Spring维护
public class AuthorizationFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取request
        final ServerHttpRequest request = exchange.getRequest();
        final MultiValueMap<String, String> queryParams = request.getQueryParams();
        // 获取请求参数
        final String authorization = queryParams.getFirst("authorization");
        if ("admin".equals(authorization)) {
            log.info("权限校验成功,放行,authorization：{}", authorization);
            // 符合，放行
            return chain.filter(exchange);
        }
        log.error("权限校验失败,authorization：{}", authorization);
        // 不符合设置响应码,结束请求
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    @Override
    public int getOrder() {
        return -1;
    }
}
```

测试：

![image-20230430224705483](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20230430224705483.png)



### 过滤器执行顺序

请求进入网关会碰到三类过滤器：

- DefaultFilter
- 当前路由的过滤器(RouteFilter)
- GlobalFilter

DefaultFilter和RouteFilter都叫做GatewayFilter,全局过滤器叫做GlobalFilter。请求路由后,会将GlobalFilter适配成GatewayFilter,再将这三个过滤器合并到一个过滤器链（集合）中，排序后依次执行每个过滤器：

![image-20210714214228409](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/springcloud_base/image-20210714214228409.png)

排序的规则是什么呢？

- 每一个过滤器都必须指定一个int类型的order值，**order值越小，优先级越高，执行顺序越靠前**。
- GlobalFilter通过实现Ordered接口，或者添加@Order注解来指定order值，由我们自己指定
- 路由过滤器和defaultFilter的order由Spring指定，默认是按照声明顺序从1递增。
- 当过滤器的order值一样时，会按照 defaultFilter > 路由过滤器 > GlobalFilter的顺序执行。

详细内容，可以查看源码：

`org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator#getFilters()`方法是先加载defaultFilters，然后再加载某个route的filters，然后合并。

`org.springframework.cloud.gateway.handler.FilteringWebHandler#handle()`方法会加载全局过滤器，与前面的过滤器合并后根据order排序，组织过滤器链



### 跨域问题

> SpringCloudGetaway是基于WebFlux实现的,它不可以想Sevlet那样通过重定向来解决跨域问题。
>
> WebFlux 是 Spring Framework5.0 中引入的一种新的响应式Web框架。完全异步和非阻塞框架。本身不会加快程序执行速度，但在高并发情况下借助异步IO能够以少量而稳定的线程处理更高的吞吐，规避文件IO/网络IO阻塞带来的线程堆积。



#### 什么是跨域问题

跨域：域名不一致就是跨域，主要包括：

- 域名不同： www.taobao.com 和 www.taobao.org 和 www.jd.com 和 miaosha.jd.com

- 域名相同，端口不同：localhost:8080和localhost8081

[跨域问题](https://www.ruanyifeng.com/blog/2016/04/cors.html)：浏览器禁止请求的发起者与服务端发生跨域ajax请求，请求被浏览器拦截的问题

解决方案：CORS(跨域资源共享)，解决ajax只能同源使用限制，目前浏览器都支持cors方案,所以说只需要服务端支持cors就可以解决跨域问题。



#### 网关解决跨域问题

```yaml
spring:
  cloud:
    gateway:
      # 。。。
      globalcors: # 全局的跨域处理
        add-to-simple-url-handler-mapping: true # 解决options请求被拦截问题,浏览器点击接收cookie的请求
        corsConfigurations:
          '[/**]':
            allowedOrigins: # 允许哪些网站的跨域请求 
              - "http://localhost:8090"
            allowedMethods: # 允许的跨域ajax的请求方式
              - "GET"
              - "POST"
              - "DELETE"
              - "PUT"
              - "OPTIONS"
            allowedHeaders: "*" # 允许在请求中携带的头信息
            allowCredentials: true # 是否允许携带cookie
            maxAge: 360000 # 这次跨域检测的有效期
```

