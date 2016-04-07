package nordlang.inter.expressions.operators;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.lexer.Tag;
import nordlang.lexer.Token;
import nordlang.machine.Program;
import nordlang.machine.commands.NegateCommand;
import nordlang.lexer.Type;

public class UnaryOperator extends Expr {

    private Expr expr;

    public UnaryOperator(int line, Token token, Expr x) throws ParserException {
        super(line, token, null);
        expr = x;
        if (expr.getType() != Type.INT) {
            typeError(Type.INT, expr.getType());
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
