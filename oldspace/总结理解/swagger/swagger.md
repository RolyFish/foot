# Swagger简介

```txt
测试接口
前后端协同开发
以json的形式告诉你运行情况

主要是写注释(controller的model)
分组（我写我的你写你的）

达到前后端分离，协同开发的作用
且是一个web组件，不依赖其他东西，postman还要下载，还不好看

上线就不启用它，enable false  从enviroment中拿，也可以写一个properties
去读（我们数据库不就这么来的么）
```

# 依赖包

```xml
swagger2  和swagger-ui
引入是有版本号的，说明第三方（boot没有集成）
得写配置类
```

# 测试

```xml
但是呢，你啥也不配置，他也可以会有默认的配置，
访问http://localhost:8080/swagger-ui.html 会有一个页面
这就是导入包就有的，这不是一个正常的请求，更像一个资源，静态资源的引入方式：webjars这个页面就是ui下面的。

swagger-docket-apiinfo：每一个docket+（apiinfo）就是一个swagger

存在默认配置，XXXdefault：
		default_group_name  enable默认开启

```

## docket

```java
@Bean
public Docket docket(Environment environment) {
    //配置生效的环境 
    Profiles profiles = Profiles.of("dev");
    boolean flag = environment.acceptsProfiles(profiles);
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            //分组
            .groupName("于延闯")
            .enable(flag)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Controller.class))
            .paths(PathSelectors.ant("/test/**"))
            .build();
}

  配置swagger开发环境有效  生成环境无效
  可变参数
  Profiles profiles = Profiles.of("dev");
  配置swagger开关
  .enable(false)
      
  RequestHandlerSelectors.basePackage("com.roily.conroller") 扫描某个包
  RequestHandlerSelectors.any()  全部
  RequestHandlerSelectors.none()  都不扫描
								withClassAnnotation  扫描类上的注解
  .apis(RequestHandlerSelectors.basePackage("com.roily.conroller"))
  .apis(RequestHandlerSelectors.withMethodAnnotation(PostMapping.class))   
```

##  注释

```xm
```

