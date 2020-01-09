package cui.shibing.intercepter;

import cui.shibing.config.JsonConfig;
import cui.shibing.token.JsonToken;

public interface Interceptor<T> {
    T onToken(JsonToken token, JsonConfig config);
}
