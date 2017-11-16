package ru.nordmine.nordlang.lexer;

import org.testng.annotations.Test;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;

import static org.testng.Assert.assertEquals;

public class LexerTest {

    private Lexer lexer;

    @Test
    public void numbers() throws SyntaxException {
        lexer = new StringLexer("   0 3\r\n135   ");

        Token zeroToken = lexer.nextToken();
        assertEquals(zeroToken.getTag(), Tag.INT);
        assertEquals(zeroToken.toString(), "0");

        Token singleDigitToken = lexer.nextToken();
        assertEquals(singleDigitToken.getTag(), Tag.INT);
        assertEquals(singleDigitToken.toString(), "3");

        Token severalDigitsToken = lexer.nextToken();
        assertEquals(severalDigitsToken.getTag(), Tag.INT);
        assertEquals(severalDigitsToken.toString(), "135");
    }

    @Test
    public void mathExpression() throws SyntaxException {
        lexer = new StringLexer("   var1+(var2/5 - 2) * 3   ");
        assertEquals(nextTag(), Tag.ID);
        assertEquals(nextTag(), Tag.PLUS);
        assertEquals(nextTag(), Tag.OPEN_BRACKET);
        assertEquals(nextTag(), Tag.ID);
        assertEquals(nextTag(), Tag.DIVISION);
        assertEquals(nextTag(), Tag.INT);
        assertEquals(nextTag(), Tag.MINUS);
        assertEquals(nextTag(), Tag.INT);
        assertEquals(nextTag(), Tag.CLOSE_BRACKET);
        assertEquals(nextTag(), Tag.MUL);
        Token realToken = lexer.nextToken();
        assertEquals(realToken.getTag(), Tag.INT);
        assertEquals(realToken.toString(), "3");
    }

    @Test
    public void assignWithExpression() throws SyntaxException {
        lexer = new StringLexer("int i=20+3");
        assertEquals(nextTag(), Tag.BASIC);
        assertEquals(nextTag(), Tag.ID);
        assertEquals(nextTag(), Tag.ASSIGN);
        assertEquals(nextTag(), Tag.INT);
        assertEquals(nextTag(), Tag.PLUS);
        assertEquals(nextTag(), Tag.INT);
    }

    @Test
    public void increment() throws SyntaxException {
        lexer = new StringLexer("+ ++");
        assertEquals(nextTag(), Tag.PLUS);
        assertEquals(nextTag(), Tag.INCREMENT);
    }

    @Test
    public void decrement() throws SyntaxException {
        lexer = new StringLexer("- --");
        assertEquals(nextTag(), Tag.MINUS);
        assertEquals(nextTag(), Tag.DECREMENT);
    }

    @Test
    public void inlineCommentOnly() throws SyntaxException {
        lexer = new StringLexer("//i");
        assertEquals(nextTag(), Tag.END_OF_FILE);
    }

    @Test
    public void beginInlineComment() throws SyntaxException {
        lexer = new StringLexer("// some text\n10");
        assertEquals(lexer.nextToken().toString(), "10");
    }

    @Test
    public void middleInlineComment() throws SyntaxException {
        lexer = new StringLexer("20 / 1  // comment\n30 ");
        assertEquals(lexer.nextToken().toString(), "20");
        assertEquals(nextTag(), Tag.DIVISION);
        assertEquals(lexer.nextToken().toString(), "1");
        assertEquals(lexer.nextToken().toString(), "30");
    }

    @Test
    public void charLiteral() throws SyntaxException {
        lexer = new StringLexer("   'a'");
        assertEquals(lexer.nextToken().toString(), "'a'");
    }

    @Test
    public void sizeLiteral() throws SyntaxException {
        lexer = new StringLexer("  size(a) ");
        assertEquals(nextTag(), Tag.SIZE);
    }

    @Test
    public void constLiteral() throws SyntaxException {
        lexer = new StringLexer("const int MAGIC = 42;");
        assertEquals(nextTag(), Tag.CONST);
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 1: char literal contains more than 1 symbol"
    )
    public void charLiteralWithoutEnding() throws SyntaxException {
        lexer = new StringLexer("    'b");
        lexer.nextToken();
    }

    @Test
    public void stringLiteral() throws SyntaxException {
        lexer = new StringLexer("   \"some text\"");
        Token stringToken = lexer.nextToken();
        assertEquals(stringToken.getTag(), Tag.STRING);
        assertEquals(stringToken.toString(), "some text");
    }

    @Test(
            expectedExceptions = SyntaxException.class,
            expectedExceptionsMessageRegExp = "Syntax error at line 2: unexpected end of string literal"
    )
    public void stringLiteralWithoutEnding() throws SyntaxException {
        lexer = new StringLexer(" \r\n  \"some text");
        lexer.nextToken();
    }

    private Tag nextTag() throws SyntaxException {
        return lexer.nextToken().getTag();
    }
}
