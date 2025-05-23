package hu.webarticum.treeprinter.printer.traditional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.decorator.TrackingTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.TreePrinter;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.Dimensions;
import hu.webarticum.treeprinter.text.LineBuffer;
import hu.webarticum.treeprinter.util.Util;

/**
 * {@link TreePrinter} implementation that prints the tree structure in a traditional fashion.
 * 
 * <p>Example output with the default settings:</p>
 * 
 * <pre>
 *                    Root
 *    ┌──────────────┬──┴──────────────┐
 *    │              │                 │
 * Child1         Child2            Child3
 *             ┌─────┴─────┐           │
 *             │           │           │
 *        Grandchild1 Grandchild2 Grandchild3
 * </pre>
 */
public class TraditionalTreePrinter implements TreePrinter {

    public static final Aligner DEFAULT_ALIGNER = new DefaultAligner();

    public static final Liner DEFAULT_LINER = new DefaultLiner();
    

    private final Aligner aligner;

    private final Liner liner;
    
    private final boolean displayPlaceholders;
    
    private final AnsiMode ansiMode;
    
    
    public TraditionalTreePrinter() {
        this(DEFAULT_ALIGNER, DEFAULT_LINER);
    }

    public TraditionalTreePrinter(AnsiFormat ansiFormat) {
        this(DEFAULT_ALIGNER, new DefaultLiner(ansiFormat));
    }

    public TraditionalTreePrinter(AnsiMode ansiMode, AnsiFormat ansiFormat) {
        this(DEFAULT_ALIGNER, new DefaultLiner(ansiFormat), false, ansiMode);
    }

    public TraditionalTreePrinter(Aligner aligner) {
        this(aligner, DEFAULT_LINER);
    }

    public TraditionalTreePrinter(Liner liner) {
        this(DEFAULT_ALIGNER, liner);
    }

    public TraditionalTreePrinter(Aligner aligner, Liner liner) {
        this(aligner, liner, false);
    }

    public TraditionalTreePrinter(boolean displayPlaceholders) {
        this(DEFAULT_ALIGNER, DEFAULT_LINER, displayPlaceholders);
    }

    public TraditionalTreePrinter(Aligner aligner, Liner liner, boolean displayPlaceholders) {
        this(aligner, liner, displayPlaceholders, AnsiMode.AUTO);
    }
    
    public TraditionalTreePrinter(Aligner aligner, Liner liner, boolean displayPlaceholders, AnsiMode ansiMode) {
        this.aligner = aligner;
        this.liner = liner;
        this.displayPlaceholders = displayPlaceholders;
        this.ansiMode = ansiMode;
    }
    
    
    @Override
    public void print(TreeNode rootNode, Appendable out) {
        TreeNode wrappedRootNode = new TrackingTreeNodeDecorator(rootNode);
        
        Map<TreeNode, Integer> widthMap = new HashMap<>();
        int rootWidth = aligner.collectWidths(widthMap, wrappedRootNode);
        
        Map<TreeNode, Position> positionMap = new HashMap<>();
        
        Dimensions rootContentDimensions = wrappedRootNode.content().dimensions();
        Placement rootPlacement = aligner.alignNode(wrappedRootNode, 0, rootWidth, rootContentDimensions.width());
        Position rootPosition = new Position(
                0, 0, rootPlacement.bottomConnection(), rootPlacement.left(), rootContentDimensions.height());
        positionMap.put(wrappedRootNode, rootPosition);
        
        LineBuffer buffer = Util.createLineBuffer(out, ansiMode);

        buffer.write(0, rootPlacement.left(), wrappedRootNode.content());
        
        buffer.flush();
        
        while (!positionMap.isEmpty()) {
            positionMap = printNextGeneration(buffer, positionMap, widthMap);
        }
        
        buffer.flush();
    }
    
    private Map<TreeNode, Position> printNextGeneration(
            LineBuffer buffer, Map<TreeNode, Position> positionMap, Map<TreeNode, Integer> widthMap) {
        Map<TreeNode, Position> newPositionMap = new HashMap<>();
        List<Integer> childBottoms = new ArrayList<>();
        for (Map.Entry<TreeNode, Position> entry: positionMap.entrySet()) {
            TreeNode node = entry.getKey();
            Position position = entry.getValue();
            handleNodeChildren(buffer, node, position, newPositionMap, widthMap, childBottoms);
        }

        if (!newPositionMap.isEmpty()) {
            int minimumChildBottom = Integer.MAX_VALUE;
            for (int bottomValue: childBottoms) {
                if (bottomValue < minimumChildBottom) {
                    minimumChildBottom = bottomValue;
                }
            }
            buffer.flush(minimumChildBottom);
        }
        
        return newPositionMap;
    }
    
    private void handleNodeChildren(
            LineBuffer buffer,
            TreeNode node,
            Position parentPosition,
            Map<TreeNode, Position> newPositionMap,
            Map<TreeNode, Integer> widthMap,
            List<Integer> childBottoms) {

        List<TreeNode> children = getFilteredChildren(node);
        if (children.isEmpty()) {
            return;
        }

        int[] alignedChildCols = aligner.alignChildren(node, children, parentPosition.col, widthMap);
        Map<TreeNode, Position> childPositions = new HashMap<>();
        List<Integer> childConnectionPoints = new ArrayList<>();

        for (int i = 0; i < children.size(); i++) {
            TreeNode child = children.get(i);
            int col = alignedChildCols[i];
            Position childPosition = calculateChildPosition(child, col, widthMap, parentPosition);
            childPositions.put(child, childPosition);
            childConnectionPoints.add(childPosition.connection);
        }

        int connectionRows = liner.printConnections(
                buffer, parentPosition.row + parentPosition.height, parentPosition.connection, childConnectionPoints);

        drawChildrenContent(buffer, childPositions, connectionRows, childBottoms);
        newPositionMap.putAll(childPositions);
    }

    private List<TreeNode> getFilteredChildren(TreeNode node) {
        List<TreeNode> children = new ArrayList<>(node.children());
        if (!displayPlaceholders) {
            children.removeIf(TreeNode::isPlaceholder);
        }
        return children;
    }

    private Position calculateChildPosition(
            TreeNode child, int col, Map<TreeNode, Integer> widthMap, Position parentPosition) {

        int childWidth = widthMap.get(child);
        Dimensions contentSize = child.content().dimensions();
        Placement placement = aligner.alignNode(child, col, childWidth, contentSize.width());

        return new Position(
                parentPosition.row + parentPosition.height,
                col,
                placement.topConnection(),
                placement.left(),
                contentSize.height());
    }

    private void drawChildrenContent(
            LineBuffer buffer,
            Map<TreeNode, Position> childPositions,
            int rowOffset,
            List<Integer> childBottoms) {

        for (Map.Entry<TreeNode, Position> entry : childPositions.entrySet()) {
            TreeNode child = entry.getKey();
            Position position = entry.getValue();
            position.row += rowOffset;
            buffer.write(position.row, position.left, child.content());
            childBottoms.add(position.row + position.height);
        }
    }

    
    
    private class Position {
        
        int row;
        
        int col;
        
        int connection;
        
        int left;
        
        int height;
        

        Position(int row, int col, int connection, int left, int height) {
            this.row = row;
            this.col = col;
            this.connection = connection;
            this.left = left;
            this.height = height;
        }
        
    }
    
}
