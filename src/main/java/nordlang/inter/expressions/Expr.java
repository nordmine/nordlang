package nordlang.inter.expressions;

import nordlang.inter.Node;
import nordlang.lexer.Token;
import nordlang.machine.Program;
import nordlang.lexer.Type;

public abstract class Expr extends Node {

    protected Token operand;
    protected Type type;

    public Expr(int line, Token operand, Type type) {
        super(line);
        this.operand = operand;
        this.type = type;
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

    public Type getType() {
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
