package cui.shibing.parser;

import cui.shibing.config.JsonConfig;
import cui.shibing.intercepter.Interceptor;
import cui.shibing.json.JsonArray;
import cui.shibing.json.JsonObject;
import cui.shibing.scanner.JsonTokenScanner;
import cui.shibing.token.JsonToken;
import cui.shibing.token.JsonToken.TokenType;

import java.io.IOException;

/**
 * JsonParser
 */
public final class JsonParser {

    private JsonTokenScanner scanner;

    private JsonConfig config;

    public JsonParser(JsonTokenScanner scanner, JsonConfig config) {
        this.scanner = scanner;
        this.config = config;
    }

    public JsonObject parseJsonObject() throws IOException {
        JsonToken token = scanner.nextToken(TokenType.LEFT_CURLY_BRACE);
        JsonObject jsonObject = new JsonObject();
        do {
            token = scanner.nextToken();

            if (token.getType() == TokenType.RIGHT_CURLY_BRACE) {
                return jsonObject;
            }

            matchTokenType(token, TokenType.STR);
            String key = token.getContent();
            scanner.nextToken(TokenType.COLON);
            token = scanner.nextToken();
            switch (token.getType()) {
                case STR:
                case NUMBER:
                case BOOLEAN:
                    jsonObject.set(key, getTokenValue(token));
                    break;
                case NULL:
                    jsonObject.set(key, null);
                    break;
                case LEFT_CURLY_BRACE:
                    scanner.rewindToken(token);
                    jsonObject.set(key, parseJsonObject());
                    break;
                case LEFT_BRACKET:
                    scanner.rewindToken(token);
                    jsonObject.set(key, parseJsonArray());
                    break;
                default:
                    throw new RuntimeException(String.format("line [%s]: syntax error, invalid token type [%s]",
                            scanner.getLine(), token.getType()));
            }
        } while ((token = scanner.nextToken()).getType() == TokenType.COMMA);

        matchTokenType(token, TokenType.RIGHT_CURLY_BRACE);
        return jsonObject;
    }

    public JsonArray parseJsonArray() throws IOException {
        JsonToken token = scanner.nextToken(TokenType.LEFT_BRACKET);
        JsonArray jsonArray = new JsonArray();
        do {
            token = scanner.nextToken();

            switch (token.getType()) {
                case STR:
                case NUMBER:
                case BOOLEAN:
                    jsonArray.add(getTokenValue(token));
                    break;
                case NULL:
                    jsonArray.add(null);
                    break;
                case LEFT_CURLY_BRACE:
                    scanner.rewindToken(token);
                    jsonArray.add(parseJsonObject());
                    break;
                case LEFT_BRACKET:
                    scanner.rewindToken(token);
                    jsonArray.add(parseJsonArray());
                    break;
                case RIGHT_BRACKET:
                    return jsonArray;
                default:
                    throw new RuntimeException(String.format("line [%s]: syntax error, invalid token type [%s]",
                            scanner.getLine(), token.getType()));
            }
        } while ((token = scanner.nextToken()).getType() == TokenType.COMMA);
        matchTokenType(token, TokenType.RIGHT_BRACKET);
        return jsonArray;
    }

    /**
     * invoke token interceptor
     * <p>
     * if interceptor is null for one token type then get the interceptor from the default config.
     */
    private Object getTokenValue(JsonToken token) {
        Interceptor<?> interceptor = config.getInterceptor(token.getType());
        if (interceptor == null) {
            interceptor = JsonConfig.getDefaultConfig().getInterceptor(token.getType());
        }
        return interceptor.onToken(token, config);
    }

    /**
     * test given json token' type whether or not match the expect,if not then throw a exception.
     */
    private void matchTokenType(JsonToken token, TokenType type) {
        if (token == null) {
            throw new RuntimeException(String.format("line [%s]: syntax error, expect token [%s] but no more token",
                    scanner.getLine(), type.name()));
        }
        if (token.getType() != type) {
            throw new RuntimeException(String.format("line [%s]: syntax error, expect token [%s] but [%s]",
                    scanner.getLine(), type.name(), token.getType().name()));
        }
    }

}