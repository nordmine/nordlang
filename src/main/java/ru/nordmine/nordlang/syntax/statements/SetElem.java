package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.operators.Access;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.SetElemCommand;

public class SetElem extends Statement {

    private VariableExpression array;
    private Expression index;
    private Expression expr;

    public SetElem(int line, Access x, Expression expr) throws SyntaxException {
        super(line);
        this.array = x.getArray();
        this.index = x.getIndex();
        this.expr = expr;
        if (ParserUtils.checkTypes(x.getType(), expr.getType()) == null) {
            ParserUtils.typeError(getLine(), x.getType(), expr.getType());
        }
    }

    @Override
    public void gen(Program program, int begin, int after) {
        expr.gen(program);
        index.gen(program);
        program.add(new SetElemCommand(array.getUniqueIndex()));
    }
}
