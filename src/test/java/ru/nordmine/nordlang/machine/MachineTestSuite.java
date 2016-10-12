package ru.nordmine.nordlang.machine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BoolTest.class,
        CharTest.class,
        IntTest.class,
        StringTest.class,
        MathTest.class,
        ScopeTest.class,
        StatementTest.class
})
public class MachineTestSuite {
}
