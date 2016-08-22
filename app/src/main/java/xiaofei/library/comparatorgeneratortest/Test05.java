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

/**
 * Created by Xiaofei on 16/8/22.
 */
public class Test05 {
    @Criterion(order = Order.ASCENDING, priority = 4)
    int j;
    String s;
    @Criterion(order = Order.DESCENDING)
    String get() {
        return s;
    }
    public static void main(String[] args) {
        System.out.println("Test05");
        Test05 a = new Test05();
        a.j = 3; a.s = "A";
        a.j = 2; a.s = "B";
        a.j = 2; a.s = "F";
        Test05[] test05s = new Test05[3];
        for (int i = 0; i < 3; i++) {
            test05s[i] = new Test05();
        }
        test05s[0].j = 3; test05s[0].s = "A";
        test05s[1].j = 2; test05s[1].s = "B";
        test05s[2].j = 2; test05s[2].s = "F";
        Comparator<Test05> comparator = new ComparatorGenerator<Test05>(Test05.class).generate();
        Arrays.sort(test05s, comparator);
        for (int i = 0 ; i < 3; ++i) {
            System.out.println("" + test05s[i].j + " " + test05s[i].s);
        }

    }
}
