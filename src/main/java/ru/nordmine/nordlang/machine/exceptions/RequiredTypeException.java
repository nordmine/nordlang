package ru.nordmine.nordlang.machine.exceptions;

import ru.nordmine.nordlang.machine.value.ValueType;

public class RequiredTypeException extends RunException {

    public RequiredTypeException(ValueType requiredType, ValueType actualType) {
        super(requiredType + " type required, but was " + actualType);
    }
}
