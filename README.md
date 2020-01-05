# 一个简单的Json解析器
## 如何使用
````java
public class Example {
    public static void main(String[] args) {
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
````
目前存在的问题：
- 解析数字时，整数都解析成`Long`类型
- 对于json语法错误，错误信息不够详细

下一步要做的：
- 语法错误信息更加详细
- 提供解析时的拦截器机制，用户可以自己处理解析到的值；提供一些基础的拦截器，比如：整数、浮点数的类型处理等
- 直接把json串映射到某个Bean