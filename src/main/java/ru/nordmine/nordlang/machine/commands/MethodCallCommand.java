package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.syntax.MethodInfo;

public class MethodCallCommand extends Command {

    private final MethodInfo methodInfo;

    public MethodCallCommand(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        state.getCallStack().push(state.getCmdIndex() + 1);
        state.pushMethodScope();
        state.setCmdIndex(methodInfo.getBeginLabel().getDstPosition());
    }

    @Override
    public String toString() {
        return "CALL " + methodInfo.getBeginLabel().getDstPosition();
    }
}
