package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class AccessCommand extends Command {

    private String name;

    public AccessCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        int offset = state.getValueStack().pop();
        state.getValueStack().push(state.getScope().get(name, offset));
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("ACCESS %s", name);
    }
}
