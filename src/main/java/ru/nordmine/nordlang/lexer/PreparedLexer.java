package ru.nordmine.nordlang.lexer;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import java.util.List;

public class PreparedLexer implements Lexer {

    private final List<Token> tokens;
    private int tokenIndex = 0;

    public PreparedLexer(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public int getLine() {
        return 0; // todo
    }

    @Override
    public Token nextToken() throws SyntaxException {
        Token token = tokens.get(tokenIndex);
        tokenIndex++;
        return token;
    }
}
