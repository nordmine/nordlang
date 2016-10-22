package ru.nordmine.nordlang.machine;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.testng.Assert.assertEquals;

public class CharTest extends MachineTest {

    @DataProvider
    public Object[][] sourceDataProvider() {
        return new Object[][]{
                {"echo 'b';", "b"},
                {"char symbol = 'B'; echo symbol;", "B"},
                {"char[] message = ['T','e','s','t']; int i = 0; while(i < 4) { echo message[i]; i = i + 1;}", "Test"},
                {"char[] chars = ['a', 'b', 'c']; echo chars[1];", "b"},
                {"char[] message = ['T','e','s','t']; message[2] = 'x'; echo message;", "[T,e,x,t]"}
        };
    }

    @Test(dataProvider = "sourceDataProvider")
    public void compare(String source, String expectedOutput) throws LangException {
        assertEquals(getResult(source), expectedOutput);
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: incompatible types: char, int"
    )
    public void charIntConcat() throws LangException {
        getResult("echo '2' + 4;");
    }
}
