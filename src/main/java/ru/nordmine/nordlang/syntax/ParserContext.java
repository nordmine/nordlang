package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.Lexer;
import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

public class ParserContext {

    private final Lexer lexer;
    private Token look; // предпросмотр
    private int line = 0;

    public ParserContext(Lexer lexer) {
        this.lexer = lexer;
    }

    public void move() throws SyntaxException {
        look = lexer.nextToken();
        line = lexer.getLine();
    }

    private void error(String s) throws SyntaxException {
        throw new SyntaxException(lexer.getLine(), s);
    }

    public void match(Tag t) throws SyntaxException {
        if (look.getTag() == t) {
            move();
        } else {
            error(String.format("Tag %s is expected, but was %s", t, look.getTag()));
        }
    }

    public Tag getTag() {
        return this.look.getTag();
    }
}
