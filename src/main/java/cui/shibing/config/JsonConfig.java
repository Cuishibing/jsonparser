package cui.shibing.config;


import cui.shibing.intercepter.BooleanTokenInterceptor;
import cui.shibing.intercepter.Interceptor;
import cui.shibing.intercepter.NumberTokenInterceptor;
import cui.shibing.intercepter.StringTokenInterceptor;
import cui.shibing.token.JsonToken;

import java.util.HashMap;
import java.util.Map;

public final class JsonConfig {

    private static final JsonConfig DEFAULT_CONFIG = new JsonConfig();

    private final Map<JsonToken.TokenType, Interceptor<?>> interceptorRegistry = new HashMap<>();

    public static JsonConfig getDefaultConfig() {
        return DEFAULT_CONFIG;
    }

    public JsonConfig() {
        registerInterceptor(JsonToken.TokenType.STR,new StringTokenInterceptor());
        registerInterceptor(JsonToken.TokenType.BOOLEAN, new BooleanTokenInterceptor());
        registerInterceptor(JsonToken.TokenType.NUMBER,new NumberTokenInterceptor());
    }

    public void registerInterceptor(JsonToken.TokenType type, Interceptor<?> interceptor) {
        interceptorRegistry.put(type, interceptor);
    }

    public Interceptor<?> getInterceptor(JsonToken.TokenType type) {
        return interceptorRegistry.get(type);
    }
}
