/**
 *
 * Copyright 2016 Xiaofei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package xiaofei.library.comparatorgeneratortest;

import java.util.Arrays;
import java.util.Comparator;

import xiaofei.library.comparatorgenerator.ComparatorGenerator;
import xiaofei.library.comparatorgenerator.Criterion;
import xiaofei.library.comparatorgenerator.Order;

class Item1 {
    @Criterion(priority = 3, order = Order.ASCENDING)
    int price;
    @Criterion(priority = 2, order = Order.DESCENDING)
    int distance;
    @Criterion(priority = 1)
    String itemName;
}

class Item2 {

    int price;

    int distance;

    String itemName;
}
public class Test02 {


    public static void main(String[] args) {
        System.out.println("Test02");
        Item1[] items1 = new Item1[5];
        for (int i = 0; i < 5; i++) {
            items1[i] = new Item1();
        }
        items1[0].price = 3;
        items1[1].price = 2; items1[1].distance = 3;
        items1[2].price = 2; items1[2].distance = 4;
        items1[3].price = 2; items1[3].distance = 1;items1[3].itemName = "6";
        items1[4].price = 2; items1[4].distance = 1;items1[4].itemName = "5";
        Comparator<Item1> comparator = new ComparatorGenerator<Item1>(Item1.class).generate();
        Arrays.sort(items1, comparator);
        for (int i = 0 ; i < 5; ++i) {
            System.out.println("" + items1[i].price + " " + items1[i].distance + " " + items1[i].itemName);
        }

        Item2[] items2 = new Item2[5];
        for (int i = 0; i < 5; i++) {
            items2[i] = new Item2();
        }
        items2[0].price = 3;
        items2[1].price = 2; items2[1].distance = 3;
        items2[2].price = 2; items2[2].distance = 4;
        items2[3].price = 2; items2[3].distance = 1;items2[3].itemName = "6";
        items2[4].price = 2; items2[4].distance = 1;items2[4].itemName = "5";
        Comparator<Item2> comparator2 = new ComparatorGenerator<Item2>(Item2.class)
                .addCriterion(3, "price", Order.ASCENDING)
                .addCriterion(2, "distance", Order.DESCENDING)
                .addCriterion(1, "itemName").generate();
        Arrays.sort(items2, comparator2);
        for (int i = 0 ; i < 5; ++i) {
            System.out.println("" + items2[i].price + " " + items2[i].distance + " " + items2[i].itemName);
        }
    }

}
