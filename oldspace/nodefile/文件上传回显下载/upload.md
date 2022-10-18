# springmvc文件操作（war包）

> 1、war包方式发布于tomcat服务器，文件系统，允许修改内部内容（将资源添加到tomcat容器，可直接访问）
>
> 2、CommonsMultipartFile工具类基本使用方法
>
> ```txt
> //获取文件名 : file.getOriginalFilename();
> String uploadFileName = file.getOriginalFilename();
> System.out.println(uploadFileName);  --mvc.png
> //获取文件的大小
> long size = file.getSize();
> System.out.println(size);  -- 159570(bit)
> //获取网络资源类型  不用以后缀的方式判断资源类型 .jpg.... image/png
> String contentType = file.getContentType();
> System.out.println(contentType);   --image/png
> ```
>
> 一般使用contentType来限制传入文件格式，反之破坏性文件
>
> 3、输入流拷贝到输出流

## 编写upload.jsp 

html也一样

```jsp
<form action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data" method="post">
    <input type="file" name="file"/>
    <input type="submit" value="upload">
</form>
```

## 编写controller

```java
//@RequestParam("file") 将name=file控件得到的文件封装成CommonsMultipartFile 对象
//批量上传CommonsMultipartFile则为数组即可
@RequestMapping(value = "/upload",method = RequestMethod.POST)
public String fileUpload(@RequestParam("file") CommonsMultipartFile file , HttpServletRequest request) throws IOException {

    //获取文件名 : file.getOriginalFilename();
    String uploadFileName = file.getOriginalFilename();
    //如果文件名为空，直接回到首页！
    if ("".equals(uploadFileName)){
    	request.setAttribute("msg","文件上传失败");
        return "redirect:/result.jsp";
    }
    System.out.println("上传文件名 : "+uploadFileName);
    //上传路径保存设置
    String path = request.getServletContext().getRealPath("/upload");
    //如果路径不存在，创建一个
    File realPath = new File(path);
    if (!realPath.exists()){
        realPath.mkdir();
    }
    System.out.println("上传文件保存地址："+realPath);

    InputStream is = file.getInputStream(); //文件输入流
    OutputStream os = new FileOutputStream(new File(realPath,uploadFileName)); //文件输出流

    //读取写出
    int len=0;
    byte[] buffer = new byte[1024];
    while ((len=is.read(buffer))!=-1){
        os.write(buffer,0,len);
        os.flush();
    }
    os.close();
    is.close();
    request.setAttribute("msg","文件上传成功");
    return "redirect:/result.jsp";
}
```

## 测试

![image-20220120040133707](upload.assets\image-20220120040133707.png)

## 访问文件

![image-20220120040228331](upload.assets\image-20220120040228331.png)

> 也是可以的。
>
> 此方式是通过，getRealPath方式来获取磁盘地址的
>
> ```java
> String path = request.getServletContext().getRealPath("/upload");
> ```

## 将它打包到linux服务器

> mvc项目没有内置tomcat其运行依赖于服务器，所以打war包

![image-20220120040828956](upload.assets\image-20220120040828956.png)

> 上传
>
> 1、scp fileUpload root@47.103.215.51:up
>
> 2、xftp

> 启动tomcat  ./startup.sh  
>
> 端口已开  8080

## 测试

![image-20220120042036687](upload.assets\image-20220120042036687.png)

![image-20220120042047666](upload.assets\image-20220120042047666.png)

![image-20220120042057815](upload.assets\image-20220120042057815.png)

> 都是可以的
>
> ps-ef|grep tomcat      查看有关tomcat的服务
>
> kill-9 pid  杀死服务
>
> firewall-cmd --list-port  查看防火墙开启的端口

## 当然你也可以使用transferTo直接写入，内部也是输入流输出流拷贝

```java
String path = request.getServletContext().getRealPath("/upload");
File realPath = new File(path);
if (!realPath.exists()){
    realPath.mkdir();
}
//上传文件地址
System.out.println("上传文件保存地址："+realPath);
//通过CommonsMultipartFile的方法写入文件
file.transferTo(new File(realPath +"/"+ file.getOriginalFilename()));
```

## 文件下载操作

> controller



```java
@RequestMapping(value="/download")
@ResponseBody
public String downloads(HttpServletResponse response , HttpServletRequest request) throws Exception{
    //要下载的图片地址
    String  path = request.getServletContext().getRealPath("/upload");
    String fileName = request.getParameter("fileName");
    //1、设置response 响应头
    response.reset(); //设置页面不缓存,清空buffer
    response.setCharacterEncoding("UTF-8"); //字符编码
    response.setContentType("multipart/form-data"); //二进制传输数据
    //设置响应头 解决弹出框中文乱码问题
    response.setHeader("Content-Disposition",
            "attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));

    File file = new File(path,fileName);
    if(!file.exists()){
        return "资源不存在";
    }
    //2、 读取文件--输入流
    InputStream input=new FileInputStream(file);
    //3、 写出文件--输出流
    OutputStream out = response.getOutputStream();

    byte[] buff =new byte[1024];
    int index=0;
    //4、执行 写出操作
    while((index= input.read(buff))!= -1){
        out.write(buff, 0, index);
        out.flush();
    }
    out.close();
    input.close();
    return null;
}
```

