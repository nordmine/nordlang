package ru.nordmine.nordlang.machine;

import ru.nordmine.nordlang.machine.commands.*;
import ru.nordmine.nordlang.machine.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Program {

    private final List<Command> commands = new ArrayList<>();
    private final List<Label> labelMap = new ArrayList<>();
    private int startCommandIndex = -1;

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

    public void addPushCommand(Value value) {
        commands.add(new PushCommand(value));
    }

    public void addIfCommand(int labelIndex) {
        commands.add(new IfCommand(labelMap.get(labelIndex)));
    }

    public void addIfFalseCommand(int labelIndex) {
        commands.add(new IfFalseCommand(labelMap.get(labelIndex)));
    }

    public void addReturnCommand() {
        commands.add(new ReturnCommand());
    }

    public void addExitCommand() {
        commands.add(new ExitCommand());
    }

    public int newLabel() {
        labelMap.add(new Label());
        return labelMap.size() - 1;
    }

    public void fixLabel(int labelIndex) {
        labelMap.get(labelIndex).setDstPosition(commands.size());
    }

    public Label getLabel(int labelIndex) {
        return labelMap.get(labelIndex);
    }

    public int getStartCommandIndex() {
        return startCommandIndex;
    }

    public void setStartCommandIndex(int startCommandIndex) {
        this.startCommandIndex = startCommandIndex;
    }

    public List<Command> getCommands() {
        return commands;
    }
}
