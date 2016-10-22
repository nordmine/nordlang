package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.value.ListValue;
import ru.nordmine.nordlang.machine.value.Value;

public class DefineArrayCommand extends Command {

    private final int nameIndex;
    private final int initialSize;
    private final Value initialValue;

    public DefineArrayCommand(int nameIndex, int initialSize, Value initialValue) {
        this.nameIndex = nameIndex;
        this.initialSize = initialSize;
        this.initialValue = initialValue;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getScope().defineInCurrentScope(nameIndex, new ListValue(initialValue, initialSize));
        state.incrementCmdIndex();
    }
}
