package nordlang.syntax.statements;

import nordlang.exceptions.SyntaxException;
import nordlang.syntax.expressions.Expression;
import nordlang.syntax.expressions.VariableExpression;
import nordlang.machine.Program;
import nordlang.machine.commands.SetCommand;

public class Set extends Statement {

    private VariableExpression variable;
    public Expression expr;

    public Set(int line, VariableExpression variable, Expression expr) throws SyntaxException {
        super(line);
        this.variable = variable;
        this.expr = expr;
        if (StatementUtils.checkTypes(variable.getType(), expr.getType()) == null) {
            StatementUtils.typeError(getLine(), variable.getType(), expr.getType());
        }
    }

    @Override
    public void gen(Program program, int b, int a) {
        expr.gen(program);
        program.add(new SetCommand(variable.getOperand().getUniqueIndex()));
    }
}
