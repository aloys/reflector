package io.domino.lab.reflection;

import io.domino.lab.reflection.exception.ReflectionException;
import io.domino.lab.reflection.model.ComplexType;
import io.domino.lab.reflection.model.SimpleType;
import io.domino.lab.reflection.utils.Calculator;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by amazimpaka on 2018-05-05
 */
public class MethodReflectorTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getAllMethods() {
        List<Method> methods = MethodReflector.getAllMethods(ComplexType.class);

        Assert.assertNotNull(methods);
        Assert.assertFalse(methods.isEmpty());
        Assert.assertEquals(methods.toString(),9,methods.size());

        Map<String,Class<?>> methodsTypes = methods
                .stream()
                .collect(Collectors.toMap(Method::getName, Method::getReturnType));

        Assert.assertTrue(methodsTypes.keySet().contains("getSimpleType"));
        Assert.assertTrue(methodsTypes.get("getSimpleType") == SimpleType.class);

        Assert.assertTrue(methodsTypes.keySet().contains("isEnabled"));
        Assert.assertTrue(methodsTypes.get("isEnabled") == Boolean.TYPE);

        Assert.assertTrue(methodsTypes.keySet().contains("getId"));
        Assert.assertTrue(methodsTypes.get("getId") == Long.TYPE);

        Assert.assertTrue(methodsTypes.keySet().contains("getName"));
        Assert.assertTrue(methodsTypes.get("getName") == String.class);
    }


    @Test
    public void invokeMethod() {
        final Calculator calculator = new Calculator();

        Object sum = MethodReflector.invokeMethod(calculator, "add", 10, 20);
        Assert.assertEquals(30.0d,Double.valueOf(sum.toString()),0.0d);

        Assert.assertEquals(0.0d, calculator.getPi(),0.0d);
        MethodReflector.invokeMethod(calculator, "setPi", Math.PI);
        Assert.assertEquals(Math.PI, MethodReflector.invokeMethod(calculator, "getPi"));
        Assert.assertEquals(Math.PI, calculator.getPi(),0.0d);
    }

    @Test
    public void getMethod() {

        Assert.assertEquals("add",MethodReflector.getMethod(Calculator.class, "add").getName());
        Assert.assertEquals("setPi",MethodReflector.getMethod(Calculator.class, "setPi").getName());
        Assert.assertEquals("getPi",MethodReflector.getMethod(Calculator.class, "getPi").getName());

        expectedException.expect(ReflectionException.class);
        MethodReflector.getMethod(Calculator.class, "invalidName");

    }


}
