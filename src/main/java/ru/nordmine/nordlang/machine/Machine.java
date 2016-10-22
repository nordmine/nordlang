package ru.nordmine.nordlang.machine;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.commands.Command;

import java.io.PrintStream;
import java.util.List;

public class Machine {

    private final PrintStream printStream;

    public Machine(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void execute(Program program) throws RunException {
        MachineState state = new MachineState(printStream);
        List<Command> commands = program.getCommands();
        state.setCmdIndex(program.getStartCommandIndex());
        while (state.isActive()) {
            commands.get(state.getCmdIndex()).execute(state);
        }
    }
}
