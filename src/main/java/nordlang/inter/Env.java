package nordlang.inter;

import nordlang.inter.expressions.Id;
import nordlang.lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class Env {

    private Map<Token, Id> table;
    private Env prev;

    public Env(Env env) {
        table = new HashMap<>();
        prev = env;
    }

    public void put(Token token, Id id) {
        table.put(token, id);
    }

    public Id get(Token token) {
        for (Env e = this; e != null; e = e.prev) {
            Id found = e.table.get(token);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}
