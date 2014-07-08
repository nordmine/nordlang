package nordlang.parser.api;

import nordlang.exceptions.ParserException;

import java.util.List;

public interface PostfixConverter {

	/**
	 * Преобразует строку в инфиксной форме в постфиксный набор элементов
	 *
	 * @param infix
	 * @return
	 * @throws ParserException
	 */
	List<String> convert(String infix) throws ParserException;

}
