本文学习TreeMap，这是一个key-value的类，是一颗红黑树。



### 简介

​	TreeMap是一颗红黑树，能够将保存的记录以key排序,默认是按升序排序，也可以指定排序的比较器，当用Iterator 遍历TreeMap时，得到的记录是排过序的。TreeMap不允许key的值为null。是一个非同步的容器。



### 属性

```java
//比较器
private final Comparator<? super K> comparator;
//根节点
private transient Entry<K,V> root;
//节点数量
private transient int size = 0;
//修改次数
private transient int modCount = 0;
//叶子节点value
private static final Object UNBOUNDED = new Object();
//红
private static final boolean RED   = false;
//黑
private static final boolean BLACK = true;
```



### 插入元素

- 符合二叉排序树特征，使用红黑树减少频繁旋转导致性能浪费
- 插入元素存在重复，覆盖元素，并返回旧址



```java
public V put(K key, V value) {
    Entry<K,V> t = root;
    if (t == null) {
        //插入元素必须是可比较的、或者自定义比较器
        compare(key, key); // type (and possibly null) check

        root = new Entry<>(key, value, null);
        size = 1;
        modCount++;
        return null;
    }
    int cmp;
    //父节点，便于后面插入
    Entry<K,V> parent;
    // split comparator and comparable paths
    Comparator<? super K> cpr = comparator;
    if (cpr != null) {
        do {
            parent = t;
            cmp = cpr.compare(key, t.key);
            //小  插入左子树
            if (cmp < 0)
                t = t.left;
            //大  插入右子树
            else if (cmp > 0)
                t = t.right;
            //等于   替换value
            else
                return t.setValue(value);
        } while (t != null);
    }
    else {
        if (key == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
        do {
            parent = t;
            cmp = k.compareTo(t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else
                return t.setValue(value);
        } while (t != null);
    }
    Entry<K,V> e = new Entry<>(key, value, parent);
    //小  作为父节点的左子节点
    if (cmp < 0)
        parent.left = e;
    //大  作为父节点的右子节点
    else
        parent.right = e;
    //修复红黑树
    fixAfterInsertion(e);
    size++;
    modCount++;
    return null;
}
```



### 获取元素

#### getEntry(Object key)

> 这是获取值方法，对红黑树不做修改，所以不用修复。

```java
final Entry<K,V> getEntry(Object key) {
    // Offload comparator-based version for sake of performance
    if (comparator != null)
        return getEntryUsingComparator(key);
    if (key == null)
        throw new NullPointerException();
    @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
    Entry<K,V> p = root;
    while (p != null) {
        int cmp = k.compareTo(p.key);
        //小于 去左子树拿
        if (cmp < 0)
            p = p.left;
        //大于  去右子树拿
        else if (cmp > 0)
            p = p.right;
        //等于  返回
        else
            return p;
    }
    //未找到返回null
    return null;
}
```



### 删除元素

#### remove(Object key)

> 此方法会调用deleteEntry(Entry<K,V> p)方法。
>
> 移除元素，如果是红色的，不会改变红黑树特性，所以不用通过旋转来修复。
>
> 如果是黑色的，移除会修改任一节点到其叶子节点黑色节点数量一致特性，需要通过旋转来修复。



### 总结

这就是一颗红黑树，只不过保存的值为TreeMap.Entry<K,V>

















