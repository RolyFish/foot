# 依赖

```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/junit/junit -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
    </dependency>

    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>
        <version>2.5</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>javax.servlet.jsp-api</artifactId>
        <version>2.3.3</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet.jsp.jstl</groupId>
        <artifactId>jstl-api</artifactId>
        <version>1.2</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.apache.taglibs/taglibs-standard-impl -->
    <dependency>
        <groupId>org.apache.taglibs</groupId>
        <artifactId>taglibs-standard-impl</artifactId>
        <version>1.2.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.47</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.78</version>
    </dependency>
</dependencies>
```

# web.xml的配置

所有的welcomepage，filter，servlet都在这里面

```xml
<!--设置欢迎页面-->
<welcome-file-list>
    <welcome-file>login.jsp</welcome-file>
</welcome-file-list>
<!--session-->
<session-config>
    <session-timeout>30</session-timeout>
</session-config>
<!--配置乱码过滤器-->
<filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>com.roily.filter.CharacterEncodingFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>

<!--配置登录过滤器-->
<filter>
    <filter-name>loginFilter</filter-name>
    <filter-class>com.roily.filter.LoginFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>loginFilter</filter-name>
    <url-pattern>/jsp/*</url-pattern>
</filter-mapping>

<!--登录功能-->
<servlet>
    <servlet-name>loginServlet</servlet-name>
    <servlet-class>com.roily.servlet.user.LoginServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>loginServlet</servlet-name>
    <url-pattern>/login.do</url-pattern>
</servlet-mapping>
<!--退出功能-->
<servlet>
    <servlet-name>logoutServlet</servlet-name>
    <servlet-class>com.roily.servlet.user.logoutServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>logoutServlet</servlet-name>
    <url-pattern>/logout.do</url-pattern>
</servlet-mapping>
<!--用户操作-->
<servlet>
    <servlet-name>userServlet</servlet-name>
    <servlet-class>com.roily.servlet.user.userServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>userServlet</servlet-name>
    <url-pattern>/jsp/user.do</url-pattern>
</servlet-mapping>

<!--供应商操作-->
<servlet>
    <servlet-name>providerServlet</servlet-name>
    <servlet-class>com.roily.servlet.provider.providerServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>providerServlet</servlet-name>
    <url-pattern>/jsp/provider.do</url-pattern>
</servlet-mapping>
<!--订单管理-->
<servlet>
    <servlet-name>billServlet</servlet-name>
    <servlet-class>com.roily.servlet.bill.billServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>billServlet</servlet-name>
    <url-pattern>/jsp/bill.do</url-pattern>
</servlet-mapping>
<!--test-->
<servlet>
    <servlet-name>test</servlet-name>
    <servlet-class>com.roily.servlet.user.testservlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>test</servlet-name>
    <url-pattern>/test.do</url-pattern>
</servlet-mapping>
```

# 数据库配置文件

```properties
driver=com.mysql.jdbc.Driver
#编码，安全链接，unicode
url=jdbc:mysql://localhost:3306/smbms?useUnicode=true&charactEncoding=utf8&useSSL=true
username=root
password=123
```

# 工具类

## pagesupport

```java
//当前页码-来自于用户输入
private int currentPageNo = 1;
//总数量（表）
private int totalCount = 0;
//页面容量
private int pageSize = 0;
//总页数-totalCount/pageSize（+1）  可以算不用这个字段
private int totalPageCount = 1;
```

# 过滤器

## chareactEncoding：并不好

```java
public class CharacterEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("字符编码过滤器加载。。。。");
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        //放行
        filterChain.doFilter(req,resp);
    }
    @Override
    public void destroy() {

    }
}
```

## loginFilter

```java
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("登录过滤器加载。。。。");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        //判断用户是否登录  判断session是否为空
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        SmbmsUser user = (SmbmsUser) request.getSession().getAttribute(Constants.USER_SESSION);
        if(user!=null){
            filterChain.doFilter(req,resp);
        }else {
            response.sendRedirect(((HttpServletRequest) req).getContextPath()+"/syserror.jsp");
        }
    }

    @Override
    public void destroy() {

    }
}
```

# baseDao

```java
public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    static{
        Properties prop = new Properties();
        //通过类加载器读取资源文件
        //static代码块只加载一次，文件io耗费资源，数据库配置文件就放这里
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = prop.getProperty("driver");
        url = prop.getProperty("url");
        username = prop.getProperty("username");
        password = prop.getProperty("password");
    }
    //获取连接
    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    //编写查询公共类
    public static ResultSet execute(Connection connection,PreparedStatement pstm,ResultSet rs,String sql,Object[] params)throws Exception{
        pstm = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstm.setObject(i+1,params[i]);
        }
        rs=pstm.executeQuery();
        return rs;
    }
    //编写查询公共类
    public static ResultSet execute(Connection connection,PreparedStatement pstm,ResultSet rs,String sql)throws Exception{
        pstm = connection.prepareStatement(sql);
        rs=pstm.executeQuery();
        return rs;
    }

    //编写update公共方法
    public static int execute(Connection conn, String sql, Object[] parms, PreparedStatement pstate) throws SQLException {
        pstate = conn.prepareStatement(sql);
        for (int i =0; i<parms.length;i++){
            pstate.setObject(i+1,parms[i]);
        }
        int updateRows = pstate.executeUpdate();
        return updateRows;
    }
    //释放资源
    public static boolean release(Connection conn, PreparedStatement pstate, ResultSet resultSet){
        Boolean flag = true;
        if(resultSet!=null){
            try {
                resultSet.close();
                //Gc回收
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if(pstate!=null){
            try {
                pstate.close();
                //Gc回收
                pstate = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if(conn!=null){
            try {
                conn.close();
                //Gc回收
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
```

# 请求分发！！！

这里如果每一个请求都要对应一个servlet，累死，web.xml也会烦死。

因为这边有四大类请求，所以可以通过url携带method参数的方式进行请求的分发！！！

```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String method = req.getParameter("method");
    if (!StringUtils.isNullOrEmpty(method)) {
        if (method.equals("savepwd")) {
            this.pwdModify(req, resp);
        }
        else if (method.equals("pwdcheck")) {
            this.pwdcheck(req,resp);
        }
        else if(method.equals("query")){
            this.userQuery(req,resp);
        }
        else if(method.equals("getrolelist")){
            this.getrolelist(req,resp);
        }
        else if(method.equals("add")){
            try {
                this.addUser(req,resp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(method.equals("ucexist")){
            this.usCheck(req,resp);
        }
        else if(method.equals("deluser")){
            this.deluser(req,resp);
        }
        else if(method.equals("view")){
            this.view(req,resp);
        }
        else if(method.equals("modify")){
            this.modify(req,resp);
        }
        else if (method.equals("modifyexe")) {
            try {
                this.modifyexe(req, resp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    } else {
        req.setAttribute(Constants.USER_Message, "错误信息 获取数据失败 尝试重新登录。。。");
        req.getRequestDispatcher(req.getContextPath() + "/error.jsp").forward(req, resp);
    }
}
```

