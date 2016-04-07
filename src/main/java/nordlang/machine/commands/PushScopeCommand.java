package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class PushScopeCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        state.pushScope();
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "PUSH_SCOPE";
    }
}
