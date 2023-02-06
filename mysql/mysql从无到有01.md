Mysql从0到0.9

> [上一篇、Mysql基础]()||[下一篇]()

#### 关系型数据库

以关系模型来组织数据的数据库。

行+列 ==>  表 *n  ==> 数据库

简单理解：表就是二维表格

市面产品代表：Mysql、Oracle、PostgreSQL

图形化产品：Sqlyog、Navicat

#### 数据类型

##### 整数

| 类型         | 空间大小 | 对比java数据类型 |
| ------------ | -------- | :--------------: |
| tinyint      | 1byte    |       byte       |
| smallint     | 2byte    |      short       |
| mediumint    | 3byte    |       特殊       |
| int\|integer | 4byte    |       int        |
| bigint       | 8byte    |       long       |

##### 浮点数

| 类型    | 空间大小 | 对比java数据类型         |
| ------- | :------: | ------------------------ |
| float   |  4byte   | float单精度              |
| double  |  8byte   | double双精度             |
| decimal |    -     | 以字符串的形式保存浮点数 |

##### 时间类型

date(yyyy-mm-dd)  、time(hh:mm:ss)  、datetime(yyyy-mm-dd hh:mm:ss)

year  timestamp(时间戳1970 1.1 至今毫秒值)

##### 字符串类型

char、varchar

tinytext（小文本）、text（大文本）、mediumtext（中等文本）



#### 字段属性（列属性）

![image-20220305163919165](mysql从无到有.assets/image-20220305163919165.png)

> unsigned：（无符号整数，有无符号会影响整型数值范围）
>

> zerofill： 不足的位数使用0填充  1->00000001+
>
> age int(10) 不是说该字段范围有10位，表示长度为10，不足0填充

> autoincrement: 自增 
>

> not null： 非空
>

> default： 设置默认值     age int(10) default 10

> comment：描述     age int(10) default 10  comment '年龄'

#### 数据库相关操作

```bash
##创建数据库
CREATE DATABASE  IF NOT EXISTS reLearnMysql
##使用(切换)数据库
USE reLearnMysql
##删除数据库
DROP DATABASE IF EXISTS reLearnMysql
##查看所有数据库
SHOW DATABASES
```



#### 操作数据库表

```bash
##创建数据库表
CREATE TABLE IF NOT EXISTS `student` (
`id` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',
`stuno` INT(10) NOT NULL COMMENT '学号',
`stuname` VARCHAR(10) DEFAULT NULL COMMENT '姓名',
`birthday` DATETIME  COMMENT '生日',
`delete` INT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除字段',
`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`modify_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间'
##PRIMARY KEY (`id`)主键、外键都可以在此约束
)ENGINE=INNODB CHARSET=utf8
##查看当前数据库表
show tables
##查看表创建语句
show create table `student`
##查看表结构
DESC `student`
##删除表
drop table if exists tablename
```

> 对于特殊的单词比如`name`建议使用  ``符号包含，防止和 mysql内部字段冲突
>
> 声明数据库引擎及字符集，防止脚本在他人电脑运行不兼容

#### myisam  及 innodb

|            | myisam                     | innodb                           |
| ---------- | -------------------------- | -------------------------------- |
| 事务支持   | n                          | y                                |
| 数据锁支持 | 锁表  不够细粒，并发能力差 | 锁行  细粒度高，并发能力相对较好 |
| 外键       | n                          | y                                |
| 全文索引   | y                          | n                                |
| 表空间大小 | 小                         | 大  2g                           |

> `mysql5.6`之后同样也支持全文索引，总结：使用innodb不用myisam

##### 以存储形式角度对比

> `student`表单使用的`innodb`     `teacher`表单使用的`myisam`

文件:

![image-20220305172127984](mysql从无到有.assets/image-20220305172127984.png)

`innodb`：

​	frm:表结构文件。desc  tablename

​	ibd:  数据文件，表记录都存于此文件（创建之初就很大）

`myisam`:

​	frm:表结构文件。desc  tablename

​	myd: myisamdata   数据文件，表记录都存于此文件（很小）

​	myi： myisamindex，索引信息

#### 操作表字段（alter）

