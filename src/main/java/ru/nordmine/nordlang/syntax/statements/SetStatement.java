package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.SetCommand;

public class SetStatement extends Statement {

    private VariableExpression variable;
    public Expression expr;

    public SetStatement(VariableExpression variable, Expression expr) throws SyntaxException {
        this.variable = variable;
        this.expr = expr;
        if (ParserUtils.checkTypes(variable.getType(), expr.getType()) == null) {
            ParserUtils.typeError(expr.getLine(), variable.getType(), expr.getType());
        }
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        expr.gen(program);
        program.add(new SetCommand(variable.getUniqueIndex()));
    }
}
