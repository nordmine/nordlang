package nordlang.syntax.expressions.operators;

import nordlang.syntax.expressions.Expression;
import nordlang.syntax.expressions.VariableExpression;
import nordlang.lexer.Tag;
import nordlang.lexer.Word;
import nordlang.machine.Program;
import nordlang.machine.commands.AccessCommand;
import nordlang.lexer.Type;

public class Access extends Expression {

    private VariableExpression array;
    private Expression index;

    public Access(int line, VariableExpression array, Expression index, Type type) {
        super(line, new Word(Tag.INDEX, "[]"), type);
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
        program.add(new AccessCommand(array.getOperand().getUniqueIndex()));
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
