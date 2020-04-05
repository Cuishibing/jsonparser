package cui.shibing.intercepter;

import cui.shibing.config.JsonConfig;
import cui.shibing.token.JsonToken;

public class BooleanTokenInterceptor implements Interceptor<Boolean> {

    @Override
    public Boolean onToken(JsonToken token, JsonConfig config) {
        return Boolean.valueOf(token.getContent());
    }
}
