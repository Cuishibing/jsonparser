package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

import java.math.BigDecimal;
import java.util.Date;

public class LongObjectMapper extends AbstractObjectMapper {
    public LongObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException {
        Long s = (Long) source;
        switch (targetClass.getSimpleName()) {
            case "Double":
            case "double":
                return (T)Double.valueOf(s.toString());
            case "Date":
                return (T)new Date(s);
            case "BigDecimal":
                return (T)new BigDecimal(s);
        }
        throw new RuntimeException(String.format("not support type [%s] map to [%s]", source.getClass(),targetClass));
    }
}
