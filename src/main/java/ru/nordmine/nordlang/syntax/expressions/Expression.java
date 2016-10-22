package ru.nordmine.nordlang.syntax.expressions;

import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.machine.Program;

public abstract class Expression {

    protected Token operand;
    protected TypeToken type;

    private final int line;

    public Expression(int line, Token operand, TypeToken type) {
        this.line = line;
        this.operand = operand;
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public abstract void gen(Program program);

    public void jumping(Program program, int trueLabel, int falseLabel) {
        emitJumps(program, trueLabel, falseLabel);
    }

    protected void emitJumps(Program program, int trueLabel, int falseLabel) {
        if (trueLabel != 0 && falseLabel != 0) {
            program.addIfCommand(trueLabel);
            program.addGotoCommand(falseLabel);
        } else if (trueLabel != 0) {
            program.addIfCommand(trueLabel);
        } else if (falseLabel != 0) {
            program.addIfFalseCommand(falseLabel);
        }
    }

    public TypeToken getType() {
        return type;
    }

    public Token getOperand() {
        return operand;
    }

    @Override
    public String toString() {
        return operand.toString();
    }
}
