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
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import xiaofei.library.comparatorgenerator.Criterion;
import xiaofei.library.comparatorgenerator.Order;

public class AnnotationUtils {

    private static Method method;

    static {
        try {
            Class<?> clazz = Class.forName("xiaofei.library.comparatorgenerator.CriterionManager");
            method = clazz.getDeclaredMethod("getCriteriaIn", Class.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private AnnotationUtils() {}

    public static ConcurrentHashMap<Integer, SortingCriterion> getCriteriaIn(Class<?> clazz) {
        try {
            return (ConcurrentHashMap<Integer, SortingCriterion>) method.invoke(null, clazz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}