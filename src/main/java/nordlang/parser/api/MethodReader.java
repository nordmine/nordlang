package nordlang.parser.api;

import nordlang.exceptions.ParserException;

import java.util.List;

public interface MethodReader {

	/**
	 * Считывает метод и нормализует его
	 */
	MethodInfo readMethod() throws ParserException;

	/**
	 * Считывает имена параметров метода
	 *
	 * @return
	 * @throws ParserException
	 */
	List<String> readParameterNames() throws ParserException;

}
