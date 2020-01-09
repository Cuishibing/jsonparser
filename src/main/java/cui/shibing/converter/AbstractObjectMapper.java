package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

public abstract class AbstractObjectMapper implements ObjectMapper {

    protected JsonConfig config;

    public AbstractObjectMapper(JsonConfig config) {
        this.config = config;
    }

    // 根据field的类型，转换value值
    protected Object mapValue(Object value, Class<?> clazz) throws InstantiationException, IllegalAccessException {
        if (value == null) {
            return null;
        }
        if (clazz == value.getClass()) {
            return value;
        }
        ObjectMapper objectMapper = config.getConverter(value.getClass());
        if (objectMapper == null) {
            throw new RuntimeException(String.format("no [%s] converter", value.getClass()));
        }
        return objectMapper.map(value, clazz);
    }
}
