package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.value.Value;

public class SetElemCommand extends Command {

    private final int nameIndex;

    public SetElemCommand(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        Value index = state.getValueStack().pop();
        state.getScope().get(nameIndex).setByIndex(index, state.getValueStack().pop());
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("SET_ELEM %s", nameIndex);
    }
}
