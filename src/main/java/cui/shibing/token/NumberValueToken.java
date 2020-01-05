package cui.shibing.token;

import cui.shibing.config.JsonConfig;
import lombok.Setter;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.math.BigDecimal;

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
        return getNumber(JsonConfig.getDefaultConfig());
    }

    public Number getNumber(JsonConfig config) {
        if (isFloat) {
            if (config.isPreferBigDecimal()) {
                return new BigDecimal(content);
            }
            return Double.valueOf(content);
        } else {
            return Long.valueOf(content);
        }
    }
}