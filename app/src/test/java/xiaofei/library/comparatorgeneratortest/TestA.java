package xiaofei.library.comparatorgeneratortest;

/**
 * Created by Eric on 16/8/22.
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
