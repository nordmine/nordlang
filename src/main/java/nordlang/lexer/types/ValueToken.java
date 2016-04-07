package nordlang.lexer.types;

import nordlang.lexer.Tag;
import nordlang.lexer.Token;
import nordlang.machine.Program;

public abstract class ValueToken extends Token {

    public ValueToken(Tag tag) {
        super(tag);
    }

    public abstract void gen(Program program);
}
