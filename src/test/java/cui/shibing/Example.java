package cui.shibing;

import cui.shibing.json.JsonArray;

import java.util.List;

public class Example {


    public static void main(String[] args) {

        String json = "[1,\"haha\",{},{\"key\":1.23e+12}]";

        JsonArray jsonArray = JsonArray.parseJsonArray(json);

        List<Object> list = jsonArray.mapTo(List.class);

        System.out.println(list);

    }
}
