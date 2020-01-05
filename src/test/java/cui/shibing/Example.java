package cui.shibing;

import cui.shibing.json.JsonArray;
import cui.shibing.json.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Example {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String json = "{\"name\":\"aabbccdd\",\"age\":23}";

        JsonObject jsonObject = JsonObject.parseJsonObject(json);

        System.out.println(jsonObject.getString("name"));
        System.out.println(jsonObject.getLong("age"));

        json = "[1,2,3,1.123e+12]";

        JsonArray jsonArray = JsonArray.parseJsonArray(json);
        System.out.println(jsonArray.getLong(0));
        System.out.println(jsonArray.getDouble(3));

        // read json from a uri
        URI jsonUri = new URI("file:///c:/Users/Cuishibing/Desktop/temp/temp.json");
        jsonObject = JsonObject.parseJsonObject(jsonUri);
        System.out.println(jsonObject.getString("name"));
    }
}
