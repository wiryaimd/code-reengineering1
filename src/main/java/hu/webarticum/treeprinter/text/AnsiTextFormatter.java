package hu.webarticum.treeprinter.text;

import java.util.regex.Matcher;

/**
 * Utility class for formatting ANSI text.
 * 
 * @see AnsiFormat
 */
public class AnsiTextFormatter {
    public static String stripAnsi(String ansiText) {
        String plainText = TextPatterns.getAnsiEscapePattern()
            .matcher(ansiText)
            .replaceAll("");
        return plainText.equals(ansiText) ? ansiText : plainText;
    }
    
    public static String format(String line, AnsiFormat format) {
        String formatString = format.toString();
        if (formatString.isEmpty()) {
            return line;
        }
        
        int length = line.length();
        StringBuffer resultBuffer = new StringBuffer();
        Matcher matcher = TextPatterns.getAnsiEscapesPattern().matcher(line);
        int endPos = 0;
        
        while (matcher.find()) {
            String ansiEscapes = matcher.group();
            boolean isEmpty = matcher.start() == endPos;
            if (!isEmpty) {
                resultBuffer.append(formatString);
            }
            boolean mustReset = !isEmpty && !ansiEscapes.startsWith(TextPatterns.getAnsiResetPattern());
            matcher.appendReplacement(resultBuffer, mustReset ? TextPatterns.getAnsiResetPattern() + ansiEscapes : ansiEscapes);
            endPos = matcher.end();
        }
        
        if (endPos < length) {
            resultBuffer.append(formatString);
            matcher.appendTail(resultBuffer);
            resultBuffer.append(TextPatterns.getAnsiResetPattern());
        }
        return resultBuffer.toString();
    }
    
    public static String ansiReset() {
        return TextPatterns.getAnsiResetPattern();
    }
} 
