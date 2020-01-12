package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

import java.lang.reflect.Type;

public class StringObjectMapper extends AbstractObjectMapper {
    public StringObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Type type) {
        String s = (String) source;
        if (type instanceof Class) {
            switch (((Class<?>) type).getSimpleName()) {
                case "StringBuilder":
                    return (T) new StringBuilder(s);
                case "int":
                case "Integer":
                    return (T) Integer.valueOf(s);
                case "String":
                    return (T) s;
            }
        }
        throw new RuntimeException(String.format("not support type [%s] map to [%s]", source.getClass(), type));
    }
}
