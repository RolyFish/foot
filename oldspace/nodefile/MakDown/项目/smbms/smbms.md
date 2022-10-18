# Smbms

## 工具

- jdk 8
- idea 2019
- mysql 5.7.19
- maven 3.5.2
- Tomcat  8

## 技术选型

- 语言 ：java
- jar包管理：maven
- 数据库：mysql
- 服务器： tomcat
- servlet+jsp

## 架构分析

<img src="D:/File/Desktop/MakDown/项目/smbms/smbms.assets/image-20211015214709215.png" alt="image-20211015214709215" style="zoom: 80%;" />

## 数据库表

用户表，角色表，商品表，供应商表，订单表

![image-20211015215106085](D:/File/Desktop/MakDown/项目/smbms/smbms.assets/image-20211015215106085.png)

## 搭建maven项目

使用idea 用模板搭建

1. 修改pom 和web配置文件

2.  创建java和resource包

3.  配置tomcat服务器，查看项目是否可以运行

4. 引入jar包（可在编写项目时依据需要引入）基本的servlet  jsp jstl taglibs conection 

5. 创建项目包结构

6. 创建实体类  orm（object relationShip mapping）映射  表和类映射

7. 编写基础公共类   

    1.数据库配置文件

    2.数据库连接操作，查询、更新基础方法  baseexque  baseupdate

    3.乱码处理过滤器也在这处理

    4.导入静态资源

## 登录功能



- ***userdao中的获取登录用户 根据账户名查询***

```java
public SmbmsUser getLoginUser(Connection conn, String userCode);
```

- ***userservice 判断账户名和pass是否匹配***

```java
public SmbmsUser login(String userCode, String password);
```

- ***编写servlet***

```java
System.out.println("login业务");
String userCode = req.getParameter("userCode");
String userPassword = req.getParameter("userPassword");
UserService userService = new UserServiceImp();
SmbmsUser user = userService.login(userCode, userPassword);
if(user!=null){
    System.out.println("查到了。。。可以登录");
    //放入session
    req.getSession().setAttribute(Constants.USER_SESSION,user);
    resp.sendRedirect("jsp/frame.jsp");
}else {
    req.setAttribute("error","用户名或密码不正确！");
    req.getRequestDispatcher("login.jsp").forward(req,resp);
}
调用service获取用户的操作
```

- ***注册servlet***

```java
	<servlet>
        <servlet-name>loginServlet</servlet-name>
        <servlet-class>com.roily.servlet.user.LoginServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>loginServlet</servlet-name>
        <url-pattern>/login.do</url-pattern>
    </servlet-mapping>
```

- ***测试登录功能***



## 退出功能

写servlet 、 移除session 、页面跳转、 注册servlet

## 编写登录拦过滤器

```java
//判断用户是否登录  判断session是否为空
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        SmbmsUser user = (SmbmsUser) request.getSession().getAttribute(Constants.USER_SESSION);
        if(user!=null){
            filterChain.doFilter(req,resp);
        }else {
            response.sendRedirect(((HttpServletRequest) req).getContextPath()+"/syserror.jsp");
        }
```

## 密码修改

写pwdmodifydao     

写pwdmodifyservice

写pwdservlet

注册pwdservlet

测试

## 优化密码修改

js动态判断旧密码是否正确

1.鼠标失去焦点  blur

2.ajax请求

3.json数据返回

```java
oldpassword.on("blur",function(){});
```

```java
$.ajax({
			type:"GET",
			url:path+"/jsp/user.do",
			data:{method:"pwdcheck",oldpassword:oldpassword.val()},
			dataType:"json",
			success:function(data){
				if(data.result == "true"){//旧密码正确
					validateTip(oldpassword.next(),{"color":"green"},imgYes,true);
				}else if(data.result == "false"){//旧密码输入不正确
					validateTip(oldpassword.next(),{"color":"red"},imgNo + " 原密码输入不正确",false);
				}else if(data.result == "sessionerror"){//当前用户session过期，请重新登录
					validateTip(oldpassword.next(),{"color":"red"},imgNo + " 当前用户session过期，请重新登录",false);
				}else if(data.result == "error"){//旧密码输入为空
					validateTip(oldpassword.next(),{"color":"red"},imgNo + " 请输入旧密码",false);
				}
			},
			error:function(data){
				//请求出错
				validateTip(oldpassword.next(),{"color":"red"},imgNo + " 请求错误",false);
			}
		});
```

```java
resp.setContentType("application/json");
PrintWriter writer = resp.getWriter();
writer.write(JSONArray.toJSONString(map));
writer.flush();
writer.close();
```

## 用户管理功能

**查询用户总数**

userdao

userservice

test。。。。

**查询用户列表**

```txt
页面包含userlist和rolelist，以及底部分页条相关信息
需要在servlet中将数据添加到request域中，达到初始化页面数据的作用
编写userdao.getuserlist(connetion,pstm,rs,params) 
//由于list页面条件查询功能和这个功能是复用的所以需要携带参数 动态拼接sql
编写userservice.getuserlist(username,usercode)
这里判断username 和 usercode是否为空，为dao传递参数数组
编写userservlet.getuserlist(req,resp)
req获取username和usercode参数，其中分页相关参数需要在这里设置
调用业务层代码，并将数据放入req域中，转发到userlist页面中
```

## 添加用户

```txt
adduser页面中有一个select标签需要初始化数据，需要在addlist.js脚本中发送一个初始化ajax请求，得到userrole列表。
usercode唯一需要定义一个鼠标失焦事件，发送ajax请求判断是否重复
```

## 删除用户

```txt
根据id删除，需添加事务处理
conn.setAutoCommit(false);
rows = userDao.delUser(conn, id);
conn.commit();
conn.setAutoCommit(true);
```

## 查看用户

```txt
根据uid查看，href到userview.jsp页面
将需要展示的数据存在req域中 
```

## 修改用户

```txt
根据id修改  天津爱事务处理
```



