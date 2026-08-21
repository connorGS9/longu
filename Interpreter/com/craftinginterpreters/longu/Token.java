package com.craftinginterpreters.longu;

Class Token {
    final TokenType type; // Enum (single-char tokens, double/single char tokens, literals, keywords)
    final String lexeme; // A single fully parsed piece of text "var, int, Connor, ==, -, etc.."
    final Object literal; // Actual interpreted value of the lexeme "3"(string of length 1) -> 3.0 (literal value of the token)
    final int line; // Line which token appears

    Token(TokenType type, String lexeme, Object literal, int line) { // Constructor
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() { // To string for a token
        return type + " " + lexeme + " " + literal;
    }
}