package hu.webarticum.treeprinter.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnsiLineMerger implements LineMerger {
    
    private static final char ESCAPE_CHAR = '\u001B';

    private static final Pattern ANSI_ESCAPE_PATTERN = Pattern.compile("^\\e(?:\\[[0-9;]*m)?");
    
    private static final String ANSI_RESET = TextUtil.ansiReset();

    @Override
    public String merge(String existingLine, int fromPosition, String replacement) {
        StringBuilder resultBuilder = new StringBuilder();

        int replacementWidth = calculateVisibleWidth(replacement);
        int replacementEnd = fromPosition + replacementWidth;

        int leadingLength = handleLeading(existingLine, fromPosition, resultBuilder);
        appendReplacement(replacement, resultBuilder);

        if (leadingLength >= 0 && !existingLine.isEmpty()) {
            appendTrailing(existingLine, leadingLength, fromPosition, replacementEnd, resultBuilder);
        }

        return resultBuilder.toString();
    }

    private int handleLeading(String existingLine, int fromPosition, StringBuilder resultBuilder) {
        if (fromPosition <= 0 || existingLine.isEmpty()) {
            TextUtil.repeat(resultBuilder, ' ', -fromPosition);
            return -fromPosition;
        }

        StringBuilder leadingEscapeStack = new StringBuilder();
        int leadingLength = locateVisible(existingLine, 0, 0, fromPosition, leadingEscapeStack);

        String leadingText = leadingLength > 0 ? existingLine.substring(0, leadingLength) : existingLine;
        resultBuilder.append(leadingText);

        if (leadingEscapeStack.length() > 0) {
            resultBuilder.append(ANSI_RESET);
        }

        return leadingLength;
    }

    private void appendReplacement(String replacement, StringBuilder resultBuilder) {
        StringBuilder escapeStack = new StringBuilder();
        locateVisible(replacement, 0, 0, replacement.length() + 1, escapeStack);

        resultBuilder.append(replacement);
        if (escapeStack.length() > 0) {
            resultBuilder.append(ANSI_RESET);
        }
    }

    private void appendTrailing(String existingLine, int leadingLength, int fromPosition, int visibleEnd, StringBuilder resultBuilder) {
        StringBuilder leadingEscapeStack = new StringBuilder();
        locateVisible(existingLine, 0, 0, fromPosition, leadingEscapeStack);

        StringBuilder trailingEscapeStack = new StringBuilder();
        int trailingIndex = locateVisible(existingLine, leadingLength, fromPosition, visibleEnd, trailingEscapeStack);

        if (trailingIndex >= 0) {
            resultBuilder.append(leadingEscapeStack);
            resultBuilder.append(trailingEscapeStack);
            resultBuilder.append(existingLine.substring(trailingIndex));
        }
    }

    private int calculateVisibleWidth(String line) {
        StringBuilder stack = new StringBuilder();
        int targetVisible = line.length() + 1;
        int result = locateVisible(line, 0, 0, targetVisible, stack);
        return targetVisible + result;
    }
    
    private int locateVisible(String line, int startIdx, int visibleStart, int visibleTarget, StringBuilder escapeStack) {
        int length = line.length();
        int idx = startIdx;
        int visibleIdx = visibleStart;

        while (idx < length && visibleIdx < visibleTarget) {
            char c = line.charAt(idx);
            if (c == ESCAPE_CHAR) {
                String ansi = extractAnsiEscape(line, idx);
                if (ANSI_RESET.equals(ansi)) {
                    escapeStack.setLength(0);
                } else {
                    escapeStack.append(ansi);
                }
                idx += ansi.length();
            } else {
                idx++;
                visibleIdx++;
            }
        }

        if (idx < length && line.substring(idx).equals(ANSI_RESET)) {
            escapeStack.setLength(0);
            return length;
        }

        return (visibleIdx < visibleTarget) ? visibleIdx - visibleTarget : idx;
    }

    private String extractAnsiEscape(String line, int position) {
        Matcher matcher = ANSI_ESCAPE_PATTERN.matcher(line.substring(position));
        return matcher.find() ? matcher.group() : String.valueOf(ESCAPE_CHAR);
    }
}
