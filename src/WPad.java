import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Timer;

public class WPad extends JFrame {

    private static JFrame ex;
    private static ImagePanel contentPane;
    private static Manager fileExtension;
    private static JToolBox jToolBox;
    private static CheckPane writeArea;
    private static CustomScroll scrollbar;
    private static JTextField title;
    private static QuickTools quickLabel;
    boolean entered = false;
    static boolean hidden = false;
    Timer timer;

    static JPanel seperatorT = new JPanel();
    static JPanel seperatorR = new JPanel();
    static JPanel seperatorL = new JPanel();

    static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    static final Dimension windowSize = new Dimension(screenSize.width/2, (int) (screenSize.height/1.5));
    static final Point windowPoint = new Point(screenSize.width/4, (int) (screenSize.height/5));

    static final Dimension quickSize = new Dimension(windowSize.width/2, windowSize.height/30);
    static final Point quickPoint = new Point((windowPoint.x+windowSize.width/2)-windowSize.width/4, (windowPoint.y+windowSize.height+windowSize.height/19));

    static final Dimension toolBoxSize = new Dimension(windowSize.width/15, windowSize.height);
    static final Point toolBoxPoint = new Point(windowPoint.x+windowSize.width+windowSize.width/100,windowPoint.y);

    static final Dimension titleSize = new Dimension(windowSize.width/2, windowSize.height/15);
    static final Point titlePoint = new Point((windowPoint.x+windowSize.width/2)-windowSize.width/4, (windowPoint.y-windowSize.height/10));

    static final Dimension fmSize = new Dimension(screenSize.width/60, screenSize.height);
    static final Point fmPoint = new Point(screenSize.width-fmSize.width, 0);

    static final double writeAreaH = windowSize.height;

    /**
     * Launch the application.
     */

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Database.intialize();
                    System.out.println("starting");
                    WPad frame = new WPad();
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

