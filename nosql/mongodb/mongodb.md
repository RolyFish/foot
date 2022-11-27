# mongodb学习



## 基础

### 安装

### 工具



### 概念

- database：数据库
- collection  集合 ==> sql中的table
- document  文档  ==> sql中的row
- field  属性（域）  ===> sql中的column
- index 索引
- primary  key   主键   mongodb会自动生成一个主键：'_id'



### shell命令

> 使用`mongosh`连接mongodb实例。

#### 数据库

> 查看所有数据库：

```bash
show dbs;
```

![image-20221125102112558](mongodb.assets/image-20221125102112558.png)



> 创建数据库

```powershell
use testdb;
```



![image-20221125102351587](mongodb.assets/image-20221125102351587.png)

> 删除数据库。
>
> db命令可查看当前数据库名称

```powershell
db;
db.dropDatabase('testdb');
```

![image-20221125102901716](mongodb.assets/image-20221125102901716.png)

#### 集合

> 创建集合，查看集合

options可选参数：

```powershell
db.createCollection("collectionname",options);

db.shwocollections;
db.showtables;
```

![image-20221125103219295](mongodb.assets/image-20221125103219295.png)



> 无需创建集合，往集合插入数据时自动创建。

![image-20221125104405685](mongodb.assets/image-20221125104405685.png)

> 删除集合

```powershell
db.collection.drop();
```

![image-20221125105402405](mongodb.assets/image-20221125105402405.png)



##### 插入文档

> 插入文档到集合中，文档的数据结构和 JSON 基本一样。所有存储在集合中的数据都是 BSON 格式。
>
> BSON 是一种类似 JSON 的二进制形式的存储格式，是 Binary JSON 的简称。
>
> 如果当下集合不存在，插入动作将会创建该集合。

- 直接插入  db.insert({})

  > 可选择插入单个，也可选择插入多个

  ```powershell
  db.collection.insert(
     <document or array of documents>,
     {
       writeConcern: <document>,
       ordered: <boolean>
     }
  )
  ```

  例：

  ```json
  db.valuse.insert(
      [
         {
             user2:{
                 username:'rolyfish',
                 userage:22
             }
         }
      ],
      {
         writeConcern:1,
         ordered:true
      }
  )
  ```

- 插入单个文档  db.insertOne({})

  

  ```powershell
  db.collection.insertOne(
     <document>,
     {
        writeConcern: <document>
     }
  )
  ```

  例：

  ```json
  db.values.insertOne(
    {
    user2: {
      username: 'rolyfish',
      userage: 22
    }
    },
    {
      writeConcern: 1
    }
  )
  ```

- 插入多个文档  db.insertMany([])

  ```powershell
  db.collection.insertMany(
     [ <document 1> , <document 2>, ... ],
     {
        writeConcern: <document>,
        ordered: <boolean>
     }
  )
  ```

  ```json
  db.values.insertMany(
    [
      {
        user2: {
          username: 'username',
          userage: 22
        }
      }
    ],
    {
    writeConcern: 1,
    ordered: true
    }
  )
  ```

参数说明：

- document：要写入的文档。
- writeConcern：写入策略，默认为 1，即要求确认写操作，0 是不要求。
- ordered：指定是否按顺序写入，默认 true，按顺序写入。



###### 插入预定义元素

> 可以先预定义属性再执行插入

```powershell
test> var users = [{user:{username:'user1',userage:1}},{user:{username:'user1',userage:1}},{user:{username:'user1',userage:1}}]

test> db.users.insert(users)
{
  acknowledged: true,
  insertedIds: {
    '0': ObjectId("638078333b88d4e3465dde03"),
    '1': ObjectId("638078333b88d4e3465dde04"),
    '2': ObjectId("638078333b88d4e3465dde05")
  }
}
```



##### 查询文档(query document)

> 初始化数据

