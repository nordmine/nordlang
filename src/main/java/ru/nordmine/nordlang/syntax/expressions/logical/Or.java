package ru.nordmine.nordlang.syntax.expressions.logical;

import ru.nordmine.nordlang.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;

public class Or extends Logical {

    public Or(int line, Token token, Expression left, Expression right) throws SyntaxException {
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
