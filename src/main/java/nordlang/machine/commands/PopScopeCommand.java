package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class PopScopeCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        state.popScope();
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "POP_SCOPE";
    }
}
