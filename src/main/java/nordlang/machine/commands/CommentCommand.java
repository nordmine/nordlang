package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class CommentCommand extends Command {

    private String comment;

    public CommentCommand(String comment) {
        this.comment = comment;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "// " + comment;
    }
}
