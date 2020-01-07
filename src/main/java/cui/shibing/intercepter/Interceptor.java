package cui.shibing.intercepter;

import cui.shibing.token.JsonToken;

public interface Interceptor<T> {
    T onToken(JsonToken token);
}
