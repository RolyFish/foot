# 重载

```java
public void max(int a, in b){
    println(a>b?a:b);
}
public void max(int a){
    println(a);
}
public void max(int a, in b,int c){
    println(a>b?a:b);
}
public void max(float a, in b){
    println(a>b?a:b);
}

#方法名不变  参数列表要变 必须
#返回值可变可不变
#只变返回值不构成重载
```

```tex
tips：
main方法是有参数的且可以设置
命令行设置
首先javac编译
到src目录下再运行  ====每个类都有路径

javac method.java
cd../
java package.method 参数1 参数2 。。。。。
```

```java
可变参数列表
    fun（String...strs）{
    foreach(String str:strs){
        sout(str);
    }
}
```

```java
递归  找到离开递归的条件  此递归就是当参数为1时
    int fun(i){
    if(i==1){
        return 1;
    }else{
        return i*fun(i-1);
    }
	}
```

