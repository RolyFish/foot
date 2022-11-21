# 几个问题
## 1、配置文件路径  application.yaml
    第一个：项目根目录下的ymal  yml properties
    第二个： 根目录下的config下的  。。
    第三个： classpath下的config下的。。。
    第四个： classpath下的  、、、、
## 2、静态资源的路径
    在webmvcconfigration下有一个  addresourceshandler方法。里面就是静态资源的具体加载方式
    1、如果使用了webjars方式，即导入了相关的jarbao  webjars、jquery，那么就可以通过访问/META-INF/jquery方式
    访问静态资源
    2、走下面的方式，还是getStaticLocations，还是这几个路径 
    classpath:/META-INF/resources/", 这个不管
    "classpath:/resources/", 
    "classpath:/static/",       
    "classpath:/public/
## 3、欢迎页面的默认设置
    在webmvcautoconfigration中有一个welcompagehandlermapping方法，
    即首页处理映射器。有一个Resources参数，对应getWelcomepage方法
    他获取了 resourcespropertis中的一个熟悉  staticlocations即
    classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/
    这四个默认值。
    然后调用getindexhtml得到资源即在上面所述的四个路径下需要index.html
    如果没有报404走boot配置的404页面。
    