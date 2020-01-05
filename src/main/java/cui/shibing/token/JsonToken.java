package cui.shibing.token;

import lombok.Getter;

/**
 * JsonToken
 */
public class JsonToken {

    public static enum TokenType {
        LEFT_CURLY_BRACE, // {
        RIGHT_CURLY_BRACE, // }
        LEFT_BRACKET, // [
        RIGHT_BRACKET, // ]
        COLON, // :
        COMMA, // ,
        STR, NUMBER,BOOLEAN,NULL
    }

    public static final JsonToken LEFT_CURLY_BRACE = new JsonToken(TokenType.LEFT_CURLY_BRACE, "{");
    public static final JsonToken RIGHT_CURLY_BRACE = new JsonToken(TokenType.RIGHT_CURLY_BRACE,"}");
    public static final JsonToken LEFT_BRACKET = new JsonToken(TokenType.LEFT_BRACKET, "[");
    public static final JsonToken RIGHT_BRACKET = new JsonToken(TokenType.RIGHT_BRACKET, "]");
    public static final JsonToken COLON = new JsonToken(TokenType.COLON,":");
    public static final JsonToken COMMA = new JsonToken(TokenType.COMMA,",");
    public static final JsonToken NULL = new JsonToken(TokenType.NULL,"null");

    @Getter
    protected TokenType type;

    @Getter
    protected String content;

    public JsonToken(TokenType type, String content) {
        this.content = content;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("(token=%s,content=%s)", type.name(),content);
    }
}