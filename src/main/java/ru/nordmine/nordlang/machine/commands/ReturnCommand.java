package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.exceptions.RunException;

public class ReturnCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        if (state.getCallStack().isEmpty()) {
            throw new RunException("empty call stack");
        } else {
            state.popMethodScope();
            state.setCmdIndex(state.getCallStack().pop());
        }
    }

    @Override
    public String toString() {
        return "RETURN";
    }
}
