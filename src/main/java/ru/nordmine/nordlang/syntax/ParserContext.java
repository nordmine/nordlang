package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import java.util.List;

public class ParserContext {

    private final List<Token> tokens;
    private int index = -1;
    private Token look; // предпросмотр

    public ParserContext(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void move() {
        index++;
        look = tokens.get(index);
    }

    public void match(Tag t) throws SyntaxException {
        if (look.getTag() == t) {
            move();
        } else {
            error(String.format("Tag %s is expected, but was %s", t, look.getTag()), look.getLine());
        }
    }

    private void error(String s, int line) throws SyntaxException {
        throw new SyntaxException(line, s);
    }

    public Tag getTag() {
        return this.look.getTag();
    }

    public Token getLook() {
        return look;
    }

    public void resetIndex() {
        index = 0;
        look = tokens.get(index);
    }
}
