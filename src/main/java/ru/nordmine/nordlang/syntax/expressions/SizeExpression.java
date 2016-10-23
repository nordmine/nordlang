package ru.nordmine.nordlang.syntax.expressions;

import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.lexer.WordToken;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.SizeCommand;

public class SizeExpression extends Expression {

    private final Expression expression;

    public SizeExpression(int line, Expression expression) {
        super(line, WordToken.SIZE, TypeToken.INT);
        this.expression = expression;
    }

    @Override
    public void gen(Program program) {
        expression.gen(program);
        program.add(new SizeCommand());
    }
}
