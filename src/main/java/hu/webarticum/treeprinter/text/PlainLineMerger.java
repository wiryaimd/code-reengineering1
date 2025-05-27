package hu.webarticum.treeprinter.text;

public class PlainLineMerger implements LineMerger {
    @Override
    public String merge(String existingLine, int fromPosition, String replacement) {
        String beforePart = buildBeforePart(existingLine, fromPosition);
        String afterPart = buildAfterPart(existingLine, fromPosition, replacement.length());
        return beforePart + replacement + afterPart;
    }

    private String buildBeforePart(String line, int fromPosition) {
        int lineLength = line.length();

        if (lineLength <= fromPosition) {
            String pad = TextUtil.repeat(' ', fromPosition - lineLength);
            return line + pad;
        }

        return line.substring(0, fromPosition);
    }

    private String buildAfterPart(String line, int fromPosition, int replacementLength) {
        int endPosition = fromPosition + replacementLength;

        if (endPosition < line.length()) {
            return line.substring(endPosition);
        }

        return "";
    }
}
