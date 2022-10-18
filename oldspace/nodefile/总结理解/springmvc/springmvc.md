mvc架构简单导图：

![image-20211213003310236](./springmvc.assets\image-20211213003310236.png)

servlet+jsp+javabean

servlet：实现httpserlet接口，web.xml注册servlet（一个请求一个类）

视图控制：重定向，转发。

```java
req.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(req,resp);
resp.sendRedirect("/WEB-INF/jsp/result.jsp");
```

![image-20211213010203557](.\springmvc.assets\image-20211213010203557.png)

这边简单的继承关系，只实现了请求方式的分发，如果说想实现请求复用的话，得在请求中携带method参数，比如说login，updata，delete



# springmvc执行原理

## 处理器映射器  hadlerMApping

解析url，得到具体控制器（http:localhost:8080/smbms/login）=>解析得到/login

## 处理器适配器  handleradapter

根据得到了控制器去去找相对应的servlet

## 视图解析器 viewReolver

配置前缀后缀，携带数据，视图渲染



![image-20211213121120619](.\springmvc.assets\image-20211213121120619.png)

1、请求来到DispatcherServlet，DispatcgerServlet找到HandlerMapping进行url解析，将url分解。**返回的是一个字符串数组。**

2、HandlerExcution调用getHandler方法得到具体的handler，返回给DispatcherServlet。**返回的是handler（object）**

3、DispatcherServlet得到具体Handler，交给HandlerAdapter得到具体的控制器（servlet），这里和数据库进行交互，处理具体业务。**返回的是ModelAndView**

4、返回数据给DispatcherServlet，DispatcherServlet调用ViewResolver将数据渲染到视图层

```xml
<!--    处理器映射器-->
    <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>

<!--    处理器适配器-->
    <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>

<!-- 视图解析器    -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="internalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

    <bean id="/hello" class="com.roily.controller.HelloController"/>
```

```java
//实现controller接口并重写里面的方法，它就是一个controller
public class HelloController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg","mvc再回顾");
        mv.setViewName("hello");
        return mv;
    }
}
```

所有请求都会走dispatcherServlet，他会做一些初始化和调用的操作

![image-20211213124647843](.\springmvc.assets\image-20211213124647843.png)

# 注解实现springmvc

依赖：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.1.9.RELEASE</version>
</dependency>
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>servlet-api</artifactId>
    <version>2.5</version>
</dependency>
<dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>jsp-api</artifactId>
    <version>2.2</version>
</dependency>
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.22</version>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.8</version>
</dependency>
```

注册dispatcherServlet：

配置文件：

```xml
扫描包
<context:component-scan base-package="com.roily.controller"/>

<!-- 让Spring MVC不处理静态资源 -->
<mvc:default-servlet-handler />

<!--
支持mvc注解驱动
    在spring中一般采用@RequestMapping注解来完成映射关系
    要想使@RequestMapping注解生效
    必须向上下文中注册DefaultAnnotationHandlerMapping
    和一个AnnotationMethodHandlerAdapter实例
    这两个实例分别在类级别和方法级别处理。
    而annotation-driven配置帮助我们自动完成上述两个实例的注入。
 -->
<mvc:annotation-driven>
    <mvc:message-converters register-defaults="true">
        <!--            json乱码问题配置-->
        <bean class="org.springframework.http.converter.StringHttpMessageConverter">
            <constructor-arg value="UTF-8"/>
        </bean>
        <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
            <property name="objectMapper">
                <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                    <property name="failOnEmptyBeans" value="false"/>
                </bean>
            </property>
        </bean>
    </mvc:message-converters>
</mvc:annotation-driven>


<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="internalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/jsp/"/>
    <property name="suffix" value=".jsp"/>
</bean>
```

```java
@Controller
public class HelloController {
    @RequestMapping("/hello1")
    public String hello(Model model){
        model.addAttribute("msg","annocation springmvc");
        return "hello";
    }
}
```

