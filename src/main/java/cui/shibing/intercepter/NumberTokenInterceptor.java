package cui.shibing.intercepter;

import cui.shibing.token.JsonToken;
import cui.shibing.token.NumberValueToken;

public class NumberTokenInterceptor implements Interceptor<Number> {
    @Override
    public Number onToken(JsonToken token) {
        NumberValueToken numberToken = (NumberValueToken) token;
        return numberToken.getNumber();
    }
}
