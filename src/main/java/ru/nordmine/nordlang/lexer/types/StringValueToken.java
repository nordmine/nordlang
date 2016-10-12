package ru.nordmine.nordlang.lexer.types;

import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.value.StringValue;

public class StringValueToken extends ValueToken {

    private final StringBuilder value;

    public StringValueToken(StringBuilder value) {
        super(Tag.STRING);
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public StringBuilder getValue() {
        return value;
    }

    @Override
    public void gen(Program program) {
        program.addPushCommand(new StringValue(value));
    }
}
