package nordlang.inter.expressions.operators;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.lexer.Token;
import nordlang.machine.Program;
import nordlang.machine.commands.BinaryCommand;

public class BinaryOperator extends Expr {

    private Expr left;
    private Expr right;

    public BinaryOperator(int line, Token token, Expr left, Expr right) throws ParserException {
        super(line, token, null);
        this.left = left;
        this.right = right;
        if (this.left.getType() != this.right.getType()) {
            typeError(this.left.getType(), this.right.getType());
        }
        this.type = this.left.getType();
    }

    @Override
    public void gen(Program program) {
        left.gen(program);
        right.gen(program);
        program.add(new BinaryCommand(operand.getTag()));
    }

    @Override
    public String toString() {
        return left.toString() + " " + operand.toString() + " " + right.toString();
    }
}
