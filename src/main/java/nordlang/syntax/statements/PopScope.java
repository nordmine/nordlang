package nordlang.syntax.statements;

import nordlang.machine.Program;
import nordlang.machine.commands.PopScopeCommand;

public class PopScope extends Statement {

    public PopScope(int line) {
        super(line);
    }

    @Override
    public void gen(Program program, int b, int a) {
        program.add(new PopScopeCommand());
    }
}
