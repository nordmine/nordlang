package ru.nordmine.nordlang.syntax.expressions.logical;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.value.BoolValue;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.Expression;

public abstract class Logical extends Expression {

    protected Expression left;
    protected Expression right;

    public Logical(int line, Token token, Expression left, Expression right) throws SyntaxException {
        super(line, token, null);
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
        int f = program.newLabel();
        int a = program.newLabel();
        this.jumping(program, 0, f);
        program.addPushCommand(BoolValue.TRUE);
        program.addGotoCommand(a);
        program.fixLabel(f);
        program.addPushCommand(BoolValue.FALSE);
        program.fixLabel(a);
    }

    @Override
    public String toString() {
        return left.toString() + " " + operand.toString() + " " + right.toString();
    }
}
