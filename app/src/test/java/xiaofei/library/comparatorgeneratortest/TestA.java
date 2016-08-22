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

/**
 * Created by Xiaofei on 16/8/22.
 */
public class TestA {
    private class A {
        int i = 5;
    }
    A a = new A();

    void f() {
        System.out.println(A.class.getName());
        class B {

        }
        System.out.println(B.class.getName());
        C c = new C() {

        };
        System.out.println(c.getClass().getName());
        class D {

        }
        System.out.println(D.class.getName());
    }
    void f3() {
        System.out.println(A.class.getName());
        class B {

        }
        System.out.println(B.class.getName());
        C c = new C() {

        };
        System.out.println(c.getClass().getName());
        class D {

        }
        System.out.println(D.class.getName());
    }
    void f2() {
        System.out.println(A.class.getName());
        class B {

        }
        System.out.println(B.class.getName());
        C c = new C() {

        };
        System.out.println(c.getClass().getName());
        class D {

        }
        System.out.println(D.class.getName());
    }
    interface C {}
}
