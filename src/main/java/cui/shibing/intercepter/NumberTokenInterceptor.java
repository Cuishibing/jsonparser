package cui.shibing.intercepter;

import cui.shibing.config.JsonConfig;
import cui.shibing.token.JsonToken;

import java.math.BigDecimal;

public class NumberTokenInterceptor implements Interceptor<Number> {
    @Override
    public Number onToken(JsonToken token, JsonConfig config) {
        String content = token.getContent();
        boolean isFloat = false;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '.' || c == 'e') {
                isFloat = true;
                break;
            }
        }
        if (isFloat) {
            try {
                return Double.valueOf(content);
            } catch (Exception e) {
                return new BigDecimal(content);
            }
        } else {
            try {
                return Integer.valueOf(content);
            } catch (Exception e) {
                return Long.valueOf(content);
            }
        }
    }
}
