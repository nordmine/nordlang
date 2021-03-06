package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.value.ListValue;
import ru.nordmine.nordlang.machine.value.Value;

public class DefineArrayCommand extends Command {

    private final int nameIndex;
    private final Value initialValue;

    public DefineArrayCommand(int nameIndex, Value initialValue) {
        this.nameIndex = nameIndex;
        this.initialValue = initialValue;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getScope().defineInCurrentScope(nameIndex, new ListValue(initialValue));
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "DEFINE_ARRAY name=" + nameIndex;
    }
}
