<%--
  Created by IntelliJ IDEA.
  User: Roly_Fish
  Date: 2021/11/2
  Time: 0:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>登录页面</h1>
<form action="${pageContext.request.contextPath}/user/login" method="post">
    <input type="text" name="username"><br>
    <input type="text" name="password"><br>
    <input type="submit" value="提交">
</form>
</body>
</html>
