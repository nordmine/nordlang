package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.lexer.ArrayToken;
import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.machine.value.*;

public class ParserUtils {

    public static TypeToken checkTypes(TypeToken type1, TypeToken type2) {
        if (type1 instanceof ArrayToken || type2 instanceof ArrayToken) {
            return null;
        } else if (type1 == TypeToken.INT && type2 == TypeToken.INT) {
            return TypeToken.INT;
        } else if (type1 == TypeToken.BOOL && type2 == TypeToken.BOOL) {
            return TypeToken.BOOL;
        } else if (type1 == TypeToken.CHAR && type2 == TypeToken.CHAR) {
            return TypeToken.CHAR;
        } else if (type1 == TypeToken.STRING || type2 == TypeToken.STRING) {
            return TypeToken.STRING;
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

    public static Value getInitialValueByToken(TypeToken typeToken) throws SyntaxException {
        if (typeToken == TypeToken.BOOL) {
            return BoolValue.FALSE;
        } else if (typeToken == TypeToken.CHAR) {
            return new CharValue(' ');
        } else if (typeToken == TypeToken.INT) {
            return new IntValue(0);
        } else if (typeToken == TypeToken.STRING) {
            return new StringValue(new StringBuilder());
        } else {
            throw new SyntaxException("Unknown typeToken: " + typeToken);
        }
    }
}
