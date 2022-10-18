![image-20210916215029763](jsp%E5%86%85%E7%BD%AE%E5%AF%B9%E8%B1%A1.assets/image-20210916215029763.png)



![image-20210916215131447](jsp%E5%86%85%E7%BD%AE%E5%AF%B9%E8%B1%A1.assets/image-20210916215131447.png)



![image-20210916215412311](jsp%E5%86%85%E7%BD%AE%E5%AF%B9%E8%B1%A1.assets/image-20210916215412311.png)

```txt
jsp,继承了httpjspbase。
httpjspbase继承了httpservlet。并有着如图所示的几大方法，包括资源的加载init，service方法和destory方法。
本质上jsp就是一个servlet，只是其为开发者提供了简单书写页面的方法。（out.printt()）
```

# jsp九大内置对象

![image-20210916215929820](jsp%E5%86%85%E7%BD%AE%E5%AF%B9%E8%B1%A1.assets/image-20210916215929820.png)

- 程序级别的
  - application   >servletContext
  - config            >servletconfig
- 页面级别的
  - pagecontext 
  - this  page
  - out   jspwriter (实现writer接口  和response.getWriter(printwriter)  一样)
- 其他作用域
  - session
  - request
  - response
  - exception

```jap
<%--内置对象--%>
<%
    pageContext.setAttribute("name1","yyc1");//页面中有效
    request.setAttribute("name2","yyc2");//一次请求
    session.setAttribute("name3","yyc3");//一次会话   打开关闭浏览器
    application.setAttribute("name4","yyc4");//保存在服务器  服务器开关
%>
${pageContext.getAttribute("name1")}
${pageContext.getAttribute("name2")}<%--找不到  但是使用el表达式可以过滤null值--%>
${pageContext.request.getAttribute("name2")}<%--可以取到--%>
${pageContext.findAttribute("name2")}
<hr>
${pageContext.findAttribute("name1")}//都可以找到
${pageContext.findAttribute("name2")}
${pageContext.findAttribute("name3")}
${pageContext.findAttribute("name4")}
```

pageContext可以获得：request   response  session   

![image-20210916220349051](jsp%E5%86%85%E7%BD%AE%E5%AF%B9%E8%B1%A1.assets/image-20210916220349051.png)

# 作用域问题

```txt
四个作用域：

page：当前页面有效
request：一次请求有效，即从http请求到服务器处理结束，返回响应的整个过程，存放在HttpServletRequest对象中。在这个过程中可以使用forward方式跳转多个jsp。在这些页面里你都可以使用这个变量。
session：一次会话有效，只要页面不关闭就一直有效（或者直到用户一直未活动导致会话过期，默认session过期时间为30分钟，或调用HttpSession的invalidate()方法）。存放在HttpSession对象中
application：是程序全局变量，对每个用户每个页面都有效。存放在ServletContext对象中。存在服务器中
=========================================================================================
对应于：
page:pagecontext    
request:request
session：session
application:  application
=========================================================================================
getAttribute和findAttribute的区别:
他们的主要区别在于，getAttribute的作用域仅为page.返回null值。
而findAttribute则是按作用域从小到大不断去找，在page中没有找到就去request中，然后依次增加范围，找不到返回null值

```

![image-20210916222001580](jsp%E5%86%85%E7%BD%AE%E5%AF%B9%E8%B1%A1.assets/image-20210916222001580.png)

```txt
pagecontext可以设置作用域。setattribute错在重载的方法。
以上两语句等价    20  覆盖 11
```

# 使用场景

```txt
request： 客户端向服务器发送请求时用request：一次性
session： 同一个客户端需要重复使用的数据   ：购物车
application：不同数据重复使用   聊天数据

```



# jsp标签   jstl 标签  el表达式   express  language

**el表达式**：

​	获取数据

​	执行运算

​	获取web常用对象

**jsp标签：最多最多用到以下俩**

```java
<jsp:include page="/index.jsp"></jsp:include>
<jsp:forward page="/index.jsp">
    <jsp:param name="name1" value="于闫闯"/>
</jsp:forward>
    
    也可以自闭合。  
    这种转发方式  和  http:/127.0.0.1:8080/index.jsp?&a=1&b=2一样
```

**jstl标签**

扩展html标签：可自定义，但是一般都是约定熟成的

***核心标签***

```txtx
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
引入jstl核心标签库：c标签
```

![image-20210916225840282](jsp%E5%86%85%E7%BD%AE%E5%AF%B9%E8%B1%A1.assets/image-20210916225840282.png)

```txt
<form action="jstl标签.jsp"  method="get">
    <input type="text" name="username" value="${param.username}">
    <input type="submit"  value="提交">
</form>
<c:if test="${param.username=='admine'}" var="isadmine">
    <c:out value="true"/>
</c:if>
<c:if test="${param.username!='admine'}" var="isadmine">
    <c:out value="false"/>
</c:if>

======================================================================================
<form action="jstl标签.jsp"  method="get">
    <input type="text" name="score" value="${param.score}">
    <input type="submit"  value="提交">
</form>
<%--<c:set var="score" value="85"/>--%>
<c:choose>
    <c:when test="${param.score<60}">
        <c:out value="你不及格"/>
    </c:when>
    <c:when test="${param.score>60} && ${param.score<80}">
        <c:out value="你刚及格"/>
    </c:when>
    <c:when test="${param.score>80} && ${param.score<101}">
        <c:out value="你优秀"/>
    </c:when>
    <c:when test="${param.score>100}">
        <c:out value="分数不正确"/>
    </c:when>
</c:choose>

=====================================================================================
<%
    ArrayList<String> ls = new ArrayList<>();
    ls.add(0,"于闫闯0");
    ls.add(1,"于闫闯1");
    ls.add(2,"于闫闯2");
    ls.add(3,"于闫闯3");
    ls.add(4,"于闫闯4");
    request.setAttribute("list",ls);
%>
<c:forEach var="pp" items="${list}">
    <c:out value="${pp}"/><br>
</c:forEach>
//   var  遍历出来的iteam    iteams需要遍历的list   begin开始下表  end结束下标 step 步长
<c:forEach var="p2" items="${list}" end="3" begin="0" step="2">
    <c:out value="${p2}"/>
</c:forEach>

            for(String name:list){
    =====>    sout(name);
            }
```

