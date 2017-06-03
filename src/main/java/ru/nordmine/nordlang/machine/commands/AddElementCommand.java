package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.value.Value;

import java.util.Stack;

public class AddElementCommand extends Command {

    private final int nameIndex;

    public AddElementCommand(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        Stack<Value> valueStack = state.getValueStack();
        Value right = valueStack.pop();
        Value left = state.getScope().get(nameIndex);
        left.addElement(right);
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "ADD_ELEMENT";
    }
}
