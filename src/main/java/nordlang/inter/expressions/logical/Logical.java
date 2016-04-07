package nordlang.inter.expressions.logical;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.lexer.Token;
import nordlang.machine.Program;
import nordlang.machine.commands.PushCommand;
import nordlang.lexer.Type;

public abstract class Logical extends Expr {

    public Expr leftExpr, rightExpr;

    public Logical(int line, Token token, Expr leftExpr, Expr rightExpr) throws ParserException {
        super(line, token, null);
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
        type = check(leftExpr.getType(), rightExpr.getType());
        if (type == null) {
            typeError(leftExpr.getType(), rightExpr.getType());
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
