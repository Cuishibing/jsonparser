package cui.shibing.converter;

public interface ObjectMapper {
    <T> T map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException;
}
