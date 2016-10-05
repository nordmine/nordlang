package nordlang.syntax.expressions.logical;

import nordlang.exceptions.SyntaxException;
import nordlang.syntax.expressions.Expression;
import nordlang.lexer.Token;
import nordlang.machine.Program;

public class And extends Logical {

    public And(int line, Token token, Expression left, Expression right) throws SyntaxException {
        super(line, token, left, right);
    }

    @Override
    public void jumping(Program program, int trueLabel, int falseLabel) {
        int label = falseLabel != 0 ? falseLabel : program.newLabel();
        leftExpr.jumping(program, 0, label);
        rightExpr.jumping(program, trueLabel, falseLabel);
        if (falseLabel == 0) {
            program.fixLabel(label);
        }
    }
}
