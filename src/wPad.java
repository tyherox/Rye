import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class wPad extends JFrame {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static JLayeredPane contentPane;
	private JPanel toolBox;
	private static checkPane writeArea;
	private CustomScroll scrollbar;
    private JLabel wc;
    boolean entered = false;
    Thread toolBoxAnimation;
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
        entered = true;
        toolBox = new JPanel();
        toolBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent arg0) {
                System.out.println("entered");
                entered = true;
                toolBoxAnimation = new Thread() {
                    public void run() {
                        int timer = 0;
                        while (entered) {
                            timer++;
                            if (timer == 40) {
                                toolBox.setOpaque(true);
                                writeArea.setOpaque(true);
                                revalidate();
                                repaint();
                                entered = false;
                            }
                            System.out.println("timer:" + timer);
                            try {
                                if (entered) {
                                    System.out.println("sleeping");
                                    TimeUnit.MILLISECONDS.sleep(10);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                toolBoxAnimation.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exited");
                entered = false;
                toolBoxAnimation = new Thread() {
                    public void run() {
                        int timer = 0;
                        while (!entered) {
                            timer++;
                            if (timer == 40) {
                                toolBox.setOpaque(false);
                                writeArea.setOpaque(false);
                                revalidate();
                                repaint();
                                entered = true;
                            }
                            System.out.println("timer:" + timer);
                            try {
                                if (!entered) {
                                    System.out.println("sleeping");
                                    TimeUnit.MILLISECONDS.sleep(10);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                toolBoxAnimation.start();
            }
        });
        toolBox.setBackground(Color.BLACK);
        toolBox.setBounds((screenSize.width / 2) + SIZE.width / 2, (screenSize.height / 2) - (SIZE.height / 2), 70, 500);
        toolBox.setFocusable(true);
        toolBox.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        toolBox.setOpaque(false);
        contentPane.add(toolBox);
        //--- tools ---//
        File buttonImage = new File("src\\Images\\tbutton.png");
        BufferedImage buttonIcon;
        if(buttonImage.exists() && !buttonImage.isDirectory()) {
            buttonIcon = ImageIO.read(buttonImage);
        }
        else
        {
            InputStream in = getClass().getResourceAsStream("/Images/tbutton.png");
            buttonIcon = ImageIO.read(in);
        }
        JButton btn1 = new JButton(new ImageIcon(buttonIcon));
        btn1.setBorder(BorderFactory.createEmptyBorder());
        btn1.setContentAreaFilled(false);
        toolBox.add(btn1);

        JButton btn2 = new JButton(new ImageIcon(buttonIcon));
        btn2.setBorder(BorderFactory.createEmptyBorder());
        btn2.setContentAreaFilled(false);
        toolBox.add(btn2);

        wc = new JLabel("Word Count: ",SwingConstants.CENTER);
        wc.setForeground(Color.WHITE);
        wc.setBorder(BorderFactory.createEmptyBorder());
        wc.setBounds((window.getX()+window.getWidth()/2)-SIZE.width/10, (window.getY()+window.getHeight()+window.getHeight()/20), SIZE.width/5, 10);
        contentPane.add(wc);

        //--- writing area ---//
        writeArea = new checkPane();
        writeArea.setBackground(Color.BLACK);
        writeArea.setForeground(Color.WHITE);
        writeArea.setFont(new Font("Monospaced", Font.PLAIN, 21));
        writeArea.setMargin(new Insets(50, 80, 50, 80));
        writeArea.setPreferredSize(new Dimension(SIZE.width, SIZE.height));
        writeArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        writeArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println(e.getKeyCode());

                Runnable updateWC = new Runnable() {
                    @Override
                    public void run(){
                        word = 0;
                        String refined = writeArea.getText();
                        refined = refined.replaceAll("(\r\n|\n)", " ");
                        System.out.println(refined);
                        String[] arr = refined.split(" ");
                        for (final String ss : arr) {
                            refined = ss.replaceAll("[\\d[^\\w\\s]]+", "").replaceAll("(\\s{2,})", " ").replaceAll("[^a-zA-Z0-9]", "");
                            System.out.println(refined);
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
        });
        //writeArea.setFont();
        writeArea.setOpaque(false);
        writeArea.setCaretColor(Color.WHITE);
        writeArea.getCaret().setBlinkRate(0);
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

        public static void scroll(String c, double i,double k)
        {
            if(c.equals("up"))
            {
                System.out.println("up");
                double percentage = i*writeArea.getHeight();
                Rectangle r = new Rectangle(1,(int)percentage,1,1);
                writeArea.scrollRectToVisible(r);
                contentPane.revalidate();
                contentPane.repaint();
            }
            if(c.equals("down"))
            {
                System.out.println("down");
                double percentage = i*writeArea.getHeight()+writeAreaH;
                Rectangle r = new Rectangle(1,(int)percentage,1,1);
                writeArea.scrollRectToVisible(r);
                contentPane.revalidate();
                contentPane.repaint();
            }
            else
            {
                System.out.println("normalizing");
                double percentage = i*writeArea.getHeight();
                Rectangle r = new Rectangle(1,(int)percentage,1,1);
                writeArea.scrollRectToVisible(r);
                contentPane.revalidate();
                contentPane.repaint();
            }
        }
	}


