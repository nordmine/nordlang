package ru.nordmine.nordlang.syntax.expressions.logical;

import ru.nordmine.nordlang.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;

public class Not extends Logical {

    public Not(int line, Token token, Expression rightExpr) throws SyntaxException {
        super(line, token, rightExpr, rightExpr);
    }

    @Override
    public void jumping(Program program, int trueLabel, int falseLabel) {
        rightExpr.jumping(program, falseLabel, trueLabel);
    }

    @Override
    public String toString() {
        return operand.toString() + " " + rightExpr.toString();
    }
}
