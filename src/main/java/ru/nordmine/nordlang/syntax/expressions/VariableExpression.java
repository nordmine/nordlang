package ru.nordmine.nordlang.syntax.expressions;

import ru.nordmine.nordlang.lexer.WordToken;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.GetCommand;
import ru.nordmine.nordlang.lexer.TypeToken;

public class VariableExpression extends Expression {

    // виртуальная машина оперирует индексами переменных вместо имён
    private final int uniqueIndex;

    public VariableExpression(WordToken id, TypeToken type, int uniqueIndex) {
        super(id, type);
        this.uniqueIndex = uniqueIndex;
    }

    public int getUniqueIndex() {
        return uniqueIndex;
    }

    @Override
    public void gen(Program program) {
        program.add(new GetCommand(uniqueIndex));
    }
}
