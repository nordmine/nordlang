package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.ArrayToken;
import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.lexer.Token;
import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.lexer.WordToken;
import ru.nordmine.nordlang.machine.Label;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.value.Value;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.ConstantExpression;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.syntax.expressions.MethodCallExpression;
import ru.nordmine.nordlang.syntax.expressions.SizeExpression;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.syntax.expressions.logical.AndExpression;
import ru.nordmine.nordlang.syntax.expressions.logical.NotExpression;
import ru.nordmine.nordlang.syntax.expressions.logical.OrExpression;
import ru.nordmine.nordlang.syntax.expressions.logical.RelExpression;
import ru.nordmine.nordlang.syntax.expressions.operators.AccessExpression;
import ru.nordmine.nordlang.syntax.expressions.operators.BinaryExpression;
import ru.nordmine.nordlang.syntax.expressions.operators.UnaryExpression;
import ru.nordmine.nordlang.syntax.statements.AddElementStatement;
import ru.nordmine.nordlang.syntax.statements.BreakStatement;
import ru.nordmine.nordlang.syntax.statements.DefineArrayStatement;
import ru.nordmine.nordlang.syntax.statements.DefineStatement;
import ru.nordmine.nordlang.syntax.statements.EchoStatement;
import ru.nordmine.nordlang.syntax.statements.ElseStatement;
import ru.nordmine.nordlang.syntax.statements.IfStatement;
import ru.nordmine.nordlang.syntax.statements.PopScopeStatement;
import ru.nordmine.nordlang.syntax.statements.PushScopeStatement;
import ru.nordmine.nordlang.syntax.statements.ReturnStatement;
import ru.nordmine.nordlang.syntax.statements.SequenceStatement;
import ru.nordmine.nordlang.syntax.statements.SetElementStatement;
import ru.nordmine.nordlang.syntax.statements.SetStatement;
import ru.nordmine.nordlang.syntax.statements.Statement;
import ru.nordmine.nordlang.syntax.statements.WhileStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatementParser {

    private ParserScope scope;
    private final Signatures signatures;
    private MethodInfo currentMethod;
    private final ParserContext context;

    public StatementParser(ParserContext context, Signatures signatures, MethodInfo currentMethod, ParserScope scope) {
        this.context = context;
        this.signatures = signatures;
        this.currentMethod = currentMethod;
        this.scope = scope;
    }

    private Statement block() throws SyntaxException {
        if (context.getLook() == null) {
            context.move();
        }
        context.match(Tag.BEGIN_BLOCK);
        ParserScope savedParserScope = scope;
        scope = new ParserScope(scope);
        Statement s = new SequenceStatement(new PushScopeStatement(), statements());
        context.match(Tag.END_BLOCK);
        scope = savedParserScope;
        return s;
    }

    public Statement statements() throws SyntaxException {
        if (context.getTag() == Tag.END_BLOCK) {
            return new PopScopeStatement();
        } else {
            return new SequenceStatement(statement(), statements());
        }
    }

    private Statement statement() throws SyntaxException {
        Expression x;
        Statement s1, s2;
        Statement savedStatement; // сохранение охватывающей конструкции для break

        switch (context.getTag()) {
            case SEMICOLON:
                context.move();
                return Statement.EMPTY;
            case BASIC:
                return definition();
            case IF:
                context.match(Tag.IF);
                context.match(Tag.OPEN_BRACKET);
                x = bool();
                context. match(Tag.CLOSE_BRACKET);
                s1 = statement();
                if (context.getTag() != Tag.ELSE) {
                    return new IfStatement(x, s1);
                }
                context.match(Tag.ELSE);
                s2 = statement();
                return new ElseStatement(x, s1, s2);
            case WHILE:
                WhileStatement whileStatement = new WhileStatement();
                savedStatement = Statement.Enclosing;
                Statement.Enclosing = whileStatement;
                context.match(Tag.WHILE);
                context.match(Tag.OPEN_BRACKET);
                x = bool();
                context.match(Tag.CLOSE_BRACKET);
                s1 = statement();
                whileStatement.init(x, s1);
                Statement.Enclosing = savedStatement;
                // reset statement.enclosing
                return whileStatement;
            case BREAK:
                Token token = context.getLook();
                context.match(Tag.BREAK);
                context.match(Tag.SEMICOLON);
                return new BreakStatement(token);
            case ECHO:
                context.match(Tag.ECHO);
                x = bool();
                return new EchoStatement(x);
            case BEGIN_BLOCK:
                return block();
            case RETURN:
                context.match(Tag.RETURN);
                x = bool();
                if (!currentMethod.getReturnType().equals(x.getType())) {
                    throw new SyntaxException(x.getLine(), "method should return " + currentMethod.getReturnType() + ", but was " + x.getType());
                }
                currentMethod.setHasReturnStatement(true);
                return new ReturnStatement(x, currentMethod);
            default:
                // инструкция присваивания начинается с имени переменной, а не с ключевого слова
                return assign();
        }
    }

    public Statement definition() throws SyntaxException {
        TypeToken typeToken = type();
        WordToken variableToken = (WordToken) context.getLook();
        context.match(Tag.ID);
        VariableExpression variable = new VariableExpression(variableToken, typeToken, scope.getUniqueIndexSequence());
        scope.put(variableToken, variable);

        Statement statement = Statement.EMPTY;
        context.match(Tag.ASSIGN);
        if (context.getTag() == Tag.OPEN_SQUARE) {
            // объявление массива через квадратные скобки
            statement = arrayDefinition((ArrayToken) typeToken, variable, statement);
        } else {
            // здесь тоже может объявляться массив (например, через вызов метода или через другую переменную)
            Value initialValue = ParserUtils.getInitialValueByToken(typeToken);
            DefineStatement defineStatement = new DefineStatement(variable, initialValue);
            statement = new SequenceStatement(defineStatement, new SetStatement(variable, bool()));
        }
        context.match(Tag.SEMICOLON);

        return statement;
    }

    private Statement arrayDefinition(ArrayToken t, VariableExpression variable, Statement statement) throws SyntaxException {
        context.move();
        TypeToken type = t.getArrayType();
        while (context.getTag() != Tag.CLOSE_SQUARE) { // одна итерация - один элемент массива
            Expression elementExpression = bool();
            if (type != elementExpression.getType()) {
                ParserUtils.typeError(type.getLine(), type, elementExpression.getType());
            }
            statement = new SequenceStatement(statement, new AddElementStatement(variable, elementExpression));
            if (context.getTag() != Tag.COMMA) {
                break;
            }
            context.match(Tag.COMMA);
        }
        context.match(Tag.CLOSE_SQUARE);
        Value initialValue = ParserUtils.getInitialValueByToken(t.getArrayType());
        DefineArrayStatement defineArrayStatement = new DefineArrayStatement(variable, initialValue);
        statement = new SequenceStatement(defineArrayStatement, statement);
        return statement;
    }

    public TypeToken type() throws SyntaxException {
        TypeToken t = (TypeToken) context.getLook();
        context.match(Tag.BASIC);
        if (context.getTag() != Tag.OPEN_SQUARE) {
            return t;
        } else {
            return dimension(t);
        }
    }

    private TypeToken dimension(TypeToken t) throws SyntaxException {
        context.match(Tag.OPEN_SQUARE);
        context.match(Tag.CLOSE_SQUARE);
        if (context.getTag() == Tag.OPEN_SQUARE) {
            t = dimension(t);
        }
        return new ArrayToken(t);
    }

    private Statement assign() throws SyntaxException {
        Token variableToken = context.getLook();
        context.match(Tag.ID);
        VariableExpression variable = scope.get(variableToken);
        if (variable == null) {
            error(variableToken, variableToken.toString() + " undefined");
        }

        Statement statement;
        if (context.getTag() == Tag.ASSIGN) {
            context.move();
            statement = new SetStatement(variable, bool());
        } else if (context.getTag() == Tag.INCREMENT) {
            Token incrementToken = context.getLook();
            context.move();
            Expression binExpr = new BinaryExpression(new Token(Tag.PLUS, incrementToken.getLine()), variable, new ConstantExpression(incrementToken.getLine(), 1));
            statement = new SetStatement(variable, binExpr);
        } else if (context.getTag() == Tag.DECREMENT) {
            Token decrementToken = context.getLook();
            context.move();
            Expression binExpr = new BinaryExpression(new Token(Tag.MINUS, decrementToken.getLine()), variable, new ConstantExpression(decrementToken.getLine(), 1));
            statement = new SetStatement(variable, binExpr);
        } else {
            AccessExpression indexedVariable = offset(variable);
            context.match(Tag.ASSIGN);
            if (indexedVariable == null) {
                // [] - добавление нового элемента в массив
                Expression elemExpr = bool();
                TypeToken arrayType = variable.getType().getArrayType();
                if (arrayType != elemExpr.getType()) {
                    ParserUtils.typeError(elemExpr.getLine(), arrayType, elemExpr.getType());
                }
                statement = new AddElementStatement(variable, elemExpr);
            } else {
                // [x] - установка в элемент x нового значения
                statement = new SetElementStatement(indexedVariable, bool());
            }
        }
        context.match(Tag.SEMICOLON);
        return statement;
    }

    // Boolean operators

    private Expression bool() throws SyntaxException {
        Expression x = join();
        while (context.getTag() == Tag.OR) {
            Token token = context.getLook();
            context.move();
            x = new OrExpression(token, x, join());
        }
        return x;
    }

    private Expression join() throws SyntaxException {
        Expression x = equality();
        while (context.getTag() == Tag.AND) {
            Token token = context.getLook();
            context.move();
            x = new AndExpression(token, x, equality());
        }
        return x;
    }

    private Expression equality() throws SyntaxException {
        Expression x = rel();
        while (context.getTag() == Tag.EQUAL || context.getTag() == Tag.NOT_EQUAL) {
            Token token = context.getLook();
            context.move();
            x = new RelExpression(token, x, rel());
        }
        return x;
    }

    private Expression rel() throws SyntaxException {
        Expression x = expr();
        switch (context.getTag()) {
            case LESS:
            case LESS_OR_EQUAL:
            case GREATER_OR_EQUAL:
            case GREATER:
                Token token = context.getLook();
                context.move();
                return new RelExpression(token, x, expr());
            default:
                return x;
        }
    }

    // Math operators

    private Expression expr() throws SyntaxException {
        Expression x = term();
        while (context.getTag() == Tag.PLUS || context.getTag() == Tag.MINUS) {
            Token token = context.getLook();
            context.move();
            x = new BinaryExpression(token, x, term());
        }
        return x;
    }

    private Expression term() throws SyntaxException {
        Expression x = unary();
        while (context.getTag() == Tag.MUL || context.getTag() == Tag.DIVISION || context.getTag() == Tag.MOD) {
            Token token = context.getLook();
            context.move();
            x = new BinaryExpression(token, x, unary());
        }
        return x;
    }

    private Expression unary() throws SyntaxException {
        if (context.getTag() == Tag.MINUS) {
            context.move();
            return new UnaryExpression(WordToken.UNARY_MINUS, unary());
        } else if (context.getTag() == Tag.NOT) {
            Token token = context.getLook();
            context.move();
            return new NotExpression(token, unary());
        } else {
            Expression expr = factor();
            if (expr.getType() == TypeToken.BOOL) {
                // фиктивное сравнение c true, чтобы была возможна короткая запись "bool b = true; if (b)..."
                return new RelExpression(WordToken.EQUAL, expr, ConstantExpression.TRUE);
            } else {
                return expr;
            }
        }
    }

    private Expression factor() throws SyntaxException {
        Expression x = null;
        switch (context.getTag()) {
            case OPEN_BRACKET:
                context.move();
                x = bool();
                context.match(Tag.CLOSE_BRACKET);
                return x;
            case TRUE:
                x = ConstantExpression.TRUE;
                context.move();
                return x;
            case FALSE:
                x = ConstantExpression.FALSE;
                context.move();
                return x;
            case INT:
                x = new ConstantExpression(context.getLook(), TypeToken.INT);
                context.move();
                return x;
            case CHAR:
                x = new ConstantExpression(context.getLook(), TypeToken.CHAR);
                context.move();
                return x;
            case STRING:
                x = new ConstantExpression(context.getLook(), TypeToken.STRING);
                context.move();
                return x;
            case SIZE:
                context.match(Tag.SIZE);
                context.match(Tag.OPEN_BRACKET);
                Expression arg = bool();
                if (arg.getType() != TypeToken.STRING && !arg.getType().getTag().equals(Tag.INDEX)) {
                    throw new SyntaxException(arg.getLine(), "string or array required for size operator, but was " + arg.getType());
                }
                context.match(Tag.CLOSE_BRACKET);
                return new SizeExpression(arg);
            case ID:
                WordToken wordToken = (WordToken) context.getLook();
                context.move();
                if (context.getTag() == Tag.OPEN_BRACKET) {
                    // если за именем идёт круглая скобка, считаем, что это имя метода (иначе - переменная)
                    String methodName = wordToken.getLexeme();
                    context.move();
                    List<Expression> paramExpressions = new ArrayList<>();
                    List<TypeToken> paramTypes = new ArrayList<>();
                    while (context.getTag() != Tag.CLOSE_BRACKET) {
                        Expression paramExpr = bool();
                        paramTypes.add(paramExpr.getType());
                        paramExpressions.add(paramExpr);
                        if (context.getTag() != Tag.COMMA) {
                            break;
                        }
                        context.match(Tag.COMMA);
                    }
                    context.match(Tag.CLOSE_BRACKET);
                    MethodInfo methodInfo = signatures.getMethodByParamTypes(methodName, paramTypes)
                            .orElseThrow(() -> {
                                String typeList = paramTypes.stream()
                                        .map(WordToken::getLexeme)
                                        .collect(Collectors.joining(", "));
                                return new SyntaxException(
                                        wordToken.getLine(), String.format("method %s(%s) is not defined", methodName, typeList)
                                );
                            });
                    return new MethodCallExpression(wordToken, methodInfo, paramExpressions);
                } else {
                    VariableExpression variable = scope.get(wordToken);
                    if (variable == null) {
                        error(wordToken, String.format("variable '%s' is not defined", wordToken.toString()));
                    }
                    if (context.getTag() == Tag.OPEN_SQUARE) {
                        Token openSquareToken = context.getLook();
                        AccessExpression accExpr = offset(variable);
                        if (accExpr == null) {
                            throw new SyntaxException(openSquareToken.getLine(), "index expected");
                        }
                        return  accExpr;
                    } else {
                        return variable;
                    }
                }
            default:
                error(context.getLook(), "unexpected token: " + context.getLook());
                return x;
        }
    }

    private AccessExpression offset(VariableExpression a) throws SyntaxException {
        TypeToken type = a.getType();
        if (type.getArrayType() == null) {
            throw new SyntaxException(type.getLine(), "array type expected");
        }
        context.match(Tag.OPEN_SQUARE);
        if (context.getTag() == Tag.CLOSE_SQUARE) {
            context.match(Tag.CLOSE_SQUARE);
            return null;
        } else {
            Expression indexExpr = bool();
            context.match(Tag.CLOSE_SQUARE);
            return new AccessExpression(a, indexExpr, type.getArrayType());
        }
    }

    // утилитарные методы

    private void error(Token token, String s) throws SyntaxException {
        throw new SyntaxException(token.getLine(), s);
    }
}
