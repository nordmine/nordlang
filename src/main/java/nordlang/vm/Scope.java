package nordlang.vm;

import nordlang.exceptions.DefinitionException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Область видимости переменных.
 */
public class Scope {

	private Map<String, TwinValue> variables = new HashMap<String, TwinValue>();
	public static final List<String> reservedNames = Arrays.asList("def", "show", "if", "else", "while", "and", "or");

	public boolean contains(String name) {
		return variables.containsKey(name);
	}

	public void put(String name, TwinValue twinValue) throws DefinitionException {
		if (contains(name)) {
			throw new DefinitionException("variable with name '" + name
					+ "' already defined in this scope");
		}
		if (reservedNames.contains(name)) {
			throw new DefinitionException("variable name '" + name
					+ "' is reserved by environment");
		}
		variables.put(name, twinValue.copy());
	}

	public TwinValue get(String name) throws DefinitionException {
		checkForExists(name);
		return variables.get(name).copy();
	}

	public void set(String name, TwinValue val) throws DefinitionException {
		checkForExists(name);
		variables.put(name, val.copy());
	}

	public void remove(String name) throws DefinitionException {
		checkForExists(name);
		variables.remove(name);
	}

	private void checkForExists(String name) throws DefinitionException {
		if (!contains(name)) {
			throw new DefinitionException("undefined variable: " + name);
		}
	}
}
