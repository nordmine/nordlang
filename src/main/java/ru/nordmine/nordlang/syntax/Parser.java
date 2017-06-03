package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.*;
import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.value.Value;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.*;
import ru.nordmine.nordlang.syntax.expressions.logical.AndExpression;
import ru.nordmine.nordlang.syntax.expressions.logical.NotExpression;
import ru.nordmine.nordlang.syntax.expressions.logical.OrExpression;
import ru.nordmine.nordlang.syntax.expressions.logical.RelExpression;
import ru.nordmine.nordlang.syntax.expressions.operators.AccessExpression;
import ru.nordmine.nordlang.syntax.expressions.operators.BinaryExpression;
import ru.nordmine.nordlang.syntax.expressions.operators.UnaryExpression;
import ru.nordmine.nordlang.syntax.statements.*;
import ru.nordmine.nordlang.syntax.statements.SetStatement;

import java.util.*;
import java.util.stream.Collectors;

public class Parser {

    public static final int NEW_LINE_CONSTANT_INDEX = 1000;

    private final String source;
    private Lexer lexer;
    private Token look; // предпросмотр
    private ParserScope top = null;
    private int line = 0;
    private MethodInfo currentMethod;
    private Signatures signatures = new Signatures();
    private boolean hasReturnStatement;

    public Parser(String source) {
        this.source = source;
    }

    private void loadMethodSignatures() throws SyntaxException {
        this.lexer = new Lexer(source);
        move();
        while (look.getTag() == Tag.BASIC) {
            MethodInfo methodInfo = readMethodSignature();
            match(Tag.BEGIN_BLOCK);
            Stack<Tag> blockStack = new Stack<>();
            while (look.getTag() != Tag.END_BLOCK || !blockStack.isEmpty()) {
                if (look.getTag() == Tag.BEGIN_BLOCK) {
                    blockStack.push(look.getTag());
                }
                if (look.getTag() == Tag.END_BLOCK) {
                    blockStack.pop();
                }
                move();
            }
            if (signatures.containsMethodName(methodInfo.getName())) {
                throw new SyntaxException(line, "method with name '" + methodInfo.getName() + "' already defined");
            }
            signatures.putMethod(methodInfo);
            move();
        }
    }

