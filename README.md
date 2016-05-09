# ComparatorGenerator
An easy-to-use helper class for generating a comparator for a specified class. Useful when sorting the instances of the specified class.

##Usage

You can specify multiple criteria. A criterion has a priority. The field with a higher priority will be considered first when sorting. A criterion has also a sorting order, which, by default, is ascending.

There are two ways to specify criteria: Using annotations and using APIs.

### Using Annotations

Take the following class as an example:

```
class Item1 {
	@Criterion(priority = 3, order = Order.ASCENDING)
	int price;
	@Criterion(priority = 2, order = Order.DESCENDING)
	int distance;
	@Criterion(priority = 1) // The default sorting order is Order.ASCENDING.
	String itemName;
}
```

Suppose there is an array named items1. Then we can sort the array as the following shows:

```
Comparator<Item1> comparator = new ComparatorGenerator<Item1>(Item1.class).generate();
Arrays.sort(items1, comparator);
```

### Using APIs

Take the following class as an example:

```
class Item2 {
	
	int price;
	
	int distance;
	
	String itemName;
}
```

Suppose there is an array named items2. Then we can sort the array as the following shows:

```
Comparator<Item2> comparator2 =
        new ComparatorGenerator<Item2>(Item2.class)
				   .addCriterion(3, "price", Order.ASCENDING)
				   .addCriterion(2, "distance", Order.DESCENDING)
				   .addCriterion(1, "itemName") // The default sorting order is Order.ASCENDING.
				   .generate();
Arrays.sort(items2, comparator2);
```
