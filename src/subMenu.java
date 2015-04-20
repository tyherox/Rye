import javax.swing.*;
import java.awt.*;

/**
 * Created by 의현 on 2015-02-17.
 */

public class SubMenu extends JPanel {

    Dimension parent;
    ImageButton addDicionary;
    ImageButton removeDictionary;
    ImageButton ignoreDictionary;
    ImageButton newFile;
    ImageButton removeFile;
    int w;

    public SubMenu(Dimension d,Dimension p) {
        w= (int) d.getWidth();
        parent = p;
        setBackground(Color.DARK_GRAY);
        setLayout(null);
        setFocusable(false);
        setBounds(10, 10, d.width, d.height);

        Dimension dimension = new Dimension(getWidth(),getHeight()/3);

        addDicionary = new ImageButton("/Images/sButton.png", "/Images/sButtonInverse.png","/Images/sButton.png", dimension);
        addDicionary.setContentAreaFilled(false);
        addDicionary.setFocusable(false);
        addDicionary.setBounds(0, 0, getWidth(), getHeight() / 3);
        add(addDicionary);
        addDicionary.setBorderPainted(false);

        removeDictionary = new ImageButton("/Images/sButton.png", "/Images/sButtonInverse.png","/Images/sButton.png",dimension);
        removeDictionary.setContentAreaFilled(false);
        removeDictionary.setFocusable(false);
        removeDictionary.setBounds(0, getHeight() / 3, getWidth(), getHeight() / 3);
        add(removeDictionary);
        removeDictionary.setBorderPainted(false);

        newFile = new ImageButton("/Images/sButton.png", "/Images/sButtonInverse.png","/Images/sButton.png",dimension);
        newFile.setContentAreaFilled(false);
        newFile.setFocusable(false);
        newFile.setBounds(0, getHeight() / 3 * 2, getWidth(), getHeight() / 3);
        add(newFile);
        newFile.setBorderPainted(false);

        removeFile = new ImageButton("/Images/sButton.png", "/Images/sButtonInverse.png","/Images/sButton.png",dimension);
        removeFile.setContentAreaFilled(false);
        removeFile.setFocusable(false);
        removeFile.setBounds(0, getHeight() / 3 * 2, getWidth(), getHeight() / 3);
        add(removeFile);
        removeFile.setBorderPainted(false);

        setVisible(false);
    }

    public void callCheckMenu(int x, int y, String text){

        JLabel one = new JLabel("Add \"" + text + "\" to dictionary");
        JLabel two = new JLabel("Remove \"" + text + "\" from dictionary");
        int twoWidth = two.getFontMetrics(one.getFont()).stringWidth(one.getText());

        if(twoWidth>getWidth())
        {
            String replace = text.substring(0,15)+"...";
            String add = "Add \"" + replace + "\" to dictionary";
            String remove = "Remove \"" + replace + "\" to dictionary";
            this.addDicionary.setText(add);
            this.removeDictionary.setText(remove);

        }
        else
        {
            this.addDicionary.setText(one.getText());
            this.removeDictionary.setText(two.getText());
        }
        ignoreDictionary.setText("Ignore");
        setBounds(x, y, w, getHeight());
    }

    public void callManagerMenu(int x, int y, String text){

        JLabel one = new JLabel("Add new File");
        JLabel two = new JLabel("Remove \"" + text + "\" from Project");
        int twoWidth = two.getFontMetrics(one.getFont()).stringWidth(one.getText());

        if(twoWidth>getWidth())
        {
            String replace = text.substring(0,15)+"...";
            String remove = "Remove \"" + replace + "\" to dictionary";
            this.removeDictionary.setText(remove);

        }
        else
        {
            this.removeDictionary.setText(two.getText());
        }
        ignoreDictionary.setText("Ignore");
        setBounds(x, y, w, getHeight());
    }
}
