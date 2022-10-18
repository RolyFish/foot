# jquery

> jquery就是一个js函数库。
>
> 提供很多操作bom对象的方法，以及封装了发送ajax请求的方法。

# ajax

> 异步请求，刷新部分页面提高用户体验。

# 操作

- 下载jqury文件

> jquery-3.6.0.min.js

- 创建maven项目

- 导入依赖，配置maven打包静态资源过滤

- mvc配置文件，配置mvc忽略静态资源

- 写请求

  - ```java
    @RequestMapping(value = "/ajax1",method = RequestMethod.POST)
    @ResponseBody
    public String ajax1() {
        return "请求成功";
    }
    ```

- 写前端页面

  - ```jsp
    
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.6.0.js"></script>
    <script>
        function b(){
            $.post({
            url:"${pageContext.request.contextPath}/ajax1",
            success:function (data) {
                 alert(data);
                  }
                })
           }
          </script>
      <body>
      <p>
          <input type="text" id="ajax" onblur="b()">
      </p>
      </body>
    </html>
    ```

    

- 测试  一个xhr请求，且响应了
  - ![image-20220110181457860](D:\File\Desktop\总结理解\springmvc\ajax.assets\image-20220110181457860.png)

> ajax参数说明
>
> url：请求url
>
> type：请求类型
>
> data：参数（json格式）
>
> success、error：回调函数

```java
function c() {
    $.ajax({
        url: "${pageContext.request.contextPath}/ajax2",
        type: "post",
        data: {"name":$("#ajax2").val().trim()},
        success: function (data) {
            alert(data);
        },
        error: function () {
            alert("ajax请求失败");
        }
    })
}
```

