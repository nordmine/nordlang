package ru.nordmine.nordlang.machine;

import org.junit.Test;
import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.machine.exceptions.RunException;

import static org.junit.Assert.assertEquals;

public class MathTest extends MachineTest {

    @Test
    public void basicMath() throws LangException {
        assertEquals("-3", getResult("echo 1 + 2*3 - 20 / 2;"));
    }

    @Test
    public void unaryMinus() throws LangException {
        assertEquals("-4", getResult("echo -4;"));
    }

    @Test
    public void divideByZero() throws LangException {
        try {
            getResult("echo 100 / 0;");
        } catch (RunException e) {
            assertEquals("Runtime error: divide by zero", e.getMessage());
        }
    }

    @Test
    public void modDivide() throws LangException {
        assertEquals("2", getResult("echo 5 % 3;"));
    }

    @Test
    public void modDivideByZero() throws LangException {
        try {
            getResult("echo 100 % 0;");
        } catch (RunException e) {
            assertEquals("Runtime error: divide by zero", e.getMessage());
        }
    }

    @Test
    public void expressionWithVariables() throws LangException {
        assertEquals("105", getResult("int a = 10; int b = a / 2; int c = b + a * a; echo c;"));
    }
}
