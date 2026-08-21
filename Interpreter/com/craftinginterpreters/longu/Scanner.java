package com.craftinginterpreters.longu;

import static com.craftinginterpreters.longu.TokenType.*;
import java.util.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0; // Beggining index of current lexeme
    private int current = 0; // Where we are in current lexeme
    private int line = 1; // Source file lone of current

    private static final Map<String, TokenType> keywords;
    static { // Static block for initializing our keywords map
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    Scanner(String source) { // Constructor
        this.source = source;
    }

    List<Token> scanTokens() { // Where the actual scanning and storing occurs and returns arraylist of tokens
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line)); //Append EOF token
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch(c) {
            // One character lexemes
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break; 
            // Possible one or two character lexemes
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            // '/' could be division or start of a comment 
            case '/':
                if (match('/')) {
                // A comment goes until the end of the line.
                while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                addToken(SLASH); // Add SLASH token type
                }
                break;

            // Skip past whitespace and new line chars
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                line++; // Update line count if a new line char is encounterd
                break;

            case '"': string(); break; //We are encountering a String literal

            default:
                if (isDigit(c)) { // Handle numbers 
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Longu.error(line, "Unexpected character.");
                }
                break;
        }
    }

    // Scan identifiers like variable names and determine if they are keywords
    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;

        addToken(type);
    }

    private void number() {
        while (isDigit(peek())) advance(); // Consume regular digits

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance(); // Consume decimal digits
        }

        // Add the number as a Double (all numbers are floating point in longu(lox))
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }
    
    // Method for dealing with string literals 
    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++; // longu supports multi-line strings 
            advance();
        }

        if (isAtEnd()) { // There is no terminating " 
            Longu.error(line, "Unterminated string.");
            return;
        }

        // The closing "
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    // Method to check two character lexemes "==, !=, <=" for their match following them (given '!' and expected '=' return true if '=' comes next)
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    // Return the character at current without advancing or a null terminator if at the end
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

     private char peekNext() { // Max lookahead od Scanner - 2 characters 
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    // Detemrine if char c is an English letter or underscore 
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9'; // Character value lies in range ['0', '9']
    } 

    private boolean isAlphaNumeric(char c) { // Method for determining if char c is either a number, letter, or underscore
        return isAlpha(c) || isDigit(c);
    }

    private char advance() { // Advance the current pointer in the Source string
        return source.charAt(current++);
    }

    private void addToken(TokenType type) { // Only takes TokenType -> overloaded method which takes (TokenType, Object Literal)
        addToken(type, null);
    }

    // Overloaded method which is called by addToken(TokenType) and handles tokens with literals
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current); // The lexeme is the substring from [start,current] *We advance current with current++ PAST the last char so we dont need substring(start, current + 1) *
        tokens.add(new Token(type, text, literal, line)); // Add the new token to the list of tokens
    }
}