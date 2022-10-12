## Pdai



###  Java特性



#### Optional

> NPE空指针异常不可避免，那么如何处理呢？
>
> Java8引入Optional类，这是一个容器对象，容器内可存储null值，通过Optional的IsPresent()方法可判断内部元素是否为null。且内部丰富api可灵活操作

##### Optional的创建

> Optional的两个构造方法都是私有的，也就是不供外部使用，也就是我们不可正常通过构造方法来创建Optional对象。但是还好Optional为我们提供了三个静态方法来创建Optional对象。

```java
private Optional() {
    this.value = null;
}
private Optional(T value) {
  this.value = Objects.requireNonNull(value);
}
```

> 如下三个静态方法

```java
public static<T> Optional<T> empty() {
    @SuppressWarnings("unchecked")
    Optional<T> t = (Optional<T>) EMPTY;
    return t;
}
public static <T> Optional<T> of(T value) {
  return new Optional<>(value);
}
public static <T> Optional<T> ofNullable(T value) {
  return value == null ? empty() : of(value);
}
```



##### 基本方法

- empty

  > 创建一个内部元素==为null==的Optional对象

  ```java
  /**
   * 创建一个内部元素为null的Optional对象
   */
  @Test
  public void testEmpty() {
      final Optional<Object> empty = Optional.empty();
  }
  ```

- of

  > 创建一个内部元素==不可为null==的Optional对象
  >
  > 如果传入nul值则会抛出NPE异常。

  ```java
  final Optional<String> optional = Optional.of("str");
  ```

- ofNullable

  > 与of()方法相似，只不过==允许为null==创建optional

  ```java
  final Optional<String> optionalOfNullable = Optional.ofNullable(null);
  ```

- isPresent

  > 判断Optional容器内的对象是否为null。不为null返回true，为null返回false

  ```java
  final boolean present = optionalOfNullable.isPresent();
  ```

- get

  > 返回Optional容器中的值。
  >
  > 存在则返回，不存在抛异常  NoSuchElementException。
  
  ```java
  final String s = optional.get();
  ```

- ifPresent

  > 如果存在则执行消费方法，如果不存在则跳过消费方法。
  
  ```java
  optional.ifPresent(System.out::println);
  ```

- orElse

  > Optional容器中颗值不为null则返回对应值，如果为null则返回默认值(即orElse的参数)
  
  ```java
  optionalOfNullable.orElse("other");
  ```

- orElseGet

  > 与orElse相似，不同的是此方法执行`Supplier`实现类的`get()`方法返回的值
  
  ```java
  optionalOfNullable.orElseGet(String::new);
  ```

- orElseThrow

  > 存在返回，不存在则抛出异常
  
  ```java
  optionalOfNullable.orElseThrow(NullPointerException::new);
  ```

- Map

  > Optional容器中值为null则返回空Optional(即执行empty()方法，内部元素为null)，否则映射为一个==可存null值==的新的Optional，此Optional的值为`Function  mapper`接口实现类的返回结果。
  >
  > 即执行Optional.ofNullable(mapper.apply(value));
  
  ```java
  final Optional<String> optionalS = optional.map(ele -> "map");
  ```

- FlatMap

  > Optional容器中值为null则返回空Optional(即执行empty()方法，内部元素为null)，否则映射为一个==不可存null值==的新的Optional，此Optional的值为`Function  mapper`接口实现类的返回结果。
  >
  > 即执行Objects.requireNonNull(mapper.apply(value));
  
  ```java
  final Optional<String> optionalFlatMap = optional.flatMap(ele -> Optional.of("map"));
  ```

- filter

  > 过滤，符合条件返回this，不符合返回empty

  ```java
  //过滤，符合条件返回this，不符合返回empty
  final Optional<String> optionalS1 = optional.filter(ele -> ele.equals(""));
  ```

##### 使用

> 多层次的null值判断

bean：

```java
@Data
class Element1 {
    Element2 element2;
    @Data
    static class Element2 {
        Element3 element3;

        @Data
        static class Element3 {
            String str;
        }
    }
}
```

一般处理：

```java
final Element1 element1 = new Element1();
if (null != element1 && null != element1.getElement2() && null != element1.getElement2().getElement3()) {
    System.out.println(element1.getElement2().getElement3().getStr());
}
```

使用Optional：

```java
final Element1 element1 = new Element1();
Optional.ofNullable(element1).map(Element1::getElement2).map(Element1.Element2::getElement3).ifPresent(System.out::println);
```



**Optional+Supplier实现**

```java
class OptionalUtil {
    public static <T> Optional<T> resolve(Supplier<T> supplier) {
        try {
            T t = supplier.get();
            return Optional.ofNullable(t);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
```

```java
final Element1 element1 = new Element1();

OptionalUtil.resolve(() -> element1.getElement2().getElement3()).ifPresent(ele -> {
    System.out.println(ele.getStr());
});
```



#### 接口可定义default方法

> java接口中可定义默认方法。

