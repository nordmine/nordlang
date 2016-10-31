package ru.nordmine.nordlang.machine;

import ru.nordmine.nordlang.machine.commands.*;
import ru.nordmine.nordlang.machine.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Program {

    private final List<Command> commands = new ArrayList<>();
    private int startCommandIndex = -1;

    public void add(Command command) {
        commands.add(command);
    }

    public void addGotoCommand(Label label) {
        commands.add(new GotoCommand(label));
    }

    public void addPushCommand(Value value) {
        commands.add(new PushCommand(value));
    }

    public void addIfCommand(Label label) {
        commands.add(new IfCommand(label));
    }

    public void addIfFalseCommand(Label label) {
        commands.add(new IfFalseCommand(label));
    }

    public void addReturnCommand() {
        commands.add(new ReturnCommand());
    }

    public void addExitCommand() {
        commands.add(new ExitCommand());
    }

    public Label newLabel() {
        return new Label();
    }

    public void fixLabel(Label label) {
        label.fix(commands.size());
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
