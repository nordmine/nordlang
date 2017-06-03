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
                {"int[] v = [42,1, 15]; v[2] = 18; echo v;", "[42,1,18]"},
                {"int[] v1 = [42,1, 15]; int[] v2 = [3,4]; v1[2] = v2[1]; echo v1;", "[42,1,4]"},
                {"int a = 3;\na++;\necho a;", "4"},
                {"int a = 3;\na--;\necho a;", "2"},
                {"int[] ar = [11]; ar[] = 22; echo ar;", "[11,22]"}
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

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: incompatible types: int, bool"
    )
    public void incompatibleTypesArrayAssign() throws LangException {
        getResult("int[] v1 = [42,1, 15]; bool[] v2 = [true, true]; v1[2] = v2[1];");
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: incompatible types: int, bool"
    )
    public void incompatibleTypesArrayDefinition() throws LangException {
        getResult("int[] v1 = [42,1,true];");
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: incompatible types: int, char"
    )
    public void incompatibleTypesArrayAddElement() throws LangException {
        getResult("int[] v1 = [42,1]; v1[] = 'c';");
    }
}
