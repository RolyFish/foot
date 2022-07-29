Mysql从0到0.9

> [上一篇、Mysql基础]()||[下一篇]()

#### 事务

##### acid原则

- 原子性	每一个事务都是原子操作，要么都成功，要么都失败
- 一致性    事务提交后，数据库前后状态不变
- 隔离性     事务之间互不影响
- 持久性     事务提交，持久化到硬盘，不可回滚

`sql`操作事务

```sql
SET autocommit = 0;--关闭自定提交
UPDATE `account` SET `momey` =  `momey`-500  WHERE `name` = 'a';
UPDATE `account` SET `momey` =  `momey`+500 WHERE `name` = 'b';
COMMIT;--提交事务
SET autocommit = 1--开启自动提交
```

> 使用命令行操作事务不太方便，如果有兴趣可开启两个命令行工具。
>
> 这边在后面的JDBC演示。

#### explain

> 使用explain 来查看sql执行计划，只需要在原有select 基础上加上explain关键字就可

```sql
EXPLAIN  SELECT * FROM `account`
```

#### 三大范式

第一范式（1nf）

​	列名原子性 不可拆分   

​		家庭信息（四口人 盐城市）拆为----->家庭人口数 （四口人） 户籍（盐城）

第二范式（2nf）

​	每张表只描述一件事物

​		订单表包含产品信息（冗余字段产品名称、产地）

第三范式（3nf）

​	每一列都要和主键直接相关

> 结：有时冗余字段有利于`sql`执行效率和产品设计方案。所以说数据库设计应需求和规范相结合，权衡后得出可靠方案。