package cui.shibing;

import cui.shibing.converter.reflection.ReflectionUtils;
import cui.shibing.json.Json;
import cui.shibing.json.JsonArray;
import cui.shibing.json.JsonObject;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.List;
public class Example {


    public static void main(String[] args) {

        String json = "[1,\"haha\",{},{\"key\":1.23e+12}]";

        JsonArray jsonArray = Json.parseJsonArray(json);

        List<Object> list = jsonArray.mapTo(List.class);

        System.out.println(list);

        String userJson = "{\"name\":\"cuishibing\", \"age\":25}";

        JsonObject obj = Json.parseJsonObject(userJson);
        User<String> user = obj.mapTo(StringNameUser.class);
        System.out.println(user);

        User<String> user1 = obj.mapTo(ReflectionUtils.makeType(User.class, new Type[]{String.class},null));
        System.out.println(user1);
    }

    @Data
    public static class User<T> {
        private T name;
        private int age;
    }

    public static class StringNameUser extends User<String> {

    }
}
