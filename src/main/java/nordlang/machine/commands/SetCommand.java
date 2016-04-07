package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class SetCommand extends Command {

    private String name;

    public SetCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getScope().set(name, 0, state.getValueStack().pop());
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("SET %s", name);
    }
}
