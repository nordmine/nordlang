package ru.nordmine.nordlang.machine.value;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.exceptions.UnsupportedOperation;

import java.util.ArrayList;
import java.util.List;

public class ListValue extends Value {

    final List<Value> values = new ArrayList<>();

    // многомерный массив - абстракция над одномерным
    final ValueType elementValueType;

    public ListValue(Value initialValue) {
        this.elementValueType = initialValue.getValueType();
    }

    @Override
    public IntValue size() throws RunException {
        return new IntValue(values.size());
    }

    @Override
    public ValueType getValueType() {
        return ValueType.LIST;
    }

    @Override
    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Value value : values) {
            sb.append(value.getAsString()).append(',');
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    @Override
    public boolean isEquals(Value right) throws RunException {
        throw new UnsupportedOperation("isEquals");
    }

    @Override
    public Value plus(Value right) throws RunException {
        throw new UnsupportedOperation("plus");
    }

    @Override
    public void addElement(Value element) throws RunException {
        checkForType(elementValueType, element);
        values.add(element);
    }

    @Override
    public Value getByIndex(Value index) throws RunException {
        checkForType(ValueType.INT, index);
        int indexValue = ((IntValue)index).value;
        if (values.size() == 0 || indexValue < 0 || indexValue > values.size() - 1) {
            throw new RunException("Index out of bound");
        }
        return values.get(indexValue);
    }

    @Override
    public void setByIndex(Value index, Value newValue) throws RunException {
        checkForType(ValueType.INT, index);
        int indexValue = ((IntValue)index).value;
        if (values.size() == 0 || indexValue < 0 || indexValue > values.size() - 1) {
            throw new RunException("Index out of bound");
        }
        checkForType(elementValueType, newValue);
        values.set(indexValue, newValue);
    }
}
