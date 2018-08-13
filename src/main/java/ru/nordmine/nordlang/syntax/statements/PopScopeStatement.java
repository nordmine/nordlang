package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.PopScopeCommand;

public class PopScopeStatement extends Statement {

    @Override
    public void gen(Program program, Label begin, Label after) {
        program.add(new PopScopeCommand());
    }
}
