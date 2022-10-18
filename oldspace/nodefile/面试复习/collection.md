***<u>容器</u>***

![image-20220209170039382](..\面试复习\array.assets\image-20220209170039382.png)

## List, Set, Queue, Map 四者的区别

- `List`(对付顺序的好帮手): 存储的元素是有序的、可重复的。
- `Set`(注重独一无二的性质): 存储的元素是无序的、不可重复的。
- `Queue`(实现排队功能的叫号机): 按特定的排队规则来确定先后顺序，存储的元素是有序的、可重复的。
- `Map`(用 key 来搜索的专家): 使用键值对（key-value）存储，类似于数学上的函数 y=f(x)，"x" 代表 key，"y" 代表 value，key 是无序的、不可重复的，value 是无序的、可重复的，每个键最多映射到一个值。



# list

list的两种实现类：ArrayList和LinkedList

list有序性的表现：插入顺序和遍历顺序一致（前提是使用`add()`方法添加元素，如果使用`set`方式替换元素，其有序性不能体现）



`ArrayList`是以`Object`数组的方式保存数据的。其在内存和逻辑上都表现了`List`数组的有序性。

​	

`LinkedList`是以双向链表的方式保存数据的（移除了循环链表）。内存无序，逻辑有序。



`Vector`：`Object[]` 数组  ==加了同步锁线程安全==

## ArrayList

```java
//默认容量
  private static final int DEFAULT_CAPACITY = 10;
//空集合
  private static final java.lang.Object[] EMPTY_ELEMENTDATA= {};
//空集合
  private static final java.lang.Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA= {};
//存放元素Object类型数组
  transient java.lang.Object[] elementData;
//数组大小
  private int size;
//数组最大容量 21亿-8
  private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
//三个构造函数
  public java.util.ArrayList(int);
  public java.util.ArrayList();
  public java.util.ArrayList(java.util.Collection<? extends E>);
//修改（修剪）数组为Size大小
  public void trimToSize();
//确保容量（一次性扩容，在做大批量的插入的时候可以在循环前加入此方法，提高程序性能）
  public void ensureCapacity(int);
//计算（最小）容量   
  private static int calculateCapacity(java.lang.Object[], int);
//确保容量足够（在内部）
  private void ensureCapacityInternal(int);
//显示确保容量（modCount++每当操作对象数组此字段加一）（此字段也是`ConcurrentModificationException`异常判断依赖）
  private void ensureExplicitCapacity(int);
//扩容核心方法（左移一位{一半}自加）[1.5倍扩容]
//若size还是不够，直接等于minCapacity
//特殊情况  21亿
  private void grow(int);
//最大容量（再大不让存了）
  private static int hugeCapacity(int);
//返回对象下标  equals  =》firstIndexOf
  public int indexOf(java.lang.Object);
  public int lastIndexOf(java.lang.Object);
//浅拷贝 重置modCount
  public java.lang.Object clone();
  E elementData(int);
//检查下标是否异常  调用elementData  返回泛型
  public E get(int);
//覆盖原元素
  public E set(int, E);
//尾部添加 添加元素modCount++ 扩容（如果执行ensureCapacity）就无序多次扩容 size++
  public boolean add(E);
//具体位置添加  扩容 元素后移 添加元素 size++
  public void add(int, E);
//检测下标  modCount++  数组左移 最后一个元素置位null等待GC回收
  public E remove(int);
  public boolean remove(java.lang.Object);
  private void fastRemove(int);
```

***<u>两个异常</u>***

`ConcurrentModificationException`   同时修改异常

`IndexOutOfBoundsException`				下标越界异常



==线程不安全的==

# Set

- `HashSet`(无序，唯一): 基于 `HashMap` 实现的，底层采用 `HashMap` 来保存元素
- `LinkedHashSet`: `LinkedHashSet` 是 `HashSet` 的子类，并且其内部是通过 `LinkedHashMap` 来实现的。有点类似于我们之前说的 `LinkedHashMap` 其内部是基于 `HashMap` 实现一样，不过还是有一点点区别的
- `TreeSet`(有序，唯一): 红黑树(自平衡的排序二叉树)



#### Map

- `HashMap`： JDK1.8 之前 `HashMap` 由数组+链表组成的，数组是 `HashMap` 的主体，链表则是主要为了解决哈希冲突而存在的（“拉链法”解决冲突）。JDK1.8 以后在解决哈希冲突时有了较大的变化，当链表长度大于阈值（默认为 8）（将链表转换成红黑树前会判断，如果当前数组的长度小于 64，那么会选择先进行数组扩容，而不是转换为红黑树）时，将链表转化为红黑树，以减少搜索时间
- `LinkedHashMap`： `LinkedHashMap` 继承自 `HashMap`，所以它的底层仍然是基于拉链式散列结构即由数组和链表或红黑树组成。另外，`LinkedHashMap` 在上面结构的基础上，增加了一条双向链表，使得上面的结构可以保持键值对的插入顺序。同时通过对链表进行相应的操作，实现了访问顺序相关逻辑。详细可以查看：[《LinkedHashMap 源码详细分析（JDK1.8）》  (opens new window)](https://www.imooc.com/article/22931)
- `Hashtable`： 数组+链表组成的，数组是 `Hashtable` 的主体，链表则是主要为了解决哈希冲突而存在的
- `TreeMap`： 红黑树（自平衡的排序二叉树）

