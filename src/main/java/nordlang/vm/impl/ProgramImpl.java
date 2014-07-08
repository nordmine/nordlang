package nordlang.vm.impl;

import nordlang.vm.Command;
import nordlang.vm.CommandType;
import nordlang.vm.TwinValue;
import nordlang.vm.api.Program;

import java.util.ArrayList;
import java.util.List;

public class ProgramImpl implements Program {

	private List<Command> commands = new ArrayList<Command>();

	@Override
	public void addCommand(CommandType type, Integer val) {
		commands.add(new Command(type, new TwinValue(val)));
	}

	@Override
	public void addCommand(CommandType type, String val) {
		commands.add(new Command(type, new TwinValue(val)));
	}

	@Override
	public void addCommand(CommandType type, TwinValue val) {
		commands.add(new Command(type, val));
	}

	@Override
	public void appendProgram(Program program) {
		commands.addAll(program.getAllCommands());
	}

	@Override
	public void addShowCommand() {
		commands.add(new Command(CommandType.SHOW));
	}

	@Override
	public void addPushCommand() {
		commands.add(new Command(CommandType.PUSH));
	}

	@Override
	public void addPopCommand() {
		commands.add(new Command(CommandType.POP));
	}

	@Override
	public void addUnDefCommand() {
		commands.add(new Command(CommandType.UNDEF));
	}

	@Override
	public int size() {
		return commands.size();
	}

	@Override
	public Command get(int index) {
		return commands.get(index);
	}

	@Override
	public List<Command> getAllCommands() {
		return commands;
	}

}
