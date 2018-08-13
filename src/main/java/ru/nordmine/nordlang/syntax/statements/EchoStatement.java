package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.commands.EchoCommand;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.machine.Program;

public class EchoStatement extends Statement {

    private Expression expr;

    public EchoStatement(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        expr.gen(program);
        program.add(new EchoCommand());
    }
}
