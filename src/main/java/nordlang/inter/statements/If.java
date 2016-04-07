package nordlang.inter.statements;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.machine.Program;
import nordlang.lexer.Type;

public class If extends Statement {

    private Expr expr;
    private Statement statement;

    public If(int line, Expr expr, Statement statement) throws ParserException {
        super(line);
        this.expr = expr;
        this.statement = statement;
        if (expr.getType() != Type.BOOL) {
            error("boolean value required in if");
        }
    }

    @Override
    public void gen(Program program, int b, int a) {
        int label = program.newLabel();
        expr.jumping(program, 0, a); // Проходим при значении true, проход к метке a при значении false
        program.fixLabel(label);
        statement.gen(program, label, a);
    }
}
