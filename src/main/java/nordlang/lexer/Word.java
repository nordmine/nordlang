package nordlang.lexer;

public class Word extends Token {

    public static final Word AND = new Word(Tag.AND, "and");
    public static final Word OR = new Word(Tag.OR, "or");
    public static final Word EQUAL = new Word(Tag.EQUAL, "==");
    public static final Word NOT_EQUAL = new Word(Tag.NOT_EQUAL, "<>");
    public static final Word LESS_OR_EQUAL = new Word(Tag.LESS_OR_EQUAL, "<=");
    public static final Word GREATER_OR_EQUAL = new Word(Tag.GREATER_OR_EQUAL, ">=");
    public static final Word UNARY_MINUS = new Word(Tag.UNARY_MINUS, "minus");
    public static final Word TRUE = new Word(Tag.TRUE, "true");
    public static final Word FALSE = new Word(Tag.FALSE, "false");
    public static final Word ECHO = new Word(Tag.ECHO, "echo");

    private final String lexeme;

    public Word(Tag tag, String lexeme) {
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
