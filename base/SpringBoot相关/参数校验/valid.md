学习一下，参数校验，及异常处理。



### 简介

​		参数校验时必要的，如何合理的处理也是必要的。学些一下@Valid 和 @Validated。

<hr>

### 为何需要Valid

​	无论是前端，或是其他系统调我们接口，我们的业务逻辑对于参数是有要求或是限制，如此也可避免不必要的异常。

> 如果我们不借助Valid来处理异常，往往会使得代码臃肿，如：

需要嵌套判断，如果参数过多，很不好。

```java
@RequestMapping(value = "/withOutValid", method = RequestMethod.POST)
public ResultVo<Object> validTest01(@RequestBody ValidTestVo validTestVo) {

    if (!ObjectUtils.isEmpty(validTestVo)){
        if (ObjectUtils.isEmpty(validTestVo.getParam01())){
            return ResultVo.error("param01参数不能为空");
        }
        if (ObjectUtils.isEmpty(validTestVo.getParam02())){
            return ResultVo.error("param02参数不能为空");
        }
        //、、、、
    }
    log.info("参数:{}", validTestVo);
   
    return ResultVo.success();
}
```



> 而使用@Valid注解可以很简洁的实现参数校验

```java
@RequestMapping(value = "/valid", method = RequestMethod.POST)
public ResultVo<Object> validTest02(@RequestBody @Valid ValidTestVo validTestVo, BindingResult bindingResult) {

    log.info("参数:{}", validTestVo);
    log.info("参数校验结果:{}", bindingResult);

    return ResultVo.success();
}
```

### 依赖

> `@Validated`隶属于org.springframework下，只要是SpringBoot项目都包含。
>
> `@Valid`隶属于javax下，版本不同，可能有区别，如果缺少依赖就添加如下依赖，版本选择本文2.6.6。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

<hr>

### 注解

#### 空检查

- @Null 只允许传null

- @NotNull 不允许传null，不会检查空字符串

- @NotBlank 不允许传null，且trim后不允许为空字符串

- @NotEmpty 不允许传null，且empty返回false

#### Booelan检查

- @AssertTrue Boolean类型，只允许传true

- @AssertFalse Boolean类型，只允许传 false

#### 长度检查

- @Size(min=, max=)  验证存在Size()api的集合长度

- @Length(min=, max=) 验证存在length()api的类  String）

#### 日期检查

- @Past 过去的时间

- @Future 未来的时间

- @Pattern 格式检查

#### 数值检查

- @Min   最小

- @Max  最大

#### 其他

- @CreitCardNumber信用卡验证

- @Email 验证是否是邮件地址，如果为null,不进行验证，算通过验证。

<hr>



### 嵌套校验

> 很多情况下，往往一个校验的参数中，嵌套着其他的对象。

#### 对于继承关系来说

> 子类继承父类，父类的校验也会被扫描到，都可以参与校验。



#### 对于聚合关系来说

##### 测试

> Param01

```java
public class Param01 {

    @ApiModelProperty("参数1，不为null校验")
    @NotNull(message = "参数不能为null")
    String param01;

    Param02 param02;
}
```

> Param02

```java
public class Param02 {

    @ApiModelProperty("参数1，不为null校验")
    @NotNull(message = "参数不能为null")
    String param01;

    @ApiModelProperty("参数2，不为null校验")
    @NotNull(message = "参数不能为null")
    String param02;

}
```

> controller

```java
@RequestMapping(value = "/validX", method = RequestMethod.POST)
public ResultVo<Object> validTest04(@RequestBody @Valid Param01 param01) {
    log.info("参数:{}", param01);
    //log.info("参数校验结果:{}", bindingResult);
    return ResultVo.success();
}
```

> postman测试

发现参数并没有参与校验！

```json
{
    "param01": "demoData",
    "param02": {
        "param01": "demoData"
    }
}
```

##### 解决

> 添加@Valid

```java
public class Param01 {
    @ApiModelProperty("参数1，不为null校验")
    @NotNull(message = "参数不能为null")
    String param01;
    
	@Valid
    Param02 param02;
}
```

##### 最佳实践

> ​	对于没有嵌套的校验，@Valid和@Validated没有区别，其校验结果都会放入BindingResult，但一般来说会通过异常捕获的方式处理校验失败的情况。
>
> ​	@Validated是Spring提供的，可以认为是对@Valid的封装，提供了分组等较灵活的使用方式。
>
> ​	使用@Validated在控制器参数校验，@Valid进行嵌套校验。



### 异常处理



> 第一步：在springBoot启动器同级目录下创建一个common.exception包，并在此目录下创建一个自定义全局异常处理类。

```java
public class GlobalCustomException extends RuntimeException {
    private static final long serialVersionUID = -7278881947512853935L;
    @ApiModelProperty("异常代码")
    private String code;
    @ApiModelProperty("异常描述")
    private String msg;
}
```

> 第二步：在该目录下，创建一个异常处理器。

一般来说会有一个`最后的屏障`来处理未知异常RuntimeException，以便友好显示。<br>

parse()方法是对BindResult的解析，显示具体哪个参数不符合校验规则，其实大可不必，上线了一般不会出现参数问题，主要方便联调。

```java
@RestControllerAdvice
@Slf4j
public class GlobalCustomExceptionHandler {
    /**
     * 未知异常
     *
     * @param e 异常
     * @return ResultVo<String>
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResultVo<String> unKnownException(RuntimeException e) {
        log.error("系统出现异常:{}", e.getMessage());
        return ResultVo.error(e.getMessage());
    }
    /**
     * 请求参数不合法
     *
     * @param notValid 请求参数不合法异常
     * @return ResultVo<String>
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultVo<Map<String, Object>> methodArgumentNotValidException(MethodArgumentNotValidException notValid) {
        Map<String, Object> errorDesc = parse(notValid);
        log.error("请求参数不合法:{}", JSON.toJSONString(errorDesc));
        return ResultVo.error(errorDesc);
    }
    
    /**
     * 解析异常
     *
     * @param notValid 异常
     * @return Map<String, String>
     */
    private Map<String, Object> parse(MethodArgumentNotValidException notValid) {
        //方法名
        String methodName = Objects.requireNonNull(notValid.getParameter().getMethod()).getName();
        //异常信息保存于BindingResult
        BindingResult exceptions = notValid.getBindingResult();
        List<ObjectError> allErrors = exceptions.getAllErrors();
        //返回结果
        Map<String, Object> result = new HashMap<>();
        //参数错误
        Properties fieldErrorProp = new Properties();
        allErrors.forEach((error) -> {
            //转化为fieldError
            FieldError fieldError = (FieldError) error;
            //放入prop
            fieldErrorProp.setProperty(fieldError.getField(), fieldError.getDefaultMessage());
        });

        result.put("methodName", methodName);
        result.put("fieldErrorProp", fieldErrorProp);
        return result;
    }
}
```