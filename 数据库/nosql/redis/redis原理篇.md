# Redis原理



## 原理-数据结构



### 动态字符串-SDS

> 字符串是Redis中最常用的一种数据结构,无论是key还是value大部分都是以字符串的形式存储。

#### C语言字符串问题

Redis底层使用C语言实现,但是Redis并没有直接使用C语言的字符串结构,因为C语言的字符串存在以下问题：

![image-20230425225812773](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230425225812773.png)

- 获取字符串长度需要运算

  > 字符数组长度减一、遍历字符数组直到`\0`

- 非二进制安全

  > C语言字符串以`\0`结尾,那么字符串中不可包含`\0`字符

- 不可修改

#### SDS

> SDS(Simple Dynamic String)---简单动态字符串。
>
> Redis自己实现了一种新的字符串结构,成为SDS。

redis中所有数据类型都会转化为对应结构体存储,SDS根据空间大小分为以下几种结构体：

其中属性：

- uint8_t len   ：字符串实际长度、已使用长度
- uint8_t alloc;：字符串申请内存长度,包含空字节
- unsigned char flags; ：SDS头类型。SDS_TYPE_5、SDS_TYPE_8等
- char buf[];  ： 字节数组

```c
struct __attribute__ ((__packed__)) sdshdr8 {
    uint8_t len; /* used */
    uint8_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr16 {
    uint16_t len; /* used */
    uint16_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr32 {
    uint32_t len; /* used */
    uint32_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr64 {
    uint64_t len; /* used */
    uint64_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
```

> 例如：执行`set name yuyc`命令

redis会创建两个结构体分别存储`name`和`yuyc`

![image-20230425232759936](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230425232759936.png)



##### SDS-动态字符串

> SDS之所以成为动态字符串,是因为他有扩容能力。

扩容规则：

- 扩容后的新字符串小于1M,则新空间长度为扩展后字符串长度的两倍+1
- 扩容后的新字符串大于1M,则新空间长度为扩展后字符串长度+1M+1。称为==内存预分配==

优点：

- 获取字符串长度时间复杂度为O(1)
- 支持动态扩容
- 支持内存预分配。内存分配非常消耗资源,内存预分配可减少内存分配带来的新能消耗
- 二进制安全。redis读取字符串依据的是字符串长度`uint8_t len`,而不是`\0`

> 例如：一个字符串`set key HE`,给它追加一个`append key LLO`,扩容过程是这样的：

```shell
10.211.55.4:7003> set key HE
OK
10.211.55.4:7003> append key LLO
(integer) 5
10.211.55.4:7003> get key
"HELLO"
```

![image-20230425234349869](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230425234349869.png)



### intset

IntSet是Redis中set集合的一种实现方式，基于整数数组来实现，并且具备长度可变、有序等特征。
结构如下：

```c
typedef struct intset {
    uint32_t encoding;
    uint32_t length;
    int8_t contents[];
} intset;
```

- Encoding,编码方式

  > 支持存放16、32、64位整数。对应Java中的Short、Int、Long
  >
  > - INTSET_ENC_INT16
  > - INTSET_ENC_INT32
  > - INTSET_ENC_INT64

- length,set集合长度,成员个数

- contents,元素数组实际存放位置的引用指针

为了方便查找，Redis会将intset中所有的整数按照升序依次保存在contents数组中，结构如图：

![image-20230426105928963](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426105928963.png)

#### inset插入元素

> intset插入元素时如果插入的新元素编码方式比旧元素大,那么就需要编码升级,拷贝旧元素,并在末尾插入新元素

