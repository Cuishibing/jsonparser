package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

import java.lang.reflect.Type;

public class IntegerObjectMapper extends AbstractObjectMapper {
    public IntegerObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Type type){
        Integer s = (Integer) source;
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            switch (clazz.getSimpleName()) {
                case "Long":
                case "long":
                    return (T) Long.valueOf(s);
                case "Float":
                case "float":
                    return (T) Float.valueOf(s.toString());
                case "Double":
                case "double":
                    return (T) Double.valueOf(s.toString());
                case "int":
                    return (T) s;
                case "String":
                    return (T)s.toString();
            }
        }
        throw new RuntimeException(String.format("not support type [%s] map to [%s]", source.getClass(), type));
    }
}
