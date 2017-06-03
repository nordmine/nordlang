package ru.nordmine.nordlang.lexer;

public class TypeToken extends WordToken {

    protected final TypeToken arrayType;

    public TypeToken(String lexeme, Tag tag, TypeToken arrayType) {
        super(tag, lexeme);
        this.arrayType = arrayType;
    }

    public static final TypeToken INT = new TypeToken("int", Tag.BASIC, null);
    public static final TypeToken CHAR = new TypeToken("char", Tag.BASIC, null);
    public static final TypeToken BOOL = new TypeToken("bool", Tag.BASIC, null);
    public static final TypeToken STRING = new TypeToken("string", Tag.BASIC, TypeToken.CHAR);

    public TypeToken getArrayType() {
        return arrayType;
    }
}
