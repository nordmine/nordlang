package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.lexer.TypeToken;

public class IfStatement extends Statement {

    private Expression expr;
    private Statement statement;

    public IfStatement(int line, Expression expr, Statement statement) throws SyntaxException {
        super(line);
        this.expr = expr;
        this.statement = statement;
        if (expr.getType() != TypeToken.BOOL) {
            ParserUtils.throwError(getLine(), "boolean value required in if");
        }
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        Label label = program.newLabel();
        expr.jumping(program, Label.EMPTY, after); // Проходим при значении true, проход к метке a при значении false
        program.fixLabel(label);
        statement.gen(program, label, after);
    }
}
