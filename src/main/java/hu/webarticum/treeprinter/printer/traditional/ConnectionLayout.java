package hu.webarticum.treeprinter.printer.traditional;

import java.util.List;

public class ConnectionLayout {
    private final int topConnection;
    private final List<Integer> bottomConnections;
    private final int topHeight;
    private final int bottomHeight;
    private final boolean displayBracket;
    
    public ConnectionLayout(int topConnection, List<Integer> bottomConnections, 
            int topHeight, int bottomHeight, boolean displayBracket) {
        this.topConnection = topConnection;
        this.bottomConnections = bottomConnections;
        this.topHeight = topHeight;
        this.bottomHeight = bottomHeight;
        this.displayBracket = displayBracket;
    }
    
    public int getStart() {
        return Math.min(topConnection, bottomConnections.get(0));
    }
    
    public int getEnd() {
        return Math.max(topConnection, bottomConnections.get(bottomConnections.size() - 1));
    }
    
    public int getTopHeight() {
        return topHeight;
    }
    
    public int getTopHeightWithBracket() {
        return topHeight + (displayBracket ? 1 : 0);
    }
    
    public int getBottomHeight() {
        return bottomHeight;
    }
    
    public int getFullHeight() {
        return getTopHeightWithBracket() + bottomHeight;
    }
    
    public int getTopConnection() {
        return topConnection;
    }
    
    public List<Integer> getBottomConnections() {
        return bottomConnections;
    }
    
    public boolean hasBottomConnection(int position) {
        return bottomConnections.contains(position);
    }
} 