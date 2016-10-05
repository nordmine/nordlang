package nordlang.syntax.statements;

import nordlang.syntax.expressions.VariableExpression;
import nordlang.machine.Program;
import nordlang.machine.commands.DefineCommand;

public class Define extends Statement {

    private VariableExpression variable;

    public Define(int line, VariableExpression variable) {
        super(line);
        this.variable = variable;
    }

    @Override
    public void gen(Program program, int b, int a) {
        program.add(new DefineCommand(variable.getOperand().getUniqueIndex(), variable.getType().getWidth()));
    }
}
