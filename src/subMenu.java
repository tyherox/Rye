import javax.swing.*;
import java.awt.*;

/**
 * Created by 의현 on 2015-02-17.
 */

public class SubMenu extends JPanel {

    Dimension parent;
    PicButton addDict;
    PicButton removeDict;
    PicButton ignore;
    int w;

    public SubMenu(Dimension d,Dimension p) {
        w= (int) d.getWidth();
        parent = p;
        setBackground(Color.DARK_GRAY);
        setLayout(null);
        setFocusable(false);
        setBounds(10, 10, d.width, d.height);

        Dimension dimension = new Dimension(getWidth(),getHeight()/3);

        addDict = new PicButton("/Images/sButton.png", "/Images/sButtonInverse.png","/Images/sButton.png", dimension);
        addDict.setContentAreaFilled(false);
        addDict.setFocusable(false);
        addDict.setBounds(0,0,getWidth(),getHeight()/3);
        add(addDict);
        addDict.setBorderPainted(false);

        removeDict = new PicButton("/Images/sButton.png", "/Images/sButtonInverse.png","/Images/sButton.png",dimension);
        removeDict.setContentAreaFilled(false);
        removeDict.setFocusable(false);
        removeDict.setBounds(0,getHeight()/3,getWidth(),getHeight()/3);
        add(removeDict);
        removeDict.setBorderPainted(false);

        ignore = new PicButton("/Images/sButton.png", "/Images/sButtonInverse.png","/Images/sButton.png",dimension);
        ignore.setContentAreaFilled(false);
        ignore.setFocusable(false);
        ignore.setBounds(0,getHeight()/3*2,getWidth(),getHeight()/3);
        add(ignore);
        ignore.setBorderPainted(false);
        ignore.setText("ignore");

        setVisible(false);
    }

    public void callMenu(int x, int y, String text){

        JLabel one = new JLabel("Add \"" + text + "\" to dictionary");
        JLabel two = new JLabel("Remove \"" + text + "\" from dictionary");
        int twoWidth = two.getFontMetrics(one.getFont()).stringWidth(one.getText());

        if(twoWidth>getWidth())
        {
            String replace = text.substring(0,15)+"...";
            String add = "Add \"" + replace + "\" to dictionary";
            String remove = "Remove \"" + replace + "\" to dictionary";
            addDict.setText(add);
            removeDict.setText(remove);

        }
        else
        {
            addDict.setText(one.getText());
            removeDict.setText(two.getText());
        }
        setBounds(x, y, w, getHeight());
    }
}
