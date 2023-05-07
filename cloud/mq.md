# MQ



## 初识MQ

### 同步和异步通讯

- 同步通讯需要实时响应,是链式调用的过程
  - 比如通过RestTemplate或FeignClient发送http请求,需要等待响应才可以继续执行
- 异步通讯不需要实时响应,是一种事件驱动模型
  - 比如MQ,消息生产者生产或发布消息,消息消费者订阅或消费消息

>  同步调用的优缺点：

- 优点

  - 时效性较强，可以立即得到结果

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

  - 耦合度极低，每个服务都可以灵活插拔，可替换

    > 异步通讯引入中间件mq,服务提供方和服务消费方遵循中间件协议通讯,那么两者无需关心内部实现细节

  - 吞吐量提升：无需等待订阅者处理完成，响应更快速

    > 服务调用方只需发布消息无需真正执行业务

  - 故障隔离：服务没有直接调用，不存在级联失败问题

  - 调用间没有阻塞，不会造成无效的资源占用

  - 流量削峰：不管发布事件的流量波动多大，都由Broker接收，订阅者可以按照自己的速度去处理事件。流量由broker承受,使得整个服务流量平稳

- 缺点：

  - 架构复杂了，业务没有明显的流程线，不好管理
  - 需要依赖于Broker的可靠、安全、性能




### MQ产品

MQ，中文是消息队列（MessageQueue），字面来看就是存放消息的队列。也就是事件驱动架构中的Broker。

比较常见的MQ实现：

- ActiveMQ
- RabbitMQ
- RocketMQ
- Kafka

几种常见MQ的对比：

|            | **RabbitMQ**            | **ActiveMQ**                   | **RocketMQ** | **Kafka**  |
| ---------- | ----------------------- | ------------------------------ | ------------ | ---------- |
| 公司/社区  | Rabbit                  | Apache                         | 阿里         | Apache     |
| 开发语言   | Erlang                  | Java                           | Java         | Scala&Java |
| 协议支持   | AMQP，XMPP，SMTP，STOMP | OpenWire,STOMP，REST,XMPP,AMQP | 自定义协议   | 自定义协议 |
| 可用性     | 高                      | 一般                           | 高           | 高         |
| 单机吞吐量 | 一般                    | 差                             | 高           | 非常高     |
| 消息延迟   | 微秒级                  | 毫秒级                         | 毫秒级       | 毫秒以内   |
| 消息可靠性 | 高                      | 一般                           | 高           | 一般       |

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

- Exchanges  交换机 。负责消息路由，交换机可以和队列绑定,可实现广播

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

官方的HelloWorld是基于最基础的消息队列模型来实现的，只包括三个角色：

- publisher：消息发布者，将消息发送到队列queue
- queue：消息队列，负责接受并缓存消息
- consumer：订阅队列，处理队列中的消息

##### 引入依赖

```xml
<!--AMQP依赖，包含RabbitMQ-->
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
    // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
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
// 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
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

> 高级消息队列协议（AMQP）是一种用于在应用程序或组织之间传递业务消息的开放标准。它连接系统，为业务流程提供所需的信息，并可靠地向前传输实现其目标的指令。

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
<!--AMQP依赖，包含RabbitMQ-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

#### 发送消息

首先配置MQ地址，在publisher服务的application.yml中添加配置：

```yaml
spring:
  rabbitmq:
    host: 10.211.55.4
    port: 5672 # 端口
    virtual-host: / # 虚拟主机
    username: rolyfish # 用户名
    password: 123456 # 密码
```

然后在publisher服务中编写测试类SpringAmqpTest，并利用RabbitTemplate实现消息发送：

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

首先配置MQ地址，在consumer服务的application.yml中添加配置：

```yaml
spring:
  rabbitmq:
    host: 10.211.55.4
    port: 5672 # 端口
    virtual-host: / # 虚拟主机
    username: rolyfish # 用户名
    password: 123456 # 密码
