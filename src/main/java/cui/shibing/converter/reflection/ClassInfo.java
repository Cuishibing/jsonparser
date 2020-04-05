package cui.shibing.converter.reflection;

import static cui.shibing.converter.reflection.ReflectionUtils.NON_ACCESS;
import static cui.shibing.converter.reflection.ReflectionUtils.NON_FIELD;
import static cui.shibing.converter.reflection.ReflectionUtils.NON_METHOD;
import static cui.shibing.converter.reflection.ReflectionUtils.getRawType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class ClassInfo {

    private Class<?> rawType;
    private Map<String, Object> fieldMap = new ConcurrentHashMap<>();
    private Map<String, Object> fieldMethodMap = new ConcurrentHashMap<>();

    public ClassInfo(Type type) {
        rawType = ReflectionUtils.getRawType(type);
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
