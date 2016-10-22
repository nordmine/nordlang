package ru.nordmine.nordlang.machine;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.machine.exceptions.RunException;

import static org.testng.Assert.assertEquals;

public class MathTest extends MachineTest {

    @DataProvider
    public Object[][] sourceDataProvider() {
        return new Object[][]{
                {"echo 1 + 2*3 - 20 / 2;", "-3"},
                {"echo -4;", "-4"},
                {"echo 5 % 3;", "2"},
                {"int a = 10; int b = a / 2; int c = b + a * a; echo c;", "105"}
        };
    }

    @Test(dataProvider = "sourceDataProvider")
    public void compare(String source, String expectedOutput) throws LangException {
        assertEquals(getResult(source), expectedOutput);
    }

    @Test(
            expectedExceptions = RunException.class,
            expectedExceptionsMessageRegExp = "Runtime error: divide by zero"
    )
    public void divideByZero() throws LangException {
        getResult("echo 100 / 0;");
    }

    @Test(
            expectedExceptions = RunException.class,
            expectedExceptionsMessageRegExp = "Runtime error: divide by zero"
    )
    public void modDivideByZero() throws LangException {
        getResult("echo 100 % 0;");
    }
}
