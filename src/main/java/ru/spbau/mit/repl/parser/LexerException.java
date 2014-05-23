package ru.spbau.mit.repl.parser;

import ru.spbau.mit.repl.error.Location;

public class LexerException extends Exception {
    private final Location location;
    private final char unexpectedChar;

    public LexerException(Location location, char unexpectedChar) {
        this.location = location;
        this.unexpectedChar = unexpectedChar;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String getMessage() {
        return String.format("Unexpected char '%c' at %s", unexpectedChar, location);
    }
}
