# 代理模式

springaop底层：代理模式

## 代理模式分类

- 静态代理

<img src="D:\File\Desktop\nodefile\MakDown\Spring/spring2.assets/image-20211024162909917.png" alt="image-20211024162909917" style="zoom:67%;" />

### 静态代理

- 抽象接口： 接口  抽象类

  - ```java
    public interface Rent {
        public void rent();
    }
    ```

- 真实角色： 被代理的角色

  - ```java
    //房东
    public class Host implements Rent{
        public void rent() {
            System.out.println("真实对象：房东：出租房子！！");
        }
    }
    ```

- 代理角色： 代理真实角色  添加附属操作

  - ```java
    public class Proxy implements Rent{
        private Host host;
    
        public Proxy() {
        }
    
        public Proxy(Host host) {
            this.host = host;
        }
    
        public void rent() {
            this.seeHouse();
            host.rent();
        }
        public void seeHouse() {
            System.out.println("代理对象：中介：看房");
        }
    
    }
    ```

- 客户 人  访问代理对象的人

  - ```java
    public static void main(String[] args) {
        System.out.println("client:租房的人：想租房子");
        Host host = new Host();
        Proxy proxy = new Proxy(host);
        proxy.rent();
    }
    ```

好处：

1. ​	真实角色所做事情更加纯粹，不用管附加业务
2. ​    附加业务交由代理对象做，分工明确
3. ​    附加业务拓展方便集中管理

缺点：

​	每一个真实对象就会产生一个代理对象，代码量大

### 动态代理

动态代理角色一样

动态生成

两类：基于接口，基于类

 	1. 基于接口   jdk动态代理
 	2. 基于类：cglib
 	3. java字节码实现    javasist

### Spring动态代理

spring   api实现

***一个接口***

```java
public interface UserService {
    public void add();
    public void del();
    public void update();
    public void query();
}
```

```java
public class Log implements MethodBeforeAdvice {
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(target.getClass().getName()+"的"+method.getName()+"执行");
    }
}
public class AfterLog implements AfterReturningAdvice {
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println(target.getClass().getName()+"的"+method.getName()+"执行"+"\n"+
                "返回值为"+returnValue);
    }
}
```

***配置文件***

```xml
<bean id="userService" class="com.roily.service.UserServiceImpl"/>
<bean id="log" class="com.roily.log.Log"/>
<bean id="afterLog" class="com.roily.log.AfterLog"/>
<aop:config>
    <!--    切入点    -->
    <aop:pointcut id="pointcut" expression="execution(* com.roily.service.UserServiceImpl.*(..))"/>
    <!-- 执行环绕增强       -->
    <aop:advisor advice-ref="log" pointcut-ref="pointcut"/>
    <aop:advisor advice-ref="afterLog" pointcut-ref="pointcut"/>
</aop:config>
```

自定义实现

```xml
<bean id="defaultlog" class="com.roily.log.defaultlog"/>
    <aop:config>
        <aop:aspect ref="defaultlog">
            <aop:pointcut id="point" expression="execution(* com.roily.service.UserServiceImpl.*(..))"/>
            <aop:before method="before" pointcut-ref="point"/>
            <aop:after method="after" pointcut-ref="point"/>
        </aop:aspect>
    </aop:config>
</beans>
```

```java
public class defaultlog {

    private void before(){
        System.out.println("++++++++before+++++++++");
    }
    private void after(){
        System.out.println("++++++++after+++++++++");
    }
}
```

***注解实现***

```java
@Component
@Aspect
public class AnnocationPointCut {

    @Before("execution(* com.roily.service.UserServiceImpl.*(..))")
    public void before(){
        System.out.println("方法执行前==");
    }
    @After("execution(* com.roily.service.UserServiceImpl.*(..))")
    public void after(){
        System.out.println("方法执行后==");
    }
}
```

```xml
<context:annotation-config/>
<context:component-scan base-package="com.roily.*"/>
<aop:aspectj-autoproxy/>
```

# spring整合mybatis































