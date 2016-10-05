package ru.nordmine.nordlang.lexer.types;

import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.machine.Program;

public class Int extends ValueToken {

    private final int value;

    public Int(int value) {
        super(Tag.INT);
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }


    public int getIntValue() {
        return value;
    }

    @Override
    public void gen(Program program) {
        program.addPushCommand(value);
    }
}
