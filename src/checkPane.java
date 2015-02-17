import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;


public class checkPane extends JTextPane {

    public checkPane() {

        final SimpleAttributeSet red = new SimpleAttributeSet();
        StyleConstants.setForeground(red, Color.YELLOW);
        final SimpleAttributeSet black = new SimpleAttributeSet();
        StyleConstants.setForeground(black, Color.WHITE);
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLast(text, offset);
                if (before < 0) before = 0;
                int after = findFirst(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after) {
                        String refined = text.substring(wordL, wordR);
                        //refined = refined.replaceAll("[\\d[^\\w\\s]]+", "").replaceAll("(\\s{2,})", " ").replaceAll("[^a-zA-Z0-9]","");
                        refined = refined.replaceAll(" ", "").replaceAll("(?!\')\\p{Punct}", "");
                        //refined = refined.replaceAll(" ", "");
                        System.out.println("at insert *"+refined+"*");
                        if(!refined.equals(""))
                        {
                            if(Mainframe.check(refined))
                            {
                                setCharacterAttributes(wordL, wordR - wordL, black, false);
                            }
                            else
                            {
                                setCharacterAttributes(wordL, wordR - wordL, red, false);
                            }
                        }
                        else
                        {
                            setCharacterAttributes(wordL, wordR - wordL, black, false);
                        }
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                String text = getText(0, getLength());
                int before = findLast(text, offs);
                if (before < 0) before = 0;
                int after = findFirst(text, offs);

                String refined = text.substring(before, after);
                refined = refined.replaceAll(" ", "").replaceAll("(?!\')\\p{Punct}", "");
                //refined = refined.replaceAll("[^a-zA-Z0-9]","");
                //System.out.println("at remove *"+refined+"*");
                if(!refined.equals(""))
                {
                    if (Mainframe.check(refined)) {
                        //System.out.println("right");
                        setCharacterAttributes(before, after - before, black, false);
                    } else {
                        //System.out.println("wrong: *" + refined + "*");
                        setCharacterAttributes(before, after - before, red, false);
                    }
                }
                else
                {
                    setCharacterAttributes(before, after - before, black, false);
                }
            }
        };
        setDocument(doc);
        Debug.Log("initialized checkPane");
    }

    private int findLast (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                if(text.charAt(index)!='\'')
                {
                    break;
                }
            }
        }
        return index;
    }

    private int findFirst (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                if(text.charAt(index)!='\'')
                {
                    break;
                }
            }
            index++;
        }
        return index;
    }
}


