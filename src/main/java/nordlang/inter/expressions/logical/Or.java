package nordlang.inter.expressions.logical;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.lexer.Token;
import nordlang.machine.Program;

public class Or extends Logical {

    public Or(int line, Token token, Expr left, Expr right) throws ParserException {
        super(line, token, left, right);
    }

    @Override
    public void jumping(Program program, int trueLabel, int falseLabel) {
        int label = trueLabel != 0 ? trueLabel : program.newLabel();
        leftExpr.jumping(program, label, 0);
        rightExpr.jumping(program, trueLabel, falseLabel);
        if (trueLabel == 0) {
            program.fixLabel(label);
        }
    }
}
