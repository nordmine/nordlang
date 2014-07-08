package nordlang.parser.impl;

import nordlang.parser.api.PostfixConverter;
import nordlang.parser.api.SourceReader;
import nordlang.exceptions.ParserException;

import java.util.*;

public class PostfixConverterImpl implements PostfixConverter {

	private Stack<String> operatorStack;
	private List<String> postfix;
	private static final Map<String, Integer> signPriorities = new HashMap<String, Integer>();

	public PostfixConverterImpl() {
		signPriorities.put("and", 1);

		signPriorities.put("or", 2);

		signPriorities.put("==", 3);
		signPriorities.put("<>", 3);

		signPriorities.put(">", 4);
		signPriorities.put("<", 4);
		signPriorities.put(">=", 4);
		signPriorities.put("<=", 4);

		signPriorities.put("+", 5);
		signPriorities.put("-", 5);

		signPriorities.put("*", 6);
		signPriorities.put("/", 6);
	}

	@Override
	public List<String> convert(String infix) throws ParserException {
		operatorStack = new Stack<String>();
		postfix = new LinkedList<String>();
		SourceReader reader = new SourceReaderImpl(infix);
		while (!reader.isEnd()) {
			if (reader.checkForMathSign()) {
				String mathSign = reader.readMathSign();
				// todo чтение отрицательных чисел
				processOperation(mathSign);
			} else if (reader.checkForLiteral("(")) {
				operatorStack.push(reader.readLiteral("("));
			} else if (reader.checkForLiteral(")")) {
				reader.readLiteral(")");
				appendAllOperationsFromBracket();
			} else if (reader.checkForInteger()) {
				postfix.add(Integer.toString(reader.readInteger()));
			} else if (reader.checkForLiteral("'")) {
				postfix.add("'" + reader.readStringLiteral());
			} else {
				postfix.add(reader.readName());
			}
		}

		// все оставшиеся операторы вытаскиваем из стека
		// и добавляем в конец постфиксной записи
		while (!operatorStack.isEmpty()) {
			postfix.add(operatorStack.pop());
		}

		return postfix;
	}

	private void processOperation(String current) {
		int currentPriority = signPriorities.get(current);
		while (!operatorStack.isEmpty()) {
			String stackTop = operatorStack.pop();
			if (stackTop.equals("(")) {
				// открывающую скобку возвращаем в стек и выходим из цикла
				operatorStack.push(stackTop);
				break;
			} else {
				int stackTopPriority = signPriorities.get(stackTop);
				if (stackTopPriority < currentPriority) {
					// если на вершине стека была менее приоритетная операция,
					// возвращаем её в стек и выходим из цикла
					operatorStack.push(stackTop);
					break;
				} else {
					// если на вершине была более приоритетная операция,
					// добавляем её в постфиксную запись
					postfix.add(stackTop);
				}
			}
		}
		// сохраняем текущую операцию в стеке
		operatorStack.push(current);
	}

	private void appendAllOperationsFromBracket() {
		while (!operatorStack.isEmpty()) {
			String item = operatorStack.pop();
			if (item.equals("(")) {
				break;
			} else {
				postfix.add(item);
			}
		}
	}

}
