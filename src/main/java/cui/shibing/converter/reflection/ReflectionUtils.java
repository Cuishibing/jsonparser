package cui.shibing.converter.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {

    public static final Object NON_FIELD = new Object();

    public static final Object NON_METHOD = new Object();

    public static final Object NON_ACCESS = new Object();

    private static Map<Class<?>, ClassInfo> classInfoMap = new HashMap<>();

    public static class ClassInfo {
        private Class<?> clazz;
        private Map<String, Object> fieldMap = new HashMap<>();
        private Map<String, Object> fieldMethodMap = new HashMap<>();

        public ClassInfo(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Object getField(String name) {
            return fieldMap.computeIfAbsent(name, k -> {
                Object field;
                try {
                    field = clazz.getDeclaredField(k);
                } catch (NoSuchFieldException e) {
                    // ignore
                    field = null;
                }
                if (field == null) {
                    Class<?> superClazz = clazz.getSuperclass();
                    if (superClazz != null) {
                        field = ReflectionUtils.getClassInfo(clazz.getSuperclass()).getField(name);
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
                    return clazz.getMethod(k, args);
                } catch (NoSuchMethodException e) {
                    return NON_METHOD;
                }
            });
        }
    }

    public static ClassInfo getClassInfo(Class<?> clazz) {
        return classInfoMap.computeIfAbsent(clazz, k -> new ClassInfo(clazz));
    }

    public static void setValue(Object target, String fieldName, Object value) {
        ClassInfo classInfo = getClassInfo(target.getClass());
        Object field = classInfo.getField(fieldName);
        if (field == NON_FIELD) {
            throw new RuntimeException(String.format("no such [%s] field", fieldName));
        }
        Field f = (Field) field;
        classInfo.setValue(f, target, value);
    }

    private static String upperFirstChar(String c) {
        char[] chars = c.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

}
