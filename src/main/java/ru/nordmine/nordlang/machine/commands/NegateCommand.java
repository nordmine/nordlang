package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;

public class NegateCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        state.getValueStack().push(state.getValueStack().pop().negate());
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "UNARY_MINUS";
    }
}
