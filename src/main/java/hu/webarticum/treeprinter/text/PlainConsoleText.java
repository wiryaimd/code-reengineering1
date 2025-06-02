package hu.webarticum.treeprinter.text;

/**
 * Implementation of {@link ConsoleText} that represents plain text.
 * 
 * @see ConsoleText
 * @see AnsiConsoleText
 */
public class PlainConsoleText implements ConsoleText {
    private final String plainText;
    
    public PlainConsoleText(char plainChar) {
        this(plainChar + "");
    }
    
    public PlainConsoleText(String plainText) {
        this(plainText, true);
    }

    PlainConsoleText(String plainText, boolean clean) {
        this.plainText = clean ? TextUtil.cleanPlain(plainText) : plainText;
    }

    @Override
    public String plain() {
        return plainText;
    }

    @Override
    public String ansi() {
        return plainText;
    }

    @Override
    public ConsoleText concat(ConsoleText consoleText) {
        if (consoleText instanceof PlainConsoleText) {
            return new PlainConsoleText(this.plainText + consoleText.plain(), false);
        } else {
            return new AnsiConsoleText(this.plainText + consoleText.ansi(), false);
        }
    }
}
