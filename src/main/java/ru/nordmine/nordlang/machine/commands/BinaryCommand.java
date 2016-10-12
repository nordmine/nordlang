package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.machine.MachineState;
import ru.nordmine.nordlang.machine.value.BoolValue;
import ru.nordmine.nordlang.machine.value.Value;

import java.util.Stack;

public class BinaryCommand extends Command {

    private Tag operator;

    public BinaryCommand(Tag operator) {
        this.operator = operator;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        Stack<Value> valueStack = state.getValueStack();
        Value right = valueStack.pop();
        Value left = valueStack.pop();
        switch (operator) {
            case PLUS: valueStack.push(left.plus(right)); break;
            case MINUS: valueStack.push(left.minus(right)); break;
            case MUL: valueStack.push(left.multiple(right)); break;
            case DIVISION: valueStack.push(left.division(right)); break;
            case MOD: valueStack.push(left.mod(right)); break;
            case EQUAL: valueStack.push(left.isEquals(right) ? BoolValue.TRUE : BoolValue.FALSE); break;
            case NOT_EQUAL: valueStack.push(!left.isEquals(right) ? BoolValue.TRUE : BoolValue.FALSE); break;
            case LESS: valueStack.push(left.isLesserThan(right) ? BoolValue.TRUE : BoolValue.FALSE); break;
            case LESS_OR_EQUAL: valueStack.push(left.isLesserThan(right) || left.isEquals(right) ? BoolValue.TRUE : BoolValue.FALSE); break;
            case GREATER: valueStack.push(left.isGreaterThan(right) ? BoolValue.TRUE : BoolValue.FALSE); break;
            case GREATER_OR_EQUAL: valueStack.push(left.isGreaterThan(right) || left.isEquals(right) ? BoolValue.TRUE : BoolValue.FALSE); break;
            default:
                throw new RunException("unexpected operation: " + operator);
        }
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return operator.toString();
    }
}
