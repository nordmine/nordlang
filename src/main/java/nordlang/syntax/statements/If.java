package nordlang.syntax.statements;

import nordlang.exceptions.SyntaxException;
import nordlang.syntax.expressions.Expression;
import nordlang.machine.Program;
import nordlang.lexer.Type;

public class If extends Statement {

    private Expression expr;
    private Statement statement;

    public If(int line, Expression expr, Statement statement) throws SyntaxException {
        super(line);
        this.expr = expr;
        this.statement = statement;
        if (expr.getType() != Type.BOOL) {
            StatementUtils.throwError(getLine(), "boolean value required in if");
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
