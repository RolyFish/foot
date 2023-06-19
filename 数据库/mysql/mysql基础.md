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
   docker run -d --restart=always --name mysql \
   -v //d/dockerspace/volume/mysql/mysql-data:/var/lib/mysql \
   -v //d/dockerspace/volume/mysql/mysql-conf:/etc/mysql \
   -v //d/dockerspace/volume/mysql/mysql-log:/var/log/mysql \
   -p 3306:3306 \
   -e TZ=Asia/Shanghai \
   -e MYSQL_ROOT_PASSWORD=123456 \
   -d \
   mysql:8.0.33 
   
   
   
   
   ```















































