package ru.spbau.mit.repl.interpreter;

import ru.spbau.mit.repl.ast.ASTNode;
import ru.spbau.mit.repl.error.ReplLocationError;

public class SemanticError extends ReplLocationError {
    public SemanticError(ASTNode errorNode, String msg) {
        super(errorNode.getBegin(), errorNode.getEnd(), msg);
    }
}
