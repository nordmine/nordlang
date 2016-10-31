package ru.nordmine.nordlang.syntax.expressions.operators;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.NegateCommand;
import ru.nordmine.nordlang.lexer.TypeToken;

public class UnaryExpression extends Expression {

    private Expression expr;

    public UnaryExpression(int line, Token token, Expression x) throws SyntaxException {
        super(line, token, null);
        expr = x;
        if (expr.getType() != TypeToken.INT) {
            ParserUtils.typeError(getLine(), TypeToken.INT, expr.getType());
        }
        this.type = this.expr.getType();
    }

    @Override
    public void gen(Program program) {
        if (operand.getTag() == Tag.UNARY_MINUS) {
            expr.gen(program);
            program.add(new NegateCommand());
        }
    }

    @Override
    public String toString() {
        return operand.toString() + " " + expr.toString();
    }
}
