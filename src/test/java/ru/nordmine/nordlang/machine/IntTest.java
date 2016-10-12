package ru.nordmine.nordlang.machine;

import org.junit.Assert;
import org.junit.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.junit.Assert.assertEquals;

public class IntTest extends MachineTest {

    @Test
    public void echoInt() throws LangException {
        assertEquals("100", getResult("echo 100;"));
    }

    @Test
    public void echoIntVariable() throws LangException {
        assertEquals("10", getResult("int number = 10; echo number;"));
    }

    @Test
    public void echoIntArray() throws LangException {
        assertEquals("[42,1]", getResult("int[] v = [42,1]; echo v;"));
    }

    @Test
    public void getIntByIndex() throws LangException {
        assertEquals("15", getResult("int[] v = [42,1, 15]; echo v[2];"));
    }

    @Test
    public void setIntByIndex() throws LangException {
        assertEquals("[42,1,18]", getResult("int[] v = [42,1, 15]; v[2] = 18; echo v;"));
    }

    @Test
    public void intGetByIndexError() throws LangException {
        try {
            getResult("int i = 100; echo i[2];");
        } catch (SyntaxException e) {
            Assert.assertEquals("Syntax error at line 1: array type expected", e.getMessage());
        }
    }

    @Test
    public void intCharConcat() throws LangException {
        try {
            getResult("echo 1 + '2';");
        } catch (SyntaxException e) {
            assertEquals("Syntax error at line 1: incompatible types: int, char", e.getMessage());
        }
    }
}
