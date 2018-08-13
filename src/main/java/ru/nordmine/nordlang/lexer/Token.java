package ru.nordmine.nordlang.lexer;

import java.util.Objects;

public class Token {

    private final Tag tag;
    private final int line;

    public Token(Tag tag, int line) {
        this.tag = tag;
        this.line = line;
    }

    public Tag getTag() {
        return tag;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return tag.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return tag == token.tag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}
