package nordlang.parser;

import nordlang.parser.api.PostfixConverter;
import nordlang.exceptions.ParserException;
import nordlang.parser.impl.PostfixConverterImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PostfixConverterTest {

	@Test
	public void convert() throws ParserException {
		PostfixConverter converter = new PostfixConverterImpl();

		List<String> postfix = converter.convert(" alice + bob -32");
		List<String> expected = Arrays.asList("alice", "bob", "+", "32", "-");
		assertEquals(expected, postfix);

		postfix = converter.convert("a+b*c");
		expected = Arrays.asList("a", "b", "c", "*", "+");
		assertEquals(expected, postfix);

		postfix = converter.convert("alice * ( bob15 + 0 )");
		expected = Arrays.asList("alice", "bob15", "0", "+", "*");
		assertEquals(expected, postfix);

		postfix = converter.convert("a+b*(c-d)");
		expected = Arrays.asList("a", "b", "c", "d", "-", "*", "+");
		assertEquals(expected, postfix);

		postfix = converter.convert(" a ");
		expected = Arrays.asList("a");
		assertEquals(expected, postfix);

		postfix = converter.convert(" 'some' + 'text' ");
		expected = Arrays.asList("'some", "'text", "+");
		assertEquals(expected, postfix);


	}

//	@Test
	public void negativeNumber() throws ParserException {
		PostfixConverter converter = new PostfixConverterImpl();
		List<String> postfix = converter.convert("a+(-b)");
	}


}
