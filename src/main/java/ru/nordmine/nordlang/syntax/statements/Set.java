package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.ParserUtils;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.SetCommand;

public class Set extends Statement {

    private VariableExpression variable;
    public Expression expr;

    public Set(int line, VariableExpression variable, Expression expr) throws SyntaxException {
        super(line);
        this.variable = variable;
        this.expr = expr;
        if (ParserUtils.checkTypes(variable.getType(), expr.getType()) == null) {
            ParserUtils.typeError(getLine(), variable.getType(), expr.getType());
        }
    }

    @Override
    public void gen(Program program, int begin, int after) {
        expr.gen(program);
        program.add(new SetCommand(variable.getUniqueIndex()));
    }
}
