package ru.nordmine.nordlang.machine.value;

import ru.nordmine.nordlang.machine.exceptions.IncompatibleTypesException;
import ru.nordmine.nordlang.machine.exceptions.RequiredTypeException;
import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.exceptions.UnsupportedOperation;

public abstract class Value {

    public abstract ValueType getValueType();

    public abstract String getAsString();

    public abstract boolean isEquals(Value right) throws RunException;

    public boolean isGreaterThan(Value right) throws RunException {
        throw new UnsupportedOperation("isGreaterThan");
    }

    public boolean isLesserThan(Value right) throws RunException {
        throw new UnsupportedOperation("isLesserThan");
    }

    public Value negate() throws RunException {
        throw new UnsupportedOperation("negate");
    }

    public Value getByIndex(Value index) throws RunException {
        throw new UnsupportedOperation("getByIndex");
    }

    public void setByIndex(Value index, Value newValue) throws RunException {
        throw new UnsupportedOperation("setByIndex");
    }

    public Value plus(Value right) throws RunException {
        throw new UnsupportedOperation("plus");
    }

    public Value minus(Value right) throws RunException {
        throw new UnsupportedOperation("minus");
    }

    public Value multiple(Value right) throws RunException {
        throw new UnsupportedOperation("multiple");
    }

    public Value division(Value right) throws RunException {
        throw new UnsupportedOperation("division");
    }

    public Value mod(Value right) throws RunException {
        throw new UnsupportedOperation("mod");
    }

    protected final void checkTypeEquals(Value right) throws RunException {
        if (this.getValueType() != right.getValueType()) {
            throw new IncompatibleTypesException(this.getValueType(), right.getValueType());
        }
    }

    protected final void checkForType(ValueType requiredType, Value right) throws RunException {
        if (requiredType != right.getValueType()) {
            throw new RequiredTypeException(requiredType, right.getValueType());
        }
    }
}
