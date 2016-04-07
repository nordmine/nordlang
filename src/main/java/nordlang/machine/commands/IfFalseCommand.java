package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.Label;
import nordlang.machine.MachineState;

public class IfFalseCommand extends Command {

    private Label label;

    public IfFalseCommand(Label label) {
        this.label = label;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        if (state.getValueStack().pop() == 0) {
            state.setCmdIndex(label.getDstPosition());
        } else {
            state.incrementCmdIndex();
        }
    }

    @Override
    public String toString() {
        return "if 0 goto " + label;
    }
}
