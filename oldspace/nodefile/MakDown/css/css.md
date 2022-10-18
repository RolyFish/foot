# css基础

css用于修饰前台页面。

语法：

```css
<style>
    h1{
        color:red;
    }
</style>
<lable style="color:red;">yyds</lable>
```

## css引入方式

1. 内部    在<head>中定义style

2. 外部1   在外部定义css文件  用<link rel=" " href="">

3. 外部2    

   ```css
   <style> 
         @import url("../CSS/CssDemo01.css");--> <!--导入式-->
   </style>
   ```

***tips：优先级***

就近原则。这也是css可实现高复用的原因。

## css选择器

### 基本选择器

```css
<!--标签选择器选择所有标签-->
<!--类选择器-->  .class
<!--id选择器-->   #id
```

***tips:优先级***：id>class>标签

### 层次选择器

```css
1. 后代选择器 
body p{  选择body下所有的p标签
}
2.子选择器
body>p{   选择body下一级的所有p标签
}
3. 相邻兄弟选择器
.id+p{     id为id的标签下的同级的第一个p标签    
}
4.通用选择器 
.id~p{      向下所有
}
```

### 属性选择器

```css
a[id] => a[id!=null]  所有  有id属性字段的a标签
a[id = '2']   id =2 的所有a标签
[href^=https] 以https开头
[href$=.com]   以.com结尾

```

### 结构伪类选择器

点击[伪类选择器](https://blog.csdn.net/u010297791/article/details/53968089)阅读

## css的一些样式

```css
<!--字体样式
    font-family
    font-size
    color
    font-weight
    font:
-->
<!--文本样式  rgba  红绿蓝透明
    color
    text-align
    text-indent  em
    height(块高度)
    line-height 行高
-->

```

