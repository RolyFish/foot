Mysql从0到0.9

> [上一篇、Mysql基础]()||[下一篇]()

> [菜鸟教程](https://www.runoob.com/mysql/mysql-functions.html)

#### 函数

```sql
数学运算
select abs(-8) --绝对值
select ceiling(9.4)  --向上取整
select floor(9.4)--向下取整
select rand()--0-1随机数
select sign()--返回符号  0-》0  -5-》-1  10-》1
SELECT ROUND(1.6)--距离1.6最近的整数 2   1.3->1
SELECT TRUNCATE(3.1415926,3)--保留3位小数
字符串函数
SELECT CHAR_LENGTH('dasdadad')--返回字符串长度
SELECT CONCAT('a','b','c')--拼接字符串,在后续的sql拼接可放sql注入
SELECT CONCAT_WS(',','a','b','c')--拼接字符串以,分隔
SELECT INSERT('str1',a,b,'str2')--str1的a位置开始，用str2替换str1的b个
SELECT LOCATE('chuan','yuyanchuang')--str1 在str2出现的起始位置
SELECT LOWER('AAA')--转小写
SELECT UPPER('aaa')--转大写
SELECT REPLACE('111222333','11','xx')--str from_str to_str
SELECT SUBSTR('abcd',1,2)--截取第一个位置开时  两个字符
时间
::年月日
SELECT CURRENT_DATE()  -- --当前日期
select curdate()--当前日期
::时分秒
SELECT CURRENT_TIME()  
SELECT CURTIME()
::年月日时分秒
select now()--当前时间
select localtime()--本地时间
select sysdate() --系统时间
::
select year(now())--
select month(now())--
select day(now())--
select dayname(now())--周几
select dayofmouth【week、year】(now())--本月第几天
select hour(now())--
select second(now())--

##其它函数
IF(expr,v1,v2)--当expr成立返回v1  否则返回v2
md5('131')
```

#### 聚合函数

常常和group by结合使用

```sql
count()
sum()
avg()
max()
min()
```

