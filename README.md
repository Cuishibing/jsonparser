## 一个简单的Json解析器
- 解析一个json串到Json对象（`JsonObject`或`JsonArray`）
````java
public class Example {

    public static void main(String[] args) {

        String json = "[1,\"haha\",{},{\"key\":1.23e+12}]";

        JsonArray jsonArray = JsonArray.parseJsonArray(json);

        json = "{\"name\":\"fake name\"}";
        
        JsonObject jsonObject = JsonObject.parseJsonObject(json);
    }
}
````   

- 把Json对象映射到Java对象
````java
public class Example {

    public static void main(String[] args) {

        String json = "[1,\"haha\",{},{\"key\":1.23e+12}]";

        JsonArray jsonArray = JsonArray.parseJsonArray(json);

        List<Object> list = jsonArray.mapTo(List.class);

        System.out.println(list);

    }
}
````
## 下一步要做的
- 直接把json串映射到某个Bean