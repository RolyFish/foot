<%--
  Created by IntelliJ IDEA.
  User: RoilyFish
  Date: 2022/1/10
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <form action="${pageContext.request.contextPath}/code/t1" method="post">
      <input type="text" name="age"><br>
      <input type="text" name="name"><br>
      <input type="submit" value="提交"><br>
  </form>
  </body>
</html>
