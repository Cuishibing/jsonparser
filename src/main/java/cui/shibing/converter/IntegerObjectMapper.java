package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

import java.math.BigDecimal;

public class IntegerObjectMapper extends AbstractObjectMapper {
    public IntegerObjectMapper(JsonConfig config) {
        super(config);
    }

    @Override
    public Object map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException {
        Integer s = (Integer) source;
        if (targetClass == Long.class) {
            return Long.valueOf(s);
        }
        if (targetClass == Float.class) {
            return Float.valueOf(s.toString());
        }
        if (targetClass == Double.class) {
            return Double.valueOf(s.toString());
        }
        if (targetClass == BigDecimal.class) {
            return new BigDecimal(s);
        }
        return null;
    }
}
