# MQ



## 初识MQ

### 同步和异步通讯

- 同步通讯需要实时响应,是链式调用的过程
  - 比如通过RestTemplate或FeignClient发送http请求,需要等待响应才可以继续执行
- 异步通讯不需要实时响应,是一种事件驱动模型
  - 比如MQ,消息生产者生产或发布消息,消息消费者订阅或消费消息

>  同步调用的优缺点：

- 优点

  - 时效性较强,可以立即得到结果

- 缺点

  - 耦合度高

    > 服务与服务之间紧耦合,如果某一天服务提供者修改接口,生产者也需要跟着修改

  - 性能和吞吐能力下降

    > 同步通讯,需要等待服务提供者响应,等待响应过程中需要占用cpu、内存等资源

  - 有额外的资源消耗

  - 有级联失败问题

    > 由于服务与服务之间紧耦合,某个服务不可用会导致整个服务不可用


>  异步调用的优缺点：

- 优点

  - 耦合度极低,每个服务都可以灵活插拔,可替换

    > 异步通讯引入中间件mq,服务提供方和服务消费方遵循中间件协议通讯,那么两者无需关心内部实现细节

  - 吞吐量提升：无需等待订阅者处理完成,响应更快速

    > 服务调用方只需发布消息无需真正执行业务

  - 故障隔离：服务没有直接调用,不存在级联失败问题

  - 调用间没有阻塞,不会造成无效的资源占用

  - 流量削峰：不管发布事件的流量波动多大,都由Broker接收,订阅者可以按照自己的速度去处理事件。流量由broker承受,使得整个服务流量平稳

- 缺点：

  - 架构复杂了,业务没有明显的流程线,不好管理
  - 需要依赖于Broker的可靠、安全、性能




### MQ产品

MQ,中文是消息队列（MessageQueue）,字面来看就是存放消息的队列。也就是事件驱动架构中的Broker。

比较常见的MQ实现：

- ActiveMQ
- RabbitMQ
- RocketMQ
- Kafka

几种常见MQ的对比：

|            | **RabbitMQ**         | **ActiveMQ**                  | **RocketMQ** | **Kafka**  |
| ---------- | -------------------- | ----------------------------- | ------------ | ---------- |
| 公司/社区  | Rabbit               | Apache                        | 阿里         | Apache     |
| 开发语言   | Erlang               | Java                          | Java         | Scala&Java |
| 协议支持   | AMQP,XMPP,SMTP,STOMP | OpenWire,STOMP,REST,XMPP,AMQP | 自定义协议   | 自定义协议 |
| 可用性     | 高                   | 一般                          | 高           | 高         |
| 单机吞吐量 | 一般                 | 差                            | 高           | 非常高     |
| 消息延迟   | 微秒级               | 毫秒级                        | 毫秒级       | 毫秒以内   |
| 消息可靠性 | 高                   | 一般                          | 高           | 一般       |

追求可用性：Kafka、 RocketMQ 、RabbitMQ

追求可靠性,消息不丢失：RabbitMQ、RocketMQ

追求吞吐能力：RocketMQ、Kafka

追求消息低延迟：RabbitMQ、Kafka



## 快速入门



### 安装RabbitMQ

