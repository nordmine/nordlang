package ru.nordmine.nordlang.machine;

import org.junit.Assert;
import org.junit.Test;
import ru.nordmine.nordlang.OutputTest;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.lexer.Lexer;
import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.syntax.Parser;

import static org.junit.Assert.assertEquals;

public class MachineTest extends OutputTest {

    @Test
    public void basicMath() throws LangException {
        assertEquals("-3", getResult("echo 1 + 2*3 - 20 / 2;"));
    }

    @Test
    public void unaryMinus() throws LangException {
        assertEquals("-4", getResult("echo -4;"));
    }

    @Test
    public void divideByZero() throws LangException {
        try {
            getResult("echo 100 / 0;");
        } catch (RunException e) {
            assertEquals("Runtime error: divide by zero", e.getMessage());
        }
    }

    @Test
    public void modDivide() throws LangException {
        assertEquals("2", getResult("echo 5 % 3;"));
    }

    @Test
    public void modDivideByZero() throws LangException {
        try {
            getResult("echo 100 % 0;");
        } catch (RunException e) {
            assertEquals("Runtime error: divide by zero", e.getMessage());
        }
    }

    @Test
    public void echoChar() throws LangException {
        assertEquals("b", getResult("echo 'b';"));
    }

    @Test
    public void echoBool() throws LangException {
        assertEquals("true", getResult("echo true;"));
    }

    @Test
    public void defineInt() throws LangException {
        assertEquals("10", getResult("int number = 10; echo number;"));
    }

    @Test
    public void defineChar() throws LangException {
        assertEquals("B", getResult("char symbol = 'B'; echo symbol;"));
    }

    @Test
    public void expressionWithVariables() throws LangException {
        assertEquals("105", getResult("int a = 10; int b = a / 2; int c = b + a * a; echo c;"));
    }

    @Test
    public void echoInWhile() throws LangException {
        assertEquals("1,2,3,4,5,", getResult("int i = 0; while(i < 5) { echo i + 1; echo ','; i=i+1;}"));
    }

    @Test
    public void echoInElse() throws LangException {
        assertEquals("8", getResult("if (5 > 6) { echo 7;} else {echo 8;}"));
    }

    @Test
    public void echoInIfWithElse() throws LangException {
        assertEquals("7", getResult("if (5 < 6) { echo 7;} else {echo 8;}"));
    }

    @Test
    public void echoInIf() throws LangException {
        assertEquals("78", getResult("if (5 < 6) { echo 7;} echo 8;"));
    }

//    @Test
    public void defineIntArray() throws LangException {
        assertEquals("13,21", getResult("int[][] values = [[11,12,13],[21,22,23]]; echo values[0][2]; echo ','; echo values[1][0];"));
    }

    @Test
    public void defineCharArray() throws LangException {
        assertEquals("Test", getResult("char[] message = ['T','e','s','t']; int i = 0; while(i < 4) { echo message[i]; i = i + 1;}"));
    }

    @Test
    public void echoBoolArray() throws LangException {
        assertEquals("[true,false]", getResult("bool[] flags = [true, false]; echo flags;"));
    }

    @Test
    public void echoIntArray() throws LangException {
        assertEquals("[42,1]", getResult("int[] v = [42,1]; echo v;"));
    }

//    @Test
    public void defineCharArrayByString() throws LangException {
        assertEquals("test", getResult("char[] message = \"test\"; int i = 0; while(i < 4) { echo message[i]; i = i + 1;}"));
    }

    @Test
    public void setElementsInArray() throws LangException {
        assertEquals("45", getResult("int[] v = [3,4];\necho v[1];\nv[1] = 5;\necho v[1];"));
    }

    @Test
    public void oneLineComment() throws LangException {
        assertEquals("13", getResult("echo 1; // echo 2;\n echo 3;"));
    }

    @Test
    public void undefinedVariable() throws LangException {
        try {
            getResult("echo var;");
        } catch (SyntaxException e) {
            assertEquals("Syntax error at line 1: variable 'var' is not defined", e.getMessage());
        }
    }

    @Test
    public void incompatibleTypesIntChar() throws LangException {
        try {
            getResult("echo 1 + '2';");
        } catch (SyntaxException e) {
            assertEquals("Syntax error at line 1: incompatible types: int, char", e.getMessage());
        }
    }

    @Test
    public void nonBoolValueInIf() throws LangException {
        try {
            getResult("int a = 1; if (a) {echo 2;}");
        } catch (SyntaxException e) {
            assertEquals("Syntax error at line 1: boolean value required in if", e.getMessage());
        }
    }

    @Test
    public void nonBoolValueInWhile() throws LangException {
        try {
            getResult("int a = 1;\nwhile (a)\n{echo 2;}");
        } catch (SyntaxException e) {
            assertEquals("Syntax error at line 2: boolean value required in while", e.getMessage());
        }
    }

    @Test
    public void conditionWithAnd() throws LangException {
        assertEquals("3", getResult("int a = 1; char b='B'; if (a >= 1 and b == 'B') { echo 3;} else {echo 4;}"));
    }

    @Test
    public void conditionWithOr() throws LangException {
        assertEquals("3", getResult("int a = 1; char b='B'; if (a < 0 or true) { echo 3;} else {echo 4;}"));
    }

    @Test
    public void innerScope() throws LangException {
        assertEquals("1 2", getResult("int a = 1; int b = 2; { int b = 3; a = a * a; b = b * b; } echo a; echo ' '; echo b;"));
    }

    @Test
    public void breakInWhile() throws LangException {
        assertEquals("1,2,3,4,", getResult("int i = 1; while(i < 10) { if (i % 5 == 0) { break; } echo i; echo','; i = i + 1; }"));
    }

    @Test
    public void echoString() throws LangException {
        assertEquals("Some text", getResult("echo \"Some text\";"));
    }

    @Test
    public void echoStringVariable() throws LangException {
        assertEquals("Some text", getResult("string message = \"Some text\"; echo message;"));
    }

    @Test
    public void stringConcat() throws LangException {
        assertEquals("Hello, Bob", getResult("string name = \"Bob\"; echo \"Hello, \" + name;"));
    }

    @Test
    public void stringNumberConcat() throws LangException {
        assertEquals("Number is 100500", getResult("int number = 100500; echo \"Number is \" + number;"));
    }

    @Test
    public void stringBoolConcat() throws LangException {
        assertEquals("Flag is false", getResult("bool flag = false; echo \"Flag is \" + flag;"));
    }

    @Test
    public void stringGetByIndex() throws LangException {
        assertEquals("s", getResult("string message = \"Test\"; echo message[2];"));
    }

    @Test
    public void stringSetByIndex() throws LangException {
        assertEquals("Text", getResult("string message = \"Test\"; message[2] = 'x'; echo message;"));
    }

    @Test
    public void intGetByIndex() throws LangException {
        try {
            getResult("int i = 100; echo i[2];");
        } catch (SyntaxException e) {
            Assert.assertEquals("Syntax error at line 1: array type expected", e.getMessage());
        }
    }

    private String getResult(String source) throws LangException {
        Lexer lexer = new Lexer(String.format("int main() { %s }", source));
        Parser parser = new Parser(lexer);
        Program program = parser.createProgram();
        Machine machine = new Machine();
        machine.execute(program);
        return output.toString();
    }
}
