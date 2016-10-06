package ru.nordmine.nordlang.syntax.expressions.operators;

import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.lexer.WordToken;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.AccessCommand;
import ru.nordmine.nordlang.lexer.TypeToken;

public class Access extends Expression {

    private VariableExpression array;
    private Expression index;

    public Access(int line, VariableExpression array, Expression index, TypeToken type) {
        super(line, new WordToken(Tag.INDEX, "[]"), type);
        this.array = array;
        this.index = index;
    }

    public VariableExpression getArray() {
        return array;
    }

    public Expression getIndex() {
        return index;
    }

    @Override
    public void gen(Program program) {
        index.gen(program);
        program.add(new AccessCommand(array.getUniqueIndex()));
    }

    @Override
    public void jumping(Program program, int trueLabel, int falseLabel) {
        emitJumps(program, trueLabel, falseLabel);
    }

    @Override
    public String toString() {
        return array.toString() + " [" + index.toString() + " ]";
    }
}
