package cui.shibing.converter;

import cui.shibing.config.JsonConfig;
import cui.shibing.json.JsonArray;

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
    public <T> T map(Object source, Type type) throws IllegalAccessException, InstantiationException {

        JsonArray s = (JsonArray) source;

        Class<?> rawType = getRawType(type);
        if (!List.class.isAssignableFrom(rawType)) {
            throw new RuntimeException(String.format("not support type [%s] on json array", type));
        }
        Type actualTypeArgument = Object.class;
        if (type instanceof ParameterizedType) {
            actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        List result;
        if (rawType.isInterface()) {
            if (rawType != List.class) {
                throw new RuntimeException(String.format("not support type [%s]", type));
            }
            result = new ArrayList<>();
        } else {
            result = (List) rawType.newInstance();
        }
        for (int i = 0; i < s.size(); i++) {
            result.add(mapValue(s.get(i), actualTypeArgument));
        }
        return (T) result;
    }
}
