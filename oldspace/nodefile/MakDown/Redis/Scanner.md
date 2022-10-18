# Scanner控制台输入输出

```java
Scanner sc = new Scanner(System.in);
//回车结束输入  但是读取不到空格后的内容
scanner.hasNext();
String str = scanner.next();

//回车结束输入  读取所有输入包括空格
scanner.hasNextline();
String strLine = scanner.nextline();

//读取特定字符  int
scanner.hasNextInt();
int i = scanner.nextInt();


scanner.close();
```



