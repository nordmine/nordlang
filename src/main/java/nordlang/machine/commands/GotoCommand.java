package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.Label;
import nordlang.machine.MachineState;

public class GotoCommand extends Command {

    private Label label;

    public GotoCommand(Label label) {
        this.label = label;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.setCmdIndex(label.getDstPosition());
    }

    @Override
    public String toString() {
        return "GOTO " + label;
    }
}
