package ru.spbau.mit.repl.parser;

import ru.spbau.mit.repl.error.Location;

public class Token {
    private final TokenType type;
    private final String value;
    private final Location begin;
    private final Location end;

    public Token(TokenType type, String value, Location begin, Location end) {
        this.type = type;
        this.value = value;
        this.begin = begin;
        this.end = end;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Location getBegin() {
        return begin;
    }

    public Location getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (!begin.equals(token.begin)) return false;
        if (!end.equals(token.end)) return false;
        if (type != token.type) return false;
        if (!value.equals(token.value)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", begin=" + begin +
                ", end=" + end +
                '}';
    }
}
