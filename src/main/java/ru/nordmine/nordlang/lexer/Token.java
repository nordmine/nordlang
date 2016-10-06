package ru.nordmine.nordlang.lexer;

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
}
