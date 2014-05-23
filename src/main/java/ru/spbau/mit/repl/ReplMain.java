package ru.spbau.mit.repl;

import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import ru.spbau.mit.repl.ast.ASTNode;
import ru.spbau.mit.repl.error.ReplLocationError;
import ru.spbau.mit.repl.highlight.ColorConsoleGenerator;
import ru.spbau.mit.repl.highlight.ErrorHighlighter;
import ru.spbau.mit.repl.highlight.HighlightedText;
import ru.spbau.mit.repl.interpreter.Command;
import ru.spbau.mit.repl.interpreter.InterpreterState;
import ru.spbau.mit.repl.interpreter.InterpreterVisitor;
import ru.spbau.mit.repl.interpreter.NoCommandFoundException;
import ru.spbau.mit.repl.parser.Parser;
import ru.spbau.mit.repl.parser.ParserResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ReplMain implements Runnable {
    private static final String UNDO_COMMAND = ":undo";

    private final InterpreterState interpreterState = new InterpreterState();
    private PrintWriter consoleWriter;
    private ColorConsoleGenerator colorConsoleGenerator = new ColorConsoleGenerator();

    public static void main(String[] args) throws IOException {
        new Thread(new ReplMain()).start();
    }

    @Override
    public void run() {
        try {
            runRepl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runRepl() throws IOException {
        ConsoleReader consoleReader = new ConsoleReader();
        consoleReader.setHistoryEnabled(true);

        consoleReader.addCompleter(new StringsCompleter(UNDO_COMMAND));

        String line;

        consoleWriter = new PrintWriter(consoleReader.getOutput());

        while ((line = consoleReader.readLine("> ")) != null) {
            line = line.trim();
            if (line.equalsIgnoreCase(UNDO_COMMAND)) {
                undoLastCommand();
            } else {
                interpret(line);
            }

            consoleWriter.flush();
        }
    }

    private void interpret(String text) {
        ParserResult parseResult = Parser.parse(text);
        List<ReplLocationError> errors = new ArrayList<>();

        if (!parseResult.isSuccessful()) {
            errors.addAll(parseResult.getErrors());
        }

        if (parseResult.getRoot() != null) {
            processParseResult(parseResult.getRoot(), errors);
        }

        if (!errors.isEmpty()) {
            highlightErrors(text, errors);
        }
    }

    private void processParseResult(ASTNode root, List<ReplLocationError> errors) {
        InterpreterVisitor interpreterVisitor = new InterpreterVisitor(interpreterState);
        Integer result = root.accept(interpreterVisitor);

        if (!interpreterVisitor.getErrors().isEmpty()) {
            errors.addAll(interpreterVisitor.getErrors());
        }

        if (errors.isEmpty()) {
            executeCommands(interpreterVisitor.getCommands());
            consoleWriter.println(result);
        }
    }

    private void executeCommands(List<Command> commands) {
        for (Command command : commands) {
            interpreterState.execute(command);
            consoleWriter.println(command.doDescription());
        }
    }

    private void undoLastCommand() {
        try {
            consoleWriter.println(
                    interpreterState.undoLastCommand().undoDescription()
            );
        } catch (NoCommandFoundException e) {
            consoleWriter.println("Nothing to undo");
        }
    }

    private void highlightErrors(String text, List<ReplLocationError> errors) {
        HighlightedText highlightedText = ErrorHighlighter.highlightErrors(text, errors);

        consoleWriter.println(colorConsoleGenerator.generateColoredString(highlightedText));

    }
}
