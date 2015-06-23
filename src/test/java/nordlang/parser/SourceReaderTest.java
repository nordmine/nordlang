package nordlang.parser;

import nordlang.CommonTest;
import org.junit.Test;
import nordlang.parser.api.SourceReader;
import nordlang.exceptions.ParserException;
import nordlang.parser.impl.SourceReaderImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SourceReaderTest extends CommonTest {

	@Test
	public void readWhiteSpace() {
		SourceReader reader = new SourceReaderImpl("   ");
		assertEquals(3, reader.readWhiteSpace());
		reader = new SourceReaderImpl("  " + newLine + " ");
		assertEquals(3 + newLine.length(), reader.readWhiteSpace());
		reader = new SourceReaderImpl("  abc");
		assertEquals(2, reader.readWhiteSpace());
		reader = new SourceReaderImpl("abc  ");
		assertEquals(0, reader.readWhiteSpace());
	}

	@Test
	public void readWhiteSpaceRequired() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" def a ");
		reader.readLiteral("def");
		reader.readWhiteSpaceRequired();
	}

	@Test(expected = ParserException.class)
	public void readWhiteSpaceWithoutSpace() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" defa ");
		reader.readLiteral("def");
		reader.readWhiteSpaceRequired();
	}

	@Test
	public void isEnd() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  def  ");
		assertFalse(reader.isEnd());
		reader.readLiteral("def");
		assertTrue(reader.isEnd());
		reader = new SourceReaderImpl("");
		assertTrue(reader.isEnd());
		reader = new SourceReaderImpl("     ");
		assertTrue(reader.isEnd());
	}

	@Test
	public void readName() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  abc ");
		assertEquals("abc", reader.readName());
		reader = new SourceReaderImpl(" xyz1 cdf ");
		assertEquals("xyz1", reader.readName());
	}

	@Test(expected = ParserException.class)
	public void readNumberAsName() throws ParserException {
		SourceReader reader = new SourceReaderImpl("123");
		reader.readName();
	}

	@Test(expected = ParserException.class)
	public void readVeryLongName() throws ParserException {
		SourceReader reader = new SourceReaderImpl(
				" veryLongNameForReadabilityButItIsNotGoodForMyMachine");
		reader.readName();
	}

	@Test(expected = ParserException.class)
	public void readReservedName() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" and ");
		reader.readName();
	}

	@Test
	public void readLiteral() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  def ");
		assertEquals("def", reader.readLiteral("def"));
		reader = new SourceReaderImpl(" echo 1 ");
		assertEquals("echo", reader.readLiteral("echo"));
	}

	@Test(expected = ParserException.class)
	public void readWrongLiteral() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  some text ");
		reader.readLiteral("def");
	}

	@Test
	public void readParams() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  ( ) ");
		List<String> params = reader.readParameters();
		assertEquals(0, params.size());

		reader = new SourceReaderImpl("  ( def a ) ");
		params = reader.readParameters();
		assertEquals(1, params.size());
		assertEquals("def a", params.get(0));

		reader = new SourceReaderImpl("  ( def a, def b,def c ) ");
		params = reader.readParameters();
		assertEquals(3, params.size());
		assertEquals("def a", params.get(0));
		assertEquals("def b", params.get(1));
		assertEquals("def c", params.get(2));
	}

	@Test(expected = ParserException.class)
	public void readBothParamsEmpty() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  (,) ");
		reader.readParameters();
	}

	@Test(expected = ParserException.class)
	public void readFirstParamEmpty() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  (,def a) ");
		reader.readParameters();
	}

	@Test(expected = ParserException.class)
	public void readSecondParamEmpty() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  (def a,) ");
		reader.readParameters();
	}

	@Test
	public void readStatements() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  {} ");
		List<String> statements = reader.readStatements();
		assertEquals(0, statements.size());

		reader = new SourceReaderImpl("  {;} ");
		statements = reader.readStatements();
		assertEquals(0, statements.size());

		reader = new SourceReaderImpl("  { def a; } ");
		statements = reader.readStatements();
		assertEquals(1, statements.size());
		assertEquals("def a", statements.get(0));

		reader = new SourceReaderImpl("  { def a = 1; echo a; } ");
		statements = reader.readStatements();
		assertEquals(2, statements.size());
		assertEquals("def a = 1", statements.get(0));
		assertEquals("echo a", statements.get(1));
	}

	@Test(expected = ParserException.class)
	public void readStatementsWithoutLastDelimiter() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  { def a; def b} ");
		reader.readStatements();
	}

	@Test(expected = ParserException.class)
	public void readStatementsExtraOpenBracket() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  { def a; { def b;} ");
		reader.readStatements();
	}

	@Test
	public void readStatementsInnerBlock() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  { def a; ; {def b; def c;}} ");
		List<String> statements = reader.readStatements();
		assertEquals(2, statements.size());
		assertEquals("def a", statements.get(0));
		assertEquals("{def b; def c;}", statements.get(1));
	}

	@Test
	public void readIfElseBlock() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  { def a; if () {show a;} else {show b;} def b;} ");
		List<String> statements = reader.readStatements();
		assertEquals(4, statements.size());
		assertEquals("def a", statements.get(0));
		assertEquals("if () {show a;}", statements.get(1));
		assertEquals("else {show b;}", statements.get(2));
		assertEquals("def b", statements.get(3));
	}

	@Test
	public void readWhileBlock() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" { def a; while() {show 4;{ show a;}} def b; }");
		List<String> statements = reader.readStatements();
		assertEquals(3, statements.size());
		assertEquals("def a", statements.get(0));
		assertEquals("while() {show 4;{ show a;}}", statements.get(1));
		assertEquals("def b", statements.get(2));
	}

	@Test
	public void readMathSign() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" * / + - > >= == <> or and ");
		assertEquals("*", reader.readMathSign());
		assertEquals("/", reader.readMathSign());
		assertEquals("+", reader.readMathSign());
		assertEquals("-", reader.readMathSign());
		assertEquals(">", reader.readMathSign());
		assertEquals(">=", reader.readMathSign());
		assertEquals("==", reader.readMathSign());
		assertEquals("<>", reader.readMathSign());
		assertEquals("or", reader.readMathSign());
		assertEquals("and", reader.readMathSign());
	}

	@Test(expected = ParserException.class)
	public void readNotMathSign() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" = ");
		reader.readMathSign();
	}

	@Test
	public void checkForMathSign() {
		SourceReader reader = new SourceReaderImpl("+");
		assertTrue(reader.checkForMathSign());

		reader = new SourceReaderImpl("b");
		assertFalse(reader.checkForMathSign());

		reader = new SourceReaderImpl("= ");
		assertFalse(reader.checkForMathSign());

		reader = new SourceReaderImpl("== ");
		assertTrue(reader.checkForMathSign());

		reader = new SourceReaderImpl("=> ");
		assertFalse(reader.checkForMathSign());

		reader = new SourceReaderImpl(">= ");
		assertTrue(reader.checkForMathSign());

		reader = new SourceReaderImpl("and ");
		assertTrue(reader.checkForMathSign());

		reader = new SourceReaderImpl("or ");
		assertTrue(reader.checkForMathSign());

		reader = new SourceReaderImpl("orand ");
		assertTrue(reader.checkForMathSign());
	}

	@Test
	public void checkForInteger() {
		SourceReader reader = new SourceReaderImpl("123");
		assertTrue(reader.checkForInteger());

		reader = new SourceReaderImpl("string");
		assertFalse(reader.checkForInteger());
	}

	@Test
	public void readInteger() throws ParserException {
		SourceReader reader = new SourceReaderImpl("1 234 0 -5");
		assertEquals(1, reader.readInteger());
		assertEquals(234, reader.readInteger());
		assertEquals(0, reader.readInteger());
//		assertEquals(-5, reader.readInteger());
	}


	@Test(expected = ParserException.class)
	public void readWordAsInteger() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" word");
		reader.readInteger();
	}

	@Test(expected = ParserException.class)
	public void readVeryLongInteger() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" 16465741356");
		reader.readInteger();
	}

	@Test
	public void readUUIDAsString() {
		UUID uuid = UUID.randomUUID();
		SourceReader reader = new SourceReaderImpl(" " + uuid + " ");
		assertEquals(uuid.toString(), reader.readString());
	}

	@Test
	public void checkForLiteral() {
		SourceReader reader = new SourceReaderImpl("(");
		assertTrue(reader.checkForLiteral("("));

		reader = new SourceReaderImpl("a");
		assertFalse(reader.checkForLiteral("("));

		reader = new SourceReaderImpl("def");
		assertTrue(reader.checkForLiteral("def"));
		assertTrue(reader.checkForLiteral("de"));
		assertFalse(reader.checkForLiteral("abc"));
	}

	@Test
	public void readToEnd() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" a is a letter ");
		reader.readLiteral("a");
		reader.readLiteral("is");
		assertEquals("a letter", reader.readToEnd());
	}

	@Test
	public void readStringLiteral() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" abc 'some text' 12 ");
		assertEquals("abc", reader.readName());
		assertEquals("some text", reader.readStringLiteral());
		assertEquals(12, reader.readInteger());

		reader = new SourceReaderImpl("' other text '");
		assertEquals(" other text ", reader.readStringLiteral());
	}

	@Test(expected = ParserException.class)
	public void readStringWithoutBeginQuote() throws ParserException {
		SourceReader reader = new SourceReaderImpl("some text'");
		reader.readStringLiteral();
	}

	@Test(expected = ParserException.class)
	public void readStringWithoutEndQuote() throws ParserException {
		SourceReader reader = new SourceReaderImpl("'some text");
		reader.readStringLiteral();
	}

	@Test
	public void readExpressionInBracket() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" (a == b) ");
		assertEquals("a == b", reader.readExpressionInBrackets());

		reader = new SourceReaderImpl(" ( 2 < (5 - 3) * 2 ) ");
		assertEquals("2 < (5 - 3) * 2", reader.readExpressionInBrackets());
	}

	@Test(expected = ParserException.class)
	public void readExpressionInBracketWithoutCloseBracket() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" ( 2 < (5 - 3 * 2 ) ");
		reader.readExpressionInBrackets();
	}

	@Test(expected = ParserException.class)
	public void readExpressionInBracketWithoutOpenBracket() throws ParserException {
		SourceReader reader = new SourceReaderImpl("  2 < (5 - 3 * 2 ) ");
		reader.readExpressionInBrackets();
	}

	@Test(expected = ParserException.class)
	public void readEmptyExpressionInBracket() throws ParserException {
		SourceReader reader = new SourceReaderImpl("()");
		reader.readExpressionInBrackets();
	}

	@Test
	public void checkForLiteralAtTheEnd() throws ParserException {
		SourceReader reader = new SourceReaderImpl(" d");
		reader.readWhiteSpace();
		assertFalse(reader.checkForLiteral("def"));

		reader = new SourceReaderImpl(" def");
		reader.readWhiteSpace();
		assertTrue(reader.checkForLiteral("def"));
	}

}
