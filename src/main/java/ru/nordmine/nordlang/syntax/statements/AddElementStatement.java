package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.AddElementCommand;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;

public class AddElementStatement extends Statement {

    private final VariableExpression variable;
    private final Expression expr;

    public AddElementStatement(VariableExpression variable, Expression expr) {
        this.variable = variable;
        this.expr = expr;
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        expr.gen(program);
        program.add(new AddElementCommand(variable.getUniqueIndex()));
    }
}
