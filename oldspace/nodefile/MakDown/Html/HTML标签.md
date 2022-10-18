# HTML

## Hype Text Markup Language

# 页面属性标签 ()

```html
<!DOCTYPE html>  规范型标签  表示该页面遵循  html规范  
<head>
    <meta charset = "utf-8">  描述型标签  用来描述这个页面是干嘛用的
    <meta name = "name" content="我的第一个网页">
    <meta name = "descripetion" content="学习网页">
    <title>Title</title>//网页标题
</head>
<body>
    页面内容展示
</body>
</html>
```

```html
标题标签
<h1>一级</h1>
<h2>二级</h2>
<h3>三级</h3>
段落
<p></p>
换行
<br/>
水平线
<hr/>
粗体
<strong>text</strong>
斜体
<em>text</em>
大于号小于号
&gt  &lt
空格
&nbsp
版权：
&@copy

tips:这些标签分为 自闭和标签 与 开标签&闭标签
其中标题  段落可自动换行
```

```html
 列表：
1 有序列表：
<ol>
    <li>a</li>
    <li>b</li>
</ol>

2 无序列表
<ul>
    <li>a</li>
    <li>b</li>
</ul>

3 自定义列表   常用于网页底部信息说明
<dl>
    <dt>标题</dt>
    <dd>列表元素</dd>
    <dd>列表元素</dd>
</dl>

表格
<table border = "1px">
    <tr>
        <td colspan = '3' rowspan='1'></td>占一行三列
        <td></td>
    </tr>
    <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>    

</table>
```

```html
a标签 超链接  可跳转1、本地页面 2、外部网址 3、页面位置调整
//blank  self
<a href="https:\\www.baidu.com" target="_blank">点击跳转百度</a>
<a href="from.html" target="_blank">点击跳转from.html</a>
//锚标签
<lable name = "top">顶部</lable>
<a href = "#top">点击到顶部</a>

图片标签
<img src ="path" alt="图片加载错误显示文字" title="鼠标悬停在图片上显示文字">
a标签嵌套图片标签
<a src="src" target = "_blank">
    <img src="path" alt="" title="">
</a>
```

```html
medio 和audio
//视频和音乐  controls控制条   autoplay自动播放
<!--<video src="../resources/Video/a.mp4" controls autoplay> </video>-->

<audio src="../resources/Audio/a.m4r" controls autoplay></audio>

```

```html
表单from
<from action="" method="post">
    //type :text password radio checkbox button img select textarea file
     <p>名字：<input type="text" name="username" value="username" readonly></p>
    <p>密码：<input type="password" name="password"></p>
    <p><input type="submit" value="提交">
    <input type="reset" value="chongzhi"></p>
    <p>
        nan<input type="radio" value="man" name="sex">
        nv<input type="radio" value="woman" name="sex">
    </p>
    <p>

        <input type="checkbox" value="sleep" name = "hobby">睡觉<br/>
        <input type="checkbox" value="game" name = "hobby">打游戏<br/>
        <input type="checkbox" value="ball" name = "hobby">打球<br/>
    </p>
    <p>
        <input type="button" value="点击" name="btn1">
<!--        <input type="image"  src="../resources/img/img.png">-->
    </p>
    <p>下拉框:
        <select name = "列表名称">
            <option value="china">china</option>
            <option value="us">us</option>
            <option value="dk">dk</option>
            <option value="ddddd">ddd</option>
        </select>
    </p>
    <p>
        <textarea rows="10" cols="50" name="textarea">文本域</textarea>
    </p>
    <p>
    <input type="file" name="filename" >

    </p>
    <h1>带验证的标签</h1>
    <p>
        邮箱：
        <input type="email" name="email" >

    </p>

    <p>
        url:
        <input type="url" name="url" >

    </p>
    <p>
        数字:
        <input type="number" name="number" max="100" min = "10" step=" 1" >

    </p>

    <p>
        滑块:
        <input type="range" name="voice" value="10" min="0" max="100" step="2">
    </p>

    <p>
        搜索:
        <input type="search" name="search" value="java">
    </p>
    <p>
        增强鼠标:
        <label for="mark" ><strong>点击跳到input</strong></label>
        <input id = "mark" name="mark">
    </p>

</from>
```

```html
简单的验证
<!--减轻服务器压力-->
<!--required  非空-->
<!--placeholder-->
<!--pattern  正则表达式-->
<p>
    name:&nbsp&nbsp&nbsp&nbsp<input name="username" placeholder="输入姓名。。。" required>
</p>
```

[点击查看正则表达式](https://www.jb51.net/tools/regexsc.htm)

密码/^[a-z0-9_-]{6,18}$/

a-z  0-9   6到18位