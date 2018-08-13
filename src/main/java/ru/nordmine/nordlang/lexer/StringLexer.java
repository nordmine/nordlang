package ru.nordmine.nordlang.lexer;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.lexer.types.CharValueToken;
import ru.nordmine.nordlang.lexer.types.StringValueToken;
import ru.nordmine.nordlang.lexer.types.IntValueToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringLexer implements Lexer {

    private Map<String, WordToken> words = new HashMap<>();

    private char peek = ' ';
    private int nextPosition = 0;
    private final char[] source;
    private int line = 1;

    @Override
    public int getLine() {
        return line;
    }

    public StringLexer(String source) {
        reserve(WordToken.AND);
        reserve(WordToken.OR);
        reserve(WordToken.IF);
        reserve(WordToken.ELSE);
        reserve(WordToken.WHILE);
        reserve(WordToken.DO);
        reserve(WordToken.BREAK);
        reserve(WordToken.ECHO);
        reserve(WordToken.TRUE);
        reserve(WordToken.FALSE);
        reserve(WordToken.RETURN);
        reserve(WordToken.SIZE);
        reserve(WordToken.CONST);

        reserve(TypeToken.BOOL);
        reserve(TypeToken.INT);
        reserve(TypeToken.CHAR);
        reserve(TypeToken.STRING);
        this.source = source.toCharArray();
    }

    private void reserve(WordToken word) {
        words.put(word.getLexeme(), word);
    }

    private boolean hasNext() {
        return nextPosition < source.length;
    }

    private void nextChar() {
        peek = source[nextPosition];
        nextPosition++;
    }

    private boolean nextIs(char expected) {
        return hasNext() && source[nextPosition] == expected;
    }

    private boolean nextIsNot(char expected) {
        return hasNext() && source[nextPosition] != expected;
    }

    private boolean nextIsDigit() {
        return hasNext() && Character.isDigit(source[nextPosition]);
    }

    private boolean nextIsLetterOrDigit() {
        return hasNext() && Character.isLetterOrDigit(source[nextPosition]);
    }

    @Override
    public Token nextToken() throws SyntaxException {
        boolean skipAll = false;
        while (hasNext()) {
            nextChar();
            if (peek == '\n') {
                line++;
                skipAll = false;
                continue;
            } else if (peek == ' ' || peek == '\t' || peek == '\r' || skipAll) {
                continue;
            }

            if (Character.isDigit(peek)) {
                StringBuilder sb = new StringBuilder();
                sb.append(peek);
                while (nextIsDigit()) {
                    nextChar();
                    sb.append(peek);
                }
                return new IntValueToken(Integer.parseInt(sb.toString()), line);
            }
            if (Character.isLetter(peek)) {
                StringBuilder sb = new StringBuilder();
                sb.append(peek);
                while (nextIsLetterOrDigit()) {
                    nextChar();
                    sb.append(peek);
                }
                String s = sb.toString();
                WordToken w = words.get(s);
                if (w != null) {
                    return w;
                }
                w = new WordToken(Tag.ID, s, line);
                words.put(s, w);
                return w;
            }
            switch (peek) {
                case '=':
                    if (nextIs('=')) {
                        nextChar();
                        return WordToken.EQUAL;
                    } else {
                        return new Token(Tag.ASSIGN, line);
                    }
                case '<':
                    if (nextIs('>')) {
                        nextChar();
                        return WordToken.NOT_EQUAL;
                    } else if (nextIs('=')) {
                        nextChar();
                        return WordToken.LESS_OR_EQUAL;
                    } else {
                        return new Token(Tag.LESS, line);
                    }
                case '>':
                    if (nextIs('=')) {
                        nextChar();
                        return WordToken.GREATER_OR_EQUAL;
                    } else {
                        return new Token(Tag.GREATER, line);
                    }
                case '+':
                    if (nextIs('+')) {
                        nextChar();
                        return WordToken.INCREMENT;
                    }
                    return new Token(Tag.PLUS, line);
                case '-':
                    if (nextIs('-')) {
                        nextChar();
                        return WordToken.DECREMENT;
                    }
                    return new Token(Tag.MINUS, line);
                case '*':
                    return new Token(Tag.MUL, line);
                case '/':
                    if (nextIs('/')) {
                        nextChar();
                        skipAll = true;
                    } else {
                        return new Token(Tag.DIVISION, line);
                    }
                    break;
                case '%':
                    return new Token(Tag.MOD, line);
                case '!':
                    return new Token(Tag.NOT, line);
                case '{':
                    return new Token(Tag.BEGIN_BLOCK, line);
                case '}':
                    return new Token(Tag.END_BLOCK, line);
                case '(':
                    return new Token(Tag.OPEN_BRACKET, line);
                case ')':
                    return new Token(Tag.CLOSE_BRACKET, line);
                case '[':
                    return new Token(Tag.OPEN_SQUARE, line);
                case ']':
                    return new Token(Tag.CLOSE_SQUARE, line);
                case ';':
                    return new Token(Tag.SEMICOLON, line);
                case ',':
                    return new Token(Tag.COMMA, line);
                case '"':
                    StringBuilder sb = new StringBuilder();
                    while (nextIsNot('"')) {
                        nextChar();
                        sb.append(peek);
                    }
                    if (hasNext()) {
                        nextChar();
                    }
                    if (peek != '"') {
                        throw new SyntaxException(line, "unexpected end of string literal");
                    }
                    return new StringValueToken(sb, line);
                case '\'':
                    nextChar();
                    if (nextIs('\'')) {
                        Token token = new CharValueToken(peek, line);
                        nextChar();
                        return token;
                    } else {
                        throw new SyntaxException(line, "char literal contains more than 1 symbol");
                    }
                default:
                    throw new SyntaxException(line, "unknown char: *" + peek + "*");
            }
        }
        return new Token(Tag.END_OF_FILE, line);
    }

    @Override
    public List<Token> getAllTokens() throws SyntaxException {
        Token token;
        List<Token> tokens = new ArrayList<>();
        do {
            token = nextToken();
            tokens.add(token);
        } while (token.getTag() != Tag.END_OF_FILE);
        return tokens;
    }
}
