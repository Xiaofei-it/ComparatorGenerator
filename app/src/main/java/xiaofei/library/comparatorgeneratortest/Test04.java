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

import android.support.annotation.Keep;

import java.util.Arrays;
import java.util.Comparator;

import xiaofei.library.comparatorgenerator.ComparatorGenerator;
import xiaofei.library.comparatorgenerator.Criterion;
import xiaofei.library.comparatorgenerator.Order;

/**
 * Created by Xiaofei on 16/5/11.
 */

class Item3 {

    int price;

    int distance;

    String itemName;

    @Criterion(priority = 3, order = Order.ASCENDING)
    int getPrice() {
        return price;
    }

    @Criterion(priority = 2, order = Order.DESCENDING)
    int getDistance() {
        return distance;
    }

    @Criterion(priority = 1)
    String getItemName() {
        return itemName;
    }
}

@Keep
class Item4 {

    int price;

    int distance;

    String itemName;

    int getPrice() {
        return price;
    }

    int getDistance() {
        return distance;
    }

    String getItemName() {
        return itemName;
    }
}
public class Test04 {

    public static void main(String[] args) {
        System.out.println("Test04");
        Item3[] items3 = new Item3[5];
        for (int i = 0; i < 5; i++) {
            items3[i] = new Item3();
        }
        items3[0].price = 3;
        items3[1].price = 2; items3[1].distance = 3;
        items3[2].price = 2; items3[2].distance = 4;
        items3[3].price = 2; items3[3].distance = 1;items3[3].itemName = "6";
        items3[4].price = 2; items3[4].distance = 1;items3[4].itemName = "5";
        Comparator<Item3> comparator = new ComparatorGenerator<Item3>(Item3.class).generate();
        Arrays.sort(items3, comparator);
        for (int i = 0 ; i < 5; ++i) {
            System.out.println("" + items3[i].price + " " + items3[i].distance + " " + items3[i].itemName);
        }

        Item4[] items4 = new Item4[5];
        for (int i = 0; i < 5; i++) {
            items4[i] = new Item4();
        }
        items4[0].price = 3;
        items4[1].price = 2; items4[1].distance = 3;
        items4[2].price = 2; items4[2].distance = 4;
        items4[3].price = 2; items4[3].distance = 1;items4[3].itemName = "6";
        items4[4].price = 2; items4[4].distance = 1;items4[4].itemName = "5";
        Comparator<Item4> comparator2 = new ComparatorGenerator<Item4>(Item4.class)
                .addCriterion(3, "getPrice", Order.ASCENDING)
                .addCriterion(2, "getDistance", Order.DESCENDING)
                .addCriterion(1, "getItemName").generate();
        Arrays.sort(items4, comparator2);
        for (int i = 0 ; i < 5; ++i) {
            System.out.println("" + items4[i].price + " " + items4[i].distance + " " + items4[i].itemName);
        }
    }
}
