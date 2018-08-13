package ru.nordmine.nordlang.syntax.expressions.logical;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;

public class AndExpression extends LogicalExpression {

    public AndExpression(Token token, Expression left, Expression right) throws SyntaxException {
        super(token, left, right);
    }

    @Override
    public void jumping(Program program, Label trueLabel, Label falseLabel) {
        Label label = falseLabel.getPosition() != 0 ? falseLabel : program.newLabel();
        left.jumping(program, Label.EMPTY, label);
        right.jumping(program, trueLabel, falseLabel);
        if (falseLabel == Label.EMPTY) {
            program.fixLabel(label);
        }
    }
}
