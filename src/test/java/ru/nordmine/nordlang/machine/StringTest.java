package ru.nordmine.nordlang.machine;

import org.junit.Test;
import ru.nordmine.nordlang.exceptions.LangException;

import static org.junit.Assert.assertEquals;

public class StringTest extends MachineTest {

    @Test
    public void echoString() throws LangException {
        assertEquals("Some text", getResult("echo \"Some text\";"));
    }

    @Test
    public void echoStringVariable() throws LangException {
        assertEquals("Some text", getResult("string message = \"Some text\"; echo message;"));
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
    public void stringConcat() throws LangException {
        assertEquals("Hello, Bob", getResult("string name = \"Bob\"; echo \"Hello, \" + name;"));
    }

    @Test
    public void stringBoolConcat() throws LangException {
        assertEquals("Flag is false", getResult("bool flag = false; echo \"Flag is \" + flag;"));
    }

    @Test
    public void stringCharConcat() throws LangException {
        assertEquals("Texts", getResult("char c = 's'; echo \"Text\" + c;"));
    }

    @Test
    public void stringIntConcat() throws LangException {
        assertEquals("Number is 100500", getResult("int number = 100500; echo \"Number is \" + number;"));
    }
}
