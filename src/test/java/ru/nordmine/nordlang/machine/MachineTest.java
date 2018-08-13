package ru.nordmine.nordlang.machine;

import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.SourceParser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

abstract class MachineTest {

    protected final String getResult(String source) throws LangException {
        SourceParser parser = new SourceParser("int main() { " + source + " return 0; }");
        Program program = parser.createProgram();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Machine machine = new Machine(new PrintStream(output));

        machine.execute(program);
        return output.toString();
    }
}
