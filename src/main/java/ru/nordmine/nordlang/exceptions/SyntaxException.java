package ru.nordmine.nordlang.exceptions;

public class SyntaxException extends LangException {

    private int line = 0;

    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(int line, String message) {
        super(message);
        this.line = line;
    }

    @Override
    public String getMessage() {
        return String.format("Syntax error at line %s: %s", line, super.getMessage());
    }
}
