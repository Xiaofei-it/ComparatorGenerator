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

import xiaofei.library.comparatorgenerator.ComparatorGenerator;
import xiaofei.library.comparatorgenerator.Criterion;
import xiaofei.library.comparatorgenerator.Order;

/**
 * Created by Xiaofei on 16/5/11.
 */
public class Test03 {
    static class A {
        int i;
        int j;

        String b;

        @Criterion(priority = 1, order = Order.DESCENDING)
        int getI() {
            return i;
        }

        @Criterion(priority = 0, order = Order.ASCENDING)
        int getJ() {
            return j;
        }
    }
    public static void main(String[] args) {
        System.out.println("Test03");
        A[] a = new A[5];
        for (int i = 0; i < a.length; ++i) {
            a[i] = new A();
        }
        a[0].i = 4; a[0].j = 2; a[0].b = "j";
        a[1].i = 5; a[1].j = -8;
        a[2].i = 4; a[2].j = 2; a[2].b = "i";
        a[3].i = 6; a[3].j = 2;
        a[4].i = 4; a[4].j = -4;
        Arrays.sort(a, new ComparatorGenerator<A>(A.class)
                .addCriterion(-1, "b", Order.ASCENDING)
                .generate());
        for (int i = 0; i < a.length; ++i) {
            System.out.println(a[i].i + " " + a[i].j + " " + a[i].b);
        }
    }
}
