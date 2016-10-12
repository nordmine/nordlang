package ru.nordmine.nordlang.lexer;

public class TypeToken extends WordToken {

    public TypeToken(String lexeme, Tag tag) {
        super(tag, lexeme);
    }

    public static final TypeToken INT = new TypeToken("int", Tag.BASIC);
    public static final TypeToken CHAR = new TypeToken("char", Tag.BASIC);
    public static final TypeToken BOOL = new TypeToken("bool", Tag.BASIC);
    public static final TypeToken STRING = new TypeToken("string", Tag.BASIC);
}
