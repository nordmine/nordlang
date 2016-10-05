package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Program;

public class Statement {

    private final int line;

    public Statement(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public static final Statement Empty = new Statement(0);
    public static Statement Enclosing = Statement.Empty; // todo какое-то шаманство со статичным полем

    protected int after = 0;

    // вызывается с метками начала и после конструкции
    public void gen(Program program, int b, int a) {}
}
