package nordlang.parser.impl;

import com.google.common.base.CharMatcher;
import nordlang.parser.api.SourceReader;
import nordlang.exceptions.ParserException;
import nordlang.vm.Scope;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class SourceReaderImpl implements SourceReader {

	public static List<String> MATH_SIGN = Arrays.asList("+", "-", "*", "/", "==", "<>", ">", "<", ">=", "<=", "and", "or");

	private String source;
	private int cursor = 0;

	public SourceReaderImpl(String source) {
		this.source = source;
	}

	@Override
	public int readWhiteSpace() {
		int startPosition = cursor;
		while (cursor < source.length() && CharMatcher.WHITESPACE.matches(source.charAt(cursor))) {
			cursor++;
		}
		return cursor - startPosition;
	}

	@Override
	public void readWhiteSpaceRequired() throws ParserException {
		if (readWhiteSpace() == 0) {
			throw new ParserException("whitespace expected");
		}
	}

	@Override
	public boolean isEnd() {
		readWhiteSpace();
		return cursor == source.length();
	}

	@Override
	public boolean checkForLiteral(String literal) {
		return cursor + literal.length() <= source.length()
				&& source.substring(cursor, cursor + literal.length()).equals(literal);
	}

	@Override
	public boolean checkForInteger() {
		return CharMatcher.DIGIT.matches(source.charAt(cursor));
	}

	@Override
	public boolean checkForMathSign() {
		int index = cursor;
		StringBuilder result = new StringBuilder();
		while (index < source.length() && listItemStartsWithSubstring(MATH_SIGN, result.toString() + source.charAt(index))) {
			result.append(source.charAt(index));
			index++;
		}
		return MATH_SIGN.contains(result.toString());
	}

	@Override
	public String readName() throws ParserException {
		readWhiteSpace();
		if (!CharMatcher.JAVA_LETTER.matches(source.charAt(cursor))) {
			throw new ParserException("name must begin from letter");
		}
		StringBuilder name = new StringBuilder();
		while (cursor < source.length() && CharMatcher.JAVA_LETTER_OR_DIGIT.matches(source.charAt(cursor))) {
			name.append(source.charAt(cursor));
			cursor++;
		}
		if (name.length() > 50) {
			throw new ParserException("name length must be less or equals than 50");
		}
		String nameString = name.toString();
		if(Scope.reservedNames.contains(nameString)) {
			throw new ParserException("name '" + nameString + "' is reserved by environment");
		}
		return nameString;
	}

	@Override
	public int readInteger() throws ParserException {
		readWhiteSpace();
		if (!CharMatcher.DIGIT.matches(source.charAt(cursor))) {
			throw new ParserException("integer must begin from digit");
		}
		StringBuilder number = new StringBuilder();
		while (cursor < source.length() && CharMatcher.DIGIT.matches(source.charAt(cursor))) {
			number.append(source.charAt(cursor));
			cursor++;
		}
		if (number.length() > 10) {
			throw new ParserException("count of digits must be less or equals than 10");
		}
		return Integer.parseInt(number.toString());
	}

	@Override
	public String readString() {
		readWhiteSpace();
		StringBuilder result = new StringBuilder();
		while (cursor < source.length() && CharMatcher.WHITESPACE.negate().matches(source.charAt(cursor))) {
			result.append(source.charAt(cursor));
			cursor++;
		}
		return result.toString();
	}

	@Override
	public String readMathSign() throws ParserException {
		readWhiteSpace();
		StringBuilder result = new StringBuilder();
		while (cursor < source.length() && listItemStartsWithSubstring(MATH_SIGN, result.toString() + source.charAt(cursor))) {
			result.append(source.charAt(cursor));
			cursor++;
		}
		String mathSign = result.toString();
		if (!MATH_SIGN.contains(mathSign)) {
			throw new ParserException("wrong math sign");
		}
		return mathSign;
	}

	private boolean listItemStartsWithSubstring(List<String> list, String substr) {
		for (String item : list) {
			if(item.startsWith(substr)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String readLiteral(String literal) throws ParserException {
		readWhiteSpace();
		String s = source.substring(cursor, cursor + literal.length());
		if (!s.equals(literal)) {
			throw new ParserException(literal + " expected");
		}
		cursor += literal.length();
		return s;
	}

	@Override
	public String readStringLiteral() throws ParserException {
		readWhiteSpace();
		if (source.charAt(cursor) == '\'') {
			cursor++;
			int beginIndex = cursor;
			int endIndex = source.indexOf("'", cursor);
			if (endIndex == -1) {
				throw new ParserException("end ' not found");
			}
			cursor = endIndex + 1;
			return source.substring(beginIndex, endIndex);
		} else {
			throw new ParserException("begin ' expected");
		}
	}

	@Override
	public String readToEnd() {
		return source.substring(cursor).trim();
	}

	@Override
	public List<String> readStatements() throws ParserException {
		return readBlock('{', '}', ';', true);
	}

	@Override
	public List<String> readParameters() throws ParserException {
		return readBlock('(', ')', ',', false);
	}

	private List<String> readBlock(
			char openBracket, char closeBracket,
			char delimiter, boolean hasLastDelimiter) throws ParserException {
		readWhiteSpace();

		List<String> statements = new LinkedList<String>();
		Stack<Character> brackets = new Stack<Character>();
		StringBuilder line = new StringBuilder();
		while (cursor < source.length()) {
			char current = source.charAt(cursor);
			// разделитель
			if (current == delimiter) {
				if (brackets.size() > 1) {
					line.append(current);
				} else {
					appendItem(statements, line, hasLastDelimiter);
					line = new StringBuilder();
				}
				// начало блока
			} else if (current == openBracket) {
				brackets.push(openBracket);
				if (brackets.size() > 1) {
					line.append(current);
				}
				// конец блока
			} else if (current == closeBracket) {
				if (brackets.isEmpty()) {
					throw new ParserException("unexpected close bracket");
				}
				if (brackets.peek() == openBracket) {
					brackets.pop();
					if (brackets.isEmpty()) {
						appendLastItem(statements, line, hasLastDelimiter);
						cursor++;
						break;
					} else {
						line.append(current);
						if (brackets.size() == 1) {
							appendItem(statements, line, hasLastDelimiter);
							line = new StringBuilder();
						}
					}
				}
				// любые другие символы
			} else {
				line.append(current);
			}
			cursor++;
		}
		if (!brackets.isEmpty()) {
			throw new ParserException(closeBracket + " expected");
		}
		return statements;
	}

	@Override
	public String readExpressionInBrackets() throws ParserException {
		readWhiteSpace();
		String expression = null;
		if (source.charAt(cursor) == '(') {
			int index = cursor;
			Stack<Character> stack = new Stack<Character>();
			while (index < source.length()) {
				char c = source.charAt(index);
				if(c == '(') {
					stack.push(c);
				}
				if(c == ')') {
					if(stack.isEmpty()) {
						throw new ParserException("unexpected close bracket");
					}
					stack.pop();
					if(stack.isEmpty()) {
						expression = source.substring(cursor + 1, index).trim();
						index++;
						cursor = index;
						break;
					}
				}
				index++;
			}
			if(!stack.isEmpty()) {
				throw new ParserException("close bracket expected");
			}
		} else {
			throw new ParserException("open bracket expected");
		}
		if(expression == null || expression.length() == 0) {
			throw new ParserException("empty expression");
		}
		return expression;
	}

	private void appendItem(List<String> statements, StringBuilder line, boolean hasLastDelimiter)
			throws ParserException {
		String lineStr = line.toString().trim();
		if (lineStr.length() > 0) {
			statements.add(lineStr);
		} else {
			if (!hasLastDelimiter) {
				throw new ParserException("empty statement");
			}
		}
	}

	private void appendLastItem(List<String> statements, StringBuilder line, boolean hasLastDelimiter)
			throws ParserException {
		String lineStr = line.toString().trim();
		if (lineStr.length() > 0) {
			if (hasLastDelimiter) {
				throw new ParserException("delimiter expected");
			} else {
				statements.add(lineStr);
			}
		} else {
			if (!hasLastDelimiter && !statements.isEmpty()) {
				throw new ParserException("empty statement");
			}
		}
	}
}
