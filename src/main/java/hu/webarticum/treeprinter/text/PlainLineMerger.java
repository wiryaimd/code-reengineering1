package hu.webarticum.treeprinter.text;

public class PlainLineMerger implements LineMerger {

    // Long Parameter List: Solusi, introduce parameter object
    @Override
    public String merge(Replacer replacer) {
        String beforeContent;
        String beforePad;

        int contextLineLength = replacer.getExistingLine().length();
        
        if (contextLineLength <= replacer.getFromPosition()) {
            beforeContent = replacer.getExistingLine();
            beforePad = TextUtil.repeat(' ', replacer.getFromPosition() - contextLineLength);
        } else {
            beforeContent = replacer.getExistingLine().substring(0, replacer.getFromPosition());
            beforePad = "";
        }

        int textLineLength = replacer.getReplacement().length();
        String afterContent;
        if (replacer.getFromPosition() + textLineLength < contextLineLength) {
            afterContent = replacer.getExistingLine().substring(replacer.getFromPosition() + textLineLength);
        } else {
            afterContent = "";
        }
        
        return beforeContent + beforePad + replacer.getReplacement() + afterContent;
    }
    
}
