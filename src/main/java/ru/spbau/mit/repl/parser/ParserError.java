package ru.spbau.mit.repl.parser;

import ru.spbau.mit.repl.error.Location;
import ru.spbau.mit.repl.error.ReplLocationError;

public class ParserError extends ReplLocationError {
    public ParserError(Location from, Location to, String msg) {
        super(from, to, msg);
    }

    public ParserError(Location location, String msg) {
        super(location, location, msg);
    }
}
