package ru.spbau.mit.repl.highlight;

import ru.spbau.mit.repl.error.ReplLocationError;
import ru.spbau.mit.repl.interpreter.SemanticError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorHighlighter {

    public static HighlightedText highlightErrors(String text, List<ReplLocationError> errors) {
        int[] colors = new int[text.length()];
        Arrays.fill(colors, Colors.COLOR_NO_COLOR);

        for (ReplLocationError error : errors) {
            int color = getColorOfError(error);

            for (int i = error.getFrom().getColumn(); i <= error.getTo().getColumn(); i++) {
                if (colors[i] == Colors.COLOR_NO_COLOR) {
                    colors[i] = color;
                }
            }
        }

        List<HighlightedChunk> chunks = new ArrayList<>();

        int i = 0;
        while (i < text.length()) {
            int from = i;

            while (i < text.length() && colors[i] == colors[from]) {
                i++;
            }

            int to = i - 1;
            chunks.add(
                    new HighlightedChunk(
                            text.substring(from, to + 1),
                            colors[from] != Colors.COLOR_NO_COLOR,
                            colors[from]
                    )
            );
        }

        return new HighlightedText(chunks);
    }

    private static int getColorOfError(ReplLocationError error) {
        if (error instanceof SemanticError) {
            return Colors.COLOR_BLUE;
        }

        return Colors.COLOR_RED;
    }
}
