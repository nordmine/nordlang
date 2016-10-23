package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.*;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.value.Value;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.ConstantExpression;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.syntax.expressions.MethodCallExpression;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.syntax.expressions.logical.And;
import ru.nordmine.nordlang.syntax.expressions.logical.Not;
import ru.nordmine.nordlang.syntax.expressions.logical.Or;
import ru.nordmine.nordlang.syntax.expressions.logical.Rel;
import ru.nordmine.nordlang.syntax.expressions.operators.Access;
import ru.nordmine.nordlang.syntax.expressions.operators.BinaryOperator;
import ru.nordmine.nordlang.syntax.expressions.operators.UnaryOperator;
import ru.nordmine.nordlang.syntax.statements.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {

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
            if (signatures.contains(methodInfo.getName())) {
                throw new SyntaxException(line, "method '" + methodInfo.getName() + "()' already defined");
            }
            signatures.putMethod(methodInfo);
            move();
        }
    }

    public Program createProgram() throws SyntaxException {
        loadMethodSignatures();
        this.lexer = new Lexer(source);
        Program program = new Program();
        move();
        while (look.getTag() == Tag.BASIC) {
            hasReturnStatement = false;
            int begin = program.newLabel();
            int after = program.newLabel();
            program.fixLabel(begin); // todo изменить механизм отложенного проставления меток
            Statement s = method();
            currentMethod.setBeginLabel(program.getLabel(begin));
            s.gen(program, begin, after);
            program.fixLabel(after);
            checkForMissingReturn(program, after);
        }
        MethodInfo mainMethodInfo = signatures.getMainMethod();
        if (mainMethodInfo == null) {
            throw new SyntaxException(line, "method 'int main()' not found");
        }
        program.setStartCommandIndex(mainMethodInfo.getBeginLabel().getDstPosition());
        return program;
    }

    private void checkForMissingReturn(Program program, int afterLabelIndex) throws SyntaxException {
        // todo улучшить контроль за наличием возвращаемых значений
        long missingReturnCount = program.getCommands().stream()
                .filter(c -> c.getDestinationLabel() == program.getLabel(afterLabelIndex))
                .count();
        if (missingReturnCount > 0 || !hasReturnStatement) {
            throw new SyntaxException(line, "missing return statement");
        }
    }

    private Statement method() throws SyntaxException {
        MethodInfo methodInfo = readMethodSignature();
        currentMethod = signatures.getMethodByName(methodInfo.getName()); // todo лишнее действие
        top = new ParserScope(null);
        List<VariableExpression> paramExprList = new ArrayList<>();
        Statement s = Statement.EMPTY;
        for (ParamInfo paramInfo : currentMethod.getParams()) {
            VariableExpression variable = new VariableExpression(line, paramInfo.getId(), paramInfo.getType(), top.getUniqueIndexSequence());
            paramExprList.add(variable);

            Statement define;
            if (paramInfo.getType().getTag() == Tag.INDEX) {
                Value initialValue = ParserUtils.getInitialValueByToken(line, ((ArrayToken) paramInfo.getType()).getArrayType());
                define = new DefineArray(line, variable, 0, initialValue);
            } else {
                Value initialValue = ParserUtils.getInitialValueByToken(line, paramInfo.getType());
                define = new Define(line, variable, initialValue); // todo initialValue реализовать в Value?
            }
            s = new Seq(line, s, define);

            top.put(paramInfo.getId(), variable);
        }
        match(Tag.BEGIN_BLOCK);
        s = new Seq(line, s, new MethodStatement(line, paramExprList, statements()));
        match(Tag.END_BLOCK);
        top = null;
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
        Statement s = new Seq(line, new PushScope(line), statements());
        match(Tag.END_BLOCK);
        top = savedParserScope;
        return s;
    }

    private Statement statements() throws SyntaxException {
        if (look.getTag() == Tag.END_BLOCK) {
            return new PopScope(line);
        } else {
            return new Seq(line, statement(), statements());
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
                    return new If(line, x, s1);
                }
                match(Tag.ELSE);
                s2 = statement();
                return new Else(lexer.getLine(), x, s1, s2);
            case WHILE:
                While whileNode = new While(line);
                savedStatement = Statement.Enclosing;
                Statement.Enclosing = whileNode;
                match(Tag.WHILE);
                match(Tag.OPEN_BRACKET);
                x = bool();
                match(Tag.CLOSE_BRACKET);
                s1 = statement();
                whileNode.init(x, s1);
                Statement.Enclosing = savedStatement;
                // reset statement.enclosing
                return whileNode;
            case BREAK:
                match(Tag.BREAK);
                match(Tag.SEMICOLON);
                return new Break(line);
            case ECHO:
                match(Tag.ECHO);
                x = bool();
                return new Echo(line, x);
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
            Define define = new Define(line, variable, initialValue);
            statement = new Seq(line, define, new Set(line, variable, bool()));
        }
        match(Tag.SEMICOLON);

        return statement;
    }

    private Statement arrayDefinition(ArrayToken t, VariableExpression variable, Statement statement) throws SyntaxException {
        move();
        int inner = 0;
        while (look.getTag() != Tag.CLOSE_SQUARE) { // одна итерация - один элемент массива
            TypeToken type = t.getArrayType();
            Access x = new Access(line, variable, new ConstantExpression(line, inner), type);
            statement = new Seq(line, statement, new SetElem(line, x, bool()));
            inner++;
            if (look.getTag() != Tag.COMMA) {
                break;
            }
            match(Tag.COMMA);
        }
        match(Tag.CLOSE_SQUARE);
        Value initialValue = ParserUtils.getInitialValueByToken(line, t.getArrayType());
        DefineArray defineArray = new DefineArray(line, variable, inner, initialValue);
        statement = new Seq(line, defineArray, statement);
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
        Token t = look;
        match(Tag.ID);
        VariableExpression variable = top.get(t);
        if (variable == null) {
            error(t.toString() + " undefined");
        }

        Statement statement;
        if (look.getTag() == Tag.ASSIGN) {
            move();
            statement = new Set(line, variable, bool());
        } else {
            Access x = offset(variable);
            match(Tag.ASSIGN);
            statement = new SetElem(line, x, bool());
        }
        match(Tag.SEMICOLON);
        return statement;
    }

    private Expression bool() throws SyntaxException {
        Expression x = join();
        while (look.getTag() == Tag.OR) {
            Token token = look;
            move();
            x = new Or(line, token, x, join());
        }
        return x;
    }

    private Expression join() throws SyntaxException {
        Expression x = equality();
        while (look.getTag() == Tag.AND) {
            Token token = look;
            move();
            x = new And(line, token, x, equality());
        }
        return x;
    }

    private Expression equality() throws SyntaxException {
        Expression x = rel();
        while (look.getTag() == Tag.EQUAL || look.getTag() == Tag.NOT_EQUAL) {
            Token token = look;
            move();
            x = new Rel(line, token, x, rel());
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
                return new Rel(line, token, x, expr());
            default:
                return x;
        }
    }

    private Expression expr() throws SyntaxException {
        Expression x = term();
        while (look.getTag() == Tag.PLUS || look.getTag() == Tag.MINUS) {
            Token token = look;
            move();
            x = new BinaryOperator(line, token, x, term());
        }
        return x;
    }

    private Expression term() throws SyntaxException {
        Expression x = unary();
        while (look.getTag() == Tag.MUL || look.getTag() == Tag.DIVISION || look.getTag() == Tag.MOD) {
            Token token = look;
            move();
            x = new BinaryOperator(line, token, x, unary());
        }
        return x;
    }

    private Expression unary() throws SyntaxException {
        if (look.getTag() == Tag.MINUS) {
            move();
            return new UnaryOperator(line, WordToken.UNARY_MINUS, unary());
        } else if (look.getTag() == Tag.NOT) {
            Token token = look;
            move();
            return new Not(line, token, unary());
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
            case ID:
                WordToken wordToken = (WordToken) look;
                move();
                if (look.getTag() == Tag.OPEN_BRACKET) {
                    // если за именем идёт круглая скобка, считаем, что это имя метода (иначе - переменная)
                    String methodName = wordToken.getLexeme();
                    move();
                    List<Expression> paramExpressions = new ArrayList<>();
                    while (look.getTag() != Tag.CLOSE_BRACKET) {
                        paramExpressions.add(bool());
                        if (look.getTag() != Tag.COMMA) {
                            break;
                        }
                        match(Tag.COMMA);
                    }
                    match(Tag.CLOSE_BRACKET);
                    if (!signatures.contains(methodName)) {
                        error(String.format("method '%s()' is not defined", methodName));
                    }
                    MethodInfo methodInfo = signatures.getMethodByName(methodName);
                    return new MethodCallExpression(line, wordToken, methodInfo, paramExpressions);
                } else {
                    VariableExpression variable = top.get(wordToken);
                    if (variable == null) {
                        error(String.format("variable '%s' is not defined", wordToken.toString()));
                    }
                    if (look.getTag() == Tag.OPEN_SQUARE) {
                        return offset(variable);
                    } else {
                        return variable;
                    }
                }
            default:
                error("unexpected token: " + look);
                return x;
        }
    }

    private Access offset(VariableExpression a) throws SyntaxException {
        // todo избавиться от костылей для строки
        TypeToken type = a.getType();
        if (type != TypeToken.STRING && type.getTag() == Tag.BASIC) {
            throw new SyntaxException(line, "array type expected");
        }
        match(Tag.OPEN_SQUARE);
        Expression indexExpr = bool();
        match(Tag.CLOSE_SQUARE);
        if (type == TypeToken.STRING) {
            return new Access(line, a, indexExpr, TypeToken.CHAR);
        } else {
            type = ((ArrayToken) type).getArrayType();
            return new Access(line, a, indexExpr, type);
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
