package ru.nordmine.nordlang.lexer;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.lexer.types.CharValueToken;
import ru.nordmine.nordlang.lexer.types.StringValueToken;
import ru.nordmine.nordlang.lexer.types.IntValueToken;

import java.util.HashMap;
import java.util.Map;

public class Lexer {

    private Map<String, WordToken> words = new HashMap<>();

    private char peek = ' ';
    private int nextPosition = 0;
    private final char[] source;
    private int line = 1;

    public int getLine() {
        return line;
    }

    public Lexer(String source) {
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

    public Token nextToken() throws SyntaxException {
        boolean skipAll = false;
        while (hasNext()) {
            nextChar();
            if (peek == '\n') {
                line++;
                skipAll = false;
                continue;
            } else if (peek == ' ' || peek == '\t' || skipAll) {
                continue;
            }

            if (Character.isDigit(peek)) {
                StringBuilder sb = new StringBuilder();
                sb.append(peek);
                while (nextIsDigit()) {
                    nextChar();
                    sb.append(peek);
                }
                return new IntValueToken(Integer.parseInt(sb.toString()));
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
                w = new WordToken(Tag.ID, s);
                words.put(s, w);
                return w;
            }
            switch (peek) {
                case '=':
                    if (nextIs('=')) {
                        nextChar();
                        return WordToken.EQUAL;
                    } else {
                        return new Token(Tag.ASSIGN);
                    }
                case '<':
                    if (nextIs('>')) {
                        nextChar();
                        return WordToken.NOT_EQUAL;
                    } else if (nextIs('=')) {
                        nextChar();
                        return WordToken.LESS_OR_EQUAL;
                    } else {
                        return new Token(Tag.LESS);
                    }
                case '>':
                    if (nextIs('=')) {
                        nextChar();
                        return WordToken.GREATER_OR_EQUAL;
                    } else {
                        return new Token(Tag.GREATER);
                    }
                case '+':
                    return new Token(Tag.PLUS);
                case '-':
                    return new Token(Tag.MINUS);
                case '*':
                    return new Token(Tag.MUL);
                case '/':
                    if (nextIs('/')) {
                        nextChar();
                        skipAll = true;
                    } else {
                        return new Token(Tag.DIVISION);
                    }
                    break;
                case '%':
                    return new Token(Tag.MOD);
                case '!':
                    return new Token(Tag.NOT);
                case '{':
                    return new Token(Tag.BEGIN_BLOCK);
                case '}':
                    return new Token(Tag.END_BLOCK);
                case '(':
                    return new Token(Tag.OPEN_BRACKET);
                case ')':
                    return new Token(Tag.CLOSE_BRACKET);
                case '[':
                    return new Token(Tag.OPEN_SQUARE);
                case ']':
                    return new Token(Tag.CLOSE_SQUARE);
                case ';':
                    return new Token(Tag.SEMICOLON);
                case ',':
                    return new Token(Tag.COMMA);
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
                    return new StringValueToken(sb);
                case '\'':
                    nextChar();
                    if (nextIs('\'')) {
                        Token token = new CharValueToken(peek);
                        nextChar();
                        return token;
                    } else {
                        throw new SyntaxException(line, "char literal contains more than 1 symbol");
                    }
                default:
                    throw new SyntaxException(line, "unknown char: *" + peek + "*");
            }
        }
        return new Token(Tag.END_OF_FILE);
    }
}
