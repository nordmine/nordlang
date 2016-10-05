package nordlang.syntax;

import nordlang.syntax.expressions.VariableExpression;
import nordlang.lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class ParserScope {

    private Map<Token, VariableExpression> table;
    private ParserScope prev;

    public ParserScope(ParserScope parserScope) {
        table = new HashMap<>();
        prev = parserScope;
    }

    public void put(Token token, VariableExpression variable) {
        table.put(token, variable);
    }

    public VariableExpression get(Token token) {
        for (ParserScope e = this; e != null; e = e.prev) {
            VariableExpression found = e.table.get(token);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}
