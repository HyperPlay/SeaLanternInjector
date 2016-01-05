package net.hyperplay.sealanterninjector.util;

import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;

public abstract class ReflectionUtil {

    public static Field getField(final Class beanClass, final String fieldName) throws NoSuchFieldException {
        Field field = null;

        Class tempClass = beanClass;
        while (field == null && tempClass != null) {
            try {
                field = tempClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                tempClass = tempClass.getSuperclass();
            }
        }

        if (field == null) {
            throw new NoSuchFieldException(fieldName + " in class " + beanClass);
        }

        return field;
    }


    public static Object getFieldValue(Class clazz, Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(clazz, fieldName);
        boolean accessible = field.isAccessible();
        Object value = null;

        field.setAccessible(true);
        try {
            value = field.get(object);
        } finally {
            field.setAccessible(accessible);
        }

        return value;
    }


    public static void setFieldValue(Class clazz, Object object, String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(clazz, fieldName);
        boolean accessible = field.isAccessible();

        field.setAccessible(true);
        try {
            field.set(object, newValue);
        } finally {
            field.setAccessible(accessible);
        }
    }


    public static Object getStaticFieldValue(Class beanClass, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(beanClass, fieldName);
        boolean accessible = field.isAccessible();
        Object value = null;

        field.setAccessible(true);
        try {
            value = field.get(beanClass);
        } finally {
            field.setAccessible(accessible);
        }

        return value;
    }

    public static void setStaticFieldValue(Class clazz, String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(clazz, fieldName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            int modifiers = field.getModifiers();
            setFieldValue(Field.class, field, "modifiers", modifiers & ~Modifier.FINAL);
            try {
                FieldAccessor fieldAccessor = ReflectionFactory.getReflectionFactory().newFieldAccessor(field, false);
                fieldAccessor.set(null, newValue);
            } finally {
                setFieldValue(Field.class, field, "modifiers", modifiers);
            }
        } finally {
            field.setAccessible(accessible);
        }
    }

    public static Method getMethod(Class beanClass, String methodName, Class... parameterTypes) throws NoSuchMethodException {

        Method method = null;

        Class tempClass = beanClass;
        while (method == null && tempClass != null) {
            try {
                method = tempClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                tempClass = tempClass.getSuperclass();
                e.printStackTrace();
            }
        }

        if (method == null) {
            throw new NoSuchMethodException();
        }

        return method;
    }

    public static Object invokeMethod(Class clazz, Object object, String methodName, Class[] parameterTypes, Object[] values) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = getMethod(clazz, methodName, parameterTypes);
        boolean accessible = method.isAccessible();
        Object value = null;

        method.setAccessible(true);
        try {
            value = method.invoke(object, values);
        } finally {
            method.setAccessible(accessible);
        }

        return value;
    }

    public static Constructor getConstructor(Class beanClass, Class... parameterTypes) throws NoSuchMethodException {
        return beanClass.getDeclaredConstructor(parameterTypes);
    }


    public static Object invokeConstructor(Class beanClass, Class[] parameterTypes, Object[] values) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Constructor constructor = getConstructor(beanClass, parameterTypes);
        boolean accessible = constructor.isAccessible();
        Object value = null;

        constructor.setAccessible(true);
        try {
            value = constructor.newInstance(values);
        } finally {
            constructor.setAccessible(accessible);
        }

        return value;
    }

    public static Object invokeEnumConstructor(Class beanClass, Class[] parameterTypes, Object[] values) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Constructor constructor = getConstructor(beanClass, parameterTypes);
        boolean accessible = constructor.isAccessible();
        Object value = null;

        constructor.setAccessible(true);
        try {
            ConstructorAccessor constructorAccessor = (ConstructorAccessor) getFieldValue(Constructor.class, constructor, "constructorAccessor");
            if (constructorAccessor == null) {
                invokeMethod(Constructor.class, constructor, "acquireConstructorAccessor", new Class[0], new Object[0]);
                constructorAccessor = (ConstructorAccessor) getFieldValue(Constructor.class, constructor, "constructorAccessor");
            }
            value = constructorAccessor.newInstance(values);
        } finally {
            constructor.setAccessible(accessible);
        }

        return value;
    }

}
