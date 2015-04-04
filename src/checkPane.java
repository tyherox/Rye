import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;


public class CheckPane extends JTextPane {

    private SubMenu SubMenu;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public CheckPane(JLayeredPane master) {

        Highlighter h = getHighlighter();
        h.removeAllHighlights();
        final SimpleAttributeSet red = new SimpleAttributeSet();
        StyleConstants.setForeground(red, Color.YELLOW);
        final SimpleAttributeSet black = new SimpleAttributeSet();
        StyleConstants.setForeground(black, Color.WHITE);

        DefaultStyledDocument doc = new DefaultStyledDocument() {

            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {

                super.insertString(offset, str, a);
                String text = getText(0, getLength());
                text = text.replace("\n", " ").replace("\r", " ");
                int before = findLast(text, offset);
                if (before < 0) before = 0;

                int after = findFirst(text, offset + str.length());

                int wordR = before;
                int wordL = before;
                JLabel test = new JLabel("");
                Font f = new Font("Monospaced", Font.PLAIN, 15);
                test.setFont(f);
                //System.out.println("text: " + text + " b: " + before + " a: " + after);
                while (wordR < after) {
                    /*
                    char letter = text.charAt(wordR);
                    test.setText(test.getText() + letter);
                    if(test.getFontMetrics(test.getFont()).stringWidth(test.getText())>getWidth()){
                        System.out.println("word wrap");
                        insertString(wordR, "asdfasdfasdfasdfasdf", black);
                        test.setText("");
                    }*/

                    if (text.charAt(wordR)==' ') {
                        String refined = text.substring(wordL, wordR);
                        refined = refined.replaceAll(" ","");
                        //System.out.println("at insert *"+refined+"*");
                        if(SpellCheck.check(refined))
                        {
                            setCharacterAttributes(wordL, wordR - wordL, black, false);
                        }
                        else
                        {
                            setCharacterAttributes(wordL, wordR - wordL, red, false);
                        }
                        wordL = wordR;
                    }
                    if((wordR+1==after&&after<=text.length()&&(str.length()>1))||(offset+1<text.length()&&text.charAt(offset+1)!=' '&&wordR+1==after))
                    {
                        String refined = text.substring(wordL, wordR+1);
                        refined = refined.replaceAll(" ","");
                        //System.out.println("at special *"+refined+"*");
                        if(SpellCheck.check(refined))
                        {
                            setCharacterAttributes(wordL, wordR - wordL + 1, black, false);
                        }
                        else
                        {
                            setCharacterAttributes(wordL, wordR - wordL + 1, red, false);
                        }
                        wordL = wordR;
                    }
                    else
                    {
                        setCharacterAttributes(wordL, wordR + 1, black, false);
                    }
                    wordR++;
                }
                }


            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                String text = getText(0, getLength());
                text = text.replace("\n", " ").replace("\r", " ");
                int before = findLast(text, offs);
                if (before < 0) before = 0;
                int after = findFirst(text, offs);

                String refined = text.substring(before, after);
                refined = refined.replaceAll(" ","");
                System.out.println("at remove *"+refined+"*");
                if(!refined.equals(""))
                {
                    if (SpellCheck.check(refined)) {
                        setCharacterAttributes(before, after - before, black, false);
                    } else {
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

            public void copy(MouseEvent e) {

            }
        });
        addMouseListener(new MouseAdapter() {
            boolean check = false;
            @Override
            public void mousePressed(MouseEvent e) {
                String text= getSelectedText();
                if(check==true&& text!=null){
                    int i = getCaretPosition();
                    setSelectionStart(0);
                    setSelectionEnd(0);
                    setCaretPosition(i);
                    check=false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                check = true;
                JToolBox.killAnimation();
                if (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown() == false) {
                    SubMenu.setVisible(false);
                }

                else {
                    if (e.getButton() == MouseEvent.BUTTON3 || e.isControlDown()) {
                        try {
                            String text = getWord();
                            if(text.length()>0) {
                                SubMenu.callMenu(e.getXOnScreen(), e.getYOnScreen(), text);
                                SubMenu.setVisible(true);
                            }
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    } else if (e.isControlDown()) {
                        try {
                            String text = getWord();
                            if(text.length()>0) {
                                SubMenu.callMenu(e.getXOnScreen(), e.getYOnScreen(), text);
                                SubMenu.setVisible(true);
                            }
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }

            public void filler(MouseEvent e) {

            }
        });

        addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if(getSelectedText()!=null){
                    int s = getSelectionStart();
                    int f = getSelectionEnd();
                    if(s>f) {

                    }
                }
            }
        });

        SubMenu = new SubMenu(new Dimension(screenSize.width/5,screenSize.height/8),new Dimension(screenSize.width/2, (int) (screenSize.height/1.5)));
        SubMenu.setBackground(Color.GRAY);
        master.add(SubMenu, JLayeredPane.MODAL_LAYER);
        setPreferredSize(new Dimension(screenSize.width / 2, (int) (screenSize.height / 1.5)));
        setOpaque(false);
        setCaretColor(Color.WHITE);
        getCaret().setBlinkRate(800);
        Font f = new Font("Monospaced", Font.PLAIN, 15);
        setStyle(f);
        setMargin(new Insets(50, 80, 50, 80));
        Debug.Log("initialized checkPane");

        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "copy");
        getActionMap().put("copy", new Copy());

        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "paste");
        getActionMap().put("paste", new Paste(doc, black));
    }

    private class Copy extends AbstractAction{
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = getSelectedText();
            StringSelection selection = new StringSelection(text);
            clipboard.setContents(selection,selection);
        }
    }

    private class Paste extends AbstractAction{
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        DefaultStyledDocument doc;
        SimpleAttributeSet attribute;

        Paste (DefaultStyledDocument d, SimpleAttributeSet a){
            doc = d;
            attribute = a;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                try {
                    String text = String.valueOf(clipboard.getData(DataFlavor.stringFlavor));
                    doc.insertString(getCaretPosition(),text,attribute);
                } catch (UnsupportedFlavorException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void setStyle(Font f) {
        setFont(f);
    }

    public void setSize(Font f) {
        setFont(f);
    }

    private int findLast (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches(" ")) {
                break;
            }
        }
        if(index==-1)
        {
            index=0;
        }
        return index;
    }

    private int findFirst (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches(" ")) {
                break;
            }
            index++;
        }
        return index;
    }

    public String getWord() throws BadLocationException {

        String result = "";

        Point coordinate = getMousePosition();
        int pos = this.viewToModel(coordinate);

        result = this.getText();

        int start = findLast(result,pos);
        int end = findFirst(result,pos);

        result = result.substring(start,end);
        System.out.println("*"+result+"*");

        return result;
    }
}



