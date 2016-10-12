package ru.nordmine.nordlang.machine.exceptions;

import ru.nordmine.nordlang.exceptions.LangException;

public class RunException extends LangException {

    public RunException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Runtime error: " + super.getMessage();
    }
}
