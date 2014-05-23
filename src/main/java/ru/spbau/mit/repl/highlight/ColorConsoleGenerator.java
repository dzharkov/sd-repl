package ru.spbau.mit.repl.highlight;

public class ColorConsoleGenerator {

    public String generateColoredString(HighlightedText text) {
        StringBuilder result = new StringBuilder();

        for (HighlightedChunk chunk : text.getChunks()) {
            String chunkText = chunk.getChunk();

            if (chunk.hasColor()) {
                String colorCode = chunk.getColor() == Colors.COLOR_RED ? "31" : "34";

                chunkText = "\u001B[" + colorCode + "m" + chunkText + "\u001B[0m";
            }

            result.append(chunkText);
        }

        return result.toString();
    }

}
