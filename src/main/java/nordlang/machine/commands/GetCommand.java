package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class GetCommand extends Command {

    private final int nameIndex;

    public GetCommand(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getValueStack().push(state.getScope().get(nameIndex, 0));
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("GET %s", nameIndex);
    }
}