[官网例子]([Query Documents — MongoDB Manual](https://www.mongodb.com/docs/v3.6/tutorial/query-documents/))



###### 查询集合中所有文档

```powershell
db.collection.find({})
```



###### 条件查询

- 简单等于匹配

> `{ <field1>: <value1>, ... }`

>以下例子field为  field.field

```powershell
 db.inventory.find({'size.h':8.5})
```

- 条件操作符 in

  > `{ <field1>: { <operator1>: <value1> }, ... }`

```shell
db.inventory.find({'status':{$in:['A','D']}})
```

- 条件操作符lt  lte  gt   gte

```powershell
db.inventory.find({'size.h':{$gt:20}});
db.inventory.find({'size.h':{$lt:20}})
db.inventory.find({'size.h':{$lte:10}})
db.inventory.find({'size.h':{$gte:10}})
```

- 条件   and

```powershell
db.inventory.find({'status':'A','size.h':{$gte:10}})
```

等价于关系型数据库条件查询语句：

```sql
select * from inventory where status = A and size.h >= 10
```

- 条件or

```powershell
 db.inventory.find({$or:[{'status':'A'},{'qty':{$gte:45}}]})
```

```sql
select * from inventory where status = A or qty >= 45
```

```shell
db.inventory.find({$or:[{'status':'A'},{'qty':{$gte:45}}], 'size.h':{$gt:20}})
```

```sql
select * from inventory where (status = A or qty >= 45) and size.h > 20
```



###### 匹配集合

初始化数据

```powershell
db.col.insertMany([
   { item: "journal", qty: 25, tags: ["blank", "red"], dim_cm: [ 14, 21 ] },
   { item: "notebook", qty: 50, tags: ["red", "blank"], dim_cm: [ 14, 21 ] },
   { item: "paper", qty: 100, tags: ["red", "blank", "plain"], dim_cm: [ 14, 21 ] },
   { item: "planner", qty: 75, tags: ["blank", "red"], dim_cm: [ 22.85, 30 ] },
   { item: "postcard", qty: 45, tags: ["blue"], dim_cm: [ 10, 15.25 ] }
]);
```

- 集合判等

> 以下例子就是集合完全相等包括元素个数，顺序以及值

```powershell
db.col.find( { tags: ["red", "blank"]})
```

- 集合包含所有元素

> 以下例子匹配都包含red和blank元素的集合，但是不关心其顺序的集合

```powershell
db.col.find( { tags: { $all: ["red", "blank"]}})
```

- 集合包含一个元素

> 集合中至少有一个元素匹配

```powershell
db.col.find({tags:'blank'})
db.col.find({dim_cm:{$gt:10,$lt:20}})
```

- 匹配指定集合元素

> 指定集合的指定元素匹配条件。
>
> 指定下标即可

```powershell
db.col.find({'dim_cm.0': { $gt: 19, $lt: 30 }})
```

- 匹配集合个数

```powershell
db.col.find({'tags': { $size: 2 }})
```



###### 匹配对象集合

> 集合中的元素不是字符串，而是对象。

初始化数据

```powershell
db.cole.insertMany( [
{ item: "journal", instock: [ { warehouse: "A", qty: 5 }, { warehouse: "C", qty: 15 } ] },
{ item: "notebook", instock: [ { warehouse: "C", qty: 5 } ] },
{ item: "paper", instock: [ { warehouse: "A", qty: 60 }, { warehouse: "B", qty: 15 } ] },
{ item: "planner", instock: [ { warehouse: "A", qty: 40 }, { warehouse: "B", qty: 5 } ] },
{ item: "postcard", instock: [ { warehouse: "B", qty: 15 }, { warehouse: "C", qty: 35 } ] }
]);
```

- 集合中包含对象

> instock集合中至少有一个元素匹配指定条件。

```powershell
db.cole.find({instock:{ warehouse: "A", qty: 5 }})
```

> 需要注意的是:{ warehouse: "A", qty: 5 }顺序不可倒

- 集合中对象属性

> instock集合中至少有一个元素对象的qty属性大于15

````shell
db.cole.find({'instock.qty':{$gt:15}});
````

- 忽略第一点顺序过滤

> 第一点注重属性顺序，可以使用$elemMatch忽略顺序

```powershell
db.cole.find( { "instock": {$elemMatch:{qty:15, warehouse: "B"}}})
```



###### 操作符

> $type,属性操作符。
>
> 以下例子为：匹配item属性为字符串的文档

```powershell
 db.cole.find({item:{$type:2}})
```

BSON   type列表：

| Type                    | Number | Alias                 | Notes               |
| :---------------------- | :----- | :-------------------- | :------------------ |
| Double                  | 1      | “double”              |                     |
| String                  | 2      | “string”              |                     |
| Object                  | 3      | “object”              |                     |
| Array                   | 4      | “array”               |                     |
| Binary data             | 5      | “binData”             |                     |
| Undefined               | 6      | “undefined”           | Deprecated.         |
| ObjectId                | 7      | “objectId”            |                     |
| Boolean                 | 8      | “bool”                |                     |
| Date                    | 9      | “date”                |                     |
| Null                    | 10     | “null”                |                     |
| Regular Expression      | 11     | “regex”               |                     |
| DBPointer               | 12     | “dbPointer”           | Deprecated.         |
| JavaScript              | 13     | “javascript”          |                     |
| Symbol                  | 14     | “symbol”              | Deprecated.         |
| JavaScript (with scope) | 15     | “javascriptWithScope” |                     |
| 32-bit integer          | 16     | “int”                 |                     |
| Timestamp               | 17     | “timestamp”           |                     |
| 64-bit integer          | 18     | “long”                |                     |
| Decimal128              | 19     | “decimal”             | New in version 3.4. |
| Min key                 | -1     | “minKey”              |                     |
| Max key                 | 127    | “maxKey”              |                     |



> $exists,    属性是否存在

```powershell
 db.cole.find({itemx:{$exists:false}})
```



##### 更新文档

初始化数据

```powershell
var users = 
[
  {name:'rolyfish',age:18,hobby:['game','ball'],status:'A'},
  {name:'xiaoming',age:21,hobby:['game','run'],status:'B'},
  {name:'xiaohong',age:20,hobby:['game','ball'],status:'B'},
  {name:'tangsan',age:23,hobby:['game','ball'],status:'A'}
];
db.users.insertMany(users);
```



updateApi:

```powershell
db.collection.updateOne();
db.collection.updateMany();
db.collection.replaceOne();
db.collection.update();
```

updateForm:

```powershell
{
  <update operator>: { <field1>: <value1>, ... },
  <update operator>: { <field2>: <value2>, ... },
  ...
}
```

一些更新选项比如$set，当属性不存在时会创建属性。

###### 属性

| Name                                                         | Description                                                  |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [`$currentDate`](https://www.mongodb.com/docs/manual/reference/operator/update/currentDate/#mongodb-update-up.-currentDate) | Sets the value of a field to current date, either as a Date or a Timestamp. |
| [`$inc`](https://www.mongodb.com/docs/manual/reference/operator/update/inc/#mongodb-update-up.-inc) | Increments the value of the field by the specified amount.   |
| [`$min`](https://www.mongodb.com/docs/manual/reference/operator/update/min/#mongodb-update-up.-min) | Only updates the field if the specified value is less than the existing field value. |
| [`$max`](https://www.mongodb.com/docs/manual/reference/operator/update/max/#mongodb-update-up.-max) | Only updates the field if the specified value is greater than the existing field value. |
| [`$mul`](https://www.mongodb.com/docs/manual/reference/operator/update/mul/#mongodb-update-up.-mul) | Multiplies the value of the field by the specified amount.   |
| [`$rename`](https://www.mongodb.com/docs/manual/reference/operator/update/rename/#mongodb-update-up.-rename) | Renames a field.                                             |
| [`$set`](https://www.mongodb.com/docs/manual/reference/operator/update/set/#mongodb-update-up.-set) | Sets the value of a field in a document.                     |
| [`$setOnInsert`](https://www.mongodb.com/docs/manual/reference/operator/update/setOnInsert/#mongodb-update-up.-setOnInsert) | Sets the value of a field if an update results in an insert of a document. Has no effect on update operations that modify existing documents. |
| [`$unset`](https://www.mongodb.com/docs/manual/reference/operator/update/unset/#mongodb-update-up.-unset) | Removes the specified field from a document.                 |

> 更新操作符$set用法.

filter operator  +    set operator

```powershell
db.users.updateOne({name:'tangsan'},{$set:{age:20}})
```

如果更新的属性不存在则该文档创建属性

```powershell
db.users.updateOne({name:'tangsan'},{$set:{sex:0}})
```

> 跟新操作符 `$max   $min`  

{age:29}  如果29j较大则更新，否则不更新

```powershell
db.users.updateOne({name:'rolyfish'},{$max:{age:29}})
```

`$min`也是同样

```powershell
 db.users.updateOne({name:'rolyfish'},{$min:{age:29}})
```

> `$mul`  指定属性乘以指定数值。
>
> 以下例子age×2

```powershell
db.users.updateOne({name:'rolyfish'},{$mul:{age:2}})
```

> `$remane`重命名属性

```powershell
db.users.updateOne({name:'rolyfish'},{$rename:{age:'ages'}})
```

> $setOnInsert
>
> 如果update的更新参数upsert:true，也就是如果要更新的文档不存在的话会插入一条新的记录，`$setOnInsert`操作符会将指定的值赋值给指定的字段，如果要更新的文档存在那么`$setOnInsert`操作符不做任何处理；

```powershell
-- 不做处理
db.users.updateOne(
  {name:'tangsan'},
  {$setOnInsert:{value:'setOnInsert'}},
  {upsert:true}
)
-- 插入  user{name:'tangsanx',value:'setOnInsert'}
db.users.updateOne(
  {name:'tangsanx'},
  {$setOnInsert:{value:'setOnInsert'}},
  {upsert:true}
)
```

> `$inc`  增量

```powershell
db.users.updateOne({name:'tangsan'},{$inc:{age:10}})
```

> `$unset`  移除某个文档的指定属性

```powershell
db.users.updateOne({name:'tangsan'},{$unset:{age:'',sex:''}})
```

> `$currentDate`更新日期为当前时间.
>
> 如果字段不存在则创建该字段。
>
> 属性操作符`$type`可修改属性

```shell
db.users.updateOne({name:'tangsan'},{$currentDate:{modifilyDate:true}});

db.users.updateOne({name:'tangsan'},{$currentDate:{modifilyTimestamp:{$type:`timestamp`}}})
```



###### 集合

| Name                                                         | Description                                                  |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [`$`](https://www.mongodb.com/docs/manual/reference/operator/update/positional/#mongodb-update-up.-) | Acts as a placeholder to update the first element that matches the query condition. |
| [`$[]`](https://www.mongodb.com/docs/manual/reference/operator/update/positional-all/#mongodb-update-up.---) | Acts as a placeholder to update all elements in an array for the documents that match the query condition. |
| [`$[identifier]`](https://www.mongodb.com/docs/manual/reference/operator/update/positional-filtered/#mongodb-update-up.---identifier--) | Acts as a placeholder to update all elements that match the `arrayFilters` condition for the documents that match the query condition. |
| [`$addToSet`](https://www.mongodb.com/docs/manual/reference/operator/update/addToSet/#mongodb-update-up.-addToSet) | Adds elements to an array only if they do not already exist in the set. |
| [`$pop`](https://www.mongodb.com/docs/manual/reference/operator/update/pop/#mongodb-update-up.-pop) | Removes the first or last item of an array.                  |
| [`$pull`](https://www.mongodb.com/docs/manual/reference/operator/update/pull/#mongodb-update-up.-pull) | Removes all array elements that match a specified query.     |
| [`$push`](https://www.mongodb.com/docs/manual/reference/operator/update/push/#mongodb-update-up.-push) | Adds an item to an array.                                    |
| [`$pullAll`](https://www.mongodb.com/docs/manual/reference/operator/update/pullAll/#mongodb-update-up.-pullAll) | Removes all matching values from an array.                   |



初始化数据：

```shell
var stus = [{ "_id" : 1, "grades" : [ 85, 82, 80 ] },
{ "_id" : 2, "grades" : [ 88, 90, 92 ] },
{ "_id" : 3, "grades" : [ 85, 100, 90 ] }];
db.collection.insertMany(stus);
```

> `$`更新匹配到的首个集合内的首个元素

```shell
db.collection.updateOne(
   { <query selector> },
   { <update operator>: { "array.$.field" : value } }
)
```

```shell
 ## query filter中匹配文档条件必须有集合。可以匹配到多个
 db.stus.updateOne(
 		{_id:1,grades:80},
 		{$set:{'grades.$':80}}
 );
```

> `$[]`。更新匹配到的首个集合内的所有元素

```shell
db.collection.updateOne(
   { <query conditions> },
   { <update operator>: { "<array>.$[].field" : value } }
)
```

```shell
db.stus.updateOne(
		{grades:99},
		{$set:{'grades.$[]':999}}
)
```

```json
var stus = [
  {
    'name': 'rolyfiush',
    'grades': [
      {
        'name': 'math',
        'source': 90,
        'sort': 1
      },
      {
        'name': 'english',
        'source': 85,
        'sort': 2
      },
      {
        'name': 'chinese',
        'source': 90,
        'sort': 1
      }
    ]
  },
  {
    'name': 'lizicheng',
    'grades': [
      {
        'name': 'math',
        'source': 90,
        'sort': 1
      },
      {
        'name': 'english',
        'source': 85,
        'sort': 2
      },
      {
        'name': 'chinese',
        'source': 90,
        'sort': 1
      }
    ]
  },
  {
    'name': 'chuangwang',
    'grades': [
      {
        'name': 'math',
        'source': 90,
        'sort': 1
      },
      {
        'name': 'english',
        'source': 85,
        'sort': 2
      },
      {
        'name': 'chinese',
        'source': 90,
        'sort': 1
      }
    ]
  }
];
db.stus.insertMany(stus);
```

```shell
db.stus.updateOne(
		{name:'rolyfish'},
		{$set:{'grades.$[].sort':11}}
)
```

> `$[identifier]`。带有过滤条件,该`arrayFilters`参数允许您指定过滤文档的数组，以确定要修改的数组元素。identifier为符合过滤条件的数组元素

```shell
db.collection.updateOne(
   { <query conditions> },
   { <update operator>: { "<array>.$[<identifier>]" : value } },
   { arrayFilters: [ { <identifier>: <condition> } ] }
)
```

```shell
db.stus.updateOne(
		{name:'lizicheng'},
		{$set:{'grades.$[ele].sort':2}},
		{arrayFilters:[{'ele.name':'math'}]}
)
```

> `$addToSet`,addToSet操作添加一个元素到数组除非该元素已经存在。

```shell
db.sources.insertMany({'a','b','c'});
```

```shell
## 不做操作
db.sources.updateOne({},{$addToSet:{sources:'c'}});
## 添加元素 ‘d’到集合
db.sources.updateOne({},{$addToSet:{sources:'d'}});
## 添加元素['d','e']到集合
db.sources.updateOne({},{$addToSet:{sources:['d','e']}})
## 想要批量添加元素  则使用$each操作符
db.sources.updateOne({},{$addToSet:{sources:{$each:['x','y','z']}}})
```

> `$pop`,弹出一个集合元素，1 从右边弹出， -1 从左边弹出

```shell
{ $pop: { <field>: <-1 | 1>, ... } };
```

```shell
db.sources.updateOne({},{$pop:{sources:1}});
```

> `$pull`  从一个已存在的集合中移出匹配元素。只移出匹配元素，未匹配元素忽略

```shell
db.sources.updateOne({},{$pull:{sources:{$in:['b','c','x']}}});
```

> `$push`  从一个已存在的集合中插入指定元素。可重复

```shell
## 初始化集合
db.students.insertOne( { _id: 1, scores: [ 44, 78, 38, 80 ] } )
## ok  可以插入
db.students.updateOne({},{$push:{scores:80}});
## 使用$each控制符 插入多个元素
db.students.updateOne({},{$push:{scores:{$each:[10,20,30]}}});
## 使用$sort 控制符可进行排序
db.students.updateOne({},{$push:{scores:{$each:[],$sort:1}}});
## 使用$slice 截断数组  保留前三个，如果存在排序控制则阶段发生在排序之后
db.students.updateOne({},{$push:{scores:{$each:[],$slice:3}}});
```

> `$pullAll`

```shell
db.students.updateOne({},{$push:{scores:{$each:[10,20,30,10,20,30],$sort:1}}});
## 和$pull一样，可以一次性移出多个匹配值。只是语法差异
db.students.updateOne({},{$pullAll:{scores:[10,20]}});
```



###### Modifiers

| Name                                                         | Description                                                  |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [`$each`](https://www.mongodb.com/docs/v3.6/reference/operator/update/each/#up._S_each) | Modifies the [`$push`](https://www.mongodb.com/docs/v3.6/reference/operator/update/push/#up._S_push) and [`$addToSet`](https://www.mongodb.com/docs/v3.6/reference/operator/update/addToSet/#up._S_addToSet) operators to append multiple items for array updates. |
| [`$position`](https://www.mongodb.com/docs/v3.6/reference/operator/update/position/#up._S_position) | Modifies the [`$push`](https://www.mongodb.com/docs/v3.6/reference/operator/update/push/#up._S_push) operator to specify the position in the array to add elements. |
| [`$slice`](https://www.mongodb.com/docs/v3.6/reference/operator/update/slice/#up._S_slice) | Modifies the [`$push`](https://www.mongodb.com/docs/v3.6/reference/operator/update/push/#up._S_push) operator to limit the size of updated arrays. |
| [`$sort`](https://www.mongodb.com/docs/v3.6/reference/operator/update/sort/#up._S_sort) | Modifies the [`$push`](https://www.mongodb.com/docs/v3.6/reference/operator/update/push/#up._S_push) operator to reorder documents stored in an array. |

控制符，`$each 所有 、$slice 截断数组、$sort 排序（1、-1）`

`$position`可指定元素位置，比如插入位置

```shell
## 从数组下标为1处开始插入元素
db.students.updateOne({},{$push:{scores:{$each:[1,2,3],$position:1}}});

```



##### 删除指定文档

Api:

```shell
db.collection.deleteOne(
   <filter>,
   {
      writeConcern: <document>,## 写入策略，可设置超时响应等
      collation: <document> 
   }
)

db.collection.deleteMany(
   <filter>,
   {
      writeConcern: <document>,
      collation: <document>
   }
)
```



```shell
db.col.insertMany(
  [
    { _id: 1, category: "café", status: "A" }, 
    { _id: 2, category: "cafe", status: "a" }, 
    { _id: 3, category: "cafE", status: "a" }
  ]
)
```

```shell
db.col.deleteOne({status:'a'})
db.col.deleteMany({status:'a'})
```

###### remove

> remove方法也可以移出元素

```shell
db.collection.remove(
   <query>,
   {
     justOne: <boolean>,
     writeConcern: <document>,
     collation: <document>
   }
)
```

```shell
db.col.remove({status:'a'},{justOne:true})
```



##### bulkWrite

> 批量操作
