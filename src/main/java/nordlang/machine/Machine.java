package nordlang.machine;

import nordlang.exceptions.RunException;
import nordlang.machine.commands.Command;

import java.util.List;

public class Machine {

    public void execute(Program program) throws RunException {
        MachineState state = new MachineState();
        List<Command> commands = program.getCommands();
        while (state.getCmdIndex() < commands.size()) {
            commands.get(state.getCmdIndex()).execute(state);
        }
    }
}
