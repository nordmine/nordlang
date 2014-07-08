package nordlang.vm;

import nordlang.OutputTest;
import nordlang.exceptions.RunException;
import nordlang.vm.api.Engine;
import nordlang.vm.api.Program;
import nordlang.vm.impl.EngineImpl;
import nordlang.vm.impl.ProgramImpl;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class EngineTest extends OutputTest {

	@Test
	public void basicMath() throws RunException {
		Program program = new ProgramImpl();
		String tempVariable = UUID.randomUUID().toString();
		program.addCommand(CommandType.GET, 0);
		program.addCommand(CommandType.DEF, tempVariable);
		// def a = 2;
		program.addCommand(CommandType.GET, 2);
		program.addCommand(CommandType.DEF, "a");
		// a = 10 / (7 - a); 10 7 a - /
		program.addCommand(CommandType.GET, 10);
		program.addPushCommand();

		program.addCommand(CommandType.GET, 7);
		program.addPushCommand();

		program.addCommand(CommandType.GET, "$a");
		program.addPushCommand();

		program.addPopCommand();
		program.addCommand(CommandType.SET, tempVariable);

		program.addPopCommand();
		program.addCommand(CommandType.MINUS, "$" + tempVariable);
		program.addPushCommand();

		program.addPopCommand();
		program.addCommand(CommandType.SET, tempVariable);

		program.addPopCommand();
		program.addCommand(CommandType.DIV, "$" + tempVariable);
		program.addPushCommand();

		program.addPopCommand();
		program.addCommand(CommandType.SET, "a");

		// show a;
		program.addCommand(CommandType.GET, "$a");
		program.addShowCommand();

		program.addCommand(CommandType.GET, tempVariable);
		program.addUnDefCommand();

		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();

		assertEquals("2\n", output.toString());
	}

	@Test(expected = RunException.class)
	public void divideByZero() throws RunException {
		Program program = new ProgramImpl();
		// show 2 / 0;
		program.addCommand(CommandType.GET, 2);
		program.addCommand(CommandType.DIV, 0);
		program.addShowCommand();

		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
	}

	@Test
	public void twoVariables() throws RunException {
		Program program = new ProgramImpl();
		// def a = 5;
		program.addCommand(CommandType.GET, 5);
		program.addCommand(CommandType.DEF, "a");
		// def b = 11;
		program.addCommand(CommandType.GET, 11);
		program.addCommand(CommandType.DEF, "b");
		// echo a;
		program.addCommand(CommandType.GET, "$a");
		program.addShowCommand();
		// echo b;
		program.addCommand(CommandType.GET, "$b");
		program.addShowCommand();
		// a = a + 2;
		program.addCommand(CommandType.GET, "$a");
		program.addCommand(CommandType.PLUS, 2);
		program.addCommand(CommandType.SET, "a");
		// b = b + 2;
		program.addCommand(CommandType.GET, "$b");
		program.addCommand(CommandType.MUL, 2);
		program.addCommand(CommandType.SET, "b");
		// show a;
		program.addCommand(CommandType.GET, "$a");
		program.addShowCommand();
		// show b;
		program.addCommand(CommandType.GET, "$b");
		program.addShowCommand();

		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();

		assertEquals("5\n11\n7\n22\n", output.toString());
	}

	@Test
	public void concatenations() throws RunException {
		Program program = new ProgramImpl();
		// def a = 'a=' + 5;
		program.addCommand(CommandType.GET, "a=");
		program.addCommand(CommandType.PLUS, 5);
		program.addCommand(CommandType.DEF, "a");
		// show a;
		program.addCommand(CommandType.GET, "$a");
		program.addShowCommand();
		// def b = 1 + ' item';
		program.addCommand(CommandType.GET, 1);
		program.addCommand(CommandType.PLUS, " item");
		program.addCommand(CommandType.DEF, "b");
		// show b;
		program.addCommand(CommandType.GET, "$b");
		program.addShowCommand();
		// def c = 'Петя' + ' - ' + 'барон';
		program.addCommand(CommandType.GET, "Петя");
		program.addCommand(CommandType.PLUS, " - ");
		program.addCommand(CommandType.PLUS, "барон");
		program.addCommand(CommandType.DEF, "c");
		// show c;
		program.addCommand(CommandType.GET, "$c");
		program.addShowCommand();

		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();

		assertEquals("a=5\n1 item\nПетя - барон\n", output.toString());
	}

	@Test
	public void jumpCommand() throws RunException {
		Program program = new ProgramImpl();
		// def a = 3;
		program.addCommand(CommandType.GET, 3);
		program.addCommand(CommandType.DEF, "a");
		// goto label;
		program.addCommand(CommandType.JUMP, 6);
		// a = a + 5;
		program.addCommand(CommandType.GET, "$a");
		program.addCommand(CommandType.PLUS, 5);
		program.addCommand(CommandType.SET, "a");
		// label: a = a + 7;
		program.addCommand(CommandType.GET, "$a");
		program.addCommand(CommandType.PLUS, 7);
		program.addCommand(CommandType.SET, "a");
		// show a;
		program.addCommand(CommandType.GET, "$a");
		program.addShowCommand();

		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();

		assertEquals("10\n", output.toString());
	}

}
