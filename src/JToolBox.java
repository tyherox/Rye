import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    static ArrayList<toolOptions> toolChild = new ArrayList<toolOptions>();
    MouseAdapter toolListener = new extendAnimation();
    boolean entered = true;
    JLayeredPane master;
    int X,Y,gap,mButton,cButton;
    toolOptions expanded = null;

    public JToolBox(JLayeredPane m, Point point, Dimension size, JTextPane editor) throws IOException {
        setBounds(point.x, point.y, size.width, size.height);
        setBackground(Color.GRAY);
        setFocusable(true);
        setLayout(null);
        setOpaque(false);

        X=getX(); Y=getY();
        gap = (int) ((double)getWidth()/15);
        mButton = (int) ((double) getWidth()/3*2);
        cButton = (int) ((double) getWidth()/5*3);

        Dimension d = new Dimension(mButton,mButton);

        toolOptions Size = new toolOptions(0,"/Images/fontsizeN.png", "/Images/fontsizeR.png","/Images/fontsizeP.png", d);
        JButton test1 = new JButton();
        test1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {

            }
        });
        Size.addOption(test1);
        JButton test2 = new JButton();
        Size.addOption(test2);
        JButton test3 = new JButton();
        Size.addOption(test3);
        add(Size);
        toolChild.add(Size);

        toolOptions Font = new toolOptions(1,"/Images/fontstyleN.png", "/Images/fontstyleR.png","/Images/fontstyleP.png", d);
        JButton test4 = new JButton();
        Font.addOption(test4);
        JButton test5 = new JButton();
        Font.addOption(test5);
        JButton test6 = new JButton();
        Font.addOption(test6);
        JButton test7 = new JButton();
        Font.addOption(test7);
        add(Font);
        toolChild.add(Font);

        toolOptions Checker = new toolOptions(2,"/Images/checkerN.png", "/Images/checkerR.png","/Images/checkerP.png", d);
        JButton test9 = new JButton();
        Checker.addOption(test9);
        JButton test10 = new JButton();
        Checker.addOption(test10);
        JButton test11 = new JButton();
        Checker.addOption(test11);
        JButton test12 = new JButton();
        Checker.addOption(test12);
        add(Checker);
        toolChild.add(Checker);

        toolOptions Save = new toolOptions(3,"/Images/saveN.png", "/Images/saveR.png","/Images/saveP.png", d);
        JButton test13 = new JButton();
        Save.addOption(test13);
        add(Save);
        toolChild.add(Save);

        toolOptions Quit = new toolOptions(4,"/Images/exitN.png", "/Images/exitR.png","/Images/exitP.png", d);
        add(Quit);
         
        toolChild.add(Quit);
    }
    public void reSizeBox(int i) {
        setBounds(getX(),getY(),getWidth()+i,getHeight());
        revalidate();
        repaint();
    }

    public void addSub(JPanel component)
    {
        add(component);
    }

    public void removeSub(JPanel component)
    {
        remove(component);
    }

    public class toolOptions extends PicButton{

        ArrayList<JPanel> options = new ArrayList<JPanel>();
        int order = -1;
        boolean animated = false;

        public boolean getAnimate(){
            return animated;
        }

        public toolOptions(int i, String image, String inverse, String pressed, Dimension d){
            super(image, inverse, pressed, d);
            order = i;
            setBounds(gap, gap + (i * (mButton + gap)), mButton, mButton);
            addMouseListener(toolListener);
        }

        public void addOption(JButton choices) {
            JPanel option = new JPanel();
            option.setLayout(new BorderLayout());
            option.setOpaque(false);
            choices.setContentAreaFilled(false);
            choices.setOpaque(false);
            option.add(choices);
            options.add(option);
        }

        public void extendOption() {
            if(animated==false)
            {
                animated=true;
                //setSize(mButton + 250, mButton);
                reSizeBox(+options.size()*(mButton+gap));

                for(int i = 0; i<options.size();i++)
                {
                    JPanel holder = options.get(i);
                    holder.setBounds(mButton+2*gap+(mButton+gap)*i, getY(),cButton, cButton);
                    addSub(holder);
                }
            }
        }

        public void contractOption() {
            animated=false;
            expanded = null;
            reSizeBox(-options.size()*(mButton+gap));
            for(int i = 0; i<options.size();i++)
            {
                JPanel holder = options.get(i);
                removeSub(holder);
            }
        }
    }

    public static void killAnimation(){
        for(int i =0;i<toolChild.size();i++)
        {
            toolOptions test =toolChild.get(i);
            if(test.getAnimate()==true){
                test.contractOption();
            }
        }
    }

    public class extendAnimation extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            toolOptions target = (toolOptions) e.getSource();
            if(expanded==target) {
                killAnimation();
            }
            else {
                killAnimation();
                expanded = target;
                Manager.clearMenu();
                expanded.extendOption();
            }

        }
    }
}
