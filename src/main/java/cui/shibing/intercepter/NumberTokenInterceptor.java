package cui.shibing.intercepter;

import cui.shibing.config.JsonConfig;
import cui.shibing.token.JsonToken;
import cui.shibing.token.NumberValueToken;

public class NumberTokenInterceptor implements Interceptor<Number> {
    @Override
    public Number onToken(JsonToken token, JsonConfig config) {
        NumberValueToken numberToken = (NumberValueToken) token;
        return numberToken.getNumber(config);
    }
}
