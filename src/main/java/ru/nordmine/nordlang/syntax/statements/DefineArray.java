package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.DefineArrayCommand;
import ru.nordmine.nordlang.machine.value.Value;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;

public class DefineArray extends Statement {

    private final VariableExpression variable;
    private final Value initialValue;
    private final int initialSize;

    public DefineArray(int line, VariableExpression variable, int initialSize, Value initialValue) {
        super(line);
        this.variable = variable;
        this.initialValue = initialValue;
        this.initialSize = initialSize;
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        program.add(new DefineArrayCommand(variable.getUniqueIndex(), initialSize, initialValue));
    }
}
