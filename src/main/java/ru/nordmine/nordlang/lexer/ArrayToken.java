package ru.nordmine.nordlang.lexer;

public class ArrayToken extends TypeToken {

    private final TypeToken arrayType;

    public ArrayToken(TypeToken arrayType) {
        super("[]", Tag.INDEX);
        this.arrayType = arrayType;
    }

    public TypeToken getArrayType() {
        return arrayType;
    }

    @Override
    public String toString() {
        return arrayType.toString() + "[]";
    }
}
