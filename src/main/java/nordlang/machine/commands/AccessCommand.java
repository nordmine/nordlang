package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class AccessCommand extends Command {

    private final int nameIndex;

    public AccessCommand(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        int offset = state.getValueStack().pop();
        state.getValueStack().push(state.getScope().get(nameIndex, offset));
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("ACCESS %s", nameIndex);
    }
}
