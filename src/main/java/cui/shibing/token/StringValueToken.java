package cui.shibing.token;

/**
 * StringValueToken
 */
public class StringValueToken extends JsonToken {

    public StringValueToken(String content) {
        super(TokenType.STR,content);
    }

}