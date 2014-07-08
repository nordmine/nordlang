package nordlang.vm;

/**
 * Команда виртуальной машины. Имеет имя и необязательный параметр.
 */
public class Command {

	private CommandType type;
	private TwinValue val;

	public Command(CommandType type) {
		this.type = type;
	}

	public Command(CommandType type, TwinValue val) {
		this.type = type;
		this.val = val;
	}

	public CommandType getType() {
		return type;
	}

	public void setType(CommandType type) {
		this.type = type;
	}

	public TwinValue getVal() {
		return val;
	}

	public void setVal(TwinValue val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return type + (val == null ? "" : " " + val);
	}
}
