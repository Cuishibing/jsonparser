package cui.shibing.intercepter;

import cui.shibing.config.JsonConfig;
import cui.shibing.token.JsonToken;

public class StringTokenInterceptor implements Interceptor<String> {
    @Override
    public String onToken(JsonToken token, JsonConfig config) {
        return token.getContent();
    }
}
