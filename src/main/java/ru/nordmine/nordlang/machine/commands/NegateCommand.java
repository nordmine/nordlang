package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;

public class NegateCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        state.getValueStack().push(-1 * state.getValueStack().pop());
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "UNARY_MINUS";
    }
}
