package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

import java.math.BigDecimal;
import java.util.Date;

public class LongObjectMapper extends AbstractObjectMapper {
    public LongObjectMapper(JsonConfig config) {
        super(config);
    }

    @Override
    public Object map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException {
        Long s = (Long) source;
        if (targetClass == Double.class) {
            return Double.valueOf(s.toString());
        }
        if (targetClass == Date.class) {
            return new Date(s);
        }
        if (targetClass == BigDecimal.class) {
            return new BigDecimal(s);
        }
        return null;
    }
}
