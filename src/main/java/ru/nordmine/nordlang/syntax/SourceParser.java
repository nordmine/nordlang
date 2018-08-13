package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.StringLexer;
import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.lexer.WordToken;
import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.value.Value;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.syntax.statements.DefineArrayStatement;
import ru.nordmine.nordlang.syntax.statements.DefineStatement;
import ru.nordmine.nordlang.syntax.statements.MethodStatement;
import ru.nordmine.nordlang.syntax.statements.SequenceStatement;
import ru.nordmine.nordlang.syntax.statements.Statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SourceParser {

    public static final int NEW_LINE_CONSTANT_INDEX = 1000;

    private MethodInfo currentMethod;

    private Signatures signatures = new Signatures();

    private ParserScope scope = null;
    private ParserContext context;
    private final String source;

    public SourceParser(String source) {
        this.source = source;
    }

    private void loadMethodSignatures() throws SyntaxException {
        context.move();
        while (context.getTag() == Tag.BASIC || context.getTag() == Tag.CONST) {
            if (context.getTag() == Tag.CONST) {
                readConstant();
            } else {
                MethodInfo methodInfo = readMethodSignature();
                context.match(Tag.BEGIN_BLOCK);
                Stack<Tag> blockStack = new Stack<>();
                while (context.getTag() != Tag.END_BLOCK || !blockStack.isEmpty()) {
                    if (context.getTag() == Tag.BEGIN_BLOCK) {
                        blockStack.push(context.getTag());
                    }
                    if (context.getTag() == Tag.END_BLOCK) {
                        blockStack.pop();
                    }
                    context.move();
                }
                if (signatures.containsMethodName(methodInfo.getName())) {
                    throw new SyntaxException(methodInfo.getMethodNameToken().getLine(), "method with name '" + methodInfo.getName() + "' already defined");
                }
                signatures.putMethod(methodInfo);
                context.move();
            }
        }
    }

    private Statement readConstant() throws SyntaxException {
        context.match(Tag.CONST);
        StatementParser statementParser = new StatementParser(context, signatures, currentMethod, scope);
        return statementParser.definition();
    }

    private MethodInfo readMethodSignature() throws SyntaxException {
        StatementParser statementParser = new StatementParser(context, signatures, currentMethod, scope);
        TypeToken returnType = statementParser.type();
        WordToken methodNameToken = (WordToken) context.getLook();
        MethodInfo methodInfo = new MethodInfo(returnType, methodNameToken);
        context.match(Tag.ID);
        context.match(Tag.OPEN_BRACKET);
        while (context.getTag() != Tag.CLOSE_BRACKET) {
            TypeToken paramType = statementParser.type();
            WordToken paramToken = (WordToken) context.getLook();
            context.match(Tag.ID);
            methodInfo.addParam(paramType, paramToken);
            if (context.getTag() != Tag.COMMA) {
                break;
            }
            context.match(Tag.COMMA);
        }
        context.match(Tag.CLOSE_BRACKET);
        return methodInfo;
    }

    public Program createProgram() throws SyntaxException {
        context = new ParserContext(new StringLexer(source).getAllTokens());
        scope = new ParserScope(null);
        loadMethodSignatures();
        Program program = new Program();

        WordToken wordToken = new WordToken(Tag.ID, "newLine");
        scope.put(wordToken, new VariableExpression(wordToken, TypeToken.STRING, NEW_LINE_CONSTANT_INDEX));

        context.resetIndex();

        MethodInfo mainMethodInfo = signatures.getMethodByParamTypes("main", Collections.emptyList())
                .orElseThrow(() -> new SyntaxException("method int main() is not defined"));

        // все константы объявляются перед методами
        while (context.getTag() == Tag.CONST) {
            Statement s = readConstant();
            Label begin = program.newLabel();
            Label after = program.newLabel();
            s.gen(program, begin, after);
        }

        program.addGotoCommand(mainMethodInfo.getBeginLabel());

        while (context.getTag() == Tag.BASIC) {
            Label begin = program.newLabel();
            Label after = program.newLabel();
            program.fixLabel(begin);
            Statement s = method();
            program.fixLabel(currentMethod.getBeginLabel());
            s.gen(program, begin, after);
            program.fixLabel(after);
            checkForMissingReturn(program, after);
        }

        program.setStartCommandIndex(0);
        return program;
    }

    private void checkForMissingReturn(Program program, Label afterLabel) throws SyntaxException {
        // todo улучшить контроль за наличием возвращаемых значений
        long missingReturnCount = program.getCommands().stream()
                .filter(c -> c.getDestinationLabel() == afterLabel)
                .count();
        if (missingReturnCount > 0 || !currentMethod.hasReturnStatement()) {
            throw new SyntaxException(currentMethod.getMethodNameToken().getLine(), "missing return statement");
        }
    }

    private Statement method() throws SyntaxException {
        MethodInfo methodInfo = readMethodSignature();
        currentMethod = signatures.getMethodByParamList(methodInfo.getName(), methodInfo.getParams()).get();
        ParserScope parentScope = scope;
        scope = new ParserScope(scope);
        List<VariableExpression> paramExprList = new ArrayList<>();
        Statement s = Statement.EMPTY;
        for (ParamInfo paramInfo : currentMethod.getParams()) {
            VariableExpression variable = new VariableExpression(paramInfo.getId(), paramInfo.getType(), scope.getUniqueIndexSequence());
            paramExprList.add(variable);

            Statement define;
            if (paramInfo.getType().getTag() == Tag.INDEX) {
                Value initialValue = ParserUtils.getInitialValueByToken(paramInfo.getType().getArrayType());
                define = new DefineArrayStatement(variable, initialValue);
            } else {
                Value initialValue = ParserUtils.getInitialValueByToken(paramInfo.getType());
                define = new DefineStatement(variable, initialValue); // todo initialValue реализовать в Value?
            }
            s = new SequenceStatement(s, define);

            scope.put(paramInfo.getId(), variable);
        }
        context.match(Tag.BEGIN_BLOCK);
        StatementParser statementParser = new StatementParser(context, signatures, currentMethod, scope);
        s = new SequenceStatement(s, new MethodStatement(paramExprList, statementParser.statements()));
        context.match(Tag.END_BLOCK);
        scope = parentScope;
        return s;
    }
}
