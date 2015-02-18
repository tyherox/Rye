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

    public void setPosition(){
        X=getX(); Y=getY();
    }

    public JToolBox(JLayeredPane m) throws IOException {
        master=m;
        setBackground(Color.GRAY);
        setFocusable(true);
        setLayout(null);
        setOpaque(false);

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
        toolOptions Size = new toolOptions(0);
        add(Size);

        toolOptions Font = new toolOptions(1);
        //add(Font);
    }

    public void reSizeBox(int i)
    {
        setBounds(getX(),getY(),getWidth()+i,getHeight());
        revalidate();
        repaint();
    }

    public class toolOptions extends JPanel{

        JPanel extension = new JPanel();
        ArrayList<JPanel> options = new ArrayList<JPanel>();
        int order = -1;

        public toolOptions(int i){
            order = i;
            extension.setBackground(Color.LIGHT_GRAY);
            extension.setVisible(false);
            master.add(extension);

            setBounds(X+15,Y+15,50,50);
            setBackground(Color.LIGHT_GRAY);
            addMouseListener(toolListener);
            setFocusable(false);
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
            System.out.println("extend");
            int py = Y + (order*60);
            setSize(300, 50);
            reSizeBox(250);
            revalidate();
            repaint();
        }

        public void contractOption() {
            System.out.println("contract");
            setSize(50, 50);
            reSizeBox(-250);
            revalidate();
            repaint();
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
