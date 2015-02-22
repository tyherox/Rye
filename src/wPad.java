import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;

public class wPad extends JFrame {

    public static JLayeredPane contentPane;
    private JToolBox jToolBox;
    private subMenu subMenu;
    private static checkPane writeArea;
    private CustomScroll scrollbar;
    private JTextField title;
    private JPanel quickLabel;
    private JLabel wc;
    private JLabel pc;
    boolean entered = false;

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    static final Dimension windowSize = new Dimension(screenSize.width/2, (int) (screenSize.height/1.5));
    static final Point windowPoint = new Point(screenSize.width/4, (int) (screenSize.height/5));

    static final Dimension quickSize = new Dimension(windowSize.width/2, windowSize.height/30);
    static final Point quickPoint = new Point((windowPoint.x+windowSize.width/2)-windowSize.width/4, (windowPoint.y+windowSize.height+windowSize.height/20));

    static final Dimension toolBoxSize = new Dimension(windowSize.width/10, windowSize.height);
    static final Point toolBoxPoint = new Point(windowPoint.x+windowSize.width+windowSize.width/100,windowPoint.y);

    static final Dimension titleSize = new Dimension(windowSize.width/2, windowSize.height/15);
    static final Point titlePoint = new Point((windowPoint.x+windowSize.width/2)-windowSize.width/4, (windowPoint.y-windowSize.height/10));

    static final double writeAreaH = windowSize.height;

    int word = 0;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    wPad frame = new wPad();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     * @throws java.io.IOException
     */

    public wPad() throws IOException, URISyntaxException {
        Debug.initialize();
        Mainframe.initialize();
        //--- screen variables ---//
        Debug.Log(String.valueOf(screenSize.width));
        Debug.Log(String.valueOf(screenSize.height));

        //--- frame settings ---//
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, screenSize.width, screenSize.height);
        Color color= new Color(24, 25, 34, 255);
        setBackground(color);

        //--- main panel ---//
        contentPane = new JLayeredPane();
        contentPane.setBackground(Color.PINK);
        contentPane.setBounds(0, 0, screenSize.width, screenSize.height);
        contentPane.setLayout(null);
        contentPane.setOpaque(false);
        setContentPane(contentPane);

        //--- window panel ---//
        JScrollPane window = new JScrollPane();
        window.setBounds(windowPoint.x, windowPoint.y, windowSize.width, windowSize.height);
        window.setBackground(Color.YELLOW);
        window.setBorder(null);
        window.setHorizontalScrollBar(null);
        window.setViewportBorder(null);
        window.getViewport().setOpaque(false);
        window.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        window.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        window.setOpaque(false);
        contentPane.add(window);

        //--- tool panel ---//
        entered = true;
        jToolBox = new JToolBox(contentPane,toolBoxPoint,toolBoxSize,writeArea);
        contentPane.add(jToolBox);

        //--- tools ---//
        quickLabel = new JPanel();
        quickLabel.setBounds(quickPoint.x, quickPoint.y, quickSize.width, quickSize.height);
        quickLabel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
        quickLabel.setOpaque(false);

        wc = new JLabel("Word Count: ",SwingConstants.CENTER);
        wc.setForeground(Color.WHITE);
        wc.setBorder(BorderFactory.createEmptyBorder());
        quickLabel.add(wc);

        pc = new JLabel("Page Count: ",SwingConstants.CENTER);
        pc.setForeground(Color.WHITE);
        pc.setBorder(BorderFactory.createEmptyBorder());
        quickLabel.add(pc);

        contentPane.add(quickLabel);

        //--- Options ---//
        subMenu = new subMenu(new Dimension(screenSize.width/10,screenSize.height/12));
        subMenu.setBackground(Color.GRAY);
        contentPane.add(subMenu,JLayeredPane.MODAL_LAYER);

        //--- seperators ---//
        JPanel separatorL = new JPanel();
        separatorL.setBackground(Color.WHITE);
        separatorL.setBounds(windowPoint.x, windowPoint.y, windowSize.width / 40, windowSize.height);
        contentPane.add(separatorL,JLayeredPane.MODAL_LAYER);

        JPanel seperatorR = new JPanel();
        seperatorR.setBackground(Color.WHITE);
        seperatorR.setBounds(windowPoint.x + windowSize.width - separatorL.getWidth(), windowPoint.y, windowSize.width / 40, windowSize.height);
        contentPane.add(seperatorR,JLayeredPane.MODAL_LAYER);

        JPanel seperatorT = new JPanel();
        seperatorT.setBackground(Color.WHITE);
        seperatorT.setBounds(titlePoint.x, titlePoint.y + titlePoint.y / 3, titleSize.width, titleSize.height / 20);
        contentPane.add(seperatorT,JLayeredPane.MODAL_LAYER);

