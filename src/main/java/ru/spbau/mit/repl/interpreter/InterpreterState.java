package ru.spbau.mit.repl.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class InterpreterState {
    private final Map<String, Integer> variables = new HashMap<>();
    private final Stack<Command> commands = new Stack<>();

    public InterpreterState() {
    }

    public InterpreterState(VariableValuePair... values) {
        for (VariableValuePair value : values) {
            setVariable(value.name, value.value);
        }
    }

    public void setVariable(String name, int value) {
        variables.put(name, value);
    }

    public Integer getVariableValue(String name) {
        return variables.get(name);
    }

    public void unsetVariable(String varName) {
        variables.remove(varName);
    }

    public void execute(Command command) {
        command.execute();
        commands.add(command);
    }

    public Command undoLastCommand() throws NoCommandFoundException {
        if (commands.isEmpty()) {
            throw new NoCommandFoundException();
        }

        Command command = commands.pop();
        command.undo();

        return command;
    }
}
