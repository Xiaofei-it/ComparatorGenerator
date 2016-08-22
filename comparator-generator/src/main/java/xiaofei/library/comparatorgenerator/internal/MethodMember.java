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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Xiaofei on 16/5/11.
 */
public class MethodMember implements Member {

    private Method method;

    public MethodMember(Method method) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        this.method = method;
    }

    public MethodMember(Class<?> clazz, String methodName) {
        this(TypeUtils.getMethod(clazz, methodName));
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Object getValue(Object object) {
        try {
            return method.invoke(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> getType() {
        return method.getReturnType();
    }
}
