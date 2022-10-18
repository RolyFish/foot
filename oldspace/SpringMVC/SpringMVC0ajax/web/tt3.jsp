<%--
  Created by IntelliJ IDEA.
  User: Roly_Fish
  Date: 2021/11/1
  Time: 23:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.6.0.js"></script>
    <script>
        $(function () {
            $("#username").blur(function () {
                $.post({
                    url:"${pageContext.request.contextPath}/tt3",
                    data:{"username":this.value},
                    success:function (data) {
                        if(data.toString().trim()=="ok"){
                            $("#us").css("color","green");
                        }else {
                            $("#us").css("color","red");
                        }
                        $("#us").html(data);
                    }
                })
            })
        })

    </script>
</head>
<body>


<p>
    <input type="text" id="username"><span id="us"></span>
</p>
<p>
    <input type="text" id="password"><span id="ps"></span>
</p>
</body>
</html>
