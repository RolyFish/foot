# jdbc

## 数据库驱动

使得应用程序可以和数据库打交道

jdbc   ：java操作数据库的规范

> connection  statement（prepStatement） ResultSet   param

```java

 //1、加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 2、用户信息和  url  支持中文编码   utf8编码  安全链接
        String url = "jdbc:mysql://localhost:3306/student?							  			useUnicode=true&characterEncoding=utf8&useSSL=true";
        String username = "root";
        String password = "123";
        //3、连接数据库
        Connection connection = DriverManager.getConnection(url, username, password);
        //4、执行sql对象  statement
        Statement statement = connection.createStatement();
        //5、执行sql
        String sql = "SELECT * FROM teacher";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println(resultSet.getObject("name"));
        }
        //6、释放资源
        resultSet.close();
        statement.close();
        connection.close();
```

![](D:\File\Desktop\MakDown\mysql\jdbc.assets\image-20220104124453404.png)

> Class.forName("com.mysql.jdbc.Driver");做的事
>
> static代码块只有在主动引用的时候才会执行。这边呢通过反射加载驱动，所以加载有效

```sql
Driver:
//DriverMannager.registerDriver(new Driver());
class.forname('com.mysql.jdbc.Driver')
url:
jdbc:mysql://localhost:3306/student?useUnicode=true&characterEncoding=utf8&useSSL=true
Connection connection = DriverManager.getConnection(url, username, password);
connection  代表数据库可操作性数据库自动提交  
        //connection.commit();
        //connection.rollback();
        //connection.setAutoCommit(true);
statement：执行sql的对象

//        statement.execute();//执行所有sql  返回resultset
//        statement.executeQuery();//查询
//        statement.executeUpdate()//更新
resultset：  查询结果集
resultset.beforefirst
resultset.afterlaster
resultset.next()  遍历
//        resultSet.getObject();
//        resultSet.getString(）;
//        resultSet.getInt();
。。。。。。。。。。。。。。。                           
close()                   
```

## statement

```java
statement  将sql发送给数据库
```

## sql注入的问题

```sql
有心者通过多输入sql语句的方法来获取数据。
SELECT * FROM `teacher` WHERE `name` = ' ' OR '1=1'  AND `pass` = ' ' OR '1=1'

  public static void login(String username , String pass){
        Connection conn = null;
        Statement state = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConn();
            state = conn.createStatement();
            //SELECT * FROM `teacher` WHERE `name` = '与牙齿' AND `pass` = ''
            String sql = "SELECT * FROM `teacher` WHERE `name` = "+username+"and `pass` = "+pass;
            rs = state.executeQuery(sql);
            while (rs.next()) {
                System.out.println("name:" + rs.getObject("name") + "\n" +
                        "pass:" + rs.getObject("pass")+"===================");
            }
        } catch (SQLException e) {
            JdbcUtil.release(conn,state,rs);
        }

    }
   
login(" '' or '1= 1' "," '' or '1=1'");
```

### preparestatement

> 预编译（占位符？）将传进来的参数 用‘’括起来 当做字符串  传入什么就是什么,忽略转义字符

```sql
//预编译
        Connection conn = null;
        PreparedStatement preparedState = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConn();
            String sql = "INSERT INTO `account`(`name`,`momey`) VALUES(?,?)";
            preparedState = conn.prepareStatement(sql);//预编译
            preparedState.setObject(1, "b");
            preparedState.setObject(2, 101111);
            int i = preparedState.executeUpdate();

            if(i>0){
                System.out.println("scuess");
            }else {
                System.out.println("failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.release(conn, preparedState, rs);
        }
        
***//preparestatement  把传进来的参数 用‘’括起来 当做字符  其中有''被当作为转义字符 ***

```

# jdbc操作事务

```java
java实现转账  事务
 Connection conn = null ;
        PreparedStatement pstate = null;
        ResultSet rs =null;
        try {
            conn = JdbcUtil.getConn();
            conn.setAutoCommit(false);//关闭自动提交事务
//            conn.commit();
            String sql1 = "update `account` set `momey` = `momey`-99 where `name`= ?";
            pstate = conn.prepareStatement(sql1);
            pstate.setObject(1,"a");
            pstate.executeUpdate();
//            int a = 2/0;
            String sql2 = "update `account` set `momey` = `momey`+99 where `name`= ?";
            pstate = conn.prepareStatement(sql2);
            pstate.setObject(1,"b");
            pstate.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try {
                System.out.println("失败 回滚");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JdbcUtil.release(conn,pstate,rs);
        }    
```

