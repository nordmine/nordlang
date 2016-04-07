package nordlang.inter.statements;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.machine.Program;
import nordlang.lexer.Type;

public class While extends Statement {

    private Expr expr;
    private Statement statement;

    public While(int line) {
        super(line);
        expr = null;
        statement = null;
    }

    public void init(Expr x, Statement s) throws ParserException {
        expr = x;
        statement = s;
        if (expr.getType() != Type.BOOL) {
            error("boolean value required in while");
        }
    }

    @Override
    public void gen(Program program, int b, int a) {
        after = a;
        expr.jumping(program, 0, a);
        int label = program.newLabel();
        program.fixLabel(label);
        statement.gen(program, label, b);
        program.addGotoCommand(b);
    }
}
