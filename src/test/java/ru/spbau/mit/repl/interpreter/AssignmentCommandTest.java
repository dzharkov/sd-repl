package ru.spbau.mit.repl.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class AssignmentCommandTest {
    @Test
    public void testDoUndo() throws Exception {
        InterpreterState state = new InterpreterState(
                new VariableValuePair("x", 2),
                new VariableValuePair("y", 3)
        );

        AssignmentCommand command = new AssignmentCommand(state, "y", 4);
        command.execute();

        Assert.assertEquals(state.getVariableValue("y").intValue(), 4);
        Assert.assertEquals(state.getVariableValue("x").intValue(), 2);

        command.undo();

        Assert.assertEquals(state.getVariableValue("y").intValue(), 3);
        Assert.assertEquals(state.getVariableValue("x").intValue(), 2);
    }

    @Test
    public void testDoUndef() throws Exception {
        InterpreterState state = new InterpreterState(
                new VariableValuePair("x", 2),
                new VariableValuePair("y", 3)
        );

        AssignmentCommand command = new AssignmentCommand(state, "z", 4);
        command.execute();

        Assert.assertEquals(state.getVariableValue("y").intValue(), 3);
        Assert.assertEquals(state.getVariableValue("x").intValue(), 2);
        Assert.assertEquals(state.getVariableValue("z").intValue(), 4);

        command.undo();

        Assert.assertEquals(state.getVariableValue("y").intValue(), 3);
        Assert.assertEquals(state.getVariableValue("x").intValue(), 2);
        Assert.assertNull(state.getVariableValue("z"));
    }
}
