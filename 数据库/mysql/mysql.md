# Mysql基础



## Mysql数据类型



### 整型

> 长度越小，占用空间越小，查询速度越快，合理设置。
>
> 一般来说创建时长度设置较小，后期不够可以扩。

| 类型      | 长度(BIT) |      |
| --------- | --------- | ---- |
| TINYINT   | 8         |      |
| SMALLINT  | 16        |      |
| MEDIUMINT | 24        |      |
| INT       | 32        |      |
| BIGINT    | 64        |      |

INT(4) 中的数字只是规定了交互工具显示字符的个数，对于存储和计算来说是没有意义的。

> 使用如下语句创建`user`表

```sql
CREATE TABLE `user` (
  `id` int(4) unsigned zerofill NOT NULL COMMENT '主键',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT into user(id,name) values(1,'yyc'),(11111,'yyc');

SELECT * from user;
```

![image-20230330152155276](mysql.assets/image-20230330152155276.png)



### 浮点数



| 类型    |      |      |
| ------- | ---- | ---- |
| FLOAT   |      |      |
| DOUBLE  |      |      |
| DECIMAl |      |      |

FLOAT 和 DOUBLE 为浮点类型，DECIMAL 为高精度小数类型。CPU 原生支持浮点运算，但是不支持 DECIMAl 类型的计算，因此 DECIMAL 的计算比浮点类型需要更高的代价。

FLOAT、DOUBLE 和 DECIMAL 都可以指定列宽，例如 DECIMAL(18, 9) 表示总共 18 位，取 9 位存储小数部分，剩下 9 位存储整数部分



### 字符串

主要有 CHAR 和 VARCHAR 两种类型，一种是定长的，一种是变长的。

- VARCHAR 这种变长类型能够节省空间，因为只需要存储必要的内容。但是在执行 UPDATE 时可能会使行变得比原来长，当超出一个页所能容纳的大小时，就要执行额外的操作。MyISAM 会将行拆成不同的片段存储，而 InnoDB 则需要分裂页来使行放进页内。

- VARCHAR 会保留字符串末尾的空格，而 CHAR 会删除。

```sql
ALTER TABLE `user` drop COLUMN `value`;
ALTER TABLE `user` drop COLUMN `value2`;

ALTER TABLE `user` add COLUMN `value1` char; -- 默认长度为1
ALTER TABLE `user` add COLUMN `value2` char(10); -- 超过指定长度,会报错
```

### 时间和日期

MySQL 提供了两种相似的日期时间类型: DATETIME 和 TIMESTAMP。

#### 1. DATETIME

保存从 1001 年到 9999 年的日期和时间，精度为秒，使用 8 字节的存储空间。

默认情况下，MySQL 以一种可排序的、无歧义的格式显示 DATETIME 值，例如“2008-01-16 22:37:08”，这是 ANSI 标准定义的日期和时间表示方法。

#### 2.TIMESTAMP

保存从 1970 年 1 月 1 日午夜(格林威治时间)以来的秒数，使用 4 个字节，只能表示从 1970 年 到 2038 年。

==应该尽量使用 TIMESTAMP，因为它比 DATETIME 空间效率更高==

```sql
ALTER TABLE `user` add COLUMN `date1` datetime;
ALTER TABLE `user` add COLUMN `date2` TIMESTAMP;

INSERT into user(id,date1,date2) VALUES(1122212123,NOW(),NOW())
```



### text & blob

#### 1. blob 类型

blob(binary large object) 是一个可以存储二进制文件的容器，主要用于存储二进制大对象，例如可以存储图片，音视频等文件。按照可存储容量大小不同来分类，blob 类型可分为以下四种：

| 类型       | 可存储大小  | 用途                     |
| :--------- | :---------- | :----------------------- |
| TINYBLOB   | 0 - 255字节 | 短文本二进制字符串       |
| BLOB       | 0 - 65KB    | 二进制字符串             |
| MEDIUMBLOB | 0 - 16MB    | 二进制形式的长文本数据   |
| LONGBLOB   | 0 - 4GB     | 二进制形式的极大文本数据 |

