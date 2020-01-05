package cui.shibing.parser;

import cui.shibing.config.JsonConfig;
import cui.shibing.json.JsonArray;
import cui.shibing.json.JsonObject;
import cui.shibing.scanner.JsonTokenScanner;
import cui.shibing.token.BooleanValueToken;
import cui.shibing.token.JsonToken;
import cui.shibing.token.JsonToken.TokenType;
import cui.shibing.token.NumberValueToken;

import java.io.IOException;

/**
 * JsonParser
 */
public final class JsonParser {

    protected JsonTokenScanner scanner;

    protected JsonConfig config;

    public JsonParser(JsonTokenScanner scanner) {
        this(scanner,JsonConfig.getDefaultConfig());
    }

    public JsonParser(JsonTokenScanner scanner,JsonConfig config) {
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
                case LEFT_CURLY_BRACE:
                    scanner.rewindToken(token);
                    jsonObject.set(key, parseJsonObject());
                    break;
                case LEFT_BRACKET:
                    scanner.rewindToken(token);
                    jsonObject.set(key, parseJsonArray());
                    break;
                case STR:
                    jsonObject.set(key, token.getContent());
                    break;
                case NUMBER:
                    jsonObject.set(key, ((NumberValueToken) token).getNumber(config));
                    break;
                case BOOLEAN:
                    jsonObject.set(key, ((BooleanValueToken) token).getBoolean());
                    break;
                case NULL:
                    jsonObject.set(key, null);
                    break;
                default:
                    throw new RuntimeException("syntax error: invalid token " + token.getContent() + " line: " + scanner.getLine());
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
                    jsonArray.add(token.getContent());
                    break;
                case NUMBER:
                    jsonArray.add(((NumberValueToken) token).getNumber());
                    break;
                case BOOLEAN:
                    jsonArray.add(((BooleanValueToken) token).getBoolean());
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
                    throw new RuntimeException("syntax error: invalid token " + token.getContent() + " line: " + scanner.getLine());
            }
        } while ((token = scanner.nextToken()).getType() == TokenType.COMMA);
        matchTokenType(token, TokenType.RIGHT_BRACKET);
        return jsonArray;
    }

    private void matchTokenType(JsonToken token, TokenType type) {
        if (token == null) {
            throw new RuntimeException("syntax error: no more token" + " line: " + scanner.getLine());
        }
        if (token.getType() != type) {
            throw new RuntimeException("syntax error: " + token.getContent() + " line: " + scanner.getLine());
        }
    }

}