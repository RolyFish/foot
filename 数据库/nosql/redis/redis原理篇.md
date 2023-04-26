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
- pauserehash  rehash是否开始

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