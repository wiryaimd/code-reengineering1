package hu.webarticum.treeprinter.text;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Utility class for cleaning text.
 * 
 * @see TextPatterns
 */
public class TextCleaner {
    private static final String TAB_SPACES = TextPatterns.getTabSpaces();
    
    public enum CleaningMode {
        PLAIN,
        ANSI
    }
    
    public static String clean(String text, CleaningMode mode) {
        Pattern controlPattern = mode == CleaningMode.PLAIN ? 
            TextPatterns.getAsciiControlPattern() : 
            TextPatterns.getAsciiControlExceptFormattingPattern();
            
        return new TextCleaningChain(text)
            .removeControlCharacters(controlPattern)
            .normalizeText()
            .replaceTabs()
            .normalizeLine()
            .getText();
    }
    
    private static class TextCleaningChain {
        private String text;
        
        public TextCleaningChain(String text) {
            this.text = text;
        }
        
        public TextCleaningChain removeControlCharacters(Pattern controlPattern) {
            text = controlPattern.matcher(text).replaceAll("");
            return this;
        }
        
        public TextCleaningChain normalizeText() {
            text = Normalizer.normalize(text, Normalizer.Form.NFD);
            return this;
        }
        
        public TextCleaningChain replaceTabs() {
            text = text.replace("\t", TextPatterns.getTabSpaces());
            return this;
        }
        
        public TextCleaningChain normalizeLine() {
            text = TextPatterns.getLineSeparatorPattern().matcher(text).replaceAll("\n");
            return this;
        }
        
        public String getText() {
            return text;
        }
    }
} 
