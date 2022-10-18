<%--
  Created by IntelliJ IDEA.
  User: lin
  Date: 2021/4/5
  Time: 12:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>

    <script src="${pageContext.request.contextPath}/statics/js/jquery-3.6.0.js"></script>

<%--ajax.post方式--%>
<%--Ajax三要素：url,data,callback--%>
    <script>
      function a(){
        $.post({
          url:"${pageContext.request.contextPath}/a1",
          data:{"name":$("#username").val()},
          success:function (data) {
            alert(data);
          }
        })
      }
    </script>

  </head>
  <body>

<%--失去一个焦点的时候，发起一个请求到后台--%>
<%--触发函数a()--%>
  用户名：<input type="text" id="username" onblur="a()">

  </body>
</html>
