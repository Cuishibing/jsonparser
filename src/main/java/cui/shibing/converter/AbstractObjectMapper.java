package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

public abstract class AbstractObjectMapper implements ObjectMapper {

    protected JsonConfig config;

    public AbstractObjectMapper(JsonConfig config) {
        this.config = config;
    }

    protected Object mapValue(Object value, Class<?> clazz) throws InstantiationException, IllegalAccessException {
        if (value == null) {
            return null;
        }
        if (clazz == value.getClass()) {
            return value;
        }
        if (clazz == Object.class) {
            return value;
        }
        if (clazz.isAssignableFrom(value.getClass())) {
            return value;
        }
        ObjectMapper objectMapper = config.getObjectMapper(value.getClass());
        if (objectMapper == null) {
            throw new RuntimeException(String.format("type [%s] no object mapper", value.getClass()));
        }
        return objectMapper.map(value, clazz);
    }
}
