package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.value.Value;

public class PushCommand extends Command {

    private final Value value;

    public PushCommand(Value value) {
        this.value = value;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getValueStack().push(value);
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "PUSH " + value;
    }
}
