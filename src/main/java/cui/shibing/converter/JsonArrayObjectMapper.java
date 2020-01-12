package cui.shibing.converter;

import cui.shibing.config.JsonConfig;
import cui.shibing.json.JsonArray;
import cui.shibing.json.JsonObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonArrayObjectMapper extends AbstractObjectMapper {
    public JsonArrayObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Type type) {

        JsonArray s = (JsonArray) source;

        Class<?> rawType = getRawType(type);

        if (rawType == String.class) {
            return (T) mapToString(s);
        }

        if (!List.class.isAssignableFrom(rawType)) {
            throw new RuntimeException(String.format("not support type [%s] on json array", type));
        }
        Type actualTypeArgument = Object.class;
        if (type instanceof ParameterizedType) {
            actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        List<Object> result;
        if (rawType.isInterface()) {
            if (rawType != List.class) {
                throw new RuntimeException(String.format("not support type [%s]", type));
            }
            result = new ArrayList<>();
        } else {
            try {
                result = (List<Object>) rawType.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < s.size(); i++) {
            result.add(mapValue(s.get(i), actualTypeArgument));
        }
        return (T) result;
    }

    private String mapToString(JsonArray jsonArray) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (int i = 0; i < jsonArray.size(); i++) {
            Object v = jsonArray.get(i);

            if (v == null) {
                builder.append("null").append(",");
                continue;
            }

            Class<?> vClass = v.getClass();
            ObjectMapper objectMapper = config.getObjectMapper(vClass);
            Object vStr = objectMapper.map(v, String.class);
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

        if (jsonArray.size() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");
        return builder.toString();
    }
}
