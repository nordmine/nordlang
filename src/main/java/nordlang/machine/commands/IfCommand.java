package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.Label;
import nordlang.machine.MachineState;

public class IfCommand extends Command {

    private Label label;

    public IfCommand(Label label) {
        this.label = label;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        if (state.getValueStack().pop() == 1) {
            state.setCmdIndex(label.getDstPosition());
        } else {
            state.incrementCmdIndex();
        }
    }

    @Override
    public String toString() {
        return "if 1 goto " + label;
    }
}
