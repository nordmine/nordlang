package ru.nordmine.nordlang.machine;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.testng.Assert.assertEquals;

public class StatementTest extends MachineTest {

    @DataProvider
    public Object[][] sourceDataProvider() {
        return new Object[][]{
                {"echo 1; // echo 2;\n echo 3;", "13"},
                {"int i = 0; while(i < 5) { echo i + 1; echo ','; i=i+1;}", "1,2,3,4,5,"},
                {"if (5 > 6) { echo 7;} else {echo 8;}", "8"},
                {"if (5 < 6) { echo 7;} else {echo 8;}", "7"},
                {"if (5 < 6) { echo 7;} echo 8;", "78"},
                {"int i = 1; while(i < 10) { if (i % 5 == 0) { break; } echo i; echo','; i = i + 1; }", "1,2,3,4,"},
                {"int a = 1; char b='B'; if (a >= 1 and b == 'B') { echo 3;} else {echo 4;}", "3"},
                {"int a = 1; char b='B'; if (a < 0 or true) { echo 3;} else {echo 4;}", "3"},
                {"string text = \"Hello\"; echo size(text);", "5"},
                {"int[] matrix = [100,200,300]; echo size(matrix);", "3"},
                {"string[] matrix = [\"one\",\"two\"]; int len = size(matrix); echo len;", "2"},
                {"bool b = true; if (b) { echo 'a'; } echo 'b';", "ab"},
                {"bool[] flags = [true, true]; if (flags[1]) { echo 'a';} echo 'b';", "ab"}
        };
    }

    @Test(dataProvider = "sourceDataProvider")
    public void compare(String source, String expectedOutput) throws LangException {
        assertEquals(getResult(source), expectedOutput);
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: boolean value required in if"
    )
    public void nonBoolValueInIf() throws LangException {
        getResult("int a = 1; if (a) {echo 2;}");
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 2: boolean value required in while"
    )
    public void nonBoolValueInWhile() throws LangException {
        getResult("\nwhile (1 + 1)\n{echo 2;}");
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: string or array required for size operator, but was int"
    )
    public void sizeOfIntValue() throws LangException {
        getResult("int a = 1; echo size(a);}");
    }
}
