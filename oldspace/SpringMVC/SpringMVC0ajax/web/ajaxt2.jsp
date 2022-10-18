<%--
  Created by IntelliJ IDEA.
  User: Roly_Fish
  Date: 2021/11/1
  Time: 22:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.6.0.js"></script>

</head>
<body>

<input type="button" id="btn" value="加载数据" onclick="ss()">
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
<script>
        function ss() {
            $.post({
                url:"${pageContext.request.contextPath}/t1",
                success:function (data) {
                    var html = "";
                    for (let i=0;i<data.length;i++){
                        html+="<tr>"+
                            "<td>"+data[i].name+"</td>"+
                            "<td>"+data[i].age+"</td>"+
                            "<td>"+data[i].paw+"</td>"+
                            "</tr>"
                    }
                    $("#tb").html(html);
                }
            })
        }
</script>
</html>
