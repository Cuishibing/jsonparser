package cui.shibing.token;

import lombok.Setter;

/**
 * NumberValueToken
 */
public class NumberValueToken extends JsonToken {

    /**
     * whether or not a float number
     */
    @Setter
    private boolean isFloat;

    public NumberValueToken(String content) {
        super(TokenType.NUMBER, content);
    }

    public NumberValueToken(String content, boolean isFloat) {
        super(TokenType.NUMBER, content);
        this.isFloat = isFloat;
    }

    public Number getNumber() {
        if (isFloat) {
            return Double.valueOf(content);
        } else {
            return Long.valueOf(content);
        }
    }
}