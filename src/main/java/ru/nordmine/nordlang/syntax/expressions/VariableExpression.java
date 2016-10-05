package ru.nordmine.nordlang.syntax.expressions;

import ru.nordmine.nordlang.lexer.Word;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.GetCommand;
import ru.nordmine.nordlang.lexer.Type;

public class VariableExpression extends Expression {

    private final int offset;

    public VariableExpression(int line, Word id, Type type, int offset) {
        super(line, id, type);
        this.offset = offset;
    }

    @Override
    public void gen(Program program) {
        program.add(new GetCommand(operand.getUniqueIndex()));
    }
}
