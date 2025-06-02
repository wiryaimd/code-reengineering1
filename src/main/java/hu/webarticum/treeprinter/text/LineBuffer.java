package hu.webarticum.treeprinter.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.util.Util;

public class LineBuffer {

    private final Appendable out;
    private final LineMerger lineMerger;
    private final AnsiMode ansiMode;
    private int flushedRowCount = 0;
    private List<String> lines = new ArrayList<>();
    
    public LineBuffer(Appendable out) {
        this(out, new PlainLineMerger());
    }

    public LineBuffer(Appendable out, LineMerger lineMerger) {
        this(out, lineMerger, AnsiMode.AUTO);
    }
    
    public LineBuffer(Appendable out, LineMerger lineMerger, AnsiMode ansiMode) {
        this.out = out;
        this.lineMerger = lineMerger;
        this.ansiMode = ansiMode;
    }
    
    public void write(int row, int col, ConsoleText content) {
        ConsoleText[] textLines = TextUtil.linesOf(content);
        int lineCount = textLines.length;
        for (int i = 0; i < lineCount; i++) {
            writeLine(row + i, col, Util.getStringContent(textLines[i], ansiMode));
        }
    }

    public void flush() {
        flush(flushedRowCount + lines.size());
    }

    public void flush(int untilRowIndex) {
        try {
            flushThrows(untilRowIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Smells yang ada:
    // 1. Dispensable, duplicate code: Solusi, extract method
    private void flushThrows(int untilRowIndex) throws IOException {
        if (untilRowIndex <= flushedRowCount) {
            return;
        }
        
        int currentLineCount = lines.size();
        int deleteLineCount = untilRowIndex - flushedRowCount;
        if (currentLineCount <= deleteLineCount) {
            // treatment extract method
            appendLine(lines.size());
            lines.clear();
        } else {
            appendLine(deleteLineCount);
            lines = new ArrayList<>(lines.subList(deleteLineCount, currentLineCount));
        }
        
        flushedRowCount = untilRowIndex;
    }

    private void appendLine(int size) throws IOException {
        for(int i = 0; i < size; i++) out.append(lines.get(i) + "\n");
    }

    private void writeLine(int row, int col, String textLine) {
        if (row < flushedRowCount) {
            return;
        }
        
        int currentLineCount = lines.size();
        int lineIndex = row - flushedRowCount;
        String originalLine;
        if (lineIndex < currentLineCount) {
            originalLine = lines.get(lineIndex);
        } else {
            for (int i = currentLineCount; i <= lineIndex; i++) {
                lines.add("");
            }
            originalLine = "";
        }

        // Long Parameter List, Treatment: Peserve Whole Object
        String newLine = lineMerger.merge(new Replacer(originalLine, col, textLine));
        lines.set(lineIndex, newLine);
    }    
}
