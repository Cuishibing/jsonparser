package cui.shibing.converter;

import cui.shibing.config.JsonConfig;
import cui.shibing.json.JsonArray;

import java.util.ArrayList;

public class JsonArrayObjectMapper extends AbstractObjectMapper {
    public JsonArrayObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException {
        
        JsonArray s = (JsonArray) source;
        ArrayList<Object> result = new ArrayList<>();

        for (int i = 0; i < s.size(); i++) {
            result.add(mapValue(s.get(i), targetClass));
        }
        return (T)result;
    }
}
