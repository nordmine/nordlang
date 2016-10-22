package ru.nordmine.nordlang.machine;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.testng.Assert.assertEquals;

public class IntTest extends MachineTest {

    @DataProvider
    public Object[][] sourceDataProvider() {
        return new Object[][]{
                {"echo 100;", "100"},
                {"int number = 10; echo number;", "10"},
                {"int[] v = [42,1]; echo v;", "[42,1]"},
                {"int[] v = [42,1, 15]; echo v[2];", "15"},
                {"int[] v = [42,1, 15]; v[2] = 18; echo v;", "[42,1,18]"}
        };
    }

    @Test(dataProvider = "sourceDataProvider")
    public void compare(String source, String expectedOutput) throws LangException {
        assertEquals(getResult(source), expectedOutput);
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: array type expected"
    )
    public void intGetByIndexError() throws LangException {
        getResult("int i = 100; echo i[2];");
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: incompatible types: int, char"
    )
    public void intCharConcat() throws LangException {
        getResult("echo 1 + '2';");
    }
}
