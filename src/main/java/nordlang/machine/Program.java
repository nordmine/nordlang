package nordlang.machine;

import nordlang.machine.commands.*;

import java.util.ArrayList;
import java.util.List;

public class Program {

    private final List<Command> commands = new ArrayList<>();
    private final List<Label> labelMap = new ArrayList<>();

    public Program() {
        // метка 0 не используется
        labelMap.add(new Label());
    }

    public void add(Command command) {
        commands.add(command);
    }

    public void addGotoCommand(int labelIndex) {
        commands.add(new GotoCommand(labelMap.get(labelIndex)));
    }

    public void addPushCommand(int value) {
        commands.add(new PushCommand(value));
    }

    public void addComment(String comment) {
        commands.add(new CommentCommand(comment));
    }

    public void addIfCommand(int labelIndex) {
        commands.add(new IfCommand(labelMap.get(labelIndex)));
    }

    public void addIfFalseCommand(int labelIndex) {
        commands.add(new IfFalseCommand(labelMap.get(labelIndex)));
    }

    public int newLabel() {
        labelMap.add(new Label());
        return labelMap.size() - 1;
    }

    public void fixLabel(int labelIndex) {
        labelMap.get(labelIndex).setDstPosition(commands.size());
    }

    public List<Command> getCommands() {
        return commands;
    }
}
