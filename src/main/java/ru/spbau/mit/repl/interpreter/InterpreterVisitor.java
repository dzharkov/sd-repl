package ru.spbau.mit.repl.interpreter;

import ru.spbau.mit.repl.ast.*;

import java.util.ArrayList;
import java.util.List;

public class InterpreterVisitor extends ASTNodeVisitor<Integer> {
    private final List<SemanticError> errors = new ArrayList<>();
    private final List<Command> commands = new ArrayList<>();

    private InterpreterState state;

    public InterpreterVisitor(InterpreterState state) {
        this.state = state;
    }

    @Override
    public Integer visit(AssignmentNode node) {
        Integer result = node.getExpression().accept(this);

        if (result != null) {
            commands.add(new AssignmentCommand(state, node.getVarName(), result));
        }

        return result;
    }

    @Override
    public Integer visit(BinOpNode node) {
        Integer left = node.getLeft().accept(this);
        Integer right = node.getRight().accept(this);

        if (left == null || right == null) {
            return null;
        }

        switch (node.getType()) {
            case PLUS:
                return left + right;
            case MINUS:
                return left - right;
            case MULTIPLY:
                return left * right;
            case DIVIDE:
                if (right.equals(0)) {
                    errors.add(new SemanticError(node.getRight(), "div0"));
                    return null;
                }

                return left / right;
            case UNKNOWN:
            default:
                return null;
        }
    }

    @Override
    public Integer visit(IdentifierNode node) {
        Integer result = state.getVariableValue(node.getIdent());

        if (result == null) {
            errors.add(new SemanticError(node, "Undefined var"));
        }

        return result;
    }

    @Override
    public Integer visit(LiteralNode node) {
        return node.getValue();
    }

    @Override
    public Integer visit(ErrorNode node) {
        return null;
    }

    public List<SemanticError> getErrors() {
        return errors;
    }

    public List<Command> getCommands() {
        return commands;
    }
}
