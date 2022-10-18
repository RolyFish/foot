# 一个单体应用

一个单体应用他的各个模块够在一起，耦合度非常高，更有甚者在jsp中写java代码，在servlet中写html标签。

很不好！！

可读性较差。

维护困难。

不易于协同开发

![image-20211213212105327](D:\File\Deskep\总结理解\springcloud\dubbo+zookeeper.assets\image-20211213212105327.png)

# mvc模式（垂直架构）

拆分模块，降低耦合，协同开发。

针对早期的mvc，需要编写大量数据库相关的代码，公共模块利用率较低。

但是他还是一个单体啊，想要应对较大的服务器压力，还是只能提供多态服务器，且资源浪费服务器性能分配不完美。

# 分布式服务架构



![image-20211213213017585](D:\File\Deskep\总结理解\springcloud\dubbo+zookeeper.assets\image-20211213213017585.png)

每个服务提供的功能不同，各司其职，需要哪种服务就去取相关服务。



# 什么是RPC

推荐阅读文章：[https://www.jianshu.com/p/2accc2840a1b](https://www.jianshu.com/p/2accc2840a1b)

 

