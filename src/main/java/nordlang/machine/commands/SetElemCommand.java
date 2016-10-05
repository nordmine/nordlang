package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class SetElemCommand extends Command {

    private final int nameIndex;

    public SetElemCommand(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        int offset = state.getValueStack().pop();
        state.getScope().set(nameIndex, offset, state.getValueStack().pop());
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("SET_ELEM %s", nameIndex);
    }
}
