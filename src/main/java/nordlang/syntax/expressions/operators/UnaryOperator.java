package nordlang.syntax.expressions.operators;

import nordlang.exceptions.SyntaxException;
import nordlang.syntax.expressions.Expression;
import nordlang.syntax.expressions.ExpressionUtils;
import nordlang.lexer.Tag;
import nordlang.lexer.Token;
import nordlang.machine.Program;
import nordlang.machine.commands.NegateCommand;
import nordlang.lexer.Type;

public class UnaryOperator extends Expression {

    private Expression expr;

    public UnaryOperator(int line, Token token, Expression x) throws SyntaxException {
        super(line, token, null);
        expr = x;
        if (expr.getType() != Type.INT) {
            ExpressionUtils.typeError(getLine(), Type.INT, expr.getType());
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
