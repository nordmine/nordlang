package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;

public class EchoCharCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        System.out.print((char)state.getValueStack().pop().intValue());
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "ECHO_CHAR";
    }
}
