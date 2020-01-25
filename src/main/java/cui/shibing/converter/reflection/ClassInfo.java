package cui.shibing.converter.reflection;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import static cui.shibing.converter.reflection.ReflectionUtils.*;

public class ClassInfo {

    private Class<?> rawType;
    private Map<String, Object> fieldMap = new HashMap<>();
    private Map<String, Object> fieldMethodMap = new HashMap<>();

    public ClassInfo(Type type) {
        rawType = ReflectionUtils.getRawType(type);
    }

    public Type getFieldParameterizedType(Field field, Type currentType) {
        Class<?> declaringClass = field.getDeclaringClass();
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type actualTypeArgument = actualTypeArguments[i];
                if (actualTypeArgument instanceof TypeVariable) {
                    String name = actualTypeArgument.getTypeName();
                    Type t = getParameterTypeByName(name, currentType, declaringClass);
                    actualTypeArguments[i] = t;
                }
            }
            return ParameterizedTypeImpl.make(field.getType(), actualTypeArguments, parameterizedType.getOwnerType());
        } else {
            if (genericType instanceof TypeVariable) {
                return getParameterTypeByName(genericType.getTypeName(), currentType, declaringClass);
            } else {
                return genericType;
            }
        }
    }

    private Type getParameterTypeByName(String name, Type currentType, Class<?> declaringClass) {
        TypeVariable<? extends Class<?>>[] typeParameters = declaringClass.getTypeParameters();
        int index = -1;
        for (int i = 0; i < typeParameters.length; i++) {
            if (typeParameters[i].getName().equals(name)) {
                index = i;
                break;
            }
        }
        Type type = getClosetGenericSuperclass(currentType);
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments()[index];
        } else if (type instanceof TypeVariable){
            Type[] bounds = ((TypeVariable) type).getBounds();
            return bounds[0];
        } else return null;
    }

    private Type getClosetGenericSuperclass(Type type) {
        Type genericSuperClass = type;
        Class<?> rawType = getRawType(type);
        while (!(genericSuperClass instanceof ParameterizedType)) {
            genericSuperClass = rawType.getGenericSuperclass();
            if (rawType.getSuperclass() != Object.class) {
                rawType = rawType.getSuperclass();
            } else {
                break;
            }
        }
        return genericSuperClass;
    }

    public Object getField(String name) {
        return fieldMap.computeIfAbsent(name, k -> {
            Object field;
            try {
                field = rawType.getDeclaredField(k);
            } catch (NoSuchFieldException e) {
                // ignore
                field = null;
            }
            if (field == null) {
                Class<?> superClazz = rawType.getSuperclass();
                if (superClazz != null && superClazz != Object.class) {
                    field = ReflectionUtils.getClassInfo(superClazz).getField(name);
                }
            }
            if (field == null) {
                field = NON_FIELD;
            }
            return field;
        });
    }

    public Object getValue(Field field, Object o) {
        try {
            return field.get(o);
        } catch (IllegalAccessException e) {
            // try to get getter method
            Object getter = getMethod("get" + upperFirstChar(field.getName()));
            if (getter != NON_METHOD) {
                Method m = (Method) getter;
                try {
                    return m.invoke(o);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                return NON_ACCESS;
            }
        }
    }

    public void setValue(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            // try to get getter method
            Object getter = getMethod("set" + upperFirstChar(field.getName()), field.getType());
            if (getter != NON_METHOD) {
                Method m = (Method) getter;
                try {
                    m.invoke(target, value);
                    return;
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    private Object getMethod(String methodName, Class<?>... args) {
        return fieldMethodMap.computeIfAbsent(methodName, k -> {
            try {
                return rawType.getMethod(k, args);
            } catch (NoSuchMethodException e) {
                return NON_METHOD;
            }
        });
    }

    private static String upperFirstChar(String c) {
        char[] chars = c.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}
