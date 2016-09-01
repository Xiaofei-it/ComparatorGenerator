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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import xiaofei.library.comparatorgenerator.Criterion;
import xiaofei.library.comparatorgenerator.Order;

public class AnnotationUtils {

    private static final String TAG = "ComparatorGenerator: ";

    private static final boolean DEBUG = true;

    private static Method method = null;

    static {
        try {
            // The class can be found if ProGuard is used.
            Class<?> clazz = Class.forName("xiaofei.library.comparatorgenerator.CriterionManager");
            // The method cannot be found if ProGuard is used.
            method = clazz.getDeclaredMethod("getCriteria", Class.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private AnnotationUtils() {}

    public static Map<Integer, SortingCriterion> getCriteria(Class<?> clazz) {
        Map<Integer, SortingCriterion> result = new ConcurrentHashMap<Integer, SortingCriterion>();
        for (Class<?> tmp = clazz; tmp != null && tmp != Object.class; tmp = tmp.getSuperclass()) {
            Map<Integer, SortingCriterion> map = null;
            try {
                if (method != null) {
                    map = (Map<Integer, SortingCriterion>) method.invoke(null, tmp);
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (map == null) {
                if (DEBUG) {
                    System.out.println(TAG + "Getting criteria for Class " + tmp.getName() + " from annotations.");
                }
                Field[] fields = tmp.getDeclaredFields();
                for (Field field : fields) {
                    // The following statement may throw NoSuchFieldError if ProGuard is used.
                    Criterion criterion = field.getAnnotation(Criterion.class);
                    if (criterion != null) {
                        int priority = criterion.priority();
                        Order order = criterion.order();
                        SortingCriterion prev = result.put(priority, new SortingCriterion(new FieldMember(field), order));
                        if (prev != null) {
                            throw new RuntimeException(
                                    "The priority value " + priority + " has already been set to the member "
                                            + prev.getMember().getName() + ". Please specify another priority value.");
                        }
                    }
                }
                List<Method> methods = TypeUtils.getNoArgMethods(tmp);
                for (Method method : methods) {
                    Criterion criterion = method.getAnnotation(Criterion.class);
                    if (criterion != null) {
                        int priority = criterion.priority();
                        Order order = criterion.order();
                        SortingCriterion prev = result.put(priority, new SortingCriterion(new MethodMember(method), order));
                        if (prev != null) {
                            throw new RuntimeException(
                                    "The priority value " + priority + " has already been set to the member "
                                            + prev.getMember().getName() + ". Please specify another priority value.");
                        }
                    }
                }
            } else {
                if (DEBUG) {
                    System.out.println(TAG + "Getting criteria for Class " + tmp.getName() + " from CriterionManager.");
                }
                for (Map.Entry<Integer, SortingCriterion> entry : map.entrySet()) {
                    SortingCriterion prev = result.put(entry.getKey(), entry.getValue());
                    if (prev != null) {
                        throw new RuntimeException(
                                "The priority value " + entry.getKey() + " has already been set to the member "
                                        + prev.getMember().getName() + ". Please specify another priority value.");
                    }
                }
            }
        }
        return result;
    }
}