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
		assertEquals("a=7\nb=49\n", output.toString());
	}

	@Test
	public void compileConcatenation() throws LangException {
		List<String> statements = new LinkedList<String>();
		statements.add("def title = 'барон'");
		statements.add("def name = 'Петя'");
		statements.add("show name + ' - ' + title");

		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setName("main");
		methodInfo.setParams(new ArrayList<String>());
		methodInfo.setStatements(statements);

		Compiler compiler = new CompilerImpl();
		Engine engine = new EngineImpl();
		engine.setProgram(compiler.compile(Arrays.asList(methodInfo)));
		engine.run();
		assertEquals("Петя - барон\n", output.toString());
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
				"def main() {\n" +
				"def a = 2;\n" +
				"if (a ==  4/2) { a = a + 5; }\n" +
				"show a;\n" +
				"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("7\n", output.toString());
	}

	@Test(expected = ParserException.class)
	public void compileElseBlock() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {\n" +
					"def a = 2;\n" +
					"else { a = a + 5; }\n" +
					"show a;\n" +
				"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		compiler.compile(Arrays.asList(methodReader.readMethod()));
	}

	@Test
	public void compileIfBlockWithElse() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {\n" +
					"def a = 0;\n" +
					"if (a <> 0) { a = a * 5; }\n" +
					"else { a = a + 5; }\n" +
					"show a;\n" +
				"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("5\n", output.toString());
	}

	@Test
	public void compileIfBlockInnerIfTrue() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
			"def main() {\n" +
				"def a = 2;\n" +
				"if (a == 2 and 5 > 4) { a = a + 3; if(a >= 5 or 2 > 1) {a = a * a * a;} }\n" +
				"else {a = a + 7;}\n" +
				"show a;\n" +
			"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("125\n", output.toString());
	}

	@Test
	public void compileIfBlockInnerIfFalseWithoutElse() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
			"def main() {\n" +
				"def a = 2;\n" +
				"if (a >= 6 - 2 * 2) { a = a + 3; if(a > 5) {a = a * a * a;} }\n" +
				"else {a = a + 7;}\n" +
				"show a;\n" +
			"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("5\n", output.toString());
	}

	@Test
	public void compileIfBlockInnerIfFalseWithElse() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
			"def main() {\n" +
				"def a = 2;\n" +
				"if (a <= 2) { \n" +
				"\ta = a + 3; \n" +
				"\tif(a-5 > 0) {\na = a * a * a;\n } \n" +
				"\telse {\na = a * 12;\n} \n" +
				"}\n" +
				"else {\na = a + 7;\n}\n" +
				"show a;\n" +
			"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("60\n", output.toString());
	}

	@Test
	public void compileWhileBlock() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {\n" +
					"def a = 2;\n" +
					"while (a <= 60) { a = a * 2; }\n" +
					"show a;\n" +
				"}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("64\n", output.toString());
	}

	@Test
	public void compileInnerWhileBlock() throws LangException {
		MethodReader methodReader = new MethodReaderImpl(
				"def main() {\n" +
						"def a = 1;\n" +
						"def b = 0;" +
						"def c = a;" +
						// todo исправить зацикливание, если убрать начало блока
						"while ( a <= 3 ) {" +
						"b = 3; c = 1;" +
						"while (b > 0) {" +
							"c = c * a; " +
							"b = b - 1;" +
						"}\n" +
						"show c;" +
						"a = a + 1;\n" +
						"}" +
						"c = 0;}"
		);
		CompilerImpl compiler = new CompilerImpl();
		Program program = compiler.compile(Arrays.asList(methodReader.readMethod()));
		Engine engine = new EngineImpl();
		engine.setProgram(program);
		engine.run();
		assertEquals("1\n8\n27\n", output.toString());
	}


}
