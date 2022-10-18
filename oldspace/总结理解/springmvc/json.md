# 了解

json  json字符串

json是一种数据格式，json可以转化为json字符串，json字符串可以解析成json对象。前端认json对象，web传输传的是字符串。所以后端得将对象转化为json字符串传输。

# json工具

- fastjson
- jackson

导入相关依赖就可以直接使用



# 使用

> @ResponseBody  响应不走视图解析器直接返回字符串

```java
@ResponseBody
@RequestMapping("/j1")
public String json1() {
    ObjectMapper objectMapper = new ObjectMapper();
    User user = new User("于延闯", 23, "男");
    String s = null;
    try {
        s = objectMapper.writeValueAsString(user);
    } catch (JsonProcessingException e) {
        e.printStackTrace();
    }
    return s;
}
```

> 这样交给前台就没有问题。
>
> 如果说浏览器支持的话，可以直接解析jaon字符串

![image-20220110170209924](D:\File\Desktop\总结理解\springmvc\json.assets\image-20220110170209924.png)

> json的工具有很多哦，具体选择吧

> json乱码统一解决

```xml
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
        <!--            json乱码问题配置  -->
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
```

> fastjson

```java
@ResponseBody
@RequestMapping("/f1")
public String fj1() {
    User user = new User("于延闯", 23, "男");
    String s = null;
    User parse = JSON.parseObject("{'name':'鸳鸯奶茶','age':'12','sex':'男'}",User.class);
    System.out.println(parse);
    s = JSON.toJSONString(user);
    return s;
}
```

