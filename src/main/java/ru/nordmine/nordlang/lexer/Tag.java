package ru.nordmine.nordlang.lexer;

public enum Tag {
    /* constructions */
    BEGIN_BLOCK,
    END_BLOCK,
    BREAK,
    WHILE,
    DO,
    ELSE,
    SEMICOLON,
    COMMA,
    IF,
    ECHO,
    INDEX,
    RETURN,
    SIZE,

    /* types */
    BASIC,
    INT,
    CHAR,
    ID,
    STRING,

    /* math signs */
    PLUS,
    MINUS,
    MUL,
    DIVISION,
    MOD,
    UNARY_MINUS,
    ASSIGN,

    /* boolean */
    AND,
    OR,
    TRUE,
    FALSE,
    EQUAL,
    GREATER_OR_EQUAL,
    LESS,
    GREATER,
    NOT_EQUAL,
    LESS_OR_EQUAL,
    NOT,


    OPEN_SQUARE,
    CLOSE_SQUARE,

    OPEN_BRACKET,
    CLOSE_BRACKET,

    END_OF_FILE
}
