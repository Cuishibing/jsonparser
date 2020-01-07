package cui.shibing;

import cui.shibing.config.JsonConfig;
import cui.shibing.intercepter.Interceptor;
import cui.shibing.json.JsonArray;
import cui.shibing.json.JsonObject;
import cui.shibing.token.JsonToken;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Example {
    public static void main(String[] args) throws URISyntaxException, IOException {
        final JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerInterceptor(JsonToken.TokenType.STR, (Interceptor<String>) token ->
                token.getContent() + "_modify");

        String json = "{\"name\":\"aabbccdd\",\"age\":\"jljkl\"}";

        JsonObject jsonObject = JsonObject.parseJsonObject(json,jsonConfig);

        System.out.println(jsonObject.getString("name"));
        System.out.println(jsonObject.getString("age"));

        json = "[1,2,3,1.123e+12]";

        JsonArray jsonArray = JsonArray.parseJsonArray(json,jsonConfig);
        System.out.println(jsonArray.getLong(0));
        System.out.println(jsonArray.getDouble(3));

        // read json from a uri
        URI jsonUri = new URI("file:///c:/Users/Cuishibing/Desktop/temp/temp.json");
        jsonObject = JsonObject.parseJsonObject(jsonUri);
        System.out.println(jsonObject.getString("name"));
    }
}
