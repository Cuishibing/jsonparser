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
        switch (targetClass.getSimpleName()) {
            case "Double":
            case "double":
                return Double.valueOf(s.toString());
            case "Date":
                return new Date(s);
            case "BigDecimal":
                return new BigDecimal(s);
        }
        return null;
    }
}
