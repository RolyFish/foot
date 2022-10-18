# JavaWeb

***web***  

1. 静态web  html  css
2. 动态web  servlet+jsp  php

Web应用程序需借助服务器才得以被访问

Web应用程序

1. html css js
2. javaapps
3. jar包
4. 配置文件

## 静态web

- *.htm \ *.html

- javaScript     伪动态    轮播图

  无法和数据库交互  无法实现持久化

## 动态web

就是将数据结合静态页面进行响应

### Web服务器

jsp/servlet：

- sun公司主推的b/s架构

- 可承载高并发

- 基于java语言   跨平台	


## Tomcat

## 安装

http协议端口80（默认可不写）

https协议端口443

tomcat  默认端口8080

```bash
    <Connector URIEncoding="utf-8" connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/>
    <Host appBase="webapps" autoDeploy="true" name="localhost" unpackWARs="true">
     端口可改，name可改
     
name对应本机IP地址，需修改计算机驱动（driver - 》 host）
C:\Windows\System32\drivers\etc
```

<img src="D:\File\Desktop\MakDown\javaweb/01javaweb.assets/image-20210906205233716.png" alt="image-20210906205233716" style="zoom: 67%;" />

```bash
一次网络请求的过程
# 客户端发送请求  url
# 解析url=》协议+域名+站点+资源
# 根据域名，检查浏览器缓存，查看浏览器dns字典
# 检查本机dns服务，查看是否存在域名对应ip地址
	# 有  则访问
	# 无  则去云DNS里找 （根域名服务器）
总之网络请求需找到ip地址
```
