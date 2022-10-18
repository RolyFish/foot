<%--
  Created by IntelliJ IDEA.
  User: lin
  Date: 2021/4/5
  Time: 16:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<%--在WEB-INF下的所有页面或者资源只能通过Controller，或者Servlet访问--%>
<body>
    <h1>登录页面</h1>
    <form action="${pageContext.request.contextPath}/user/login" method="post">
        用户名：<input type="text" name="username" >
        密 码：<input type="text" name="password"  >
        <input type="submit" value="登录">
    </form>
</body>
</html>
