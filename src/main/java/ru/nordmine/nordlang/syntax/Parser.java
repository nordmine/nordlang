package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.*;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.machine.value.Value;
import ru.nordmine.nordlang.syntax.exceptions.SyntaxException;
import ru.nordmine.nordlang.syntax.expressions.ConstantExpression;
import ru.nordmine.nordlang.syntax.expressions.Expression;
import ru.nordmine.nordlang.syntax.expressions.VariableExpression;
import ru.nordmine.nordlang.syntax.expressions.logical.And;
import ru.nordmine.nordlang.syntax.expressions.logical.Not;
import ru.nordmine.nordlang.syntax.expressions.logical.Or;
import ru.nordmine.nordlang.syntax.expressions.logical.Rel;
import ru.nordmine.nordlang.syntax.expressions.operators.Access;
import ru.nordmine.nordlang.syntax.expressions.operators.BinaryOperator;
import ru.nordmine.nordlang.syntax.expressions.operators.UnaryOperator;
import ru.nordmine.nordlang.syntax.statements.*;

public class Parser {

    private Lexer lexer;
    private Token look; // предпросмотр
    private ParserScope top = null;
    private int line = 0;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

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

    public Program createProgram() throws SyntaxException {
        Program program = new Program();
        move();
        Statement s = method();
        int begin = program.newLabel();
        int after = program.newLabel();
        program.fixLabel(begin);
        s.gen(program, begin, after);
        program.fixLabel(after);
        program.addComment("return");
        return program;
    }

    private Statement method() throws SyntaxException {
        // todo обработка метода и его параметров
        TypeToken type = type();
        match(Tag.ID);
        match(Tag.OPEN_BRACKET);
        match(Tag.CLOSE_BRACKET);
        return block();
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
                return Statement.Empty;
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
            default:
                return assign();
        }
    }

    private Statement definition() throws SyntaxException {
        TypeToken t = type();
        WordToken variableToken = (WordToken) look;
        match(Tag.ID);
        VariableExpression variable = new VariableExpression(line, variableToken, t, top.getUniqueIndexSequence());
        top.put(variableToken, variable);

        Statement statement = Statement.Empty;
        match(Tag.ASSIGN);
        if (look.getTag() == Tag.OPEN_SQUARE) {
            // объявление массива через квадратные скобки
            statement = arrayDefinition((ArrayToken) t, variable, statement);
        } else {
            Value initialValue = ParserUtils.getInitialValueByToken(t);
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
            //Expression indexExpr = new ConstantExpression(line, outer * t.getWidth() + inner);
            TypeToken type = t.getArrayType();

            //Expression widthExpr = new ConstantExpression(line, type.getWidth());
            //Expression loc = new BinaryOperator(line, new Token(Tag.MUL), indexExpr, widthExpr);
            Access x = new Access(line, variable, new ConstantExpression(line, inner), type);
            statement = new Seq(line, statement, new SetElem(line, x, bool()));
            inner++;
            if (look.getTag() != Tag.COMMA) {
                break;
            }
            match(Tag.COMMA);
        }
        match(Tag.CLOSE_SQUARE);
        Value initialValue = ParserUtils.getInitialValueByToken(t.getArrayType());
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
            default:
                error("unexpected token: " + look);
                return x;
            case ID:
                WordToken variableToken = (WordToken) look;
                VariableExpression variable = top.get(variableToken);
                if (variable == null) {
                    error(String.format("variable '%s' is not defined", look.toString()));
                }
                move();
                if (look.getTag() != Tag.OPEN_SQUARE) {
                    return variable;
                } else {
                    return offset(variable);
                }
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
}
