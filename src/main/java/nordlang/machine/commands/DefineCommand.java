package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class DefineCommand extends Command {

    private String name;
    private int width;

    public DefineCommand(String name, int width) {
        this.name = name;
        this.width = width;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getScope().defineInCurrentScope(name, width);
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("DEF %s, width %s", name, width);
    }
}
