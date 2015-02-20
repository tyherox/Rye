import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    MouseAdapter toolListener = new extendAnimation();
    boolean entered = true;
    JLayeredPane master;
    int X,Y,gap,mButton,cButton;

    public JToolBox(JLayeredPane m, Dimension d) throws IOException {

        master = m;
        int toolBoxX = (d.width / 2) + (d.width / 2) / 2;
        int toolBoxY = (int) ((d.height / 2) - (d.height / 2) / 1.5);
        setBounds(toolBoxX, toolBoxY, d.height / 20, d.height / 3);
        setBackground(Color.GRAY);
        setFocusable(true);
        setLayout(null);
        System.out.println(toolBoxX + ", " + toolBoxY);
        setOpaque(false);

        X=getX(); Y=getY();
        gap = (int) ((double)getWidth()/6);
        mButton = (int) ((double) getWidth()/3*2);
        cButton = getWidth()/6;

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
        add(Font);
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

            setBounds(gap, gap + (i * (mButton + gap)), mButton, mButton);
            setBackground(Color.LIGHT_GRAY);
            addMouseListener(toolListener);
            setFocusable(false);
        }

        public void addOption(JButton choices)
        {
            JPanel option = new JPanel();
            option.add(choices);
            options.add(option);
        }

        public void extendOption()
        {
            System.out.println("extend");
            setSize(getWidth()+250, getHeight());
            reSizeBox(250);
            revalidate();
            repaint();
        }

        public void contractOption() {
            System.out.println("contract");
            setSize(getWidth()-250, getHeight());
            reSizeBox(-250);
            revalidate();
            repaint();
        }

    }
    public class aasdf extends MouseAdapter
    {

    }

    public class extendAnimation extends MouseAdapter
    {
        @Override
        public void mouseEntered(MouseEvent e) {
            entered = true;
            final toolOptions target = (toolOptions) e.getSource();
            target.extendOption();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            System.out.println("exited");
            entered = false;
            final toolOptions target = (toolOptions) e.getSource();
            target.contractOption();
        }
    }
}
