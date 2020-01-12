package cui.shibing.json;

import cui.shibing.config.JsonConfig;
import cui.shibing.converter.ObjectMapper;
import cui.shibing.parser.JsonParser;
import cui.shibing.scanner.JsonTokenScanner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonArray {
    private List<Object> attrs;

    public int size() {
        return attrs != null ? attrs.size() : 0;
    }

    public void add(Object attr) {
        if (attrs == null) {
            attrs = new ArrayList<>();
        }
        attrs.add(attr);
    }

    public Object get(int index) {
        if (attrs == null) {
            return null;
        }
        return attrs.get(index);
    }

    public String getString(int index) {
        return (String) get(index);
    }

    public Long getLong(int key) {
        return (Long) get(key);
    }

    public Integer getInteger(int key) {
        return (Integer) get(key);
    }

    public Number getNumber(int key) {
        return (Number) get(key);
    }

    public BigDecimal getBigDecimal(int key) {
        return (BigDecimal) get(key);
    }

    public Double getDouble(int key) {
        return (Double) get(key);
    }

    public Boolean getBoolean(int key) {
        return (Boolean) get(key);
    }

    public JsonObject getJsonObject(int key) {
        return (JsonObject) get(key);
    }

    public JsonArray getJsonArray(int key) {
        return (JsonArray) get(key);
    }

    public <T> T mapTo(Type type) {
        return mapTo(type,JsonConfig.getDefaultConfig());
    }

    public <T> T mapTo(Type type,JsonConfig config){
        ObjectMapper objectMapper = config.getObjectMapper(JsonArray.class);
        return objectMapper.map(this,type);
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

    public static JsonArray parseJsonArray(URI uri) throws IOException {
        URL url = uri.toURL();
        try (Reader reader = new InputStreamReader(url.openStream())) {
            return parseJsonArray(reader, JsonConfig.getDefaultConfig());
        }
    }

    public static JsonArray parseJsonArray(URI uri, JsonConfig config) throws IOException {
        URL url = uri.toURL();
        try (Reader reader = new InputStreamReader(url.openStream())) {
            return parseJsonArray(reader, config);
        }
    }

}
