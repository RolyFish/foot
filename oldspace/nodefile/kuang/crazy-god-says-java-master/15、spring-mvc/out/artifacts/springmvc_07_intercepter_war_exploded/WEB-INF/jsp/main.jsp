<%--
  Created by IntelliJ IDEA.
  User: lin
  Date: 2021/4/5
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>首页</h1>
    <span>${username}</span>
    <p>
        <a href="${pageContext.request.contextPath}/user/goOut">注销</a>
    </p>
</body>
</html>
