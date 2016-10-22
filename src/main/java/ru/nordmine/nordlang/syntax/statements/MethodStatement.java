package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.PushScopeCommand;
import ru.nordmine.nordlang.machine.commands.SetCommand;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;

import java.util.List;

public class MethodStatement extends Statement {

    private final Statement bodyStatement;
    private final List<VariableExpression> paramExprList;

    public MethodStatement(int line, List<VariableExpression> paramExprList, Statement bodyStatement) {
        super(line);
        this.bodyStatement = bodyStatement;
        this.paramExprList = paramExprList;
    }

    @Override
    public void gen(Program program, int begin, int after) {
        for (int i = paramExprList.size(); i > 0; i--) {
            program.add(new SetCommand(paramExprList.get(i - 1).getUniqueIndex()));
        }
        bodyStatement.gen(program, begin, after);
    }
}
