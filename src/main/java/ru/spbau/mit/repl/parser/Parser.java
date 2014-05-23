package ru.spbau.mit.repl.parser;

import ru.spbau.mit.repl.ast.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private List<ParserError> errors = new ArrayList<>();
    private int currentTokenIndex = 0;
    private int parenthesesDepth = 0;

    private Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public static ParserResult parse(String text) {
        Lexer lexer = new Lexer(text);

        List<Token> tokenList;

        try {
            tokenList = lexer.tokenize();
        } catch (LexerException e) {
            return new ParserResult(
                    Arrays.asList(
                            new ParserError(e.getLocation(), e.getLocation(), e.getMessage())
                    )
            );
        }

        Parser parser = new Parser(tokenList);
        return parser.parse();
    }

    private ParserResult parse() {
        ASTNode root = null;

        if (tokens.size() > 0) {
            root = parseStatement();
        }

        return new ParserResult(
                root,
                errors
        );
    }

    private ASTNode parseStatement() {
        if (tokens.size() > 2 &&
                tokens.get(0).getType() == TokenType.IDENTIFIER &&
                tokens.get(1).getType() == TokenType.ASSIGNMENT) {
            currentTokenIndex = 2;

            return new AssignmentNode(
                    tokens.get(0).getBegin(),
                    tokens.get(tokens.size() - 1).getEnd(),
                    tokens.get(0).getValue(),
                    parseExpression()
            );
        }

        currentTokenIndex = 0;

        return parseExpression();
    }

    private Token currentToken() {
        if (currentTokenIndex >= tokens.size()) {
            return null;
        }

        return tokens.get(currentTokenIndex);
    }

    private Token nextToken() {
        ++currentTokenIndex;
        return currentToken();
    }

    private Token currentOrLastToken() {
        if (currentToken() != null) {
            return currentToken();
        } else {
            return tokens.get(tokens.size() - 1);
        }
    }

    private void addError(String msg) {
        errors.add(
                new ParserError(currentOrLastToken().getBegin(), currentOrLastToken().getEnd(), msg)
        );
    }

    private ASTNode buildErrorNode() {
        return new ErrorNode(currentOrLastToken().getBegin(), currentOrLastToken().getEnd());
    }

    private void assertTokenType(TokenType type, String msg) {
        if (currentToken() == null || currentToken().getType() != type) {
            addError(msg);
        } else {
            nextToken();
        }
    }

    private ASTNode parseExpression() {
        ExpressionParserStack stack = new ExpressionParserStack();

        while (currentToken() != null) {
            ASTNode operand = parseOperand();

            if (operand instanceof ErrorNode &&
                    !stack.operators.empty() &&
                    stack.operators.peek() == BinOpType.UNKNOWN &&
                    stack.isAwaitingOperand()) {
                stack.operators.pop();
                continue;
            }

            stack.operands.push(operand);

            if (currentToken() == null || (currentToken().getType() == TokenType.RIGHT_PARENTHESIS && parenthesesDepth > 0)) {
                break;
            }

            BinOpType type;

            if (currentToken().getType() != TokenType.BINARY_OPERATOR) {
                addError("Binary operator expected");
                type = BinOpType.UNKNOWN;
            } else {
                type = BinOpUtils.getType(currentToken().getValue());
                nextToken();
            }

            while (!stack.operators.empty() &&
                    BinOpUtils.priority(stack.operators.peek()) >= BinOpUtils.priority(type)) {
                stack.shiftOperation();
            }

            stack.operators.push(type);

        }

        while (stack.operators.size() > 0) {
            if (stack.isAwaitingOperand()) {
                addError("Operand expected");
                stack.operands.push(buildErrorNode());
            }
            stack.shiftOperation();
        }

        if (stack.operands.empty()) {
            return buildErrorNode();
        }

        return stack.result();
    }

    private ASTNode parseOperand() {
        if (currentToken().getType() == TokenType.LEFT_PARENTHESIS) {
            nextToken();

            parenthesesDepth++;

            ASTNode result = parseExpression();

            assertTokenType(TokenType.RIGHT_PARENTHESIS, "\")\" expected");

            parenthesesDepth--;

            return result;
        }

        if (currentToken().getType() == TokenType.RIGHT_PARENTHESIS) {
            if (parenthesesDepth > 0) {
                addError("operand expected");
                return new ErrorNode(currentToken().getBegin(), currentToken().getEnd());
            }
        }

        return parseLiteralOrVar();
    }

    private ASTNode parseLiteralOrVar() {
        Token token = currentToken();
        nextToken();

        switch (token.getType()) {
            case IDENTIFIER:
                return new IdentifierNode(token.getBegin(), token.getValue());
            case LITERAL:
                return new LiteralNode(token.getBegin(), token.getEnd(), Integer.valueOf(token.getValue()));
            default:
                addError("Unexpected term value");
                return new ErrorNode(token.getBegin(), token.getEnd());
        }
    }
}
