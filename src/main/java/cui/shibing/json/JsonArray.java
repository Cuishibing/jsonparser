package cui.shibing.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JsonArray extends Json {
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

}
