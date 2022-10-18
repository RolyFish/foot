<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>首页</h1>
<span>
    ${pageContext.request.session.getAttribute('loginfo')}

    <a href="${pageContext.request.contextPath}/user/goout">注销</a>
</span>
</body>
</html>
