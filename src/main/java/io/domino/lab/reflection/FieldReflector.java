package io.domino.lab.reflection;

import io.domino.lab.reflection.exception.ReflectionException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by amazimpaka on 2017-12-16
 */
public final class FieldReflector {

    private static final Map<Class<?>, Map<String, Field>> FIELDS_CACHE = new ConcurrentHashMap<>();

    private static final List<String> EXCLUDED_FIELDS_NAMES = Arrays.asList("class","$jacocoData");

    private FieldReflector() {
    }

    public static Optional<Field> getField(Class<?> type, String name) {
        getAllFields(type);
        final Map<String, Field> cacheValues = FIELDS_CACHE.computeIfAbsent(type, key -> new ConcurrentHashMap<>());
        return Optional.ofNullable(cacheValues.get(name));
    }


    public static Object getValue(Object object, String fieldName) {
        Optional<Field> field = getField(object.getClass(), fieldName);
        if (field.isPresent()) {
            return getValue(object,field.get());
        }
        return null;
    }

    public static Object getValue(Object object,Field field) {
        final boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        } finally {
            field.setAccessible(accessible);
        }
    }

    public static void setValue(Object object, String fieldName, Object fieldValue) {
        Optional<Field> field = getField(object.getClass(), fieldName);
        if (field.isPresent()) {
            setValue(object, field.get(), fieldValue);
        }
    }

    public static void setValue(Object object,Field field, Object value) {
        final boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        } finally {
            field.setAccessible(accessible);
        }
    }


    public static List<Field> getAllFields(Class<?> type) {

        final Map<String, Field> cacheValues = FIELDS_CACHE.get(type);
        final Set<Field> fieldsSet = new HashSet<>();
        if(cacheValues != null){
            fieldsSet.addAll(cacheValues.values());
        }else{
            getAllFields(type, type, fieldsSet);
        }


        final List<Field> fields = new ArrayList<>(fieldsSet);
        Collections.sort(fields, Comparator.comparing(Field::getName));
        return fields;
    }



    private static void getAllFields(Class<?> type, Class<?> contextType, Set<Field> fields) {
        if (type != null && !type.equals(Object.class)) {

            final Map<String, Field> cacheValues = FIELDS_CACHE.computeIfAbsent(contextType, key-> new ConcurrentHashMap<>());

            for (final Field field : type.getDeclaredFields()) {
                if (!EXCLUDED_FIELDS_NAMES.contains(field.getName())) {
                    fields.add(field);
                    cacheValues.put(field.getName(), field);
                }

            }
            for (final Field field : type.getFields()) {
                if (!EXCLUDED_FIELDS_NAMES.contains(field.getName())) {
                    fields.add(field);
                    cacheValues.put(field.getName(), field);

                }
            }

            getAllFields(type.getSuperclass(), contextType, fields);
        }

    }

    public static boolean isFinal(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isFinal(modifiers);
    }

    public static boolean isStatic(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers);
    }

}
