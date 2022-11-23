### PostgreSQl

> PostgreSQl安装和基本使用。

[postgresql下载](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)

#### Windows



##### 安装

[菜鸟教程](https://www.runoob.com/postgresql/windows-install-postgresql.html)

##### 基础语法

```sql
-- 创建用户 CREATE ROLE rolyfish WITH
  LOGIN
  SUPERUSER
  INHERIT
  CREATEDB
  CREATEROLE
  REPLICATION
  ENCRYPTED PASSWORD 'md59ad6569969286bd3c5fc37a91f56bd09';
-- 创建schema
CREATE SCHEMA IF NOT EXISTS springall
AUTHORIZATION rolyfish;
	
-- 创建数据库
-- Database: springall
-- DROP DATABASE IF EXISTS springall;
CREATE DATABASE springall
    WITH
    OWNER = rolyfish
    ENCODING = 'UTF8'
    LC_COLLATE = 'Chinese (Simplified)_China.936'
    LC_CTYPE = 'Chinese (Simplified)_China.936'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
-- 创建模式
CREATE SCHEMA IF NOT EXISTS springall
    AUTHORIZATION rolyfish;

COMMENT ON SCHEMA public
    IS 'springdemocolldb';

GRANT ALL ON SCHEMA public TO PUBLIC;

GRANT ALL ON SCHEMA public TO rolyfish;

-- 创建表
CREATE TABLE if not EXISTS "springall"."test" (
  "id" int4 NOT NULL,
  "value" varchar(255),
  PRIMARY KEY ("id")
);
COMMENT ON COLUMN "springall"."test"."id" IS '主键';
COMMENT ON COLUMN "springall"."test"."value" IS '属性';

-- 序列
-- 查询序列
SELECT * FROM	pg_class WHERE relkind = 'S';
-- 创建序列
CREATE SEQUENCE seq_test INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999 START WITH 1 NO CYCLE;
ALTER TABLE test ALTER COLUMN id  set DEFAULT nextval('seq_test'::regclass);

ALTER TABLE "springall"."test" 
  DROP CONSTRAINT "test_pkey",
  ADD COLUMN "otherid" int4 NOT NULL,
  ADD CONSTRAINT "test_pkey" PRIMARY KEY ("id", "otherid");
	COMMENT ON COLUMN "springall"."test"."otherid" IS '另一个主键';

create SEQUENCE seq_test_otherid increment by 1 MINVALUE 1 MAXVALUE 9999999 start WITH 5 no CYCLE;
ALTER TABLE springall.test ALTER COLUMN otherid set default nextval('seq_test_otherid'::regclass);

-- INSERT 语句
insert INTO springall.test (value) VALUES('1'),('2');
```

##### 图形化工具

> 使用navicatPremiums。

[navicatPremiums](https://www.ghxi.com/navicat16.html)

解压即用