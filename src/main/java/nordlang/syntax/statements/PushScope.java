package nordlang.syntax.statements;

import nordlang.machine.Program;
import nordlang.machine.commands.PushScopeCommand;

public class PushScope extends Statement {

    public PushScope(int line) {
        super(line);
    }

    @Override
    public void gen(Program program, int b, int a) {
        program.add(new PushScopeCommand());
    }
}
