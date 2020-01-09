package cui.shibing;

import cui.shibing.config.JsonConfig;
import cui.shibing.converter.ObjectMapper;
import cui.shibing.json.JsonObject;

public class Example {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        String json = "{\"age\":1,\"aa\":{\"age\":24,\"aa\":{\"name\":\"name222\"}}}";

        JsonObject jsonObject = JsonObject.parseJsonObject(json);

        JsonConfig config = JsonConfig.getDefaultConfig();

        ObjectMapper converter = config.getConverter(JsonObject.class);

        Person person = (Person) converter.map(jsonObject, Person.class);

        System.out.println(person);
    }
}
