import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by 의현 on 2015-02-03.
 */
public class JToolBox extends JPanel {

    Thread toolBoxAnimation;
    MouseAdapter toolListener = new extendAnimation();
    boolean entered = true;
    JLayeredPane master;
    int X;
    int Y;

    public JToolBox(JLayeredPane m,int x, int y) throws IOException {
        master = m;
        X=x; Y=y;
        //setBackground(Color.GRAY);
        setFocusable(true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setOpaque(false);

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
        toolOptions Size = new toolOptions();
        add(Size);

        toolOptions Font = new toolOptions();
        add(Font);
    }

    public class toolOptions extends JPanel{

        JPanel extension = new JPanel();
        ArrayList<JPanel> options = new ArrayList<JPanel>();
        int px;
        int py;
        public toolOptions(){
            setPreferredSize(new Dimension(50, 50));
            setBackground(Color.LIGHT_GRAY);
            addMouseListener(toolListener);
            setFocusable(false);
            px = X+10;
            py = Y+10;
        }

        public void addOption(JButton choices)
        {
            JPanel option = new JPanel();
            option.setPreferredSize(new Dimension(40, 40));
            setOpaque(false);
            option.add(choices);
        }

        public void extendOption()
        {
            int z = 0;
            int x = px;
            int y = py;
            int amount = options.size();
            System.out.println(x + ", " + y);
            extension.setBounds(x, y, 40, 40);
            extension.setBackground(Color.RED);
            extension.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    entered = true;
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    entered = false;
                }
            });
            master.add(extension);
            int h = 0;
            while(h<x+60)
            {
                z+=5;
                h = x + z;
                System.out.println(z);
                extension.setBounds(h,y,extension.getWidth(),extension.getHeight());
                revalidate();
                repaint();
                try {
                    if (entered) {
                        TimeUnit.MILLISECONDS.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for(int i = 0; i<options.size();i++)
            {

            }
        }

        public void contractOption()
        {
            System.out.println("contract");
            int z = 0;
            int x =extension.getX();
            int y = py;
            int amount = options.size();
            System.out.println(x + ", " + y);
            int h = 10000;
            while(h>x-60)
            {
                z+=5;
                h = x - z;
                System.out.println(h);
                extension.setBounds(h,y,extension.getWidth(),extension.getHeight());
                revalidate();
                repaint();
                try {
                    if (entered) {
                        TimeUnit.MILLISECONDS.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            master.remove(extension);
        }

    }

    public class extendAnimation extends MouseAdapter
    {
        @Override
        public void mouseEntered(MouseEvent e) {
            entered = true;
            final toolOptions target = (toolOptions) e.getSource();
            toolBoxAnimation = new Thread() {
                public void run() {
                    int timer = 0;
                    while (entered&&timer <= 40) {
                        timer++;
                        if (timer == 41) {
                            target.extendOption();
                        }
                        try {
                            if (entered) {
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
            final toolOptions target = (toolOptions) e.getSource();
            toolBoxAnimation = new Thread() {
                public void run() {
                    int timer = 0;
                    while (entered==false&&timer <= 80) {
                        timer++;
                        if (timer == 81) {
                            target.contractOption();
                        }
                        try {
                            if (entered==false) {
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
    }
}
