package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;

public class EchoCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        state.getPrintStream().print(state.getValueStack().pop().getAsString());
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "ECHO";
    }
}
