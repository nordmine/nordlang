package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class ParserScope {

    private static int uniqueIndexSequence = 2000;

    private Map<Token, VariableExpression> table;
    private ParserScope prev;

    public ParserScope(ParserScope parserScope) {
        table = new HashMap<>();
        prev = parserScope;
    }

    public int getUniqueIndexSequence() {
        return uniqueIndexSequence;
    }

    public void put(Token token, VariableExpression variable) {
        table.put(token, variable);
        uniqueIndexSequence++;
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
