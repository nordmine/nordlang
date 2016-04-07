package nordlang.inter.statements;

import nordlang.inter.Node;
import nordlang.machine.Program;

public class Statement extends Node {

    public Statement(int line) {
        super(line);
    }

    public static final Statement Empty = new Statement(0);
    public static Statement Enclosing = Statement.Empty;

    protected int after = 0;

    // вызывается с метками начала и после конструкции
    public void gen(Program program, int b, int a) {}
}
