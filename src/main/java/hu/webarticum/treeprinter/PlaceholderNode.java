package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.text.ConsoleText;

/**
 * 
 * This is a default implementation for placeholder nodes.
 * 
 * @see TreeNode
 * @see TreePrinter
 * @see TreePrinter.Builder
 */
public class PlaceholderNode implements TreeNode {

    @Override
    public ConsoleText content() {
        return ConsoleText.empty();
    }

    @Override
    public List<TreeNode> children() {
        return new ArrayList<>(0);
    }

    @Override
    public boolean isDecorable() {
        return false;
    }

    @Override
    public boolean isPlaceholder() {
        return true;
    }

}
