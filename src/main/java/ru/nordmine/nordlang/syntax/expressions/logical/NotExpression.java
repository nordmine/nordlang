package ru.nordmine.nordlang.syntax.expressions.logical;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;

public class NotExpression extends LogicalExpression {

    public NotExpression(Token token, Expression right) throws SyntaxException {
        super(token, right, right);
    }

    @Override
    public void jumping(Program program, Label trueLabel, Label falseLabel) {
        right.jumping(program, falseLabel, trueLabel);
    }

    @Override
    public String toString() {
        return operand.toString() + " " + right.toString();
    }
}
