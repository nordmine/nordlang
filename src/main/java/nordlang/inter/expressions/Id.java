package nordlang.inter.expressions;

import nordlang.lexer.Word;
import nordlang.machine.Program;
import nordlang.machine.commands.GetCommand;
import nordlang.lexer.Type;

public class Id extends Expr {

    private final int offset;

    public Id(int line, Word id, Type type, int offset) {
        super(line, id, type);
        this.offset = offset;
    }

    @Override
    public void gen(Program program) {
        program.add(new GetCommand(operand.toString()));
    }
}
