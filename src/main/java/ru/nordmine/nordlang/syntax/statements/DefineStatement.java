package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.DefineCommand;
import ru.nordmine.nordlang.machine.value.Value;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;

public class DefineStatement extends Statement {

    private final VariableExpression variable;
    private final Value initialValue;

    public DefineStatement(VariableExpression variable, Value initialValue) {
        this.variable = variable;
        this.initialValue = initialValue;
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        program.add(new DefineCommand(variable.getUniqueIndex(), initialValue));
    }
}
