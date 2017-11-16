package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.Lexer;
import ru.nordmine.nordlang.lexer.StringLexer;
import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.lexer.Token;
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
    private boolean hasReturnStatement;

    private Signatures signatures = new Signatures();

    private ParserScope top = null;
    private final ParserContext context;

    public SourceParser(String source) {
        this.context = new ParserContext(new StringLexer(source));
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
                    throw new SyntaxException(line, "method with name '" + methodInfo.getName() + "' already defined");
                }
                signatures.putMethod(methodInfo);
                context.move();
            }
        }
    }

    private Statement readConstant() throws SyntaxException {
        context.match(Tag.CONST);
        StatementParser statementParser = new StatementParser(context);
        return definition();
    }

    private MethodInfo readMethodSignature() throws SyntaxException {
        TypeToken returnType = type();
        WordToken methodNameToken = (WordToken) look;
        MethodInfo methodInfo = new MethodInfo(returnType, methodNameToken.getLexeme());
        match(Tag.ID);
        match(Tag.OPEN_BRACKET);
        while (look.getTag() != Tag.CLOSE_BRACKET) {
            TypeToken paramType = type();
            WordToken paramToken = (WordToken) look;
            match(Tag.ID);
            methodInfo.addParam(paramType, paramToken);
            if (look.getTag() != Tag.COMMA) {
                break;
            }
            match(Tag.COMMA);
        }
        match(Tag.CLOSE_BRACKET);
        return methodInfo;
    }

    public Program createProgram() throws SyntaxException {
        top = new ParserScope(null);
        loadMethodSignatures();
        Program program = new Program();

        WordToken wordToken = new WordToken(Tag.ID, "newLine");
        top.put(wordToken, new VariableExpression(line, wordToken, TypeToken.STRING, NEW_LINE_CONSTANT_INDEX));

        move();

        MethodInfo mainMethodInfo = signatures.getMethodByParamTypes("main", Collections.emptyList())
                .orElseThrow(() -> new SyntaxException(line, "method int main() is not defined"));

        // все константы объявляются перед методами
        while (look.getTag() == Tag.CONST) {
            Statement s = readConstant();
            Label begin = program.newLabel();
            Label after = program.newLabel();
            s.gen(program, begin, after);
            program.addGotoCommand(mainMethodInfo.getBeginLabel());
        }

        while (look.getTag() == Tag.BASIC) {
            hasReturnStatement = false;
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
        if (missingReturnCount > 0 || !hasReturnStatement) {
            throw new SyntaxException(line, "missing return statement");
        }
    }

    private Statement method() throws SyntaxException {
        MethodInfo methodInfo = readMethodSignature();
        currentMethod = signatures.getMethodByParamList(methodInfo.getName(), methodInfo.getParams()).get();
        ParserScope parentScope = top;
        top = new ParserScope(top);
        List<VariableExpression> paramExprList = new ArrayList<>();
        Statement s = Statement.EMPTY;
        for (ParamInfo paramInfo : currentMethod.getParams()) {
            VariableExpression variable = new VariableExpression(line, paramInfo.getId(), paramInfo.getType(), top.getUniqueIndexSequence());
            paramExprList.add(variable);

            Statement define;
            if (paramInfo.getType().getTag() == Tag.INDEX) {
                Value initialValue = ParserUtils.getInitialValueByToken(line, paramInfo.getType().getArrayType());
                define = new DefineArrayStatement(line, variable, initialValue);
            } else {
                Value initialValue = ParserUtils.getInitialValueByToken(line, paramInfo.getType());
                define = new DefineStatement(line, variable, initialValue); // todo initialValue реализовать в Value?
            }
            s = new SequenceStatement(line, s, define);

            top.put(paramInfo.getId(), variable);
        }
        match(Tag.BEGIN_BLOCK);
        s = new SequenceStatement(line, s, new MethodStatement(line, paramExprList, statements()));
        match(Tag.END_BLOCK);
        top = parentScope;
        return s;
    }



}
