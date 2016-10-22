package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.lexer.TypeToken;

public class If extends Statement {

    private Expression expr;
    private Statement statement;

    public If(int line, Expression expr, Statement statement) throws SyntaxException {
        super(line);
        this.expr = expr;
        this.statement = statement;
        if (expr.getType() != TypeToken.BOOL) {
            ParserUtils.throwError(getLine(), "boolean value required in if");
        }
    }

    @Override
    public void gen(Program program, int begin, int after) {
        int label = program.newLabel();
        expr.jumping(program, 0, after); // Проходим при значении true, проход к метке a при значении false
        program.fixLabel(label);
        statement.gen(program, label, after);
    }
}
