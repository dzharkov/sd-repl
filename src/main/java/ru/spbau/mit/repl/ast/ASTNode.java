package ru.spbau.mit.repl.ast;

import ru.spbau.mit.repl.error.Location;

abstract public class ASTNode {
    private final Location begin;
    private final Location end;

    protected ASTNode(Location begin, Location end) {
        this.begin = begin;
        this.end = end;
    }

    protected ASTNode(Location begin) {
        this(begin, null);
    }

    public Location getBegin() {
        return begin;
    }

    public Location getEnd() {
        return end;
    }

    abstract public <T> T accept(ASTNodeVisitor<T> visitor);
}
