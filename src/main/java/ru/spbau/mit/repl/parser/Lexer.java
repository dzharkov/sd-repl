package ru.spbau.mit.repl.parser;

import ru.spbau.mit.repl.error.Location;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final static String[] keyTokens = {
            "=", "(", ")"
    };
    private final static TokenType[] keyTokensTypes = {
            TokenType.ASSIGNMENT, TokenType.LEFT_PARENTHESIS, TokenType.RIGHT_PARENTHESIS
    };

    private String text;
    private int position = 0;

    private List<Token> result = new ArrayList<>();

    public Lexer(String text) {
        this.text = text;
    }

    public List<Token> tokenize() throws LexerException {
        Token next = null;
        while ((next = nextToken()) != null) {
            result.add(next);
        }

        return result;
    }

    public Token nextToken() throws LexerException {
        handleWhitespaces();

        if (!hasTextToParse()) {
            return null;
        }

        Token next;

        if ((next = tryParseKeyToken()) != null) {
            return next;
        }

        if ((next = tryParseIdent()) != null) {
            return next;
        }

        if ((next = tryParseLiteral()) != null) {
            return next;
        }

        throw new LexerException(buildCurrentLocation(), currentChar());
    }

    private boolean hasTextToParse() {
        return position < text.length();
    }

    private char currentChar() {
        return text.charAt(position);
    }

    private String currentSuffix() {
        return text.substring(position);
    }

    private int currentLine() {
        return 0;
    }

    private Location buildCurrentLocation() {
        return new Location(currentLine(), position);
    }

    private Location buildPreviousLocation() {
        return buildCurrentLocationPlus(-1);
    }

    private Location buildCurrentLocationPlus(int delta) {
        return new Location(currentLine(), position + delta);
    }

    private void handleWhitespaces() {
        while (hasTextToParse() && currentChar() == ' ') {
            position++;
        }
    }

    private Token tryParseIdent() {
        if (Character.isJavaIdentifierStart(currentChar())) {
            Location begin = buildCurrentLocation();
            String ident = "" + currentChar();
            position++;

            while (hasTextToParse() && Character.isJavaIdentifierPart(currentChar())) {
                ident += currentChar();
                position++;
            }

            return new Token(
                    TokenType.IDENTIFIER,
                    ident,
                    begin,
                    buildPreviousLocation()
            );
        }

        return null;
    }

    private Token tryParseLiteral() {
        Location begin = buildCurrentLocation();
        String value = "";

        while (hasTextToParse() && Character.isDigit(currentChar())) {
            value += currentChar();
            position++;
        }

        if (value.length() > 0) {
            return new Token(
                    TokenType.LITERAL,
                    value,
                    begin,
                    buildPreviousLocation()
            );
        }

        return null;
    }

    private Token tryParseKeyToken() {
        Integer index;
        if ((index = tryMatchWithArray(BinOpUtils.operators)) != null) {
            String value = BinOpUtils.operators[index];
            position += value.length();
            return new Token(
                    TokenType.BINARY_OPERATOR,
                    value,
                    buildCurrentLocationPlus(-value.length()),
                    buildPreviousLocation()
            );
        }

        if ((index = tryMatchWithArray(keyTokens)) != null) {
            String value = keyTokens[index];
            position += value.length();
            return new Token(
                    keyTokensTypes[index],
                    value,
                    buildCurrentLocationPlus(-value.length()),
                    buildPreviousLocation()
            );
        }

        return null;
    }

    private Integer tryMatchWithArray(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            if (currentSuffix().startsWith(strings[i])) {
                return i;
            }
        }

        return null;
    }
}
