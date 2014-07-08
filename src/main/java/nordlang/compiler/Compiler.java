package nordlang.compiler;

import nordlang.exceptions.ParserException;
import nordlang.parser.api.MethodInfo;
import nordlang.vm.api.Program;

import java.util.List;

/**
 * Переводит текстовое представление кода в команды виртуальной машины
 */
public interface Compiler {

	/**
	 * Преобразует исходный код метода main() в команды виртуальной машины
	 *
	 * @param parsedMethods
	 * @return
	 */
	Program compile(List<MethodInfo> parsedMethods) throws ParserException;

}
