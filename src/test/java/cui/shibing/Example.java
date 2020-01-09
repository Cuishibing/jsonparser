package cui.shibing;

import cui.shibing.config.JsonConfig;
import cui.shibing.converter.ObjectMapper;
import cui.shibing.json.JsonArray;

public class Example {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        String json = "[1,2,3,444444,55555,66666]";

        JsonArray jsonObject = JsonArray.parseJsonArray(json);

        JsonConfig config = JsonConfig.getDefaultConfig();

        ObjectMapper converter = config.getConverter(JsonArray.class);

        Object map = converter.map(jsonObject, float.class);

        System.out.println(map);
    }
}