其中最常用的就是 blob 字段类型了，最多可存储 65KB 大小的数据，一般可用于存储图标或 logo 图片。不过数据库并不适合直接存储图片，如果有大量存储图片的需求，请使用对象存储或文件存储，数据库中可以存储图片路径来调用。

#### 2. text 类型

text 类型同 char、varchar 类似，都可用于存储字符串，一般情况下，遇到存储长文本字符串的需求时可以考虑使用 text 类型。按照可存储大小区分，text 类型同样可分为以下四种：

| 类型       | 可存储大小            | 用途           |
| :--------- | :-------------------- | :------------- |
| TINYTEXT   | 0 - 255字节           | 一般文本字符串 |
| TEXT       | 0 - 65 535字节        | 长文本字符串   |
| MEDIUMTEXT | 0 - 16 772 150字节    | 较大文本数据   |
| LONGTEXT   | 0 - 4 294 967 295字节 | 极大文本数据   |

不过在日常场景中，存储字符串还是尽量用 varchar ，只有要存储长文本数据时，可以使用 text 类型。对比 varchar ，text 类型有以下特点：

- text 类型无须指定长度。
- 若数据库未启用严格的 sqlmode ，当插入的值超过 text 列的最大长度时，则该值会被截断插入并生成警告。
- text 类型字段不能有默认值。
- varchar 可直接创建索引，text 字段创建索引要指定前多少个字符。
- text 类型检索效率比 varchar 要低。

下面我们来具体测试下 text 类型的使用方法：

通过以上测试，我们注意到，text 类型可存储容量是以字节为单位而不是字符。例如 tinytext 最多存储 255 个字节而不是 255 个字符，在 utf8 字符集下，一个英文字母或数字占用一个字节，而一个中文汉字占用三个字节。也就是说 tinytext 最多存储 255/3=85 个汉字，text 最多存储 65535/3=21845 个汉字。而 varchar(M) 中的 M 指的是字符数，一个英文、数字、汉字都是占用一个字符，即 tinytext 可存储的大小并不比 varchar(255) 多。

#### 总结：

本篇文章介绍了 blob 及 text 字段类型相关知识。虽然数据库规范中一般不推荐使用 blob 及 text 类型，但由于一些历史遗留问题或是某些场景下，还是会用到这两类数据类型的。这篇文章仅当做个记录了，使用到的时候可以参考下。



### 优化数据类型

- 更小的通常更好；更小的数据类型通常更快，因为它们占用更少的磁盘、内存和CPU缓存，并且处理时需要的CPU周期也更少；
- 简单就好；例如，整形比字符串操作代价更低；使用内建类型而不是字符串来存储日期和时间；用整形存储IP地址等；
- 尽量避免NULL；如果查询中包含可为NULL的列，对MySQL来说更难优化，因为可为NULL 的列使得索引、索引统计和值比较都更复杂。尽管把可为NULL的列改为NOT NULL带来的性能提升比较小，但如果计划在列上创建索引，就应该尽量避免设计成可为NULL的列；



#### 字符串选型

##### VARCHAR 和 CHAR

VARCHAR是最常见的字符串类型。VARCHAR节省了存储空间，所以对性能也有帮助。但是，由于行是可变的，在UPDATE时可能使行变得比原来更长，这就导致需要做额外的工作。如果一个行占用的空间增长，并且在页内没有更多的空间可以存储，MyISAM会将行拆成不同的片段存储；InnoDB则需要分裂页来使行可以放进页内。

下面这些情况使用VARCHAR是合适的：字符串的最大长度比平均长度大很多；列的更新很少，所以碎片不是问题；使用了像UTF-8这样复杂的字符集，每个字符都使用不同的字节数进行存储。

当存储CHAR值时，MySQL会删除所有的末尾空格。CHAR值会根据需要采用空格进行填充以方便比较。

CHAR适合存储很短的字符串，或者所有值都接近同一个长度，如密码的MD5值。对于经常变更的数据，CHAR也比VARCHAR更好，因为CHAR不容易产生碎片

##### varchar(5) && varchar(200)

