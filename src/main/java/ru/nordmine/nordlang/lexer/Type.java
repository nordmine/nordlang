package ru.nordmine.nordlang.lexer;

public class Type extends Word {

    protected int width = 0; // todo удалить, т.к. int лежит в основе всех примитивов

    public Type(String lexeme, Tag tag, int width) {
        super(tag, lexeme);
        this.width = width;
    }

    public static final Type INT = new Type("int", Tag.BASIC, 1);
    public static final Type CHAR = new Type("char", Tag.BASIC, 1);
    public static final Type BOOL = new Type("bool", Tag.BASIC, 1);

    public int getWidth() {
        return width;
    }
}
