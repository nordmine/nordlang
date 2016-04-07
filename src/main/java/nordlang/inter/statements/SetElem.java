package nordlang.inter.statements;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.operators.Access;
import nordlang.inter.expressions.Expr;
import nordlang.inter.expressions.Id;
import nordlang.machine.Program;
import nordlang.machine.commands.SetElemCommand;
import nordlang.lexer.Array;
import nordlang.lexer.Type;

public class SetElem extends Statement {

    private Id array;
    private Expr index;
    private Expr expr;

    public SetElem(int line, Access x, Expr expr) throws ParserException {
        super(line);
        this.array = x.getArray();
        this.index = x.getIndex();
        this.expr = expr;
        if (check(x.getType(), expr.getType()) == null) {
            typeError(x.getType(), expr.getType());
        }
    }

    private Type check(Type type1, Type type2) {
        if (type1 instanceof Array || type2 instanceof Array) {
            return null;
        } else if (type1 == Type.INT && type2 == Type.INT) {
            return Type.INT;
        } else if (type1 == Type.BOOL && type2 == Type.BOOL) {
            return Type.BOOL;
        } else if (type1 == Type.CHAR && type2 == Type.CHAR) {
            return Type.CHAR;
        } else {
            return null;
        }
    }

    @Override
    public void gen(Program program, int b, int a) {
        expr.gen(program);
        index.gen(program);
        program.add(new SetElemCommand(array.toString()));
    }
}
