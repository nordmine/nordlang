package nordlang.inter.statements;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.inter.expressions.Id;
import nordlang.machine.Program;
import nordlang.machine.commands.SetCommand;
import nordlang.lexer.Type;

public class Set extends Statement {

    private Id id;
    public Expr expr;

    public Set(int line, Id id, Expr expr) throws ParserException {
        super(line);
        this.id = id;
        this.expr = expr;
        if (check(id.getType(), expr.getType()) == null) {
            typeError(id.getType(), expr.getType());
        }
    }

    private Type check(Type type1, Type type2) {
        if (type1 == Type.INT && type2 == Type.INT) {
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
        program.add(new SetCommand(id.toString()));
    }
}
