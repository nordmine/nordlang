package ru.nordmine.nordlang.machine;

import org.junit.Test;
import ru.nordmine.nordlang.exceptions.LangException;

import static org.junit.Assert.assertEquals;

public class BoolTest extends MachineTest {

    @Test
    public void echoBool() throws LangException {
        assertEquals("true", getResult("echo true;"));
    }

    @Test
    public void echoBoolVariable() throws LangException {
        assertEquals("true", getResult("bool flag = true; echo flag;"));
    }

    @Test
    public void echoBoolArray() throws LangException {
        assertEquals("[true,false]", getResult("bool[] flags = [true, false]; echo flags;"));
    }

    @Test
    public void getBoolByIndex() throws LangException {
        assertEquals("false", getResult("bool[] flags = [true, false]; echo flags[1];"));
    }

    @Test
    public void setBoolByIndex() throws LangException {
        assertEquals("[true,true]", getResult("bool[] flags = [true, false]; flags[1] = true; echo flags;"));
    }
}
