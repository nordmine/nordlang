package ru.nordmine.nordlang.lexer;

public class WordToken extends Token {

    public static final WordToken AND = new WordToken(Tag.AND, "and");
    public static final WordToken OR = new WordToken(Tag.OR, "or");
    public static final WordToken EQUAL = new WordToken(Tag.EQUAL, "==");
    public static final WordToken NOT_EQUAL = new WordToken(Tag.NOT_EQUAL, "<>");
    public static final WordToken LESS_OR_EQUAL = new WordToken(Tag.LESS_OR_EQUAL, "<=");
    public static final WordToken GREATER_OR_EQUAL = new WordToken(Tag.GREATER_OR_EQUAL, ">=");
    public static final WordToken UNARY_MINUS = new WordToken(Tag.UNARY_MINUS, "minus");
    public static final WordToken TRUE = new WordToken(Tag.TRUE, "true");
    public static final WordToken FALSE = new WordToken(Tag.FALSE, "false");
    public static final WordToken ECHO = new WordToken(Tag.ECHO, "echo");
    public static final WordToken IF = new WordToken(Tag.IF, "if");
    public static final WordToken ELSE = new WordToken(Tag.ELSE, "else");
    public static final WordToken WHILE = new WordToken(Tag.WHILE, "while");
    public static final WordToken DO = new WordToken(Tag.DO, "do");
    public static final WordToken BREAK = new WordToken(Tag.BREAK, "break");

    // для переменных здесь содержится её имя
    private final String lexeme;

    public WordToken(Tag tag, String lexeme) {
        super(tag);
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return lexeme;
    }
}
