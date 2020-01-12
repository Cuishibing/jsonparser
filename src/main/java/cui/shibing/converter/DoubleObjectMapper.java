package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class DoubleObjectMapper extends AbstractObjectMapper {
    public DoubleObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Type type) {
        if (type == BigDecimal.class) {
            return (T) BigDecimal.valueOf((Double) source);
        } else if (type == String.class) {
            return (T) source.toString();
        }
        throw new RuntimeException(String.format("not support type [%s] map to [%s]", source.getClass(), type));
    }
}
