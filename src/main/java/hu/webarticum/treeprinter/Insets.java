package hu.webarticum.treeprinter;

/**
 * Value class for storing inset values at top, right, bottom and left
 * 
 * @see TreeNode#insets()
 */
public class Insets {
    
    public static final Insets EMPTY = new Insets(0);
    private final int top;
    private final int right;
    private final int bottom;
    private final int left;
    
    public Insets(int inset) {
        this(inset, inset, inset, inset);
    }

    public Insets(int vertical, int horizontal) {
        this.top = vertical;
        this.right = horizontal;
        this.bottom = vertical;
        this.left = horizontal;
    }

    public Insets(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    private Insets(Builder builder) {
        this.top = builder.top;
        this.right = builder.right;
        this.bottom = builder.bottom;
        this.left = builder.left;
    }

    public static Builder builder() {
        return new Builder();
    }
    

    public int top() {
        return top;
    }

    public int right() {
        return right;
    }

    public int bottom() {
        return bottom;
    }

    public int left() {
        return left;
    }

    public Insets extendedWith(int inset) {
        return new Insets(
                this.top + inset,
                this.right + inset,
                this.bottom + inset,
                this.left + inset);
    }
    
    public Insets extendedWith(Insets other) {
        return new Insets(
                this.top + other.top,
                this.right + other.right,
                this.bottom + other.bottom,
                this.left + other.left);
    }
    
    public boolean isSymmetrical() {
        return top == bottom && left == right;
    }
    
    public int getTotalPadding() {
        return top + right + bottom + left;
    }
    
    public void applyTo(StringBuilder content) {
        addVerticalPadding(content, top, true);
        addHorizontalPadding(content, left, right);
        addVerticalPadding(content, bottom, false);
    }
    
    private void addVerticalPadding(StringBuilder content, int size, boolean isTop) {
        for (int i = 0; i < size; i++) {
            if (isTop) {
                content.insert(0, "\n");
            } else {
                content.append("\n");
            }
        }
    }
    
    private void addHorizontalPadding(StringBuilder content, int leftPad, int rightPad) {
        String[] lines = content.toString().split("\n");
        StringBuilder result = new StringBuilder();
        String leftPadding = " ".repeat(leftPad);
        String rightPadding = " ".repeat(rightPad);
        
        for (String line : lines) {
            result.append(leftPadding).append(line).append(rightPadding).append("\n");
        }
        content.setLength(0);
        content.append(result);
    }
    
    public static class Builder {

        private int top = 0;
        
        private int right = 0;
        
        private int bottom = 0;
        
        private int left = 0;
        

        public void top(int top) {
            this.top = top;
        }
        
        public void right(int right) {
            this.right = right;
        }
        
        public void bottom(int bottom) {
            this.bottom = bottom;
        }
        
        public void left(int left) {
            this.left = left;
        }
        
        public void all(int all) {
            this.top = all;
            this.right = all;
            this.bottom = all;
            this.left = all;
        }

        public void vertical(int vertical) {
            this.top = vertical;
            this.bottom = vertical;
        }

        public void horizontal(int horizontal) {
            this.right = horizontal;
            this.left = horizontal;
        }

        public Insets build() {
            if (top == 0 && right == 0 && bottom == 0 && left == 0) {
                return Insets.EMPTY;
            }
            
            return new Insets(this);
        }

    }
    
}
