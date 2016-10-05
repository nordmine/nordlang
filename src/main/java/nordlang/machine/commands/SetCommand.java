package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class SetCommand extends Command {

    private final int nameIndex;

    public SetCommand(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getScope().set(nameIndex, 0, state.getValueStack().pop());
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("SET %s", nameIndex);
    }
}
