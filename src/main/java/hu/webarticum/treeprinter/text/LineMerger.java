package hu.webarticum.treeprinter.text;

@FunctionalInterface
public interface LineMerger {

    public String merge(Replacer replacer);
    
}
