### docker 启动mysql

1. 查找镜像

   ```shell
   docker search mysql
   ```

2. 拉取镜像

   ```shell
   docker pull mysql:8.0.33
   ```

3. 启动mysql

   ```shell
   docker run --name mysql-xxljob \
   -p 3316:3306 \
   -v /home/rolyfish/home/mysql/data:/var/lib/mysql \
   -v /home/rolyfish/home/mysql/conf:/etc/mysql/conf.d \
   -e MYSQL_ROOT_PASSWORD=123456 \
   -d mysql:8.0.33
   ```

   