    public WPad() throws IOException, URISyntaxException {
        /*String laf = "javax.swing.plaf.metal.MetalLookAndFeel";
        try {
            UIManager.setLookAndFeel(laf);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }*/
        Debug.initialize();
        SpellCheck.initialize();
        //--- screen variables ---//
        Debug.Log(String.valueOf("screen size: " + screenSize.width + ", " + screenSize.height));

        //--- frame settings ---//
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, screenSize.width, screenSize.height);
        Color color= new Color(12, 12, 34, 255);
        setBackground(color);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                jToolBox.killAnimation();
            }
        });

        //--- main panel ---//
        contentPane = new ImagePanel("/Images/1.png");
        contentPane.setBackground(Color.RED);
        contentPane.setBounds(0, 0, screenSize.width, screenSize.height);
        contentPane.setLayout(null);
        //contentPane.setOpaque(true);
        setContentPane(contentPane);

        //--- window panel ---//
        JScrollPane window = new JScrollPane();
        window.setBounds(windowPoint.x, windowPoint.y, windowSize.width, windowSize.height);
        window.setBorder(null);
        window.setHorizontalScrollBar(null);
        window.setViewportBorder(null);
        window.getViewport().setOpaque(false);
        window.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        window.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        window.setOpaque(false);
        window.addMouseWheelListener(new MouseWheelListener() {
            boolean start = false;
            int scrolltimer = 0;
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scrollbar.setVisible(true);
                if (hidden == true) {
                    if (start == false) {
                        Runnable hidescroll = new Runnable() {
                            @Override
                            public void run() {
                                start = true;
                                scrolltimer = 0;
                                while (scrolltimer <= 20) {
                                    try {
                                        Thread.sleep(100);
                                        scrolltimer++;
                                        if (scrolltimer >= 20) {
                                            if (hidden == false) {
                                                start = false;
                                                break;
                                            }
                                            scrollbar.setVisible(false);
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
                scrolltimer=0;
            }
        });
        contentPane.add(window);

        //--- tool panel ---//
        entered = true;
        jToolBox = new JToolBox(contentPane,toolBoxPoint,toolBoxSize,writeArea);
        contentPane.add(jToolBox);

        //--- QuickTools ---//
        quickLabel = new QuickTools();
        quickLabel.setBounds(quickPoint.x, quickPoint.y, quickSize.width, quickSize.height);
        contentPane.add(quickLabel);

        //--- title ---///
        title = new JTextField("Title");
        title.setDragEnabled(false);
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {

                JToolBox.killAnimation();
            }

            public void filler(MouseEvent e) {

            }
        });
        title.setBounds(titlePoint.x, titlePoint.y, titleSize.width, titleSize.height);
        title.setBorder(null);
        title.setOpaque(false);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("굴림", Font.PLAIN, 30));
        title.setHorizontalAlignment(JTextField.CENTER);
        title.setCaretColor(Color.WHITE);
        contentPane.add(title);

        //--- writing area ---//
        writeArea = new CheckPane(contentPane);
        window.setViewportView(writeArea);
        writeArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                //ex.setVisible(false);
                //gd.setFullScreenWindow(ex);
               // ex.setVisible(true);
            }
        });

        //--- scrollbar optimization ---//
        final JScrollBar sb = window.getVerticalScrollBar();
        sb.addAdjustmentListener(new AdjustmentListener() {
            boolean start = false;
            int scrolltimer = 0;
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (writeArea.getHeight() != 0) {
                    scrollbar.updateGraphic((double) e.getValue() / (double) (writeArea.getHeight()), writeAreaH / writeArea.getHeight());
                }
            }
        });
        sb.setPreferredSize(new Dimension(0, 0));
        FontMetrics test = writeArea.getFontMetrics(writeArea.getFont());
        sb.setUnitIncrement(test.getHeight());

        decoratePad();

        //--- scrollbar implementation ---//

        scrollbar = new CustomScroll(screenSize.width/20,screenSize.height+screenSize.height/100,screenSize);
        scrollbar.setBounds(screenSize.width / 50, 0, screenSize.width / 20, screenSize.height + screenSize.height / 100);
        Color c= new Color(148, 138, 138, 255);
        scrollbar.setBackground(c);
        ImagePanel decoration = new ImagePanel("/Images/track.png");
        decoration.setBounds(scrollbar.getWidth()/4,0,scrollbar.getWidth()/2,scrollbar.getHeight());
        Color r= new Color(103, 100, 100, 255);
        decoration.setBackground(r);
        scrollbar.add(decoration);
        contentPane.add(scrollbar);

        // --- exit and minimize ---//

        Dimension d =  new Dimension(screenSize.width/50,screenSize.width/50);
        ImageButton exit = new ImageButton("/Images/exitM.png", "/Images/exitM.png","/Images/exitM.png",d);
        exit.setBounds(0,0,screenSize.width/50,screenSize.width/50);
        exit.setBorder(BorderFactory.createEmptyBorder());
        exit.setContentAreaFilled(false);
        add(exit);

        ImageButton minimize = new ImageButton("/Images/minimizeM.png", "/Images/minimizeM.png","/Images/minimizeM.png",d);
        minimize.setBounds(0,screenSize.width/50,screenSize.width/50,screenSize.width/50);
        minimize.setBorder(BorderFactory.createEmptyBorder());
        minimize.setContentAreaFilled(false);
        add(minimize);

        ///--- File Management ---///

        fileExtension = new Manager();
        fileExtension.setBounds(fmPoint.x, fmPoint.y, fmSize.width, fmSize.height);
        fileExtension.initializeMenu();
        contentPane.add(fileExtension);

        //// full screen mode code ////

        if (gd.isFullScreenSupported()) {
            setUndecorated(true);
            gd.setFullScreenWindow(this);
        } else {
            System.err.println("Full screen not supported");
            setSize(screenSize.width, screenSize.height);
            setVisible(true);
        }
        ex = this;
        Debug.Log("initialized wPad");
    }

    public void decoratePad(){
        //--- seperators ---//
        Color c = new Color(87, 85, 92);

        seperatorL.setBackground(c);
        seperatorL.setBounds(windowPoint.x, windowPoint.y, windowSize.width / 100, windowSize.height);
        contentPane.add(seperatorL,JLayeredPane.MODAL_LAYER);

        seperatorR.setBackground(c);
        seperatorR.setBounds(windowPoint.x + windowSize.width - seperatorL.getWidth(), windowPoint.y, windowSize.width / 100, windowSize.height);
        contentPane.add(seperatorR,JLayeredPane.MODAL_LAYER);

        seperatorT.setBackground(c);
        seperatorT.setBounds(titlePoint.x, titlePoint.y + titlePoint.y / 3, titleSize.width, titleSize.height / 20);
        contentPane.add(seperatorT,JLayeredPane.MODAL_LAYER);
    }

    public static void distractionShow() {
        hidden = false;
        fileExtension.setVisible(true);
        jToolBox.setVisible(true);
        title.setVisible(true);
        seperatorT.setVisible(true);
        scrollbar.setVisible(true);
    }

    public static void distractionHide() {
        hidden = true;
        fileExtension.setVisible(false);
        jToolBox.setVisible(false);
        title.setVisible(false);
        seperatorT.setVisible(false);
        scrollbar.setVisible(false);
    }

    public void testMethod() {

    }

    public static void minimize() {
        //gd.setFullScreenWindow(null);
        //ex.setState(Frame.ICONIFIED);
    }

    public static void scroll(String c, double i)
    {
        if(c.equals("up"))
        {
            double percentage = i*writeArea.getHeight();
            Rectangle r = new Rectangle(1,(int)percentage,1,1);
            writeArea.scrollRectToVisible(r);
        }
        if(c.equals("down"))
        {
            double percentage = i*writeArea.getHeight()+writeAreaH;
            Rectangle r = new Rectangle(1,(int)percentage,1,1);
            writeArea.scrollRectToVisible(r);
        }
        else
        {
            double percentage = i*writeArea.getHeight();
            Rectangle r = new Rectangle(1,(int)percentage,1,1);
            writeArea.scrollRectToVisible(r);
        }
    }

}


