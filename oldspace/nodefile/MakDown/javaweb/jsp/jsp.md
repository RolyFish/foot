# jsp

```java
public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    final java.lang.String _jspx_method = request.getMethod();
    if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSP 只允许 GET、POST 或 HEAD。Jasper 还允许 OPTIONS");
      return;
    }

```

## jsp内置对象

```java
final javax.servlet.jsp.PageContext pageContext;
final javax.servlet.ServletContext application;
final javax.servlet.ServletConfig config;
javax.servlet.jsp.JspWriter out = null;
final java.lang.Object page = this;
javax.servlet.jsp.JspWriter _jspx_out = null;
javax.servlet.jsp.PageContext _jspx_page_context = null;
HttpServletRequest request
HttpServletResponse response
  
    可以在jsp页面直接使用
response.setContentType("text/html; charset=UTF-8");
pageContext = _jspxFactory.getPageContext(this, request, response,
                                          null, false, 8192, true);
_jspx_page_context = pageContext;
application = pageContext.getServletContext();
config = pageContext.getServletConfig();
out = pageContext.getOut();
_jspx_out = out;
```

## jsp原理

```txt
jsp文件被部署到tomcat服务器，浏览器访问jsp生成xxx_jsp.java(servlet),编译成.class文件。
```

## japservice

```java
_jspService  body内的变量都在这个方法作用域内

<%--jsp基本语法
<%=输出的内容%>
--%>
<%=new Date()%><br>
<%--jsp  java 脚本片段--%>

<%
    int sum =0;
    for(int i=0;i<100;i++){
        sum+=i;
    }
    out.println(sum);
%>
<%--java内嵌jsp--%>
<%
    for(int i=0;i<5;i++){
%>
<h1>A</h1>
<%
    }
%>
    
<%--代码块作用域--%>
<%
int i=0;
%>
<%
out.print(i);
%>
```

## jsp声明

```jsp
<%!
public void xx(){
    System.out.println("jsp声明");
}

%>
```

## jsp指令

```
<%@ page errorPage="500.jsp" %>
<%@ page import %>

<%@include file="common/header.jsp"%>
将俩页面合二为一  在代码块中定义同名变量报错，作用做相同
<%@include page="/common/header.jsp"%>
拼接页面  在代码块中定义同名变量作用域不同不会报错
```

### 定制错误页面

```jsp
    <error-page>
        <error-code>500</error-code>
        <location>/500.jsp</location>
    </error-page>
    <error-page>
        <error-code>404</error-code>
        <location>/404.jsp</location>
    </error-page>
```

# 9大内置对象

1. pagecontext   上下文对象 **存东西**
2. request            **存东西**
3. response
4. session   **存东西**
5. application   servletcontext **存东西**
6. config     servletconfig  
7. out
8. page
9. exception

```java
<%--内置对象--%>
<%
    pageContext.setAttribute("name1","yyc1");//页面中有效
    request.setAttribute("name2","yyc2");//一次请求
    session.setAttribute("name3","yyc3");//一次会话   打开关闭浏览器
    application.setAttribute("name4","yyc4");//保存在服务器  服务器开关
%>
${pageContext.getAttribute("name1")}
${pageContext.getAttribute("name2")}<%--找不到  但是过滤null值--%>
${pageContext.request.getAttribute("name2")}<%--可以取到--%>
${pageContext.findAttribute("name2")}
<hr>
${pageContext.findAttribute("name1")}
${pageContext.findAttribute("name2")}
${pageContext.findAttribute("name3")}
${pageContext.findAttribute("name4")}
```

```java
<%
pageContext.setAttribute("age",11,pageContext.SESSION_SCOPE);
session.setAttribute("age",20);
%>
```

