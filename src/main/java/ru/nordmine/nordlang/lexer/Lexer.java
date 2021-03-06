package ru.nordmine.nordlang.lexer;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import java.util.List;

public interface Lexer {

    Token nextToken() throws SyntaxException;

    List<Token> getAllTokens() throws SyntaxException;
}
