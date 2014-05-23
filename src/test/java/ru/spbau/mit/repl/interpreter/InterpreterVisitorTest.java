package ru.spbau.mit.repl.interpreter;

import org.junit.Assert;
import org.junit.Test;
import ru.spbau.mit.repl.ast.*;

import java.util.List;

public class InterpreterVisitorTest {
    private void assertSuccessful(int expected, ASTNode node, VariableValuePair... values) {
        InterpreterVisitor visitor = new InterpreterVisitor(new InterpreterState(
            values
        ));

        Integer result = node.accept(visitor);

        Assert.assertTrue(visitor.getErrors().isEmpty());
        Assert.assertEquals(expected, result.intValue());

    }

    private void assertErrors(int errorsCount, ASTNode node, VariableValuePair... values) {
        InterpreterVisitor visitor = new InterpreterVisitor(new InterpreterState(
                values
        ));

        node.accept(visitor);

        Assert.assertEquals(errorsCount, visitor.getErrors().size());
    }

    @Test
    public void testExpression() throws Exception {
        assertSuccessful(15,
                new BinOpNode(
                        BinOpType.PLUS,
                        new BinOpNode(
                                BinOpType.MULTIPLY,
                                new LiteralNode(null, null, 2),
                                new IdentifierNode(null, "x")
                        ),
                        new BinOpNode(
                                BinOpType.MINUS,
                                new BinOpNode(
                                        BinOpType.DIVIDE,
                                        new IdentifierNode(null, "x"),
                                        new LiteralNode(null, null, 2)
                                ),
                                new LiteralNode(null, null, 5)
                        )
                ),
                new VariableValuePair("x", 8)
        );
    }

    @Test
    public void testExpressionErrors() throws Exception {
        assertErrors(2,
                new BinOpNode(
                        BinOpType.PLUS,
                        new BinOpNode(
                                BinOpType.MULTIPLY,
                                new IdentifierNode(null, "x"),
                                new IdentifierNode(null, "y")
                        ),
                        new BinOpNode(
                                BinOpType.MINUS,
                                new BinOpNode(
                                        BinOpType.DIVIDE,
                                        new IdentifierNode(null, "x"),
                                        new IdentifierNode(null, "z")
                                ),
                                new LiteralNode(null, null, 5)
                        )
                ),
                new VariableValuePair("x", 8), new VariableValuePair("z", 0)
        );
    }

    @Test
    public void testAssignment() throws Exception {
        InterpreterVisitor visitor = new InterpreterVisitor(new InterpreterState());

        new AssignmentNode(null, null, "abc", new LiteralNode(null, null, 1)).accept(visitor);
        new AssignmentNode(null, null, "cde", new LiteralNode(null, null, 2)).accept(visitor);

        List<Command> commands = visitor.getCommands();
        Assert.assertEquals(2, commands.size());

        AssignmentCommand command = (AssignmentCommand) commands.get(0);
        Assert.assertEquals(command.getNewValue(), 1);
        Assert.assertEquals(command.getVarName(), "abc");

        command = (AssignmentCommand) commands.get(1);
        Assert.assertEquals(command.getNewValue(), 2);
        Assert.assertEquals(command.getVarName(), "cde");
    }
}
