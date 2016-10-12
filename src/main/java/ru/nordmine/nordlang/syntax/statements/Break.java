package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.syntax.ParserUtils;

public class Break extends Statement {

    private Statement statement;

    public Break(int line) throws SyntaxException {
        super(line);
        if (Enclosing == null) {
            ParserUtils.throwError(getLine(), "unenclosed break");
        }
        this.statement = Enclosing;
    }

    @Override
    public void gen(Program program, int b, int a) {
        program.addGotoCommand(statement.after);
    }
}
