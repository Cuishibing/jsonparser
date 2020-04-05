package cui.shibing.scanner;

import cui.shibing.token.JsonToken;
import lombok.Getter;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * JsonTokenScanner
 */
public final class JsonTokenScanner {

    public static final int BUFFER_SIZE = 2048;

    protected CharBuffer buffer = CharBuffer.allocate(BUFFER_SIZE);

    protected Reader reader;

    protected Deque<JsonToken> tokenRewindBuffer = new ArrayDeque<>(1);

    @Getter
    protected int line = 1;

    public JsonTokenScanner(Reader reader) {
        this.reader = reader;
        buffer.flip();
    }

    private boolean isDigital(char c) {
        return c >= '0' && c <= '9';
    }

    private char escapeChar() throws IOException {
        Character c = readChar();
        switch (c) {
            case '"':
                return '"';
            case '\\':
                return '\\';
            case '/':
                return '/';
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            case 'u': {
                return readUnicodePoint();
            }
            default:
                throw new RuntimeException(String.format("line [%s]: syntax error, not support escape char [%s]", line, c));
        }
    }

    private int hexToInt(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        throw new RuntimeException(String.format("line [%s]: syntax error, invalid unicode point code [%s]", line, c));
    }

    private char readUnicodePoint() throws IOException {
        char c3 = readChar();
        char c2 = readChar();
        char c1 = readChar();
        char c0 = readChar();
        return (char) (hexToInt(c3) * 4096 + hexToInt(c2) * 256 + hexToInt(c1) * 16 + hexToInt(c0));
    }

    private JsonToken readNumberToken() throws IOException {
        int state = 0;// start state
        char readChar;
        StringBuilder builder = new StringBuilder();
        while (true) {
            switch (state) {
                case 0: {
                    readChar = readChar();
                    if (readChar == '-') {
                        builder.append(readChar);
                        state = 1;
                        break;
                    }
                    if (isDigital(readChar)) {
                        builder.append(readChar);
                        state = 2;
                        break;
                    }
                    throw new RuntimeException(String.format("line [%s]: syntax error, invalid number char [%s]", line, readChar));
                }
                case 1: {
                    readChar = readChar();
                    if (isDigital(readChar)) {
                        builder.append(readChar);
                        state = 2;
                        break;
                    }
                    throw new RuntimeException(String.format("line [%s]: syntax error, invalid number char [%s]", line, readChar));
                }
                case 2: {
                    readChar = readChar();
                    if (isDigital(readChar)) {
                        builder.append(readChar);
                        state = 2;
                        break;
                    }
                    if (readChar == '.') {
                        builder.append(readChar);
                        state = 3;
                        break;
                    }
                    buffer.position(buffer.position() - 1);
                    return new JsonToken(JsonToken.TokenType.NUMBER, builder.toString());
                }
                case 3: {
                    readChar = readChar();
                    if (isDigital(readChar)) {
                        builder.append(readChar);
                        state = 3;
                        break;
                    }
                    if (readChar == 'e' || readChar == 'E') {
                        builder.append(readChar);
                        state = 4;
                        break;
                    }
                    buffer.position(buffer.position() - 1);
                    return new JsonToken(JsonToken.TokenType.NUMBER, builder.toString());
                }
                case 4: {
                    readChar = readChar();
                    if (readChar == '+' || readChar == '-') {
                        builder.append(readChar);
                        state = 5;
                        break;
                    }
                    if (isDigital(readChar)) {
                        builder.append(readChar);
                        state = 6;
                        break;
                    }
                    throw new RuntimeException(String.format("line [%s]: syntax error, invalid number char [%s]", line, readChar));
                }
                case 5: {
                    readChar = readChar();
                    if (isDigital(readChar)) {
                        builder.append(readChar);
                        state = 6;
                        break;
                    }
                    throw new RuntimeException(String.format("line [%s]: syntax error, invalid number char [%s]", line, readChar));
                }
                case 6: {
                    readChar = readChar();
                    if (isDigital(readChar)) {
                        builder.append(readChar);
                        state = 6;
                        break;
                    }
                    buffer.position(buffer.position() - 1);
                    return new JsonToken(JsonToken.TokenType.NUMBER, builder.toString());
                }
            }
        }
    }

    public void rewindToken(JsonToken token) {
        tokenRewindBuffer.push(token);
    }

    public JsonToken nextToken(JsonToken.TokenType type) throws IOException {
        JsonToken token = nextToken();
        if (token == null) {
            throw new RuntimeException(String.format("line [%s]: syntax error, expect token [%s] but no more token",
                    line, type.name()));
        }
        if (token.getType() != type) {
            throw new RuntimeException(String.format("line [%s]: syntax error, expect token [%s] but [%s]",
                    line, type.name(), token.getType().name()));
        }
        return token;
    }

    public JsonToken nextToken() throws IOException {
        if (tokenRewindBuffer.size() > 0) {
            return tokenRewindBuffer.pop();
        }

        char readChar;

        // skip space \t \r \n
        do {
            readChar = readChar();
            if (readChar == '\n') {
                line++;
            }
        } while (readChar == ' ' || readChar == '\t' || readChar == '\r' || readChar == '\n');

        switch (readChar) {
            case '{':
                return JsonToken.LEFT_CURLY_BRACE;
            case '}':
                return JsonToken.RIGHT_CURLY_BRACE;
            case '[':
                return JsonToken.LEFT_BRACKET;
            case ']':
                return JsonToken.RIGHT_BRACKET;
            case ':':
                return JsonToken.COLON;
            case ',':
                return JsonToken.COMMA;
            case '"': {
                StringBuilder builder = new StringBuilder();
                do {
                    readChar = readChar();
                    if (readChar == '\\') {
                        builder.append(escapeChar());
                    } else if (readChar != '"') {
                        builder.append(readChar);
                    }
                } while (readChar != '"');
                return new JsonToken(JsonToken.TokenType.STR, builder.toString());
            }
            default: {
                switch (readChar) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    case '-': {
                        buffer.position(buffer.position() - 1);
                        return readNumberToken();
                    }
                    case 't':
                    case 'f': {
                        int counter = readChar == 't' ? 3 : 4;
                        StringBuilder builder = new StringBuilder();
                        builder.append(readChar);
                        while (counter-- > 0) {
                            readChar = readChar();
                            builder.append(readChar);
                        }
                        String s = builder.toString();
                        if ("true".equals(s) || "false".equals(s)) {
                            return new JsonToken(JsonToken.TokenType.BOOLEAN, builder.toString());
                        } else {
                            throw new RuntimeException(String.format("line [%s]: syntax error, invalid boolean value [%s]",
                                    line, s));
                        }
                    }
                    case 'n': {
                        StringBuilder builder = new StringBuilder();
                        builder.append(readChar);
                        int counter = 3;
                        while (counter-- > 0) {
                            readChar = readChar();
                            builder.append(readChar);
                        }
                        if (JsonToken.NULL.getContent().equals(builder.toString())) {
                            return JsonToken.NULL;
                        } else {
                            throw new RuntimeException(String.format("line [%s]: syntax error, invalid null value [%s]",
                                    line, builder.toString()));
                        }
                    }
                }
                throw new RuntimeException(String.format("line [%s]: syntax error, invalid char [%s]",
                        line, readChar));
            }
        }

    }

    private Character readChar() throws IOException {
        if (buffer.hasRemaining()) {
            return buffer.get();
        }

        buffer.clear();
        int read = reader.read(buffer);
        buffer.flip();
        if (read < 0) {
            throw new RuntimeException();
        } else {
            return buffer.get();
        }
    }

}