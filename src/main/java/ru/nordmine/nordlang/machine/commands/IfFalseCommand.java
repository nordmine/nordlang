package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.value.BoolValue;

public class IfFalseCommand extends Command {

    private Label label;

    public IfFalseCommand(Label label) {
        this.label = label;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        if (state.getValueStack().pop() == BoolValue.FALSE) {
            state.setCmdIndex(label.getDstPosition());
        } else {
            state.incrementCmdIndex();
        }
    }

    @Override
    public String toString() {
        return "if FALSE goto " + label;
    }
}
