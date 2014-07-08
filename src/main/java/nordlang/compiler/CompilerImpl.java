package nordlang.compiler;

import com.google.common.base.CharMatcher;
import nordlang.exceptions.ParserException;
import nordlang.parser.api.MethodInfo;
import nordlang.parser.api.PostfixConverter;
import nordlang.parser.api.SourceReader;
import nordlang.parser.impl.PostfixConverterImpl;
import nordlang.parser.impl.SourceReaderImpl;
import nordlang.vm.CommandType;
import nordlang.vm.TwinValue;
import nordlang.vm.api.Program;
import nordlang.vm.impl.ProgramImpl;

import java.util.*;

public class CompilerImpl implements Compiler {

	private PostfixConverter postfixConverter = new PostfixConverterImpl();

	@Override
	public Program compile(List<MethodInfo> parsedMethods)
			throws ParserException {
		Program program = new ProgramImpl();
		MethodInfo startMethod = selectStartMethod(parsedMethods);
		// здесь хранятся значения, в которые нужно подставлять адреса окончания блоков
		// ключом является уникальный номер блока
		Map<String, BlockInfo> blockInfoMap = new HashMap<String, BlockInfo>();
		for (String statement : startMethod.getStatements()) {
			SourceReader reader = new SourceReaderImpl(statement);
			if (reader.checkForLiteral("def")) {
				reader.readLiteral("def");
				String name = reader.readName();
				reader.readLiteral("=");
				program.appendProgram(convertExpression(reader.readToEnd()));
				program.addCommand(CommandType.DEF, name);
			} else if (reader.checkForLiteral("show")) {
				reader.readLiteral("show");
				program.appendProgram(convertExpression(reader.readToEnd()));
				program.addShowCommand();
			} else if (reader.checkForLiteral("if ")) { // if id_блока выражение
				reader.readLiteral("if");
				String blockId = reader.readString();
				program.appendProgram(convertExpression(reader.readToEnd()));
				TwinValue endBlockAddress = new TwinValue(0);
				program.addCommand(CommandType.JZ, endBlockAddress);
				BlockInfo info = new BlockInfo();
				// сюда нужно будет вставить адрес конца первой ветви условия
				info.setFirstAddress(endBlockAddress);
				blockInfoMap.put(blockId, info);
			} else if (reader.checkForLiteral("else ")) { // else id_блока, необязательный элемент
				reader.readLiteral("else");
				String blockId = reader.readString();
				TwinValue endBlockAddress = new TwinValue(0);
				program.addCommand(CommandType.JUMP, endBlockAddress);
				blockInfoMap.get(blockId).getFirstAddress().setIntVal(program.size());
				blockInfoMap.get(blockId).setSecondAddress(endBlockAddress);
			} else if (reader.checkForLiteral("endif ")) { // endif id_блока
				reader.readLiteral("endif");
				String blockId = reader.readString();
				// есть ли в данной конструкции ветка else
				// todo ошибка, если последняя строка в методе
				if(blockInfoMap.get(blockId).getSecondAddress() == null) {
					blockInfoMap.get(blockId).getFirstAddress().setIntVal(program.size());
				} else {
					blockInfoMap.get(blockId).getSecondAddress().setIntVal(program.size());
				}
			} else if (reader.checkForLiteral("while ")) {
				reader.readLiteral("while");
				String blockId = reader.readString();
				BlockInfo info = new BlockInfo();
				info.setFirstAddress(new TwinValue(program.size()));
				program.appendProgram(convertExpression(reader.readToEnd()));
				TwinValue endBlockAddress = new TwinValue(0);
				program.addCommand(CommandType.JZ, endBlockAddress);
				info.setSecondAddress(endBlockAddress);
				blockInfoMap.put(blockId, info);
			} else if (reader.checkForLiteral("endwhile ")) {
				reader.readLiteral("endwhile");
				String blockId = reader.readString();
				program.addCommand(CommandType.JUMP, blockInfoMap.get(blockId).getFirstAddress());
				blockInfoMap.get(blockId).getSecondAddress().setIntVal(program.size());
			} else {
				String name = reader.readName();
				reader.readLiteral("=");
				program.appendProgram(convertExpression(reader.readToEnd()));
				program.addCommand(CommandType.SET, name);
			}
		}
		return program;
	}

	private MethodInfo selectStartMethod(List<MethodInfo> parsedMethods) throws ParserException {
		MethodInfo startMethod = null;
		for(MethodInfo method : parsedMethods) {
			if(method.getName().equals("main")) {
				startMethod = method;
				break;
			}
		}

		if(startMethod == null) {
			throw new ParserException("unable to find main method with signature: def main() {}");
		}
		return startMethod;
	}

	private Program convertExpression(String expression) throws ParserException {
		List<String> postfix = postfixConverter.convert(expression);
		Program program = new ProgramImpl();

		// создаём временную переменную со случайным именем
		// в ней будут сохраняться промежуточные вычисления
		String tempVariable = UUID.randomUUID().toString();
		program.addCommand(CommandType.GET, 0);
		program.addCommand(CommandType.DEF, tempVariable);

		for (String item : postfix) {
			if (CharMatcher.DIGIT.matchesAllOf(item)) {
				program.addCommand(CommandType.GET, Integer.parseInt(item));
				program.addPushCommand();
			} else if (item.startsWith("'")) {
				program.addCommand(CommandType.GET, item.substring(1));
				program.addPushCommand();
			} else if (SourceReaderImpl.MATH_SIGN.contains(item)) {
				program.addPopCommand();
				program.addCommand(CommandType.SET, tempVariable);
				program.addPopCommand();
				TwinValue val = new TwinValue("$" + tempVariable);
				if (item.equals("+")) {
					program.addCommand(CommandType.PLUS, val);
				} else if (item.equals("-")) {
					program.addCommand(CommandType.MINUS, val);
				} else if (item.equals("*")) {
					program.addCommand(CommandType.MUL, val);
				} else if (item.equals("/")) {
					program.addCommand(CommandType.DIV, val);
				} else if(item.equals("==")) {
					program.addCommand(CommandType.EQUAL, val);
				} else if(item.equals("<>")) {
					program.addCommand(CommandType.NOT_EQUAL, val);
				} else if(item.equals(">")) {
					program.addCommand(CommandType.GREATER, val);
				} else if(item.equals("<")) {
					program.addCommand(CommandType.LESS, val);
				} else if(item.equals(">=")) {
					program.addCommand(CommandType.GREATER_OR_EQUAL, val);
				} else if(item.equals("<=")) {
					program.addCommand(CommandType.LESS_OR_EQUAL, val);
				} else if(item.equals("and")) {
					program.addCommand(CommandType.AND, val);
				} else if(item.equals("or")) {
					program.addCommand(CommandType.OR, val);
				}
				program.addPushCommand();
			} else if (CharMatcher.JAVA_LETTER_OR_DIGIT.matchesAllOf(item)) {
				program.addCommand(CommandType.GET, "$" + item);
				program.addPushCommand();
			} else {
				throw new ParserException("Unable to detect type of operator");
			}
		}

		// удаляем временную переменную
		program.addCommand(CommandType.GET, tempVariable);
		program.addUnDefCommand();

		program.addPopCommand();
		return program;
	}
}
