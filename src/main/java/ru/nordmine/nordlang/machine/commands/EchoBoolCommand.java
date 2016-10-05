package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;

public class EchoBoolCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        int value = state.getValueStack().pop();
        if (value == 1) {
            System.out.print("true");
        } else if (value == 0) {
            System.out.println("false");
        }
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "ECHO_BOOL";
    }
}
