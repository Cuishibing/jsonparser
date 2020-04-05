package cui.shibing.converter;

import cui.shibing.config.JsonConfig;
import cui.shibing.converter.reflection.ClassInfo;
import cui.shibing.converter.reflection.ReflectionUtils;
import cui.shibing.json.JsonArray;
import cui.shibing.json.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JsonObjectObjectMapper extends AbstractObjectMapper {

    public JsonObjectObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Type type) {
        JsonObject jsonObject = (JsonObject) source;
        Class<?> clazz = getRawType(type);
        if (Map.class.isAssignableFrom(clazz)) {
            try {
                return (T) mapToMap(jsonObject, clazz, type);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }

        } else {
            if ("String".equals(clazz.getSimpleName())) {
                return (T) mapToString(jsonObject);
            }
            // java bean
            try {
                return (T) mapToBean(jsonObject, clazz, type);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mapToMap(JsonObject jsonObject, Class<?> clazz, Type type)
            throws IllegalAccessException, InstantiationException {
        Map<String, Object> instance;
        if (clazz.isInterface()) {
            if (clazz == Map.class) {
                instance = new HashMap<>();
            } else {
                throw new RuntimeException(String.format("not support type [%s]", type));
            }
        } else {
            instance = (Map<String, Object>) clazz.newInstance();
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
            instance.put(k, o);
        }
        return instance;
    }

    private Object mapToBean(JsonObject jsonObject, Class<?> clazz, Type type)
            throws IllegalAccessException, InstantiationException {
        if (clazz.isInterface()) {
            throw new RuntimeException(String.format("type [%s] is interface or abstract class", type));
        }
        Object instance = clazz.newInstance();
        for (String k : jsonObject.keySet()) {
            ClassInfo classInfo = ReflectionUtils.getClassInfo(clazz);
            Object field = classInfo.getField(k);
            if (field == ReflectionUtils.NON_FIELD) {
                // ignore un known property
                continue;
            }
            Field f = (Field) field;
            classInfo.setValue(f, instance,
                    mapValue(jsonObject.get(k), ReflectionUtils.getFieldParameterizedType(f, type)));
        }
        return instance;
    }

    private String mapToString(JsonObject jsonObject) {
        StringBuilder builder = new StringBuilder();
        builder.append('{');

        for (String k : jsonObject.keySet()) {
            builder.append("\"").append(k).append("\"").append(":");
            Object v = jsonObject.get(k);

            if (v == null) {
                builder.append("null").append(",");
                continue;
            }

            Class<?> vClass = v.getClass();
            ObjectMapper objectMapper = config.getObjectMapper(vClass);

            String vStr;
            vStr = objectMapper.map(v, String.class);
            if (v instanceof JsonObject || v instanceof JsonArray) {
                builder.append(vStr);
            } else {
                boolean needQuote = false;
                if (v instanceof CharSequence) {
                    needQuote = true;
                }
                if (needQuote) {
                    builder.append("\"");
                }
                builder.append(vStr);
                if (needQuote) {
                    builder.append("\"");
                }
            }
            builder.append(",");
        }
        if (jsonObject.keySet().size() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("}");
        return builder.toString();
    }

}
