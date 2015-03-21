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
                Element root = getDefaultRootElement();
                String text = getText(0, getLength());
                int before = findLast(text, offset);
                if (before < 0) before = 0;

                int after = findFirst(text, offset + str.length());

                int wordR = before;
                int wordL = before;

                text = text.replace("\n", " ").replace("\r", " ");

                if(!str.equals("\n"))
                {
                    String sub = text.substring(before, after);

                    while (wordR < after) {
                        /*
                        System.out.println(root.getElementIndex(wordR));
                        if((root.getElementIndex(wordR)+1)%6 == 0&&(root.getElementIndex(wordR))!=0&&pageBreak==false) {
                            System.out.println("new line " + root.getElementIndex(wordR));
                            insertString(wordR-1,"\n",black);
                            pageBreak=true;
                        }*/

                        //System.out.println("wordR: " + wordR);
                        if (text.charAt(wordR)==' ') {
                            String refined = text.substring(wordL, wordR);
                            refined = refined.replaceAll(" ","");
                            System.out.println("at insert *"+refined+"*");
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
                            System.out.println("at special *"+refined+"*");
                            if(SpellCheck.check(refined))
                            {
                                setCharacterAttributes(wordL, wordR - wordL+1, black, false);
                            }
                            else
                            {
                                setCharacterAttributes(wordL, wordR - wordL+1, red, false);
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
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                String text = getText(0, getLength());
                int before = findLast(text, offs);
                if (before < 0) before = 0;
                int after = findFirst(text, offs);

                Element root = getDefaultRootElement();
                boolean pageBreak = false;

                String refined = text.substring(before, after);
               // refined = refined.replaceAll(" ", "").replaceAll("(?!\')\\p{Punct}", "");
                refined = refined.replaceAll(" ","");
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

        /*doc.addUndoableEditListener(new UndoableEditListener(){

            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                System.out.println(e.getEdit().toString());
            }
        });*/

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
                        try {
                            SubMenu.callMenu(e.getXOnScreen(), e.getYOnScreen(), getWord());
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                        SubMenu.setVisible(true);
                    } else if (e.isControlDown()) {
                        System.out.println("mac click");
                        try {
                            SubMenu.callMenu(e.getXOnScreen(), e.getYOnScreen(), getWord());
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                        SubMenu.setVisible(true);
                    }
                }
            }

            public void filler(MouseEvent e) {

            }
        });

        /*
        SimpleAttributeSet aSet = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(aSet, 1);
        doc.setParagraphAttributes(0, doc.getLength(), aSet, false);*/

        SubMenu = new SubMenu(new Dimension(screenSize.width/10,screenSize.height/12),new Dimension(screenSize.width/2, (int) (screenSize.height/1.5)));
        SubMenu.setBackground(Color.GRAY);
        master.add(SubMenu, JLayeredPane.MODAL_LAYER);

        setPreferredSize(new Dimension(screenSize.width / 2, (int) (screenSize.height / 1.5)));
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
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
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



    /*
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(204, 192, 194, 128));

        FontMetrics fm = g2.getFontMetrics();
        int textHeight = fm.getHeight();
        int offset = 80-textHeight*2;

        for (int i = textHeight; i < getHeight(); i += (10 * textHeight)) {
            //g2.drawLine(getWidth()/4, i + offset, getWidth()*3/4, i + offset);
        }

        g2.dispose();
    }*/

}



