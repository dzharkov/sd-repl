package ru.spbau.mit.repl.interpreter;

public interface Command {
    void execute();

    void undo();

    boolean isExecuted();

    String doDescription();

    String undoDescription();
}