![image-20220120044158396](upload.assets\image-20220120044158396.png)

# springboot操作文件

> ***首先测试使用getRealPath的方式获取下载路径，及资源获取***

```java
@RequestMapping("/upload")
public String upload(HttpServletRequest req, MultipartFile file){
    String realPath = req.getServletContext().getRealPath("/upload");

    System.out.println(realPath);
    //获取文件名
    //String originalFilename = file.getOriginalFilename();
    return realPath;
}
```

![image-20220120044945026](upload.assets\image-20220120044945026.png)

> 这个路径可以看作我们window下的运行环境，很明显并不是我们想要的

> 我们把它打包到linux上
>
> nohup  java -jar demo.jar 

![image-20220120045841681](upload.assets\image-20220120045841681.png)

> 这个是我们linux的工作空间（临时文件），不太建议修改linu的文件目录，liunx一切皆文件
>
> 把他删了。换种方式

## 获取项目父级目录

```
String root = System.getProperty("user.dir");
```

```java
@RequestMapping("/upload2")
@ResponseBody
public String upload2(HttpServletRequest req, MultipartFile file) throws IOException {
    //使用boot-start-web自带的json序列化工具封装返回结果
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> map = new HashMap<>();
    if (file.isEmpty()) {
        map.put("statu", 500);
        map.put("msg", "文件为空，请选择文件！");
        objectMapper.writeValueAsString(map);
    }
    //获取文件名
    String originalFilename = file.getOriginalFilename();
    map.put("文件名：", originalFilename);
    //文件类型
    String contentType = file.getContentType();
    map.put("文件类型：", contentType);
    //文件大小
    long size = file.getSize();
    map.put("文件大小：", size);
    //获取当前项目  父级目录
    String root = System.getProperty("user.dir");
    File img = new File(root, "img");
    if (!img.exists()) {
        img.mkdirs();
    }
    //唯一性处理 这里简单 （雪花算法。。。。）
    long time = new Date().getTime();
    String uuid = UUID.randomUUID().toString().replace("-", "");
    String fileName = time + "-" + uuid + "-" + originalFilename;
    map.put("文件按存储名：",fileName);
    //File.separator  文件分割符 =》  '/'
    file.transferTo(new File(root+File.separator+"img"+File.separator,fileName));
    map.put("真实存储路径：",root+File.separator+"img"+File.separator+fileName);
    return objectMapper.writeValueAsString(map);
}
```

![image-20220120053816912](upload.assets\image-20220120053816912.png)

> 但是该资源还是只能通过全路径去访问

![image-20220120054325823](upload.assets\image-20220120054325823.png)

> 配置静态资源过滤，也叫映射

```java
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:"+"E:/programmeTools/idea/devspace/fileupload/img/");
    }
}
```

> 这样就可以拿到图片了

![image-20220120060011744](upload.assets\image-20220120060011744.png)

> 放入服务器试试

第一种：路径还是一样的

![image-20220120060423351](upload.assets\image-20220120060423351.png)

测试正常的：

![image-20220120060545981](upload.assets\image-20220120060545981.png)





。。。。。。资源映射没改，不测了结束！













```
@RequestMapping("/dl")
@ResponseBody
public String download(HttpServletResponse resp) {

    //获取项目根路径
    String root = System.getProperty("user.dir");
    //文件名
    String fileName = "开发相关资料.txt";
    File file = new File(root+"/img", fileName);
    if (!file.exists()) {
        //文件不存在
        throw new RuntimeException("文件不存在");
    }
    try {
        //1、设置response 响应头
        resp.reset(); //设置页面不缓存,清空buffer
        resp.setCharacterEncoding("UTF-8"); //字符编码
        resp.setContentType("multipart/form-data"); //二进制传输数据
        //设置响应头 解决弹出框中文乱码问题
        resp.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));

        //2、 读取文件--输入流
        InputStream input =  new FileInputStream(file);

        //3、 写出文件--输出流
        OutputStream out = resp.getOutputStream();

        byte[] buff = new byte[1024];
        int index = 0;
        //4、执行 写出操作
        while ((index = input.read(buff)) != -1) {
            out.write(buff, 0, index);
            out.flush();
        }
        out.close();
        input.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    return "success";
}
```