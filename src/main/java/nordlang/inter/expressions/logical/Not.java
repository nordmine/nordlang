package nordlang.inter.expressions.logical;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.lexer.Token;
import nordlang.machine.Program;

public class Not extends Logical {

    public Not(int line, Token token, Expr rightExpr) throws ParserException {
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
