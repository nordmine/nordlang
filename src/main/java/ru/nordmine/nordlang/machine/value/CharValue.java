package ru.nordmine.nordlang.machine.value;

import ru.nordmine.nordlang.machine.exceptions.RunException;

public class CharValue extends Value {

    final char value;

    public CharValue(char value) {
        this.value = value;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.CHAR;
    }

    @Override
    public String getAsString() {
        return Character.toString(value);
    }

    @Override
    public boolean isEquals(Value right) throws RunException {
        checkTypeEquals(right);
        return this.value == ((CharValue)right).value;
    }
}
