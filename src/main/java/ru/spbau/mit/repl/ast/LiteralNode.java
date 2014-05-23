package ru.spbau.mit.repl.ast;

import ru.spbau.mit.repl.error.Location;

public class LiteralNode extends ASTNode {
    private final int value;

    public LiteralNode(Location begin, Location end, int value) {
        super(begin, end);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
