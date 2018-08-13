package ru.nordmine.nordlang.syntax.expressions.logical;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.value.BoolValue;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.Expression;

public abstract class LogicalExpression extends Expression {

    protected Expression left;
    protected Expression right;

    public LogicalExpression(Token token, Expression left, Expression right) throws SyntaxException {
        super(token, null);
        this.left = left;
        this.right = right;
        type = check(left.getType(), right.getType());
        if (type == null) {
            ParserUtils.typeError(getLine(), left.getType(), right.getType());
        }
    }

    protected TypeToken check(TypeToken type1, TypeToken type2) {
        if (type1 == TypeToken.BOOL && type2 == TypeToken.BOOL) {
            return TypeToken.BOOL;
        } else {
            return null;
        }
    }

    @Override
    public void gen(Program program) {
        Label falseLabel = program.newLabel();
        Label after = program.newLabel();
        this.jumping(program, Label.EMPTY, falseLabel);
        program.addPushCommand(BoolValue.TRUE);
        program.addGotoCommand(after);
        program.fixLabel(falseLabel);
        program.addPushCommand(BoolValue.FALSE);
        program.fixLabel(after);
    }

    @Override
    public String toString() {
        return left.toString() + " " + operand.toString() + " " + right.toString();
    }
}
