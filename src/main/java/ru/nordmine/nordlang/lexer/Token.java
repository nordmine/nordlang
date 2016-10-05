package ru.nordmine.nordlang.lexer;

public class Token {

    private static int uniqueIndexSequence = 2000;

    private final Tag tag;

    // виртуальная машина оперирует индексами переменных вместо имён
    private final int uniqueIndex; // todo возможно, перенести в другое место?

    public Token(Tag tag) {
        this.tag = tag;
        this.uniqueIndex = uniqueIndexSequence;
        uniqueIndexSequence++;
    }

    public Tag getTag() {
        return tag;
    }

    public int getUniqueIndex() {
        return uniqueIndex;
    }

    @Override
    public String toString() {
        return tag.toString();
    }
}
