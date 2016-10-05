package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.exceptions.SyntaxException;
import ru.nordmine.nordlang.lexer.Array;
import ru.nordmine.nordlang.lexer.Type;

public class StatementUtils {

    public static Type checkTypes(Type type1, Type type2) {
        if (type1 instanceof Array || type2 instanceof Array) {
            return null;
        } else if (type1 == Type.INT && type2 == Type.INT) {
            return Type.INT;
        } else if (type1 == Type.BOOL && type2 == Type.BOOL) {
            return Type.BOOL;
        } else if (type1 == Type.CHAR && type2 == Type.CHAR) {
            return Type.CHAR;
        } else {
            return null;
        }
    }

    public static void throwError(int line, String s) throws SyntaxException {
        throw new SyntaxException(line, s);
    }

    public static void typeError(int line, Type type1, Type type2) throws SyntaxException {
        throwError(line, "incompatible types: " + type1 + ", " + type2);
    }
}
