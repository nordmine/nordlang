package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.exceptions.RunException;

public class MethodCallCommand extends Command {

    private final Label beginLabel;

    public MethodCallCommand(Label beginLabel) {
        this.beginLabel = beginLabel;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getCallStack().push(state.getCmdIndex() + 1);
        state.pushMethodScope();
        state.setCmdIndex(beginLabel.getPosition());
    }

    @Override
    public String toString() {
        return "CALL " + beginLabel.getPosition();
    }
}
