package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;

public class PopScopeCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        state.popBlockScope();
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "POP_SCOPE";
    }
}
