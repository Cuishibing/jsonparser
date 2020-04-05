package cui.shibing.converter.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl.make;

public class ReflectionUtils {

    public static final Object NON_FIELD = new Object();

    public static final Object NON_METHOD = new Object();

    public static final Object NON_ACCESS = new Object();

    private static Map<Type, ClassInfo> classInfoMap = new ConcurrentHashMap<>();

    /**
     * 获取ClassInfo
     * 
     * @param type 类型
     * @return type类型对应的ClassInfo
     */
    public static ClassInfo getClassInfo(Type type) {
        return classInfoMap.computeIfAbsent(type, k -> new ClassInfo(type));
    }

    /**
     * 给一个属性设置值
     * 
     * @param target    要设置属性的对象实例
     * @param fieldName 属性名称
     * @param value     要设置的值
     */
    public static void setValue(Object target, String fieldName, Object value) {
        ClassInfo classInfo = getClassInfo(target.getClass());
        Object field = classInfo.getField(fieldName);
        if (field == NON_FIELD) {
            throw new RuntimeException(String.format("no such [%s] field", fieldName));
        }
        Field f = (Field) field;
        classInfo.setValue(f, target, value);
    }

    /**
     * 获取一个类型的真是类型，例如：类型 Map<Integer> 的真实类型是 Map.class
     * 
     * @param type 目标类型
     * @return 该类型的真是类型
     */
    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawType(((ParameterizedType) type).getRawType());
        }
        throw new RuntimeException(String.format("not support type [%s]", type));
    }

    public static Type makeType(Class<?> clazz, Type[] types, Type owner) {
        return make(clazz, types, owner);
    }

    /**
     * 获取一个泛型属性的泛型类型，例如：List<Integer>获取到Integer.
     * 
     * @param field       属性对应的Field对象
     * @param currentType 属性所在类的类型
     * @return 泛型属性的泛型类型
     */
    public static Type getFieldParameterizedType(Field field, Type currentType) {
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
            return makeType(field.getType(), actualTypeArguments, parameterizedType.getOwnerType());
        } else {
            if (genericType instanceof TypeVariable) {
                return getParameterTypeByName(genericType.getTypeName(), currentType, declaringClass);
            } else {
                return genericType;
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private static Type getParameterTypeByName(String name, Type currentType, Class<?> declaringClass) {
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
        } else if (type instanceof TypeVariable) {
            Type[] bounds = ((TypeVariable) type).getBounds();
            return bounds[0];
        } else
            return null;
    }

    private static Type getClosetGenericSuperclass(Type type) {
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

}
