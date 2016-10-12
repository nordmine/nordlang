package ru.nordmine.nordlang.machine.value;

import ru.nordmine.nordlang.machine.exceptions.RunException;

public class StringValue extends Value {

    final StringBuilder value;

    public StringValue(StringBuilder value) {
        this.value = value;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.STRING;
    }

    @Override
    public String getAsString() {
        return value.toString();
    }

    @Override
    public boolean isEquals(Value right) throws RunException {
        checkTypeEquals(right);
        return this.value.equals(((StringValue)right).value);
    }

    @Override
    public Value plus(Value right) throws RunException {
        return new StringValue(new StringBuilder(value).append(right.getAsString()));
    }

    @Override
    public Value getByIndex(Value index) throws RunException {
        checkForType(ValueType.INT, index);
        int indexValue = ((IntValue)index).value;
        if (value.length() == 0 || indexValue < 0 || indexValue > value.length() - 1) {
            throw new RunException("Index out of bound");
        }
        return new CharValue(value.charAt(indexValue));
    }

    @Override
    public void setByIndex(Value index, Value newValue) throws RunException {
        checkForType(ValueType.INT, index);
        int indexValue = ((IntValue)index).value;
        if (value.length() == 0 || indexValue < 0 || indexValue > value.length() - 1) {
            throw new RunException("Index out of bound");
        }
        checkForType(ValueType.CHAR, newValue);
        value.setCharAt(indexValue, ((CharValue)newValue).value);
    }
}
