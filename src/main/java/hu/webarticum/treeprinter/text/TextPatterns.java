package hu.webarticum.treeprinter.text;

import java.util.regex.Pattern;

/**
 * Utility class for text patterns.
 * 
 * @see TextCleaner
 */
public class TextPatterns {
    private static final Pattern lineSeparator = Pattern.compile("\\R");
    private static final Pattern ansiEscape = Pattern.compile("\\e\\[[0-9;]*m");
    private static final Pattern ansiEscapes = Pattern.compile("(?:\\e\\[[0-9;]*m)+");
    private static final Pattern asciiControl = Pattern.compile("[\\u0000-\\u0008\\u000B\\u000C\\u000E-\\u001F]");
    private static final Pattern asciiControlExceptFormatting = Pattern.compile("([\\u0000-\\u0008\\u000B\\u000C\\u000E-\\u001A\\u001C-\\u001F]|\\e(?!\\[[0-9;]*m))");
    private static final String ansiReset = "\u001B[0m";
    private static final String tabSpaces = "    ";
       
    public static Pattern getLineSeparatorPattern() { return lineSeparator; }
    public static Pattern getAnsiEscapePattern() { return ansiEscape; }
    public static Pattern getAnsiEscapesPattern() { return ansiEscapes; }
    public static Pattern getAsciiControlPattern() { return asciiControl; }
    public static Pattern getAsciiControlExceptFormattingPattern() { return asciiControlExceptFormatting; }
    public static String getAnsiResetPattern() { return ansiReset; }
    public static String getTabSpaces() { return tabSpaces; }
    
} 
