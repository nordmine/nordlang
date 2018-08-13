package ru.nordmine.nordlang.syntax.expressions.logical;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;

public class OrExpression extends LogicalExpression {

    public OrExpression(Token token, Expression left, Expression right) throws SyntaxException {
        super(token, left, right);
    }

    @Override
    public void jumping(Program program, Label trueLabel, Label falseLabel) {
        Label label = trueLabel != Label.EMPTY ? trueLabel : program.newLabel();
        left.jumping(program, label, Label.EMPTY);
        right.jumping(program, trueLabel, falseLabel);
        if (trueLabel == Label.EMPTY) {
            program.fixLabel(label);
        }
    }
}
