package ru.nordmine.nordlang.machine;

import org.testng.annotations.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.testng.Assert.assertEquals;

public class ScopeTest extends MachineTest {

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: variable 'var' is not defined"
    )
    public void undefinedVariable() throws LangException {
        getResult("echo var;");
    }

    @Test
    public void innerScope() throws LangException {
        assertEquals("1 2", getResult("int a = 1; int b = 2; { int b = 3; a = a * a; b = b * b; } echo a; echo ' '; echo b;"));
    }
}
