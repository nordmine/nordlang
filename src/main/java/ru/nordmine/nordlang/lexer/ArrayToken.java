package ru.nordmine.nordlang.lexer;

import java.util.Objects;

public class ArrayToken extends TypeToken {

    public ArrayToken(TypeToken arrayType) {
        super("[]", Tag.INDEX, arrayType);
    }

    @Override
    public String toString() {
        return arrayType.toString() + "[]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayToken)) return false;
        if (!super.equals(o)) return false;
        ArrayToken that = (ArrayToken) o;
        return Objects.equals(arrayType, that.arrayType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), arrayType);
    }
}
