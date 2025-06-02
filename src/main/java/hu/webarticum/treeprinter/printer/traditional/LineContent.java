package hu.webarticum.treeprinter.printer.traditional;

import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;

public class LineContent {
    private final String content;
    private final AnsiFormat format;
    
    public LineContent(String content, AnsiFormat format) {
        this.content = content;
        this.format = format;
    }
    
    public LineContent(char content, AnsiFormat format) {
        this(String.valueOf(content), format);
    }
    
    public ConsoleText format() {
        return ConsoleText.of(content).format(format);
    }
    
    public String getContent() {
        return content;
    }
    
    public AnsiFormat getFormat() {
        return format;
    }
} 