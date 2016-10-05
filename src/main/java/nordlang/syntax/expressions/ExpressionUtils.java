package nordlang.syntax.expressions;

import nordlang.exceptions.SyntaxException;
import nordlang.lexer.Type;

public class ExpressionUtils {

    public static void typeError(int line, Type type1, Type type2) throws SyntaxException {
        throw new SyntaxException(line, "incompatible types: " + type1 + ", " + type2);
    }
}
