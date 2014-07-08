package nordlang.parser.api;

import nordlang.exceptions.ParserException;

import java.util.List;

public interface SourceReader {

	/**
	 * Сдвигает указатель считывания до тех пор, пока не встретится символ,
	 * отличный от пустых символов
	 *
	 * @return Количество пропущенных символов
	 */
	int readWhiteSpace();

	/**
	 * Считывает хотя бы один пробельный символ.
	 *
	 * @throws ParserException Если нет ни одного пробельного символа.
	 */
	void readWhiteSpaceRequired() throws ParserException;

	/**
	 * Считывает указанную строку
	 *
	 * @param literal
	 */
	String readLiteral(String literal) throws ParserException;

	/**
	 * Считывает элементы, разделённые точкой с запятой, между двумя
	 * фигурными скобками.
	 *
	 * @return
	 * @throws ParserException
	 */
	List<String> readStatements() throws ParserException;

	/**
	 * Считывает элементы, разделённые запятой, между двумя круглыми скобками
	 *
	 * @return
	 * @throws ParserException
	 */
	List<String> readParameters() throws ParserException;

	/**
	 * Считывает выражение, заключённое в круглые скобки.
	 * @return
	 * @throws ParserException Если выражение начинается не со скобки.
	 */
	String readExpressionInBrackets() throws ParserException;

	/**
	 * Считывает имя переменной.
	 *
	 * @return
	 */
	String readName() throws ParserException;

	/**
	 * Проверяет, достигнут ли конец строки исходного кода.
	 * Если остались только пробельные символы, считается, что конец достигнут.
	 *
	 * @return
	 */
	boolean isEnd();

	/**
	 * Проверяет, можно ли с текущей позиции считать указанный литерал
	 *
	 * @param literal
	 * @return
	 */
	boolean checkForLiteral(String literal);

	/**
	 * Проверяет, можно ли с текущей позиции считать целое число
	 *
	 * @return
	 */
	boolean checkForInteger();

	/**
	 * Считывает целое число
	 *
	 * @return
	 */
	int readInteger() throws ParserException;

	String readString();

	/**
	 * Проверяет, можно ли с текущей позиции считать знак
	 * арифметической операции
	 *
	 * @return
	 */
	boolean checkForMathSign();

	/**
	 * Считывает знак арифметической операции
	 *
	 * @return
	 */
	String readMathSign() throws ParserException;

	/**
	 * Считывает строку, заключённую в одинарные кавычки
	 *
	 * @return
	 */
	String readStringLiteral() throws ParserException;

	/**
	 * Возвращает подстроку исходного кода, которая начинается
	 * с текущей позиции.
	 *
	 * @return
	 */
	String readToEnd();

}
