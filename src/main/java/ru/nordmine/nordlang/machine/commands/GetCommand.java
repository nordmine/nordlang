package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;

public class GetCommand extends Command {

    private final int nameIndex;

    public GetCommand(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getValueStack().push(state.getScope().get(nameIndex));
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("GET %s", nameIndex);
    }
}
