package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;

public class Statement {

    public static final Statement EMPTY = new Statement(0);
    public static Statement Enclosing = Statement.EMPTY; // todo какое-то шаманство со статичным полем

    private final int line;

    public Statement(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    protected Label after = Label.EMPTY;

    // вызывается с метками начала и после конструкции
    public void gen(Program program, Label begin, Label after) {}
}
