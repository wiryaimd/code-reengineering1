package hu.webarticum.treeprinter.printer.boxing;

public class Offset {

    private int topOffset, leftOffset;

    public Offset(int topOffset, int leftOffset) {
        this.topOffset = topOffset;
        this.leftOffset = leftOffset;
    }

    public int getTopOffset() {
        return topOffset;
    }

    public int getLeftOffset() {
        return leftOffset;
    }
}
