package ru.nordmine.nordlang.machine;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.nordmine.nordlang.exceptions.LangException;

import static org.testng.Assert.assertEquals;

public class BoolTest extends MachineTest {

    @DataProvider
    public Object[][] sourceDataProvider() {
        return new Object[][]{
                {"echo true;", "true"},
                {"bool flag = true; echo flag;", "true"},
                {"bool[] flags = [true, false]; echo flags;", "[true,false]"},
                {"bool[] flags = [true, false]; echo flags[1];", "false"},
                {"bool[] flags = [true, false]; flags[1] = true; echo flags;", "[true,true]"}
        };
    }

    @Test(dataProvider = "sourceDataProvider")
    public void echoBool(String source, String expectedOutput) throws LangException {
        assertEquals(getResult(source), expectedOutput);
    }
}
