package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.DefineCommand;

public class Define extends Statement {

    private VariableExpression variable;

    public Define(int line, VariableExpression variable) {
        super(line);
        this.variable = variable;
    }

    @Override
    public void gen(Program program, int b, int a) {
        program.add(new DefineCommand(variable.getUniqueIndex(), variable.getType().getWidth()));
    }
}
