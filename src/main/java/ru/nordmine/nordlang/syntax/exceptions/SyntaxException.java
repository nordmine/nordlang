package ru.nordmine.nordlang.syntax.exceptions;

import ru.nordmine.nordlang.exceptions.LangException;

public class SyntaxException extends LangException {

    private Integer line;

    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(int line, String message) {
        super(message);
        this.line = line;
    }

    @Override
    public String getMessage() {
        if (line == null) {
            return String.format("Syntax error: %s", super.getMessage());
        }
        return String.format("Syntax error at line %s: %s", line, super.getMessage());
    }
}
