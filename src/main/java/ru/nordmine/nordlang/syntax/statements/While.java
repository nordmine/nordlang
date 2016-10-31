package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.lexer.TypeToken;

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
        if (expr.getType() != TypeToken.BOOL) {
            ParserUtils.throwError(getLine(), "boolean value required in while");
        }
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        this.after = after;
        expr.jumping(program, Label.EMPTY, after);
        Label label = program.newLabel();
        program.fixLabel(label);
        statement.gen(program, label, begin);
        program.addGotoCommand(begin);
    }
}
