### XXJOB

[xxl-job官方文档](https://www.xuxueli.com/xxl-job/)



#### 简介

> XXL-JOB是一个分布式任务调度平台，开发迅速、学习简单、轻量级、易扩展、开箱即用。

#### 官方Demo

[githup](https://github.com/xuxueli/xxl-job)

[gitee](https://gitee.com/xuxueli0323/xxl-job)

![image-20230605173851828](.\spring-xxjob.assets\image-20230605173851828.png)

#### 环境搭建

1. 初始化调度数据库

   - sql脚本

     [官方sql地址](https://github.com/xuxueli/xxl-job/blob/master/doc/db/tables_xxl_job.sql)

     就在doc `> `db目录下

   - docker 启动mysql

     ```shell
     docker run --name mysql-xxljob \
     -p 3316:3306 \
     -v /home/rolyfish/home/mysql/data:/var/lib/mysql \
     -v /home/rolyfish/home/mysql/conf:/etc/mysql/conf.d \
     -e MYSQL_ROOT_PASSWORD=123456 \
     -d mysql:8.0.33
     ```

   - 执行sql脚本

2. 启动xxl-job-admin

   > - 修改xxl-job-admin配置的数据源
   > - 启动  访问  `http:/localhost:8080/xxl-job-admin`默认用户名密码是`admin\123456`

3. 使用docker启动xxl-job-admin

   ```shell
   ## 拉取镜像
   $ docker pull xuxueli/xxl-job-admin:2.4.0
   
   # 启动 xxljob-admin 并加入 网路
   docker run  -p 8088:8080 \
   --name xxljob-admin-local \
   -e PARAMS='--spring.datasource.url=jdbc:mysql://192.168.111.128:3316/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true --spring.datasource.username=root --spring.datasource.password=123456' \
   -d \
   xuxueli/xxl-job-admin:2.4.0
   ```

4. nginx负载均衡(可选)

   > xxljob-admin支持集群部署, 可以使用nginx做负载均衡, 提升容灾和可用性。

   - 拉取nginx镜像

     ```shell
     docker pull nginx:latest
     ```

   - 拷贝默认文件

     ```shell
     docker cp nginx:/etc/nginx /home/rolyfish/home/nginx/nginx
     ## 拷贝日志
     docker cp nginx:/var/log/nginx /home/rolyfish/home/nginx/log
     ## 拷贝pid文件
     docker cp nginx:/var/run/nginx.pid /home/rolyfish/home/nginx/log/nginx.pid
     ## 拷贝静态资源问津
     docker cp nginx:/usr/share/nginx/html /home/rolyfish/home/nginx/html
     ```

   - 修改配置

     > 修改default.conf这个配置, 配置文件nginx.conf会包含 conf.d下的所有配置

     ```properties
     upstream xxljob.admin.com { 
           server  192.168.111.128:8088  max_fails=5 fail_timeout=10s weight=1; 
     } 
     server {
         listen       80;
         listen  [::]:80;
         server_name  localhost;
     
         location /xxl-job-admin {
         	proxy_pass  http://xxljob.admin.com/xxl-job-admin/;
         }
     
         error_page   500 502 503 504  /50x.html;
         location = /50x.html {
             root   /usr/share/nginx/html;
         }
     }
     ```

   - 启动nginx

     ```shell
     docker run --name nginx-xxljob-admin \
     -p 80:80 \
     -v /home/rolyfish/home/nginx/nginx/nginx.conf:/etc/nginx/nginx.conf \
     -v /home/rolyfish/home/nginx/html/:/usr/share/nginx/html/ \
     -v /home/rolyfish/home/nginx/log/:/var/log/nginx/ \
     -v /home/rolyfish/home/nginx/nginx/conf.d/:/etc/nginx/conf.d/ \
     --privileged=true \
     -d nginx:latest
     ```

     

#### demo

> 官方执行器例子

##### 部署步骤

1. 引入依赖

   ```xml
   <!-- https://mvnrepository.com/artifact/com.xuxueli/xxl-job-core -->
   <dependency>
       <groupId>com.xuxueli</groupId>
       <artifactId>xxl-job-core</artifactId>
       <version>2.4.0</version>
   </dependency>
   ```

2. 配置

   ```yaml
   # web port
   server.port=8081
   # no web
   #spring.main.web-environment=false
   # log config
   logging.config=classpath:logback.xml
   ### xxl-job admin address list, such as "http://address" or "http://address01,http://address02"
   #xxl.job.admin.addresses=http://127.0.0.1:8080/xxl-job-admin
   xxl.job.admin.addresses=http://192.168.111.128:8088/xxl-job-admin
   ### xxl-job, access token
   xxl.job.accessToken=default_token
   ### xxl-job executor appname
   xxl.job.executor.appname=xxl-job-executor-sample
   ### xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
   xxl.job.executor.address=
   ### xxl-job executor server-info
   xxl.job.executor.ip=
   xxl.job.executor.port=9999
   ### xxl-job executor log-path
   xxl.job.executor.logpath=/data/applogs/xxl-job/jobhandler
   ### xxl-job executor log-retention-days
   xxl.job.executor.logretentiondays=30
   ```

3. 注入配置类

   `XxlJobConfig.java`

4. 编写任务

   1. JavaBean模式

      > 继承 `IJobHandler`重写 execute方法即可

      ```java
      @Component
      @Slf4j
      public class XxlJobTaskDemo extends IJobHandler {
          @Override
          public void execute() throws Exception {
              log.info("hello xxlJob");
          }
      
          @Override
          public void init() throws Exception {
              super.init();
              log.info("hello xxlJob == init");
          }
      
          @Override
          public void destroy() throws Exception {
              super.destroy();
              log.info("hello xxlJob == destroy");
          }
      }
      ```

   2. 方法模式

      > 注解 + 方法模式

      ```java
      @Component
      @Slf4j
      public class XxlJobTaskMethod {
      
          @XxlJob(value = "helloXxlJob2", init = "init", destroy = "destroy")
          public void execute(String... params) {
              String jobParam = XxlJobHelper.getJobParam();
              log.info("jobParam:{}",  jobParam);
              log.info("hello xxlJob");
              // 默认成功 可显示设置 结果
              XxlJobHelper.handleSuccess("执行成功");
          }
      
          public void init() {
              log.info("hello xxlJob == init");
          }
      
          public void destroy() {
              log.info("hello xxlJob == destroy");
          }
      }
      ```

5. 新建任务

6. 新建执行器

   > 选择自动注册
   >
   > 执行器需要配置执行器名称

   ![image-20230609143413646](D:\Desktop\myself\foot\base\foot\pdai\spring\assets\image-20230609143413646.png)

   

   

   















