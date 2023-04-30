# docker



## 初识docker







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



#### 容器命令

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



#### 挂载数据卷

