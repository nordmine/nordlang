package ru.nordmine.nordlang.machine.value;

import ru.nordmine.nordlang.machine.exceptions.RunException;

public class IntValue extends Value {

    final int value;

    public IntValue(int value) {
        this.value = value;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.INT;
    }

    @Override
    public String getAsString() {
        return Integer.toString(value);
    }

    @Override
    public boolean isEquals(Value right) throws RunException {
        checkTypeEquals(right);
        return this.value == ((IntValue)right).value;
    }

    @Override
    public boolean isGreaterThan(Value right) throws RunException {
        checkTypeEquals(right);
        return this.value > ((IntValue)right).value;
    }

    @Override
    public boolean isLesserThan(Value right) throws RunException {
        checkTypeEquals(right);
        return this.value < ((IntValue)right).value;
    }

    @Override
    public Value negate() throws RunException {
        return new IntValue((-1) * value);
    }

    @Override
    public Value plus(Value right) throws RunException {
        checkTypeEquals(right);
        return new IntValue(this.value + ((IntValue)right).value);
    }

    @Override
    public Value minus(Value right) throws RunException {
        checkTypeEquals(right);
        return new IntValue(this.value - ((IntValue)right).value);
    }

    @Override
    public Value multiple(Value right) throws RunException {
        checkTypeEquals(right);
        return new IntValue(this.value * ((IntValue)right).value);
    }

    @Override
    public Value division(Value right) throws RunException {
        checkTypeEquals(right);
        int rightValue = ((IntValue)right).value;
        if (rightValue == 0) {
            throw new RunException("divide by zero");
        }
        return new IntValue(this.value / rightValue);
    }

    @Override
    public Value mod(Value right) throws RunException {
        checkTypeEquals(right);
        int rightValue = ((IntValue)right).value;
        if (rightValue == 0) {
            throw new RunException("divide by zero");
        }
        return new IntValue(this.value % rightValue);
    }
}
