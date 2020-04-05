package cui.shibing.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import cui.shibing.config.JsonConfig;
import cui.shibing.converter.ObjectMapper;
import cui.shibing.parser.JsonParser;
import cui.shibing.scanner.JsonTokenScanner;

public class Json {
    public <T> T mapTo(Type type) {
        return mapTo(type, JsonConfig.getDefaultConfig());
    }

    public <T> T mapTo(Type type, JsonConfig config) {
        ObjectMapper objectMapper = config.getObjectMapper(getClass());
        return objectMapper.map(this, type);
    }

    public static JsonArray parseJsonArray(Reader reader) {
        return parseJsonArray(reader, JsonConfig.getDefaultConfig());
    }

    public static JsonArray parseJsonArray(Reader reader, JsonConfig config) {
        JsonParser jsonParser = new JsonParser(new JsonTokenScanner(reader), config);
        try {
            return jsonParser.parseJsonArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonArray parseJsonArray(String json) {
        return parseJsonArray(json, JsonConfig.getDefaultConfig());
    }

    public static JsonArray parseJsonArray(String json, JsonConfig config) {
        return parseJsonArray(new InputStreamReader(new ByteArrayInputStream(json.getBytes())), config);
    }

    public static JsonObject parseJsonObject(Reader reader) {
        return parseJsonObject(reader, JsonConfig.getDefaultConfig());
    }

    public static JsonObject parseJsonObject(Reader reader, JsonConfig config) {
        JsonParser jsonParser = new JsonParser(new JsonTokenScanner(reader), config);
        try {
            return jsonParser.parseJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject parseJsonObject(String json) {
        return parseJsonObject(json, JsonConfig.getDefaultConfig());
    }

    public static JsonObject parseJsonObject(String json, JsonConfig config) {
        return parseJsonObject(new InputStreamReader(new ByteArrayInputStream(json.getBytes())), config);
    }

}
