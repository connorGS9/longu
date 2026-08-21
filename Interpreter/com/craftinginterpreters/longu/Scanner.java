package com.craftinginterpreters.longu;

import com.craftinginterpreters.longu.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    Scanner(String source) {
        this.source = source;
    }

    List<Tokens> scanTokens() {
        while (!isAtEnd) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null)); //Append EOF token
        return tokens;
    }
}