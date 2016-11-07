package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.lexer.TypeToken;

public class ElseStatement extends Statement {

    private Expression expr;
    private Statement statement1, statement2;

    public ElseStatement(int line, Expression expr, Statement statement1, Statement statement2) throws SyntaxException {
        super(line);
        this.expr = expr;
        this.statement1 = statement1;
        this.statement2 = statement2;
        if (expr.getType() != TypeToken.BOOL) {
            ParserUtils.throwError(getLine(), "boolean required in if");
        }
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        Label label1 = program.newLabel();
        Label label2 = program.newLabel();
        expr.jumping(program, Label.EMPTY, label2);
        program.fixLabel(label1);
        statement1.gen(program, label1, after);
        program.addGotoCommand(after);
        program.fixLabel(label2);
        statement2.gen(program, label2, after);
    }
}
