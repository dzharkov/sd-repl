package ru.spbau.mit.repl.ast;

import ru.spbau.mit.repl.error.Location;

public class IdentifierNode extends ASTNode {
    private final String ident;

    public IdentifierNode(Location begin, String ident) {
        super(begin);
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
