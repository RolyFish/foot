<%--
  Created by IntelliJ IDEA.
  User: Roly_Fish
  Date: 2021/11/1
  Time: 23:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.6.0.js"></script>
    <script>
        $(function () {
            $("#sbtn").click(function () {
                $.post({
                    url:"${pageContext.request.contextPath}/tt1",
                    success:function (data) {
                        var html="";
                        for (let i = 0; i<data.length; i++){
                            html+="<tr>"+
                                    "<td>"+data[i].name+"</td>"+
                                    "<td>"+data[i].age+"</td>"+
                                    "<td>"+data[i].paw+"</td>"+
                                  "</tr>";
                        }
                        $("#tb").html(html);
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
<input type="button" id="sbtn" value="家在数据"><br>
<table>
    <tr>
        <td>姓名</td>
        <td>年龄</td>
        <td>密码</td>
    </tr>
    <tbody id="tb">
    </tbody>
</table>

</body>
</html>
