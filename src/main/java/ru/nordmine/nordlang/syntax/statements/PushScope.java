package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.PushScopeCommand;

public class PushScope extends Statement {

    public PushScope(int line) {
        super(line);
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        program.add(new PushScopeCommand());
    }
}
