<%--
  Created by IntelliJ IDEA.
  User: Roly_Fish
  Date: 2021/11/1
  Time: 23:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.6.0.js"></script>
    <script>
        $(function () {
            $("#tc").change(function () {
                $.post({
                    url:"${pageContext.request.contextPath}/tt2",
                    data:{"in1":$("#tc").val()},
                    success:function (data) {
                        $("#sid").html(data.toString());
                    },
                    error:function () {
                        alert("error");
                    }
                })
            })
        })

    </script>
</head>
<body>

<p>
    <input type="text" id="tc">
    <span id="sid"></span>
</p>

</body>
</html>
