package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.value.BoolValue;

public class IfCommand extends Command {

    private Label label;

    public IfCommand(Label label) {
        this.label = label;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        if (state.getValueStack().pop() == BoolValue.TRUE) {
            state.setCmdIndex(label.getDstPosition());
        } else {
            state.incrementCmdIndex();
        }
    }

    @Override
    public String toString() {
        return "if TRUE goto " + label;
    }

    @Override
    public Label getDestinationLabel() {
        return label;
    }
}
