package nordlang.syntax.statements;

import nordlang.syntax.expressions.Expression;
import nordlang.machine.Program;
import nordlang.machine.commands.EchoBoolCommand;
import nordlang.machine.commands.EchoCharCommand;
import nordlang.machine.commands.EchoIntCommand;
import nordlang.lexer.Type;

public class Echo extends Statement {

    private Expression expr;

    public Echo(int line, Expression expr) {
        super(line);
        this.expr = expr;
    }

    @Override
    public void gen(Program program, int b, int a) {
        expr.gen(program);
        if (expr.getType() == Type.INT) {
            program.add(new EchoIntCommand());
        } else if (expr.getType() == Type.CHAR) {
            program.add(new EchoCharCommand());
        } else if (expr.getType() == Type.BOOL) {
            program.add(new EchoBoolCommand());
        }
    }
}
