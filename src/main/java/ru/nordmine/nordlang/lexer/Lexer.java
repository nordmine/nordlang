package ru.nordmine.nordlang.lexer;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

public interface Lexer {

    int getLine();

    Token nextToken() throws SyntaxException;
}
