# Maven

包管理工具，用来导入jar包的   ***约定大于配置***

```xml
防止maven读取不到  项目目录中的配置文件 
<resources>
          <resource>
              <directory>src/main/resources</directory>
              <includes>
                  <include>**/*.properties</include>
                  <include>**/*.xml</include>
              </includes>
              <filtering>false</filtering>
          </resource>
          <resource>
              <directory>src/main/java</directory>
              <includes>
                  <include>**/*.properties</include>
                  <include>**/*.xml</include>
              </includes>
              <filtering>false</filtering>
          </resource>
  </resources>
```

# 环境配置

> 不用配置也可以用，之后的springboot学习，项目可能会自动引用这个配置，所以配上

> M2_HOME   MAVEN_HOME  path

![image-20220106183956436](Maven.assets\image-20220106183956436.png)

![image-20220106184126977](Maven.assets\image-20220106184126977.png)

# 阿里云镜像

