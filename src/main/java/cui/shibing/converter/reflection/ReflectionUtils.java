package cui.shibing.converter.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {

    public static final Object NON_FIELD = new Object();

    public static final Object NON_METHOD = new Object();

    public static final Object NON_ACCESS = new Object();

    private static Map<Type, ClassInfo> classInfoMap = new HashMap<>();


    public static ClassInfo getClassInfo(Type type) {
        return classInfoMap.computeIfAbsent(type, k -> new ClassInfo(type));
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

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawType(((ParameterizedType) type).getRawType());
        }
        throw new RuntimeException(String.format("not support type [%s]", type));
    }

}