    public Program createProgram() throws SyntaxException {
        loadMethodSignatures();
        this.lexer = new Lexer(source);
        Program program = new Program();

        top = new ParserScope(null);
        WordToken wordToken = new WordToken(Tag.ID, "newLine");
        top.put(wordToken, new VariableExpression(line, wordToken, TypeToken.STRING, NEW_LINE_CONSTANT_INDEX));

        move();
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
        MethodInfo mainMethodInfo = signatures.getMethodByParamTypes("main", Collections.emptyList())
                .orElseThrow(() -> new SyntaxException(line, "method int main() is not defined"));

        program.setStartCommandIndex(mainMethodInfo.getBeginLabel().getPosition());
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

    private Statement block() throws SyntaxException {
        match(Tag.BEGIN_BLOCK);
        ParserScope savedParserScope = top;
        top = new ParserScope(top);
        Statement s = new SequenceStatement(line, new PushScopeStatement(line), statements());
        match(Tag.END_BLOCK);
        top = savedParserScope;
        return s;
    }

    private Statement statements() throws SyntaxException {
        if (look.getTag() == Tag.END_BLOCK) {
            return new PopScopeStatement(line);
        } else {
            return new SequenceStatement(line, statement(), statements());
        }
    }

    private Statement statement() throws SyntaxException {
        Expression x;
        Statement s1, s2;
        Statement savedStatement; // сохранение охватывающей конструкции для break

        switch (look.getTag()) {
            case SEMICOLON:
                move();
                return Statement.EMPTY;
            case BASIC:
                return definition();
            case IF:
                match(Tag.IF);
                match(Tag.OPEN_BRACKET);
                x = bool();
                match(Tag.CLOSE_BRACKET);
                s1 = statement();
                if (look.getTag() != Tag.ELSE) {
                    return new IfStatement(line, x, s1);
                }
                match(Tag.ELSE);
                s2 = statement();
                return new ElseStatement(lexer.getLine(), x, s1, s2);
            case WHILE:
                WhileStatement whileStatement = new WhileStatement(line);
                savedStatement = Statement.Enclosing;
                Statement.Enclosing = whileStatement;
                match(Tag.WHILE);
                match(Tag.OPEN_BRACKET);
                x = bool();
                match(Tag.CLOSE_BRACKET);
                s1 = statement();
                whileStatement.init(x, s1);
                Statement.Enclosing = savedStatement;
                // reset statement.enclosing
                return whileStatement;
            case BREAK:
                match(Tag.BREAK);
                match(Tag.SEMICOLON);
                return new BreakStatement(line);
            case ECHO:
                match(Tag.ECHO);
                x = bool();
                return new EchoStatement(line, x);
            case BEGIN_BLOCK:
                return block();
            case RETURN:
                match(Tag.RETURN);
                x = bool();
                if (!currentMethod.getReturnType().equals(x.getType())) {
                    throw new SyntaxException(line, "method should return " + currentMethod.getReturnType() + ", but was " + x.getType());
                }
                hasReturnStatement = true;
                return new ReturnStatement(line, x, currentMethod);
            default:
                // инструкция присваивания начинается с имени переменной, а не с ключевого слова
                return assign();
        }
    }

    private Statement definition() throws SyntaxException {
        TypeToken typeToken = type();
        WordToken variableToken = (WordToken) look;
        match(Tag.ID);
        VariableExpression variable = new VariableExpression(line, variableToken, typeToken, top.getUniqueIndexSequence());
        top.put(variableToken, variable);

        Statement statement = Statement.EMPTY;
        match(Tag.ASSIGN);
        if (look.getTag() == Tag.OPEN_SQUARE) {
            // объявление массива через квадратные скобки
            statement = arrayDefinition((ArrayToken) typeToken, variable, statement);
        } else {
            // здесь тоже может объявляться массив (например, через вызов метода или через другую переменную)
            Value initialValue = ParserUtils.getInitialValueByToken(line, typeToken);
            DefineStatement defineStatement = new DefineStatement(line, variable, initialValue);
            statement = new SequenceStatement(line, defineStatement, new SetStatement(line, variable, bool()));
        }
        match(Tag.SEMICOLON);

        return statement;
    }

    private Statement arrayDefinition(ArrayToken t, VariableExpression variable, Statement statement) throws SyntaxException {
        move();
        TypeToken type = t.getArrayType();
        while (look.getTag() != Tag.CLOSE_SQUARE) { // одна итерация - один элемент массива
            Expression elementExpression = bool();
            if (type != elementExpression.getType()) {
                ParserUtils.typeError(line, type, elementExpression.getType());
            }
            statement = new SequenceStatement(line, statement, new AddElementStatement(line, variable, elementExpression));
            if (look.getTag() != Tag.COMMA) {
                break;
            }
            match(Tag.COMMA);
        }
        match(Tag.CLOSE_SQUARE);
        Value initialValue = ParserUtils.getInitialValueByToken(line, t.getArrayType());
        DefineArrayStatement defineArrayStatement = new DefineArrayStatement(line, variable, initialValue);
        statement = new SequenceStatement(line, defineArrayStatement, statement);
        return statement;
    }

    private TypeToken type() throws SyntaxException {
        TypeToken t = (TypeToken) look;
        match(Tag.BASIC);
        if (look.getTag() != Tag.OPEN_SQUARE) {
            return t;
        } else {
            return dimension(t);
        }
    }

    private TypeToken dimension(TypeToken t) throws SyntaxException {
        match(Tag.OPEN_SQUARE);
        match(Tag.CLOSE_SQUARE);
        if (look.getTag() == Tag.OPEN_SQUARE) {
            t = dimension(t);
        }
        return new ArrayToken(t);
    }

    private Statement assign() throws SyntaxException {
        Token variableToken = look;
        match(Tag.ID);
        VariableExpression variable = top.get(variableToken);
        if (variable == null) {
            error(variableToken.toString() + " undefined");
        }

        Statement statement;
        if (look.getTag() == Tag.ASSIGN) {
            move();
            statement = new SetStatement(line, variable, bool());
        } else if (look.getTag() == Tag.INCREMENT) {
            move();
            Expression binExpr = new BinaryExpression(line, new Token(Tag.PLUS), variable, new ConstantExpression(line, 1));
            statement = new SetStatement(line, variable, binExpr);
        } else if (look.getTag() == Tag.DECREMENT) {
            move();
            Expression binExpr = new BinaryExpression(line, new Token(Tag.MINUS), variable, new ConstantExpression(line, 1));
            statement = new SetStatement(line, variable, binExpr);
        } else {
            AccessExpression indexedVariable = offset(variable);
            match(Tag.ASSIGN);
            if (indexedVariable == null) {
                // [] - добавление нового элемента в массив
                Expression elemExpr = bool();
                TypeToken arrayType = variable.getType().getArrayType();
                if (arrayType != elemExpr.getType()) {
                    ParserUtils.typeError(line, arrayType, elemExpr.getType());
                }
                statement = new AddElementStatement(line, variable, elemExpr);
            } else {
                // [x] - установка в элемент x нового значения
                statement = new SetElementStatement(line, indexedVariable, bool());
            }
        }
        match(Tag.SEMICOLON);
        return statement;
    }

    // Boolean operators

    private Expression bool() throws SyntaxException {
        Expression x = join();
        while (look.getTag() == Tag.OR) {
            Token token = look;
            move();
            x = new OrExpression(line, token, x, join());
        }
        return x;
    }

    private Expression join() throws SyntaxException {
        Expression x = equality();
        while (look.getTag() == Tag.AND) {
            Token token = look;
            move();
            x = new AndExpression(line, token, x, equality());
        }
        return x;
    }

    private Expression equality() throws SyntaxException {
        Expression x = rel();
        while (look.getTag() == Tag.EQUAL || look.getTag() == Tag.NOT_EQUAL) {
            Token token = look;
            move();
            x = new RelExpression(line, token, x, rel());
        }
        return x;
    }

    private Expression rel() throws SyntaxException {
        Expression x = expr();
        switch (look.getTag()) {
            case LESS:
            case LESS_OR_EQUAL:
            case GREATER_OR_EQUAL:
            case GREATER:
                Token token = look;
                move();
                return new RelExpression(line, token, x, expr());
            default:
                return x;
        }
    }

    // Math operators

    private Expression expr() throws SyntaxException {
        Expression x = term();
        while (look.getTag() == Tag.PLUS || look.getTag() == Tag.MINUS) {
            Token token = look;
            move();
            x = new BinaryExpression(line, token, x, term());
        }
        return x;
    }

    private Expression term() throws SyntaxException {
        Expression x = unary();
        while (look.getTag() == Tag.MUL || look.getTag() == Tag.DIVISION || look.getTag() == Tag.MOD) {
            Token token = look;
            move();
            x = new BinaryExpression(line, token, x, unary());
        }
        return x;
    }

    private Expression unary() throws SyntaxException {
        if (look.getTag() == Tag.MINUS) {
            move();
            return new UnaryExpression(line, WordToken.UNARY_MINUS, unary());
        } else if (look.getTag() == Tag.NOT) {
            Token token = look;
            move();
            return new NotExpression(line, token, unary());
        } else {
            return factor();
        }
    }

    private Expression factor() throws SyntaxException {
        Expression x = null;
        switch (look.getTag()) {
            case OPEN_BRACKET:
                move();
                x = bool();
                match(Tag.CLOSE_BRACKET);
                return x;
            case TRUE:
                x = ConstantExpression.TRUE;
                move();
                return x;
            case FALSE:
                x = ConstantExpression.FALSE;
                move();
                return x;
            case INT:
                x = new ConstantExpression(line, look, TypeToken.INT);
                move();
                return x;
            case CHAR:
                x = new ConstantExpression(line, look, TypeToken.CHAR);
                move();
                return x;
            case STRING:
                x = new ConstantExpression(line, look, TypeToken.STRING);
                move();
                return x;
            case SIZE:
                match(Tag.SIZE);
                match(Tag.OPEN_BRACKET);
                Expression arg = bool();
                if (arg.getType() != TypeToken.STRING && !arg.getType().getTag().equals(Tag.INDEX)) {
                    throw new SyntaxException(line, "string or array required for size operator, but was " + arg.getType());
                }
                match(Tag.CLOSE_BRACKET);
                return new SizeExpression(line, arg);
            case ID:
                WordToken wordToken = (WordToken) look;
                move();
                if (look.getTag() == Tag.OPEN_BRACKET) {
                    // если за именем идёт круглая скобка, считаем, что это имя метода (иначе - переменная)
                    String methodName = wordToken.getLexeme();
                    move();
                    List<Expression> paramExpressions = new ArrayList<>();
                    List<TypeToken> paramTypes = new ArrayList<>();
                    while (look.getTag() != Tag.CLOSE_BRACKET) {
                        Expression paramExpr = bool();
                        paramTypes.add(paramExpr.getType());
                        paramExpressions.add(paramExpr);
                        if (look.getTag() != Tag.COMMA) {
                            break;
                        }
                        match(Tag.COMMA);
                    }
                    match(Tag.CLOSE_BRACKET);
                    MethodInfo methodInfo = signatures.getMethodByParamTypes(methodName, paramTypes)
                            .orElseThrow(() -> {
                                String typeList = paramTypes.stream()
                                        .map(WordToken::getLexeme)
                                        .collect(Collectors.joining(", "));
                                return new SyntaxException(
                                        line, String.format("method %s(%s) is not defined", methodName, typeList)
                                );
                            });
                    return new MethodCallExpression(line, wordToken, methodInfo, paramExpressions);
                } else {
                    VariableExpression variable = top.get(wordToken);
                    if (variable == null) {
                        error(String.format("variable '%s' is not defined", wordToken.toString()));
                    }
                    if (look.getTag() == Tag.OPEN_SQUARE) {
                        AccessExpression accExpr = offset(variable);
                        if (accExpr == null) {
                            throw new SyntaxException(line, "index expected");
                        }
                        return  accExpr;
                    } else {
                        return variable;
                    }
                }
            default:
                error("unexpected token: " + look);
                return x;
        }
    }

    private AccessExpression offset(VariableExpression a) throws SyntaxException {
        TypeToken type = a.getType();
        if (type.getArrayType() == null) {
            throw new SyntaxException(line, "array type expected");
        }
        match(Tag.OPEN_SQUARE);
        if (look.getTag() == Tag.CLOSE_SQUARE) {
            match(Tag.CLOSE_SQUARE);
            return null;
        } else {
            Expression indexExpr = bool();
            match(Tag.CLOSE_SQUARE);
            return new AccessExpression(line, a, indexExpr, type.getArrayType());
        }
    }

    // утилитарные методы

    private void move() throws SyntaxException {
        look = lexer.nextToken();
        line = lexer.getLine();
    }

    private void error(String s) throws SyntaxException {
        throw new SyntaxException(lexer.getLine(), s);
    }

    private void match(Tag t) throws SyntaxException {
        if (look.getTag() == t) {
            move();
        } else {
            error(String.format("Tag %s is expected, but was %s", t, look.getTag()));
        }
    }
}
