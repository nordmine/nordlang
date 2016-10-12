package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.commands.EchoCommand;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.machine.Program;

public class Echo extends Statement {

    private Expression expr;

    public Echo(int line, Expression expr) {
        super(line);
        this.expr = expr;
    }

    @Override
    public void gen(Program program, int b, int a) {
        expr.gen(program);
        program.add(new EchoCommand());
    }
}
