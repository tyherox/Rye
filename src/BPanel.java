import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.*;

public class BPanel extends JLayeredPane {
    String path="/Images/backgroundN_Plain.png";

    /*@Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        Graphics2D graphics = (Graphics2D) g.create();
        Image img = new ImageIcon(getClass().getResource(path)).getImage();
        System.out.println(getWidth() + ", " + getHeight());
        graphics.drawImage(img, -100, -50, 2000, 1000, null);
    }*/
}
