## 简介

linux基于***<u>unix</u>***，或者说其内核是unix，目前流行的发行版本***<u>Ubuntu、Centos</u>***。一切皆文件。

特点：开源，社区活跃的很高、稳定、多用户多任务



## linux目录

```bash
/ 根目录
/etc  放一些配置文件和文件夹
/home：用户的主目录，在Linux中，每个用户都有一个自己的目录，一般该目录名是以用户的账号命名的。
/opt：这是给主机额外安装软件所摆放的目录。比如你安装一个ORACLE数据库则就可以放到这个目录下。默认是空的。
/usr：这是一个非常重要的目录，用户的很多应用程序和文件都放在这个目录下，类似于windows下的program files目录。
```

## 基本命令

```bash
cd / 
ls -[tag] 
```

```bash
mkdir
rmdir(-p)  （rm-rf 具体path）//-p递归删除
touch
```

```bash
cp  //拷贝文件
```

```bash
mv  //移动文件
```

## 文件前的属性

![image-20220215210009855](linux.assets\image-20220215210009855.png)

在Linux中第一个字符代表这个文件是目录、文件或链接文件等等：

当为[ d ]则是目录
当为[ - ]则是文件；
若是[ l ]则表示为链接文档 ( link file )；（快捷方式）
若是[ b ]则表示为装置文件里面的可供储存的接口设备 ( 可随机存取装置 )；
若是[ c ]则表示为装置文件里面的串行端口设备，例如键盘、鼠标 ( 一次性读取装置 )。

接下来的字符中，以三个为一组，且均为『rwx』 的三个参数的组合。

其中，[ r ]代表可读(read)、[ w ]代表可写(write)、[ x ]代表可执行(execute)。

修改文件属性：

```bash
chmod [-R] xyz 文件或目录

r:4    w:2    x:1

可读可写不可执行 rw- 6
可读可写不课执行 rwx 7

chomd  777  文件赋予所有用户可读可执行！
```

## 查看文件

```bash
cat
nl  //显示行号
head -n 3 //显示第几行
vim
```

## vim

```bash
vim path
[i]  insert
esc
[:]
q!
wq

光标移动几行
数字＋enter

/word  向下搜索
?word	向上搜索
```

## 磁盘管理和进程管理

```bash
df   du
```

```bash
ps -aux|grep java
pstree -pu  # p parent父进程id  u user 用户组
kill -9 杀死进程
```

## 安装软件

- rpm  rpm源安装
- 压缩包  XXX.gz
- yum  (docker)

```bash
tar -zxvf apache-tomcat-9.0.22.tar.gz
```

## 防火墙命令

```bash
# 查看防火墙状态
[root@RoilyFish tomcat]# firewall-cmd --state
running
# 查看放行的端口
[root@RoilyFish tomcat]# firewall-cmd --zone=public --list-ports
21/tcp 9100/tcp 20/tcp 8080/tcp 9200/tcp 3306/tcp 9000/tcp 3310/tcp 3344/tcp 3355/tcp 6379/tcp 5601/tcp 22/tcp
# 重载配置 不重启
[root@RoilyFish tomcat]# firewall-cmd --reload
success
# 暴露一个端口 （--permanent永久生效，没有此参数重启后失效）
[root@RoilyFish tomcat]#firewall-cmd --zone=public --add-port=80/tcp --permanent    
```

```bash
systemctl status firewalld
systemctl start firewalld
systemctl stop firewalld
systemctl restart firewalld
```

## docker

```
docker images
docker ps -a
docker stop containor
docker restart containor
docker stop containor

```

