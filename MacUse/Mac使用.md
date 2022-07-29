### 终端



#### zsh

最新的mac都使用zsh.作为终端

#### 查看方式

~~~zsh
cat /etc/shells
## 或
vim  /etc/shells
~~~

可以发现有很多中终端

<img src="/Users/rolyfish/Desktop/MyFoot/MacUse/Mac使用.assets/image-20220612231304647.png" alt="image-20220612231304647" style="zoom: 50%;" />

#### 切换终端

切换为bash

~~~zsh
chsh -s /bin/bash
~~~



#### 配置文件

配置文件存在于访问下的用户目录下，并且都是隐藏文件夹。

使用 Shift+command+.可以切换，隐藏、显示隐藏文件。

zsh的配置文件是.zshrc



### item2

> 在任何地方隐藏唤醒终端命令：option+space

需要设置开启：

![image-20220612234134549](/Users/rolyfish/Desktop/MyFoot/MacUse/Mac使用.assets/image-20220612234134549.png)



### 安装brew

> Homebrew是 mac的包管理器，仅需执行相应的命令,就能下载安装需要的软件包，可以省掉自己去下载、解压、拖拽(安装)等繁琐的步骤。

#### 方法一

官方提供的安装脚本：

~~~zsh
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
~~~

但是很慢



#### 方法二：

镜像安装，这里选择中科大的镜像仓库。

~~~zsh
/usr/bin/ruby -e "$(curl -fsSL https://cdn.jsdelivr.net/gh/ineo6/homebrew-install/install)"
~~~