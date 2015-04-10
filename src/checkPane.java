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
import java.awt.image.BufferedImage;
import java.io.IOException;


public class CheckPane extends JTextPane {

    private SubMenu SubMenu;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    boolean hidden;

    public CheckPane(JLayeredPane master) {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Point hotSpot = new Point(0,0);
        BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
        final Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");
        final Cursor originalCursor = Cursor.getDefaultCursor();

        Color select = new Color(128, 32, 40, 185);
        setSelectionColor(select);
        final SimpleAttributeSet wrong = new SimpleAttributeSet();
        StyleConstants.setForeground(wrong, Color.YELLOW);
        final SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setForeground(right, Color.WHITE);
        Font f = new Font("Monospaced", Font.PLAIN, 14);

        DefaultStyledDocument doc = new DefaultStyledDocument() {
            Highlighter h = getHighlighter();
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);
                String text = getText(0, getLength());
                text = text.replace("\n", " ").replace("\r", " ").replace("\t"," ");
                int before = findLast(text, offset);
                if (before < 0) before = 0;

                int after = findFirst(text, offset + str.length());

                int wordR = before;
                int wordL = before;
                JLabel test = new JLabel("");
                //System.out.println("text: " + text + " b: " + before + " a: " + after);
                while (wordR < after) {
                    if (text.charAt(wordR)==' ') {
                        String refined = text.substring(wordL, wordR);
                        refined = refined.replaceAll(" ","");
                        //System.out.println("at insert *"+refined+"*");
                        if(SpellCheck.check(refined))
                        {
                            setCharacterAttributes(wordL, wordR - wordL, right, false);
                        }
                        else
                        {
                            setCharacterAttributes(wordL, wordR - wordL, wrong, false);
                            //h.addHighlight(wordL,wordL+(wordR-wordL),DefaultHighlighter.DefaultPainter);
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
                            setCharacterAttributes(wordL, wordR - wordL + 1, right, false);
                        }
                        else
                        {
                            setCharacterAttributes(wordL, wordR - wordL + 1, wrong, false);
                        }
                        wordL = wordR;
                    }
                    else
                    {
                        setCharacterAttributes(wordL, wordR - wordL + 1, right, false);
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
                //System.out.println("at remove *"+refined+"*");
                if(!refined.equals(""))
                {
                    if (SpellCheck.check(refined)) {
                        setCharacterAttributes(before, after - before, right, false);
                    } else {
                        setCharacterAttributes(before, after - before, wrong, false);
                    }
                }
                else
                {
                    setCharacterAttributes(before, after - before, right, false);
                }
            }
        };
        setDocument(doc);
        SimpleAttributeSet attribs = new SimpleAttributeSet();
        //StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_JUSTIFIED);
        setParagraphAttributes(attribs,false);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                //System.out.println(e.getKeyCode());
                if (e.getKeyCode() == 27) {
                    //WPad.minimize();
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

            boolean entered = false;
            int time = 0;
            @Override
            public void mouseEntered(MouseEvent e) {
                entered = true;
                time = 0;
                Runnable distraction = new Runnable() {
                    int word = 0;
                    @Override
                    public void run() {
                        while(entered==true&&time<200)
                        {
                            try {
                                Thread.sleep(10);
                                time++;
                                if(time==200)
                                {
                                    WPad.distractionHide();
                                    setCursor(invisibleCursor);
                                    hidden=true;
                                }
                            } catch(InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                };
                new Thread(distraction).start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                entered=false;
                WPad.distractionShow();
                hidden=false;
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            boolean start = false;
            int mousetimer = 0;
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                setCursor(originalCursor);
                if (hidden == true) {
                    if (start == false) {
                        Runnable hidescroll = new Runnable() {
                            @Override
                            public void run() {
                                start = true;
                                mousetimer = 0;
                                while (mousetimer <= 20) {
                                    try {
                                        Thread.sleep(100);
                                        mousetimer++;
                                        if (mousetimer >= 20) {
                                            if (hidden == false) {
                                                start = false;
                                                break;
                                            }
                                            setCursor(invisibleCursor);
                                            start = false;
                                            break;
                                        }
                                    } catch (InterruptedException ex) {
                                        Thread.currentThread().interrupt();
                                    }
                                }
                                start = false;
                            }

                            public void filler() {

                            }
                        };
                        new Thread(hidescroll).start();
                    } else {
                        restartTimer();
                    }
                }
            }
            public void restartTimer() {
                mousetimer=0;
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

            public void filler(){

            }
        });

        SubMenu = new SubMenu(new Dimension(screenSize.width/5,screenSize.height/8),new Dimension(screenSize.width/2, (int) (screenSize.height/1.5)));
        SubMenu.setBackground(Color.GRAY);
        master.add(SubMenu, JLayeredPane.MODAL_LAYER);
        setPreferredSize(new Dimension(screenSize.width / 2, (int) (screenSize.height / 1.5)));
        setOpaque(false);
        setCaretColor(Color.WHITE);
        getCaret().setBlinkRate(800);
        setStyle(f);
        FontMetrics test = getFontMetrics(getFont());
        setMargin(new Insets(test.getHeight(), 40, (int) (test.getHeight()/2.2), 40));
        Debug.Log("initialized checkPane");

        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "copy");
        getActionMap().put("copy", new Copy());

        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "paste");
        getActionMap().put("paste", new Paste(doc, right));
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
            return index;
        }
        else
        {
            return ++index;
        }
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

        int start = findLast(result, pos);
        int end = findFirst(result, pos);

        result = result.substring(start,end);
        System.out.println("*"+result+"*");
        result = result.replace("\n", "").replace("\r", "").replace("\t","");
        return result;
    }
}



