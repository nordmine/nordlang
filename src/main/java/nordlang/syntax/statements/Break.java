package nordlang.syntax.statements;

import nordlang.exceptions.SyntaxException;
import nordlang.machine.Program;

public class Break extends Statement {

    private Statement statement;

    public Break(int line) throws SyntaxException {
        super(line);
        if (Statement.Enclosing == null) {
            StatementUtils.throwError(getLine(), "unenclosed break");
        }
        this.statement = Statement.Enclosing;
    }

    @Override
    public void gen(Program program, int b, int a) {
        program.addGotoCommand(statement.after);
    }
}
