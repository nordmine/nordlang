package ru.nordmine.nordlang.machine.exceptions;

import ru.nordmine.nordlang.machine.value.ValueType;

public class IncompatibleTypesException extends RunException {

    public IncompatibleTypesException(ValueType left, ValueType right) {
        super(String.format("Incompatible types: %s, %s", left, right));
    }
}
