package ru.spbau.mit.repl.ast;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

abstract public class ASTNodeVisitor<T> {

    public T visit(AssignmentNode node) {
        throw new NotImplementedException();
    }

    public T visit(BinOpNode node) {
        throw new NotImplementedException();
    }

    public T visit(IdentifierNode node) {
        throw new NotImplementedException();
    }

    public T visit(LiteralNode node) {
        throw new NotImplementedException();
    }

    public T visit(ErrorNode node) {
        throw new NotImplementedException();
    }
}
