package hu.webarticum.treeprinter.demo;

import hu.webarticum.treeprinter.*;
import hu.webarticum.treeprinter.decorator.*;
import hu.webarticum.treeprinter.printer.boxing.BoxingTreePrinter;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;

/*
 * Smells dalam kode
 * 1. Long Method			: solusinya extract method
 * 2. Primitive Obsession	: solusinya replace data value dengan object
 * 3. Feature Envy			: solusinya extract method
 */

public class ExamplesMain {

    private static final int SEPARATOR_WIDTH = 75;

    public static void main(String[] args) {
        TreeNode rootNode = buildTree();

        printAllVariants(rootNode);
    }

    private static void printAllVariants(TreeNode rootNode) {
        printDefaultListing(rootNode);
        printSeparator();

        printAsciiListing(rootNode);
        printSeparator();

        printAlignedListingWithPadding(rootNode);
        printSeparator();

        printBorderedListingWithPadding(rootNode);
        printSeparator();

        printTraditionalWithShadow(rootNode);
        printSeparator();

        printBoxing(rootNode);
    }

    private static void printDefaultListing(TreeNode node) {
        new ListingTreePrinter().print(node);
    }

    private static void printAsciiListing(TreeNode node) {
        ListingTreePrinter.builder()
                .ascii()
                .liningSpace("...")
                .build()
                .print(node);
    }

    private static void printAlignedListingWithPadding(TreeNode node) {
        ListingTreePrinter.builder()
                .displayRoot(false)
                .align(true)
                .build()
                .print(PadTreeNodeDecorator.builder().bottomPad(1).buildFor(node));
    }

    private static void printBorderedListingWithPadding(TreeNode node) {
        new ListingTreePrinter().print(
                new BorderTreeNodeDecorator(
                        PadTreeNodeDecorator.builder()
                                .verticalPad(1)
                                .horizontalPad(2)
                                .buildFor(node)));
    }

    private static void printTraditionalWithShadow(TreeNode node) {
        new TraditionalTreePrinter().print(
                new ShadowTreeNodeDecorator(
                        BorderTreeNodeDecorator.builder().wideUnicode().buildFor(
                                new PadTreeNodeDecorator(node, new Insets(0, 1)))));
    }

    private static void printBoxing(TreeNode node) {
        new BoxingTreePrinter().print(node);
    }

    private static TreeNode buildTree() {
        TestNode rootNode = new TestNode("root");

        TestNode subNode1 = new TestNode("SUB asdf\nSSS fdsa\nxxx yyy");
        TestNode subNode2 = new TestNode("lorem ipsum");
        TestNode subNode3 = new TestNode("ggggg");

        TestNode subSubNode11 = new TestNode("AAA");
        TestNode subSubNode12 = new TestNode("BBB");
        TestNode subSubNode21 = new TestNode("CCC");
        TestNode subSubNode22 = new TestNode("DDD");
        TestNode subSubNode23 = new TestNode("EEE");
        TestNode subSubNode24 = new TestNode("FFF");
        TestNode subSubNode31 = new TestNode("GGG");

        TestNode subSubSubNode231 = new TestNode("(eee)");
        TestNode subSubSubNode232 = new TestNode("(eee2)");
        TestNode subSubSubNode311 = new TestNode("(ggg)");

        rootNode.addChild(subNode1);
        rootNode.addChild(new PlaceholderNode());
        rootNode.addChild(subNode2);
        rootNode.addChild(subNode3);

        subNode1.addChild(subSubNode11);
        subNode1.addChild(subSubNode12);
        subNode2.addChild(subSubNode21);
        subNode2.addChild(subSubNode22);
        subNode2.addChild(subSubNode23);
        subNode2.addChild(subSubNode24);
        subNode3.addChild(subSubNode31);

        subSubNode23.addChild(subSubSubNode231);
        subSubNode23.addChild(subSubSubNode232);
        subSubNode31.addChild(subSubSubNode311);

        return rootNode;
    }

    private static void printSeparator() {
        System.out.println();
        System.out.println("=".repeat(SEPARATOR_WIDTH));
        System.out.println();
    }

    private static class TestNode extends SimpleTreeNode {

        TestNode(String content) {
            super(content);
        }

        @Override
        public boolean isDecorable() {
            String plainContent = content().plain();
            return plainContent.isEmpty() || plainContent.charAt(0) != '(';
        }
    }
}
