package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.PopScopeCommand;

public class PopScope extends Statement {

    public PopScope(int line) {
        super(line);
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        program.add(new PopScopeCommand());
    }
}
