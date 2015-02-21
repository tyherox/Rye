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
    private JLabel wc;
    boolean entered = false;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static final double writeAreaH = screenSize.height/3*2;
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
        Dimension SIZE = new Dimension(screenSize.width/2, (int) (screenSize.height/1.5));

        //--- frame settings ---//
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, SIZE.width*2, (int) (SIZE.height*1.5));
        Color color= new Color(73, 73, 73, 255);
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
        window.setBounds((screenSize.width / 2) - (SIZE.width / 2), (screenSize.height / 2) - (SIZE.height / 2), SIZE.width, SIZE.height);
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
        int toolBoxX = (screenSize.width / 2) + SIZE.width / 2;
        int toolBoxY =  (screenSize.height / 2) - (SIZE.height / 2);
        entered = true;
        jToolBox = new JToolBox(contentPane,new Dimension(screenSize.width, screenSize.height));
        contentPane.add(jToolBox);
        //--- tools ---//

        wc = new JLabel("Word Count: ",SwingConstants.CENTER);
        wc.setForeground(Color.WHITE);
        wc.setBorder(BorderFactory.createEmptyBorder());
        wc.setBounds((window.getX()+window.getWidth()/2)-SIZE.width/10, (window.getY()+window.getHeight()+window.getHeight()/20), SIZE.width/5, 10);
        contentPane.add(wc);

        //--- Options ---//
        subMenu = new subMenu(new Dimension(screenSize.width/10,screenSize.height/12));
        subMenu.setBackground(Color.GRAY);
        contentPane.add(subMenu,JLayeredPane.MODAL_LAYER);

        //--- writing area ---//
        writeArea = new checkPane();
        writeArea.setBackground(Color.BLACK);
        writeArea.setFont(new Font("Monospaced", Font.PLAIN, 21));
        writeArea.setMargin(new Insets(50, 80, 50, 80));
        writeArea.setPreferredSize(new Dimension(SIZE.width, SIZE.height));
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
        //writeArea.setFont();
        //writeArea.setOpaque(false);
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
        scrollbar = new CustomScroll(screenSize.width/20,screenSize.height+screenSize.height/100,screenSize);
        scrollbar.setBounds(screenSize.width/50, 0, screenSize.width/20, screenSize.height+screenSize.height/100);
        Color c= new Color(88, 88, 88, 255);
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


