package nordlang.inter;

import nordlang.exceptions.ParserException;
import nordlang.lexer.Type;

public abstract class Node {

    private int line = 0;

    public Node(int line) {
        this.line = line;
    }

    protected void error(String s) throws ParserException {
        throw new ParserException(line, s);
    }

    protected void typeError(Type type1, Type type2) throws ParserException {
        error("incompatible types: " + type1 + ", " + type2);
    }
}
