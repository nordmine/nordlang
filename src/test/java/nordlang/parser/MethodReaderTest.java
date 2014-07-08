package nordlang.parser;

import nordlang.exceptions.ParserException;
import nordlang.parser.api.MethodInfo;
import nordlang.parser.api.MethodReader;
import nordlang.parser.impl.MethodReaderImpl;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MethodReaderTest {

	@Test
	public void readMethod() throws ParserException {
		MethodReader reader = new MethodReaderImpl("def sin() {def a = 1;}");
		MethodInfo info = reader.readMethod();
		assertEquals("sin", info.getName());
		assertEquals(0, info.getParams().size());
		assertEquals(1, info.getStatements().size());

		reader = new MethodReaderImpl(" def cos (def x)\n{\ndef a = 2;\nshow a;\n}\n");
		info = reader.readMethod();
		assertEquals("cos", info.getName());
		assertEquals(1, info.getParams().size());
		assertEquals("x", info.getParams().get(0));
		assertEquals(2, info.getStatements().size());

		reader = new MethodReaderImpl("def Method1( def x , def y )\n{\ndef z\n= x * y ;\n}\n");
		info = reader.readMethod();
		assertEquals("Method1", info.getName());
		assertEquals(2, info.getParams().size());
		assertEquals("x", info.getParams().get(0));
		assertEquals("y", info.getParams().get(1));
		assertEquals(1, info.getStatements().size());
	}

	@Test
	public void readTwoMethods() throws ParserException {
		MethodReader reader = new MethodReaderImpl("def sin(def x) {def a = 0;} def cos(def y) {def b = 0;}");

		MethodInfo info = reader.readMethod();
		assertEquals("sin", info.getName());
		assertEquals(1, info.getParams().size());
		assertEquals("x", info.getParams().get(0));
		assertEquals(1, info.getStatements().size());

		info = reader.readMethod();
		assertEquals("cos", info.getName());
		assertEquals(1, info.getParams().size());
		assertEquals("y", info.getParams().get(0));
		assertEquals(1, info.getStatements().size());
	}

	@Test
	public void readParameterNames() throws ParserException {
		MethodReader reader = new MethodReaderImpl("  (def x, def y) ");
		List<String> names = reader.readParameterNames();
		assertEquals(2, names.size());
		assertEquals("x", names.get(0));
		assertEquals("y", names.get(1));
	}

	@Test(expected = ParserException.class)
	public void readParameterNamesWithoutSpace() throws ParserException {
		MethodReader reader = new MethodReaderImpl("  (def y, defx) ");
		reader.readParameterNames();
	}

}
