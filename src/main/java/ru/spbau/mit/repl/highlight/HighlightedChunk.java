package ru.spbau.mit.repl.highlight;

public class HighlightedChunk {
    private final String chunk;
    private final boolean hasColor;
    private final int color;

    public HighlightedChunk(String chunk, boolean hasColor, int color) {
        this.chunk = chunk;
        this.hasColor = hasColor;
        this.color = color;
    }

    public String getChunk() {
        return chunk;
    }

    public boolean hasColor() {
        return hasColor;
    }

    public int getColor() {
        return color;
    }
}
