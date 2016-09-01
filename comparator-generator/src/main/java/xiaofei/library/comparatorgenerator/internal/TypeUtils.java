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
import java.util.ArrayList;
import java.util.List;

public class TypeUtils {

    private static final String TAG = "ComparatorGenerator: ";

    private static final boolean DEBUG = true;

    private TypeUtils() {}

    public static Field getFieldIncludingSuperClass(Class<?> clazz, String fieldName) {
        if (DEBUG) {
            System.out.println(TAG + "Finding field " + fieldName + " in " + clazz.getName());
        }
        for (Class<?> tmp = clazz; tmp != null && tmp != Object.class; tmp = tmp.getSuperclass()) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {

            }
        }
        return null;
    }

    private static boolean isNoArgMethod(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes == null || parameterTypes.length == 0;
    }

    public static List<Method> getNoArgMethods(Class<?> clazz) {
        List<Method> result = new ArrayList<Method>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isSynthetic() && isNoArgMethod(method)) {
                result.add(method);
            }
        }
        return result;
    }

    public static Method getMethodIncludingSuperClass(Class<?> clazz, String methodName) {
        if (DEBUG) {
            System.out.println(TAG + "Finding method " + methodName + " in " + clazz.getName());
        }
        for (Class<?> tmp = clazz; tmp != null && tmp != Object.class; tmp = tmp.getSuperclass()) {
            try {
                return clazz.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {

            }
        }
        return null;
    }

    public static void checkField(Field field) {
        Class<?> type = field.getType();
        if (!(Comparable.class.isAssignableFrom(type) || type.isPrimitive())) {
            throw new RuntimeException(
                    "The field " + field.getName() + " is not a primitive type or does not implement the Comparable interface. "
                            + "It must be a primitive type or implement the Comparable interface.");
        }
    }

    public static void checkMethod(Method method) {
        Class<?> type = method.getReturnType();
        if (!(Comparable.class.isAssignableFrom(type) || type.isPrimitive())) {
            throw new RuntimeException(
                    "The return type of the method " + method.getName() + " is not a primitive type or does not implement the Comparable interface. "
                            + "It must be a primitive type or implement the Comparable interface.");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            throw new RuntimeException(
                    "The method " + method.getName() + " has parameters. It must have no parameters.");
        }
    }

}