package ru.nordmine.nordlang.machine;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.testng.Assert.assertEquals;

public class StringTest extends MachineTest {

    @DataProvider
    public Object[][] sourceDataProvider() {
        return new Object[][]{
                {"echo \"Some text\";", "Some text"},
                {"string message = \"Some text\"; echo message;", "Some text"},
                {"string message = \"Test\"; echo message[2];", "s"},
                {"string message = \"Test\"; message[2] = 'x'; echo message;", "Text"},
                {"string name = \"Bob\"; echo \"Hello, \" + name;", "Hello, Bob"},
                {"bool flag = false; echo \"Flag is \" + flag;", "Flag is false"},
                {"char c = 's'; echo \"Text\" + c;", "Texts"},
                {"int number = 100500; echo \"Number is \" + number;", "Number is 100500"},
                {"echo newLine;", System.getProperty("line.separator")},
                {"echo \"New line\" + newLine;", String.format("New line%n")},
                {"string[] names = [\"Петя\"]; names[] = \"Вася\"; echo names;", "[Петя,Вася]"}
        };
    }

    @Test(dataProvider = "sourceDataProvider")
    public void compare(String source, String expectedOutput) throws LangException {
        assertEquals(getResult(source), expectedOutput);
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: incompatible types: string, bool"
    )
    public void incompatibleTypesArrayDefinition() throws LangException {
        getResult("string[] names = [\"Петя\",true];");
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: incompatible types: string, char"
    )
    public void incompatibleTypesArrayAddElement() throws LangException {
        getResult("string[] names = [\"Петя\"]; names[] = 'c';");
    }
}
