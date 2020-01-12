package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

import java.lang.reflect.Type;

public class BooleanObjectMapper extends AbstractObjectMapper {
    public BooleanObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Type type) {
        Boolean s = (Boolean) source;
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            switch (clazz.getSimpleName()) {
                case "Boolean":
                case "boolean":
                    return (T) s;
                case "String":
                    return (T) s.toString();
            }
        }
        throw new RuntimeException(String.format("not support type [%s] map to [%s]", source.getClass(), type));
    }
}
