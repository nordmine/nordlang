package ru.nordmine.nordlang.lexer;

import java.util.Objects;

public class Token {

    private final Tag tag;

    public Token(Tag tag) {
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
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
