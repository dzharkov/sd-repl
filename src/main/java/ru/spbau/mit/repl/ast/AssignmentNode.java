package ru.spbau.mit.repl.ast;

import ru.spbau.mit.repl.error.Location;

public class AssignmentNode extends ASTNode {
    private final String varName;
    private final ASTNode expression;

    public AssignmentNode(Location begin, Location end, String varName, ASTNode expression) {
        super(begin, end);
        this.varName = varName;
        this.expression = expression;
    }

    public String getVarName() {
        return varName;
    }

    public ASTNode getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
