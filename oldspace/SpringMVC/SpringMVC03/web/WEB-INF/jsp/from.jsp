<%--
  Created by IntelliJ IDEA.
  User: Roly_Fish
  Date: 2021/10/27
  Time: 21:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/c3/t3/3/5" method="post">
    <input type="submit" value="提交">
</form>

<form action="${pageContext.request.contextPath}/c3/t3" method="post">
    <input type="text" name="a"/>
    <input type="text" name="b"/>
    <input type="submit" value="提交">
</form>

<form action="${pageContext.request.contextPath}/u1/t5" method="post">
    <input value="text" name="name">
    <input type="submit" value="提交">
</form>
</body>
</html>
