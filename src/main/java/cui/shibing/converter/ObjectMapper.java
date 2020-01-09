package cui.shibing.converter;

public interface ObjectMapper {
    Object map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException;
}
