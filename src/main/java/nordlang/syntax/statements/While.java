package nordlang.syntax.statements;

import nordlang.exceptions.SyntaxException;
import nordlang.syntax.expressions.Expression;
import nordlang.machine.Program;
import nordlang.lexer.Type;

public class While extends Statement {

    private Expression expr;
    private Statement statement;

    public While(int line) {
        super(line);
        expr = null;
        statement = null;
    }

    public void init(Expression x, Statement s) throws SyntaxException {
        expr = x;
        statement = s;
        if (expr.getType() != Type.BOOL) {
            StatementUtils.throwError(getLine(), "boolean value required in while");
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
