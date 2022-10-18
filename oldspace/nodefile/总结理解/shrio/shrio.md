# shrio

## 三个对象

- realm   **（继承AuthorizingRealm）**

  - ```txt
    shrio的认证和授权操作在realm中做
    ```

  - 认证  

    - ```java
      匹配username是否正确。这里会抛出异常。用户不存在，密码错误。。。
      ```

  - 授权

- securityManager

  ```xml
  他的实例化需要realm作为参数
  ```

  

- subject  （shriofilterfactorybean）

  ```xml
  需要securityMAnager
  ```

## controller

![image-20211124123942113](D:/File/Deskep/%E6%80%BB%E7%BB%93%E7%90%86%E8%A7%A3/shrio/shrio.assets/image-20211124123942113.png)