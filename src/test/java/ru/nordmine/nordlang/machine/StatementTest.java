package ru.nordmine.nordlang.machine;

import org.junit.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.junit.Assert.assertEquals;

public class StatementTest extends MachineTest {

    @Test
    public void oneLineComment() throws LangException {
        assertEquals("13", getResult("echo 1; // echo 2;\n echo 3;"));
    }

    @Test
    public void echoInWhile() throws LangException {
        assertEquals("1,2,3,4,5,", getResult("int i = 0; while(i < 5) { echo i + 1; echo ','; i=i+1;}"));
    }

    @Test
    public void echoInElse() throws LangException {
        assertEquals("8", getResult("if (5 > 6) { echo 7;} else {echo 8;}"));
    }

    @Test
    public void echoInIfWithElse() throws LangException {
        assertEquals("7", getResult("if (5 < 6) { echo 7;} else {echo 8;}"));
    }

    @Test
    public void echoInIf() throws LangException {
        assertEquals("78", getResult("if (5 < 6) { echo 7;} echo 8;"));
    }

    @Test
    public void breakInWhile() throws LangException {
        assertEquals("1,2,3,4,", getResult("int i = 1; while(i < 10) { if (i % 5 == 0) { break; } echo i; echo','; i = i + 1; }"));
    }

    @Test
    public void nonBoolValueInIf() throws LangException {
        try {
            getResult("int a = 1; if (a) {echo 2;}");
        } catch (SyntaxException e) {
            assertEquals("Syntax error at line 1: boolean value required in if", e.getMessage());
        }
    }

    @Test
    public void nonBoolValueInWhile() throws LangException {
        try {
            getResult("int a = 1;\nwhile (a)\n{echo 2;}");
        } catch (SyntaxException e) {
            assertEquals("Syntax error at line 2: boolean value required in while", e.getMessage());
        }
    }

    @Test
    public void conditionWithAnd() throws LangException {
        assertEquals("3", getResult("int a = 1; char b='B'; if (a >= 1 and b == 'B') { echo 3;} else {echo 4;}"));
    }

    @Test
    public void conditionWithOr() throws LangException {
        assertEquals("3", getResult("int a = 1; char b='B'; if (a < 0 or true) { echo 3;} else {echo 4;}"));
    }
}
