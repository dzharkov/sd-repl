package ru.spbau.mit.repl.parser;

import ru.spbau.mit.repl.ast.ASTNode;

import java.util.List;

public class ParserResult {
    private ASTNode root = null;
    private List<ParserError> errors = null;

    public ParserResult(ASTNode root) {
        this.root = root;
    }

    public ParserResult(List<ParserError> errors) {
        this.errors = errors;
    }

    public ParserResult(ASTNode root, List<ParserError> errors) {
        this.root = root;
        this.errors = errors;
    }

    public ASTNode getRoot() {
        return root;
    }

    public List<ParserError> getErrors() {
        return errors;
    }

    public boolean isSuccessful() {
        return (errors == null || errors.isEmpty());
    }
}
