package ru.nordmine.nordlang.machine;

import org.junit.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.junit.Assert.assertEquals;

public class ScopeTest extends MachineTest {

    @Test
    public void undefinedVariable() throws LangException {
        try {
            getResult("echo var;");
        } catch (SyntaxException e) {
            assertEquals("Syntax error at line 1: variable 'var' is not defined", e.getMessage());
        }
    }

    @Test
    public void innerScope() throws LangException {
        assertEquals("1 2", getResult("int a = 1; int b = 2; { int b = 3; a = a * a; b = b * b; } echo a; echo ' '; echo b;"));
    }
}
