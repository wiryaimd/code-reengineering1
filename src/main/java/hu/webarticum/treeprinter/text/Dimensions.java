package hu.webarticum.treeprinter.text;

/**
 * Value class for storing the dimensions of a text.
 * 
 * @see ConsoleText#dimensions()
 */
public class Dimensions {
    
    private final int width;
    private final int height;
    
    public Dimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }
    
    public int height() {
        return height;
    }    
}
