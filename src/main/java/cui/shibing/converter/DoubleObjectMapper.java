package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

import java.math.BigDecimal;

public class DoubleObjectMapper extends AbstractObjectMapper {
    public DoubleObjectMapper(JsonConfig config) {
        super(config);
    }

    @Override
    public Object map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException {
        if (targetClass == BigDecimal.class) {
            return BigDecimal.valueOf((Double) source);
        }
        return null;
    }
}
