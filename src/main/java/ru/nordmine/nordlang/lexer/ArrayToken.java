package ru.nordmine.nordlang.lexer;

import ru.nordmine.nordlang.exceptions.SyntaxException;

public class ArrayToken extends TypeToken {

    private TypeToken arrayType;
    private int size = -1;

    public ArrayToken(int size, TypeToken arrayType) {
        super("[]", Tag.INDEX, size * arrayType.width);
        this.size = size;
        this.arrayType = arrayType;
    }

    public TypeToken getArrayType() {
        return arrayType;
    }

    public void setSize(int size) throws SyntaxException {
        if (this.size == -1) {
            this.size = size;
            this.width = size * arrayType.width;
        } else {
            if (this.size < size) {
                throw new SyntaxException("Wrong array size. Expected size: " + this.size);
            }
        }
    }

    @Override
    public String toString() {
        return "[" + size + "] " + arrayType.toString();
    }
}