> intset插入的新元素编码方式和比旧元素小,无需编码升级
>
> - 如果插入元素,在旧元素数组中存在,直接返回报错
> - 如果新元素大于旧元素最大值,数组扩容,直接在末尾插入即可,
>
> - 如果小于旧元素最小值或如果插入元素在旧元素数组之间,计算得到插入元素位置`pos`,数组扩容,旧元素需要右移`pos`位,并在``pos`处插入新元素

#### 总结

Intset可以看做是特殊的整数数组，具备一些特点：

* Redis会确保Intset中的元素唯一、有序
* 具备类型升级机制，可以节省内存空间
* 底层采用二分查找方式来查询,获取插入元素的`pos`



### Dict

我们知道Redis是一个键值型（Key-Value Pair）的数据库，我们可以根据键实现快速的增删改查。而键与值的映射关系正是通过Dict来实现的。
Dict由三部分组成，分别是：哈希表（DictHashTable）、哈希节点（DictEntry）、字典（Dict）

dict:

- dictType   dict类型
- privdata 
- dictht    h[0]平常用,h[1]做rehash时用
- rehashidx  rehash过程中记录的下标
-    rehash是否开始

dictht：

- dictEntry   指向dictEntry数组的指针
- size     哈希表大小
- sizeamask   掩码,值为size-1,用于计算散列下标
- used   dictentry个数

dictEntry：

- key    指向键的指针
- union： value
- next   下一个entry指针

![1653985570612](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653985570612.png)

#### 添加元素

> 向dict添加键值对时
>
> - 键值对封装成dictEntry
>
> - 根据key值计算散列下标(dictEntry放入table的何处)
>
>   > 如何计算散列下标：key的哈希值 hash 和 哈希表大小掩码按位与 `hash & sizemask`。其实就是除以取余。
>   >
>   > 前提是哈希表的大小size永远为2^n^
>
> - 插入元素
>
>   - 未出现冲突,直接插入元素
>   - 出现冲突,拉链法解决冲突,头插法插入元素。

例子说明：k1 v1出现哈希冲突,拉链法头插法解决冲突

![image-20230426123215928](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426123215928.png)

dict,包含两个dictht,一个平常用,一个rehash用：

![image-20230426123126773](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426123126773.png)

#### dict扩容

Dict中的HashTable就是数组结合单向链表的实现，当集合中元素较多时，必然导致哈希冲突增多，链表过长，则查询效率会大大降低。
Dict在每次新增键值对时都会检查负载因子（LoadFactor = used/size） ，满足以下两种情况时会触发哈希表扩容：

- 哈希表的 LoadFactor >= 1，并且服务器没有执行 BGSAVE 或者 BGREWRITEAOF 等后台进程；
- 哈希表的 LoadFactor > 5 ；



#### Dict的rehash

> 当Dict容量发生变化时,就必须对元素重新分配,分配的过程就叫做rehash。

rehash原因：Dict容量发生变化,则sizemask就会变化,元素散列的位置依赖sizemark,索引容量变化就需要rehash。

无论是扩容还是收缩,都会触发rehash。

* 计算新hash表的realeSize，值取决于当前要做的是扩容还是收缩：

  * 如果是扩容，则新size为第一个大于等于dict.ht[0].used + 1的2^n
  * 如果是收缩，则新size为第一个大于等于dict.ht[0].used的2^n （不得小于4）

* 按照新的realeSize申请内存空间，创建dictht，并赋值给dict.ht[1]

* 设置dict.rehashidx = 0，标示开始rehash

* 将dict.ht[0]中的每一个dictEntry都rehash到dict.ht[1]

  > 这里采用渐进式rehash,而不是一次性rehash,避免主线程阻塞。

* 将dict.ht[1]赋值给dict.ht[0]，给dict.ht[1]初始化为空哈希表，释放原来的dict.ht[0]的内存

* 将rehashidx赋值为-1，代表rehash结束

* 在rehash过程中，新增操作，则直接写入ht[1]，查询、修改和删除则会在dict.ht[0]和dict.ht[1]依次查找并执行。这样可以确保ht[0]的数据只减不增，随着rehash最终为空


#### 总结

Dict的结构：

rehashidx rehash过程中记录的下标,pauserehash是否开始rehash,Dictht ht[2]用有两个哈希表,一个平常用一个rehash过程用。

* 类似java的HashTable，底层是数组加链表来解决哈希冲突
* Dict包含两个哈希表，ht[0]平常用，ht[1]用来rehash

Dict的伸缩：

* 当LoadFactor大于5或者LoadFactor大于1并且没有子进程任务时，Dict扩容
* 当LoadFactor小于0.1时，Dict收缩
* 扩容大小为第一个大于等于used + 1的2^n
* 收缩大小为第一个大于等于used 的2^n
* Dict采用渐进式rehash，每次访问Dict时执行一次rehash
* rehash时ht[0]只减不增，新增操作只在ht[1]执行，其它操作在两个哈希表

### ziplist

ZipList 是一种特殊的“双端链表” ，由一系列特殊编码的连续内存块组成,拥有双向链表特性,但未使用指针实现。可以在任意一端进行压入/弹出操作, 并且该操作的时间复杂度为 O(1)。



#### ziplist结构

![1653985987327](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653985987327.png)

![1653986020491](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653986020491.png)

| **属性** | **类型** | **长度** | **用途**                                                     |
| -------- | -------- | -------- | ------------------------------------------------------------ |
| zlbytes  | uint32_t | 4 字节   | 记录整个压缩列表占用的内存字节数                             |
| zltail   | uint32_t | 4 字节   | 记录压缩列表表尾节点距离压缩列表的起始地址有多少字节，通过这个偏移量，可以确定表尾节点的地址。 |
| zllen    | uint16_t | 2 字节   | 记录了压缩列表包含的节点数量。 最大值为UINT16_MAX （65534），如果超过这个值，此处会记录为65535，但节点的真实数量需要遍历整个压缩列表才能计算得出。 |
| entry    | 列表节点 | 不定     | 压缩列表包含的各个节点，节点的长度由节点保存的内容决定。     |
| zlend    | uint8_t  | 1 字节   | 特殊值 0xFF （十进制 255 ），用于标记压缩列表的末端。        |

#### ZipListEntry

##### 结构

ZipList 中的Entry并不像普通链表那样记录前后节点的指针，因为记录两个指针要占用16个字节，浪费内存。而是采用了下面的结构：

![image-20230426143320790](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426143320790.png)

* previous_entry_length：前一节点的长度，占1个或5个字节。
  * 如果前一节点的长度小于254字节，则采用1个字节来保存这个长度值
  * 如果前一节点的长度大于254字节，则采用5个字节来保存这个长度值，第一个字节为0xfe，后四个字节才是真实长度数据

* encoding：编码属性，记录content的数据类型（字符串还是整数）以及长度，占用1个、2个或5个字节
* contents：负责保存节点的数据，可以是字符串或整数

ZipList中所有存储长度的数值均采用小端字节序，即低位字节在前，高位字节在后。例如：数值0x1234，采用小端字节序后实际存储值为：0x3412

##### encoding

> ZipListEntry中的encoding编码分为字符串和整数两种,encoding占用字节长度取决于content内容的大小。

1. 字符串：如果encoding是以“00”、“01”或者“10”开头，则证明content是字符串

| **编码**                                             | **编码长度** | **字符串大小**      |
| ---------------------------------------------------- | ------------ | ------------------- |
| \|00pppppp\|                                         | 1 bytes      | <= 63 bytes         |
| \|01pppppp\|qqqqqqqq\|                               | 2 bytes      | <= 16383 bytes      |
| \|10000000\|qqqqqqqq\|rrrrrrrr\|ssssssss\|tttttttt\| | 5 bytes      | <= 4294967295 bytes |

例如，我们要保存字符串：“ab”和 “bc”

“ab”entry：

- previous_entry_length  占用一个字节,为第一个元素,值为： 0000 0000
- encoding 字符串长度小于63,占用一个字节,值为  0000 0010
- content：字节数组,“ab”占两个字节, 0110 0010

转化为16进制就是：0x00 0x02 0x62 0x62。previous_entry_length和encoding小端存储,但只有一个字节不用动。

![1653986172002](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653986172002.png)

2. 整数

如果encoding是以“11”开始，则证明content是整数，且encoding固定只占用1个字节

| **编码** | **编码长度** | **整数类型**                                               |
| -------- | ------------ | ---------------------------------------------------------- |
| 11000000 | 1            | int16_t（2 bytes）                                         |
| 11010000 | 1            | int32_t（4 bytes）                                         |
| 11100000 | 1            | int64_t（8 bytes）                                         |
| 11110000 | 1            | 24位有符整数(3 bytes)                                      |
| 11111110 | 1            | 8位有符整数(1 bytes)                                       |
| 1111xxxx | 1            | 直接在xxxx位置保存数值，范围从0001~1101，减1后结果为实际值 |

例如：存整数：2和 5

2和5在0 -12之间采用最后一种编码方式,即没有content,数据保存在encoding中。

![image-20230426144814215](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426144814215.png)

#### ziplist连锁更新问题

ZipList的每个Entry都包含previous_entry_length来记录上一个节点的大小，长度是1个或5个字节：

- 如果前一节点的长度小于254字节，则采用1个字节来保存这个长度值
- 如果前一节点的长度大于等于254字节，则采用5个字节来保存这个长度值，第一个字节为0xfe，后四个字节才是真实长度数据

现在，假设我们有N个连续的、长度为250~253字节之间的entry，因此entry的previous_entry_length属性用1个字节即可表示，如图所示：

![1653986328124](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653986328124.png)

但是此刻执行一个`lpush`添加一个长度大于254的节点到ziplist中,那么就需要5个字节数来保存新加入节点的长度,导致之后的所有节点都需要更新。

ZipList这种特殊情况下产生的连续多次空间扩展操作称之为连锁更新（Cascade Update）。新增、删除都可能导致连锁更新的发生。

#### 总结

* 压缩列表的可以看做一种连续内存空间的"双向链表"
* 列表的节点之间不是通过指针连接，而是记录上一节点和本节点长度来寻址，内存占用较低
* 如果列表数据过多，导致链表过长，可能影响查询性能
* 增或删较大数据时有可能发生连续更新问题



### quicklist

问题1：ZipList虽然节省内存，但申请内存必须是连续空间，如果内存占用较多，申请内存效率很低。怎么办？

​	答：为了缓解这个问题，我们必须限制ZipList的长度和entry大小。

问题2：但是我们要存储大量数据，超出了ZipList最佳的上限该怎么办？

​	答：我们可以创建多个ZipList来分片存储数据。

问题3：数据拆分后比较分散，不方便管理和查找，这多个ZipList如何建立联系？

​	答：Redis在3.2版本引入了新的数据结构QuickList，它是一个双端链表，只不过链表中的每个节点都是一个ZipList。

![image-20230426161019489](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426161019489.png)

为了避免QuickList中的每个ZipList中entry过多，Redis提供了一个配置项：list-max-ziplist-size来限制。
如果值为正，则代表ZipList的允许的entry个数的最大值
如果值为负，则代表ZipList的最大内存大小，分5种情况：

* -1：每个ZipList的内存占用不能超过4kb
* -2：每个ZipList的内存占用不能超过8kb
* -3：每个ZipList的内存占用不能超过16kb
* -4：每个ZipList的内存占用不能超过32kb
* -5：每个ZipList的内存占用不能超过64kb

其默认值为 -2：

![image-20230426161133171](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426161133171.png)

quicklist源码：

![image-20230426161158060](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426161158060.png)



我们接下来用一段流程图来描述当前的这个结构

![1653986718554](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653986718554.png)

总结：

QuickList的特点：

* 是一个节点为ZipList的双端链表
* 节点采用ZipList，解决了传统链表的内存占用问题
* 控制了ZipList大小，解决连续内存空间申请效率问题
* 中间节点可以压缩，进一步节省了内存



### SkipList

> ZipList和QuickList他们空间利用率是很好的,查询头尾数据性能也还行,但是在查询中间数据时,性能一般。
>
> Redis引入了SkipList通过空间换时间来提升查找效率。

#### 结构

SkipList（跳表）首先是链表，但与传统链表相比有几点差异：

- 元素按照升序排列存储
- 节点可能包含多个指针，指针跨度不同。

![1653986771309](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653986771309.png)



![1653986813240](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653986813240.png)

#### 查询原理

> SkipList中的每个节点SkipListNode可以包含多个指针,指针跨度不同。
>
> 查询性能和红黑树相当

![image-20230426162942173](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426162942173.png)

#### 总结

SkipList的特点：

* 跳表是一个双向链表，每个节点都包含score和ele值
* 节点按照score值排序，score值一样则按照ele字典排序
* 每个节点都可以包含多层指针，层数是1到32之间的随机数
* 不同层指针到下一个节点的跨度不同，层级越高，跨度越大
* 增删改查效率与红黑树基本一致，实现却更简单

### redisobject

#### 简介

Redis中的任意数据类型的键和值都会被封装为一个RedisObject，也叫做Redis对象，源码如下：

![1653986956618](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653986956618.png)

1、什么是redisObject：
从Redis的使用者的角度来看，⼀个Redis节点包含多个database（非cluster模式下默认是16个，cluster模式下只能是1个），而一个database维护了从key space到object space的映射关系。这个映射关系的key是string类型，⽽value可以是多种数据类型，比如：
string, list, hash、set、sorted set等。我们可以看到，key的类型固定是string，而value可能的类型是多个。
⽽从Redis内部实现的⾓度来看，database内的这个映射关系是用⼀个dict来维护的。dict的key固定用⼀种数据结构来表达就够了，这就是动态字符串sds。而value则比较复杂，为了在同⼀个dict内能够存储不同类型的value，这就需要⼀个通⽤的数据结构，这个通用的数据结构就是robj，全名是redisObject。

#### 编码方式

Redis中会根据存储的数据类型不同，选择不同的编码方式，共包含11种不同类型：

| **编号** | **编码方式**            | **说明**               |
| -------- | ----------------------- | ---------------------- |
| 0        | OBJ_ENCODING_RAW        | raw编码动态字符串      |
| 1        | OBJ_ENCODING_INT        | long类型的整数的字符串 |
| 2        | OBJ_ENCODING_HT         | hash表（字典dict）     |
| 3        | OBJ_ENCODING_ZIPMAP     | 已废弃                 |
| 4        | OBJ_ENCODING_LINKEDLIST | 双端链表               |
| 5        | OBJ_ENCODING_ZIPLIST    | 压缩列表               |
| 6        | OBJ_ENCODING_INTSET     | 整数集合               |
| 7        | OBJ_ENCODING_SKIPLIST   | 跳表                   |
| 8        | OBJ_ENCODING_EMBSTR     | embstr的动态字符串     |
| 9        | OBJ_ENCODING_QUICKLIST  | 快速列表               |
| 10       | OBJ_ENCODING_STREAM     | Stream流               |

#### 数据结构

Redis中会根据存储的数据类型不同，选择不同的编码方式。每种数据类型的使用的编码方式如下：

| **数据类型** | **编码方式**                                       |
| ------------ | -------------------------------------------------- |
| OBJ_STRING   | int、embstr、raw                                   |
| OBJ_LIST     | LinkedList和ZipList(3.2以前)、QuickList（3.2以后） |
| OBJ_SET      | intset、HT                                         |
| OBJ_ZSET     | ZipList、HT、SkipList                              |
| OBJ_HASH     | ZipList、HT                                        |

#### Redis数据类型-String

String是Redis中最常见的数据存储类型：

- 其基本编码方式是RAW，基于简单动态字符串（SDS）实现，存储上限为512mb。
- 如果存储的SDS长度小于44字节，则会采用EMBSTR编码，此时object head与SDS是一段连续空间。申请内存时只需要调用一次内存分配函数，效率更高。
- 如果存储的字符串是整数值，并且大小在LONG_MAX范围内，则会采用INT编码：直接将数据保存在RedisObject的ptr指针位置（刚好8字节），不再需要SDS了。

1. RAW编码方式底层实现⽅式：动态字符串sds
   RAW编码方式,String的内部存储结构是sds(Simple Dynamic String,可以动态扩展内存),RedisObject的指针`ptr`指向一个`SDS`结构体。![1653987103450](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653987103450.png)

2. EMBSTR编码方式,如果存储的SDS长度小于44字节则会采用此编码方式。此时redisobject和SDS一起分配内存,效率更高

   ![image-20230426183636926](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426183636926.png)

3. Int编码方式,如果存储的字符串是整数值采用此编码方式。

   ![image-20230426183822400](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426183822400.png)

##### 例子

- int、embstr、raw编码例子：

![image-20230426212601295](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426212601295.png)

- 浮点数会以字符串的形式处理：

![image-20230426212712334](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426212712334.png)

- 当我们对int类型编码的字符串,执行incr、decr操作时,redis会尝试将string转化成log类型,并直接进行加减操作,结果任然是,int类型编码。如果转换失败将会报错

![image-20230426212843997](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426212843997.png)

- 但是对int类型编码的字符串,做incrbyfloat、append、setbit时,redis会直接将他们当做字符串处理,输出结果将是raw会embstr类型

  ![image-20230426215059818](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426215059818.png)

#### Redis数据类型-list

> Redis数据类型list有头尾插入,头尾推出等命令。有着双向链表的特征,在Redis提供的结构体中ziplist、quicklist都有此特性。而Redis在3.2版本之后引入quicklist就是redis-list的默认实现。
>
> Redis数据类型List其实是一个RedisObject,robj的ptr指针指向一个QuickList结构体,QuickList是一个双向链表,存储着多个ZipList。

在3.2版本之前，Redis采用ZipList和LinkedList来实现List，当元素数量小于512并且元素大小小于64字节时采用ZipList编码，超过则采用LinkedList编码。

在3.2版本之后，Redis统一采用QuickList来实现List：

![1653987313461](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653987313461.png)

##### 例子

![image-20230427010231851](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230427010231851.png)



#### Redis数据类型-set

> Set是Redis中的单列集合，满足下列特点：
>
> * 不保证有序性
> * 保证元素唯一
> * 求交集、并集、差集
>
> set有`SISMEMBER、SINTER、SDIFF、SUNION`,判断元素是否存在、交集、差集、并集等命令,而求交集并集差集命令要求set必须拥有不错的查询效率。

满足以上set集合特征的结构体有：

- DICT

  > 以key作为member,value为null,保证唯一性
  >
  > DICT基于哈希表实现,查询时间复杂度是O(0),常数阶

- IntSet

  > INTSET底层基于整数数组实现,有序且保证成员唯一性
  >
  > INTSET有序,且成员类型一致,基于二分查找查询成员,查询效率可以保障

> 所以Redis-Set数据类型采用以上两种编码方式实现：

当SET中的成员都是整数类型,且SET集合的长度不超多`set-max-intset-entries`时,SET会采用INTSET编码。INTSET没有过多的指针,因此他的内存利用率较高,但是长度不能太长,避免查询效率低下。

当SET中的成员其中有一个不是整数类型,那么SET就会使用DICT结构体,采用HT编码

![image-20230426225934589](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426225934589.png)

![image-20230426230032335](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426230032335.png)

##### 例子

![image-20230427010102194](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230427010102194.png)





#### Redis数据类型-ZSET

ZSet也就是SortedSet，其中每一个元素都需要指定一个score值和member值：

* 可以根据score值排序
* member必须唯一
* 可以根据member查询分数

因此，zset底层数据结构必须满足键值存储、键必须唯一、可排序这几个需求。之前学习的哪种编码结构可以满足？

- SKIPLIST：满足键值存储,可排序
- HT(DICT):满足键值存储,可跟据key找value。(ZSET中即是根据member找score),可做member唯一性判断

> 但是以上两个数据结构都不可以满足zset的所有需求。所以zset的其中一种实现就是这两种结构体的组合实现。
>
> ZEST数据类型在底层会生成一个robj,robj中的指针会指向一个机构体,这个结构体内存着两个指针,分别指向一个DICT和一个SkipList.
>
> - 当我们做通过Key(member)查询score(value)时,走的是DICT这个结构体
> - 当我们做按分数排序和范围查找时,走的是SkipList结构体

![image-20230426235509928](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426235509928.png)

![image-20230426235525211](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230426235525211.png)



> 上面的ZSET的一种实现,它存在非常多的指针,甚至数据也保存了两份,非常浪费内存。所以在数据量不大的时候,ZSET会使用ZipList结构体来实现。

前提条件是：

- zset中元素个数小于zset_max_ziplist_entries,默认128
- 每个元素都小于zset_max_ziplist_value字节,默认64

![image-20230427000515376](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230427000515376.png)

但是ZipList它既没有键值对概念,也没有排序功能,所以redis是基于ziplist通过业务逻辑的方式使得ziplist具有键值对概念、并且可以排序、并且可以基于member查询score。

- 键值对的概念是这样的,ziplist中所有的entry都是连续存储的,socre和member是挨在一起的两个entry,顺序遍历即可。score小的entry放在头部、大的放在尾部
- 基于member查询score,就是变量,查询到member,根据member的entry的前一个entry的长度,找到score

![1653992299740](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653992299740-2527236.png)

##### 例子

> listpack主要用于解决ziplist它连锁更新问题,实现原理差不多。

![image-20230427004829770](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230427004829770.png)

> 此刻若设置`zset-max-ziplist-entries = 5`,并且再插入两个score-member,那么zset就会进行数据类型转换

![image-20230427005422522](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230427005422522.png)

#### Redis数据类型-hash

Hash结构与Redis中的Zset非常类似：

* 都是键值存储
* 都需求根据键获取值
* 键必须唯一

区别如下：

* zset的键是member，值是score；hash的键和值都是任意值
* zset要根据score排序；hash则无需排序

> 底层实现方式：压缩列表ziplist 或者 字典dict

当Hash中数据项比较少的情况下，Hash底层才⽤压缩列表ziplist进⾏存储数据，随着数据的增加，底层的ziplist就可能会转成dict，具体配置如下：

- hash数据类型entry个数, hash-max-ziplist-entries 512
- hash数据类型entry最大限制,hash-max-ziplist-value 64

当满足上面两个条件其中之⼀的时候，Redis就使⽤dict字典来实现hash。
Redis的hash之所以这样设计，是因为当ziplist变得很⼤的时候，它有如下几个缺点：

* 每次插⼊或修改引发的realloc(重新申请内存)操作会有更⼤的概率造成内存拷贝，从而降低性能。
* ⼀旦发生内存拷贝，内存拷贝的成本也相应增加，因为要拷贝更⼤的⼀块数据。
* 当ziplist数据项过多的时候，在它上⾯查找指定的数据项就会性能变得很低，因为ziplist上的查找需要进行遍历。

总之，ziplist本来就设计为各个数据项挨在⼀起组成连续的内存空间，这种结构并不擅长做修改操作。⼀旦数据发⽣改动，就会引发内存realloc，可能导致内存拷贝。

因此，Hash底层采用的编码与Zset也基本一致，只需要把排序有关的SkipList去掉即可：

Hash结构默认采用ZipList编码，用以节省内存。 ZipList中相邻的两个entry 分别保存field和value

![1653992413406](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653992413406.png)

##### 例子

![image-20230427005706541](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230427005706541.png)



## Redis网络模型



### 用户空间和内核空间

`Linux`有很多发行版,比如Ubuntu、CentOS、Fedroa等,这些发行版都是对Linux做了一层包装,器内核还是Linux内核。

Linux内核也是一个应用,运行在内核空间,需要占用资源,可以和硬件资源(cpu、磁盘、网卡、文件系统)直接进行交互。

而Redis、Nginx、Mysql等,这些应用成为用户应用,运行在用户空间。

为什么分为用户空间和内存空间：避免用户程序占用过多资源,导致系统崩溃,用户程序的运行需要Linux内核的管理

那么用户程序如何访问硬件资源: 可以和硬件资源直接交互的只有Linux内核,所以用户程序想要访问硬件资源需要调用内核提供的接口,通过内核调用硬件资源

> 用户态内核态切换：

在linux中，他们权限分成两个等级，0和3，用户空间只能执行受限的命令（Ring3），而且不能直接调用系统资源，必须通过内核提供的接口来访问内核空间可以执行特权命令（Ring0）来调用一切系统资源，所以一般情况下，用户的操作是运行在用户空间，而内核运行的数据是在内核空间的，而有的情况下，一个应用程序需要去调用一些特权资源，去调用一些内核空间的操作，所以此时他俩需要在用户态和内核态之间进行切换。

比如：

Linux系统为了提高IO效率，会在用户空间和内核空间都加入缓冲区：

写数据时，要把用户缓冲数据拷贝到内核缓冲区，然后写入设备

读数据时，要从设备读取数据到内核缓冲区，然后拷贝到用户缓冲区

针对这个操作：我们的用户在写读数据时，会去向内核态申请，想要读取内核的数据，而内核数据要去等待驱动程序从硬件上读取数据，当从磁盘上加载到数据之后，内核会将数据写入到内核的缓冲区中，然后再将数据拷贝到用户态的buffer中，然后再返回给应用程序，整体而言，速度慢，就是这个原因，为了加速，我们希望read也好，还是wait for data也最好都不要等待，或者时间尽量的短。

![1653896687354](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653896687354.png)

### 网络模型-阻塞IO

在《UNIX网络编程》一书中，总结归纳了5种IO模型：

* 阻塞IO（Blocking IO）
* 非阻塞IO（Nonblocking IO）
* IO多路复用（IO Multiplexing）
* 信号驱动IO（Signal Driven IO）
* 异步IO（Asynchronous IO）

应用程序想要去读取数据，他是无法直接去读取磁盘数据的

- 需要先到内核里边去等待内核操作硬件拿到数据，是需要等待的
- 等到内核从磁盘上把数据加载出来之后，再把这个数据写给用户的缓存区

如果是阻塞IO，那么整个过程中，用户从发起读请求开始，一直到读取到数据，都是一个阻塞状态。

![image-20230427163644050](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230427163644050.png)

具体流程如下图：

用户去读取数据时，会去先发起recvform一个命令，去尝试从内核上加载数据，如果内核没有数据，那么用户就会等待，此时内核会去从硬件上读取数据，内核读取数据之后，会把数据拷贝到用户态，并且返回ok，整个过程，都是阻塞等待的，这就是阻塞IO

总结如下：

顾名思义，阻塞IO就是两个阶段都必须阻塞等待：

**阶段一：**

- 用户进程尝试读取数据（比如网卡数据）
- 此时数据尚未到达，内核需要等待数据
- 此时用户进程也处于阻塞状态

阶段二：

* 数据到达并拷贝到内核缓冲区，代表已就绪
* 将内核数据拷贝到用户缓冲区
* 拷贝过程中，用户进程依然阻塞等待
* 拷贝完成，用户进程解除阻塞，处理数据

可以看到，阻塞IO模型中，用户进程在两个阶段都是阻塞状态。



![1653897270074](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653897270074.png)

### 网络模型-非阻塞IO

顾名思义，非阻塞IO的recvfrom操作会立即返回结果而不是阻塞用户进程。

阶段一：

* 用户进程尝试读取数据（比如网卡数据）
* 此时数据尚未到达，内核需要等待数据
* 返回异常给用户进程
* 用户进程拿到error后，再次尝试读取
* 循环往复，直到数据就绪

阶段二：

* 将内核数据拷贝到用户缓冲区
* 拷贝过程中，用户进程依然阻塞等待
* 拷贝完成，用户进程解除阻塞，处理数据
* 可以看到，非阻塞IO模型中，用户进程在第一个阶段是非阻塞，第二个阶段是阻塞状态。虽然是非阻塞，但性能并没有得到提高。而且忙等机制会导致CPU空转，CPU使用率暴增。

![1653897490116](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653897490116.png)

### 网络模型-IO多路复用

> 阻塞IO和非阻塞IO过程分为两个阶段,用户应用发起recvfrom命令,内核读取并处理取数。
>
> 阻塞IO和非阻塞IO,区别在于处理recvfrom命令返回结果的方式不同,阻塞IO会阻塞,非阻塞IO会返回error,并再次尝试recvfrom。
>
> 无论是阻塞IO还是非阻塞IO性能都不太行,并且阻塞IO不能充分利用CPU资源,非阻塞IO会导致CPU空转。

而在单线程情况下，只能依次处理IO事件，如果正在处理的IO事件恰好未就绪（数据不可读或不可写），线程就会被阻塞，所有IO事件都必须等待，性能自然会很差。

> 如何解决以上问题呢？

- 增加线程
- 不阻塞,线程监测内核数据是否就绪,就绪了再发送recvfrom命令



> IO多路复用过程
>
> 一个线程监听多个fd,如果有fd就绪(有数据就绪),那么内核返回一个readable状态给到用户应用,用户应用拿到就绪的fd发送recvfrom命令请求内核,这样就不存在阻塞的情况。
>
> io多路复用有不同的实现模式,有select模式、poll模式、epoll模式。一些高性能的IO框架采用的都是epoll模式。

![1653898691736](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653898691736.png)

#### FD

文件描述符（File Descriptor）：简称FD，是一个从0 开始的无符号整数，用来关联Linux中的一个文件。在Linux中，一切皆文件，例如常规文件、视频、硬件设备等，当然也包括网络套接字（Socket）。

通过FD，我们的网络模型可以利用一个线程监听多个FD，并在某个FD可读、可写时得到通知，从而避免无效的等待，充分利用CPU资源。

#### IO多路复用-select模式

> Linux早期的IO多路复用模型使用的select模式。

把需要处理的数据封装成FD(可以是一个管道、socket套接字)，然后在用户态时创建一个fd的集合（这个集合的大小是要监听的那个FD的最大值+1，但是大小整体是有限制的 ），这个集合的长度大小是有限制的，同时在这个集合中，标明出来我们要控制哪些数据。

select函数的定义如下：

- nfds -- 为需要监视的fd最大值加一,表示读到这里就结束
- readfds--监视的读操作fd集合
- writefds--监视的写操作fd集合
- exceptfds--监视的异常事件fd集合
- timeout--超时时间

![image-20230427175743420](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230427175743420.png)

select模式下的IO多路复用过程：

- 在用户态创建一个fd集合

  > - 这个集合是有限制的,集合长度是32,元素是long int占32位,加起来就是1024位
  > - fd是一个正数值类型,在fd_set的保存方式是位的形式。比如fd为10,那么fd_set第十位就标为1(节省内存)

- 在用户态设置要监听的fd

  > 比如以下图片,监听的就是1,2,5这三个fd

- 在用户态执行select函数

- 将用户态创建的fd集合,拷贝到内核态

- 在内核态拿到拷贝的fd集合,遍历它

- 询问fd数据是否就绪,如果没有数据就绪,则等待数据就绪或超时。此刻内核监听这些fd

- 等到有数据就绪,内核态返回readable给用户态,并将可以访问的fd放在fd集合中,返回给用户态

- 用户态拿到fd集合,遍历它,并得到可读的fd。通过这个fd再次发送recvfrom命令(有数据不阻塞)

- 循环发送select命令

![image-20230427182228545](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230427182228545.png)



select模式存在以下问题：

- 频繁的用户态和内核态切换
- fd数组频繁拷贝
- fd集合频繁遍历
- 监听fd数量有限制-1024



#### IO多路复用-poll模式

> poll模式是对select模式的优化,但性能提升不大。

1. 首先poll模式的poll命令携带的参数不一样：

   - pollfd数组,存储着多个pollfd结构体,长度自定义,没有容量限制
     - 将fd封装成一个pollfd结构体,结构体中有①监听的fd②监听fd的类型(读、写、异常)③实际发生的事件类型(内核会修改此属性)

   - pollfd数组长度

   - 超时时间

![1653900721427](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653900721427.png)

2. poll模式下io流程
   - 在用户态创建pollfd数组,添加需要监听的fd信息,pollfd数组大小以及超时时间
   - 调用poll命令,将pollfd数组拷贝到内核空间,转储为链表(无上限)
   - 内核遍历pollfd数组,判断fd是否就绪
   - 数据就绪或者超时后,将pollfd数组拷贝到用户空间,并返回就绪的fd数量
   - 用户应用判断就绪fd数量是否大于0,如果大于0则遍历pollfd数组
   - 拿到fd信息,发送recvfrom请求。接下来就是将内核换粗拷贝到用户空间缓存,并将数据返回给客户端
3. 与select模式对比
   - select模式中的fd_set大小固定为1024，而pollfd在内核中采用链表，理论上无上限
   - 监听FD越多，每次遍历消耗时间也越久，性能反而会下降



#### IO多路复用-epoll模式

> epoll模式是对select和poll的改进,整体性能提升较大,高性能的IO框架使用的都是此模式。

![image-20230427230947939](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230427230947939.png)

IO多路复用模型-epoll模式过程：

- 首先调用epoll_create(int size),在内核创建一个eventpoll结构体
  - eventpoll结构体内有两个属性
    - 红黑树 rb_root。存放需要监听的FD
    - 链表list_head 。存放已经就绪的FD
- 然后调用epoll_ctl将FD加入到rb_root红黑树中
  - epoll_ctl命令会携带哪些参数
    - epfd   epoll实例句柄(用于回调监听)
    - op   选项,执行的操作类型(add、mod、del)
    - fd  需要监听的FD
    - epoll_event   监听的事件类型
  - 会为添加的FD添加回调函数,如果FD就绪就触发这个回调函数,将这个FD添加到list_head队列中
- 最后调用epoll_wait命令检查list_head中有没有就绪的FD
  - epoll_wait命令参数
    - epfd   epoll句柄
    - epoll_event  接收就绪的FD
    - maxevents  epoll_event数组长度
    - timeout  超时时间

#### 小结

select模式存在的三个问题：

* 能监听的FD最大不超过1024
* 每次select都需要把所有要监听的FD都拷贝到内核空间
* 每次都要遍历所有FD来判断就绪状态

poll模式的问题：

* poll利用链表解决了select中监听FD上限的问题，但依然要遍历所有FD，如果监听较多，性能会下降

epoll模式中如何解决这些问题的？

* 基于epoll实例中的红黑树保存要监听的FD，理论上无上限，而且增删改查效率都非常高
* 每个FD只需要执行一次epoll_ctl添加到红黑树，以后每次epol_wait无需传递任何参数，无需重复拷贝FD到内核空间
* 利用ep_poll_callback机制来监听FD状态，无需遍历所有FD，因此性能不会随监听的FD数量增多而下降

####  网络模型-基于epoll的服务器端流程

我们来梳理一下这张图

服务器启动以后，服务端会去调用epoll_create，创建一个epoll实例，epoll实例中包含两个数据

1、红黑树（为空）：rb_root 用来去记录需要被监听的FD

2、链表（为空）：list_head，用来存放已经就绪的FD

创建好了之后，会去调用epoll_ctl函数，此函数会会将需要监听的数据添加到rb_root中去，并且对当前这些存在于红黑树的节点设置回调函数，当这些被监听的数据一旦准备完成，就会被调用，而调用的结果就是将红黑树的fd添加到list_head中去(但是此时并没有完成)

3、当第二步完成后，就会调用epoll_wait函数，这个函数会去校验是否有数据准备完毕（因为数据一旦准备就绪，就会被回调函数添加到list_head中），在等待了一段时间后(可以进行配置)，如果等够了超时时间，则返回没有数据，如果有，则进一步判断当前是什么事件，如果是建立连接事件，则调用accept() 接受客户端socket，拿到建立连接的socket，然后建立起来连接，如果是其他事件，则把数据进行写出

![image-20230428002655327](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230428002655327.png)



### 网络模型-信号驱动IO



信号驱动IO是与内核建立SIGIO的信号关联并设置回调，当内核有FD就绪时，会发出SIGIO信号通知用户，期间用户应用可以执行其它业务，无需阻塞等待。

阶段一：

* 用户进程调用sigaction，注册信号处理函数
* 内核返回成功，开始监听FD
* 用户进程不阻塞等待，可以执行其它业务
* 当内核数据就绪后，回调用户进程的SIGIO处理函数

阶段二：

* 收到SIGIO回调信号
* 调用recvfrom，读取
* 内核将数据拷贝到用户空间
* 用户进程处理数据

![1653911776583](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653911776583.png)

问题：

- 当有大量IO操作时，信号较多，SIGIO处理函数不能及时处理可能导致信号队列溢出
- 而且内核空间与用户空间的频繁信号交互性能也较低。



### 异步IO

这种方式，不仅仅是用户态在试图读取数据后，不阻塞，而且当内核的数据准备完成后，也不会阻塞

他会由内核将所有数据处理完成后，由内核将数据写入到用户态中，然后才算完成，所以性能极高，不会有任何阻塞，全部都由内核完成，可以看到，异步IO模型中，用户进程在两个阶段都是非阻塞状态。

![1653911877542](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653911877542.png)

### 对比

![1653912219712](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653912219712.png)



## Redis单线程

**Redis到底是单线程还是多线程？**

* 如果仅仅聊Redis的核心业务部分（命令处理），答案是单线程
* 如果是聊整个Redis，那么答案就是多线程

在Redis版本迭代过程中，在两个重要的时间节点上引入了多线程的支持：

* Redis v4.0：引入多线程异步处理一些耗时较旧的任务，例如异步删除命令unlink
* Redis v6.0：在核心网络模型中引入 多线程，进一步提高对于多核CPU的利用率

因此，对于Redis的核心网络模型，在Redis 6.0之前确实都是单线程。是利用epoll（Linux系统）这样的IO多路复用技术在事件循环中不断处理客户端情况。

**为什么Redis要选择单线程？**

* 抛开持久化不谈，Redis是纯  内存操作，执行速度非常快，它的性能瓶颈是网络延迟而不是执行速度，因此多线程并不会带来巨大的性能提升。
* 多线程会导致过多的上下文切换，带来不必要的开销
* 引入多线程会面临线程安全问题，必然要引入线程锁这样的安全手段，实现复杂度增高，而且性能也会大打折扣

**redis为什么快？**

- 存内存操作
- 优秀的数据结构(结构体),他会根据数据特点选择合适的结构体。兼顾内存和性能
- epoll这样的多路复用IO,不断处理客户端请求,性能较高



## Redis网络模型



### Redis单线程网络模型

> Redis为了提升效率底层采用IO多路复用模型,并且采用epoll模式。
>
> 但是针对不同的操作系统,epoll模式实现肯定不同,所以Redis针对不同操作系统有不同的epoll实现,但是api大致相同

![image-20230428131239294](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230428131239294.png)

> Redis启动流程,源码。
>
> - redis启动执行`server.c`的`main`方法
>   - 初始化服务
>     - 创建一个结构体event_poll(红黑树+链表)---epoll_create
>     - 创建serversocketFD(RedisServer)  --- epoll_ctl
>     - 监听这个ssfd,并设置他的回调函数(也就是一个事件处理器,这个事件在ssfd可读时触发,也就是有client连接进来时触发)
>     - 注册一个前置处理器(处理csfd写事件)
>   - 循环监听注册的FD(包括ssfd、cfd)
>     - 处理csfd的写事件
>     - 等待fd就绪(如果redis刚启动,那么红黑树里只有一个FD就是ssfd)    --- epoll_wait
>     - epoll_wait返回就绪fd数量,遍历FD。FD就绪调用各自的回调函数(处理器)
>       - 如果是ssfd就绪,也就是说有客户端建立连接,触发ssfd的处理器。那么执行accept函数,将clientsocketFD注册进红黑树,监听csfd,并设置他的回调函数(readqueryfromclient)
>       - 如果是csfd就绪,也就是说client发送一条命令。那么执行csfd的命令请求处理器readqueryfromclient
>         - 获取客户端client
>         - 将请求写入客户端缓存buf
>         - 解析客户端请求,以空格分割,转化成argv数组存储
>         - 处理命令(根据argv[0],映射具体的api,比如pingCommand等)
>         - 命令执行完,结果写入client缓存,如果写不下则转换为链表存储
>         - 将客户端client,放入待输出队列(clients_penging_write),等待输出
>         - 触发命令回复处理器,此处理器在服务启动时注册,将结果输出到redis客户端

整个流程

![image-20230428153242785](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230428153242785.png)

客户端发出命令到redis服务触发csfd回调

![image-20230428153318365](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230428153318365.png)

前置处理器,处理客户端输出请求

![image-20230428153417333](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230428153417333.png)

1. Redis启动入口在`server.c`的main方法

    ```c
    int main(int argc, char **argv) {
        //①初始化evevtPoll结构体
        //②创建serverSocket
        //③监听serverSocketFD,设置回调函数当有client连接进来(ssfd就绪)如何处理
        //④注册前置处理器  处理client写事件
        // 总的来说初始化服务
        initServer();
        
        // 开启事件监听循环(epoll_wait等待ssfd、csfd就绪)
        aeMain(server.el);
        
    }
    ```

2. initServer()源码,具体初始化流程

   ```c
   void initServer(void) {
   	// 初始化evevtPoll结构体
       server.el = aeCreateEventLoop(server.maxclients+CONFIG_FDSET_INCR);
       // 当未配置port默认6379。监听TCP端口,创建ssfd
   	listenToPort(server.port,&server.ipfd) 
       // 监听ssfd,注册回调函数。当fd可读的时候触发回调函数(不仅仅只是放入就绪链表)
   	createSocketAcceptHandler(&server.ipfd, acceptTcpHandler)
       // 注册epoll_wait前置处理器。处理客户端写事件
       aeSetBeforeSleepProc(server.el,beforeSleep);
   }
   ```

3. aeMain()源码,及流程

   ```c
   void aeMain(aeEventLoop *eventLoop) {
       eventLoop->stop = 0;
       // 循环
       while (!eventLoop->stop) {
           aeProcessEvents(eventLoop, AE_ALL_EVENTS|
                                      AE_CALL_BEFORE_SLEEP|
                                      AE_CALL_AFTER_SLEEP);
       }
   }
   ```

   - aeProcessEvents处理就绪fd

     > ssfd什么时候就绪？有client连接进来就绪

     ```c
     int aeProcessEvents(aeEventLoop *eventLoop, int flags){
         // epoll_wait调用前置处理器,处理client的写请求
         eventLoop->beforesleep(eventLoop);
         // epoll_wait等待fd就绪。返回就绪fd数量
         numevents = aeApiPoll(eventLoop, tvp);
         for (j = 0; j < numevents; j++) {
             //遍历处理fd,调用对应处理器
         	...
         }
     }
     ```

   - redis服务刚启动只有ssfd,ssfd就绪后触发acceptTcpHandler处理器

     > ssfd何时准备就绪？就是有client建立连接时就绪，acceptTcpHandler函数就是注册监听csfd,并为其添加readQueryFromClient回调函数,当client发送命令时,csfd就绪,触发readQueryFromClient回调函数,处理命令。

     ```c
     void acceptTcpHandler(aeEventLoop *el, int fd, void *privdata, int mask) {
     	// 创建clientsocketfd
         fd = accept(s,sa,len);
         
         // 创建client连接,并关联fd
         connection *conn = connCreateSocket();
         conn->fd = fd;
         conn->state = CONN_STATE_ACCEPTING;
         
         // 监听clientSocketFD读事件,并添加回调函数
      	connSetReadHandler(conn,readQueryFromClient)
     }
     ```

     

![1653982278727](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653982278727.png)

当我们的客户端想要去连接我们服务器，会去先到IO多路复用模型去进行排队，会有一个连接应答处理器，他会去接受读请求，然后又把读请求注册到具体模型中去，此时这些建立起来的连接，如果是客户端请求处理器去进行执行命令时，他会去把数据读取出来，然后把数据放入到client中， clinet去解析当前的命令转化为redis认识的命令，接下来就开始处理这些命令，从redis中的command中找到这些命令，然后就真正的去操作对应的数据了，当数据操作完成后，会去找到命令回复处理器，再由他将数据写出。



## Redis通讯协议RESP

Redis是一个CS架构的软件，通信一般分两步（不包括pipeline和PubSub）：

- 客户端（client）向服务端（server）发送一条命令
- 服务端解析并执行命令，返回响应结果给客户端

因此客户端发送命令的格式、服务端响应结果的格式必须有一个规范，这个规范就是通信协议。

而在Redis中采用的是RESP（Redis Serialization Protocol）协议：

- Redis 1.2版本引入了RESP协议
- redis 2.0版本中成为与Redis服务端通信的标准，称为RESP2
- Redis 6.0版本中，从RESP2升级到了RESP3协议，增加了更多数据类型并且支持6.0的新特性--客户端缓存

但目前，默认使用的依然是RESP2协议，也是我们要学习的协议版本（以下简称RESP）。



在RESP中，通过首字节的字符来区分不同数据类型，常用的数据类型包括5种：

- 单行字符串：首字节是 ‘+’ ，后面跟上单行字符串，以CRLF（ "\r\n" ）结尾。例如返回"OK"： "+OK\r\n"

- 错误（Errors）：首字节是 ‘-’ ，与单行字符串格式一样，只是字符串是异常信息，例如："-Error message\r\n"

- 数值：首字节是 ‘:’ ，后面跟上数字格式的字符串，以CRLF结尾。例如：":10\r\n"

- 多行字符串：首字节是 ‘$’ ，表示二进制安全的字符串，最大支持512MB：

  - 如果大小为0，则代表空字符串："$0\r\n\r\n"

  - 如果大小为-1，则代表不存在："$-1\r\n"

- 数组：首字节是 ‘*’，后面跟上数组元素个数，再跟上元素，元素数据类型不限:

![image-20230428221327405](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/image-20230428221327405.png)



### 基于RESP模拟Redis客户端

> 使用Java基于RESP模拟Redis客户端。



## Redis内存回收-过期key清理

### Redis内存设置

在redis.conf中有内存配置

```properties
# maxmemory <bytes>
```

### key的TTL以及删除策略

> Redis内存有一定上限,避免内存超出,我们为一些Key设置ttl(超时时间),当我们下次访问已经过期的key,则会返回nil,打到内存回收的目的。
>
> 那么key的ttl如何存储,以及它的过期策略是增样的？

#### key的TTL存储方式

> 在redis中可以设置多个数据库,可以在redis.conf中配置`databases 16`来设置数据库数量。那么每一个数据库在Redis底层都是一个结构体`struct redisDb`,这个结构体的定义如下：

```c
typedef struct redisDb {
    dict *dict;                 /* The keyspace for this DB */
    dict *expires;              /* Timeout of keys with a timeout set */
    dict *blocking_keys;        /* Keys with clients waiting for data (BLPOP)*/
    dict *ready_keys;           /* Blocked keys that received a PUSH */
    dict *watched_keys;         /* WATCHED keys for MULTI/EXEC CAS */
    int id;                     /* Database ID */
    long long avg_ttl;          /* Average TTL, just for stats */
    unsigned long expires_cursor; /* Cursor of the active expire cycle. */
    list *defrag_later;         /* List of key names to attempt to defrag one by one, gradually. */
} redisDb;
```

- dict  dict

  > 这是一个字典,它用于保存次数据库的所有`key-value`的映射,其中维护着一个dictHT结构体,dictEntry中存储着key和value指向结构体的指针。

- dict  expires

  > 这也是一个字典,存储着设置了过期时间的`key-ttl`的映射。

- dict blocking_keys

  > 字典,存储特殊`key-value`映射。比如BLPOP

- ave_ttl

  > 用于统计,平均ttl

> 结论就是：key的过期时间会以`key-ttl`的方式存在一个Dict结构体中。

> RedisDB机构体如下：其中dictEntry中存储的是key-value的结构体(redisObject)指针

![1653983606531](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653983606531.png)



#### 惰性删除

> 顾名思义也就是不主动删除,即便ttl时间到期了也不会主动删除。而是在key被访问的时候检查ttl是否到期,如果到期了再删除。



#### 周期性删除

> 惰性删除存在一个缺陷,就是已经过了ttl时间的key,如果不去访问它,那么它将一直存在不会被删除,一直占用内存。所以redis又提供了周期性删除的过期key清理策略。

周期删除：通过一个定时任务，周期性的抽样部分过期的key，然后执行删除。执行周期有两种：

- Redis服务初始化函数initServer()中设置定时任务，按照server.hz(redis.conf中可配置,默认10hz,也就是每秒执行10次,每100ms执行一次)的频率来执行过期key清理，模式为SLOW

- Redis的每个事件循环前会调用beforeSleep()函数，执行过期key清理，模式为FAST

  > beforeSleep()在redis启动时设置,在循环监听ssfd和csfd时执行,用于处理csfd的写事件,将数据返回给客户端。beforeSleep()还有一个任务就是周期删除key(这个周期是2ms,清理速度1ms),清理key较多。

SLOW模式规则：

> SlOW模式,redis在每次执行过期key清理前都会检测一下近100ms时间内有没有清理过,如果没有才会执行slow模式周期性清理过期key,否则不执行清理。这样设计的目的是slow模型较慢,清理时间是几十毫秒级别,便面影响主线程

* 执行频率受server.hz影响，默认为10，即每秒执行10次，每个执行周期100ms。
* 执行清理耗时不超过一次执行周期的25%.默认slow模式耗时不超过25ms
* 逐个遍历db，逐个遍历db中的bucket，抽取20个key判断是否过期
* 如果没达到时间上限（25ms）并且过期key比例大于10%，再进行一次抽样，否则结束

FAST模式规则（过期key比例小于10%不执行 ）：

> fast模式在beforeSleep()中执行,每次循环监听都会执行,执行频率较高,执行周期较短,清理key较少。

* 执行频率受beforeSleep()调用频率影响，但两次FAST模式间隔不低于2ms
* 执行清理耗时不超过1ms
* 逐个遍历db，逐个遍历db中的bucket，抽取20个key判断是否过期
  如果没达到时间上限（1ms）并且过期key比例大于10%，再进行一次抽样，否则结束

#### 总结

RedisKey的TTL记录方式：

- 在RedisDB中通过一个Dict记录每个Key的TTL时间

过期key的删除策略：

- 惰性清理：每次查找key时判断是否过期，如果过期则删除
- 定期清理：定期抽样部分key，判断是否过期，如果过期则删除。

定期清理的两种模式：

- SLOW模式执行频率默认为10，每次不超过25ms
- FAST模式执行频率不固定，但两次间隔不低于2ms，每次耗时不超过1ms



## Redis内存淘汰策略

**内存淘汰**：就是当Redis内存使用达到设置的上限时，主动挑选部分key删除以释放更多内存的流程。

**执行内存淘汰的时机**：Redis会在处理客户端命令的方法processCommand()中尝试做内存淘汰：

![1653983978671](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653983978671.png)



**淘汰策略**

Redis支持8种不同策略来选择要删除的key：

* noeviction： 不淘汰任何key，但是内存满时不允许写入新数据，默认就是这种策略。

  ```properties
  # The default is:
  #
  # maxmemory-policy noeviction
  ```

* volatile-ttl： 对设置了TTL的key，比较key的剩余TTL值，TTL越小越先被淘汰

* allkeys-random：对全体key ，随机进行淘汰。也就是直接从db->dict中随机挑选

* volatile-random：对设置了TTL的key ，随机进行淘汰。也就是从db->expires中随机挑选。

* allkeys-lru： 对全体key，基于LRU算法进行淘汰

* volatile-lru： 对设置了TTL的key，基于LRU算法进行淘汰

* allkeys-lfu： 对全体key，基于LFU算法进行淘汰

* volatile-lfu： 对设置了TTL的key，基于LFI算法进行淘汰

  

比较容易混淆的有两个：

* LRU（Least Recently Used），最长时间未被使用。用当前时间减去最后一次访问时间，这个值越大则淘汰优先级越高。
* LFU（Least Frequently Used），最少频率使用。会统计每个key的访问频率，值越小淘汰优先级越高。



Redis的数据都会被封装为RedisObject结构：

![1653984029506](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653984029506.png)



LFU的访问次数之所以叫做逻辑访问次数，是因为并不是每次key被访问都计数，而是通过运算：

* 生成0~1之间的随机数R
* 计算 1 / (旧次数 *lfu_log_factor + 1)，记录为P 。 lfu_log_factor默认10
* 如果 R < P ，则计数器 + 1，且最大不超过255
* 访问次数会随时间衰减，距离上一次访问时间每隔 lfu_decay_time 分钟，计数器 -1

最后用一副图来描述当前的这个流程吧

![1653984085095](https://xiaochuang6.oss-cn-shanghai.aliyuncs.com/nosql/redis/reids_yuanli/1653984085095.png)