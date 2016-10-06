package ru.nordmine.nordlang.lexer;

public class TypeToken extends WordToken {

    protected int width = 0; // todo удалить, т.к. int лежит в основе всех примитивов

    public TypeToken(String lexeme, Tag tag, int width) {
        super(tag, lexeme);
        this.width = width;
    }

    public static final TypeToken INT = new TypeToken("int", Tag.BASIC, 1);
    public static final TypeToken CHAR = new TypeToken("char", Tag.BASIC, 1);
    public static final TypeToken BOOL = new TypeToken("bool", Tag.BASIC, 1);

    public int getWidth() {
        return width;
    }
}
