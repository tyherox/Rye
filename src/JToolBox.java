import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by 의현 on 2015-02-03.
 */
public class JToolBox extends JPanel {

    Thread toolBoxAnimation;
    MouseAdapter toolListener = new extendAnimation();
    boolean entered = true;

    public JToolBox() throws IOException {

        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
        //setOpaque(false);

        //addMouseListener(toolListener);

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
        JPanel btn1 = new JPanel();
        btn1.setBorder(BorderFactory.createEmptyBorder());
        //btn1.setContentAreaFilled(false);
        btn1.setPreferredSize(new Dimension(50, 50));
        btn1.setBackground(Color.RED);
        btn1.addMouseListener(toolListener);
        btn1.setFocusable(false);
        add(btn1);

        JPanel btn2 = new JPanel();
        btn2.setBorder(BorderFactory.createEmptyBorder());
        //btn2.setContentAreaFilled(false);
        btn2.setPreferredSize(new Dimension(50,50));
        btn2.setBackground(Color.RED);
        btn2.addMouseListener(toolListener);
        btn2.setFocusable(false);
        add(btn2);

    }

    public class extendAnimation extends MouseAdapter
    {
        @Override
        public void mouseEntered(MouseEvent e) {
            System.out.println("entered");
            entered = true;
            final JPanel target = (JPanel) e.getSource();
            toolBoxAnimation = new Thread() {
                public void run() {
                    final int i = target.getWidth();
                    int timer = 0;
                    while (entered&&target.getWidth()<i+50) {
                        timer++;
                        if (timer >= 40) {
                            target.setSize(target.getWidth()+10,target.getHeight());
                            revalidate();
                            repaint();

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
            /*
            System.out.println("exited");
            entered = false;
            final JButton target = (JButton) e.getSource();
            toolBoxAnimation = new Thread() {
                public void run() {
                    int timer = 0;
                    while (!entered) {
                        timer++;
                        if (timer == 40) {
                            target.setOpaque(false);
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
            toolBoxAnimation.start();*/
        }
    }
}
