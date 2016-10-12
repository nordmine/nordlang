package ru.nordmine.nordlang.machine;

import org.junit.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.junit.Assert.assertEquals;

public class CharTest extends MachineTest {

    @Test
    public void echoChar() throws LangException {
        assertEquals("b", getResult("echo 'b';"));
    }

    @Test
    public void echoCharVariable() throws LangException {
        assertEquals("B", getResult("char symbol = 'B'; echo symbol;"));
    }

    @Test
    public void echoCharArray() throws LangException {
        assertEquals("Test", getResult("char[] message = ['T','e','s','t']; int i = 0; while(i < 4) { echo message[i]; i = i + 1;}"));
    }

    @Test
    public void getCharByIndex() throws LangException {
        assertEquals("b", getResult("char[] chars = ['a', 'b', 'c']; echo chars[1];"));
    }

    @Test
    public void setCharByIndex() throws LangException {
        assertEquals("[T,e,x,t]", getResult("char[] message = ['T','e','s','t']; message[2] = 'x'; echo message;"));
    }

    @Test
    public void charIntConcat() throws LangException {
        try {
            getResult("echo '2' + 4;");
        } catch (SyntaxException e) {
            assertEquals("Syntax error at line 1: incompatible types: char, int", e.getMessage());
        }
    }
}
