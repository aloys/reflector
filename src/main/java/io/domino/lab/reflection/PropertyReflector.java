package io.domino.lab.reflection;

import io.domino.lab.reflection.exception.ReflectionException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by amazimpaka on 2018-05-06
 */
public final class PropertyReflector {

    private static final Map<Class<?>, Map<String, PropertyDescriptor>> PROPERTIES_CACHE = new ConcurrentHashMap<>();

    private static final List<String> EXCLUDED_FIELDS_NAMES = Arrays.asList("class","$jacocoData");

    private PropertyReflector() {
    }

    public static Object getValue(Object object,String propertyName) {
        try {

            final PropertyDescriptor pd = getPropertyDescriptor(object.getClass(), propertyName);

            final Method readMethod = pd.getReadMethod();
            if(readMethod == null){
                throw new ReflectionException(String.format("No read method for property: %s  of: %s",propertyName,object.getClass()));
            }

            return readMethod.invoke(object);

        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }



    public static void setValue(Object object,String propertyName,Object value) {
        try {

            final PropertyDescriptor pd = getPropertyDescriptor(object.getClass(), propertyName);
            final Method writeMethod = pd.getWriteMethod();
            if(writeMethod == null){
                throw new ReflectionException(String.format("No write method for property: %s  of: %s",propertyName,object.getClass()));
            }
            writeMethod.invoke(object, value);

        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ReflectionException(e);
        }

    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> type, String propertyName) {
        getPropertyDescriptors(type);

        final Map<String, PropertyDescriptor> cachedProperties = PROPERTIES_CACHE.get(type);
        final PropertyDescriptor pd = cachedProperties.get(propertyName);
        if(pd == null){
            throw new ReflectionException(String.format("No property: %s  of: %s",propertyName,type));
        }
        return pd;
    }

    public static List<PropertyDescriptor> getPropertyDescriptors(Class<?> type) {
        final Map<String, PropertyDescriptor> cachedProperties = PROPERTIES_CACHE.get(type);

        final Set<PropertyDescriptor> propertiesSet = new HashSet<>();
        if(cachedProperties != null){
            propertiesSet.addAll(cachedProperties.values());
        }else{
            getAllProperties(type, type, propertiesSet);
        }

        final List<PropertyDescriptor> properties = new ArrayList<>(propertiesSet);
        Collections.sort(properties, Comparator.comparing(PropertyDescriptor::getName));
        return properties;

    }

    private static void getAllProperties(Class<?> type, Class<?> contextType, Set<PropertyDescriptor> properties) {

        if (type != null && !type.equals(Object.class)) {

            try {

                final Map<String, PropertyDescriptor> cacheValues = PROPERTIES_CACHE.computeIfAbsent(contextType, key -> new ConcurrentHashMap<>());

                final BeanInfo beanInfo = Introspector.getBeanInfo(type);
                for (final PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {

                    if (!EXCLUDED_FIELDS_NAMES.contains(pd.getName())) {
                        properties.add(pd);
                        cacheValues.put(pd.getName(), pd);
                    }

                }

            } catch (IntrospectionException e) {
                throw new ReflectionException(String.format("[%s] -> %s", type, e.getMessage()), e);
            }

            getAllProperties(type.getSuperclass(), contextType, properties);
        }
    }


}
