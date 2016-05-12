# ComparatorGenerator

一个易用的生成Comparator的工具类。在排序时特别有用。

##特色

Java编程经常要对数据进行排序，此时需要写一个Comparator。但Comparator比较难写，尤其是在多字段排序的时候更加复杂。

你可以方便地使用此工具类生成Comparator。你只需要指定在排序的时候要依据哪些字段以及升序还是降序，这个工具类会为你生成相应的Comparator。

##Gradle


```
dependencies {
    compile 'xiaofei.library:comparator-generator:1.1'
}
```

##Maven

```
<dependency>
  <groupId>xiaofei.library</groupId>
  <artifactId>comparator-generator</artifactId>
  <version>1.1</version>
  <type>pom</type>
</dependency>
```


##用法

现在假设要对一个类的许多实例进行排序。排序的时候依据某些字段或者函数的返回值。

那么你要做的就是告诉工具类这些字段和函数，并且指定权重以及升降序。

有两种方法指指定排序策略：使用注解或者使用API。

###使用注解

现在要对Item1生成Comparator。排序的时候首先对price进行升序，如果price相同则对distance进行降序，如果distance也相同则对itemName进行升序。

那么对Item1的相应字段加上注解：

```
class Item1 {
    @Criterion(priority = 3, order = Order.ASCENDING)
    private int price;
    @Criterion(priority = 2, order = Order.DESCENDING)
    private int distance;
    @Criterion(priority = 1) // order默认是升序
    private String itemName;
}
```

使用的时候只需要这样写：

```
Comparator<Item1> comparator = new ComparatorGenerator<Item1>(Item1.class).generate();
Arrays.sort(items1, comparator);
```

再看一个例子。对Item3排序，先按总价升序，再按itemName升序。总价是由函数给出的，我们依然可以对函数加上注解。

注意：函数必须是无参成员函数！

```
class Item3 {
	
    private int price;
    private int number;

    @Criterion(priority = 1) // 默认升序
    private String itemName;
    
    @Criterion(priority = 2, order = Order.ASCENDING)
    int getTotalPrice() {
        return price * number;
    }

}
```

使用的时候写如下代码：

```
Comparator<Item3> comparator = new ComparatorGenerator<Item3>(Item3.class).generate();
Arrays.sort(items3, comparator);
```


###使用API

某些时候要对一个类写出不同Comparator，比如对订单排序的时候会有很多种：距离优先、评价优先、价格优先。

那么这时候就不能使用注解，因为通过注解只能生成唯一的Comparator。

这时候使用api直接指定。

以下有一个类Item2。

```
class Item2 {
	
    private int price;
	
    private int distance;
	
    private String itemName;
}
```

首先对price升序，然后对distance降序，然后对itemName升序。

```
Comparator<Item2> comparator2 =
    new ComparatorGenerator<Item2>(Item2.class)
        .addCriterion(3, "price", Order.ASCENDING)
        .addCriterion(2, "distance", Order.DESCENDING)
        .addCriterion(1, "itemName") // 默认升序
        .generate();
Arrays.sort(items2, comparator2);
```

再看一个例子。


```
class Item4 {
	
    private int price;
    private int number;
    private int distance;
    
    int getTotalPrice() {
        return price * number;
    }
}
```

对Item4排序，首先按总价升序，然后按距离降序。

```
Comparator<Item4> comparator4 =
    new ComparatorGenerator<Item4>(Item4.class)
        .addCriterion(3, "getTotalPrice", Order.ASCENDING)
        .addCriterion(2, "distance", Order.DESCENDING)
        .generate();
Arrays.sort(items4, comparator4);
```
