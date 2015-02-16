import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


public class checkTest extends JFrame {

    private JTextPane textPane;
    private JLabel timeCount;
    private  ArrayList<String> words = new ArrayList<String>();
    private String holder = "";

    /**
     * Launch the application.
     */

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    System.out.println("Hi");
                    checkTest frame = new checkTest();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */

    public checkTest() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 69, 584, 392);
        contentPane.add(scrollPane);


        final SimpleAttributeSet red = new SimpleAttributeSet();
        StyleConstants.setForeground(red, Color.RED);
        final SimpleAttributeSet black = new SimpleAttributeSet();
        StyleConstants.setForeground(black, Color.BLACK);

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
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        String refined = text.substring(wordL, wordR);
                        refined = refined.replaceAll("[\\d[^\\w\\s]]+", "").replaceAll("(\\s{2,})", " ").replaceAll("[^a-zA-Z0-9]","");
                        refined = refined.replaceAll(" ","");
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
                refined = refined.replaceAll("[\\d[^\\w\\s]]+", "").replaceAll("(\\s{2,})", " ").replaceAll("[^a-zA-Z0-9]","");
                System.out.println("at remove *"+refined+"*");
                if(!refined.equals(""))
                {
                    if (Mainframe.check(refined)) {
                        System.out.println("right");
                        setCharacterAttributes(before, after - before, black, false);
                    } else {
                        System.out.println("wrong: *" + refined + "*");
                        setCharacterAttributes(before, after - before, red, false);
                    }
                }
                else
                {
                    setCharacterAttributes(before, after - before, black, false);
                }
            }
        };

        textPane = new JTextPane(doc);
        scrollPane.setViewportView(textPane);

        JLabel wordCount = new JLabel("Total Words: ");
        wordCount.setBounds(6, 36, 212, 16);
        contentPane.add(wordCount);

        timeCount = new JLabel("Time:");
        timeCount.setBounds(360, 36, 212, 16);
        contentPane.add(timeCount);

    }

    private int findLast (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
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
}


