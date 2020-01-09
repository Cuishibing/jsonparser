package cui.shibing.token;

import cui.shibing.config.JsonConfig;
import lombok.Setter;

/**
 * NumberValueToken
 */
public class NumberValueToken extends JsonToken {

    /**
     * whether or not a float number (content has '.')
     */
    @Setter
    private boolean isFloat;

    public NumberValueToken(String content, boolean isFloat) {
        super(TokenType.NUMBER, content);
        this.isFloat = isFloat;
    }

    public Number getNumber(JsonConfig config) {
        if (isFloat) {
            return Double.valueOf(content);
        } else {
            long value = Long.parseLong(content);
            if (value <= Integer.MAX_VALUE) {
                return (int) value;
            }
            return value;
        }
    }
}