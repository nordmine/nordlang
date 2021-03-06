package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.syntax.ParserUtils;

public class BreakStatement extends Statement {

    private Statement statement;

    public BreakStatement(Token token) throws SyntaxException {
        if (Enclosing == null) {
            ParserUtils.throwError(token.getLine(), "unenclosed break");
        }
        this.statement = Enclosing;
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        program.addGotoCommand(statement.after);
    }
}
