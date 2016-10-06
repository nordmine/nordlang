package ru.nordmine.nordlang.syntax.expressions;

import ru.nordmine.nordlang.exceptions.SyntaxException;
import ru.nordmine.nordlang.lexer.TypeToken;

public class ExpressionUtils {

    public static void typeError(int line, TypeToken type1, TypeToken type2) throws SyntaxException {
        throw new SyntaxException(line, "incompatible types: " + type1 + ", " + type2);
    }
}
