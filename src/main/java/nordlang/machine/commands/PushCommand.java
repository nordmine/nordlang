package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class PushCommand extends Command {

    private int value;

    public PushCommand(int value) {
        this.value = value;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getValueStack().push(value);
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "PUSH " + value;
    }
}
