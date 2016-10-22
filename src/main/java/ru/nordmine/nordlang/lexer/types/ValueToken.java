package ru.nordmine.nordlang.lexer.types;

import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;

public abstract class ValueToken extends Token {

    public ValueToken(Tag tag) {
        super(tag);
    }

    public abstract void gen(Program program);
}
