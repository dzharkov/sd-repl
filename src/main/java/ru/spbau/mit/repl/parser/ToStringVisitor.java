package ru.spbau.mit.repl.parser;

import ru.spbau.mit.repl.ast.*;

public class ToStringVisitor extends ASTNodeVisitor<String> {
    @Override
    public String visit(AssignmentNode node) {
        return String.format("%s=%s", node.getVarName(), node.getExpression().accept(this));
    }

    @Override
    public String visit(BinOpNode node) {
        return String.format("(%s%s%s)",
                node.getLeft().accept(this),
                BinOpUtils.toString(node.getType()),
                node.getRight().accept(this)
        );
    }

    @Override
    public String visit(IdentifierNode node) {
        return node.getIdent();
    }

    @Override
    public String visit(LiteralNode node) {
        return Integer.valueOf(node.getValue()).toString();
    }

    @Override
    public String visit(ErrorNode node) {
        return "e";
    }
}
