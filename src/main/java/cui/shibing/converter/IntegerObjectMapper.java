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
        switch (targetClass.getSimpleName()) {
            case "Long":
            case "long":
                return Long.valueOf(s);
            case "Float":
            case "float":
                return Float.valueOf(s.toString());
            case "Double":
            case "double":
                return Double.valueOf(s.toString());
            case "BigDecimal":
                return new BigDecimal(s);
            case "int":
                return s;
        }
        return null;
    }
}
