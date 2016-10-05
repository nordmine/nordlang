package nordlang.syntax.statements;

import nordlang.machine.Program;

public class Seq extends Statement {

    private Statement statement1;
    private Statement statement2;

    public Seq(int line, Statement statement1, Statement statement2) {
        super(line);
        this.statement1 = statement1;
        this.statement2 = statement2;
    }

    @Override
    public void gen(Program program, int b, int a) {
        if (statement1 == Statement.Empty) {
            statement2.gen(program, b, a);
        } else if (statement2 == Statement.Empty) {
            statement1.gen(program, b, a);
        } else {
            int label = program.newLabel();
            statement1.gen(program, b, label);
            program.fixLabel(label);
            statement2.gen(program, label, a);
        }
    }
}
