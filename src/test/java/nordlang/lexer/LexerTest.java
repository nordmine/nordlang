package nordlang.lexer;

import nordlang.exceptions.ParserException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LexerTest {

    private Lexer lexer;

    @Test
    public void numbers() throws ParserException {
        lexer = new Lexer("   0 3 135   ");

        Token zeroToken = lexer.nextToken();
        assertEquals(Tag.INT, zeroToken.getTag());
        assertEquals("0", zeroToken.toString());

        Token singleDigitToken = lexer.nextToken();
        assertEquals(Tag.INT, singleDigitToken.getTag());
        assertEquals("3", singleDigitToken.toString());

        Token severalDigitsToken = lexer.nextToken();
        assertEquals(Tag.INT, severalDigitsToken.getTag());
        assertEquals("135", severalDigitsToken.toString());
    }

    @Test
    public void mathExpression() throws ParserException {
        lexer = new Lexer("   var1+(var2/5 - 2) * 3   ");
        assertEquals(Tag.ID, nextTag());
        assertEquals(Tag.PLUS, nextTag());
        assertEquals(Tag.OPEN_BRACKET, nextTag());
        assertEquals(Tag.ID, nextTag());
        assertEquals(Tag.DIVISION, nextTag());
        assertEquals(Tag.INT, nextTag());
        assertEquals(Tag.MINUS, nextTag());
        assertEquals(Tag.INT, nextTag());
        assertEquals(Tag.CLOSE_BRACKET, nextTag());
        assertEquals(Tag.MUL, nextTag());
        Token realToken = lexer.nextToken();
        assertEquals(Tag.INT, realToken.getTag());
        assertEquals("3", realToken.toString());
    }

    @Test
    public void assignWithExpression() throws ParserException {
        lexer = new Lexer("int i=20+3");
        assertEquals(Tag.BASIC, nextTag());
        assertEquals(Tag.ID, nextTag());
        assertEquals(Tag.ASSIGN, nextTag());
        assertEquals(Tag.INT, nextTag());
        assertEquals(Tag.PLUS, nextTag());
        assertEquals(Tag.INT, nextTag());
    }

    @Test
    public void inlineCommentOnly() throws ParserException {
        lexer = new Lexer("//i");
        assertEquals(Tag.END_OF_FILE, nextTag());
    }

    @Test
    public void beginInlineComment() throws ParserException {
        lexer = new Lexer("// some text\n10");
        assertEquals("10", lexer.nextToken().toString());
    }

    @Test
    public void middleInlineComment() throws ParserException {
        lexer = new Lexer("20 / 1  // comment\n30 ");
        assertEquals("20", lexer.nextToken().toString());
        assertEquals(Tag.DIVISION, nextTag());
        assertEquals("1", lexer.nextToken().toString());
        assertEquals("30", lexer.nextToken().toString());
    }

    @Test
    public void charLiteral() throws ParserException {
        lexer = new Lexer("   'a'");
        assertEquals("'a'", lexer.nextToken().toString());
    }

    @Test
    public void charLiteralWithoutEnding() {
        lexer = new Lexer("    'b");
        try {
            lexer.nextToken();
        } catch (ParserException e) {
            assertEquals("Syntax error at line 1: char literal contains more than 1 symbol", e.getMessage());
        }
    }

    @Test
    public void stringLiteral() throws ParserException {
        lexer = new Lexer("   \"some text\"");
        Token stringToken = lexer.nextToken();
        assertEquals(Tag.STRING, stringToken.getTag());
        assertEquals("some text", stringToken.toString());
    }

    @Test
    public void stringLiteralWithoutEnding() {
        lexer = new Lexer("  \"some text");
        try {
            lexer.nextToken();
        } catch (ParserException e) {
            assertEquals("Syntax error at line 1: unexpected end of string literal", e.getMessage());
        }
    }

    private Tag nextTag() throws ParserException {
        return lexer.nextToken().getTag();
    }
}
