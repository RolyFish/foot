<%--
  Created by IntelliJ IDEA.
  User: Roly_Fish
  Date: 2021/10/29
  Time: 21:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.6.0.js"></script>
    <script>
        function a() {
            $.post({
                url: "${pageContext.request.contextPath}/test2",
                data: {"name": $("#username").val()},
                success: function (data) {
                    alert(data);
                }
            })
        }

        function b() {
            $.post({
                url: "${pageContext.request.contextPath}/ajax1",
                success: function (data) {
                    alert(data);
                }
            })
        }

        function c() {
            $.ajax({
                url: "${pageContext.request.contextPath}/ajax2",
                type: "post",
                data: {"name":$("#ajax2").val().trim()},
                success: function (data) {
                    alert(data);
                },
                error: function () {
                    alert("ajax请求失败");
                }
            })
        }
    </script>
</head>
<body>
<p>
    <input type="text" id="username" onblur="a()">
</p>
<p>
    <input type="text" id="ajax" onblur="b()">
</p>

<p>
    <input type="text" id="ajax2" onblur="c()">
</p>
</body>
</html>
