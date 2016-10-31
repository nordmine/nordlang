package ru.nordmine.nordlang.syntax.statements;

import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.syntax.MethodInfo;
import ru.nordmine.nordlang.syntax.expressions.Expression;

public class ReturnStatement extends Statement {

    private final Expression expression;
    private final MethodInfo methodInfo;

    public ReturnStatement(int line, Expression expression, MethodInfo methodInfo) {
        super(line);
        this.expression = expression;
        this.methodInfo = methodInfo;
    }

    @Override
    public void gen(Program program, Label begin, Label after) {
        expression.gen(program);
        if (methodInfo.getName().equals("main")) {
            program.addExitCommand();
        } else {
            program.addReturnCommand();
        }
    }
}
