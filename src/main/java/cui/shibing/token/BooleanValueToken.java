package cui.shibing.token;

public class BooleanValueToken extends JsonToken {
    public BooleanValueToken(String content) {
        super(TokenType.BOOLEAN, content);
    }

    public boolean validBooleanValue() {
        return "true".equals(content) || "false".equals(content);
    }

    public Boolean getBoolean() {
        if (validBooleanValue()) {
            return Boolean.valueOf(content);
        } else {
            throw new RuntimeException("invalid boolean value: " + content);
        }

    }
}