> MySQL通常会分配固定大小的内存块来保存内部值，更长的列会消耗更多的内存。尤其是使用内存临时表进行排序或其他操作时会特别糟糕。在利用磁盘临时表进行排序时也同样糟糕。
>
> 所以最好的策略是只分配真正需要的空间

##### BLOB 和 TEXT

BLOB和TEXT都是为存储很大的数据而设计的数据类型，分别采用二进制和字符方式存储。

与其他类型不同，MySQL把每个BLOB和TEXT值当做一个独立的对象去处理。当BLOB和TEXT值太大时，InnoDB会使用专门的“外部”存储区域来进行存储，此时每个值在行内需要1~4个字节存储一个指针，然后在外部存储区域存储实际的值。

MySQL对BLOB和TEXT列进行排序与其他类型是不同的：它只对每个列的最前max_sort_length个字节而不是整个字符串做排序。同样的，MySQL也不能将BLOB或TEXT列全部长度的字符串进行索引。



##### 选择表示符（identifier）

整数类型通常是标识列的最佳选择，因为它们很快并且可以使用AUTO_INCREMENT。 如果可能，应该避免使用字符串类型作为标识列，因为它们很耗空间，并且比数字类型慢。 对于完全随机的字符串也需要多加注意，例如MD5(),SHA1()或者UUID()产生的字符串。这些函数生成的新值会任意分布在很大的空间内，这会导致INSERT以及一些SELECT语句变得很慢：

- 因为插入值会随机的写入到索引的不同位置，所以使得INSERT语句更慢。这会导致页分裂、磁盘随机访问。
- SELECT语句会变的更慢，因为逻辑上相邻的行会分布在磁盘和内存的不同地方。
- 随机值导致缓存对所有类型的查询语句效果都很差，因为会使得缓存赖以工作的局部性原理失效。





## Mysql存储引擎

> 存储引擎是Mysql数据库的大脑,负责处理优化过后的执行计划。根据不同的场景和需求选择合适的存储引擎，热门的存储引擎有InnoDB和Myisam，Innodb凭借支持事务和不俗的性能成为当下默认存储引擎。

### Innodb

- 是MySQL默认的事务型存储引擎，支持事务。
- 实现了四个标准的隔离级别，默认级别是可重复读(REPEATABLE READ)。在可重复读隔离级别下，通过多版本并发控制(MVCC)+ 间隙锁(Next-Key Locking)防止幻影读。
- MVCC机制可替代锁，从而提高并发度
- 主索引是聚簇索引，在索引中保存了数据，从而避免直接读取磁盘，因此对查询性能有很大的提升。
- 支持真正的在线热备份。
- 支持外键

> 数据库是文件系统,存储引擎是Innodb的表的文件结构：
>
> 其中`*.frm`文件为表结构文件，`*.ibd`文件存储着数据和索引信息。

![image-20230331163829421](mysql.assets/image-20230331163829421.png)



### Myisam

- 提供了大量的特性，包括压缩表、空间数据索引等。
- 不支持事务
- 不支持行级锁，只能对整张表加锁，读取时会对需要读到的所有表加共享锁，写入时则对表加排它锁
- 不支持数据恢复,存在数据丢失的风险
- 不支持外键

> 其中`*.frm`文件为表结构文件，`*.myd`文件存储着数据信息,`*.myi`文件存储着表索引信息,

![image-20230331165452460](mysql.assets/image-20230331165452460.png)





### 对比

|          | myisam | innodb       |
| -------- | ------ | ------------ |
| 事务     | 不支持 | 支持         |
| 并发     | 表级锁 | 行级锁、MVCC |
| 外键     | 不支持 | 支持         |
| 热备份   | 不支持 | 支持         |
| 崩溃恢复 | 不支持 | 支持         |
|          |        |              |





# Mysql原理



## Mysql系统框架

> Mysql是关系型数据库,用于存储数据,其包含许多模块用于处理连接、解析sql、优化sql、执行sql、存储数据等。

### 连接模块

> 市面上许多mysql连接驱动(客户端),程序通过这些连接驱动与mysql建立连接,从而进行通信。频繁的连接断开操作会使mysql不堪重负,因此产生了连接池模块,池中的连接可以重复利用。
>
> 不同的mysql性能不一样,所以合理配置连接池可以较大程度的发挥mysql性能,其中有两个参数：最大连接数、可接收数据的最大报文长度。

