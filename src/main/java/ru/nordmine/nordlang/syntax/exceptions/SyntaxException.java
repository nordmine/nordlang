package ru.nordmine.nordlang.syntax.exceptions;

import ru.nordmine.nordlang.exceptions.LangException;

public class SyntaxException extends LangException {

    private int line = -1;

    public SyntaxException(int line, String message) {
        super(message);
        this.line = line;
    }

    @Override
    public String getMessage() {
        return String.format("Syntax error at line %s: %s", line, super.getMessage());
    }
}
