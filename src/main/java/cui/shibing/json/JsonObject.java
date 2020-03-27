package cui.shibing.json;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * JsonObj
 */
public class JsonObject extends Json {

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

}