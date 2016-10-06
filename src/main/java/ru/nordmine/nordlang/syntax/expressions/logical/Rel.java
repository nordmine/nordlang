package ru.nordmine.nordlang.syntax.expressions.logical;

import ru.nordmine.nordlang.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.BinaryCommand;
import ru.nordmine.nordlang.lexer.ArrayToken;
import ru.nordmine.nordlang.lexer.TypeToken;

public class Rel extends Logical {

    public Rel(int line, Token token, Expression left, Expression right) throws SyntaxException {
        super(line, token, left, right);
    }

    @Override
    protected TypeToken check(TypeToken type1, TypeToken type2) {
        if (type1 instanceof ArrayToken || type2 instanceof ArrayToken) {
            return null;
        } else if (type1 == type2) {
            return TypeToken.BOOL;
        } else {
            return null;
        }
    }

    @Override
    public void jumping(Program program, int trueLabel, int falseLabel) {
        left.gen(program);
        right.gen(program);
        program.add(new BinaryCommand(operand.getTag()));
        emitJumps(program, trueLabel, falseLabel);
    }
}

