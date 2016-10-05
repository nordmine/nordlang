package ru.nordmine.nordlang.syntax.expressions.operators;

import ru.nordmine.nordlang.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.syntax.expressions.ExpressionUtils;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.BinaryCommand;

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
