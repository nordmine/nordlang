package nordlang.vm.api;

import nordlang.vm.Command;
import nordlang.vm.CommandType;
import nordlang.vm.TwinValue;

import java.util.List;

/**
 * Набор команд для выполнения виртуальной машиной.
 */
public interface Program {

	/**
	 * Добавляет команду с указанным числовым аргументом.
	 *
	 * @param type
	 * @param val
	 */
	void addCommand(CommandType type, Integer val);

	/**
	 * Добавляет команду с указанным строковым аргументом.
	 *
	 * @param type
	 * @param val
	 */
	void addCommand(CommandType type, String val);

	void addCommand(CommandType type, TwinValue val);

	void appendProgram(Program program);

	/**
	 * Добавляет команды вывода на экран.
	 */
	void addShowCommand();

	/**
	 * Добавляет команду помещения элемента в стек.
	 */
	void addPushCommand();

	/**
	 * Добавляет команду удаления из стека.
	 */
	void addPopCommand();


	void addUnDefCommand();

	int size();

	Command get(int index);

	List<Command> getAllCommands();
}
