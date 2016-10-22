package ru.nordmine.nordlang.machine;

import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.Parser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public abstract class MachineTest {

    protected String wrapSource(String source) {
        return String.format("int main() { %s return 0; }", source);
    }

    protected final String getResult(String source) throws LangException {
        Parser parser = new Parser(wrapSource(source));
        Program program = parser.createProgram();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Machine machine = new Machine(new PrintStream(output));

        machine.execute(program);
        return output.toString();
    }
}
