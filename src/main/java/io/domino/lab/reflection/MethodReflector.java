package io.domino.lab.reflection;


import io.domino.lab.reflection.exception.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by amazimpaka on 2017-12-16
 */
public final class MethodReflector {

    private static final Map<Class<?>, Map<String, Method>> METHODS_CACHE = new ConcurrentHashMap<>();

    private static final Set<String> EXCLUDED_METHODS = new HashSet<>();
    static {

        for (final Method method : Object.class.getDeclaredMethods()) {
            EXCLUDED_METHODS.add(method.getName());
        }
        for (final Method method : Object.class.getMethods()) {
            EXCLUDED_METHODS.add(method.getName());
        }

        EXCLUDED_METHODS.add("$jacocoInit");
    }


    private MethodReflector() {
    }

    public static Object invokeMethod(Object object,String methodName,Object... parameters) {
        final Method method = getMethod(object.getClass(), methodName);
        return invokeMethod(object, method,parameters);
    }

    public static Object invokeMethod(Object object,Method method,Object... parameters) {
        try {
            if(method.getReturnType() == Void.TYPE || method.getReturnType() == Void.class){
                method.invoke(object,parameters);
                return Void.TYPE;
            }

            return method.invoke(object,parameters);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static Method getMethod(Class<?> type,String methodName) {
        getAllMethods(type);

        final Map<String, Method> cacheValues = METHODS_CACHE.get(type);
        final Method method = cacheValues.get(methodName);
        if(method == null){
            throw new ReflectionException(String.format("No method: %s  of: %s",methodName,type));
        }
        return method;
    }

    public static List<Method> getAllMethods(Class<?> type) {

        final Map<String, Method> cacheValues = METHODS_CACHE.get(type);
        final Set<Method> methodsSet = new HashSet<>();
        if(cacheValues != null){
            methodsSet.addAll(cacheValues.values());
        }else{
            getAllMethods(type,type, methodsSet);
        }


        final List<Method> methods = new ArrayList<>();
        methods.addAll(methodsSet
                .stream()
                .filter(method -> !EXCLUDED_METHODS.contains(method.getName()))
                .collect(Collectors.toList()));

        Collections.sort(methods,Comparator.comparing(Method::getName));
        return methods;
    }


    private static void getAllMethods(Class<?> type, Class<?> contextType, Set<Method> methods) {
        if (type != null && !type.equals(Object.class)) {

            final Map<String, Method> cacheValues = METHODS_CACHE.computeIfAbsent(contextType, key -> new ConcurrentHashMap<>());


            for (final Method method : type.getDeclaredMethods()) {
                methods.add(method);
                cacheValues.put(method.getName(), method);
            }
            for (final Method method : type.getMethods()) {
                methods.add(method);
                cacheValues.put(method.getName(), method);
            }

            getAllMethods(type.getSuperclass(), contextType, methods);

        }

    }

}
