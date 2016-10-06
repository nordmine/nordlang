package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.exceptions.SyntaxException;
import ru.nordmine.nordlang.lexer.ArrayToken;
import ru.nordmine.nordlang.lexer.TypeToken;

public class StatementUtils {

    public static TypeToken checkTypes(TypeToken type1, TypeToken type2) {
        if (type1 instanceof ArrayToken || type2 instanceof ArrayToken) {
            return null;
        } else if (type1 == TypeToken.INT && type2 == TypeToken.INT) {
            return TypeToken.INT;
        } else if (type1 == TypeToken.BOOL && type2 == TypeToken.BOOL) {
            return TypeToken.BOOL;
        } else if (type1 == TypeToken.CHAR && type2 == TypeToken.CHAR) {
            return TypeToken.CHAR;
        } else {
            return null;
        }
    }

    public static void throwError(int line, String s) throws SyntaxException {
        throw new SyntaxException(line, s);
    }

    public static void typeError(int line, TypeToken type1, TypeToken type2) throws SyntaxException {
        throwError(line, "incompatible types: " + type1 + ", " + type2);
    }
}
