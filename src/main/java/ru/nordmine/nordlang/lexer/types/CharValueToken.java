package ru.nordmine.nordlang.lexer.types;

import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.value.CharValue;

public class CharValueToken extends ValueToken {

    private final char value;

    public CharValueToken(char value) {
        super(Tag.CHAR);
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("'%s'", value);
    }

    @Override
    public void gen(Program program) {
        program.addPushCommand(new CharValue(value));
    }
}
