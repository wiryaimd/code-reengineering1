package hu.webarticum.treeprinter.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.AnsiLineMerger;
import hu.webarticum.treeprinter.text.ConsoleText;
import hu.webarticum.treeprinter.text.LineBuffer;
import hu.webarticum.treeprinter.text.LineMerger;
import hu.webarticum.treeprinter.text.PlainLineMerger;

public final class Util {
    
    private Util() {}

    public static String getStringContent(ConsoleText content, AnsiMode ansiMode) {
        return ansiMode.isEnabled() ? content.ansi() : content.plain();
    }

    public static ConsoleText toConsoleText(String stringContent, AnsiMode ansiMode) {
        return ansiMode.isEnabled() ? ConsoleText.ofAnsi(stringContent) : ConsoleText.of(stringContent);
    }

    public static LineBuffer createLineBuffer(Appendable out, AnsiMode ansiMode) {
        LineMerger lineMerger = ansiMode.isEnabled() ? new AnsiLineMerger() : new PlainLineMerger();
        return new LineBuffer(out, lineMerger, ansiMode);
    }
    
    // Smells Long Method: Solusi, Extract Method
    public static int getDepth(TreeNode treeNode) {
        List<TreeNode> levelNodes = new ArrayList<>();
        levelNodes.add(treeNode);
        int depth = 0;
        while (true) {
            // extract method getLevelNodes()
            List<TreeNode> newLevelNodes = getLevelNodes(levelNodes);
            if (newLevelNodes.isEmpty()) {
                break;
            }
            
            levelNodes = newLevelNodes;
            depth++;
        }
        return depth;
    }

    private static List<TreeNode> getLevelNodes(List<TreeNode> levelNodes) {
        List<TreeNode> newLevelNodes = new ArrayList<>();
        for (TreeNode levelNode: levelNodes) {
            newLevelNodes.addAll(levelNode.children());
        }
        return newLevelNodes;
    }

    public static void write(Appendable out, String content) {
        try {
            out.append(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeln(Appendable out, String content) {
        write(out, content + "\n");
    }
    
}
