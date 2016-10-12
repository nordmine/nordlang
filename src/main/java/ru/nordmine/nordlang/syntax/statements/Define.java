package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.DefineCommand;
import ru.nordmine.nordlang.machine.value.Value;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;

public class Define extends Statement {

    private final VariableExpression variable;
    private final Value initialValue;

    public Define(int line, VariableExpression variable, Value initialValue) {
        super(line);
        this.variable = variable;
        this.initialValue = initialValue;
    }

    @Override
    public void gen(Program program, int b, int a) {
        program.add(new DefineCommand(variable.getUniqueIndex(), initialValue));
    }
}