package nordlang.inter.statements;

import nordlang.exceptions.ParserException;
import nordlang.machine.Program;

public class Break extends Statement {

    private Statement statement;

    public Break(int line) throws ParserException {
        super(line);
        if (Statement.Enclosing == null) {
            error("unenclosed break");
        }
        this.statement = Statement.Enclosing;
    }

    @Override
    public void gen(Program program, int b, int a) {
        program.addGotoCommand(statement.after);
    }
}
