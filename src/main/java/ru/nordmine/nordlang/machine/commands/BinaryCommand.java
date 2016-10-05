package ru.nordmine.nordlang.machine.commands;

import ru.nordmine.nordlang.exceptions.RunException;
import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.machine.MachineState;

import java.util.Stack;

public class BinaryCommand extends Command {

    private Tag operator;

    public BinaryCommand(Tag operator) {
        this.operator = operator;
    }

    @Override
    public void execute(MachineState state) throws RunException {
        Stack<Integer> valueStack = state.getValueStack();
        int right = valueStack.pop();
        int left = valueStack.pop();
        switch (operator) {
            case PLUS: valueStack.push(left + right); break;
            case MINUS: valueStack.push(left - right); break;
            case MUL: valueStack.push(left * right); break;
            case DIVISION:
                if (right == 0) {
                    throw new RunException("divide by zero");
                }
                valueStack.push(left / right);
                break;
            case MOD:
                if (right == 0) {
                    throw new RunException("divide by zero");
                }
                valueStack.push(left % right);
                break;
            case EQUAL: valueStack.push(left == right? 1 : 0); break;
            case NOT_EQUAL: valueStack.push(left != right? 1 : 0); break;
            case LESS: valueStack.push(left < right? 1 : 0); break;
            case LESS_OR_EQUAL: valueStack.push(left <= right? 1 : 0); break;
            case GREATER: valueStack.push(left > right? 1 : 0); break;
            case GREATER_OR_EQUAL: valueStack.push(left >= right? 1 : 0); break;
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
