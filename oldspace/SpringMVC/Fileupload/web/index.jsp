<%--
  Created by IntelliJ IDEA.
  User: RoilyFish
  Date: 2022/1/10
  Time: 20:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>


<form action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data" method="post">
    <input type="file" name="file"/>
    <input type="submit" value="upload">
</form>
<hr>
<form action="${pageContext.request.contextPath}/upload2" enctype="multipart/form-data" method="post">
    <input type="file" name="file"/>
    <input type="submit" value="upload">
</form>
<hr>

<form action="${pageContext.request.contextPath}/download" method="get">
    <input type="text" name="fileName"/>
    <input type="submit" value="下载"/>
</form>
<hr>
<a href="${pageContext.request.contextPath}/download">点击下载</a>
</body>
</html>
