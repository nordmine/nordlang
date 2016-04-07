package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class SetElemCommand extends Command {

    private String name;

    public SetElemCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        int offset = state.getValueStack().pop();
        state.getScope().set(name, offset, state.getValueStack().pop());
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("SET_ELEM %s", name);
    }
}
