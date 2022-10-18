# HelloWorld

1. 新建一个txt  改后缀名为java。编写代码：

```java
public class Helloworld{
    public void static main(String [] args){
        System.out.print("Hello,World!")
    }
}
```

2. 打开cmd进入 java文件夹  
3. 输入 javac HelloWorld.java
4. 输入 java HelloWorld 

# java程序运行机制

```bash'
java源文件->java编译器->class文件 类装载器 字节码校验 解释器
操作系统只认识二进制文件
```

