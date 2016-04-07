package nordlang.inter.expressions.logical;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.lexer.Token;
import nordlang.machine.Program;

public class And extends Logical {

    public And(int line, Token token, Expr left, Expr right) throws ParserException {
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
