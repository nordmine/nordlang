package ru.nordmine.nordlang.syntax.expressions.logical;

import ru.nordmine.nordlang.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.commands.BinaryCommand;
import ru.nordmine.nordlang.lexer.Array;
import ru.nordmine.nordlang.lexer.Type;

public class Rel extends Logical {

    public Rel(int line, Token token, Expression leftExpr, Expression rightExpr) throws SyntaxException {
        super(line, token, leftExpr, rightExpr);
    }

    @Override
    public Type check(Type type1, Type type2) {
        if (type1 instanceof Array || type2 instanceof Array) {
            return null;
        } else if (type1 == type2) {
            return Type.BOOL;
        } else {
            return null;
        }
    }

    @Override
    public void jumping(Program program, int trueLabel, int falseLabel) {
        leftExpr.gen(program);
        rightExpr.gen(program);
        program.add(new BinaryCommand(operand.getTag()));
        emitJumps(program, trueLabel, falseLabel);
    }
}

