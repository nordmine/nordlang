package nordlang.syntax.statements;

import nordlang.exceptions.SyntaxException;
import nordlang.syntax.expressions.operators.Access;
import nordlang.syntax.expressions.Expression;
import nordlang.syntax.expressions.VariableExpression;
import nordlang.machine.Program;
import nordlang.machine.commands.SetElemCommand;

public class SetElem extends Statement {

    private VariableExpression array;
    private Expression index;
    private Expression expr;

    public SetElem(int line, Access x, Expression expr) throws SyntaxException {
        super(line);
        this.array = x.getArray();
        this.index = x.getIndex();
        this.expr = expr;
        if (StatementUtils.checkTypes(x.getType(), expr.getType()) == null) {
            StatementUtils.typeError(getLine(), x.getType(), expr.getType());
        }
    }

    @Override
    public void gen(Program program, int b, int a) {
        expr.gen(program);
        index.gen(program);
        program.add(new SetElemCommand(array.getOperand().getUniqueIndex()));
    }
}
