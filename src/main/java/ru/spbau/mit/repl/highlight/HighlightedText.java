package ru.spbau.mit.repl.highlight;

import java.util.List;

public class HighlightedText {
    private final List<HighlightedChunk> chunks;

    public HighlightedText(List<HighlightedChunk> chunks) {
        this.chunks = chunks;
    }

    public List<HighlightedChunk> getChunks() {
        return chunks;
    }
}
