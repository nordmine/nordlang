package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.PushScopeCommand;

public class PushScopeStatement extends Statement {

    public PushScopeStatement(int line) {
        super(line);
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        program.add(new PushScopeCommand());
    }
}
