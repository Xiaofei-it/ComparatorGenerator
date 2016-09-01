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

package xiaofei.library.comparatorgenerator.annotationprocessor;

import java.util.concurrent.ConcurrentHashMap;

import xiaofei.library.comparatorgenerator.Criterion;
import xiaofei.library.comparatorgenerator.Order;
import xiaofei.library.comparatorgenerator.internal.FieldMember;
import xiaofei.library.comparatorgenerator.internal.MethodMember;
import xiaofei.library.comparatorgenerator.internal.SortingCriterion;

/**
 * Created by Xiaofei on 16/8/22.
 */
public class Example {

    static class A1 {
        @Criterion
        int j;
        @Criterion
        public int g() {
            return 0;
        }
    }

    static class A2 {
        @Criterion
        int j;
        @Criterion
        public int g() {
            return 0;
        }
    }

    private static ConcurrentHashMap<Class<?>, ConcurrentHashMap<Integer, SortingCriterion>> maps = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<Integer, SortingCriterion>>();

    static {
        putCriterionForField(A1.class, "", 1, Order.ASCENDING);
        putCriterionForField(A1.class, "", 1, Order.ASCENDING);
    }

    private static void putCriterionForMethod(Class<?> clazz, String methodName, int priority, Order order) {
        ConcurrentHashMap<Integer, SortingCriterion> map = maps.putIfAbsent(clazz, new ConcurrentHashMap<Integer, SortingCriterion>());
        map.put(priority, new SortingCriterion(new MethodMember(clazz, methodName), order));
    }

    private static void putCriterionForField(Class<?> clazz, String fieldName, int priority, Order order) {
        ConcurrentHashMap<Integer, SortingCriterion> map = maps.putIfAbsent(clazz, new ConcurrentHashMap<Integer, SortingCriterion>());
        map.put(priority, new SortingCriterion(new FieldMember(clazz, fieldName), order));
    }

    public static ConcurrentHashMap<Integer, SortingCriterion> getCriteriaIn(Class<?> clazz) {
        return maps.get(clazz);
    }
}