### mysql解析器

> 解析sql语句,如果sql不能解析,则会返回错误客户端。

### mysql优化器

> sql解析完成之后，mysql会根据自身的数据结构、索引情况来优化、重组我们的sql。

### 存储引擎

> 存储引擎有很多种,根据不同的场景旋转合适的存储引擎。
>
> 有读取快的、一致性高的、功能齐全的、量级轻的。
>
> 其中innodb  性能较好、可靠作为默认存储引擎

### 其他

> 缓存、安全、恢复、集群等模块



## mysql写入原理

> 客户端发送sql,mysql解析器对sql进行解析,解析成功后sql优化器对sql进行优化、重组,最后交给执行器，执行器调用存储引擎进行数据存储。
>
> 那么在存储引擎中做哪些呢？
>
> 拿到优化后的sql执行计划会传给存储引擎，存储引擎的执行器会按照对应命令运行。
>
> 内存操作相较于磁盘io拥有较高的性能,所以一切的逻辑处理和读写操作都在内存中完成,这块内存缓存区域称作为buffer pool。
>
> 为了支持数据回滚,在数据写入内存缓存区之前会将数据回滚操作放入undo log文件中。
>
> 回滚操作写入完成后，innodb会开启线程将内存中的数据读出来，并写入.ibd文件。
>
> 为了避免数据库宕机造成内存中数据丢失，innodb会将写入日志记录在Redolog buffer中，并借助特定的刷盘策略将写入日志持久化到redolog中。

- 回滚操作写入Undolog文件                        （undolog除了用于回滚记录，还用于mvcc替换不必要的锁）
- 逻辑操作和写入操作，写入内存缓存区      （内存操作快于磁盘io）
- 写入操作记录redologbuffer
- redologbuffer 刷盘，记录进redolog  （数据库重启后优先将redolog中未持久化到.ibd的数据初始化）
- 记录数据变更历史，记录进binlog
- commit提交事务，为redolog打上commit标志
- 缓存数据区数据持久化到磁盘XXX.ibd文件

#### redologbuffer刷盘策略

> 可使用如下命令查看设置当前数据据的刷盘策略

```sql
show VARIABLES like 'innodb_flush_log_at_trx_commit%';

set GLOBAL binnodb_flush_log_at_trx_commit = 1;
```

-- redolog 刷盘策略 
-- 1 ：每当事务提交后，会将’更新写入信息‘写入到redolog buffer中 随后添加到操作系统内存中并执行刷盘操作
-- 0 2 策略一致型不是很强  0 只会写入redologbuffer中，每隔一秒放入系统内存并进行刷盘操作
-- 2 只会写入系统内存  并隔一段时间刷盘



## mysql存储结构









## Mysql优化









# mysql安装



## docker安装Mysql

> 拉取镜像

```bash
➜  ~ docker search mysql
➜  ~ docker pull mysql
➜  ~ docker images
```

> 启动mysql

```bash
➜  ~ docker images
REPOSITORY   TAG       IMAGE ID       CREATED        SIZE
mysql        latest    fcf06b16ec81   37 hours ago   544MB
➜  ~ docker run -itd --name mysql-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql
➜  ~ docker ps -a
```

> 创建本地挂载目录

```bash
mkdir -pv  /Users/rolyfish/home/mysql/mysqltest/data  /Users/rolyfish/home/mysql/mysqltest/conf.d  /Users/rolyfish/home/mysql/mysqltest/logs
```

> 停止并删除之前的容器，重新启动容器

```bash
➜  mysqltest docker container stop mysql-test
➜  mysqltest docker container rm mysql-test
```

> 启动容器

```bash
docker run  -p 3317:3306 --name mysqltest2 --privileged=true \
  -v /Users/rolyfish/home/mysql/mysqltest/conf.d:/etc/mysql/conf.d \
  -v /Users/rolyfish/home/mysql/mysqltest/data:/var/lib/mysql \
  -v /Users/rolyfish/home/mysql/mysqltest/logs:/var/log/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -d mysql:latest
```



















