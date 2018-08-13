package ru.nordmine.nordlang.syntax.expressions;

import ru.nordmine.nordlang.lexer.types.IntValueToken;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.lexer.types.ValueToken;
import ru.nordmine.nordlang.lexer.WordToken;
import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.machine.value.BoolValue;

public class ConstantExpression extends Expression {

    public ConstantExpression(Token token, TypeToken type) {
        super(token, type);
    }

    public ConstantExpression(int line, int i) {
        super(new IntValueToken(i, line), TypeToken.INT);
    }

    public static final ConstantExpression TRUE = new ConstantExpression(WordToken.TRUE, TypeToken.BOOL);
    public static final ConstantExpression FALSE = new ConstantExpression(WordToken.FALSE, TypeToken.BOOL);

    @Override
    public void jumping(Program program, Label trueLabel, Label falseLabel) {
        if (this == TRUE && trueLabel != Label.EMPTY) {
            program.addGotoCommand(trueLabel);
        } else if (this == FALSE && falseLabel != Label.EMPTY) {
            program.addGotoCommand(falseLabel);
        }
    }

    @Override
    public void gen(Program program) {
        if (type == TypeToken.INT || type == TypeToken.CHAR || type == TypeToken.STRING) {
            ((ValueToken)operand).gen(program);
        } else if (this == TRUE) {
            program.addPushCommand(BoolValue.TRUE);
        } else if (this == FALSE) {
            program.addPushCommand(BoolValue.FALSE);
        }
    }

    @Override
    public String toString() {
        return "#" + operand.toString();
    }
}
