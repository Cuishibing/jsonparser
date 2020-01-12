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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * JsonObj
 */
public class JsonObject {

    private Map<String, Object> attrs;

    public Set<String> keySet() {
        if (attrs == null) {
            return Collections.emptySet();
        }
        return attrs.keySet();
    }

    public Object get(String key) {
        if (attrs == null) {
            return null;
        }
        return attrs.get(key);
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public Long getLong(String key) {
        return (Long) get(key);
    }

    public Integer getInteger(String key) {
        return (Integer) get(key);
    }

    public Number getNumber(String key) {
        return (Number) get(key);
    }

    public BigDecimal getBigDecimal(String key) {
        return (BigDecimal) get(key);
    }

    public Double getDouble(String key) {
        return (Double) get(key);
    }

    public Boolean getBoolean(String key) {
        return (Boolean) get(key);
    }

    public JsonObject getJsonObject(String key) {
        return (JsonObject) get(key);
    }

    public JsonArray getJsonArray(String key) {
        return (JsonArray) get(key);
    }

    public void set(String key, Object attr) {
        if (attrs == null) {
            attrs = new HashMap<>();
        }
        attrs.put(key, attr);
    }

    public <T> T mapTo(Type type) {
        return mapTo(type, JsonConfig.getDefaultConfig());
    }

    public <T> T mapTo(Type type, JsonConfig config) {
        ObjectMapper objectMapper = config.getObjectMapper(JsonObject.class);
        return objectMapper.map(this, type);
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

    public static JsonObject parseJsonObject(URI uri) throws IOException {
        return parseJsonObject(uri, JsonConfig.getDefaultConfig());
    }

    public static JsonObject parseJsonObject(URI uri, JsonConfig config) throws IOException {
        URL url = uri.toURL();
        try (Reader reader = new InputStreamReader(url.openStream())) {
            return parseJsonObject(reader, config);
        }
    }

}