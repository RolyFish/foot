Mysql从0到0.9

本章总结DML数据库操作语言，以及数DQL据库查询语言的基本使用

> [上一篇、Mysql基础]()||[下一篇]()

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



#### DQL  data query language

##### 基本查询

给字段取别名 ` t.id  AS teacherid ` 是可以省略`as`关键字的

```sql
SELECT t.id  teacherid,  t.name tname, g.id gradeid, g.gradename  gradename
	FROM `teacher` t , `grade` g 
WHERE g.id = t.gradeid
=======聚合函数===============
SELECT `id`, CONCAT('name:',NAME) FROM teacher
SELECT CURRENT_DATE()
SELECT CURRENT_TIME()
SELECT CURRENT_TIMESTAMP()
=======去重，多用于子查询=======
SELECT DISTINCT `name` FROM teacher
=======模糊查询=================
is null  /like / between / in
SELECT * FROM `teacher` WHERE `name` IS NULL
SELECT * FROM `teacher` WHERE `id` BETWEEN 0 AND 3
SELECT * FROM `teacher` WHERE `gradeid` IN (1,2)
SELECT * FROM `teacher` WHERE `gradeid` IN (SELECT DISTINCT `id` FROM `grade`)
SELECT * FROM `teacher` WHERE `name` LIKE '%张%'
SELECT * FROM `teacher` WHERE `name` LIKE '_老师'
```

##### 嵌套查询

> 创建新表  `department`（部门）   `employee`（员工）

```sql

CREATE TABLE IF NOT EXISTS `department`(

	`deptId` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '部门id',
	`deptName` VARCHAR(10) NOT NULL COMMENT '部门名称',
	`delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除字段',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
	`modify_time` DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间'
)ENGINE=INNODB CHARSET=utf8

