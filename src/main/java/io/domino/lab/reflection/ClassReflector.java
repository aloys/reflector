package io.domino.lab.reflection;

import io.domino.lab.reflection.exception.ReflectionException;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by amazimpaka on 2018-05-03
 */
public final class ClassReflector {

    private static int maxAllowableDepth = 10;

    private ClassReflector() {
    }

    public static <K> Class<K> loadClass(String className, ClassLoader loader) {
        try {
            return (Class<K>) Class.forName(className, true, loader);
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }


    public static <K> Class<K> loadClass(String className) {
        try {
            return (Class<K>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }

    public static <K> K newInstance(Class<K> instanceClass) {
        return newInstance(instanceClass, 1);
    }


    private static <K> K newInstance(Class<K> instanceClass, int currentDepth) {
        try {

            final K instance = instanceClass.newInstance();

            if (currentDepth <= maxAllowableDepth) {
                for (Field field : instanceClass.getDeclaredFields()) {

                    if(!FieldReflector.isFinal(field)){
                        long nonArguCounstructorCount = Arrays.stream(field.getType().getConstructors())
                                .filter(constructor -> constructor.getParameterCount() == 0)
                                .count();

                        if (nonArguCounstructorCount != 0) {

                            final boolean accessible = field.isAccessible();
                            try {
                                field.setAccessible(true);
                                field.set(instance, newInstance(field.getType(),currentDepth+1));
                            } finally {
                                field.setAccessible(accessible);
                            }
                        }
                    }

                }
            }

            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            String message = String.format("Message: %s / Type: %s / Current Depth: %s",e.getMessage(),instanceClass,currentDepth);
            throw new ReflectionException(message,e);
        }
    }
}
