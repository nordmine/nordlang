package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.exceptions.RunException;
import ru.nordmine.nordlang.machine.MachineState;

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
