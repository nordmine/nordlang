package nordlang.vm.api;

import nordlang.exceptions.RunException;
import nordlang.vm.api.Program;

/**
 * Движок виртуальной машины.
 */
public interface Engine {

	/**
	 * Устанавливает новый набор команд для выполнения.
	 *
	 * @param program
	 */
	void setProgram(Program program);



	/**
	 * Последовательно выполняет все добавленные команды.
	 *
	 * @throws RunException
	 */
	void run() throws RunException;
}
