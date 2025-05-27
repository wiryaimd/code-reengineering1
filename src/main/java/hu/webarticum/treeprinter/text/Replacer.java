package hu.webarticum.treeprinter.text;

// Treatment introduce parameter object
public class Replacer {
    private String existingLine;
    private int fromPosition;
    private String replacement;

    public Replacer(String existingLine, int fromPosition, String replacement) {
        this.existingLine = existingLine;
        this.fromPosition = fromPosition;
        this.replacement = replacement;
    }

    public String getExistingLine() {
        return existingLine;
    }

    public int getFromPosition() {
        return fromPosition;
    }
    
    public String getReplacement() {
        return replacement;
    }
}
