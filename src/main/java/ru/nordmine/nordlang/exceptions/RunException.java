package ru.nordmine.nordlang.exceptions;

public class RunException extends LangException {

    public RunException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Runtime error: " + super.getMessage();
    }
}