# 数据库连接池

DateSource  实现这个接口

DBCP

C3p0

Druid

```java
package util;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
public class JdbcPoolUtil {
    private  static  DataSource dataSource;
    //静态代码块  读取数据信息
    static{
        try {
            InputStream inputStream = JdbcPoolUtil.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            dataSource = BasicDataSourceFactory.createDataSource(properties);//数据源获取配置文件按信息
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConn() throws SQLException {
        return dataSource.getConnection();
    }

    public static void release(Connection conn, Statement state , ResultSet result) {
        if(result!=null){
            try {
                result.close();
            } catch (SQLException e) {
                System.out.println("关闭结果集资源失败");
            }
        }if(state!=null){
            try {
                state.close();
            } catch (SQLException e) {
                System.out.println("关闭state sql执行资源失败");
            }
        }if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("关闭连接资源失败");
            }
        }

    }
}
tips:  从数据源获取连接 实现的datesource接口 驱动自动加载  内部执行class.forname反射加载驱动
dbcpconfig.properties里的driverClassName死的不能动！！！！
```

```properties
#dbcpconfig.properties配置文件
#连接设置
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/student?useUnicode=true&charactEncoding=utf8&useSSL=true
username=root
password=123
#<!-- 初始化连接 -->
initialSize=10
#最大连接数量
maxActive=50
#<!-- 最大空闲连接 -->
maxIdle=20
#<!-- 最小空闲连接 -->
minIdle=5
#<!-- 超时等待时间以毫秒为单位 6000毫秒/1000等于60秒 -->
maxWait=60000
#JDBC驱动建立连接时附带的连接属性属性的格式必须为这样：[属性名=property;] 
#注意："user" 与 "password" 两个属性会被明确地传递，因此这里不需要包含他们。
connectionProperties=useUnicode=true;characterEncoding=utf8
#指定由连接池所创建的连接的自动提交（auto-commit）状态。
defaultAutoCommit=true
#driver default 指定由连接池所创建的连接的事务级别（TransactionIsolation）。
#可用值为下列之一：（详情可见javadoc。）NONE,READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE
defaultTransactionIsolation=READ_UNCOMMITTED

```

### c3p0

```xml
<!--c3p0配置文件-->
<c3p0-config>
    <name-config name="mysql">
        <property name="driverClass">com.mysql.jdbc.Driver</property>
        <property name="jdbcUrl">jdbc:mysql://localhost:3306/student?useUnicode=true&amp;charactEncoding=utf8&amp;useSSL=true</property>
        <property name="user">root</property>
        <property name="password">123</property>

        <property name="initialPoolSize">10</property>
        <property name="maxIdleTime">30</property>
        <property name="maxPoolSize">100</property>
        <property name="minPoolSize">10</property>
        <property name="maxStatements">200</property>
    </name-config>
    <default-config >
        <property name="driverClass">com.mysql.jdbc.Driver</property>
        <property name="jdbcUrl">jdbc:mysql://localhost:3306/student?useUnicode=true&amp;charactEncoding=utf8&amp;useSSL=true</property>
        <property name="user">root</property>
        <property name="password">123</property>

        <property name="initialPoolSize">10</property>
        <property name="maxIdleTime">30</property>
        <property name="maxPoolSize">100</property>
        <property name="minPoolSize">10</property>
        <property name="maxStatements">200</property>
    </default-config>
</c3p0-config>


```



```java
工具类  !!!!!!!!文件名  要和配置文件标签名一致  c3p0-config.xml!!!!
private static DateSource datesoure;
datesource = new ComboPooledDataSource("mysql");//参数不设置 默认的那个标签

测试就是一样的
```

==总结==

```text
无论数据源怎么不同，其数据库对象conn的接口不变，所以只需修改数据源的配置文件按和数据源的映入方法即可！！！
getconn()

dbcp：数据源获取方式
InputStream inputStream = JdbcPoolUtil.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
Properties properties = new Properties();
properties.load(inputStream);
dataSource = BasicDataSourceFactory.createDataSource(properties);

c3p0：数据源获取方式
Datasource = new ComboPooledDatascource("mysql")；
```

