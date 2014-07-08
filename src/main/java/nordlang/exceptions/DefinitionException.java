package nordlang.exceptions;

/**
 * Ошибка, связанная с определением переменной.
 */
public class DefinitionException extends RunException {

	public DefinitionException(String message) {
		super(message);
	}
}