```

然后在consumer服务的`cn.itcast.mq.listener`包中新建一个类SpringRabbitListener，代码如下：

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

Work queues，也被称为（Task queues），任务模型。简单来说就是**让多个消费者绑定到一个队列，共同消费队列中的消息**。

当消息处理比较耗时的时候，可能生产消息的速度会远远大于消息的消费速度。长此以往，消息就会堆积越来越多，无法及时处理。

此时就可以使用work 模型，多个消费者共同处理消息处理，速度就能大大提高了

![image-20210717164238910](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717164238910.png)



#### 消息发送

这次我们循环发送，模拟大量消息堆积现象。

在publisher服务中的SpringAmqpTest类中添加一个测试方法：

```java
/**
 * workQueue
 * 向队列中不停发送消息，模拟消息堆积。
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

- 多个消费者绑定到一个队列，同一条消息只会被一个消费者处理
- 通过设置prefetch来控制消费者预取的消息数量



### pub/sub



![image-20210717165309625](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717165309625.png)



可以看到，在订阅模型中，多了一个exchange角色，而且过程略有变化：

- Publisher：生产者，也就是要发送消息的程序，但是不再发送到队列中，而是发给exchange（交换机）
- Exchange：交换机。一方面，接收生产者发送的消息。另一方面，知道如何处理消息，例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作，取决于Exchange的类型。Exchange有以下3种类型：
  - Fanout：广播，将消息交给所有绑定到交换机的队列
  - Direct：定向，把消息交给符合指定routing key 的队列
  - Topic：通配符，把消息交给符合routing pattern（路由模式） 的队列
- Consumer：消费者，与以前一样，订阅队列，没有变化
- Queue：消息队列也与以前一样，接收消息、缓存消息。



**Exchange（交换机）只负责转发消息，不具备存储消息的能力**，因此如果没有任何队列与Exchange绑定，或者没有符合路由规则的队列，那么消息会丢失！



#### Fanout

Fanout,广播。即发送到交换机的消息会广播到所有绑定的消息队列。

![image-20210717165438225](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717165438225.png)

在广播模式下，消息发送流程是这样的：

- 1）  可以有多个队列
- 2）  每个队列都要绑定到Exchange（交换机）
- 3）  生产者发送的消息，只能发送到交换机，交换机来决定要发给哪个队列，生产者无法决定
- 4）  交换机把消息发送给绑定过的所有队列
- 5）  订阅队列的消费者都能拿到消息

我们的计划是这样的：

- 创建一个交换机 itcast.fanout，类型是Fanout
- 创建两个队列fanout.queue1和fanout.queue2，绑定到交换机itcast.fanout

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
- 不能缓存消息，路由失败，消息丢失
- FanoutExchange的会将消息路由到每个绑定的队列

声明队列、交换机、绑定关系的Bean是什么？

- Queue
- FanoutExchange
- Binding



#### Direct

>  在Fanout模式中，一条消息，会被所有订阅的队列都消费。但是，在某些场景下，我们希望不同的消息被不同的队列消费。这时就要用到Direct类型的Exchange。
>
> 在Direct模型下：
>
> - 队列与交换机的绑定，不能是任意绑定了，而是要指定一个`RoutingKey`（路由key）
> - 消息的发送方在 向 Exchange发送消息时，也必须指定消息的 `RoutingKey`。
> - Exchange不再把消息交给每一个绑定的队列，而是根据消息的`Routing Key`进行判断，只有队列的`Routingkey`与消息的 `Routing key`完全一致，才会接收到消息

![image-20210717170041447](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/mq_base/image-20210717170041447.png)

##### 案例需求如下

1. 利用@RabbitListener声明Exchange、Queue、RoutingKey

2. 在consumer服务中，编写两个消费者方法，分别监听direct.queue1和direct.queue2

3. 在publisher中编写测试方法，向itcast. direct发送消息



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
- 如果多个队列具有相同的RoutingKey，则与Fanout功能类似

基于@RabbitListener注解声明队列和交换机有哪些常见注解？

- @Queue
- @Exchange



#### Topic

`Topic`类型的`Exchange`与`Direct`相似，都是可以根据`RoutingKey`把消息路由到不同的队列。只不过`Topic`类型`Exchange`可以让队列在绑定`Routing key` 的时候使用通配符！

`Routingkey` 一般都是有一个或多个单词组成，多个单词之间以”.”分割，例如： `item.insert`

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

2. 在consumer服务中，编写两个消费者方法，分别监听topic.queue1和topic.queue2

3. 在publisher中编写测试方法，向itcast. topic发送消息

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

- Topic交换机接收的消息RoutingKey必须是多个单词，以 `**.**` 分割
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

