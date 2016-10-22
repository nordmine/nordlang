package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.value.Value;

public class DefineCommand extends Command {

    private final int nameIndex;
    private final Value initialValue;

    public DefineCommand(int nameIndex, Value initialValue) {
        this.nameIndex = nameIndex;
        this.initialValue = initialValue;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getScope().defineInCurrentScope(nameIndex, initialValue);
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("DEF %s", nameIndex);
    }
}
