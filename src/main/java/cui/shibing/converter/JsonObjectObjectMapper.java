package cui.shibing.converter;

import cui.shibing.config.JsonConfig;
import cui.shibing.converter.reflection.ReflectionUtils;
import cui.shibing.json.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class JsonObjectObjectMapper extends AbstractObjectMapper implements ObjectMapper {

    public JsonObjectObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Type type) throws IllegalAccessException, InstantiationException {
        JsonObject jsonObject = (JsonObject) source;
        Object instance;
        Class<?> clazz = getRawType(type);
        if (Map.class.isAssignableFrom(clazz)) {
            if (clazz.isInterface()) {
                if (clazz == Map.class) {
                    instance = new HashMap<>();
                } else {
                    throw new RuntimeException(String.format("not support type [%s]", type));
                }
            } else {
                instance = clazz.newInstance();
            }
            Type valueType = Object.class;
            // try to get generic type
            if (type instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 2) {
                    valueType = actualTypeArguments[1];
                }
            }
            for (String k : jsonObject.keySet()) {
                Object o = mapValue(jsonObject.get(k), valueType);
                ((Map)instance).put(k,o);
            }
        } else {
            if (clazz.isInterface()) {
                throw  new RuntimeException(String.format("type [%s] is interface or abstract class", type));
            }
            instance = clazz.newInstance();
            for (String k : jsonObject.keySet()) {
                ReflectionUtils.ClassInfo classInfo = ReflectionUtils.getClassInfo(clazz);
                Object field = classInfo.getField(k);
                if (field == ReflectionUtils.NON_FIELD) {
                    // ignore un known property
                    continue;
                }
                Field f = (Field) field;
                Type t;
                if (f.getGenericType() instanceof TypeVariable) {
                    // can not get more detail type info
                    t = f.getType();
                } else {
                    t = f.getGenericType();
                }
                classInfo.setValue(f, instance, mapValue(jsonObject.get(k), t));
            }
        }

        return (T) instance;
    }

}
