package io.domino.lab.reflection;

import io.domino.lab.reflection.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * Created by amazimpaka on 2018-05-05
 */
public class ClasspathScannerTest {


    @Test
    public void scanByImplementedInterface() {
        Set<Class<? extends Type>> scannedClasses = ClasspathScanner.scanByImplementedInterface(Type.class,"io.domino.lab.reflection");

        Assert.assertNotNull(scannedClasses);
        Assert.assertFalse(scannedClasses.isEmpty());
        Assert.assertEquals(2,scannedClasses.size());

        Assert.assertTrue(scannedClasses.contains(SimpleType.class));
        Assert.assertTrue(scannedClasses.contains(ComplexType.class));
        Assert.assertFalse(scannedClasses.contains(SpecialType.class));
    }


    @Test
    public void scanByExtendedClass() {
        Set<Class<? extends AbstractType>> scannedClasses = ClasspathScanner.scanByExtendedClass(AbstractType.class,"io.domino.lab.reflection");

        Assert.assertNotNull(scannedClasses);
        Assert.assertFalse(scannedClasses.isEmpty());
        Assert.assertEquals(2,scannedClasses.size());

        Assert.assertTrue(scannedClasses.contains(SimpleType.class));
        Assert.assertTrue(scannedClasses.contains(ComplexType.class));
        Assert.assertFalse(scannedClasses.contains(SpecialType.class));
    }


    @Test
    public void scanByAnnotation() {
        Set<Class<?>> scannedClasses = ClasspathScanner.scanByAnnotation(Marker.class,"io.domino.lab.reflection");

        Assert.assertNotNull(scannedClasses);
        Assert.assertFalse(scannedClasses.isEmpty());
        Assert.assertEquals(1,scannedClasses.size());

        Assert.assertFalse(scannedClasses.contains(SimpleType.class));
        Assert.assertFalse(scannedClasses.contains(ComplexType.class));
        Assert.assertTrue(scannedClasses.contains(SpecialType.class));
    }

}