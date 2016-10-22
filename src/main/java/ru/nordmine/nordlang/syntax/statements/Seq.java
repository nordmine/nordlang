package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Program;

public class Seq extends Statement {

    private Statement statement1;
    private Statement statement2;

    public Seq(int line, Statement statement1, Statement statement2) {
        super(line);
        this.statement1 = statement1;
        this.statement2 = statement2;
    }

    @Override
    public void gen(Program program, int begin, int after) {
        if (statement1 == Statement.EMPTY) {
            statement2.gen(program, begin, after);
        } else if (statement2 == Statement.EMPTY) {
            statement1.gen(program, begin, after);
        } else {
            int label = program.newLabel();
            statement1.gen(program, begin, label);
            program.fixLabel(label);
            statement2.gen(program, label, after);
        }
    }
}
