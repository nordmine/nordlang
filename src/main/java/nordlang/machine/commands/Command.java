package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public abstract class Command {

    public abstract void execute(MachineState state) throws RunException;
}
