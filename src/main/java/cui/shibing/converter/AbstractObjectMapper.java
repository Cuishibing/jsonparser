package cui.shibing.converter;

import cui.shibing.config.JsonConfig;
import cui.shibing.converter.reflection.ReflectionUtils;
import cui.shibing.json.JsonArray;
import cui.shibing.json.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractObjectMapper implements ObjectMapper {

    protected JsonConfig config;

    public AbstractObjectMapper(JsonConfig config) {
        this.config = config;
    }

    protected Object mapValue(Object value, Type type) {
        if (value == null) {
            return null;
        }
        if (type == value.getClass()) {
            return value;
        }
        if (type == Object.class) {
            if (value instanceof JsonObject) {
                // JsonObject --> HashMap
                ObjectMapper objectMapper = config.getObjectMapper(JsonObject.class);
                return objectMapper.map(value, HashMap.class);
            } else if (value instanceof JsonArray) {
                // JsonArray --> ArrayList
                ObjectMapper objectMapper = config.getObjectMapper(JsonArray.class);
                return objectMapper.map(value, ArrayList.class);
            }
            return value;
        }
        if (type instanceof Class) {
            if (((Class<?>) type).isAssignableFrom(value.getClass())) {
                return value;
            }
        }
        ObjectMapper objectMapper = config.getObjectMapper(value.getClass());
        if (objectMapper == null) {
            throw new RuntimeException(String.format("type [%s] no object mapper", value.getClass()));
        }
        return objectMapper.map(value, type);
    }

    protected Class<?> getRawType(Type type) {
        return ReflectionUtils.getRawType(type);
    }
}
