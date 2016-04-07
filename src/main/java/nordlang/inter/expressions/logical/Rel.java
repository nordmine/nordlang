package nordlang.inter.expressions.logical;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.lexer.Token;
import nordlang.machine.Program;
import nordlang.machine.commands.BinaryCommand;
import nordlang.lexer.Array;
import nordlang.lexer.Type;

public class Rel extends Logical {

    public Rel(int line, Token token, Expr leftExpr, Expr rightExpr) throws ParserException {
        super(line, token, leftExpr, rightExpr);
    }

    @Override
    public Type check(Type type1, Type type2) {
        if (type1 instanceof Array || type2 instanceof Array) {
            return null;
        } else if (type1 == type2) {
            return Type.BOOL;
        } else {
            return null;
        }
    }

    @Override
    public void jumping(Program program, int trueLabel, int falseLabel) {
        leftExpr.gen(program);
        rightExpr.gen(program);
        program.add(new BinaryCommand(operand.getTag()));
        emitJumps(program, trueLabel, falseLabel);
    }
}

