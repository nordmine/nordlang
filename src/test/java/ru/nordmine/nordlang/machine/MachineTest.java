package ru.nordmine.nordlang.machine;

import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.lexer.StringLexer;
import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.syntax.MethodInfo;
import ru.nordmine.nordlang.syntax.ParserContext;
import ru.nordmine.nordlang.syntax.Signatures;
import ru.nordmine.nordlang.syntax.StatementParser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public abstract class MachineTest {

    protected final String getResult(String source) throws LangException {
        StatementParser parser = new StatementParser(new ParserContext(new StringLexer(source)), new Signatures(), new MethodInfo(TypeToken.INT, "main"));
        Program program = parser.createProgram();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Machine machine = new Machine(new PrintStream(output));

        machine.execute(program);
        return output.toString();
    }
}
