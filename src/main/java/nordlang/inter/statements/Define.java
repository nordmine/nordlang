package nordlang.inter.statements;

import nordlang.inter.expressions.Id;
import nordlang.machine.Program;
import nordlang.machine.commands.DefineCommand;

public class Define extends Statement {

    private Id id;

    public Define(int line, Id id) {
        super(line);
        this.id = id;
    }

    @Override
    public void gen(Program program, int b, int a) {
        program.add(new DefineCommand(id.getOperand().toString(), id.getType().getWidth()));
    }
}
