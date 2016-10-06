package ru.nordmine.nordlang.syntax.expressions;

import ru.nordmine.nordlang.lexer.types.IntValueToken;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.lexer.types.ValueToken;
import ru.nordmine.nordlang.lexer.WordToken;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.lexer.TypeToken;

public class Constant extends Expression {

    public Constant(int line, Token token, TypeToken type) {
        super(line, token, type);
    }

    public Constant(int line, int i) {
        super(line, new IntValueToken(i), TypeToken.INT);
    }

    public static final Constant TRUE = new Constant(0, WordToken.TRUE, TypeToken.BOOL);
    public static final Constant FALSE = new Constant(0, WordToken.FALSE, TypeToken.BOOL);

    @Override
    public void jumping(Program program, int trueLabel, int falseLabel) {
        if (this == TRUE && trueLabel != 0) {
            program.addGotoCommand(trueLabel);
        } else if (this == FALSE && falseLabel != 0) {
            program.addGotoCommand(falseLabel);
        }
    }

    @Override
    public void gen(Program program) {
        if (type == TypeToken.INT || type == TypeToken.CHAR) {
            ((ValueToken)operand).gen(program);
        } else if (this == TRUE) {
            program.addPushCommand(1);
        } else if (this == FALSE) {
            program.addPushCommand(0);
        }
    }

    @Override
    public String toString() {
        return "#" + operand.toString();
    }
}
