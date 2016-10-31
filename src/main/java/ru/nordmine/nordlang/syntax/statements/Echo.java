package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
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
    public void gen(Program program, Label begin, Label after) {
        expr.gen(program);
        program.add(new EchoCommand());
    }
}
