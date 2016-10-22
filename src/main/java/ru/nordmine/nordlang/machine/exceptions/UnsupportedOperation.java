package ru.nordmine.nordlang.machine.exceptions;

public class UnsupportedOperation extends RunException {

    public UnsupportedOperation(String operationName) {
        super("unsupported operation " + operationName);
    }
}
