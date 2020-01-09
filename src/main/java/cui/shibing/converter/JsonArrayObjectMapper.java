package cui.shibing.converter;

import cui.shibing.config.JsonConfig;
import cui.shibing.json.JsonArray;

import java.util.ArrayList;

public class JsonArrayObjectMapper extends AbstractObjectMapper {
    public JsonArrayObjectMapper(JsonConfig config) {
        super(config);
    }

    @Override
    public Object map(Object source, Class<?> targetClass) throws IllegalAccessException, InstantiationException {
        JsonArray s = (JsonArray) source;
        ArrayList<Object> result = new ArrayList<>();

        return result;
    }
}
