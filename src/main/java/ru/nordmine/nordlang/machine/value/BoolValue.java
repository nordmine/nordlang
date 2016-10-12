package ru.nordmine.nordlang.machine.value;

import ru.nordmine.nordlang.machine.exceptions.RunException;

public class BoolValue extends Value {

    public static final BoolValue FALSE = new BoolValue(false);
    public static final BoolValue TRUE = new BoolValue(true);

    final boolean value;

    private BoolValue(boolean value) {
        this.value = value;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.BOOL;
    }

    @Override
    public String getAsString() {
        return Boolean.toString(value);
    }

    @Override
    public Value negate() {
        return this.value ? FALSE : TRUE;
    }

    @Override
    public boolean isEquals(Value right) throws RunException {
        checkTypeEquals(right);
        BoolValue rightValue = (BoolValue)right;
        return this.value == rightValue.value;
    }
}
