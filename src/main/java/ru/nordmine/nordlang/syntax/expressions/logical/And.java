package ru.nordmine.nordlang.syntax.expressions.logical;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;

public class And extends Logical {

    public And(int line, Token token, Expression left, Expression right) throws SyntaxException {
        super(line, token, left, right);
    }

    @Override
    public void jumping(Program program, int trueLabel, int falseLabel) {
        int label = falseLabel != 0 ? falseLabel : program.newLabel();
        left.jumping(program, 0, label);
        right.jumping(program, trueLabel, falseLabel);
        if (falseLabel == 0) {
            program.fixLabel(label);
        }
    }
}
