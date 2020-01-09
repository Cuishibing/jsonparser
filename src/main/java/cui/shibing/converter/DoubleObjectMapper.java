package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

import java.math.BigDecimal;

public class DoubleObjectMapper extends AbstractObjectMapper {
    public DoubleObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException {
        if (targetClass == BigDecimal.class) {
            return (T)BigDecimal.valueOf((Double) source);
        }
        throw new RuntimeException(String.format("not support type [%s] map to [%s]", source.getClass(),targetClass));
    }
}
