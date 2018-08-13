package ru.nordmine.nordlang.syntax.expressions;

import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;

public abstract class Expression {

    protected Token operand;
    protected TypeToken type;

    public Expression(Token operand, TypeToken type) {
        this.operand = operand;
        this.type = type;
    }

    public int getLine() {
        return operand.getLine();
    }

    public abstract void gen(Program program);

    public void jumping(Program program, Label trueLabel, Label falseLabel) {
        emitJumps(program, trueLabel, falseLabel);
    }

    protected void emitJumps(Program program, Label trueLabel, Label falseLabel) {
        if (trueLabel != Label.EMPTY && falseLabel != Label.EMPTY) {
            program.addIfCommand(trueLabel);
            program.addGotoCommand(falseLabel);
        } else if (trueLabel != Label.EMPTY) {
            program.addIfCommand(trueLabel);
        } else if (falseLabel != Label.EMPTY) {
            program.addIfFalseCommand(falseLabel);
        }
    }

    public TypeToken getType() {
        return type;
    }

    @Override
    public String toString() {
        return operand.toString();
    }
}
