package nordlang.inter.statements;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Expr;
import nordlang.machine.Program;
import nordlang.lexer.Type;

public class Else extends Statement {

    private Expr expr;
    private Statement statement1, statement2;

    public Else(int line, Expr expr, Statement statement1, Statement statement2) throws ParserException {
        super(line);
        this.expr = expr;
        this.statement1 = statement1;
        this.statement2 = statement2;
        if (expr.getType() != Type.BOOL) {
            error("boolean required in if");
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
