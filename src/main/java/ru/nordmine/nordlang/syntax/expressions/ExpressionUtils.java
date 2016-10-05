package ru.nordmine.nordlang.syntax.expressions;

import ru.nordmine.nordlang.exceptions.SyntaxException;
import ru.nordmine.nordlang.lexer.Type;

public class ExpressionUtils {

    public static void typeError(int line, Type type1, Type type2) throws SyntaxException {
        throw new SyntaxException(line, "incompatible types: " + type1 + ", " + type2);
    }
}
