package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;

public class SequenceStatement extends Statement {

    private Statement statement1;
    private Statement statement2;

    public SequenceStatement(Statement statement1, Statement statement2) {
        this.statement1 = statement1;
        this.statement2 = statement2;
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        if (statement1 == Statement.EMPTY) {
            statement2.gen(program, begin, after);
        } else if (statement2 == Statement.EMPTY) {
            statement1.gen(program, begin, after);
        } else {
            Label label = program.newLabel();
            statement1.gen(program, begin, label);
            program.fixLabel(label);
            statement2.gen(program, label, after);
        }
    }
}
