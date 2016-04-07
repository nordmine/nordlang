package nordlang.lexer.types;

import nordlang.lexer.Tag;
import nordlang.machine.Program;

public class Char extends ValueToken {

    private final char value;

    public Char(char value) {
        super(Tag.CHAR);
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("'%s'", value);
    }

    @Override
    public void gen(Program program) {
        program.addPushCommand(value);
    }
}
