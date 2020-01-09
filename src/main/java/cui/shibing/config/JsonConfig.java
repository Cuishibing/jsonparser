package cui.shibing.config;


import cui.shibing.converter.*;
import cui.shibing.intercepter.BooleanTokenInterceptor;
import cui.shibing.intercepter.Interceptor;
import cui.shibing.intercepter.NumberTokenInterceptor;
import cui.shibing.intercepter.StringTokenInterceptor;
import cui.shibing.json.JsonArray;
import cui.shibing.json.JsonObject;
import cui.shibing.token.JsonToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class JsonConfig extends Properties {

    private static final JsonConfig DEFAULT_CONFIG = new JsonConfig();

    private final Map<JsonToken.TokenType, Interceptor<?>> interceptorRegistry = new HashMap<>();

    private final Map<Class<?>, ObjectMapper> objectMapperRegistry = new HashMap<>();

    public static JsonConfig getDefaultConfig() {
        return DEFAULT_CONFIG;
    }

    public JsonConfig() {
        registerInterceptor(JsonToken.TokenType.STR, new StringTokenInterceptor());
        registerInterceptor(JsonToken.TokenType.BOOLEAN, new BooleanTokenInterceptor());
        registerInterceptor(JsonToken.TokenType.NUMBER, new NumberTokenInterceptor());

        registerObjectMapper(JsonObject.class, new JsonObjectObjectMapper(this));
        registerObjectMapper(JsonArray.class ,new JsonArrayObjectMapper(this));
        registerObjectMapper(String.class, new StringObjectMapper(this));
        registerObjectMapper(Integer.class, new IntegerObjectMapper(this));
        registerObjectMapper(Long.class, new LongObjectMapper(this));
        registerObjectMapper(Double.class, new DoubleObjectMapper(this));
    }

    public void registerInterceptor(JsonToken.TokenType type, Interceptor<?> interceptor) {
        interceptorRegistry.put(type, interceptor);
    }

    public Interceptor<?> getInterceptor(JsonToken.TokenType type) {
        return interceptorRegistry.get(type);
    }

    public void registerObjectMapper(Class<?> clazz, ObjectMapper mapper) {
        objectMapperRegistry.put(clazz, mapper);
    }

    public ObjectMapper getConverter(Class<?> clazz) {
        return objectMapperRegistry.get(clazz);
    }
}
