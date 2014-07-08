package nordlang.parser.impl;

import nordlang.exceptions.ParserException;
import nordlang.parser.api.MethodInfo;
import nordlang.parser.api.MethodReader;
import nordlang.parser.api.SourceReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MethodReaderImpl implements MethodReader {

	private SourceReader sourceReader;

	public MethodReaderImpl(String source) {
		sourceReader = new SourceReaderImpl(source);
	}

	@Override
	public MethodInfo readMethod() throws ParserException {
		MethodInfo info = null;
		sourceReader.readWhiteSpace();
		if(!sourceReader.isEnd() && sourceReader.checkForLiteral("def")) {
			sourceReader.readLiteral("def");
			info = new MethodInfo();
			info.setName(sourceReader.readName());
			info.setParams(readParameterNames());
			info.setStatements(expandBlocks(sourceReader.readStatements()));
		}
		return info;
	}

	private List<String> expandBlocks(List<String> source) throws ParserException {
		List<String> statements = new ArrayList<String>();
		UUID blockId = null;
		String prevStatement = "";
		for(String statement : source) {
			SourceReader reader = new SourceReaderImpl(statement);
			if(statement.startsWith("if")) {
				reader.readLiteral("if");
				blockId = UUID.randomUUID();
				statements.add("if " + blockId + " " + reader.readExpressionInBrackets());
				statements.addAll(expandBlocks(reader.readStatements()));
				statements.add("endif " + blockId);
			} else if (statement.startsWith("else")) {
				if(!prevStatement.startsWith("if")) {
					throw new ParserException("else without if");
				}
				reader.readLiteral("else");
				// удаляем лишний endif от первой ветви условия
				statements.remove(statements.size() - 1);
				statements.add("else " + blockId);
				statements.addAll(expandBlocks(reader.readStatements()));
				statements.add("endif " + blockId);
			} else if (statement.startsWith("while")) {
				reader.readLiteral("while");
				blockId = UUID.randomUUID();
				statements.add("while " + blockId + " " + reader.readExpressionInBrackets());
				statements.addAll(expandBlocks(reader.readStatements()));
				statements.add("endwhile " + blockId);
			} else {
				statements.add(statement);
			}
			prevStatement = statement;
		}
		return statements;
	}

	@Override
	public List<String> readParameterNames() throws ParserException {
		List<String> parameters = sourceReader.readParameters();
		List<String> names = new LinkedList<String>();
		for (String param : parameters) {
			SourceReader reader = new SourceReaderImpl(param);
			reader.readLiteral("def");
			reader.readWhiteSpaceRequired();
			names.add(reader.readName());
		}
		return names;
	}

}
