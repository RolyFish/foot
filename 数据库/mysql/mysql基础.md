## 安转Mysql



### docker安装mysql

#### windows

> 查看docker是否安装。

```shell
 docker --version
```

1. [dockerhup搜索镜像](https://hub.docker.com/_/mysql)

2. 拉取镜像

   ```shell
   $ docker pull mysql:8.0.33
   
   $ docker images
   REPOSITORY   TAG       IMAGE ID       CREATED      SIZE
   mysql        8.0.33    05db07cd74c0   8 days ago   565MB
   ```

3. 启动容器

   ```shell
   ## 准备挂载目录
   mkdir data conf
   
   ## 启动容器
   docker run --name mysql \
   -p 3316:3306 \
   -v /home/rolyfish/home/mysql/data:/var/lib/mysql \
   -v /home/rolyfish/home/mysql/conf:/etc/mysql/conf.d \
   -e MYSQL_ROOT_PASSWORD=123456 \
   -d mysql:8.0.33
   
   
   
   docker run --name mysql \
   -p 3316:3306 \
   -v ./data:/var/lib/mysql \
   -v ./conf:/etc/mysql/conf.d \
   -e MYSQL_ROOT_PASSWORD=123456 \
   -d mysql:8.0.33
   ```

### 可视化客户端

Navicat、Dbeaver、Idea



## mysql工作原理

![image-20230621171651868](D:\Desktop\myself\foot\数据库\mysql\assets\image-20230621171651868.png)

### 连接管理

- 三次握手客户端与mysql建立连接
- 用户名与密码校验并获取权限
  - 密码错误, 报异常
  - 密码正确, 获得权限, 后续操作基于此权限

> 连接管理模块的连接池和线程池：
>
> 不止有一个客户端会和mysql建立连接, 客户端也不止只会创建一个连接。
>
> 为了避免频繁创建销毁连接, 也为了避免创建过多连接。`MySQL`服务器里有专门的`TCP`连接池，采用长连接模式复用`TCP`连接。
>
> 为了加快处理速度mysql内部有线程池进行权限认证和获取。

### 解析与优化

> 客户端与mysql建立连接后就可以发送sql并进行sql解析和优化。

- 如果是查询语句`select sql`, mysql会优先查询缓存, 缓存命中直接返回数据

  > 使用缓存必须管理内存,  查询缓存在高本版中的mysql已经移除

- 如果不是查询语句或缓存未命中, sql语句会交给`分析器`进行解析

  > 我们给到mysql的只是一个字符串, 分析器会按照语法对sql进行解析

- 优化器优化sql, 并产出执行计划

  > 优化什么：①外连接转化为内连接 ②子查询转化为连接 ③ 表达式简化 ④ 查询条件数据类型优化 ⑤ 索引优化

- 执行器 拿到执行计划, 首先判断是否有权限。 调用存储引擎API, 执行执行计划



### 存储引擎

> 以上的连接管理、sql解析器、sql优化器、执行器统称为mysql的server层, 目的是为了产出执行计划。
>
> 执行计划产出后执行器会调用 存储引擎API, 真正的读写数据。

#### innodb

- 是MySQL默认的事务型存储引擎，==支持事务==
- 实现了四个标准的隔离级别，默认级别是==可重复读==(REPEATABLE READ)。在可重复读隔离级别下，通过多版本并发控制(MVCC)+ 间隙锁(Next-Key Locking)防止幻影读。
- ==MVCC机制==可替代锁，从而提高并发度
- 主键索引是聚簇索引，在索引中保存了数据(索引和数据保存在一起, 叶子节点保存数据)，对查询性能有很大的提升。
- 支持真正的在线热备份。
- 支持外键

> 例子

```sql
create database if not exists `demo`;
use `demo`;
drop table if exists `t_user`;
create table if not exists `t_user`
(
    `id`    int unsigned not null auto_increment comment '主键',
    `email` varchar(20) comment '邮箱',
    `phone` varchar(20)  not null comment '电话',
    `name`  varchar(100) not null comment '姓名',
    primary key (id) using BTREE,
    unique key (phone) using BTREE,
    key t_user_email_name_index (email, name)
)engine=innodb auto_increment=1 default charset=utf8mb3 comment '用户表';
show indexes from t_user;
insert into t_user(email, phone, name) VALUES ('email@qq.com','17501236947','李自成'),('email①@qq.com','17501236987','yyc');
```

数据库是文件系统,存储引擎是Innodb的表的文件结构：`*.ibd`文件存储着数据和索引信息。

![image-20230621194751122](D:\Desktop\myself\foot\数据库\mysql\assets\image-20230621194751122.png)



#### myisam

- 提供了大量的特性，包括压缩表、空间数据索引等。
- 不支持事务
- 不支持行级锁，只能对整张表加锁，读取时会对需要读到的所有表加共享锁，写入时则对表加排它锁
- 不支持数据恢复,存在数据丢失的风险
- 不支持外键

```sql
drop table if exists `t_user_bak`;
create table if not exists `t_user_bak`
(
    `id`    int unsigned not null auto_increment comment '主键',
    `email` varchar(20) comment '邮箱',
    `phone` varchar(20)  not null comment '电话',
    `name`  varchar(100) not null comment '姓名',
    primary key (id) using BTREE,
    unique key (phone) using BTREE,
    key t_user_email_name_index (email, name)
)engine=myisam auto_increment=1 default charset=utf8mb3 comment '用户表';
show indexes from t_user_bak;
insert into t_user_bak(email, phone, name) VALUES ('email@qq.com','17501236947','李自成'),('email①@qq.com','17501236987','yyc');
```

> `*.myd`文件存储着数据信息,`*.myi`文件存储着表索引信息,

![image-20230621194739417](D:\Desktop\myself\foot\数据库\mysql\assets\image-20230621194739417.png)

#### 对比

> 基本上无脑选innodb

|          | myisam | innodb       |
| -------- | ------ | ------------ |
| 事务     | 不支持 | 支持         |
| 并发     | 表级锁 | 行级锁、MVCC |
| 外键     | 不支持 | 支持         |
| 热备份   | 不支持 | 支持         |
| 崩溃恢复 | 不支持 | 支持         |

## mysql数据类型











































## Mysql日志













































