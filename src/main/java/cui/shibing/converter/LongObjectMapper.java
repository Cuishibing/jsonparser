package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;

public class LongObjectMapper extends AbstractObjectMapper {
    public LongObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Type type) {
        Long s = (Long) source;
        if (type instanceof Class) {
            switch (((Class) type).getSimpleName()) {
                case "Double":
                case "double":
                    return (T) Double.valueOf(s.toString());
                case "Date":
                    return (T) new Date(s);
                case "BigDecimal":
                    return (T) new BigDecimal(s);
                case "String":
                    return (T) s.toString();
            }
        }
        throw new RuntimeException(String.format("not support type [%s] map to [%s]", source.getClass(), type));
    }
}
