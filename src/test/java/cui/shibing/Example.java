package cui.shibing;

import java.util.List;

import cui.shibing.config.JsonConfig;
import cui.shibing.converter.ObjectMapper;
import cui.shibing.json.JsonArray;

public class Example {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        String json = "[1,2,3,444444,55555,66666]";

        JsonArray jsonObject = JsonArray.parseJsonArray(json);

        JsonConfig config = JsonConfig.getDefaultConfig();

        ObjectMapper converter = config.getObjectMapper(JsonArray.class);

        List<Number> values = converter.map(jsonObject, Long.class);

        System.out.println(values);
    }
}
