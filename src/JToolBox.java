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
    ArrayList<toolOptions> toolChild = new ArrayList<toolOptions>();
    MouseAdapter toolListener = new extendAnimation();
    MouseAdapter areaChecker = new extendedChecker();
    boolean entered = true;
    JLayeredPane master;
    int X,Y,gap,mButton,cButton;
    toolOptions expanded;
    Rectangle expandedArea;

    public JToolBox(JLayeredPane m, Point point, Dimension size, JTextPane editor) throws IOException {
        setBounds(point.x, point.y, size.width, size.height);
        setBackground(Color.GRAY);
        setFocusable(true);
        setLayout(null);
        setOpaque(false);

        X=getX(); Y=getY();
        gap = (int) ((double)getWidth()/6);
        mButton = (int) ((double) getWidth()/3*2);
        cButton = (int) ((double) getWidth()/5*3);

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
        JButton test1 = new JButton("1");
        Size.addOption(test1);
        JButton test2 = new JButton("2");
        Size.addOption(test2);
        JButton test3 = new JButton("3");
        Size.addOption(test3);
        add(Size);
        Size.optimizeArea();
        toolChild.add(Size);

        toolOptions Font = new toolOptions(1);
        JButton test4 = new JButton("4");
        Font.addOption(test4);
        JButton test5 = new JButton("5");
        Font.addOption(test5);
        JButton test6 = new JButton("6");
        Font.addOption(test6);
        JButton test7 = new JButton("7");
        Font.addOption(test7);
        JButton test8 = new JButton("8");
        Font.addOption(test8);
        add(Font);
        Font.optimizeArea();
        toolChild.add(Font);

        toolOptions Checker = new toolOptions(2);
        JButton test9 = new JButton("9");
        Checker.addOption(test9);
        JButton test10 = new JButton("10");
        Checker.addOption(test10);
        JButton test11 = new JButton("11");
        Checker.addOption(test11);
        JButton test12 = new JButton("12");
        Checker.addOption(test12);
        add(Checker);
        Checker.optimizeArea();
        toolChild.add(Checker);
    }

    public void reSizeBox(int i)
    {
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
    public int masterSize() {
        int width = getWidth();
        return width;
    }

    public class toolOptions extends JPanel{

        ArrayList<JPanel> options = new ArrayList<JPanel>();
        int order = -1;
        JPanel checker;

        public toolOptions(int i){
            order = i;
            setBounds(gap, gap + (i * (mButton + gap)), mButton, mButton);
            setBackground(Color.LIGHT_GRAY);
            addMouseListener(toolListener);
            setFocusable(false);
        }

        public void optimizeArea()
        {
            Point coord = new Point(getX()-gap,getY()-gap);
            Rectangle area = new Rectangle(coord,new Dimension(masterSize()+options.size()*(mButton+gap),cButton+gap*2));
            checker = new JPanel();
            checker.setOpaque(false);
            checker.setBounds(area);
            addSub(checker);
        }

        public void addOption(JButton choices)
        {
            JPanel option = new JPanel();
            option.setLayout(new BorderLayout());
            option.setBackground(Color.RED);
            //option.add(choices);
            options.add(option);
        }

        public void extendOption()
        {
            System.out.println("extend");
            //setSize(mButton + 250, mButton);
            reSizeBox(+options.size()*(mButton+gap));

            for(int i = 0; i<options.size();i++)
            {
                System.out.println("add");
                JPanel holder = options.get(i);
                holder.setBounds(mButton+2*gap+(mButton+gap)*i, getY(),cButton, cButton);
                addSub(holder);
            }
            checker.addMouseListener(areaChecker);
        }

        public void contractOption() {
            System.out.println("contract");
            checker.removeMouseListener(areaChecker);
            reSizeBox(-options.size()*(mButton+gap));
            for(int i = 0; i<options.size();i++)
            {
                System.out.println("remove");
                JPanel holder = options.get(i);
                removeSub(holder);
            }


        }

    }

    public class extendAnimation extends MouseAdapter
    {
        @Override
        public void mouseReleased(MouseEvent e) {
            toolOptions target = (toolOptions) e.getSource();
            expanded = target;
            expanded.extendOption();
        }
    }

    public class extendedChecker extends MouseAdapter
    {
        @Override
        public void mouseExited(MouseEvent e) {
            expanded.contractOption();
        }
    }
}