        //--- title ---///
        title = new JTextField("title");
        title.setBounds(titlePoint.x, titlePoint.y, titleSize.width, titleSize.height);
        title.setBorder(null);
        title.setOpaque(false);
        title.setForeground(Color.white);
        title.setFont(new Font("굴림", Font.PLAIN, 30));
        title.setHorizontalAlignment(JTextField.CENTER);
        contentPane.add(title);

        //--- writing area ---//
        writeArea = new checkPane();
        writeArea.setBackground(Color.BLACK);
        writeArea.setFont(new Font("Monospaced", Font.PLAIN, 21));
        writeArea.setMargin(new Insets(50, 80, 50, 80));
        writeArea.setPreferredSize(new Dimension(windowSize.width, windowSize.height));
        writeArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                //System.out.println(e.getKeyCode());

                Runnable updateWC = new Runnable() {
                    @Override
                    public void run() {
                        word = 0;
                        String refined = writeArea.getText();
                        refined = refined.replaceAll("(\r\n|\n)", " ");
                        String[] arr = refined.split(" ");
                        for (final String ss : arr) {
                            refined = ss.replaceAll("[\\d[^\\w\\s]]+", "").replaceAll("(\\s{2,})", " ").replaceAll("[^a-zA-Z0-9]", "");
                            if (!refined.equals(" ") && !refined.equals("")) {
                                word++;
                            }
                        }
                        wc.setText("Word Count: " + word);
                    }
                };
                SwingUtilities.invokeLater(updateWC);
                if (e.getKeyCode() == 27) {
                    System.exit(0);
                }
            }

            public void filler() {

            }

        });
        writeArea.setOpaque(false);
        writeArea.setCaretColor(Color.WHITE);
        writeArea.getCaret().setBlinkRate(800);
        writeArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1&&e.isControlDown()==false)
                {
                    subMenu.setVisible(false);
                }
                else
                {
                    if(e.getButton() == MouseEvent.BUTTON3||e.isControlDown())
                    {
                        subMenu.callMenu(e.getXOnScreen(),e.getYOnScreen(),"text");
                        subMenu.setVisible(true);
                    }
                    else if(e.isControlDown())
                    {
                        System.out.println("mac click");
                        subMenu.callMenu(e.getXOnScreen(),e.getYOnScreen(),"text");
                        subMenu.setVisible(true);
                    }
                }
            }
        });
        window.setViewportView(writeArea);

        //--- scrollbar implementation ---//
        scrollbar = new CustomScroll(screenSize.width/17,screenSize.height+screenSize.height/100,screenSize);
        scrollbar.setBounds(screenSize.width/50, 0, screenSize.width/17, screenSize.height+screenSize.height/100);
        JPanel decoration = new JPanel();
        decoration.setBounds(scrollbar.getWidth()/4, 0, scrollbar.getWidth()/2, screenSize.height+screenSize.height/100);
        decoration.setBackground(Color.DARK_GRAY);
        Color c= new Color(148, 138, 138, 255);
        scrollbar.add(decoration);
        scrollbar.setBackground(c);

        contentPane.add(scrollbar);

        //--- scrollbar optimization ---//
        JScrollBar sb = window.getVerticalScrollBar();
        sb.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (writeArea.getHeight() != 0) {
                    scrollbar.updateGraphic((double) e.getValue() / (double) (writeArea.getHeight()), writeAreaH / writeArea.getHeight());
                    contentPane.revalidate();
                    contentPane.repaint();
                }
            }
        });
        sb.setPreferredSize(new Dimension(0, 0));
        sb.setUnitIncrement(30);

        //// full screen mode code ////
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            setUndecorated(true);
            gd.setFullScreenWindow(this);
        } else {
            System.err.println("Full screen not supported");
            setSize(100, 100); // just something to let you see the window
            setVisible(true);
        }
        Debug.Log("initialized wPad");
    }

    public static void scroll(String c, double i)
    {
        if(c.equals("up"))
        {
            double percentage = i*writeArea.getHeight();
            Rectangle r = new Rectangle(1,(int)percentage,1,1);
            writeArea.scrollRectToVisible(r);
            contentPane.revalidate();
            contentPane.repaint();
        }
        if(c.equals("down"))
        {
            double percentage = i*writeArea.getHeight()+writeAreaH;
            Rectangle r = new Rectangle(1,(int)percentage,1,1);
            writeArea.scrollRectToVisible(r);
            contentPane.revalidate();
            contentPane.repaint();
        }
        else
        {
            double percentage = i*writeArea.getHeight();
            Rectangle r = new Rectangle(1,(int)percentage,1,1);
            writeArea.scrollRectToVisible(r);
            contentPane.revalidate();
            contentPane.repaint();
        }
    }


}


