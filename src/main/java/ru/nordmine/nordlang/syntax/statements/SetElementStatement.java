package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.operators.AccessExpression;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.SetElemCommand;

public class SetElementStatement extends Statement {

    private VariableExpression array;
    private Expression index;
    private Expression expr;

    public SetElementStatement(AccessExpression x, Expression expr) throws SyntaxException {
        this.array = x.getArray();
        this.index = x.getIndex();
        this.expr = expr;
        if (ParserUtils.checkTypes(x.getType(), expr.getType()) == null) {
            ParserUtils.typeError(expr.getLine(), x.getType(), expr.getType());
        }
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        expr.gen(program);
        index.gen(program);
        program.add(new SetElemCommand(array.getUniqueIndex()));
    }
}
