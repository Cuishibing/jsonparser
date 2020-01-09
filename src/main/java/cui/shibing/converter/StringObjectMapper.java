package cui.shibing.converter;

import cui.shibing.config.JsonConfig;

public class StringObjectMapper extends AbstractObjectMapper {
    public StringObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException {
        String s = (String) source;
        switch (targetClass.getSimpleName()) {
            case "StringBuilder":
                return (T) new StringBuilder(s);
        }
        throw new RuntimeException(String.format("not support type [%s] map to [%s]", source.getClass(),targetClass));
    }
}
