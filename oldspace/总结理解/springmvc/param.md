# springmvc参数接收方式

## 参数匹配

> 传入的参数名和接收的参数名匹配，直接接收，参数通过model回显

```java
 	@RequestMapping(value = "/t1",method = RequestMethod.GET)
    public String t1(String name, Model model){
        System.out.println(name);
        model.addAttribute("name",name);
        return "param";
    }
```

![image-20220110161152032](D:\File\Desktop\总结理解\springmvc\param.assets\image-20220110161152032.png)

## 参数不匹配

> 传入的参数名和接收的参数名不匹配，@RequestParam约束，参数通过model回显

```java
@RequestMapping(value = "/t2",method = RequestMethod.GET)
public String t2(@RequestParam("realName") String name, Model model){
    System.out.println(name);
    model.addAttribute("name",name);
    return "param";
}
```

![image-20220110161309970](D:\File\Desktop\总结理解\springmvc\param.assets\image-20220110161309970.png)

## 传对象

> 传入对象：会自动匹配属性，如果不传或者属性不匹配就为空或者默认值

***<u>实体类：</u>***

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int age;
    private String name;
}
```

```java
@RequestMapping(value = "/t3",method = RequestMethod.GET)
public String t3(User user, Model model){
    System.out.println(user.toString());
    model.addAttribute("user",user);
    return "param";
}
```

![image-20220110162039718](D:\File\Desktop\总结理解\springmvc\param.assets\image-20220110162039718.png)

## model modemap

```java
@RequestMapping(value = "/t4",method = RequestMethod.GET)
public String t4(User user, ModelMap model){
    System.out.println(user.toString());
    model.addAttribute("user",user);
    return "param";
}
```

> 没神么区别，一般用model就够了

```java
public class ExtendedModelMap extends ModelMap implements Model 
```

这说明modelMap和model有着密切联系，web容器或者spring容器会去匹配到合适的mmodel去传值

# 乱码解决方式

```java
@RequestMapping(value = "/t1",method = RequestMethod.POST)
public String t1(User user, Model model) {
    System.out.println(user);
    model.addAttribute("user",user);
    return "param";
}
```

```jsp
<form action="/param/t1" method="post">
    <input type="text" name="age"><br>
    <input type="text" name="name"><br>
    <input type="submit" value="提交"><br>
</form>
```

<img src="D:\File\Desktop\总结理解\springmvc\param.assets\image-20220110163400903.png" alt="image-20220110163400903" style="zoom:67%;" />

> 思考：这里如果使用req.setCharectEncoding("utf-8")会不会有效呢

我们知道在servlet中，请求的参数和传递都是通过req和resp的，也就是参数存在于他们的作用域中，这样处理req和resp的编码就会解决编码的问题，但是这里加入了一个model啊，会不会影响他的参数接收和传递呢？

> 配置spring的乱码过滤器

```xml
	<filter>
        <filter-name>charectEncoding</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>charectEncoding</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

<img src="D:\File\Desktop\总结理解\springmvc\param.assets\image-20220110164616967.png" alt="image-20220110164616967" style="zoom:80%;" />