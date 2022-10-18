<%--
  Created by IntelliJ IDEA.
  User: Roly_Fish
  Date: 2021/11/1
  Time: 22:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.6.0.js"></script>
    <script>
        function tn() {
            $.post({
                url:"${pageContext.request.contextPath}/t2",
                data: {"username":$("#username").val()},
                success:function (data) {
                   if(data.toString().trim()=="可以使用"){
                       $("#stn").css("color","green");
                   }else {
                       $("#stn").css("color","red");
                   }
                    $("#stn").html(data);
                }
            })
        }
    </script>
</head>
<body>


<p>
    <input type="text" id="username" onblur="tn()"><span id="stn"></span>
</p>
<p>
    <input type="pwd" id="pwd" onblur="tn()"><span id="stp"></span>
</p>
</body>
</html>
