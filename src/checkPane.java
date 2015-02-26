import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class CheckPane extends JTextPane {

    private SubMenu SubMenu;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public CheckPane(JLayeredPane master) {

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
                boolean two = false;
                if(str.equals(" ")) {
                    System.out.println("two true");
                    two = true;
                }

                while (wordR <= after) {
                    if (wordR == after||(text.charAt(wordR)==' '&&(wordR!=0&&wordR!=text.length())&&two==true)) {
                        String refined = text.substring(wordL, wordR);
                        //refined = refined.replaceAll("[\\d[^\\w\\s]]+", "").replaceAll("(\\s{2,})", " ").replaceAll("[^a-zA-Z0-9]","");
                        refined = refined.replaceAll(" ", "").replaceAll("\\n", "").replaceAll("(?!\')\\p{Punct}", "");
                        //refined = refined.replaceAll(" ", "");
                        System.out.println("at insert *"+refined+"*");
                        if(!refined.equals("")) {
                            if(SpellCheck.check(refined))
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
                    if (SpellCheck.check(refined)) {
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
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                //System.out.println(e.getKeyCode());
                if (e.getKeyCode() == 27) {
                    System.exit(0);
                }
                Runnable updateWC = new Runnable() {
                    int word = 0;
                    @Override
                    public void run() {
                        String refined = getText();
                        refined = refined.replaceAll("(\r\n|\n)", " ");
                        String[] arr = refined.split(" ");
                        for (final String ss : arr) {
                            refined = ss.replaceAll("[\\d[^\\w\\s]]+", "").replaceAll("(\\s{2,})", " ").replaceAll("[^a-zA-Z0-9]", "");
                            if (!refined.equals(" ") && !refined.equals("")) {
                                word++;
                            }
                        }
                        QuickTools.returnWc(word);
                    }
                };
                SwingUtilities.invokeLater(updateWC);
            }
            public void filler(MouseEvent e) {

            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {

                JToolBox.killAnimation();
                if (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown() == false) {
                    SubMenu.setVisible(false);
                }

                else {
                    if (e.getButton() == MouseEvent.BUTTON3 || e.isControlDown()) {
                        SubMenu.callMenu(e.getXOnScreen(), e.getYOnScreen(), "text");
                        SubMenu.setVisible(true);
                    } else if (e.isControlDown()) {
                        System.out.println("mac click");
                        SubMenu.callMenu(e.getXOnScreen(), e.getYOnScreen(), "text");
                        SubMenu.setVisible(true);
                    }
                }
            }

            public void filler(MouseEvent e) {

            }
        });

        SubMenu = new SubMenu(new Dimension(screenSize.width/10,screenSize.height/12),new Dimension(screenSize.width/2, (int) (screenSize.height/1.5)));
        SubMenu.setBackground(Color.GRAY);
        master.add(SubMenu, JLayeredPane.MODAL_LAYER);

        setPreferredSize(new Dimension(screenSize.width/2, (int) (screenSize.height/1.5)));
        setOpaque(false);
        setCaretColor(Color.WHITE);
        getCaret().setBlinkRate(800);
        Font f = new Font("Monospaced", Font.PLAIN, 21);
        setStyle(f);
        setMargin(new Insets(50, 80, 50, 80));
        Debug.Log("initialized checkPane");
    }

    public void setStyle(Font f) {
        setFont(f);
    }

    public void setSize(Font f) {
        setFont(f);
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



