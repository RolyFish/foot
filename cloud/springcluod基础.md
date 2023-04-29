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

