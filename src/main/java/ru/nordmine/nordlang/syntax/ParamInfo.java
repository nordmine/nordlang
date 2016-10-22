package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.lexer.WordToken;

public class ParamInfo {

    private final TypeToken type;
    private final WordToken id;

    public ParamInfo(TypeToken type, WordToken id) {
        this.type = type;
        this.id = id;
    }

    public TypeToken getType() {
        return type;
    }

    public WordToken getId() {
        return id;
    }

    @Override
    public String toString() {
        return type + " " + id;
    }
}
