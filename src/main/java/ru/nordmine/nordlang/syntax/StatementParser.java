package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.ArrayToken;
import ru.nordmine.nordlang.lexer.Lexer;
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

    //private Lexer lexer;
    //private Token look; // предпросмотр
    private ParserScope top = null;
//    private int line = 0;
    private final Signatures signatures;
    private MethodInfo currentMethod;
    private final ParserContext context;

    public StatementParser(ParserContext context, Signatures signatures, MethodInfo currentMethod) {
        this.context = context;
        this.signatures = signatures;
        this.currentMethod = currentMethod;
    }

    public Program createProgram() throws SyntaxException {
        Program program = new Program();
        Statement s = block();
        Label begin = program.newLabel();
        Label after = program.newLabel();
        s.gen(program, begin, after);
        return program;
    }

    private Statement block() throws SyntaxException {
        context.match(Tag.BEGIN_BLOCK);
        ParserScope savedParserScope = top;
        top = new ParserScope(top);
        Statement s = new SequenceStatement(line, new PushScopeStatement(line), statements());
        context.match(Tag.END_BLOCK);
        top = savedParserScope;
        return s;
    }

    private Statement statements() throws SyntaxException {
        if (context.getTag() == Tag.END_BLOCK) {
            return new PopScopeStatement(line);
        } else {
            return new SequenceStatement(line, statement(), statements());
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
                    return new IfStatement(line, x, s1);
                }
                context.match(Tag.ELSE);
                s2 = statement();
                return new ElseStatement(line, x, s1, s2);
            case WHILE:
                WhileStatement whileStatement = new WhileStatement(line);
                savedStatement = Statement.Enclosing;
                Statement.Enclosing = whileStatement;
                context.match(Tag.WHILE);
                context. match(Tag.OPEN_BRACKET);
                x = bool();
                context.match(Tag.CLOSE_BRACKET);
                s1 = statement();
                whileStatement.init(x, s1);
                Statement.Enclosing = savedStatement;
                // reset statement.enclosing
                return whileStatement;
            case BREAK:
                context. match(Tag.BREAK);
                context.match(Tag.SEMICOLON);
                return new BreakStatement(line);
            case ECHO:
                context.match(Tag.ECHO);
                x = bool();
                return new EchoStatement(line, x);
            case BEGIN_BLOCK:
                return block();
            case RETURN:
                context.match(Tag.RETURN);
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
        context.match(Tag.ID);
        VariableExpression variable = new VariableExpression(line, variableToken, typeToken, top.getUniqueIndexSequence());
        top.put(variableToken, variable);

        Statement statement = Statement.EMPTY;
        context.match(Tag.ASSIGN);
        if (context.getTag() == Tag.OPEN_SQUARE) {
            // объявление массива через квадратные скобки
            statement = arrayDefinition((ArrayToken) typeToken, variable, statement);
        } else {
            // здесь тоже может объявляться массив (например, через вызов метода или через другую переменную)
            Value initialValue = ParserUtils.getInitialValueByToken(line, typeToken);
            DefineStatement defineStatement = new DefineStatement(line, variable, initialValue);
            statement = new SequenceStatement(line, defineStatement, new SetStatement(line, variable, bool()));
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
                ParserUtils.typeError(line, type, elementExpression.getType());
            }
            statement = new SequenceStatement(line, statement, new AddElementStatement(line, variable, elementExpression));
            if (context.getTag() != Tag.COMMA) {
                break;
            }
            context.match(Tag.COMMA);
        }
        context.match(Tag.CLOSE_SQUARE);
        Value initialValue = ParserUtils.getInitialValueByToken(line, t.getArrayType());
        DefineArrayStatement defineArrayStatement = new DefineArrayStatement(line, variable, initialValue);
        statement = new SequenceStatement(line, defineArrayStatement, statement);
        return statement;
    }

    private TypeToken type() throws SyntaxException {
        TypeToken t = (TypeToken) look;
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
        Token variableToken = look;
        context.match(Tag.ID);
        VariableExpression variable = top.get(variableToken);
        if (variable == null) {
            error(variableToken.toString() + " undefined");
        }

        Statement statement;
        if (context.getTag() == Tag.ASSIGN) {
            context.move();
            statement = new SetStatement(line, variable, bool());
        } else if (look.getTag() == Tag.INCREMENT) {
            context.move();
            Expression binExpr = new BinaryExpression(line, new Token(Tag.PLUS), variable, new ConstantExpression(line, 1));
            statement = new SetStatement(line, variable, binExpr);
        } else if (look.getTag() == Tag.DECREMENT) {
            context.move();
            Expression binExpr = new BinaryExpression(line, new Token(Tag.MINUS), variable, new ConstantExpression(line, 1));
            statement = new SetStatement(line, variable, binExpr);
        } else {
            AccessExpression indexedVariable = offset(variable);
            context.match(Tag.ASSIGN);
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
        context.match(Tag.SEMICOLON);
        return statement;
    }

    // Boolean operators

    private Expression bool() throws SyntaxException {
        Expression x = join();
        while (context.getTag() == Tag.OR) {
            Token token = look;
            context.move();
            x = new OrExpression(line, token, x, join());
        }
        return x;
    }

    private Expression join() throws SyntaxException {
        Expression x = equality();
        while (context.getTag() == Tag.AND) {
            Token token = look;
            context.move();
            x = new AndExpression(line, token, x, equality());
        }
        return x;
    }

    private Expression equality() throws SyntaxException {
        Expression x = rel();
        while (context.getTag() == Tag.EQUAL || context.getTag() == Tag.NOT_EQUAL) {
            Token token = look;
            context.move();
            x = new RelExpression(line, token, x, rel());
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
                Token token = look;
                context.move();
                return new RelExpression(line, token, x, expr());
            default:
                return x;
        }
    }

    // Math operators

    private Expression expr() throws SyntaxException {
        Expression x = term();
        while (context.getTag() == Tag.PLUS || context.getTag() == Tag.MINUS) {
            Token token = look;
            context.move();
            x = new BinaryExpression(line, token, x, term());
        }
        return x;
    }

    private Expression term() throws SyntaxException {
        Expression x = unary();
        while (context.getTag() == Tag.MUL || context.getTag() == Tag.DIVISION || context.getTag() == Tag.MOD) {
            Token token = look;
            context.move();
            x = new BinaryExpression(line, token, x, unary());
        }
        return x;
    }

    private Expression unary() throws SyntaxException {
        if (context.getTag() == Tag.MINUS) {
            context.move();
            return new UnaryExpression(line, WordToken.UNARY_MINUS, unary());
        } else if (context.getTag() == Tag.NOT) {
            Token token = look;
            context.move();
            return new NotExpression(line, token, unary());
        } else {
            Expression expr = factor();
            if (expr.getType() == TypeToken.BOOL) {
                // фиктивное сравнение c true, чтобы была возможна короткая запись "bool b = true; if (b)..."
                return new RelExpression(expr.getLine(), WordToken.EQUAL, expr, ConstantExpression.TRUE);
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
                x = new ConstantExpression(line, look, TypeToken.INT);
                context.move();
                return x;
            case CHAR:
                x = new ConstantExpression(line, look, TypeToken.CHAR);
                context.move();
                return x;
            case STRING:
                x = new ConstantExpression(line, look, TypeToken.STRING);
                context.move();
                return x;
            case SIZE:
                context.match(Tag.SIZE);
                context.match(Tag.OPEN_BRACKET);
                Expression arg = bool();
                if (arg.getType() != TypeToken.STRING && !arg.getType().getTag().equals(Tag.INDEX)) {
                    throw new SyntaxException(line, "string or array required for size operator, but was " + arg.getType());
                }
                context.match(Tag.CLOSE_BRACKET);
                return new SizeExpression(line, arg);
            case ID:
                WordToken wordToken = (WordToken) look;
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
                                        line, String.format("method %s(%s) is not defined", methodName, typeList)
                                );
                            });
                    return new MethodCallExpression(line, wordToken, methodInfo, paramExpressions);
                } else {
                    VariableExpression variable = top.get(wordToken);
                    if (variable == null) {
                        error(String.format("variable '%s' is not defined", wordToken.toString()));
                    }
                    if (context.getTag() == Tag.OPEN_SQUARE) {
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
        context.match(Tag.OPEN_SQUARE);
        if (context.getTag() == Tag.CLOSE_SQUARE) {
            context.match(Tag.CLOSE_SQUARE);
            return null;
        } else {
            Expression indexExpr = bool();
            context.match(Tag.CLOSE_SQUARE);
            return new AccessExpression(line, a, indexExpr, type.getArrayType());
        }
    }

    // утилитарные методы

   /* private void move() throws SyntaxException {
        look = lexer.nextToken();
        line = lexer.getLine();
    }*/

    private void error(String s) throws SyntaxException {
        throw new SyntaxException(lexer.getLine(), s);
    }

    /*private void match(Tag t) throws SyntaxException {
        if (look.getTag() == t) {
            move();
        } else {
            error(String.format("Tag %s is expected, but was %s", t, look.getTag()));
        }
    }*/
}
