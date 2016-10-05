package nordlang.syntax;

import nordlang.exceptions.SyntaxException;
import nordlang.exceptions.RunException;
import nordlang.lexer.Lexer;
import nordlang.machine.Machine;
import nordlang.machine.Program;
import nordlang.machine.commands.Command;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void test() throws SyntaxException, RunException {
        Lexer lexer = new Lexer(SOURCE);
        Parser parser = new Parser(lexer);
        Program program = parser.createProgram();
        List<Command> commands = program.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            Command cmd = commands.get(i);
            System.out.println(String.format("% 4d     %s", i, cmd));
        }

        Machine machine = new Machine();
        machine.execute(program);
    }

    private static final String SOURCE =
            "int main() {\n" +
            "int i = 0;\n" +
            "int j = 0;\n" +
//            "int magic = 8;\n" +
//            "char c = 'B';\n" +
//            "char[] name = \"Привет, мир!\";\n" +
            "int[][] values = [[10,-5,12],[20,21,22]];\n" +
            "while (i < 2) {\n" +
            "j = 0;\n" +
            "while (j < 3) {\n" +
            "echo values[i][j];\n" +
            "echo ',';\n" +
            "j = j + 1;\n" +
            "}\n" +
            "echo '|';\n" +
            "i = i + 1;\n" +
//            "v = 4 ;\n" +
            "}\n" +
            "}\n";
}
