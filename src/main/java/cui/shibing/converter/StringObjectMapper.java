package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

public class StringObjectMapper extends AbstractObjectMapper {
    public StringObjectMapper(JsonConfig config) {
        super(config);
    }

    @Override
    public Object map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException {
        String s = (String) source;
        if (targetClass == StringBuilder.class) {
            return new StringBuilder(s);
        }
        return null;
    }
}
