# java三大版本

- javase（桌面开发，控制台开发）
- javaee（web开发）
- javame（应用程序开发（手机端。。。））

# jdk jre jvm

> 包含关系
>
> jdk（java development kit）还包含了一些工具，java javac javadoc jar。。。。
>
> jre   (java runtime entirment)包含了各种类库
>
> jvm (java virtual machine)java虚拟机

<img src="jdk jre.assets\image-20211230095159895.png" alt="image-20211230095159895" style="zoom:67%;" />

# 删除jdk

1. 删除java安装包  在环境配置里可以看到安装路径
2. 删除 javahome  及 path相关配置
3. cmd查看 java—version

# 安装jdk

1. 下载jkd
2. 安装jdk(安装程序版，压缩包版本)
3. 配置javahome   
4. path   %java_home%\bin  %java_home%\jre\bin（%类似于引用的意思）
5. cmd查看java-version

# 编译型和解释型

- 编译型
  - 将程序一次性转换为1、0文件，直接运行
- 解释型
  - 运行一行转换一行

 <img src="jdk jre.assets\image-20211230100525804.png" alt="image-20211230100525804" style="zoom:67%;" />

> 解释型语言他的运行效率不如编译型语言
>
> 但是用于网页开发足够了（网页不是有加载的概念嘛，人们也习惯了）