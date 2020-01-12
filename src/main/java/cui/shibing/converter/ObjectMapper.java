package cui.shibing.converter;

import java.lang.reflect.Type;

public interface ObjectMapper {
    <T> T map(Object source, Type type);
}
