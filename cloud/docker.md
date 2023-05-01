# docker



## 初识docker



### docker是什么

微服务虽然具备各种各样的优势，但服务的拆分通用给部署带来了很大的麻烦。

- 分布式系统中，依赖的组件非常多，不同组件之间部署时往往会产生一些冲突。
- 在数百上千台服务中重复部署，环境不一定一致，会遇到各种问题





### 什么是镜像

> 镜像是将应用程序及其所需要的系统函数库、环境、配置、依赖打包而成。



### 安装docker

[docker官方安装docker教程](https://docs.docker.com/desktop/install/linux-install/)

> 很晦气,官方步骤安装不下来,[外门教程](https://grigorkh.medium.com/how-to-install-docker-on-ubuntu-20-04-f1b99845959e)

安装成功后注册docker服务：

```shell
systemctl enable docker
```

这样就可以通过如下命令启动和停止docker：

```shell
systemctl start docker 
systemctl restart docker 
systemctl stop docker 
```



#### 配置阿里云镜像

[阿里云镜像配置手册](https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors)

```shell
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://3rfiuyyp.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

![image-20230501022524952](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501022524952.png)





## docker基本操作



### 镜像操作

镜像的名称组成：

- repository    仓库 例如mysql
- tag    版本 例如5.7

格式是[repository]:[tag]例如mysql:5.7 ,这里的mysql就是repository，5.7就是tag，合一起就是镜像名称，代表5.7版本的MySQL镜像。



#### 镜像命令

> 可以通过`docker image -help`查看

![image-20230501023938830](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501023938830.png)

![image-20210731155649535](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20210731155649535.png)

> 镜像如何共享？
>
> - push镜像到镜像服务器,别人拉取
> - docker image save -o mysql.tar.gz mysql:latest   配合 docker image load -i mysql.tar.gz

##### 拉取镜像

> 需要拉取指定版本镜像,则去[dockerhup](https://hub.docker.com/)查看。不指定版本则默认。

1. 查看tag

   ![image-20230501030143822](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501030143822.png)

2. 拉取镜像

   ```shell
   docker search nginx
   docker pull nginx
   ```

   ![image-20230501030437965](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501030437965.png)

3. 打包镜像

   ```shell
   docker save -o path [imagename]
   # 或者
   docker image save -o path [imagename]
   ```

   ```shell
   docker save -o nginx.tar.gz nginx:latest
   find nginx.tar.gz 
   nginx.tar.gz
   ```

4. 加载镜像

   ```shell
   docker load -i [filename]
   # 或者
   docker image load -i [filename]
   ```

   ```shell
   # 删除本地镜像
   docker image rm nginx:latest
   # 加载镜像
   docker load -i nginx.tar.gz
   ```

### 容器命令

> 可以通过`docker --help`和`docker container --help`查看帮助文档。
>
> `docker --help`和`docker container --help`有很多命令都是等价的

![image-20210731161950495](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20210731161950495.png)

容器保护三个状态：

- 运行：进程正常运行
- 暂停：进程暂停，CPU不再运行，并不释放内存
- 停止：进程终止，回收进程占用的内存、CPU等资源



其中：

- docker run：创建并运行一个容器，处于运行状态
- docker pause：让一个运行的容器暂停
- docker unpause：让一个容器从暂停状态恢复运行
- docker stop：停止一个运行的容器
- docker start：让一个停止的容器再次运行

- docker rm：删除一个容器



##### 创建并运行容器

> 创建容器可以使用如下命令：

```shell
Aliases:
  docker container create, docker create
# 或者
Aliases:
  docker container run, docker run
```

这两组命令的参数是一样的

| 参数              | 说明                                               |                  |
| ----------------- | -------------------------------------------------- | ---------------- |
| -e                | Set environment variables                          | 设置环境变量     |
| -h                | Container host name                                | 主机名称         |
| -p                | Publish a container's port(s) to the host          | 映射宿主机端口   |
| -u                | Username or UID                                    | 用户名称         |
| -v                | Bind mount a volume                                | 绑定数据卷       |
| -d                | Run container in background and print container ID | 后台运行         |
| --privileged=true |                                                    | 容器拥有root权限 |
|                   |                                                    |                  |

> 从nginx镜像创建并运行容器。
>
> 将nginx的80端口和宿主机的8080端口绑定,访问宿主机8080端口即可以访问nginx的80端口。

```shell
docker run --name nginx -p 8080:80 \
    --privileged=true -d nginx:latest
```

![image-20230501033422445](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501033422445.png)

##### 进入nginx并操作

> `docker exec`帮助文档：

```shell
Usage:  docker exec [OPTIONS] CONTAINER COMMAND [ARG...]
# docker container exec, docker exe等价
Aliases:
  docker container exec, docker exec
Options:
  -d, --detach               Detached mode: run command in the background
      --detach-keys string   Override the key sequence for detaching a container
  -e, --env list             Set environment variables
      --env-file list        Read in a file of environment variables
  -i, --interactive          Keep STDIN open even if not attached
      --privileged           Give extended privileges to the command
  -t, --tty                  Allocate a pseudo-TTY
  -u, --user string          Username or UID (format: "<name|uid>[:<group|gid>]")
  -w, --workdir string       Working directory inside the container
```

1. 进入创建的nginx容器：

   ```shell
   docker container exec -it nginx /bin/bash
   ```

   命令解读：

   - docker exec ：进入容器内部，执行一个命令

   - -it : 给当前进入的容器创建一个标准输入、输出终端，允许我们与容器交互

   - nginx ：要进入的容器的名称

   - bash：进入容器后执行的命令，bash是一个linux终端交互命令

2. 修该nginx的首页文件

   > 静态文件存放路径可以去nginx镜像出处查看,也就是`dockerhup`。

   ```shell
   ## 进入nginx静态资源目录
   cd /usr/share/nginx/html
   ## nginx是一个阉割版的linux,没有vi命令,使用sed命令修改index.html
   sed -i -e 's/Welcome to nginx/666666/g' index.html
   ```

   ![image-20230501035536737](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501035536737.png)

##### 小结

docker run命令的常见参数有哪些？

- --name：指定容器名称
- -p：指定端口映射
- -d：让容器后台运行

查看容器日志的命令：

- docker logs
- 添加 -f 参数可以持续查看日志

查看容器状态：

- docker ps
- docker ps -a 查看所有容器，包括已经停止的

### 挂载数据卷

> 在docker容器中修改文件不好管理,当docker容器删除修改的数据也跟着丢失,造成docker容器与数据的紧耦合不利于管理(docker容器升级)。
>
> docker数据卷(volume),是一个虚拟目录,指向宿主机的一个真实目录。

docker volume操作：

![image-20230501132212001](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501132212001.png)

#### 将nginx容器的html挂载到数据卷

> 将`html`数据卷挂载到 nginx容器的/usr/share/nginx/html目录。

```shell
docker run --name nginx -p 8080:80 \
	-v html:/usr/share/nginx/html \
    --privileged=true -d nginx:latest
```

![image-20230501133529789](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501133529789.png)

![image-20230501133539871](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501133539871.png)



> 修改本地挂载的数据卷,即可更新docker内的文件。

```shell
sed -i  -e 's/Welcome to nginx!/666666/g' index.html
```

![image-20230501133945090](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501133945090.png)



#### 挂载宿主机目录

> 数据卷默认挂载目录由docker决定, -v 后面跟数据卷名称,数据卷不存在docker会自动帮创建。
>
> 也可以指定宿主机目录,将宿主机目录和容器内目录绑定。

##### 创建mysql容器并挂载宿主机目录

[dockerhup-mysql](https://hub.docker.com/_/mysql)选择合适版本,这里选择latest

![image-20230501143251227](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501143251227.png)

> 挂载mysql的date和conf

- 宿主机创建本地目录

  ```shell
  mkdir -p mysql/conf
  mkdir -p mysql/data
  pwd
  /home/tmp/docker
  ```

- 挂载配置文件

  > [查看官方文档说明](https://hub.docker.com/_/mysql)
  >
  > MySQL的默认配置可以在`/etc/mysql/my.cnf`中找到，它可以在`!includedir`中找到其他目录，如`/etc/mysql/conf.d`或`/etc/mysql/mysql.conf.d`。
  >
  > 我们只需要配置`/etc/mysql/conf.d`或`/etc/mysql/mysql.conf.d`即可

  创建在宿主机conf下创建mysql.conf

  ```shell
  vim mysql/conf/mysql.conf
  ```

  ```properties
  [mysqld]
  skip-name-resolve
  character_set_server=utf8
  datadir=/var/lib/mysql
  server-id=1000
  ```

- 启动容器

  ```shell
  docker run \
  --name mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -v ./mysql/conf:/etc/mysql/conf.d \
  -v ./mysql/data:/var/lib/mysql \
  -d \
  mysql:latest
  ```

  ![image-20230501145141549](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501145141549.png)





### dockerfile

常见的镜像在DockerHub就能找到，但是我们自己写的项目就必须自己构建镜像了。

而要自定义镜像，就必须先了解镜像的结构才行。

#### 镜像结构

镜像是将应用程序及其需要的系统函数库、环境、配置、依赖打包而成。

我们以MySQL为例，来看看镜像的组成结构：

![image-20210731175806273](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20210731175806273.png)

简单来说，镜像就是在系统函数库、运行环境基础上，添加应用程序文件、配置文件、依赖文件等组合，然后编写好启动脚本打包在一起形成的文件。

我们要构建镜像，其实就是实现上述打包的过程。



#### dockerfile语法

**Dockerfile**就是一个文本文件，其中包含一个个的**指令(Instruction)**，用指令来说明要执行什么操作来构建镜像。每一个指令都会形成一层Layer。

[Dockerfile reference](https://docs.docker.com/engine/reference/builder/)

![image-20210731180321133](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20210731180321133.png)



#### 构建java项目

1. 准备依赖

   > 准备 `java.jar`包,合适的`jdk`,以及一个`docker file`
   >
   > 注意：根据自己电脑架构选择jdk,我是[arm64](https://www.oracle.com/hk/java/technologies/downloads/#license-lightbox)。

   ![image-20230501172538606](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501172538606.png)

2. dockerfile内容

   ```shell
   # 指定基础镜像
   FROM ubuntu:latest
   # 配置环境变量，JDK的安装目录
   ENV JAVA_DIR=/usr/local
   
   # 拷贝jdk和java项目的包至容器指定目录
   COPY ./jdk8.tar.gz $JAVA_DIR/
   COPY ./docker-demo.jar /tmp/app.jar
   
   # 安装JDK
   RUN cd $JAVA_DIR \
    && tar -xf ./jdk8.tar.gz \
    && mv ./jdk1.8.0_144 ./java8
   
   # 配置环境变量
   ENV JAVA_HOME=$JAVA_DIR/java8
   ENV PATH=$PATH:$JAVA_HOME/bin
   
   # 暴露端口
   EXPOSE 8090
   # 入口，java项目的启动命令
   ENTRYPOINT java -jar /tmp/app.jar
   ```

3. 指定dockerfile构建镜像

   ```shell
   docker build -t javaweb:1.0 -f ./Dockerfile .
   ```

   ![image-20230501181929901](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501181929901.png)

4. 运行

   ```shell
   docker run \
   --name javaweb \
   -p 8090:8090 \
   -d \
   --privileged=true \
   javaweb:1.0
   ```

   ![image-20230501181957919](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501181957919.png)

5. 访问

   [地址](http://10.211.55.4:8090/hello/count)

   ![image-20230501182043478](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230501182043478.png)



#### 基于java8构建java项目

虽然我们可以基于Ubuntu基础镜像，添加任意自己需要的安装包，构建镜像，但是却比较麻烦。所以大多数情况下，我们都可以在一些安装了部分软件的基础镜像上做改造。

例如，构建java项目的镜像，可以在已经准备了JDK的基础镜像基础上构建。



需求：基于java:8-alpine镜像，将一个Java项目构建为镜像

实现思路如下：

- ① 新建一个空的目录，然后在目录中新建一个文件，命名为Dockerfile

- ② 拷贝课前资料提供的docker-demo.jar到这个目录中

- ③ 编写Dockerfile文件：

  - a ）基于java:8-alpine作为基础镜像

  - b ）将app.jar拷贝到镜像中

  - c ）暴露端口

  - d ）编写入口ENTRYPOINT

    内容如下：

    ```properties
    FROM java:8-alpine
    COPY ./docker-demo.jar /tmp/app.jar
    EXPOSE 8090
    ENTRYPOINT java -jar /tmp/app.jar
    ```

- ④ 使用docker build命令构建镜像

- ⑤ 使用docker run创建容器并运行

#### 小结

1. Dockerfile的本质是一个文件，通过指令描述镜像的构建过程

2. Dockerfile的第一行必须是FROM，从一个基础镜像来构建

3. 基础镜像可以是基本操作系统，如Ubuntu。也可以是其他人制作好的镜像，例如：java:8-alpine





## docker-compose

Compose是一个用于定义和运行多容器Docker应用程序的工具。 Compose可以基于YAML文件来配置应用程序的服务。 然后，使用一个命令，根据配置创建并启动所有服务。

### 基础

Compose文件是一个yml格式文本文件，通过配置定义集群中的每个容器如何运行。(也就是docker run的集合)

### docker-compose安装



### docker-compose部署微服务集群





## docker镜像仓库



### 搭建私有仓库

搭建镜像仓库可以基于Docker官方提供的DockerRegistry来实现。

官网地址：https://hub.docker.com/_/registry

#### DockerRegistry

Docker官方的Docker Registry是一个基础版本的Docker镜像仓库，具备仓库管理的完整功能，但是没有图形化界面。

搭建方式比较简单，命令如下：

```shell
docker run -d \
    --restart=always \
    --name registry	\
    -p 5000:5000 \
    -v registry-data:/var/lib/registry \
    registry
```

命令中挂载了一个数据卷registry-data到容器内的/var/lib/registry 目录，这是私有镜像库存放数据的目录。

访问http://YourIp:5000/v2/_catalog 可以查看当前私有镜像服务中包含的镜像

#### 带有图形化界面版本

使用DockerCompose部署带有图象界面的DockerRegistry，命令如下：

```yaml
version: '3.0'
services:
  registry:
    image: registry
    volumes:
      - ./registry-data:/var/lib/registry
  ui:
    image: joxit/docker-registry-ui:static
    ports:
      - 8080:80
    environment:
      - REGISTRY_TITLE=myself-registry
      - REGISTRY_URL=http://registry:5000
    depends_on:
      - registry
```

##### 配置私有地址

我们的私服采用的是http协议，默认不被Docker信任，所以需要做一个配置：

```json
# 打开要修改的文件
vi /etc/docker/daemon.json
# 添加内容：
"insecure-registries":["http://10.211.55.4:8080"]
# 重加载
systemctl daemon-reload
# 重启docker
systemctl restart docker
```

##### 启动

```shell
docker-compose up -d 
```

![image-20230502025915627](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230502025915627.png)

访问：[地址](http://10.211.55.4:8080/)

![image-20230502030002068](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230502030002068.png)

### 推送拉取镜像

1. 重命名本地镜像tag,名称前缀为私有仓库的地址：192.168.150.101:8080/

   ```shell
   docker tag nginx:latest 10.211.55.4:8080/nginx:1.0 
   ```

   ![image-20230502030328094](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230502030328094.png)

2. 推送镜像

   ```shell
   docker push 10.211.55.4:8080/nginx:1.0 
   ```

   ![image-20230502030455487](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230502030455487.png)

   ![image-20230502030522527](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230502030522527.png)

3. 拉取镜像

   ```shell
   docker pull 10.211.55.4:8080/nginx:1.0
   ```

   ![image-20230502030746207](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/cloud/docker_simple/image-20230502030746207.png)