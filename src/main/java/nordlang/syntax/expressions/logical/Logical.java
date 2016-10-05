package nordlang.syntax.expressions.logical;

import nordlang.exceptions.SyntaxException;
import nordlang.syntax.expressions.Expression;
import nordlang.syntax.expressions.ExpressionUtils;
import nordlang.lexer.Token;
import nordlang.machine.Program;
import nordlang.machine.commands.PushCommand;
import nordlang.lexer.Type;

public abstract class Logical extends Expression {

    public Expression leftExpr, rightExpr;

    public Logical(int line, Token token, Expression leftExpr, Expression rightExpr) throws SyntaxException {
        super(line, token, null);
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
        type = check(leftExpr.getType(), rightExpr.getType());
        if (type == null) {
            ExpressionUtils.typeError(getLine(), leftExpr.getType(), rightExpr.getType());
        }
    }

    public Type check(Type type1, Type type2) {
        if (type1 == Type.BOOL && type2 == Type.BOOL) {
            return Type.BOOL;
        } else {
            return null;
        }
    }

    @Override
    public void gen(Program program) {
        int f = program.newLabel();
        int a = program.newLabel();
        this.jumping(program, 0, f);
        program.add(new PushCommand(1));
        program.addGotoCommand(a);
        program.fixLabel(f);
        program.add(new PushCommand(0));
        program.fixLabel(a);
    }

    @Override
    public String toString() {
        return leftExpr.toString() + " " + operand.toString() + " " + rightExpr.toString();
    }
}
