package ru.spbau.mit.repl.parser;

import ru.spbau.mit.repl.ast.ASTNode;
import ru.spbau.mit.repl.ast.BinOpNode;
import ru.spbau.mit.repl.ast.BinOpType;

import java.util.Stack;

class ExpressionParserStack {
    final Stack<ASTNode> operands = new Stack<>();
    final Stack<BinOpType> operators = new Stack<>();

    void shiftOperation() {
        ASTNode right = operands.pop();
        ASTNode left = operands.pop();
        BinOpType opType = operators.pop();

        operands.push(new BinOpNode(opType, left, right));
    }

    ASTNode result() {
        return operands.peek();
    }

    boolean isAwaitingOperand() {
        return operators.size() * 2 > operands.size();
    }
}
