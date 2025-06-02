package hu.webarticum.treeprinter.printer.traditional;

import hu.webarticum.treeprinter.text.LineBuffer;

public class LineWriter {
    private final LineBuffer buffer;
    
    public LineWriter(LineBuffer buffer) {
        this.buffer = buffer;
    }
    
    public void writeVerticalLine(int row, int col, int height, LineContent content) {
        for (int i = 0; i < height; i++) {
            buffer.write(row + i, col, content.format());
        }
    }
    
    public void writeHorizontalLine(int row, int startCol, String content, LineContent formatter) {
        buffer.write(row, startCol, formatter.format());
    }
    
    public void writeBottomConnections(int row, int startCol, String content, LineContent formatter, int height) {
        for (int i = 0; i < height; i++) {
            buffer.write(row + i, startCol, formatter.format());
        }
    }
} 