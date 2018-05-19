package io.domino.lab.reflection;

import io.domino.lab.reflection.exception.ReflectionException;
import io.domino.lab.reflection.model.ComplexType;
import io.domino.lab.reflection.model.SimpleType;
import io.domino.lab.reflection.model.SpecialType;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.fail;

/**
 * Created by amazimpaka on 2018-05-03
 */
public class ClassReflectorTest {


    private final ClassLoader classLoader = getClass().getClassLoader();


    @Test
    public void testLoadClass() {
        Class<Object> loadedClass1 = ClassReflector.loadClass("java.lang.String");
        Assert.assertNotNull(loadedClass1);
        Assert.assertEquals(String.class, loadedClass1);

        Class<Object> loadedClass2 = ClassReflector.loadClass("io.domino.lab.reflection.model.SimpleType",
                classLoader);
        Assert.assertNotNull(loadedClass2);
        Assert.assertEquals(SimpleType.class, loadedClass2);
    }

    @Test
    public void testLoadClass_ClassNotFoundException() {

        try {
            ClassReflector.loadClass("InvalidClass");
            fail("Expected exception");
        } catch (ReflectionException e) {
            // expected exception
        }

        try {
            ClassReflector.loadClass("InvalidClass", classLoader);
            fail("Expected exception");
        } catch (ReflectionException e) {
            // expected exception
        }
    }

    @Test
    public void testNewInstance() throws Exception {
        Assert.assertNotNull(ClassReflector.newInstance(SimpleType.class));
        Assert.assertNotNull(ClassReflector.newInstance(ComplexType.class));

        changeAllowableMaxDepth(1);


        Assert.assertNotNull(ClassReflector.newInstance(ComplexType.class));

    }

    private void changeAllowableMaxDepth(int value) throws NoSuchFieldException, IllegalAccessException {
        Field allowableMaxDepth = ClassReflector.class.getDeclaredField("maxAllowableDepth");
        allowableMaxDepth.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");

        modifiersField.setAccessible(true);
        int modifiers = allowableMaxDepth.getModifiers();
        modifiersField.setInt(allowableMaxDepth, modifiers & ~Modifier.FINAL);

        allowableMaxDepth.set(null,value);

        modifiersField.setInt(allowableMaxDepth, modifiers);
        modifiersField.setAccessible(false);

        allowableMaxDepth.setAccessible(false);
    }


    @Test
    public void testNewInstance_InstantiationException() {

        try {
            Assert.assertNotNull(ClassReflector.newInstance(SpecialType.class));
            fail("Expected exception");
        } catch (ReflectionException e) {
            // expected exception
        }

    }


}
