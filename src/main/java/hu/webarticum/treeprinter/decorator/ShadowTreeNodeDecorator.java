package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.Insets;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.UnicodeMode;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;
import hu.webarticum.treeprinter.text.Dimensions;
import hu.webarticum.treeprinter.text.PlainConsoleText;
import hu.webarticum.treeprinter.text.TextUtil;

public class ShadowTreeNodeDecorator extends AbstractTreeNodeDecorator {

    private static final char EMPTY_CHAR = ' ';
    private static final char UNICODE_SHADOW_CHAR = '\u2592';
    private static final char ASCII_SHADOW_CHAR = '#';

    private final char shadowChar;
    private final int verticalOffset;
    private final int horizontalOffset;
    private final AnsiFormat format;

    public ShadowTreeNodeDecorator(TreeNode baseNode) {
        this(baseNode, builder());
    }

    public ShadowTreeNodeDecorator(TreeNode baseNode, AnsiFormat format) {
        this(baseNode, builder().format(format));
    }

    private ShadowTreeNodeDecorator(TreeNode baseNode, Builder builder) {
        super(baseNode, builder.inherit, builder.decorable);
        this.shadowChar = builder.shadowChar;
        this.verticalOffset = builder.verticalOffset;
        this.horizontalOffset = builder.horizontalOffset;
        this.format = builder.format;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public ConsoleText decoratedContent() {
        ConsoleText baseContent = baseNode.content();
        ConsoleText[] baseLines = TextUtil.linesOf(baseContent);
        Dimensions baseDimensions = baseContent.dimensions();
        int baseWidth = baseDimensions.width();
        int baseHeight = baseDimensions.height();

        StringBuilder resultBuilder = new StringBuilder();

        drawShadowTop(resultBuilder, baseHeight, baseWidth);
        drawContentWithShadow(resultBuilder, baseLines, baseWidth, baseHeight);
        drawShadowBottom(resultBuilder, baseHeight, baseWidth);

        String decoratedContent = resultBuilder.toString();
        boolean isPlain = (baseNode instanceof PlainConsoleText) && (format == AnsiFormat.NONE);
        return isPlain ? ConsoleText.of(decoratedContent) : ConsoleText.ofAnsi(decoratedContent);
    }

    private void drawShadowTop(StringBuilder builder, int baseHeight, int baseWidth) {
        String shadowLine = buildShadowLine(baseWidth);
        String shadowEmptyPrefix = buildShadowEmptyPrefix();

        int topStart = Math.min(0, verticalOffset);
        int topEnd = Math.min(0, baseHeight + verticalOffset);

        for (int i = topStart; i < topEnd; i++) {
            builder.append(shadowEmptyPrefix).append(shadowLine).append('\n');
        }
        for (int i = topEnd; i < 0; i++) {
            builder.append('\n');
        }
    }

    private void drawContentWithShadow(StringBuilder builder, ConsoleText[] baseLines, int baseWidth, int baseHeight) {
        String emptyPrefix = buildEmptyPrefix();
        String shadowPrefix = buildShadowPrefix();
        String shadowSuffix = buildShadowSuffix();

        int middleStart = Math.max(0, Math.min(baseHeight, verticalOffset));
        int middleEnd = Math.max(0, Math.min(baseHeight, baseHeight + verticalOffset));

        for (int i = 0; i < middleStart; i++) {
            builder.append(emptyPrefix).append(baseLines[i].ansi()).append('\n');
        }
        for (int i = middleStart; i < middleEnd; i++) {
            builder.append(formatShadow(shadowPrefix).ansi());
            builder.append(baseLines[i].ansi());
            TextUtil.repeat(builder, ' ', baseWidth - baseLines[i].dimensions().width());
            builder.append(formatShadow(shadowSuffix).ansi());
            builder.append('\n');
        }
        for (int i = middleEnd; i < baseHeight; i++) {
            builder.append(emptyPrefix).append(baseLines[i].ansi()).append('\n');
        }
    }

    private void drawShadowBottom(StringBuilder builder, int baseHeight, int baseWidth) {
        String shadowLine = buildShadowLine(baseWidth);
        String shadowEmptyPrefix = buildShadowEmptyPrefix();

        int bottomStart = Math.max(baseHeight, verticalOffset);
        int bottomEnd = Math.max(baseHeight, baseHeight + verticalOffset);

        for (int i = baseHeight; i < bottomStart; i++) {
            builder.append('\n');
        }
        for (int i = bottomStart; i < bottomEnd; i++) {
            builder.append(shadowEmptyPrefix).append(formatShadow(shadowLine).ansi()).append('\n');
        }
    }

    private ConsoleText formatShadow(String shadowText) {
        return ConsoleText.of(shadowText).format(format);
    }

    private String buildShadowLine(int width) {
        return TextUtil.repeat(shadowChar, width);
    }

    private String buildShadowEmptyPrefix() {
        return horizontalOffset <= 0 ? "" : TextUtil.repeat(EMPTY_CHAR, horizontalOffset);
    }

    private String buildEmptyPrefix() {
        return horizontalOffset >= 0 ? "" : TextUtil.repeat(EMPTY_CHAR, -horizontalOffset);
    }

    private String buildShadowPrefix() {
        return horizontalOffset >= 0 ? "" : TextUtil.repeat(shadowChar, -horizontalOffset);
    }

    private String buildShadowSuffix() {
        return horizontalOffset <= 0 ? "" : TextUtil.repeat(shadowChar, horizontalOffset);
    }

    @Override
    public Insets insets() {
        Insets shadowInsets = new Insets(
                Math.max(0, -verticalOffset),
                Math.max(0, horizontalOffset),
                Math.max(0, verticalOffset),
                Math.max(0, -horizontalOffset));
        return baseNode.insets().extendedWith(shadowInsets);
    }

    @Override
    protected TreeNode wrapChild(TreeNode childNode, int index) {
        return builder()
                .decorable(decorable)
                .inherit(inherit)
                .shadowChar(shadowChar)
                .verticalOffset(verticalOffset)
                .horizontalOffset(horizontalOffset)
                .format(format)
                .buildFor(childNode);
    }

    public static class Builder {

        private boolean inherit = true;
        private boolean decorable = true;
        private char shadowChar = UnicodeMode.isUnicodeDefault() ? UNICODE_SHADOW_CHAR : ASCII_SHADOW_CHAR;
        private int verticalOffset = 1;
        private int horizontalOffset = 1;
        private AnsiFormat format = AnsiFormat.NONE;

        public Builder inherit(boolean inherit) {
            this.inherit = inherit;
            return this;
        }

        public Builder decorable(boolean decorable) {
            this.decorable = decorable;
            return this;
        }

        public Builder shadowChar(char shadowChar) {
            this.shadowChar = shadowChar;
            return this;
        }

        public Builder verticalOffset(int verticalOffset) {
            this.verticalOffset = verticalOffset;
            return this;
        }

        public Builder horizontalOffset(int horizontalOffset) {
            this.horizontalOffset = horizontalOffset;
            return this;
        }

        public Builder format(AnsiFormat format) {
            this.format = format;
            return this;
        }

        public ShadowTreeNodeDecorator buildFor(TreeNode node) {
            return new ShadowTreeNodeDecorator(node, this);
        }
    }
}