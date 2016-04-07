package nordlang.lexer;

import nordlang.exceptions.ParserException;

public class Array extends Type {

    private Type arrayType;
    private int size = -1;

    public Array(int size, Type arrayType) {
        super("[]", Tag.INDEX, size * arrayType.width);
        this.size = size;
        this.arrayType = arrayType;
    }

    public Type getArrayType() {
        return arrayType;
    }

    public void setSize(int size) throws ParserException {
        if (this.size == -1) {
            this.size = size;
            this.width = size * arrayType.width;
        } else {
            if (this.size < size) {
                throw new ParserException("Wrong array size. Expected size: " + this.size);
            }
        }
    }

    @Override
    public String toString() {
        return "[" + size + "] " + arrayType.toString();
    }
}
