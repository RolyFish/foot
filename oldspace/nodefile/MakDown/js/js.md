# javaScript

### 函数的定义和引用

```javascript
方式一：
function abs(){
    if(x>0) return x;
    else return -x;
}
方拾二：
var abs = function (x){
    if(x>0) return x;
    else return -x;
}
调用  abs(x)
javascript对于参数无要求,因为这里没有类型 只会报nan
tips:function abs(a,...rest)
	rest  可接受所有除了a的其他参数
```

### 作用域问题

> var 		
>
>  let   	局部变量（for循环）
>
> const  定义常量

```
js函数体内的变量只能在函数体内用，内部函数可以用外部函数的变量。
tips：window作为整个网页窗口的全局对象。
	自己也可以定义全局对象  var yyc = {}；
	jqury$就是自定义的全局对象
======================================================
var  let const
let解决var局部变量的问题
例子：
var demo = function(){
	for(var i =0;i<10;i++){
		console.log(i);
	}
	console.log(i);//这个i会输出啥呢？
}
结果是：10。也就是说这个i属于demo这个方法而不是属于for循环。
解决   var->let

以前定义常量是  var PI="3.14";大写
现在用  const pt = "3.14";
```

### js  this的问题

```java
js函数中  this和其调用者有关和函数本身无关
举个栗子
函数：fn1
function fn1(){
    console.log(this.a)
}
函数：fn2
var fn2 = function(a){
    console.log(this.a)
}
对象：obj
var obj = {
    a:22,
    fn1:fn1,
    fn2:fn2
}

obj.fn1() =>  22
obj.fn2(11) =>22

```

### BOM

```javascript
bom常用对象
1、window   窗口对象
2、navigator 浏览器对象  封装了浏览器的相关信息
3、screen 屏幕  尺寸
4、location location.assign('https://www.baidu.com')
5、host
。。。。。。。。。。。。。。。
```

### dom

```javas
document object model 文档对象模型
浏览器就是第一个dom树由各种节点组成的树型结构
1、得到dom树节点 （和css选择器一样）
    doucment.getElementById('#id')
    doucment.getElementByTagName('tag')
    document.getElementByClassName('.class')
2、删除dom树节点
	找到父节点->删除子节点
	dom.getbyid('#id').parent()
	parent.removechild(节点对象)
3、更新dom树节点
	node =dom.getByID('#id')
	//覆盖
	node.innerText = '';
	node.innerHtml = '';
	//追加
	node.innerHtml+='';
	node.appendChild(节点对象);
4、添加dom树节点
	node.appendChild(节点对象);

```



### jqury-简单

```javas
1、jquery：一个js方法库。
2、$：jQuerya自定义的全局变量（var $ = {}）
3、jquery 使用格式
	$('选择器')方法:	$('#id')action

```

```javascript
使用：
1.引入<script src="lib/jquery-3.5.1.min.js"></script>（也可以引入在线的）
2、入口函数：保证页面加载完全（避免定位不到标签）
	$(function(){
        在里面写东西
    })
3、例子：
    <p id='test'>点击弹窗</p>
	<script>
    	$(function(){
    		$('#test').click(function(){
                alert('hhhh');
            })
		})
    </script>
4、事件
	鼠标事件  mouseover。。。。。
    键盘事件
   例子：获取鼠标坐标  $(function () {
        $('#divover').mousemove(function (e){
            $('#mouseOver').text('x:'+e.pageX+'y:'+e.pageY)
        })
    })
```





