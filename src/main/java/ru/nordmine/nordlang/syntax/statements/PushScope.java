package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.PushScopeCommand;

public class PushScope extends Statement {

    public PushScope(int line) {
        super(line);
    }

    @Override
    public void gen(Program program, int b, int a) {
        program.add(new PushScopeCommand());
    }
}