```sql
-- 修改表秒速
alter table user comment '用户表';
-- 修改字段秒速
alter table user modify column version varchar(10) comment '乐观锁';
-- 修改列名  
alter table user change `delete` `is_delete` int4 comment '逻辑删除';

alter table tablename rename as tablenewname  --修改tablename
alter table teacher add age int(11) --添加字段
--修改字段  属性给全
alter table teache modify age varchar(11)--  修改约束
alter table teacher change age age1 int（2）--修改字段名 并添加字段属性
--删除字段
alter table teacher drop age
```

#### 外键(FOREIGN KEY)(FK)

> 说明：
>
> 开发中避免在数据库层面做逻辑相关业务，包括外键级联，逻辑删除，创建时间等等。应在代码层面解决。

> 原因：外键级联会使得数据库读写性能差、删除主表存在附表约束

> 但是外键也有好处，外键保证了数据一致性。

```sql
CREATE TABLE IF NOT EXISTS `grade`(
`id` INT(5) ZEROFILL NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '年级id',
`gradename` VARCHAR(20) NOT NULL COMMENT '年级'
)ENGINE=INNODB CHARSET=utf8

CREATE TABLE IF NOT EXISTS `teacher` (
`id` INT(5) ZEROFILL NOT NULL PRIMARY KEY  COMMENT '工号',
`name` VARCHAR(20) NOT NULL COMMENT '姓名',
`gradeid` INT(5) ZEROFILL NOT NULL COMMENT '年级id',
KEY `FK_gradeid` (`gradeid`),
CONSTRAINT `FK_gradeid` FOREIGN KEY (`gradeid`) REFERENCES `grade` (`id`)
)ENGINE=INNODB CHARSET=utf8

或
ALTER TABLE teacher ADD CONSTRAINT `FK_gradeid` FOREIGN KEY(`gradeid`) REFERENCES `grade`(`id`)
```

执行插入语句

```sql
INSERT INTO `grade`( `id`,`gradename`) VALUES(1,'一年级'),(2,'二年级')
INSERT INTO `teacher`( `id`,`name`,`gradeid`) VALUES(001,'张老师',1),(002,'王老师',2)
INSERT INTO `teacher`( `id`,`name`) VALUES(003,'马老师')
```

> 思考：如果说删除附表（grade）那么主表的gradeid还有意义么？mysql有没有什么机制防止这样的情况发生

测试

```sql
删除附表数据
DELETE FROM `grade` WHERE `id` = 1
```

结果：报错。附表与主表绑定，删除附表数据会影响数据一致性。但是如果附表数据并没有和主表绑定删除还是没有影响的。

```sql
插入第三条数据，此数据没有和主表绑定
INSERT INTO `grade`( `id`,`gradename`) VALUES(3,'三年级')
可以删除
DELETE FROM `grade` WHERE `id` = 3
```



#### DML

Data  Manipulation Language（数据库操作语言）

```sql
insert into tablename(字段名1.。。。) values('值1'。。。。)--一一对应
##批量插入
insert into tablename(字段名1.。。。) values('值1'。。。。)，('值1'。。。。)，--一一对应
UPDATE  tablename SET 字段名1 = '2021-01-02 11:12:19' WHERE id = 2
delete from tablename where id = 1 
##批量删除
delete from tablename
## 属于ddl  数据定义语言
truncate tablename 、truncate table tablename 
```

> drop删除表数据及结构、delete，truncate删除数据保留结构

delete  truncate区别

- delete逐行删除，效率慢、可回滚。truncate立刻回退状态，立刻生效，效率快，不可回滚
- 对于自增列来说delete不会将自增列回退到1（但是重启mysql服务会查询最大自增列max，回退到1.原因就是innodb对于自增列大部分是主键索引会存放在内存中，重启服务内存会清空）。truncate会回退到1
- 对于有被外键约束的表不能使用truncate

总结：想要快速清空一张表，不要求回滚事务使用truncate，但是做好备份。想要删除部分数据，并且要求回滚且支持事务使用delete。



#### 常用操作符和关键字

基本： =    >  <    >=  <=  !=   <>

between...and    and   or   not

```sql
SELECT * FROM teacher  WHERE id  = 1
SELECT * FROM teacher  WHERE id  != 1
SELECT * FROM teacher  WHERE id  <> 1
SELECT * FROM teacher  WHERE NOT id  = 1
SELECT * FROM teacher  WHERE  id  BETWEEN 0 AND 3
SELECT * FROM teacher  WHERE  id  BETWEEN 0 AND 3 AND NAME LIKE '_老师'
SELECT * FROM teacher  WHERE  id  =1 OR NAME NOT LIKE '王%'
```
