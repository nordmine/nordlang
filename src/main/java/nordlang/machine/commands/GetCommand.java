package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class GetCommand extends Command {

    private String name;

    public GetCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getValueStack().push(state.getScope().get(name, 0));
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("GET %s", name);
    }
}
