/**
 * Created by 의현 on 2015-02-14.
 */
import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;

public class test extends JFrame {
    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    public test () {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        String refined = text.substring(wordL, wordR);
                        refined = refined.replaceAll(" ","");
                        System.out.println("*"+refined+"*");
                        if(SpellCheck.check(refined))
                        {
                            System.out.println("1");
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        }
                        else
                        {
                            System.out.println("2");
                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
                        }
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);

                if (SpellCheck.check(text.substring(before, after))) {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                } else {
                    setCharacterAttributes(before, after - before, attr, false);
                }
            }
        };
        JTextPane txt = new JTextPane(doc);
        txt.setText("public class Hi {}");
        add(new JScrollPane(txt));
        setVisible(true);
    }

    public static void main (String args[]) {
        new test();
    }
}
