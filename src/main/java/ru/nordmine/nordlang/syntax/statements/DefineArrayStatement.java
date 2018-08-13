package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.DefineArrayCommand;
import ru.nordmine.nordlang.machine.value.Value;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;

public class DefineArrayStatement extends Statement {

    // todo по возможности заменить variableExpression на nameIndex в подобных Statement'ах
    private final VariableExpression variable;
    private final Value initialValue;

    public DefineArrayStatement(VariableExpression variable, Value initialValue) {
        this.variable = variable;
        this.initialValue = initialValue;
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        program.add(new DefineArrayCommand(variable.getUniqueIndex(), initialValue));
    }
}
