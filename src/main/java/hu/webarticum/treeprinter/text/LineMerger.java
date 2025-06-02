package hu.webarticum.treeprinter.text;

/**
 * Functional interface for merging lines of text.
 * 
 * @see Replacer
 */
@FunctionalInterface
public interface LineMerger {

    public String merge(Replacer replacer);
    
}
