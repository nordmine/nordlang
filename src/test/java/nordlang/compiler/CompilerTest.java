package nordlang.compiler;

import nordlang.OutputTest;
import nordlang.exceptions.LangException;
import nordlang.exceptions.ParserException;
import nordlang.parser.api.MethodInfo;
import nordlang.parser.api.MethodReader;
import nordlang.parser.impl.MethodReaderImpl;
import nordlang.vm.api.Engine;
import nordlang.vm.api.Program;
import nordlang.vm.impl.EngineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CompilerTest extends OutputTest {

	@Test
	public void compileMath() throws LangException {
		List<String> statements = new LinkedList<String>();
		statements.add("def a = 10");
		statements.add("def b = 5");
		statements.add("a = (a - b) + 6 / 3");
		statements.add("b = a * 7");
		statements.add("show 'a=' + a");
		statements.add("show 'b=' + b");

		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setParams(new ArrayList<String>());
		methodInfo.setName("main");
		methodInfo.setStatements(statements);

		Compiler compiler = new CompilerImpl();
		Engine engine = new EngineImpl();
		engine.setProgram(compiler.compile(Arrays.asList(methodInfo)));
		engine.run();
		assertEquals("a=7" + newLine + "b=49" + newLine, output.toString());
	}

	@Test
	public void compileConcatenation() throws LangException {
		List<String> statements = new LinkedList<String>();
		statements.add("def title = 'duke'");
		statements.add("def name = 'Peter'");
		statements.add("show name + ' - ' + title");

		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setName("main");
		methodInfo.setParams(new ArrayList<String>());
		methodInfo.setStatements(statements);

		Compiler compiler = new CompilerImpl();
		Engine engine = new EngineImpl();
		engine.setProgram(compiler.compile(Arrays.asList(methodInfo)));
		engine.run();
		assertEquals("Peter - duke" + newLine, output.toString());
	}

	@Test(expected = ParserException.class)
	public void compileWithoutMainMethod() throws ParserException {
		MethodInfo firstMethod = new MethodInfo();
		firstMethod.setName("sin");

		MethodInfo secondMethod = new MethodInfo();
		secondMethod.setName("cos");

		Compiler compiler = new CompilerImpl();
		compiler.compile(Arrays.asList(firstMethod, secondMethod));
	}

	@Test
	public void compileIfBlockWithoutElse() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {" + newLine +
						"def a = 2;" + newLine +
						"if (a ==  4/2) { a = a + 5; }" + newLine +
						"show a;" + newLine +
						"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("7" + newLine, output.toString());
	}

	@Test(expected = ParserException.class)
	public void compileElseBlock() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {" + newLine +
						"def a = 2;" + newLine +
						"else { a = a + 5; }" + newLine +
						"show a;" + newLine +
						"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		compiler.compile(Arrays.asList(methodReader.readMethod()));
	}

	@Test
	public void compileIfBlockWithElse() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {" + newLine +
						"def a = 0;" + newLine +
						"if (a <> 0) { a = a * 5; }" + newLine +
						"else { a = a + 5; }" + newLine +
						"show a;" + newLine +
						"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("5" + newLine, output.toString());
	}

	@Test
	public void compileIfBlockInnerIfTrue() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {" + newLine +
						"def a = 2;" + newLine +
						"if (a == 2 and 5 > 4) { a = a + 3; if(a >= 5 or 2 > 1) {a = a * a * a;} }" + newLine +
						"else {a = a + 7;}" + newLine +
						"show a;" + newLine +
						"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("125"+newLine+"", output.toString());
	}

	@Test
	public void compileIfBlockInnerIfFalseWithoutElse() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {" + newLine +
						"def a = 2;" + newLine +
						"if (a >= 6 - 2 * 2) { a = a + 3; if(a > 5) {a = a * a * a;} }" + newLine +
						"else {a = a + 7;}" + newLine +
						"show a;" + newLine +
						"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("5" + newLine, output.toString());
	}

	@Test
	public void compileIfBlockInnerIfFalseWithElse() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {" + newLine +
						"def a = 2;" + newLine +
						"if (a <= 2) { " + newLine +
						"\ta = a + 3; " + newLine +
						"\tif(a-5 > 0) {" + newLine + "a = a * a * a;" + newLine + " } " + newLine +
						"\telse {" + newLine + "a = a * 12;" + newLine + "} " + newLine +
						"}" + newLine +
						"else {" + newLine + "a = a + 7;" + newLine + "}" + newLine +
						"show a;" + newLine +
						"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("60" + newLine, output.toString());
	}

	@Test
	public void compileWhileBlock() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {" + newLine +
						"def a = 2;" + newLine +
						"while (a <= 60) { a = a * 2; }" + newLine +
						"show a;" + newLine +
						"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("64" + newLine, output.toString());
	}

	@Test
	public void compileInnerWhileBlock() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {" + newLine +
						"def a = 1;" + newLine +
						"def b = 0;" +
						"def c = a;" +
						// todo исправить зацикливание, если убрать начало блока
						"while ( a <= 3 ) {" +
						"b = 3; c = 1;" +
						"while (b > 0) {" +
						"c = c * a; " +
						"b = b - 1;" +
						"}" + newLine +
						"show c;" +
						"a = a + 1;" + newLine +
						"}" +
						"c = 0;}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("1" + newLine + "8" + newLine + "27" + newLine, output.toString());
	}


}
