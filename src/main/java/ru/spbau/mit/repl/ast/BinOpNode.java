package ru.spbau.mit.repl.ast;


public class BinOpNode extends ASTNode {
    private final BinOpType type;
    private final ASTNode left;
    private final ASTNode right;

    public BinOpNode(BinOpType type, ASTNode left, ASTNode right) {
        super(left.getBegin(), right.getEnd());
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public BinOpType getType() {
        return type;
    }

    public ASTNode getLeft() {
        return left;
    }

    public ASTNode getRight() {
        return right;
    }


    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
