package nordlang.syntax.expressions.operators;

import nordlang.exceptions.SyntaxException;
import nordlang.syntax.expressions.Expression;
import nordlang.syntax.expressions.ExpressionUtils;
import nordlang.lexer.Token;
import nordlang.machine.Program;
import nordlang.machine.commands.BinaryCommand;

public class BinaryOperator extends Expression {

    private Expression left;
    private Expression right;

    public BinaryOperator(int line, Token token, Expression left, Expression right) throws SyntaxException {
        super(line, token, null);
        this.left = left;
        this.right = right;
        if (this.left.getType() != this.right.getType()) {
            ExpressionUtils.typeError(getLine(), this.left.getType(), this.right.getType());
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
