package ru.nordmine.nordlang.machine;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.value.Value;

import java.util.Stack;

public class MachineState {

    private MachineScope scope = new MachineScope(null);
    private Stack<Value> valueStack = new Stack<>();
    private int cmdIndex = 0;

    public MachineScope getScope() {
        return scope;
    }

    public void pushScope() {
        this.scope = new MachineScope(this.scope);
    }

    public void popScope() throws RunException {
        if (this.scope.getParent() == null) {
            throw new RunException("parent scope is null");
        }
        this.scope = this.scope.getParent();
    }

    public Stack<Value> getValueStack() {
        return valueStack;
    }

    public int getCmdIndex() {
        return cmdIndex;
    }

    public void setCmdIndex(int cmdIndex) {
        this.cmdIndex = cmdIndex;
    }

    public void incrementCmdIndex() {
        this.cmdIndex++;
    }
}