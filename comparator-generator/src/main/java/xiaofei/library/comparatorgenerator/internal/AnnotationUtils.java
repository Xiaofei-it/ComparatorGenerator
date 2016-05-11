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

package xiaofei.library.comparatorgenerator.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.TreeMap;

import xiaofei.library.comparatorgenerator.Criterion;
import xiaofei.library.comparatorgenerator.Order;

public class AnnotationUtils {

    private AnnotationUtils() {}

    public static TreeMap<Integer, SortingCriterion> getCriteriaIn(Class<?> clazz) {
        TreeMap<Integer, SortingCriterion> map = new TreeMap<Integer, SortingCriterion>();
        List<Field> fields = TypeUtils.getFields(clazz);
        for (Field field : fields) {
            Criterion criterion = field.getAnnotation(Criterion.class);
            if (criterion != null) {
                TypeUtils.checkField(field);
                int priority = criterion.priority();
                Order order = criterion.order();
                SortingCriterion prev = map.put(priority, new SortingCriterion(new FieldMember(field), order));
                if (prev != null) {
                    throw new RuntimeException(
                            "The priority value " + priority + " has already been set to the field "
                                    + prev.getMember().getName() + ". Please specify another priority value.");
                }
            }
        }
        List<Method> methods = TypeUtils.getNoArgMethods(clazz);
        for (Method method : methods) {
            Criterion criterion = method.getAnnotation(Criterion.class);
            if (criterion != null) {
                TypeUtils.checkMethod(method);
                int priority = criterion.priority();
                Order order = criterion.order();
                SortingCriterion prev = map.put(priority, new SortingCriterion(new MethodMember(method), order));
                if (prev != null) {
                    throw new RuntimeException(
                            "The priority value " + priority + " has already been set to the method "
                                    + prev.getMember().getName() + ". Please specify another priority value.");
                }
            }
        }
        return map;
    }
}