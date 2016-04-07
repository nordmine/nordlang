package nordlang.inter;

import nordlang.exceptions.ParserException;
import nordlang.inter.expressions.Constant;
import nordlang.inter.expressions.Expr;
import nordlang.inter.expressions.Id;
import nordlang.inter.expressions.logical.And;
import nordlang.inter.expressions.logical.Not;
import nordlang.inter.expressions.logical.Or;
import nordlang.inter.expressions.logical.Rel;
import nordlang.inter.expressions.operators.Access;
import nordlang.inter.expressions.operators.BinaryOperator;
import nordlang.inter.expressions.operators.UnaryOperator;
import nordlang.inter.statements.*;
import nordlang.lexer.*;
import nordlang.lexer.types.Char;
import nordlang.lexer.types.CharArray;
import nordlang.machine.Program;

public class Parser {

    private Lexer lexer;
    private Token look; // предпросмотр
    private Env top = null;
    int used = 0; // память для объявлений
    int line = 0;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    private void move() throws ParserException {
        look = lexer.nextToken();
        line = lexer.getLine();
    }

    private void error(String s) throws ParserException {
        throw new ParserException(lexer.getLine(), s);
    }

    private void match(Tag t) throws ParserException {
        if (look.getTag() == t) {
            move();
        } else {
            error(String.format("Tag %s is expected, but was %s", t, look.getTag()));
        }
    }

