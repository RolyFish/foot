# Servlet

***实现了sun公司的Servlet接口的web程序叫做Setvlet***

XXXservlet   extends  HttpServlet   extends  GenericHttpservlet  immp Servlet

```bash
servlet接口有一个service抽象方法  
GenericServlet抽象类中使用abstract 修饰了该抽象方法但是未实现
HttpServlet 实现了service方法，简单含义就是什么请求对应什么方法
自己创建的XXXservlet继承HttpServlet（servlet实现类），并重写dogetdopost等方法  
```

## servlet原理

```java
浏览器发送请求->web服务器（容器)
    if(首次访问){
        生成servlet.class文件
    }
	服务器处理http请求->调用Servlet的service方法即HttpServlet的service实现方法，处理请求，调用对应的方法（dopost doget），而这些请求方法会在我们自己编写的XXXservlet重写，及我们需要实现对应的请求方法，就只需实现Httpservlet的dopost  doget方法即可。最后将响应放在response中返回。    
```

## mapping

映射路径对应servlet

- 指定一个mapping
- 指定多个mapping
- 指定通配符
- 自定义文件后缀           *.yyc  *.do   只要是以do结尾的都走这个setvlet

***tips:但是mapping不可以有/在启动路径上，但是直接在url上可以随便加

```xml
<servlet-mapping>
    <servlet-name>HelloServlet</servlet-name>
    <url-pattern>*.do</url-pattern>
</servlet-mapping>

error:
<servlet-mapping>
    <servlet-name>HelloServlet</servlet-name>
    <url-pattern>/*.do</url-pattern>
</servlet-mapping>
<servlet-mapping>
    <servlet-name>HelloServlet</servlet-name>
    <url-pattern>/hello/*.do</url-pattern>
</servlet-mapping>
```

## context

```java
System.out.println("进入doget");
//        this.getInitParameter()   初始化参数 web.xml initparam
//        this.getServletConfig()   servlet配置
//        this.getServletContext()   servlet上下文
        System.out.println(this.getInitParameter("user"));
        System.out.println(this.getServletConfig());
        ServletContext servletContext = this.getServletContext();

        System.out.println("context  数据放入");
        servletContext.setAttribute("username","于延闯");

```

```java
ServletContext context = this.getServletContext();
        Object username = context.getAttribute("username");
        System.out.println("取出context数据");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter writer = resp.getWriter();
        writer.print("<h1>"+username+"</h1>");
        System.out.println(username);
```

```java
 ServletContext context = this.getServletContext();
        String user = context.getInitParameter("user");
        System.out.println(user);

<context-param>
        <param-name>user</param-name>  //jdbc
        <param-value>大窗</param-value>//url
</context-param>
```

## 请求转发

```java
ServletContext context = this.getServletContext();
context.setAttribute("username","于延闯dad");
resp.setContentType("text/html");
resp.setCharacterEncoding("utf-8");
PrintWriter writer = resp.getWriter();
writer.print("数据放入context,等待三秒进入s2");
RequestDispatcher reqdis = context.getRequestDispatcher("/s2");
reqdis.forward(req,resp);
转发到s2，路径不变
```

## web获取配置文件的方法--

```java
Properties prop = new Properties();
InputStream inputs = this.getServletContext().getResourceAsStream("/WEB-INF/classes/db.properties");
prop.load(inputs);
resp.setContentType("text/html");
resp.setCharacterEncoding("utf-8");
PrintWriter writer = resp.getWriter();
writer.write("<h1>"+"properties文件的配置参数为："+"</h1>");
writer.write(prop.getProperty("username"));
```

## request   response

### response

```txt
状态码：
int SC_CONTINUE = 100;
int SC_SWITCHING_PROTOCOLS = 101;
int SC_OK = 200;
int SC_CREATED = 201;
int SC_ACCEPTED = 202;
int SC_NON_AUTHORITATIVE_INFORMATION = 203;
int SC_NO_CONTENT = 204;
int SC_RESET_CONTENT = 205;
int SC_PARTIAL_CONTENT = 206;
int SC_MULTIPLE_CHOICES = 300;
int SC_MOVED_PERMANENTLY = 301;
int SC_MOVED_TEMPORARILY = 302;
int SC_FOUND = 302;
int SC_SEE_OTHER = 303;
int SC_NOT_MODIFIED = 304;
int SC_USE_PROXY = 305;
int SC_TEMPORARY_REDIRECT = 307;
int SC_BAD_REQUEST = 400;
int SC_UNAUTHORIZED = 401;
int SC_PAYMENT_REQUIRED = 402;
int SC_FORBIDDEN = 403;
int SC_NOT_FOUND = 404;
int SC_METHOD_NOT_ALLOWED = 405;
int SC_NOT_ACCEPTABLE = 406;
int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
int SC_REQUEST_TIMEOUT = 408;
int SC_CONFLICT = 409;
int SC_GONE = 410;
int SC_LENGTH_REQUIRED = 411;
int SC_PRECONDITION_FAILED = 412;
int SC_REQUEST_ENTITY_TOO_LARGE = 413;
int SC_REQUEST_URI_TOO_LONG = 414;
int SC_UNSUPPORTED_MEDIA_TYPE = 415;
int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
int SC_EXPECTATION_FAILED = 417;
int SC_INTERNAL_SERVER_ERROR = 500;
int SC_NOT_IMPLEMENTED = 501;
int SC_BAD_GATEWAY = 502;
int SC_SERVICE_UNAVAILABLE = 503;
int SC_GATEWAY_TIMEOUT = 504;
int SC_HTTP_VERSION_NOT_SUPPORTED = 505;
```

response常见应用

1. resp.getwriter()  向html页面输出东西
2. 下载文件
   1. 获取下载文件的路径
   2. 获取文件名
   3. 获取输入流
   4. 创建缓冲区
   5. 获取outputstream  输出流
   6. 将FileoutputStream  流写入到缓冲区
   7. 用outputstream  将缓冲区数据输出到客户端

```java
 		String realPath = "E:\\ProgrammingTools\\ideal\\workspace2\\javaweb-01-Servlett\\response\\src\\main\\resources\\1.png";
        System.out.println("文件路径："+realPath);
        String filename = realPath.substring(realPath.lastIndexOf("\\") + 1);
        System.out.println("文件名："+filename);
        resp.setHeader("Content-disposition","attachment;filename="+filename);
        FileInputStream in = new FileInputStream(realPath);
        byte[] buffer = new byte[1024];
        ServletOutputStream out = resp.getOutputStream();
        int len = 0;
        while ((len = in.read(buffer))>0){
            out.write(buffer,0,len);
        }
in.close();
out.close();
=================================================
解决中文文件不显示文件名的问题
=================================================
        String realpath = "E:\\ProgrammingTools\\ideal\\workspace2\\javaweb-01-Servlett\\response\\src\\main\\resources\\图片.png";
        System.out.println("文件路径：" + realpath);
        String filename =  realpath.substring(realpath.lastIndexOf("\\")+1);
        System.out.println("文件名：" + filename);

        resp.setHeader("Content-Disposition", "attachment;fileName="+ URLEncoder.encode(filename,"utf-8"));
        FileInputStream in = new FileInputStream(realpath);
        byte[] buffer = new byte[1];
        int len = 0;
        ServletOutputStream out = resp.getOutputStream();
        while ((len = in.read(buffer)) > 0){
            out.write(buffer,0,len);
        }

        in.close();
        out.close();

```

