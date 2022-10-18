配置sqlsessionfactory

我们知道对于数据库的操作底层只有一个东西叫做Connection！！

到mabatis这里我们会发现有一个sqlsession，他完全代替了conn，有他的所有操作，包括事务提交回滚

这个sqlsession是怎么来的呢：sqlsessionfactorybuilder---------mybatis配置文件(走一些config的配置类)------->

build方法生成一个sqlsessionfactory- openseqqsion()--》得到sqlsession



<img src="D:/File/Deskep/ssm.assets/image-20211028211801579.png" alt="image-20211028211801579" style="zoom: 50%;" />



得到的并不是sqlsession得到的是他的实现类



等到spring时发现了一个叫sqlsessionTemplate的类。在mybatis的spring下，没什么区别，实现了sqlsession接口，也有各种查询操作



这里又有了一个sqlsessionfactorybean

![image-20211028211122542](D:/File/Deskep/ssm.assets/image-20211028211122542.png)

其内有一个buildsqlsession的方法

![image-20211028213006361](D:/File/Deskep/ssm.assets/image-20211028213006361.png)

![image-20211028213040354](D:/File/Deskep/ssm.assets/image-20211028213040354.png)





![image-20211028214010078](D:/File/Deskep/ssm.assets/image-20211028214010078.png)

mapper扫描

```txt
之前如果说我们有一个mapper接口的话，如果要用它就必须实现其接口并通过spring ioc来把它注入到spring中

现在通过mapperscanner来扫描，将其自动注入到spring中，这和动态代理模式很像，可以理解为因为某些操作使得我们实现某个目的变得丰满、到位，那么一定是有方法的增强！！也就是说我们的mapper接口被代理了。
```

![image-20211028214502449](D:/File/Deskep/ssm.assets/image-20211028214502449.png)

```txx
然后这里有一个现象，就是service的实现类。可以通过set方式注入进去，而且是通过ref引用的方式
那么这就证实了我们的mapper被注入了被spring托管了知识我们看不到。
```

