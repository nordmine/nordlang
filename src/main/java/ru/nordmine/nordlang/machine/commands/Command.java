package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;

public abstract class Command {

    public abstract void execute(MachineState state) throws RunException;

    public Label getDestinationLabel() {
        return null;
    }
}
