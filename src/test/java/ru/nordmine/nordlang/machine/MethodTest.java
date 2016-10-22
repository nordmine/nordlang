package ru.nordmine.nordlang.machine;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.testng.Assert.assertEquals;

public class MethodTest extends MachineTest {

    @DataProvider
    public Object[][] sourceDataProvider() {
        return new Object[][]{
                {"string getMessage() { return \"Превед!\"; } int main() { echo getMessage(); return 0; }", "Превед!"},
                {"int main() { echo getMessage(); return 0; } string getMessage() { return \"Превед!\"; }", "Превед!"},
                {"string getMessage() { return \"Превед\"; } int main() { string message = getMessage() + '!';" +
                        " echo message; return 0; }", "Превед!"},
                {"int main() { echo quad(3); return 0; } int quad(int val) { return val * val; }", "9"},
                {"int main() { echo pow(5,4); return 0; } " +
                        "int pow(int val, int power) { int i = 1; int result = val; " +
                        "while(i < power) { result = result * val; i = i + 1; }" +
                        "return result; }", "625"},
                {"int main() { echo report(\"square: \", 45); return 0; } " +
                        "string report(string message, int square) { return message + square; }", "square: 45"},
                {"int main() { echo max(100, max(50, 150)); return 0; } " +
                        "int max(int a, int b) { if (a > b) {return a;} else {return b;} }", "150"},
                {"int main() { echo quad(5); return 0;} " +
                        "int pow(int val, int power) { int i = 1; int result = val; " +
                        "while(i < power) { result = result * val; i = i + 1; } return result; } " +
                        "int quad(int val) { return pow(val, 2); }", "25"},
                {"int main() { echo abs(100 - 500); return 0; } " +
                        "int abs(int val) { if (val < 0) {val = -1 * val;} return val; }", "400"},
        };
    }

    @Test(dataProvider = "sourceDataProvider")
    public void compare(String source, String expectedOutput) throws LangException {
        assertEquals(getResult(source), expectedOutput);
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: method 'test\\(\\)' is not defined"
    )
    public void methodNotFound() throws LangException {
        getResult("int main() { return test(); }");
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: method should return int, but was bool"
    )
    public void badReturnType() throws LangException {
        getResult("int main() { return false; }");
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: missing return statement"
    )
    public void missingReturnStatement() throws LangException {
        getResult("int main() {}");
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: method 'int main\\(\\)' not found"
    )
    public void methodMainNotFound() throws LangException {
        getResult("string getMessage() { return \"Превед\"; }");
    }

    @Override
    protected String wrapSource(String source) {
        return source;
    }
}