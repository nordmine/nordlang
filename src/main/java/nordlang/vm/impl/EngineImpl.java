package nordlang.vm.impl;

import nordlang.exceptions.DefinitionException;
import nordlang.exceptions.RunException;
import nordlang.vm.Command;
import nordlang.vm.Scope;
import nordlang.vm.TwinValue;
import nordlang.vm.api.Engine;
import nordlang.vm.api.Program;

import java.util.Stack;

public class EngineImpl implements Engine {


	private Program program = new ProgramImpl();
	private Scope scope;
	private TwinValue leftValue, rightValue;
	private Stack<TwinValue> valueStack = new Stack<TwinValue>();

	@Override
	public void setProgram(Program program) {
		this.program = program;
	}

	@Override
	public void run() throws RunException {
		scope = new Scope();
		int i = 0;
		while (i < program.size()) {
			Command cmd = program.get(i);
			switch (cmd.getType()) {
				case GET:
					leftValue = loadVariableIfNeeded(cmd.getVal());
					break;
				case SET: // здесь всегда имя переменной в аргументе
					String variableName = cmd.getVal().getStrVal();
					scope.set(variableName, leftValue);
					break;
				case PUSH:
					valueStack.push(leftValue);
					break;
				case POP:
					leftValue = valueStack.pop();
					break;
				case DEF:
					String leftStr = cmd.getVal().getStrVal();
					scope.put(leftStr, leftValue);
					break;
				case UNDEF:
					scope.remove(leftValue.getStrVal());
					break;
				case PLUS:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					if (leftValue.getIntVal() != null && rightValue.getIntVal() != null) {
						// сложение двух чисел
						leftValue.setIntVal(leftValue.getIntVal() + rightValue.getIntVal());
					} else {
						// конкатенация двух строк, одна из которых может являться числом
						leftValue.setStrVal(leftValue.toString() + rightValue.toString());
					}
					break;
				case MINUS:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					leftValue.setIntVal(leftValue.getIntVal() - rightValue.getIntVal());
					break;
				case MUL:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					leftValue.setIntVal(leftValue.getIntVal() * rightValue.getIntVal());
					break;
				case DIV:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					if (rightValue.getIntVal() == 0) {
						throw new RunException("divide by zero");
					}
					leftValue.setIntVal(leftValue.getIntVal() / rightValue.getIntVal());
					break;
				case EQUAL:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					leftValue.setIntVal(leftValue.getIntVal() - rightValue.getIntVal() == 0 ? 1 : 0);
					break;
				case NOT_EQUAL:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					leftValue.setIntVal(leftValue.getIntVal() - rightValue.getIntVal() != 0 ? 1 : 0);
					break;
				case GREATER:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					leftValue.setIntVal(leftValue.getIntVal() - rightValue.getIntVal() > 0 ? 1 : 0);
					break;
				case LESS:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					leftValue.setIntVal(leftValue.getIntVal() - rightValue.getIntVal() < 0 ? 1 : 0);
					break;
				case GREATER_OR_EQUAL:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					leftValue.setIntVal(leftValue.getIntVal() - rightValue.getIntVal() >= 0 ? 1 : 0);
					break;
				case LESS_OR_EQUAL:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					leftValue.setIntVal(leftValue.getIntVal() - rightValue.getIntVal() <= 0 ? 1 : 0);
					break;
				case AND:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					leftValue.setIntVal(integerToBoolean(leftValue.getIntVal()) & integerToBoolean(rightValue.getIntVal()));
					break;
				case OR:
					rightValue = loadVariableIfNeeded(cmd.getVal());
					leftValue.setIntVal(integerToBoolean(leftValue.getIntVal()) | integerToBoolean(rightValue.getIntVal()));
					break;
				case JUMP:
					i = changeIndex(cmd);
					continue;
				case JZ:
					if(leftValue.getIntVal() == 0) {
						i = changeIndex(cmd);
						continue;
					}
					break;
				case SHOW:
					System.out.println(leftValue.toString());
					break;
				default:
					throw new RunException("wrong instruction type");
			}
			// нельзя инкрементировать индекс для команд JUMP и JZ
			i++;
		}
	}

	private int changeIndex(Command cmd) throws RunException {
		rightValue = loadVariableIfNeeded(cmd.getVal());
		Integer destinationIndex = rightValue.getIntVal();
		if (destinationIndex == null || destinationIndex < 0 || destinationIndex >= program.size()) {
			throw new RunException("wrong destination index");
		}
		return destinationIndex;
	}

	private int integerToBoolean(int val) {
		return val == 0 ? 0 : 1;
	}

	/**
	 * Если строка в переданном значении содержит имя переменной, то
	 * возвращается значение этой переменной.
	 * Иначе возвращается исходное значение.
	 *
	 * @param val
	 * @return
	 * @throws DefinitionException
	 */
	private TwinValue loadVariableIfNeeded(TwinValue val) throws DefinitionException {
		String strValue = val.getStrVal();
		if (strValue != null && strValue.startsWith("$")) {
			return scope.get(strValue.substring(1));
		}
		return val;
	}
}
