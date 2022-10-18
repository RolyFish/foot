<%--
  Created by IntelliJ IDEA.
  User: lin
  Date: 2021/4/5
  Time: 17:25
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
      <input type="submit">
    </form>

    <a href="/download">点击下载</a>

  </body>
</html>
