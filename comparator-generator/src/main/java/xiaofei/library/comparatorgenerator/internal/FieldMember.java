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

import com.sun.deploy.security.ValidationState;

import java.lang.reflect.Field;

/**
 * Created by Xiaofei on 16/5/11.
 */
public class FieldMember implements Member {

    private Field field;

    public FieldMember(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        this.field = field;
    }

    public FieldMember(Class<?> clazz, String fieldName) {
        this(TypeUtils.getField(clazz, fieldName));
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public Object getValue(Object object) {
        try {
            return field.get(object);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
