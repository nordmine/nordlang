package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.MachineState;

public class GotoCommand extends Command {

    private final Label label;

    public GotoCommand(Label label) {
        this.label = label;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.setCmdIndex(label.getPosition());
    }

    @Override
    public String toString() {
        return "GOTO " + label;
    }

    @Override
    public Label getDestinationLabel() {
        return label;
    }
}
