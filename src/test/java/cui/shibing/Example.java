package cui.shibing;

import cui.shibing.json.Json;
import cui.shibing.json.JsonArray;

import java.io.IOException;
import java.util.List;

public class Example {


    public static void main(String[] args) throws IOException {

        String json = "[1,\"haha\",{},{\"key\":1.23e+12}]";

        JsonArray jsonArray = Json.parseJsonArray(json);

        List<Object> list = jsonArray.mapTo(List.class);

        System.out.println(list);
    }
}
