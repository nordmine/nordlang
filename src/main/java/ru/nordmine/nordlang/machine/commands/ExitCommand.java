package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.exceptions.RunException;

public class ExitCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        state.setActive(false);
        state.incrementCmdIndex();
    }
}
