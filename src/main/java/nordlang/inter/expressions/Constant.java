package nordlang.inter.expressions;

import nordlang.lexer.types.Int;
import nordlang.lexer.Token;
import nordlang.lexer.types.ValueToken;
import nordlang.lexer.Word;
import nordlang.machine.Program;
import nordlang.lexer.Type;

public class Constant extends Expr {

    public Constant(int line, Token token, Type type) {
        super(line, token, type);
    }

    public Constant(int line, int i) {
        super(line, new Int(i), Type.INT);
    }

    public static final Constant True = new Constant(0, Word.TRUE, Type.BOOL);
    public static final Constant False = new Constant(0, Word.FALSE, Type.BOOL);

    @Override
    public void jumping(Program program, int trueLabel, int falseLabel) {
        if (this == True && trueLabel != 0) {
            program.addGotoCommand(trueLabel);
        } else if (this == False && falseLabel != 0) {
            program.addGotoCommand(falseLabel);
        }
    }

    @Override
    public void gen(Program program) {
        if (type == Type.INT || type == Type.CHAR) {
            ((ValueToken)operand).gen(program);
        } else if (this == True) {
            program.addPushCommand(1);
        } else if (this == False) {
            program.addPushCommand(0);
        }
    }

    @Override
    public String toString() {
        return "#" + operand.toString();
    }
}
