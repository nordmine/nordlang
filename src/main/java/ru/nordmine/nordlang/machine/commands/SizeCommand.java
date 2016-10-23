package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.exceptions.RunException;

public class SizeCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        state.getValueStack().push(state.getValueStack().pop().size());
        state.incrementCmdIndex();
    }
}