    public Program createProgram() throws ParserException {
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

    private Statement method() throws ParserException {
        // todo обработка метода и его параметров
        Type type = type();
        match(Tag.ID);
        match(Tag.OPEN_BRACKET);
        match(Tag.CLOSE_BRACKET);
        return block();
    }

    private Statement block() throws ParserException {
        match(Tag.BEGIN_BLOCK);
        Env savedEnv = top;
        top = new Env(top);
        Statement s = new Seq(line, new PushScope(line), statements());
        match(Tag.END_BLOCK);
        top = savedEnv;
        return s;
    }

    private Statement statements() throws ParserException {
        if (look.getTag() == Tag.END_BLOCK) {
            return new PopScope(line);
        } else {
            return new Seq(line, statement(), statements());
        }
    }

    private Statement statement() throws ParserException {
        Expr x;
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

    private Statement definition() throws ParserException {
        Type t = type();
        Token token = look;
        match(Tag.ID);
        Id id = new Id(line, (Word)token, t, used);
        top.put(token, id);
        used = used + t.getWidth();
        Define define = new Define(line, id);

        Statement statement = Statement.Empty;
        match(Tag.ASSIGN);
        if (look.getTag() == Tag.OPEN_SQUARE) {
            // объявление массива через квадратные скобки
            statement = arrayDefinition((Array) t, id, statement, 0);
        } else if (look.getTag() == Tag.STRING) {
            // объявление строкового массива через кавычки
            statement = stringDefinition((Array) t, id, statement);
        } else {
            statement = new Set(line, id, bool());
        }
        match(Tag.SEMICOLON);

        return new Seq(line, define, statement);
    }

    private Statement arrayDefinition(Array t, Id id, Statement statement, int outer) throws ParserException {
        move();
        int inner = 0;
        while (look.getTag() != Tag.CLOSE_SQUARE) {
            if (look.getTag() == Tag.OPEN_SQUARE) {
                statement = arrayDefinition((Array) t.getArrayType(), id, statement, inner);
            } else {
                Expr indexExpr = new Constant(line, outer * t.getWidth() + inner);
                Type type = t.getArrayType();
                Expr widthExpr = new Constant(line, type.getWidth());
                Expr loc = new BinaryOperator(line, new Token(Tag.MUL), indexExpr, widthExpr);
                Access x = new Access(line, id, loc, type);
                statement = new Seq(line, statement, new SetElem(line, x, bool()));
            }
            inner++;
            if (look.getTag() != Tag.COMMA) {
                break;
            }
            match(Tag.COMMA);
        }
        match(Tag.CLOSE_SQUARE);
        t.setSize(inner);
        return statement;
    }

    private Statement stringDefinition(Array t, Id id, Statement statement) throws ParserException {
        StringBuilder sb = ((CharArray)look).getValue();
        move();
        for (int i = 0; i < sb.length(); i++) {
            Expr indexExpr = new Constant(line, i);
            Type type = t.getArrayType();
            Expr widthExpr = new Constant(line, type.getWidth());
            Expr loc = new BinaryOperator(line, new Token(Tag.MUL), indexExpr, widthExpr);
            Access x = new Access(line, id, loc, type);
            statement = new Seq(line, statement, new SetElem(line, x, new Constant(line, new Char(sb.charAt(i)), Type.CHAR)));
        }
        t.setSize(sb.length());
        return statement;
    }

    private Type type() throws ParserException {
        Type t = (Type)look;
        match(Tag.BASIC);
        if (look.getTag() != Tag.OPEN_SQUARE) {
            return t;
        } else {
            return dimension(t);
        }
    }

    private Type dimension(Type t) throws ParserException {
        match(Tag.OPEN_SQUARE);
        match(Tag.CLOSE_SQUARE);
        if (look.getTag() == Tag.OPEN_SQUARE) {
            t = dimension(t);
        }
        return new Array(-1, t);
    }

    private Statement assign() throws ParserException {
        Token t = look;
        match(Tag.ID);
        Id id = top.get(t);
        if (id == null) {
            error(t.toString() + " undefined");
        }

        Statement statement;
        if (look.getTag() == Tag.ASSIGN) {
            move();
            statement = new Set(line, id, bool());
        } else {
            Access x = offset(id);
            match(Tag.ASSIGN);
            statement = new SetElem(line, x, bool());
        }
        match(Tag.SEMICOLON);
        return statement;
    }

    private Expr bool() throws ParserException {
        Expr x = join();
        while (look.getTag() == Tag.OR) {
            Token token = look;
            move();
            x = new Or(line, token, x, join());
        }
        return x;
    }

    private Expr join() throws ParserException {
        Expr x = equality();
        while (look.getTag() == Tag.AND) {
            Token token = look;
            move();
            x = new And(line, token, x, equality());
        }
        return x;
    }

    private Expr equality() throws ParserException {
        Expr x = rel();
        while (look.getTag() == Tag.EQUAL || look.getTag() == Tag.NOT_EQUAL) {
            Token token = look;
            move();
            x = new Rel(line, token, x, rel());
        }
        return x;
    }

    private Expr rel() throws ParserException {
        Expr x = expr();
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

    private Expr expr() throws ParserException {
        Expr x = term();
        while (look.getTag() == Tag.PLUS || look.getTag() == Tag.MINUS) {
            Token token = look;
            move();
            x = new BinaryOperator(line, token, x, term());
        }
        return x;
    }

    private Expr term() throws ParserException {
        Expr x = unary();
        while (look.getTag() == Tag.MUL || look.getTag() == Tag.DIVISION || look.getTag() == Tag.MOD) {
            Token token = look;
            move();
            x = new BinaryOperator(line, token, x, unary());
        }
        return x;
    }

    private Expr unary() throws ParserException {
        if (look.getTag() == Tag.MINUS) {
            move();
            return new UnaryOperator(line, Word.UNARY_MINUS, unary());
        } else if (look.getTag() == Tag.NOT) {
            Token token = look;
            move();
            return new Not(line, token, unary());
        } else return factor();
    }

    private Expr factor() throws ParserException {
        Expr x = null;
        switch (look.getTag()) {
            case OPEN_BRACKET:
                move();
                x = bool();
                match(Tag.CLOSE_BRACKET);
                return x;
            case INT:
                x = new Constant(line, look, Type.INT);
                move();
                return x;
            case CHAR:
                x = new Constant(line, look, Type.CHAR);
                move();
                return x;
            case TRUE:
                x = Constant.True;
                move();
                return x;
            case FALSE:
                x = Constant.False;
                move();
                return x;
            case STRING:
                CharArray charArray = (CharArray) look;
                x = new Constant(line, look, new Array(charArray.getValue().length(), Type.CHAR));
                move();
                return x;
            default:
                error("unexpected token: " + look);
                return x;
            case ID:
                Id id = top.get(look);
                if (id == null) {
                    error(String.format("variable '%s' is not defined", look.toString()));
                }
                move();
                if (look.getTag() != Tag.OPEN_SQUARE) {
                    return id;
                } else {
                    return offset(id);
                }
        }
    }

    private Access offset(Id a) throws ParserException {
        Type type = a.getType();
        match(Tag.OPEN_SQUARE);
        Expr indexExpr = bool();
        match(Tag.CLOSE_SQUARE);
        type = ((Array)type).getArrayType();
        Expr widthExpr = new Constant(line, type.getWidth());
        Expr t1 = new BinaryOperator(line, new Token(Tag.MUL), indexExpr, widthExpr);
        Expr loc = t1;
        while (look.getTag() == Tag.OPEN_SQUARE) {
            match(Tag.OPEN_SQUARE);
            indexExpr = bool();
            match(Tag.CLOSE_SQUARE);
            type = ((Array)type).getArrayType();
            widthExpr = new Constant(line, type.getWidth());
            t1 = new BinaryOperator(line, new Token(Tag.MUL), indexExpr, widthExpr);
            Expr t2 = new BinaryOperator(line, new Token(Tag.PLUS), loc, t1);
            loc = t2;
        }
        return new Access(line, a, loc, type);
    }
}
