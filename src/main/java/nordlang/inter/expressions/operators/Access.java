package nordlang.inter.expressions.operators;

import nordlang.inter.expressions.Expr;
import nordlang.inter.expressions.Id;
import nordlang.lexer.Tag;
import nordlang.lexer.Word;
import nordlang.machine.Program;
import nordlang.machine.commands.AccessCommand;
import nordlang.lexer.Type;

public class Access extends Expr {

    private Id array;
    private Expr index;

    public Access(int line, Id array, Expr index, Type type) {
        super(line, new Word(Tag.INDEX, "[]"), type);
        this.array = array;
        this.index = index;
    }

    public Id getArray() {
        return array;
    }

    public Expr getIndex() {
        return index;
    }

    @Override
    public void gen(Program program) {
        index.gen(program);
        program.add(new AccessCommand(array.toString()));
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
