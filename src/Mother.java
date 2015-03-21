import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;

public class Mother extends JFrame {

    public static JLayeredPane contentPane;
    private static Manager fileExtension;
    private JToolBox jToolBox;
    private static CheckPane writeArea;
    private CustomScroll scrollbar;
    private JTextField title;
    private QuickTools quickLabel;
    boolean entered = false;

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    static final Dimension windowSize = new Dimension(screenSize.width/2, (int) (screenSize.height/1.5));
    static final Point windowPoint = new Point(screenSize.width/4, (int) (screenSize.height/5));

    static final Dimension quickSize = new Dimension(windowSize.width/2, windowSize.height/30);
    static final Point quickPoint = new Point((windowPoint.x+windowSize.width/2)-windowSize.width/4, (windowPoint.y+windowSize.height+windowSize.height/20));

    static final Dimension toolBoxSize = new Dimension(windowSize.width/15, windowSize.height);
    static final Point toolBoxPoint = new Point(windowPoint.x+windowSize.width+windowSize.width/100,windowPoint.y);

    static final Dimension titleSize = new Dimension(windowSize.width/2, windowSize.height/15);
    static final Point titlePoint = new Point((windowPoint.x+windowSize.width/2)-windowSize.width/4, (windowPoint.y-windowSize.height/10));

    static final Dimension fmSize = new Dimension(screenSize.width/60, screenSize.height);
    static final Point fmPoint = new Point(screenSize.width-fmSize.width, 0);

    static final double writeAreaH = windowSize.height;

    int word = 0;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Mother frame = new Mother();
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

    public Mother() throws IOException, URISyntaxException {
        Debug.initialize();
        SpellCheck.initialize();
        //--- screen variables ---//
        Debug.Log(String.valueOf(screenSize.width));
        Debug.Log(String.valueOf(screenSize.height));

        //--- frame settings ---//
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, screenSize.width, screenSize.height);
        Color color= new Color(24, 25, 34, 255);
        setBackground(color);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                jToolBox.killAnimation();
            }
        });

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

        //--- QuickTools ---//
        quickLabel = new QuickTools();
        quickLabel.setBounds(quickPoint.x, quickPoint.y, quickSize.width, quickSize.height);
        contentPane.add(quickLabel);

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
        writeArea = new CheckPane(contentPane);
        window.setViewportView(writeArea);

        //--- scrollbar implementation ---//
        scrollbar = new CustomScroll(screenSize.width/20,screenSize.height+screenSize.height/100,screenSize);
        scrollbar.setBounds(screenSize.width/50, 0, screenSize.width/20, screenSize.height+screenSize.height/100);
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

        decoratePad();

        ///--- File Management ---///

        fileExtension = new Manager();
        fileExtension.setBounds(fmPoint.x, fmPoint.y, screenSize.width-fmPoint.x, screenSize.height-fmPoint.y);
        fileExtension.setLayout(null);
        contentPane.add(fileExtension);

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

    public void decoratePad(){
        //--- seperators ---//
        Color c = new Color(87, 85, 92);
        JPanel separatorL = new JPanel();
        separatorL.setBackground(c);
        separatorL.setBounds(windowPoint.x, windowPoint.y, windowSize.width / 60, windowSize.height);
        contentPane.add(separatorL,JLayeredPane.MODAL_LAYER);

        JPanel seperatorR = new JPanel();
        seperatorR.setBackground(c);
        seperatorR.setBounds(windowPoint.x + windowSize.width - separatorL.getWidth(), windowPoint.y, windowSize.width / 60, windowSize.height);
        contentPane.add(seperatorR,JLayeredPane.MODAL_LAYER);

        JPanel seperatorT = new JPanel();
        seperatorT.setBackground(c);
        seperatorT.setBounds(titlePoint.x, titlePoint.y + titlePoint.y / 3, titleSize.width, titleSize.height / 20);
        contentPane.add(seperatorT,JLayeredPane.MODAL_LAYER);
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


