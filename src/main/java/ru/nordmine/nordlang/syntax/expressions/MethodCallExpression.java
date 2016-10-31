package ru.nordmine.nordlang.syntax.expressions;

import ru.nordmine.nordlang.lexer.WordToken;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.MethodCallCommand;
import ru.nordmine.nordlang.syntax.MethodInfo;

import java.util.List;

public class MethodCallExpression extends Expression {

    private final MethodInfo methodInfo;
    private final List<Expression> paramExpressions;

    public MethodCallExpression(int line, WordToken id, MethodInfo methodInfo, List<Expression> paramExpressions) {
        super(line, id, methodInfo.getReturnType());
        this.methodInfo = methodInfo;
        this.paramExpressions = paramExpressions;
    }

    @Override
    public void gen(Program program) {
        for (Expression expr : paramExpressions) {
            expr.gen(program);
        }
        program.add(new MethodCallCommand(methodInfo.getBeginLabel()));
    }
}
