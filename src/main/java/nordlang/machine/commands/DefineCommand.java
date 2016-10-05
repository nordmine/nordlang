package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class DefineCommand extends Command {

    private final int nameIndex;
    private final int width;

    public DefineCommand(int nameIndex, int width) {
        this.nameIndex = nameIndex;
        this.width = width;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getScope().defineInCurrentScope(nameIndex, width);
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return String.format("DEF %s, width %s", nameIndex, width);
    }
}
