> mp
>
> [mp官网]([https://baomidou.com](https://baomidou.com/))
>
> [prev]()            [next]()



#### mp简介

`Mybatis`的增强工具，在 `MyBatis` 的基础上只做增强不做改变，为简化开发、提高效率而生。

#### 特点



#### 使用

##### 数据库脚本

```bash
SHOW CREATE TABLE `user`
CREATE TABLE `user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(30) DEFAULT NULL COMMENT '姓名',
  `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
  `deleted` INT(11) DEFAULT NULL COMMENT '逻辑删除',
  `version` INT(11) DEFAULT NULL COMMENT '乐观锁',
  `create_time` DATETIME DEFAULT NULL COMMENT '插入时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8
```

##### 依赖

```xml
<dependency>web相关..</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.37</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.0.5</version>
</dependency>

<dependency>
    <groupId>org.apache.velocity</groupId>
    <artifactId>velocity-engine-core</artifactId>
    <version>2.0</version>
</dependency>
<!--mp可自动生成swagger api文档-->
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-core</artifactId>
    <version>1.6.0</version>
</dependency>
```

##### 配置

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/mybatis_plus?useUnicode=true&charactEncoding=utf8&useSSL=true&serverTimezone=GMT%2B8
#mybatisplus 日志配置
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      #      logic-delete-field: flag # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
#全局配置默认支持驼峰命名规则
#数据库配置默认配置包括，主键自增生成规则、数据库默认other（可设固定）、表明前缀
```

##### 创建实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    //主键自增
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    //private long age;
    private String email;
    //插入填充
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //    //更新填充
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    @TableField(fill = FieldFill.INSERT)
    private int version;
    //逻辑删除字段
    @TableLogic
    private Integer deleted;
}
```