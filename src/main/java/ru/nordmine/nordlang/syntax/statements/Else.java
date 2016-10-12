package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.lexer.TypeToken;

public class Else extends Statement {

    private Expression expr;
    private Statement statement1, statement2;

    public Else(int line, Expression expr, Statement statement1, Statement statement2) throws SyntaxException {
        super(line);
        this.expr = expr;
        this.statement1 = statement1;
        this.statement2 = statement2;
        if (expr.getType() != TypeToken.BOOL) {
            ParserUtils.throwError(getLine(), "boolean required in if");
        }
    }

    @Override
    public void gen(Program program, int b, int a) {
        int label1 = program.newLabel();
        int label2 = program.newLabel();
        expr.jumping(program, 0, label2);
        program.fixLabel(label1);
        statement1.gen(program, label1, a);
        program.addGotoCommand(a);
        program.fixLabel(label2);
        statement2.gen(program, label2, a);
    }
}