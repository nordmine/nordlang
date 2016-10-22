package ru.nordmine.nordlang.machine;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.value.Value;

import java.io.PrintStream;
import java.util.Stack;

public class MachineState {

    private MachineScope scope = new MachineScope(null);
    private Stack<MachineScope> scopeStack = new Stack<>();
    private Stack<Value> valueStack = new Stack<>();
    private final PrintStream printStream;
    private boolean isActive = true;

    public MachineState(PrintStream printStream) {
        this.printStream = printStream;
    }

    // когда происходит вызов метода, заносим сюда номер текущей команды
    // когда происходит возврат из метода, извлекаем значение отсюда и переходим на нужную команду
    private Stack<Integer> callStack = new Stack<>();

    private int cmdIndex = 0;

    public MachineScope getScope() {
        return scope;
    }

    public void pushBlockScope() {
        this.scope = new MachineScope(this.scope);
    }

    public void popBlockScope() throws RunException {
        if (this.scope.getParent() == null) {
            throw new RunException("parent scope is null");
        }
        this.scope = this.scope.getParent();
    }

    public void pushMethodScope() {
        scopeStack.push(this.scope);
        this.scope = new MachineScope(null);
    }

    public void popMethodScope() throws RunException {
        if (scopeStack.isEmpty()) {
            throw new RunException("scope stack is empty");
        }
        this.scope = scopeStack.pop();
    }

    public Stack<Value> getValueStack() {
        return valueStack;
    }

    public Stack<Integer> getCallStack() {
        return callStack;
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

    public PrintStream getPrintStream() {
        return printStream;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
