package nordlang.exceptions;

/**
 * Ошибка, связанная с разбором исходного кода
 */
public class ParserException extends LangException {
	// todo помимо текста ошибки также нужно указывать позицию, в которой она произошла
	public ParserException(String message) {
		super(message);
	}
}
