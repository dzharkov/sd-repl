package ru.spbau.mit.repl.ast;

import ru.spbau.mit.repl.error.Location;

public class ErrorNode extends ASTNode {
    public ErrorNode(Location begin, Location end) {
        super(begin, end);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
