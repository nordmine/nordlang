package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;

public class PushScopeCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        state.pushBlockScope();
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "PUSH_SCOPE";
    }
}
