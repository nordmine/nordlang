package nordlang.syntax.expressions.logical;

import nordlang.exceptions.SyntaxException;
import nordlang.syntax.expressions.Expression;
import nordlang.lexer.Token;
import nordlang.machine.Program;

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
