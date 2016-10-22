package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.value.Value;

public class AccessCommand extends Command {

    private final int nameIndex;

    public AccessCommand(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        Value value = state.getValueStack().pop();
        state.getValueStack().push(state.getScope().get(nameIndex).getByIndex(value));
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("ACCESS %s", nameIndex);
    }
}
