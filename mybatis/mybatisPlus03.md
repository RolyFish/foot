mybatisPlus标准crud接口



> [xxxx]()



#### IService

```java
public interface IService<T> 
```

通用 Service CRUD 封装接口，进一步封装 CRUD 采用 `get 查询单行``remove 删除` `list 查询集合` `page 分页` 前缀命名方式区分 `Mapper` 层避免混淆。

T为任意实体。

##### ServiceImpl

mybatis提供的实现类。M为mapper接口    T为任意实体。

```java
public class ServiceImpl<M extends BaseMapper<T>, T> implements IService<T> 
```

##### save

```java
default boolean save(T entity) {
    return SqlHelper.retBool(getBaseMapper().insert(entity));
}
default boolean saveBatch(Collection<T> entityList) {
    return saveBatch(entityList, DEFAULT_BATCH_SIZE);
}
boolean saveBatch(Collection<T> entityList, int batchSize);
```



##### saveOrUpdate

```java
// TableId 注解存在更新记录，否插入一条记录
boolean saveOrUpdate(T entity);
// 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
default boolean saveOrUpdate(T entity, Wrapper<T> updateWrapper) {
    return update(entity, updateWrapper) || saveOrUpdate(entity);
} 
default boolean saveOrUpdateBatch(Collection<T> entityList) {
    return saveOrUpdateBatch(entityList, DEFAULT_BATCH_SIZE);
}
boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize);
```



##### Remove

```java
//根据 wraper 条件删除
default boolean remove(Wrapper<T> queryWrapper) {
    return SqlHelper.retBool(getBaseMapper().delete(queryWrapper));
}
//批量删除
default boolean removeByIds(Collection<?> list) {
    if (CollectionUtils.isEmpty(list)) {
        return false;
    }
    return SqlHelper.retBool(getBaseMapper().deleteBatchIds(list));
}
default boolean removeByIds(Collection<?> list, boolean useFill) {
    if (CollectionUtils.isEmpty(list)) {
        return false;
    }
    if (useFill) {
        return removeBatchByIds(list, true);
    }
    return SqlHelper.retBool(getBaseMapper().deleteBatchIds(list));
}
/*批量删除(jdbc批量提交)*/
default boolean removeBatchByIds(Collection<?> list) {
    return removeBatchByIds(list, DEFAULT_BATCH_SIZE);
}
default boolean removeBatchByIds(Collection<?> list, boolean useFill) {
    return removeBatchByIds(list, DEFAULT_BATCH_SIZE, useFill);
}
```



##### Update

```java
default boolean updateById(T entity) {
    return SqlHelper.retBool(getBaseMapper().updateById(entity));
}
default boolean update(Wrapper<T> updateWrapper) {
    return update(null, updateWrapper);
}
default boolean update(T entity, Wrapper<T> updateWrapper) {
    return SqlHelper.retBool(getBaseMapper().update(entity, updateWrapper));
}
default boolean updateBatchById(Collection<T> entityList) {
    return updateBatchById(entityList, DEFAULT_BATCH_SIZE);
}
boolean updateBatchById(Collection<T> entityList, int batchSize);
```



##### Get



```java
default T getById(Serializable id) {
    return getBaseMapper().selectById(id);
}
Map<String, Object> getMap(Wrapper<T> queryWrapper);
<V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
default T getOne(Wrapper<T> queryWrapper) {
    return getOne(queryWrapper, true);
}
T getOne(Wrapper<T> queryWrapper, boolean throwEx);
```



##### List

```java
// 查询所有
List<T> list();
// 查询列表
List<T> list(Wrapper<T> queryWrapper);
// 查询（根据ID 批量查询）
Collection<T> listByIds(Collection<? extends Serializable> idList);
// 查询（根据 columnMap 条件）
Collection<T> listByMap(Map<String, Object> columnMap);
// 查询所有列表
List<Map<String, Object>> listMaps();
// 查询列表
List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper);
// 查询全部记录
List<Object> listObjs();
// 查询全部记录
<V> List<V> listObjs(Function<? super Object, V> mapper);
// 根据 Wrapper 条件，查询全部记录
List<Object> listObjs(Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询全部记录
<V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
```



##### page 

```java
// 无条件分页查询
IPage<T> page(IPage<T> page);
// 条件分页查询
IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper);
// 无条件分页查询
IPage<Map<String, Object>> pageMaps(IPage<T> page);
// 条件分页查询
IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper);
```



##### count

```java
// 查询总记录数
int count();
// 根据 Wrapper 条件，查询总记录数
int count(Wrapper<T> queryWrapper);
```



##### Chain

query

```java
// 链式查询 普通
QueryChainWrapper<T> query();
// 链式查询 lambda 式。注意：不支持 Kotlin
LambdaQueryChainWrapper<T> lambdaQuery();

// 示例：
query().eq("column", value).one();
lambdaQuery().eq(Entity::getId, value).list();
```

update

```java
// 链式更改 普通
UpdateChainWrapper<T> update();
// 链式更改 lambda 式。注意：不支持 Kotlin
LambdaUpdateChainWrapper<T> lambdaUpdate();

// 示例：
update().eq("column", value).remove();
lambdaUpdate().eq(Entity::getId, value).update(entity);
```