CREATE TABLE IF NOT EXISTS `employee`(
	`id` INT(10) NOT NULL PRIMARY KEY COMMENT '员工id',
	`empName` VARCHAR(10) NOT NULL COMMENT '员工名称',
	`deptId` INT(10) COMMENT '部门id',
	`salary` INT(10) DEFAULT 1000 COMMENT '员工工资',
	`delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除字段',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
	`modify_time` DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间'
)ENGINE=INNODB CHARSET=utf8

INSERT INTO `department`(`deptName`) VALUES('部门一'),('部门二'),('部门三'),('部门四'),('部门五'),('部门六')
NSERT INTO `employee`(`id`,`empName`,`deptId`,`salary`) 
VALUES
(1001,'员工0',1,3000),
(1002,'员工1',1,3000),
(1003,'员工2',2,4000),
(1004,'员工3',3,5000),
(1005,'员工4',4,6000),
(1006,'员工5',5,7000),
(1007,'员工6',6,4000),
(1008,'员工7',1,8000),
(1009,'员工8',2,7000),
(1010,'员工9',3,6000),
(1011,'员工10',4,5000),
(1012,'员工11',5,1000),
(1013,'员工12',6,2000)
```

> 嵌套查询，将上一个表的查询结果作为下一个表的查询条件

例子：查询在部门一、部门二、和部门三的所有员工

员工表`employee`中没有部门名称只有部门id，所有得先查询部门id

```sql
SELECT * FROM `employee` 
	WHERE `deptid` IN(
	SELECT deptId FROM `department` 
		WHERE `deptName` IN 
		('部门一','部门二','部门三')
	)
```

>  当然实际开发中远远没有如此简单，可能配合聚合函数、多级嵌套以及联表查询
>
> 这里其实有缺点，会发现得不到其他表的字段（部门名称），得配合联表查询

##### 联表查询

接上一个例子，这样皆可以得到`deptname`

```sql
SELECT e.*, d.`deptname` FROM `employee` e, `department` d
	WHERE 
	e.`deptid` = d.`id`
	AND
	e.`deptid` IN(
	SELECT deptId FROM `department` 
		WHERE `deptName` IN 
		('部门一','部门二','部门三')
	)
```

> 下面四种联表查询，效果、结果完全一样。可互换

```sql
SELECT * FROM `employee`,`department`
SELECT * FROM `employee`   JOIN `department`
SELECT * FROM `employee`   CROSS JOIN `department`
SELECT * FROM `employee`  INNER JOIN `department`
```

> inner join  需搭配on用于指定联表条件，即表之间怎么被联合

```sql
## 前表的deptid和后表的id相同时，两条记录联合
SELECT * FROM `employee` t1 INNER JOIN `department` t2
ON t1.`deptid` = t2.`deptId`
```

> 左连接  右连接

`LEFT JOIN` 时，右边表中不满足 `ON` 或 `USING` 指定的条件时，会在结果中以 `NULL` 呈现。最终我们可以用`where`对结果过滤。

```sql
SELECT * FROM `employee` t1   LEFT JOIN `department` t2
ON t1.`deptid` = t2.`deptId`
## deptId提到第一列且只出现一次
SELECT * FROM `employee` t1   LEFT JOIN `department` t2
USING(`deptId`)
##过滤null值
SELECT * FROM `employee` t1   LEFT JOIN `department` t2
ON t1.`deptid` = t2.`deptId`
WHERE t2.`deptId` IS NOT NULL
```

`RIGHT JOIN`和`LEFT JOIN`类似，左边null填充。

`NATURAL [LEFT] JOIN` 与 `INNER JOIN` 和 `LEFT JOIN` 配合使用了 `USING` 指定表中所有列的情况等效

```sql
##这里创建时间不一样所以不显示
SELECT * FROM `employee`  NATURAL  JOIN `department` 
```

`FUll OUTER JOIN``MYSQL`不支持，只有`ORCAL`支持。

```sql
##左表独有的（右表null填充）left join   右表独有的（左表null填充）right join  左右都有的 inner join  
SELECT * FROM `employee`  t1 LEFT JOIN `department` t2 ON t1.deptid=t2.deptid
 UNION 
SELECT * FROM `employee`  t1 RIGHT JOIN `department` t2 ON t1.deptid=t2.deptid
```

总结：

```sql
INNER JOIN 
JOIN
CROSS JOIN
=====
LEFT JOIN
RIGHT JOIN
NATURAL JOIN
==
FUll OUTER JOIN
```

`on``USing`区别：

on展示所有列。USING合并对比列再展示。

通用sql模板

```sql
select [all | distinct(去重）]
	{* | table.* |[table.fieldl[as field1][,table.field2[as field2]][.......]]}
from table1....
	left/right/inner join tablename   on--判断
	where--条件
	group by --分组
 	having--过滤分组
    order by--排序
    limit     --分页  
```

> 说明：`where`和`having`都是用于结果筛选。
>
> `where`直接从表中进行筛选，`having`是从查询1出来的结果进行过滤。
>
> 具体表现：`where`不能使用别名，`having`可以。

#### 练习

```bash
-- 1．列出至少有一个员工的所有部门。
SELECT * FROM `department` dept 
INNER JOIN 
(SELECT `deptId` dpid , COUNT(`deptId`) `empnum` FROM `employee` 
GROUP BY `dpid`) t
ON dept.`deptid` = t.`dpid`
-- 2．列出薪金比"员工一"多的所有员工。
SELECT * FROM `employee`
WHERE	`salary` > 
(SELECT DISTINCT `salary` FROM `employee` 
WHERE `empName` = '员工1')

-- 8．列出在部门 "部门一" 工作的员工的姓名
SELECT * FROM `employee`
WHERE	`deptid` =
(SELECT deptid FROM `department` WHERE `deptname` = '部门一')
-- 9．列出薪金高于公司平均薪金的所有员工。
SELECT * FROM `employee` 
WHERE salary >
(SELECT AVG(salary) FROM `employee`)
-- 13．列出在每个部门工作的员工数量、平均工资。
SELECT * FROM `department` t1 
INNER JOIN(
SELECT `deptId`, COUNT(`id`) `empnum` FROM `employee` GROUP BY `deptId`
)t2 
USING(deptId)
INNER JOIN(
SELECT `deptId`, AVG(`salary`) `avgsalary` FROM `employee` GROUP BY `deptId`
)t3
USING(deptId)

-- 16．列出各部门的最低工资。
SELECT * FROM `department` t1 
INNER JOIN(
SELECT `deptId`, MIN(`salary`) `minsalary` FROM `employee` GROUP BY `deptId`
)t2 
USING(deptId)
```