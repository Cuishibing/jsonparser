package cui.shibing.json;

import cui.shibing.config.JsonConfig;
import cui.shibing.parser.JsonParser;
import cui.shibing.scanner.JsonTokenScanner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * JsonObj
 */
public class JsonObject {

    private Map<String, Object> attrs;

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

    @Override
    public String toString() {
        return Objects.toString(attrs);
    }


    public static JsonObject parseJsonObject(Reader reader) {
        return parseJsonObject(reader,JsonConfig.getDefaultConfig());
    }

    public static JsonObject parseJsonObject(Reader reader, JsonConfig config) {
        JsonParser jsonParser = new JsonParser(new JsonTokenScanner(reader),config);
        try {
            return jsonParser.parseJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject parseJsonObject(String json) {
        return parseJsonObject(new InputStreamReader(new ByteArrayInputStream(json.getBytes())));
    }

    public static JsonObject parseJsonObject(URI uri) throws IOException {
        URL url = uri.toURL();
        try (Reader reader = new InputStreamReader(url.openStream())) {
            return parseJsonObject(reader);
        }
    }

}