[RabbitMQ官网](https://www.rabbitmq.com/)

#### 单机部署

[RabbitMQ官方安装文档](https://www.rabbitmq.com/download.html)

[基于docker安装RabbitMQ](https://registry.hub.docker.com/_/rabbitmq/)

1. 拉取RabbitMQ镜像

   [dockerhup-rabbitmq](https://hub.docker.com/_/rabbitmq)选择合适版本拉取RabbitMQ镜像:

   ```shell
   docker pull rabbitmq:management-alpine
   ```

   ![image-20230502163533878](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502163533878.png)

2. 启动RabbicMQ容器

   > - -e设置环境变量,分别是用户姓名的密码,用于客户端连接
   > - Hostname   单机部署可以不指定
   > - name   容器名称
   > - 15672 rabbitmq后台管理页面端口
   > - 5672 rabbitmq消息通讯接口

   ```shell
   docker run \
    -e RABBITMQ_DEFAULT_USER=rolyfish \
    -e RABBITMQ_DEFAULT_PASS=123456 \
    --name rabbitmq \
    --hostname mq1 \
    -p 15672:15672 \
    -p 5672:5672 \
    -d \
   rabbitmq:management-alpine
   ```

   ![image-20230502164010783](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502164010783.png)

3. 访问RabbitMQ后台管理界面

   ![image-20230502164158241](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502164158241.png)



##### rmq基本结构

![image-20210717162752376](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717162752376.png)



##### 概念

- OverView   概览

- Connection  连接,客户端的连接在这里展示

- Channels  通道  用户发送消息到RMQ需要基于channel

- Exchanges  交换机 。负责消息路由,交换机可以和队列绑定,可实现广播

- Queues   消息队列。存放消息的队列

- Admin   用户管理

  > 多租户&虚拟主机。
  >
  > - virtualHost 虚拟主机避免交换机路径重复。隔离不同租户的exchange、queue、消息的隔离
  > - 多租户指的是,rmq的每个用户应该拥有自己的虚拟主机

### RabbitMQ消息模型

[RabbitMQ官方提供的Demo](https://www.rabbitmq.com/getstarted.html)

![image-20210717163332646](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717163332646.png)

### Demo

#### 简单队列模型

[RabbitMQ官方提供Demo](https://www.rabbitmq.com/tutorials/tutorial-one-java.html)

![image-20210717163434647](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717163434647.png)

官方的HelloWorld是基于最基础的消息队列模型来实现的,只包括三个角色：

- publisher：消息发布者,将消息发送到队列queue
- queue：消息队列,负责接受并缓存消息
- consumer：订阅队列,处理队列中的消息

##### 引入依赖

```xml
<!--AMQP依赖,包含RabbitMQ-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

##### publisher实现

思路：

- 建立连接
- 创建Channel
- 声明队列
- 发送消息
- 关闭连接和channel

```java
public void testSendMessage() throws IOException, TimeoutException {
    // 1.建立连接
    ConnectionFactory factory = new ConnectionFactory();
    // 1.1.设置连接参数,分别是：主机名、端口号、vhost、用户名、密码
    factory.setHost("10.211.55.4");
    factory.setPort(5672);
    factory.setVirtualHost("/");
    factory.setUsername("rolyfish");
    factory.setPassword("123456");
    // 1.2.建立连接
    Connection connection = factory.newConnection();

    // 2.创建通道Channel
    Channel channel = connection.createChannel();

    // 3.创建队列
    String queueName = "simple.queue";
    channel.queueDeclare(queueName, false, false, false, null);

    // 4.发送消息
    String message = "hello, rabbitmq!";
    channel.basicPublish("", queueName, null, message.getBytes());
    System.out.println("发送消息成功：【" + message + "】");

    // 5.关闭通道和连接
    channel.close();
    connection.close();

}
```

![image-20230502172025541](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502172025541.png)

##### Consumer实现

代码思路：

- 建立连接
- 创建Channel
- 声明队列
- 订阅消息

```java
// 1.建立连接
ConnectionFactory factory = new ConnectionFactory();
// 1.1.设置连接参数,分别是：主机名、端口号、vhost、用户名、密码
factory.setHost("10.211.55.4");
factory.setPort(5672);
factory.setVirtualHost("/");
factory.setUsername("rolyfish");
factory.setPassword("123456");
// 1.2.建立连接
Connection connection = factory.newConnection();

// 2.创建通道Channel
Channel channel = connection.createChannel();

// 3.创建队列
String queueName = "simple.queue";
channel.queueDeclare(queueName, false, false, false, null);

// 4.订阅消息
channel.basicConsume(queueName, true, new DefaultConsumer(channel){
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body) throws IOException {
        // 5.处理消息
        String message = new String(body);
        System.out.println("接收到消息：【" + message + "】");
    }
});
System.out.println("等待接收消息。。。。");
```

![image-20230502172149311](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502172149311.png)

##### 总结

基本消息队列的消息发送流程：

1. 建立connection

2. 创建channel

3. 利用channel声明队列

4. 利用channel向队列发送消息

基本消息队列的消息接收流程：

1. 建立connection

2. 创建channel

3. 利用channel声明队列

4. 定义consumer的消费行为handleDelivery()

5. 利用channel将消费者与队列绑定

## SpringAMQP

### AMQP

[AMQP](https://www.amqp.org/about/what)

> 高级消息队列协议（AMQP）是一种用于在应用程序或组织之间传递业务消息的开放标准。它连接系统,为业务流程提供所需的信息,并可靠地向前传输实现其目标的指令。

### SpringAMQP

[SpringAMQP](https://spring.io/projects/spring-amqp)

> Spring AMQP是一个基于AMQP协议的项目,它将Spring概念结合AMQP。提供了一个抽象template实现消息的发送和接收。

特征：

- Listener container for asynchronous processing of inbound messages

  > 监听器容器异步处理入栈消息

- RabbitTemplate for sending and receiving messages

  > RabbitTemplate接收和发送消息

- RabbitAdmin for automatically declaring queues, exchanges and bindings

  > RabbitAdmin自动的创建队列、交换机以及他们的绑定



### BasicQueue

> 简单队列模型。

#### 引入依赖

```xml
<!--AMQP依赖,包含RabbitMQ-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

#### 发送消息

首先配置MQ地址,在publisher服务的application.yml中添加配置：

```yaml
spring:
  rabbitmq:
    host: 10.211.55.4
    port: 5672 # 端口
    virtual-host: / # 虚拟主机
    username: rolyfish # 用户名
    password: 123456 # 密码
```

然后在publisher服务中编写测试类SpringAmqpTest,并利用RabbitTemplate实现消息发送：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringAMQPTest {
@Autowired
RabbitTemplate rabbitTemplate;
    @Test
    public void test() {
        String queueName = "simple.queue";
        Map<String, String> message = Map.of("k1", "v1", "k2", "v2");
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
```



#### 接收消息

首先配置MQ地址,在consumer服务的application.yml中添加配置：

```yaml
spring:
  rabbitmq:
    host: 10.211.55.4
    port: 5672 # 端口
    virtual-host: / # 虚拟主机
    username: rolyfish # 用户名
    password: 123456 # 密码
```

然后在consumer服务的`cn.itcast.mq.listener`包中新建一个类SpringRabbitListener,代码如下：

```java
@Slf4j
@Component
public class SpringRabbitListener {
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(Map<String, String> message) throws InterruptedException {
        log.info("spring 消费者接收到消息：【{}】", message);
    }
}
```



#### 小结

> 消息发送者发送消息步骤

- 引入amqp依赖

- 配置RabbitMQ

- 通过RabbitTemplate模板化发送消息至消息队列

  > RabbitTemplate简化的消息发送流程,他会帮我们管理connection和channel



> 消息接收者接收消息

- 引入amqp依赖
- 配置RabbitMQ
- 定义Component,添加方法加上@RabbitListener注解



### WorkQueue

Work queues,也被称为（Task queues）,任务模型。简单来说就是**让多个消费者绑定到一个队列,共同消费队列中的消息**。

当消息处理比较耗时的时候,可能生产消息的速度会远远大于消息的消费速度。长此以往,消息就会堆积越来越多,无法及时处理。

此时就可以使用work 模型,多个消费者共同处理消息处理,速度就能大大提高了

![image-20210717164238910](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717164238910.png)



#### 消息发送

这次我们循环发送,模拟大量消息堆积现象。

在publisher服务中的SpringAmqpTest类中添加一个测试方法：

```java
/**
 * workQueue
 * 向队列中不停发送消息,模拟消息堆积。
 */
@Test
public void testWorkQueue() throws InterruptedException {
    // 队列名称
    String queueName = "simple.queue";
    // 消息
    String message = "hello, message_";
    for (int i = 0; i < 50; i++) {
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message + i);
        Thread.sleep(20);
    }
}
```



#### 接收消息

> 定义两个消息消费者,一个处理速度较快,一个速度较慢。我们希望处理消息较快的消费者处理更多的消息。

```java
/**
 * workqueue  listener1
 */
@RabbitListener(queues = "simple.queue")
public void workQueueMessageListener1(String message) {
    log.info("springListener1 消费者接收到消息：【{}】", message);
}

/**
 * workqueue  listener2
 */
@RabbitListener(queues = "simple.queue")
public void workQueueMessageListener2(String message) {
    log.error("springListener2 消费者接收到消息：【{}】", message);
}
```

#### 测试

> 消息处理并非在一秒内结束,消息也不是处理较快的消费者处理较多消息。

![image-20230502194810187](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502194810187.png)

#### 限制预取

> RocketMQ消费者有预取消息机制,消息到达消息队列,会提前将消息拿到,即便消费者没有能力处理。
>
> 所以需要限制消息预取,消息处理完了再来消息队列取消息。

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        prefetch: 1 # 限制消费者每次只取一个消息,消息处理完成再去消息队列取消息
```

#### 小结

Work模型的使用：

- 多个消费者绑定到一个队列,同一条消息只会被一个消费者处理
- 通过设置prefetch来控制消费者预取的消息数量



### pub/sub



![image-20210717165309625](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717165309625.png)



可以看到,在订阅模型中,多了一个exchange角色,而且过程略有变化：

- Publisher：生产者,也就是要发送消息的程序,但是不再发送到队列中,而是发给exchange（交换机）
- Exchange：交换机。一方面,接收生产者发送的消息。另一方面,知道如何处理消息,例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作,取决于Exchange的类型。Exchange有以下3种类型：
  - Fanout：广播,将消息交给所有绑定到交换机的队列
  - Direct：定向,把消息交给符合指定routing key 的队列
  - Topic：通配符,把消息交给符合routing pattern（路由模式） 的队列
- Consumer：消费者,与以前一样,订阅队列,没有变化
- Queue：消息队列也与以前一样,接收消息、缓存消息。



**Exchange（交换机）只负责转发消息,不具备存储消息的能力**,因此如果没有任何队列与Exchange绑定,或者没有符合路由规则的队列,那么消息会丢失！



#### Fanout

Fanout,广播。即发送到交换机的消息会广播到所有绑定的消息队列。

![image-20210717165438225](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717165438225.png)

在广播模式下,消息发送流程是这样的：

- 1）  可以有多个队列
- 2）  每个队列都要绑定到Exchange（交换机）
- 3）  生产者发送的消息,只能发送到交换机,交换机来决定要发给哪个队列,生产者无法决定
- 4）  交换机把消息发送给绑定过的所有队列
- 5）  订阅队列的消费者都能拿到消息

我们的计划是这样的：

- 创建一个交换机 itcast.fanout,类型是Fanout
- 创建两个队列fanout.queue1和fanout.queue2,绑定到交换机itcast.fanout

![image-20210717165509466](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717165509466.png)

##### 声明队列、交换机并绑定

> Sping-amqp定义了接口Exchange,其不同的实现就是不同的交换机：

![image-20230502220843320](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502220843320.png)

> 这里定义一个FanoutExchange,并将其与队列绑定

```java
@Configuration
public class FanoutExchangeConfig {

    /**
     * 声明交换机
     */
    protected @Bean FanoutExchange fanoutExchange() {
        return new FanoutExchange("itcast.fanout");
    }

    /**
     * 声明queue1
     */
    protected @Bean Queue fanoutQueue1() {
        return new Queue("fanout.queue1");
    }

    /**
     * 声明queue2
     */
    protected @Bean Queue fanoutQueue2() {
        return new Queue("fanout.queue2");
    }

    /**
     * 交换机与queue1绑定
     */
    protected @Bean Binding fanoutBandingQueue1(@Autowired FanoutExchange fanoutExchange, @Autowired /*@Qualifier("fanoutQueue1") */Queue fanoutQueue1) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    /**
     * 交换机与queue2绑定
     */
    protected @Bean Binding fanoutBandingQueue2(@Autowired FanoutExchange fanoutExchange, @Autowired /*@Qualifier("fanoutQueue1") */Queue fanoutQueue2) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
```

##### 发送消息

```java
/**
 * fanoutExchange
 * 向itcast.fanout交换机发送消息,交换机决定消息发送到哪个队列
 */
@Test
public void testFanoutExchange() throws InterruptedException {
    // 队列名称
    String exchange = "itcast.fanout";
    // 消息
    String message = "exchange send message";
    rabbitTemplate.convertAndSend(exchange, "", message);
}
```



##### 消息接收

```java
/**
 * workqueue  listener2
 */
@RabbitListener(queues = "fanout.queue1")
public void listenFanoutQueue1(String msg) {
    log.info("队列fanout.queue1........接收到消息：【" + msg + "】" + LocalTime.now());
}

/**
 * workqueue  listener2
 */
@RabbitListener(queues = "fanout.queue2")
public void listenFanoutQueue2(String msg) {
    log.info("队列fanout.queue2........接收到消息：【" + msg + "】" + LocalTime.now());
}
```

> 消息生产者只发送了一条消息到exchange,exchange将消息广播到了其所绑定的两个队列。

![image-20230502222732533](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502222732533.png)

![image-20230502223032945](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502223032945.png)

##### 小结

交换机的作用是什么？

- 接收publisher发送的消息
- 将消息按照规则路由到与之绑定的队列
- 不能缓存消息,路由失败,消息丢失
- FanoutExchange的会将消息路由到每个绑定的队列

声明队列、交换机、绑定关系的Bean是什么？

- Queue
- FanoutExchange
- Binding



#### Direct

>  在Fanout模式中,一条消息,会被所有订阅的队列都消费。但是,在某些场景下,我们希望不同的消息被不同的队列消费。这时就要用到Direct类型的Exchange。
>
> 在Direct模型下：
>
> - 队列与交换机的绑定,不能是任意绑定了,而是要指定一个`RoutingKey`（路由key）
> - 消息的发送方在 向 Exchange发送消息时,也必须指定消息的 `RoutingKey`。
> - Exchange不再把消息交给每一个绑定的队列,而是根据消息的`Routing Key`进行判断,只有队列的`Routingkey`与消息的 `Routing key`完全一致,才会接收到消息

![image-20210717170041447](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717170041447.png)

##### 案例需求如下

1. 利用@RabbitListener声明Exchange、Queue、RoutingKey

2. 在consumer服务中,编写两个消费者方法,分别监听direct.queue1和direct.queue2

3. 在publisher中编写测试方法,向itcast. direct发送消息



##### 基于注解声明队列交换机和routingkey

```java
/**
 * directExchange  listener1
 */
@RabbitListener(bindings = @QueueBinding(
        exchange = @Exchange(value = "itcast.direct", type = ExchangeTypes.DIRECT), // 交换机
        value = @Queue(value = "direct.queue1"), // 队列名称
        key = {"blue", "red"}
))
public void listenDirectQueue1(String msg) {
    log.info("交换机itcast.direct,队列direct.queue1。消息：【" + msg + "】" + LocalTime.now());
}

/**
 * directExchange  listener2
 */
@RabbitListener(bindings = @QueueBinding(
        exchange = @Exchange(value = "itcast.direct", type = ExchangeTypes.DIRECT), // 交换机
        value = @Queue(value = "direct.queue2"), // 队列名称
        key = {"yello", "red"}
))
public void listenDirectQueue2(String msg) {
    log.info("交换机itcast.direct,队列direct.queue2。消息：【" + msg + "】" + LocalTime.now());
}
```



##### 消息发送

```java
/**
 * DirectExchange
 * 向itcast.direct交换机发送消息,交换机根据routingkey决定发送到哪个消息队列
 */
@Test
public void testDirectExchange() {
    // 队列名称
    String exchange = "itcast.direct";
    // 1、消息  routing  blue
    // String message = "directexchange send message blue";
    //
    // rabbitTemplate.convertAndSend(exchange, "blue", message);
    // 2、消息  routing  red
    String message = "directexchange send message red";
    rabbitTemplate.convertAndSend(exchange, "red", message);
}
```

第一次routingkey设置为blue,只会被dircet.queue1的监听器监听到。第二次发送routingkey设置为red,会被dircet.queue1和dircet.queue2的监听器都能监听到

![image-20230502225617807](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502225617807.png)

声明的交换机及其队列的routingkey信息：

![image-20230502225830052](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502225830052.png)

##### 小结

描述下Direct交换机与Fanout交换机的差异？

- Fanout交换机将消息路由给每一个与之绑定的队列
- Direct交换机根据RoutingKey判断路由给哪个队列
- 如果多个队列具有相同的RoutingKey,则与Fanout功能类似

基于@RabbitListener注解声明队列和交换机有哪些常见注解？

- @Queue
- @Exchange



#### Topic

`Topic`类型的`Exchange`与`Direct`相似,都是可以根据`RoutingKey`把消息路由到不同的队列。只不过`Topic`类型`Exchange`可以让队列在绑定`Routing key` 的时候使用通配符！

`Routingkey` 一般都是有一个或多个单词组成,多个单词之间以”.”分割,例如： `item.insert`

 通配符规则：

`#`：匹配一个或多个词

`*`：匹配不多不少恰好1个词

举例：

`item.#`：能够匹配`item.spu.insert` 或者 `item.spu`

`item.*`：只能匹配`item.spu`

![image-20210717170705380](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717170705380.png)

案例需求：

实现思路如下：

1. 并利用@RabbitListener声明Exchange、Queue、RoutingKey

2. 在consumer服务中,编写两个消费者方法,分别监听topic.queue1和topic.queue2

3. 在publisher中编写测试方法,向itcast. topic发送消息

![image-20210717170829229](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717170829229.png)

##### 基于注解声明队列交换机和routingkey

```java
/**
 * topicExchange  listener1
 */
@RabbitListener(bindings = @QueueBinding(
        exchange = @Exchange(value = "itcast.topic", type = ExchangeTypes.TOPIC), // 交换机
        value = @Queue(value = "topic.queue1"), // 队列名称
        key = {"china.#"}
))
public void listenTopicQueue1(String msg) {
    log.info("交换机itcast.topic,topic.queue1。消息：【" + msg + "】" + LocalTime.now());
}

/**
 * topicExchange  listener2
 */
@RabbitListener(bindings = @QueueBinding(
        exchange = @Exchange(value = "itcast.topic", type = ExchangeTypes.TOPIC), // 交换机
        value = @Queue(value = "topic.queue2"), // 队列名称
        key = {"#.news"}
))
public void listenTopicQueue2(String msg) {
    log.info("交换机itcast.topic,topic.queue2。消息：【" + msg + "】" + LocalTime.now());
}
```



##### 消息发送

```java
/**
 * TopicExchange
 * 向itcast.topic交换机发送消息,交换机根据routingkey决定发送到哪个消息队列
 * routingkey支持通配符
 */
@Test
public void testTopicExchange() {

    // 队列名称
    String exchange = "itcast.topic";
    // 1、消息  routing  china.news
    // String message = "message china news";
    //
    // rabbitTemplate.convertAndSend(exchange, "china.news", message);

    // 2、消息  routing  red
    String message = "message china weather";

    rabbitTemplate.convertAndSend(exchange, "china.weather", message);
}
```

![image-20230502231152752](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502231152752.png)

![image-20230502231351902](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502231351902.png)



##### 小结

描述下Direct交换机与Topic交换机的差异？

- Topic交换机接收的消息RoutingKey必须是多个单词,以 `**.**` 分割
- Topic交换机与队列绑定时的bindingKey可以指定通配符
- `#`：代表0个或多个词
- `*`：代表1个词



### 消息转换器

> rabbitTemplate.convertAndSend方法参数是Object,也就是兼容任何类型,底层通过一个消息转化器回将消息转化成自己存储到消息队列中,默认是SimpleMessageConverter底层使用JDK序列化(objos、objis)。

#### 测试默认消息转化器

```java
@Test
public void test() {
    String queueName = "simple.queue";
    Map<String, String> message = Map.of("k1", "v1", "k2", "v2");
    rabbitTemplate.convertAndSend(queueName, message);
}
```

![image-20230502233821431](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502233821431.png)



#### 自定义消息转化器

> 自定义消息转化器代替默认消息转化器。

在消息生产者和消费者注入自定义消息转化器

```java
@Configuration
public class MQMessageConverter implements MessageConverter {
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {

        return new Message(JSON.toJSONString(object).getBytes(), messageProperties);

    }
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        final String s = new String(message.getBody());
        return JSON.toJSON(s);
    }
}
```

测试:

![image-20230502234913101](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502234913101.png)

消费消息：

![image-20230502235026878](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230502235026878.png)



#### Jackson

> jackson.dataformat帮我们做好了现成的消息转化器

消息生产者和消息消费者都添加Jackson依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.15.0</version>
</dependency>
```

配置消息转换器。

在启动类中添加一个Bean即可：

```java
@Bean
public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
}
```



## MQ高级

消息队列的使用需要考虑的问题：

- 消息的可靠性

  > 确保消息至少被消费一次。
  >
  > 消息发送者确认机制confirm-rollback、return-callback
  >
  > 消息消费者确认机制, auto模式,消息失败重试机制(本地重试默认reject),失败策略(reject、republish、publish到其他(error)交换机)
  >
  > 消息持久化, 默认都是开启的, 显示开启可读性好一些

- 延迟消息问题

  > TTL(消息或者队列设置TTL)配合死信交换机,可实现延时队列
  >
  > 延时队列插件delayExchange, 通过交换机保存消息,到延时时间在发送到指定队列

- 高可用

  > 集群(普通集群[消息丢失风险]、镜像集群[基于主从同步节点各自备份镜像,主节点挂了镜像节点可以替换主节点]、仲裁队列[用于替换镜像集群, raft协议保证强一致性])

- 消息堆积

  > 消息默认存储于内存, 队列满了,mq将会拒绝服务,或者抛弃消息,造成消息死信。惰性队列的消息存储于磁盘, 但是可用性降低,性能受限于磁盘IO。

### 消息可靠性

MQ中消息的流动过程：

<img src="https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210718155059371.png" alt="image-20210718155059371" style="zoom:50%;" />

其中的每一步都可能导致消息丢失,常见的丢失原因包括：

- 发送时丢失：
  - 生产者发送的消息未送达exchange
  - 消息到达exchange后未到达queue 
- MQ宕机,queue将消息丢失
- consumer接收到消息后未消费就宕机



#### 保证消息可靠性方案

针对以上问题,RabbitMQ分别给出了解决方案：

- 生产者发送消息确认机制
- mq持久化
- 消费者确认机制
- 失败重试机制



#### 生产者消息确认机制

> RabbitMQ提供了publisher confirm机制来避免消息发送到MQ过程中丢失。

> 这种机制必须给每个消息指定一个唯一ID。消息发送到MQ以后,会返回一个结果给发送者,表示消息是否处理成功。

生产者消息确认步骤：

- publisher-confirm,发送者确认
  - 消息成功投递到交换机,返回ack
  - 消息未投递到交换机,返回nack
- publisher-return,发送者回执
  - 消息投递到交换机了,但是没有路由到队列。返回ACK,及路由失败原因。

##### 实现

###### docker运行Rmq

1. 拉取镜像

   `docker pull rabbitmq:management-alpine`

2. 运行

   ```shell
   docker run \
    -e RABBITMQ_DEFAULT_USER=rolyfish \
    -e RABBITMQ_DEFAULT_PASS=123456 \
    --name rabbitmq \
    --hostname mq1 \
    -p 15672:15672 \
    -p 5672:5672 \
    -d \
   rabbitmq:management-alpine
   ```

   

###### 修改配置

> 消息发送者(publisher)服务,添加配置：

```yaml
spring:
  rabbitmq:
    publisher-confirm-type: correlated # 消息确认机制, 关联异步回调
    publisher-returns: true # 开启消息回执
    template:
      mandatory: true
```

说明：

- `publish-confirm-type`：开启publisher-confirm,这里支持两种类型：
  - `simple`：同步等待confirm结果,直到超时
  - `correlated`：异步回调,定义ConfirmCallback,MQ返回结果时会回调这个ConfirmCallback
- `publish-returns`：开启publish-return功能,同样是基于callback机制,不过是定义ReturnCallback
- `template.mandatory`：定义消息路由失败时的策略。true,则调用ReturnCallback；false：则直接丢弃消息

###### 定义return回调

> 每个RabbitTemplate只能配置一个ReturnCallback,因此需要在项目加载时配置：
>
> 修改publisher服务,添加一个：

```java
@Slf4j
@Configuration
public class CommonConfig implements ApplicationContextAware {
@Override
public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    // 获取RabbitTemplate对象
    RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
    // 配置ReturnCallback
    rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
        // 记录日志
        log.error("消息发送到队列失败,响应码：{}, 失败原因：{}, 交换机: {}, 路由key：{}, 消息: {}",
                 replyCode, replyText, exchange, routingKey, message.toString());
        // 如果有需要的话,重发消息
    });
}
}
```

###### 定义confirm回调

> ConfirmCallback可以在发送消息时指定,因为每个业务处理confirm成功或失败的逻辑不一定相同。
>
> 在publisher服务的cn.itcast.mq.spring.SpringAmqpTest类中,定义一个单元测试方法：

```java
@Test
public void testSendMessage2SimpleQueue() throws InterruptedException {
    // 1.准备消息
    String message = "hello, spring amqp!";
    // 2.准备CorrelationData
    // 2.1.消息ID
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    // 2.2.准备ConfirmCallback
    correlationData.getFuture().addCallback(result -> {
        // 判断结果
        if (Boolean.TRUE.equals(result.isAck())) {
            // ACK
            log.debug("消息成功投递到交换机！消息ID: {}", correlationData.getId());
        } else {
            // NACK
            log.error("消息投递到交换机失败！消息ID：{}", correlationData.getId());
            // 重发消息
        }
    }, ex -> {
        // 记录日志
        log.error("消息发送失败！", ex);
        // 重发消息
    });
    // 3.发送消息
    rabbitTemplate.convertAndSend("amq.topic", "simple.test", message, correlationData);
}
```

###### 小结

消息发送分两个阶段：

- 生产者到交换机
- 交换机到队列

所以需要两次确认机制：

- confirmCallback

  > 成功到达交换机返回ack确认, 没有成功到达交换机则返回nack,并且在回调中进行重试

- returnCallBack

  > 消息发送到队列失败,返回错误信息,并在回调中重试

confirm可以定义多个,return只能定义一个：

> 消息发送者发送消息只能确认交换机, 由交换机路由到不同的队列。

#### 消息持久化

生产者确认可以确保消息投递到RabbitMQ的队列中,但是消息发送到RabbitMQ以后,如果突然宕机,也可能导致消息丢失。

要想确保消息在RabbitMQ中安全保存,必须开启消息持久化机制。

- 交换机持久化
- 队列持久化
- 消息持久化

##### 交换机持久化

RabbitMQ中交换机默认基于内存存储,是非持久化的,mq重启后就丢失。

![image-20230510135535180](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510135535180.png)

可以通过选择Durability位Durable来实现交换机持久化。



> SpringAMQP中创建交换机时可以指定交换机持久化和空绑定删除。
>
> SpringAMQP默认情况下就是如下配置,但是显示配置可读性好

```java
/**
 * 开启交换机持久化  durable=true
 * 关闭空绑定自动删除
 */
@Bean
public DirectExchange simpleDirect(){
    return new DirectExchange("simple.direct",true,false);
}
```

![image-20230510135956935](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510135956935.png)

##### 队列持久化

```java
// 可以通过new Queue的方式来配置队列持久化
Queue(String name, boolean durable, boolean exclusive, boolean autoDelete) 

// 也可以通过QueueBuilder构建持久化队列
@Bean
public Queue simpleQueue(){
    // 使用QueueBuilder构建队列,durable就是持久化的
    return QueueBuilder.durable("simple.queue").build();
}
```

![image-20230510140319211](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510140319211.png)

##### 消息持久化

利用SpringAMQP发送消息时,可以设置消息的属性(MessageProperties),指定delivery-mode：

默认情况下,SpringAMQP发出的任何消息都是持久化的,不用特意指定。

```java
// 消息默认支持持久化
MessageDeliveryMode DEFAULT_DELIVERY_MODE = MessageDeliveryMode.PERSISTENT;
```

消息发送者发送消息指定消息持久化：

```java
// 发送的消息如果不是Message类型,则会使用message进行包装,默认的配置就是支持持久化的
@Test
public void testDurableMessage() {
    // 1.准备消息
    Message message = MessageBuilder.withBody("hello, spring".getBytes(StandardCharsets.UTF_8))
            .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
            .build();
    // 2.发送消息
    rabbitTemplate.convertAndSend("simple.queue", message);
}
```

#### 消费者消息确认机制

RabbitMQ确认消息被消费者消费(发送ack回执)后会立刻删除。

> 消费者返回消息确认机制很重要,如果不返回ack,由于消费者宕机可能会导致消息丢失(未能成功消费一次)。

而SpringAMQP则允许配置三种确认模式：

- manual：手动ack,需要在业务代码结束后,调用api发送ack。
- auto：自动ack,由spring监测listener代码是否出现异常,没有异常则返回ack；抛出异常则返回nack
- none：关闭ack,MQ假定消费者获取消息后会成功处理,因此消息投递后立即被删除



##### none模式

1. 配置

   ```yaml
   spring:
     rabbitmq:
       listener:
         simple:
           acknowledge-mode: none # 关闭消息确认
   ```

2. 测试

   > 消费者消息确认机制为node模式下, 不会返回ack给mq, 消息被阅读mq就会删除消息,如果消费者出现异常则会丢失消息

   ![image-20230510143943590](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510143943590.png)





##### auto模式

1. 修改配置

   ```yaml
   spring:
     rabbitmq:
       listener:
         simple:
           acknowledge-mode: auto # 开启消息确认, 由Spring管理。 消息消费成功返回ack、失败返回nack
   ```

2. 测试

   > 消费者消息确认机制为auto时,会由Spring代理消费者, 当成功消费返回ack给mq,失败则返回nack给mq。
   >
   > 消息被阅读后会进入Unacked状态, 当收到ack后会删除消息, 收到nack则将消息放回到消息队列。
   >
   > 默认的重试机制是mq无限循环立刻requeue将消息放回消息队列。

   ![image-20230510145130754](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510145130754.png)





#### 消息失败重试机制

> auto默认的消息重试机制是无限循环立刻重试,这样会给MQ带来压力,不太好。

##### 本地重试

我们可以利用Spring的retry机制，在消费者出现异常时利用本地重试，而不是无限制的requeue到mq队列。达到最大消费次数或者最大重试时长后丢弃消息。

1. 修改配置

   ```yaml
   spring:
     rabbitmq:
       listener:
         simple:
           retry:
             enabled: true # 开启消费者失败重试
             initial-interval: 1000 # 初识的失败等待时长为1秒
             multiplier: 1 # 失败的等待时长倍数，下次等待时长 = multiplier * last-interval
             max-attempts: 3 # 最大重试次数
             stateless: true # true无状态；false有状态。如果业务中包含事务，这里改为false
             max-interval: 10000 # 最大重试时长 默认10000ms
   ```

2. 测试

   ![image-20230510150600970](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510150600970.png)





##### 失败策略

> 本地重试解决了,消息确认机制auto模式无线循环requeue问题,但是默认的失败策略会丢弃消息。这是不友好的。
>
> Spring提供了以下三种失败策略：MessageRecovery的子类
>
> - RejectAndDontRequeueRecoverer：重试耗尽后，直接reject，丢弃消息。默认就是这种方式
> - ImmediateRequeueMessageRecoverer：重试耗尽后，返回nack，消息重新入队
>
> - RepublishMessageRecoverer：重试耗尽后，将失败消息投递到指定的交换机



###### ImmediateRequeueMessageRecoverer

1. 注入MessageRecovery

   ```java
   /**
    * - requeue  当消息消费失败,并且重试耗尽,则返回nack  mq requeue
    */
   @Bean
   public MessageRecoverer immediateRequeueMessageRecoverer() {
       return new ImmediateRequeueMessageRecoverer();
   }
   ```

2. 测试

   > 消息requeue

   ![image-20230510154011765](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510154011765.png)



###### RepublishMessageRecoverer

> 这种失败策略比较好,他不会requeue,因为经过耗尽重试后,此消息大可能是不可用的。
>
> 所以不requeue而是 republish到其他交换机

1. 定义处理失败消息的交换机、队列、以及绑定关系

   ```java
   @Configuration
   public class ErrorMessageConfig {
       private static final String ERROR_DIRECT = "error.direct";
       private static final String ERROR_QUEUE = "error.queue";
       private static final String ERROR_ROUTING_KEY = "error";
       /**
        * 定义消费失败消息交换机
        */
       @Bean
       public DirectExchange errorMessageExchange() {
           return new DirectExchange(ERROR_DIRECT, true, false);
       }
       @Bean
       public Queue errorQueue() {
           return QueueBuilder.durable(ERROR_QUEUE).build();
       }
       @Bean
       public Binding errorMessageBinding() {
           return BindingBuilder.bind(errorQueue()).to(errorMessageExchange()).with(ERROR_ROUTING_KEY);
       }
       @Bean
       public MessageRecoverer republishMessageRecoverer(@Autowired RabbitTemplate rabbitTemplate) {
           return new RepublishMessageRecoverer(rabbitTemplate, ERROR_DIRECT, ERROR_ROUTING_KEY);
       }
   }
   ```

2. 测试

   ![image-20230510155130450](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510155130450.png)



 #### 小结

如何确保RabbitMQ消息的可靠性？

- 开启生产者确认机制，确保生产者的消息能到达队列
- 开启持久化功能，确保消息未消费前在队列中不会丢失
- 开启消费者确认机制为auto，由spring确认消息处理成功后完成ack
- 开启消费者失败重试机制，并设置MessageRecoverer，多次重试失败后将消息投递到异常交换机，交由人工处理



### 死信交换机

#### 死信

当一个队列中的消息满足下列情况之一时，可以成为死信（dead letter）：

- 消费者使用basic.reject或 basic.nack声明消费失败，并且消息的requeue参数设置为false
- 消息是一个过期消息，超时无人消费
- 要投递的队列消息满了，无法投递

#### 死信交换机

> 处理死信的交换机, 只不过死信交换机的死信来源是队列。

如果这个包含死信的队列配置了`dead-letter-exchange`属性，指定了一个交换机，那么队列中的死信就会投递到这个交换机中，而这个交换机称为**死信交换机**（Dead Letter Exchange，检查DLX）。

![image-20230510162401106](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510162401106.png)

#### 利用死信交换机消费死信

##### 定义死信交换机

> 创建普通交换机、普通队列和死信交换机、死信队列。
>
> 并且关闭republish失败策略

```java
Configuration
public class DLExchangeConfig {
    /**
     * 开启交换机持久化  durable=true
     * 关闭空绑定自动删除
     */
    @Bean
    public DirectExchange simpledlDirect() {
        return new DirectExchange("simpledl.direct", true, false);
    }
    /**
     * 普通队列,绑定死信交换机和routing key
     */
    @Bean
    public Queue simpledlQueue() {
        return QueueBuilder
                .durable("simpledl.queue")
                .deadLetterExchange("dl.direct")
                .deadLetterRoutingKey("dl")
                .build();
    }
    @Bean
    public Binding simple2Binding(@Autowired DirectExchange simpledlDirect, @Autowired Queue simpledlQueue) {
        return BindingBuilder.bind(simpledlQueue).to(simpledlDirect).with("simpledl");
    }
    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange dlDirect() {
        return new DirectExchange("dl.direct", true, false);
    }
    /**
     * 死信队列
     */
    @Bean
    public Queue dlQueue() {
        return QueueBuilder
                .durable("dl.queue")
                .build();
    }
    @Bean
    public Binding dlBinding(@Autowired DirectExchange dlDirect, @Autowired Queue dlQueue) {
        return BindingBuilder.bind(dlQueue).to(dlDirect).with("dl");
    }
}
```

##### 监听队列

> 监听普通队列和死信队列。
>
> 由于普通队列会失败, 且关闭了republish, 默认会reject。那么这条消息就变成死信了。

```java
/**
 * 监听普通队列
 */
@RabbitListener(queues = "simpledl.queue")
public void listenSimpledlQueue(String msg) {
    log.debug("消费者接收到simpledl.queue的消息：【" + msg + "】");
    System.out.println(1 / 0);
    log.info("消费者处理消息成功！");
}

/**
 * 监听死信队列
 */
@RabbitListener(queues = "dl.queue")
public void listenDlQueue(String msg) {
    log.debug("消费者接收到死信队列dl.queue的消息：【" + msg + "】");
    log.info("消费者处理消息成功！");
}
```

##### 发送消息

> 发送消息到死信队列。

```java
/**
 * 测试死信消息
 */
@Test
public void testDLMessage() {
    // 1.准备消息
    Message message = MessageBuilder.withBody("hello, dl".getBytes(StandardCharsets.UTF_8))
            .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
            .build();
    // 2.发送消息
    rabbitTemplate.convertAndSend("simpledl.direct","simpledl", message);
}
```



##### 测试结果

![image-20230510164951340](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510164951340.png)



#### TTL

> 可以为消息和队列设置TTL(超时时间), 如果消息超时则会被认为是死信。如果对列设置了私信队列和routing key则死信会publish到死信交换机上。

![image-20230510170120758](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510170120758.png)

1. 创建消费者, 声明死信交换机、routing key和死信队列

   ```java
   @RabbitListener(bindings = @QueueBinding(
           value = @Queue(name = "dl.ttl.queue", durable = "true"),// 声明死信队列,并开启持久化
           exchange = @Exchange(name = "dl.ttl.direct"), // 声明死信交换机
           key = "ttl" // 声明routing key
   ))
   public void listenTTLDlQueue(String msg) {
       log.info("接收到 dl.ttl.queue的延迟消息：{}", msg);
   }
   ```

2. 定义超时队列、交换机、routing key，并绑定死信交换机

   ```java
   @Configuration
   public class TTLMessageConfig {
       /**
        * 超时队列交换机
        */
       @Bean
       public DirectExchange ttlDirectExchange(){
           return new DirectExchange("ttl.direct");
       }
       /**
        * 超时队列
        */
       @Bean
       public Queue ttlQueue(){
           return QueueBuilder
                   .durable("ttl.queue")
                   .ttl(10000)
                   .deadLetterExchange("dl.direct") // 声明死信交换机和routing key
                   .deadLetterRoutingKey("dl")
                   .build();
       }
       @Bean
       public Binding ttlBinding(@Autowired Queue ttlQueue, @Autowired DirectExchange ttlDirectExchange){
           return BindingBuilder.bind(ttlQueue).to(ttlDirectExchange).with("ttl");
       }
   }
   ```

3. 发送消息

   ```java
   @Test
   public void testTTLMessage() {
       // 1.准备消息
       Message message = MessageBuilder
               .withBody("hello, ttl messsage".getBytes(StandardCharsets.UTF_8))
               .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
               .setExpiration("5000")
               .build();
       CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
       correlationData.getFuture().addCallback(result -> {
           // 判断结果
           if (Boolean.TRUE.equals(result.isAck())) {
               // ACK
               log.debug("消息成功投递到交换机！消息ID: {}", correlationData.getId());
           } else {
               // NACK
               log.error("消息投递到交换机失败！消息ID：{}", correlationData.getId());
               // 重发消息
           }
       }, ex -> {
           // 记录日志
           log.error("消息发送失败！", ex);
           // 重发消息
       });
   
       // 2.发送消息
       rabbitTemplate.convertAndSend("ttl.direct", "ttl", message,correlationData);
   }
   ```

4. 测试

   > 消息五秒超时, 因为没有监听者监听ttl.queue,所以这个消息必定超时。超时后此消息就会进入死信队列，且会被成功消费。

   ![image-20230510171012548](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510171012548.png)

### 延时队列

> 利用TTL结合死信交换机，我们实现了消息发出后，消费者延迟收到消息的效果。这种消息模式就称为延迟队列（Delay Queue）模式。
>
> 使用TTL加死信交换机实现延时队列不太方便。

延时队列使用场景：

- 延迟发送短信
- 用户下单，如果用户在15 分钟内未支付，则自动取消
- 预约工作会议，20分钟后自动通知所有参会人员

#### 延时队列插件

因为延迟队列的需求非常多，所以RabbitMQ的官方也推出了一个插件，原生支持延迟队列效果。

[RabbitMQ的插件列表](https://www.rabbitmq.com/community-plugins.html)

[delayed-message-exchange](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange)

#### 安装

1. [下载插件](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases)

2. 上传插件到挂载目录

   ```shell
   ## 插件挂载目录
   /Users/rolyfish/home/rabbitmq/plugins
   ```

3. 启动容器

   ```java
   docker run \
    -e RABBITMQ_DEFAULT_USER=rolyfish \
    -e RABBITMQ_DEFAULT_PASS=123456 \
    -v /Users/rolyfish/home/rabbitmq/plugins:/plugins \
    --name mq-standalone \
    --hostname mq1 \
    -p 15672:15672 \
    -p 5672:5672 \
    -d \
    rabbitmq:management-alpine
   ```

4. 进入mq容器，启动插件

   > 执行`rabbitmq-plugins enable rabbitmq_delayed_message_exchange`

   ![image-20230510182001554](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510182001554.png)

#### DelayExchange原理

> DelayExchange对官方的交换机做了一层包装, mq自带的交换机没有存储消息的功能, 而DelayExchange具备暂存消息的功能, 延时时间过了以后, DelayExchange会将消息发送到指定队列。

DelayExchange需要将一个交换机声明为delayed类型。当我们发送消息到delayExchange时，流程如下：

- 接收消息
- 判断消息是否具备x-delay属性
- 如果有x-delay属性，说明是延迟消息，持久化到硬盘，读取x-delay值，作为延迟时间
- 返回routing not found结果给消息发送者
- x-delay时间到期后，重新投递消息到指定队列

#### dashbord使用delayExchange

创建延时交换机：

声明类型携带参数

![image-20230510183801445](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510183801445.png)

发送消息到延时交换机：

声明延时时间

![image-20230510183940374](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510183940374.png)

#### SpringAMQP使用delay

> 使用延时交换机和使用普通交换机没有太大变化, 只要指定交换机类型为延时交换机即可。

1. 声明延时交换机

   - java代码方式

     ```java
     @Bean
     public DirectExchange delayExchange() {
         return ExchangeBuilder
                 .directExchange("delay.exchange") // 延时交换机名称
                 .durable(true) // 开启持久化
                 .delayed() // 声明延时交换机
                 .build();
     }
     
     @Bean
     public Queue delayQueue() {
         return QueueBuilder.durable("delay.queue").build();
     }
     
     @Bean
     public Binding delayBinding(@Autowired DirectExchange delayExchange, @Autowired Queue delayQueue) {
         return BindingBuilder.bind(delayQueue).to(delayExchange).with("delay");
     }
     ```

     ![image-20230510184714826](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510184714826.png)

   - 声明式注解方式

     ```java
     @RabbitListener(bindings = @QueueBinding(
             value = @Queue(name = "delay.ann.quque", durable = "true"),
             exchange = @Exchange(name = "delay.ann.exchange", delayed = "true"), // 声明延时交换机
             key = "delay" // 声明routing key
     ))
     public void listenDelayANOQueue(String msg) {
         log.info("接收到 delay.ann.queue的延迟消息：{}", msg);
     }
     ```

     

     ![image-20230510184920056](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510184920056.png)

2. 发送延时消息

   ```java
   /**
    * 测试延时交换机
    */
   @Test
   public void testDelayExchange() throws InterruptedException {
       // 1. 准备消息
       final Message message = MessageBuilder
               .withBody("hello, delay exchange".getBytes(StandardCharsets.UTF_8))
               .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
               .setHeader("x-delay", 5000)
               .build();
       // 2.准备CorrelationData
       // 2.1.消息ID
       CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
       // 2.2.准备ConfirmCallback
       correlationData.getFuture().addCallback(result -> {
           // 判断结果
           if (Boolean.TRUE.equals(result.isAck())) {
               // ACK
               log.debug("消息成功投递到交换机！消息ID: {}", correlationData.getId());
           } else {
               // NACK
               log.error("消息投递到交换机失败！消息ID：{}", correlationData.getId());
               // 重发消息
           }
       }, ex -> {
           // 记录日志
           log.error("消息发送失败！", ex);
           // 重发消息
       });
       // 3.发送消息
       rabbitTemplate.convertAndSend("delay.ann.exchange", "delay", message, correlationData);
   }
   ```

3. 测试

   ![image-20230510185502360](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510185502360.png)

4. 问题

   > 官方的交换机会立刻转发消息到指定对列, 如果转发失败则会出发return-rollback回调。延时队列也会触发回调,但是按理说不应该打印异常。
   >
   > 所以需要修改return-rollback回调函数, 当这个消息是延时消息, 则忽略

   ```java
   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       // 获取RabbitTemplate对象
       RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
       // 配置ReturnCallback
       rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
           // 判断是否是延迟消息
           Integer receivedDelay = message.getMessageProperties().getReceivedDelay();
           if (receivedDelay != null && receivedDelay > 0) {
               // 是一个延迟消息，忽略这个错误提示
               return;
           }
           // 记录日志
           log.error("消息发送到队列失败，响应码：{}, 失败原因：{}, 交换机: {}, 路由key：{}, 消息: {}",
                    replyCode, replyText, exchange, routingKey, message.toString());
           // 如果有需要的话，重发消息
       });
   }
   ```

### 消息堆积

#### 消息堆积问题

当生产者发送消息的速度超过了消费者处理消息的速度，就会导致队列中的消息堆积，直到队列存储消息达到上限。之后发送的消息就会成为死信，可能会被丢弃，这就是消息堆积问题。

![image-20230510203029116](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510203029116.png)

解决消息堆积思路：

- 增加更多消费者，提高消费速度。也就是我们之前说的work queue模式.

- 单个消费者异步处理业务

- 扩大队列容积，提高堆积上限

- 使用死信交换机消费死信

  > 死信队列也有上限, 不能从跟本上解决。并且死信队列体现在运维上而不是业务上

#### 惰性队列

从RabbitMQ的3.6.0版本开始，就增加了Lazy Queues的概念，也就是惰性队列。惰性队列的特征如下：

- 接收到消息后直接存入磁盘而非内存
- 消费者要消费消息时才会从磁盘中读取并加载到内存
- 支持数百万条的消息存储

##### 基于命令行设置Lazy-queue

而要设置一个队列为惰性队列，只需要在声明队列时，指定x-queue-mode属性为lazy即可。可以通过命令行将一个运行中的队列修改为惰性队列：

```shell
rabbitmqctl set_policy Lazy "^lazy-queue$" '{"queue-mode":"lazy"}' --apply-to queues  
```

命令解读：

- `rabbitmqctl` ：RabbitMQ的命令行工具
- `set_policy` ：添加一个策略
- `Lazy` ：策略名称，可以自定义
- `"^lazy-queue$"` ：用正则表达式匹配队列的名字
- `'{"queue-mode":"lazy"}'` ：设置队列模式为lazy模式
- `--apply-to queues  `：策略的作用对象，是所有的队列

##### 基于注解

![image-20230510204531096](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510204531096.png)

##### 基于bean

![image-20230510204626009](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510204626009.png)





#### 小结

消息堆积问题的解决方案？

- 队列上绑定多个消费者，提高消费速度
- 单个消费者开启异步任务
- 死信队列消费(一定程度上解决)
- 使用惰性队列，可以再mq中保存更多消息

惰性队列的优点有哪些？

- 基于磁盘存储，消息上限高
- 没有间歇性的page-out(会导致mq拒绝服务)，性能比较稳定

惰性队列的缺点有哪些？

- 基于磁盘存储，消息时效性会降低
- 性能受限于磁盘的IO





### MQ集群

RabbitMQ的是基于Erlang语言编写，而Erlang又是一个面向并发的语言，天然支持集群模式。RabbitMQ的集群有两种模式：

- **普通集群**：是一种分布式集群，将队列分散到集群的各个节点，从而提高整个集群的并发能力。
-  **镜像集群**：是一种主从集群，普通集群的基础上，添加了主从备份功能，提高集群的数据可用性。

镜像集群虽然支持主从，但主从同步并不是强一致的，某些情况下可能有数据丢失的风险。因此在RabbitMQ的3.8版本以后，推出了新的功能：**仲裁队列**来代替镜像集群，底层采用Raft协议确保主从的数据一致性。



#### 普通集群

##### 普通集群特征

普通集群，或者叫标准集群(classic cluster)，具备下列特征：

- 会在集群的各个节点间共享部分数据，包括：交换机、队列元信息(队列引用)。不包含队列中的消息。
- 当访问集群某节点时，如果队列不在该节点，会从数据所在节点传递到当前节点并返回
- 队列所在节点宕机，队列中的消息就会丢失

结构如图：

![image-20230510210439130](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510210439130.png)



##### 部署普通集群

1. 使用docker部署,准备镜像

   ![image-20230510210549748](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510210549748.png)

2. 准备网络

   ```java
   docker network create mq-simple-net
   ```

3. 准备挂载目录

   ```shell
   mkdir mq1 mq2 mq3 
   mkdir mq1/conf mq2/conf mq3/conf
   # 拷贝插件
   printf "%s\n" mq1 mq2 mq3 | xargs -t -I {} cp -r plugins {}/plugins
   ```

4. 配置文件

   ```shell
   vim rabbitmq.conf
   ```

   ```properties
   loopback_users.guest = false
   listeners.tcp.default = 5672
   cluster_formation.peer_discovery_backend = rabbit_peer_discovery_classic_config
   cluster_formation.classic_config.nodes.1 = rabbit@mq1
   cluster_formation.classic_config.nodes.2 = rabbit@mq2
   cluster_formation.classic_config.nodes.3 = rabbit@mq3
   ```

5. 记录cookie

   > MQ之间需要通过授权才可以通讯, 授权机制就是基于cookie的

   ```shell
   ## 获取cookie
   docker exec -it mq-standalone cat /var/lib/rabbitmq/.erlang.cookie
   ```

   ```tex
   PMWXGIOLOLUEJCMTCACU%
   ```

   ```shell
   # 创建 .erlang.cookie文件
   vim .erlang.cookie
   # 授权 不让改
   chmod 600 .erlang.cookie
   ```

6. 拷贝配置文件和.erlang.cookie到指定挂载卷目录下

   ```shell
   printf "%s\n" mq1 mq2 mq3 | xargs -t -I {} cp  rabbitmq.conf {}/conf/
   printf "%s\n" mq1 mq2 mq3 | xargs -t -I {} cp .erlang.cookie {}/conf/
   ```

7. 启动集群

   ```shell
   docker run -d --net mq-simple-net \
   -v ${PWD}/mq1/conf/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf \
   -v ${PWD}/mq1/conf/.erlang.cookie:/var/lib/rabbitmq/.erlang.cookie \
   -v ${PWD}/mq1/plugins:/plugins \
   -e RABBITMQ_DEFAULT_USER=rolyfish \
   -e RABBITMQ_DEFAULT_PASS=123456 \
   --name mq1 \
   --hostname mq1 \
   -p 8071:5672 \
   -p 8081:15672 \
   rabbitmq:management-alpine
   ```

   ```shell
   docker run -d --net mq-simple-net \
   -v ${PWD}/mq2/conf/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf \
   -v ${PWD}/mq2/conf/.erlang.cookie:/var/lib/rabbitmq/.erlang.cookie \
   -v ${PWD}/mq2/plugins:/plugins \
   -e RABBITMQ_DEFAULT_USER=rolyfish \
   -e RABBITMQ_DEFAULT_PASS=123456 \
   --name mq2 \
   --hostname mq2 \
   -p 8072:5672 \
   -p 8082:15672 \
   rabbitmq:management-alpine
   ```

   ```shell
   docker run -d --net mq-simple-net \
   -v ${PWD}/mq2/conf/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf \
   -v ${PWD}/mq2/conf/.erlang.cookie:/var/lib/rabbitmq/.erlang.cookie \
   -v ${PWD}/mq2/plugins:/plugins \
   -e RABBITMQ_DEFAULT_USER=rolyfish \
   -e RABBITMQ_DEFAULT_PASS=123456 \
   --name mq3 \
   --hostname mq3 \
   -p 8073:5672 \
   -p 8083:15672 \
   rabbitmq:management-alpine
   ```

8. 查看集群状态

   ![image-20230510213033686](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510213033686.png)

   ![image-20230510213109297](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510213109297.png)

   ![image-20230510213146559](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510213146559.png)

#### 镜像集群

##### 镜像集群特征

镜像集群：本质是主从模式，具备下面的特征：

- 交换机、队列、队列中的消息会在各个mq的镜像节点之间同步备份。
- 创建队列的节点被称为该队列的**主节点，**备份到的其它节点叫做该队列的**镜像**节点。
- 一个队列的主节点可能是另一个队列的镜像节点
- 所有操作都是主节点完成，然后同步给镜像节点
- 主宕机后，镜像节点会替代成新的主

![image-20230510222014069](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510222014069.png)

##### 部署镜像集群

普通集群末实现,队列只保存在创建该队列的节点上。而镜像模式下，创建队列的节点被称为该队列的**主节点**，队列还会拷贝到集群中的其它节点，也叫做该队列的**镜像**节点。

当主节点宕机后, 镜像节点就会代替成为新的主节点,并且会在其他节点上再保存一份队列的镜像,达到高可用的目的。

总结如下：

- 镜像队列结构是一主多从（从就是镜像）
- 所有操作都是主节点完成，然后同步给镜像节点
- 主宕机后，镜像节点会替代成新的主（如果在主从同步完成前，主就已经宕机，可能出现数据丢失）
- 不具备负载均衡功能，因为所有操作都会有主节点完成（但是不同队列，其主节点可以不同，可以利用这个提高吞吐量）



###### 镜像模式

| ha-mode         | ha-params         | 效果                                                         |
| :-------------- | :---------------- | :----------------------------------------------------------- |
| 准确模式exactly | 队列的副本量count | 集群中队列副本（主服务器和镜像服务器之和）的数量。count如果为1意味着单个副本：即队列主节点。count值为2表示2个副本：1个队列主和1个队列镜像。换句话说：count = 镜像数量 + 1。如果群集中的节点数少于count，则该队列将镜像到所有节点。如果有集群总数大于count+1，并且包含镜像的节点出现故障，则将在另一个节点上创建一个新的镜像。 |
| all             | (none)            | 队列在群集中的所有节点之间进行镜像。队列将镜像到任何新加入的节点。镜像到所有节点将对所有群集节点施加额外的压力，包括网络I / O，磁盘I / O和磁盘空间使用情况。推荐使用exactly，设置副本数为（N / 2 +1）。 |
| nodes           | *node names*      | 指定队列创建到哪些节点，如果指定的节点全部不存在，则会出现异常。如果指定的节点在集群中存在，但是暂时不可用，会创建节点到当前客户端连接到的节点。 |

###### exactly(准确模式)

```shell
rabbitmqctl set_policy ha-two "^two\." '{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}'
```

- `rabbitmqctl set_policy`：固定写法
- `ha-two`：策略名称，自定义
- `"^two\."`：匹配队列的正则表达式，符合命名规则的队列才生效，这里是任何以`two.`开头的队列名称
- `'{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}'`: 策略内容
  - `"ha-mode":"exactly"`：策略模式，此处是exactly模式，指定副本数量
  - `"ha-params":2`：策略参数，这里是2，就是副本数量为2，1主1镜像
  - `"ha-sync-mode":"automatic"`：同步策略，默认是manual，即新加入的镜像节点不会同步旧的消息。如果设置为automatic，则新加入的镜像节点会把主节点中所有消息都同步，会带来额外的网络开销

配置：

```shell
docker exec -it mq1 /bin/bash
```

在Admin->policies下可以看到策略内容：

![image-20230510223300903](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510223300903.png)



测试：

![image-20230510223431656](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510223431656.png)

队列：

![image-20230510223428750](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510223428750.png)

重启mq1测试。在普通集群模式下, mq1宕机过后,由于其他节点只是保存的队列引用,导致队列不可用。那么镜像模式下即便主节点宕机了, 镜像节点会替换成为主节点,并且在其他节点上再做一次镜像备份。

![image-20230510223810527](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510223810527.png)



#### 仲裁队列

从RabbitMQ 3.8版本开始，引入了新的仲裁队列，他具备与镜像队里类似的功能，但使用更加方便。

##### 仲裁队列特征

仲裁队列：仲裁队列是3.8版本以后才有的新功能，用来替代镜像队列，具备下列特征：

- 与镜像队列一样，都是主从模式，支持主从数据同步
- 使用非常简单，没有复杂的配置
- 主从同步基于Raft协议，强一致

##### 添加仲裁队列

> 仲裁队列默认 count为5 ,也就是一主4镜像。

![image-20230510225210123](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510225210123.png)

查看队列信息：

默认一主4镜像, 不足的话所有节点都作为镜像节点

![image-20230510225327564](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510225327564.png)



##### Java代码创建仲裁集群

##### SpringAMQP连接MQ集群

```yaml
spring:
  rabbitmq:
    addresses: localhost:8071, localhost:8072, localhost:8073
```

##### 创建仲裁队列

```java
/**
 * 创建仲裁队列
 */
@Bean
public Queue quorumQueue() {
    return QueueBuilder.durable("quorum.queue2").quorum().build();
}
```

![image-20230510230217733](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20230510230217733.png)
