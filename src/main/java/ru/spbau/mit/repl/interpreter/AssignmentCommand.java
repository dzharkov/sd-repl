package ru.spbau.mit.repl.interpreter;

public class AssignmentCommand implements Command {
    private final InterpreterState state;
    private final String varName;
    private final int newValue;

    private Integer oldValue;
    private boolean isExecuted = false;

    public AssignmentCommand(InterpreterState state, String varName, int newValue) {
        this.state = state;
        this.varName = varName;
        this.newValue = newValue;
    }

    @Override
    public void execute() {
        oldValue = state.getVariableValue(varName);
        state.setVariable(varName, newValue);

        isExecuted = true;
    }

    @Override
    public void undo() {
        if (oldValue == null) {
            state.unsetVariable(varName);
        } else {
            state.setVariable(varName, oldValue);
        }

        isExecuted = false;
    }

    @Override
    public String doDescription() {
        return "Assignment: " + varName + "=" + newValue;
    }

    @Override
    public String undoDescription() {
        return "Restore value: " + varName + "=" +
                (oldValue == null ? "null" : oldValue.toString());
    }

    @Override
    public boolean isExecuted() {
        return isExecuted;
    }

    public String getVarName() {
        return varName;
    }

    public int getNewValue() {
        return newValue;
    }
}
