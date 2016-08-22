package xiaofei.library.comparatorgeneratortest;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
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