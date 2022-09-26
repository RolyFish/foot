## Pdai



###  Java特性



#### Optional

> NPE空指针异常不可避免，那么如何处理呢？
>
> Java8引入Optional类，这是一个容器对象，容器内可存储null值，通过Optional的IsPresent()方法可判断内部元素是否为null。且内部丰富api可灵活操作z

##### 基本方法

- of

  > 为非null的值创建Optional。
  >
  > 如果传入nul值则会抛出NPE异常。

  ```java
  final Optional<String> optional = Optional.of("str");
  ```

- ofNullable

  > 与of()方法相似，只不过允许为null创建optional

  ```java
  final Optional<String> optionalOfNullable = Optional.ofNullable(null);
  ```

- isPresent

  > 判断Optional容器内是否为null

  ```java
  final boolean present = optionalOfNullable.isPresent();
  ```

- get

  > get返回Optional容器中的值。
  >
  > 存在则返回，不存在抛异常  NoSuchElementException

```java
inal String s = optional.get();
```

- ifPres0ent

  > 如果存在则执行消费方法，如果不存在则不执行消费方法

```java
optional.ifPresent(System.out::println);
```

- orElse

  > Optional容器中颗值不为null则返回对应值，如果为null则返回默认值

```java
optionalOfNullable.orElse("other");
```

- orElseGet

  > 与orElse相似，不同的是可返回Optional

```
optionalOfNullable.orElseGet(String::new);
```

- orElseThrow

  > 存在返回，不存在则抛出异常

```java
optionalOfNullable.orElseThrow(NullPointerException::new);
```

- Map

  > Optional容器中颗值为null则返回空Optional，否则映射为自定义Optional。
  >
  > Function接口实现支持返回null。

```java
final Optional<String> optionalS = optional.map(ele -> "map");
```

- FlatMap

  > Optional容器中颗值为null则返回空Optional，否则映射为自定义Optional。
  >
  > Function接口实现不支持返回null。

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

