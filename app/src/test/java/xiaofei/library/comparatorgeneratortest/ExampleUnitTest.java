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

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by Xiaofei on 16/8/22.
 */
public class ExampleUnitTest {
    @Test
    public void f() throws Exception {
        Class<?> clazz = Class.forName("xiaofei.library.comparatorgeneratortest.TestA$A");
        Field field = clazz.getDeclaredField("i");
        TestA testA = new TestA();
        System.out.println(field.get(testA.a));
        field.set(testA.a, 34);
        System.out.println(field.get(testA.a));
        testA.f();
        testA.f2();
    }
}