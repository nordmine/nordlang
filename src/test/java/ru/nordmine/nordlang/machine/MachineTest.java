package ru.nordmine.nordlang.machine;

import ru.nordmine.nordlang.OutputTest;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.lexer.Lexer;
import ru.nordmine.nordlang.syntax.Parser;

public abstract class MachineTest extends OutputTest {

    protected String getResult(String source) throws LangException {
        Lexer lexer = new Lexer(String.format("int main() { %s }", source));
        Parser parser = new Parser(lexer);
        Program program = parser.createProgram();
        Machine machine = new Machine();
        machine.execute(program);
        return output.toString();
    }
}
