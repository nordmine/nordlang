package ru.nordmine.nordlang.lexer.types;

import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.value.IntValue;

public class IntValueToken extends ValueToken {

    private final int value;

    public IntValueToken(int value) {
        super(Tag.INT);
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public void gen(Program program) {
        program.addPushCommand(new IntValue(value));
    }
}
